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
        ps.setInt    (5, resourceRoleId);
        ps.executeUpdate();
    }
}

}
