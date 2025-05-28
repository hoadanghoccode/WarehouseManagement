package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.PermPayload;
import model.Permission;
import model.Resource;
import model.Role;
import model.RolePayload;
import model.RolePermission;

public class AuthenDAO {

    private Connection conn;

    public AuthenDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    /**
     * Trả về danh sách Permission từ database.
     */
    public List<Permission> getPermission() throws SQLException {
        List<Permission> list = new ArrayList<>();
        String sql
                = "SELECT "
                + "  rr.Resource_role_id AS id, "
                + "  res.Resource_id     AS resourceId, "
                + "  res.Name            AS resourceName, "
                + "  rr.Can_add          AS canCreate, "
                + "  rr.Can_view         AS canRead, "
                + "  rr.Can_update       AS canUpdate, "
                + "  rr.Can_delete       AS canDelete "
                + "FROM Resource_role rr "
                + "INNER JOIN Resource res ON rr.Resource_id = res.Resource_id "
                + "INNER JOIN Users u      ON rr.Role_id = u.Role_id";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getInt("id"));
                p.setResourceId(rs.getInt("resourceId")); // ⬅ Thêm dòng này
                p.setResourceName(rs.getString("resourceName"));
                p.setCanCreate(rs.getBoolean("canCreate"));
                p.setCanRead(rs.getBoolean("canRead"));
                p.setCanUpdate(rs.getBoolean("canUpdate"));
                p.setCanDelete(rs.getBoolean("canDelete"));
                list.add(p);
            }
        }

        return list;
    }

    /**
     * Trả về chuỗi JSON array theo format JS `features`: [ {"name":"...",
     * "catalogue":true, "read":false, "write":true, "edit":false}, ... ]
     */
    public String getPermissionJson() throws SQLException {
        List<Permission> list = getPermission();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            Permission p = list.get(i);
            sb.append("{")
                    .append("\"resourceId\":").append(p.getResourceId()).append(",")
                    .append("\"name\":\"").append(p.getResourceName()).append("\",")
                    .append("\"catalogue\":").append(p.isCanCreate()).append(",")
                    .append("\"read\":").append(p.isCanRead()).append(",")
                    .append("\"write\":").append(p.isCanUpdate()).append(",")
                    .append("\"edit\":").append(p.isCanDelete())
                    .append("}");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int addResource(String name) throws SQLException {
        String sql = "INSERT INTO Resource(Name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Không lấy được resource_id");
                }
            }
        }
    }

    public void updatePermission(int resourceRoleId,
            boolean canCreate,
            boolean canRead,
            boolean canUpdate,
            boolean canDelete) throws SQLException {
        String sql = "UPDATE Resource_role SET "
                + "Can_add = ?, Can_view = ?, Can_update = ?, Can_delete = ? "
                + "WHERE Resource_role_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, canCreate);
            ps.setBoolean(2, canRead);
            ps.setBoolean(3, canUpdate);
            ps.setBoolean(4, canDelete);
            ps.setInt(5, resourceRoleId);
            ps.executeUpdate();
        }
    }

    public void addPermission(int resourceId, int roleId,
            boolean canCreate, boolean canRead,
            boolean canUpdate, boolean canDelete) throws SQLException {
        String sql = "INSERT INTO Resource_role "
                + "(Resource_id, Role_id, Can_add, Can_view, Can_update, Can_delete, Created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resourceId);
            ps.setInt(2, roleId);
            ps.setBoolean(3, canCreate);
            ps.setBoolean(4, canRead);
            ps.setBoolean(5, canUpdate);
            ps.setBoolean(6, canDelete);
            ps.executeUpdate();
        }
    }

    public int getRoleIdByUser(int userId) throws SQLException {
        String sql = "SELECT Role_id FROM Users WHERE User_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Role_id");
                } else {
                    throw new SQLException("User không tồn tại");
                }
            }
        }

    }

    // --- phương thức bạn đã có sẵn cho bảng `resource` ---
    public int insertAndGetId(String name, String description) throws SQLException {
        String sql = """
            INSERT INTO resource(name, description, created_at, updated_at)
            VALUES (?, ?, NOW(), NOW())
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Không lấy được resource_id");
                }
            }
        }
    }

    // --- bạn thêm vào phần implement cho Permission (resource_role) ---
    public int insertAndGetId(Permission p) throws SQLException {
        String sql = """
            INSERT INTO resource_role(
                resource_id, role_id,
                can_add, can_view, Can_update, can_delete,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getResourceId());
            ps.setInt(2, p.getRoleId());
            ps.setBoolean(3, p.isCanCreate());  // tương ứng với can_add
            ps.setBoolean(4, p.isCanRead());       // can_view
            ps.setBoolean(5, p.isCanUpdate());      // Can_update
            ps.setBoolean(6, p.isCanDelete());       // can_delete

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Không lấy được resource_role_id");
                }
            }
        }
    }

    public boolean deleteResourceById(int resourceId) throws SQLException {
        String deleteResourceRoleSql = "DELETE FROM Resource_role WHERE Resource_id = ?";
        String deleteResourceSql = "DELETE FROM Resource WHERE Resource_id = ?";

        try (
                PreparedStatement stmt1 = conn.prepareStatement(deleteResourceRoleSql); PreparedStatement stmt2 = conn.prepareStatement(deleteResourceSql)) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa các dòng con trong Resource_role trước
            stmt1.setInt(1, resourceId);
            stmt1.executeUpdate();

            // Xóa dòng trong Resource
            stmt2.setInt(1, resourceId);
            int affected = stmt2.executeUpdate();

            conn.commit(); // Commit transaction

            return affected > 0;

        } catch (SQLException e) {
            conn.rollback(); // Rollback nếu có lỗi
            e.printStackTrace();
            return false;
        } finally {
            conn.setAutoCommit(true); // Trả lại chế độ auto commit
        }
    }

    /**
     * Lấy tất cả resources
     */
    public ArrayList<Resource> getAllResources() {
        ArrayList<Resource> list = new ArrayList<>();
        String sql = "SELECT Resource_id, Name, Description, Created_at, Updated_at FROM Resource";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Resource r = new Resource();
                r.setresourceId(rs.getInt("Resource_id"));
                r.setName(rs.getString("Name"));
                r.setDescription(rs.getString("Description"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lọc in-memory theo tên (case-insensitive)
     */
    public ArrayList<Resource> searchByName(ArrayList<Resource> src, String keyword) {
        ArrayList<Resource> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Resource r : src) {
            if (r.getName().toLowerCase().contains(kw)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Lấy sub-list từ start (inclusive) đến end (exclusive)
     */
    public ArrayList<Resource> getListByPage(ArrayList<Resource> src, int start, int end) {
        return new ArrayList<>(src.subList(start, end));
    }

    /**
     * Xóa resource theo ID
     */
    public boolean deleteResource(int resourceId) {
        String deleteChildSql = "DELETE FROM Resource_role WHERE Resource_id = ?";
        String deleteParentSql = "DELETE FROM Resource WHERE Resource_id = ?";
        boolean result = false;
        try {
            // Bắt đầu transaction
            conn.setAutoCommit(false);

            // Xóa bản ghi bảng con
            try (PreparedStatement psChild = conn.prepareStatement(deleteChildSql)) {
                psChild.setInt(1, resourceId);
                psChild.executeUpdate();
            }

            // Xóa bản ghi bảng cha
            try (PreparedStatement psParent = conn.prepareStatement(deleteParentSql)) {
                psParent.setInt(1, resourceId);
                int affected = psParent.executeUpdate();
                result = (affected > 0);
            }

            // Commit nếu thành công
            conn.commit();
        } catch (SQLException e) {
            // Rollback khi lỗi xảy ra
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Trả lại auto-commit
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean existsResource(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Resource WHERE Name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean addResource(String name, String description) throws SQLException {
        String sql = "INSERT INTO Resource(Name, Description, Created_at) VALUES(?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        }
    }

    public List<RolePermission> getAllRolePermissions() throws SQLException {
        String sql
                = "SELECT \n"
                + "  ro.Role_id,\n"
                + "  ro.Name         AS Role_name,\n"
                + "  r.Resource_id,\n"
                + "  r.Name          AS Resource_name,\n"
                + "  COALESCE(rr.Can_add,    0) AS Can_add,\n"
                + "  COALESCE(rr.Can_view,   0) AS Can_view,\n"
                + "  COALESCE(rr.Can_update, 0) AS Can_update,\n"
                + "  COALESCE(rr.Can_delete, 0) AS Can_delete\n"
                + "FROM Role AS ro\n"
                + "CROSS JOIN Resource AS r\n"
                + "LEFT JOIN Resource_role AS rr\n"
                + "  ON rr.Role_id     = ro.Role_id\n"
                + " AND rr.Resource_id = r.Resource_id\n"
                + "ORDER BY \n"
                + "  ro.Name,\n"
                + "  r.Name;";

        List<RolePermission> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(rs.getInt("Role_id"));
                rp.setRoleName(rs.getString("Role_name"));
                rp.setResourceId(rs.getInt("Resource_id"));
                rp.setResourceName(rs.getString("Resource_name"));
                rp.setCanAdd(rs.getBoolean("Can_add"));
                rp.setCanView(rs.getBoolean("Can_view"));
                rp.setCanUpdate(rs.getBoolean("Can_update"));
                rp.setCanDelete(rs.getBoolean("Can_delete"));
                list.add(rp);
            }
        }
        return list;
    }

    public void upsertRolePermissions(List<RolePayload> roles) throws SQLException {
        // 1) Chuẩn bị 3 statement: select, insert, update
        String selectSql = """
        SELECT 1
        FROM Resource_role 
        WHERE Role_id = ? AND Resource_id = ?
    """;
        String insertSql = """
        INSERT INTO Resource_role
          (Role_id, Resource_id, Can_add, Can_view, Can_update, Can_delete, Created_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW())
    """;
        String updateSql = """
        UPDATE Resource_role
        SET Can_add    = ?,
            Can_view   = ?,
            Can_update = ?,
            Can_delete = ?,
            Updated_at = NOW()
        WHERE Role_id = ? AND Resource_id = ?
    """;

        try (
                PreparedStatement psSelect = conn.prepareStatement(selectSql); PreparedStatement psInsert = conn.prepareStatement(insertSql); PreparedStatement psUpdate = conn.prepareStatement(updateSql);) {
            for (RolePayload role : roles) {
                int rid = role.getRoleId();
                for (PermPayload p : role.getPermissions()) {
                    int resid = p.getResourceId();
                    boolean add = p.isCanAdd();
                    boolean view = p.isCanView();
                    boolean upd = p.isCanUpdate();
                    boolean del = p.isCanDelete();

                    // 2) Kiểm tra tồn tại
                    psSelect.setInt(1, rid);
                    psSelect.setInt(2, resid);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            // 3a) Nếu đã có → UPDATE
                            psUpdate.setBoolean(1, add);
                            psUpdate.setBoolean(2, view);
                            psUpdate.setBoolean(3, upd);
                            psUpdate.setBoolean(4, del);
                            psUpdate.setInt(5, rid);
                            psUpdate.setInt(6, resid);
                            psUpdate.executeUpdate();
                        } else {
                            // 3b) Nếu chưa có → INSERT
                            psInsert.setInt(1, rid);
                            psInsert.setInt(2, resid);
                            psInsert.setBoolean(3, add);
                            psInsert.setBoolean(4, view);
                            psInsert.setBoolean(5, upd);
                            psInsert.setBoolean(6, del);
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void deleteRole(int roleId) throws SQLException {
        // Tắt auto-commit để dùng transaction
        boolean oldAuto = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try (
                // 1) Xoá user–group liên quan
                PreparedStatement ps1 = conn.prepareStatement(
                        "DELETE FROM Group_has_User "
                        + "WHERE Group_id IN (SELECT Group_id FROM `Group` WHERE Role_id = ?)"); // 2) Xoá nhóm
                 PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE Group SET Role_id = NULL WHERE Role_id = ?"); // 3) Xóa quyền khỏi department
                 PreparedStatement ps3 = conn.prepareStatement(
                        "UPDATE Users SET Role_id = NULL WHERE Role_id = ?"); // 4) Xoá quyền resource
                 PreparedStatement ps4 = conn.prepareStatement(
                        "DELETE FROM Resource_role WHERE Role_id = ?"); // 5) Xoá chính role
                 PreparedStatement ps5 = conn.prepareStatement(
                        "DELETE FROM `Role` WHERE Role_id = ?");) {
            // Thiết lập tham số và thực thi từng câu
            ps1.setInt(1, roleId);
            ps1.executeUpdate();

            ps2.setInt(1, roleId);
            ps2.executeUpdate();

            ps3.setInt(1, roleId);
            ps3.executeUpdate();

            ps4.setInt(1, roleId);
            ps4.executeUpdate();

            ps5.setInt(1, roleId);
            ps5.executeUpdate();

            // Nếu không có lỗi, commit
            conn.commit();
        } catch (SQLException ex) {
            // Rollback nếu có lỗi
            conn.rollback();
            throw ex;
        } finally {
            // Trả lại auto-commit như ban đầu
            conn.setAutoCommit(oldAuto);
        }
    }
    
    public void insertRole(Role role) throws SQLException {
        String sql = "INSERT INTO Role(Name, Description) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Tạo Role thất bại, không có dòng nào bị thêm.");
            }

            // Lấy ID vừa sinh
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    role.setRoleId(rs.getInt(1));
                }
            }
        }
    }
}
