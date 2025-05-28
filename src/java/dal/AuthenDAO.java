package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Permission;

public class AuthenDAO {

    private Connection conn;

    public AuthenDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    /**
     * Trả về danh sách Permission từ database.
     */
    public List<Permission> getPermission(int userId) throws SQLException {
        List<Permission> list = new ArrayList<>();
        String sql
                = "SELECT "
                + "  rr.Resource_role_id AS id, "
                + "  res.Name           AS resourceName, "
                + "  rr.Can_add         AS canCreate, "
                + "  rr.Can_view        AS canRead, "
                + "  rr.Can_update      AS canUpdate, "
                + "  rr.Can_delete      AS canDelete  "
                + "FROM Resource_role rr "
                + "INNER JOIN Resource res ON rr.Resource_id = res.Resource_id "
                + "INNER JOIN Users u      ON rr.Role_id     = u.Role_id "
                + "WHERE u.User_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Permission p = new Permission();
                    p.setId(rs.getInt("id"));
                    p.setResourceName(rs.getString("resourceName"));
                    p.setCanCreate(rs.getBoolean("canCreate"));
                    p.setCanRead(rs.getBoolean("canRead"));
                    p.setCanUpdate(rs.getBoolean("canUpdate"));
                    p.setCanDelete(rs.getBoolean("canDelete"));
                    list.add(p);
                }
            }
        }
        return list;
    }

    /**
     * Trả về chuỗi JSON array theo format JS `features`: [ {"name":"...",
     * "catalogue":true, "read":false, "write":true, "edit":false}, ... ]
     */
    public String getPermissionJson(int userId) throws SQLException {
        List<Permission> list = getPermission(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            Permission p = list.get(i);
            sb.append("{")
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
}
