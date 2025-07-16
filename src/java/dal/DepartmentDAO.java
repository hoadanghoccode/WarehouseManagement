package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DeptRoleResource;
import model.Department;
import model.DepartmentDetail;
import model.ResourcePerm;

public class DepartmentDAO extends DBContext {

    private Connection connection;

    public DepartmentDAO() {
        try {
            // Lấy connection từ lớp DBContext
            connection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DeptRoleResource> getAllDepartmentsWithRoles() throws SQLException {
        List<DeptRoleResource> result = new ArrayList<>();

        String sql = "SELECT \n"
                + "    d.department_id, \n"
                + "    d.name AS department_name,\n"
                + "    d.description,\n"
                + "    r.role_id, \n"
                + "    r.name AS role_name,\n"
                + "    rr.resource_id, \n"
                + "    res.name AS resource_name,\n"
                + "    rr.can_add, \n"
                + "    rr.can_view, \n"
                + "    rr.can_update, \n"
                + "    rr.can_delete,\n"
                + "    IFNULL(u.user_count, 0) AS user_count\n"
                + "FROM \n"
                + "    Department d\n"
                + "LEFT JOIN \n"
                + "    Role r ON d.role_id = r.role_id\n"
                + "LEFT JOIN \n"
                + "    Resource_role rr ON r.role_id = rr.role_id\n"
                + "LEFT JOIN \n"
                + "    Resource res ON rr.resource_id = res.resource_id\n"
                + "LEFT JOIN (\n"
                + "    SELECT dhu.department_id, COUNT(*) AS user_count\n"
                + "    FROM Department_has_User dhu\n"
                + "    GROUP BY dhu.department_id\n"
                + ") u ON d.department_id = u.department_id";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer departmentId = rs.getInt("department_id");
                    String departmentName = rs.getString("department_name");
                    String description = rs.getString("description");

                    Integer roleId = rs.getObject("role_id") != null
                            ? rs.getInt("role_id")
                            : null;
                    String roleName = rs.getString("role_name");

                    Integer resourceId = rs.getObject("resource_id") != null
                            ? rs.getInt("resource_id")
                            : null;
                    String resourceName = rs.getString("resource_name");

                    Boolean canAdd = rs.getObject("can_add") != null
                            ? rs.getBoolean("can_add")
                            : null;
                    Boolean canView = rs.getObject("can_view") != null
                            ? rs.getBoolean("can_view")
                            : null;
                    Boolean canUpdate = rs.getObject("can_update") != null
                            ? rs.getBoolean("can_update")
                            : null;
                    Boolean canDelete = rs.getObject("can_delete") != null
                            ? rs.getBoolean("can_delete")
                            : null;

                    Integer userCount = rs.getObject("user_count") != null
                            ? rs.getInt("user_count")
                            : 0;

                    DeptRoleResource row = new DeptRoleResource(
                            departmentId,
                            description,      // Đúng thứ tự: description trước
                            departmentName,   // Đúng thứ tự: departmentName sau
                            roleId,
                            roleName,
                            resourceId,
                            resourceName,
                            canAdd,
                            canView,
                            canUpdate,
                            canDelete,
                            userCount // Thêm dòng này
                    );
                    result.add(row);
                }
            }
        }
        return result;
    }

    public boolean insertDepartment(Department dept) throws SQLException {
        String sql = "INSERT INTO Department (Name, Role_id, Description) VALUES (?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dept.getName());
            ps.setInt(2, dept.getRole_id());
            ps.setString(3, dept.getDescription());
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean deleteDepartment(int departmentId) throws SQLException {
        // Giả sử bạn đã khai báo FK ON DELETE CASCADE trên Department_has_User,
        // khi xóa Department thì MySQL sẽ tự động xóa các bản ghi ở Department_has_User.
        String sql = "DELETE FROM Department WHERE Department_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            int affected = ps.executeUpdate();
            return affected > 0; // > 0 nghĩa là có ít nhất 1 dòng bị xóa
        }
    }

    public DepartmentDetail getDepartmentDetail(int departmentId) throws SQLException {
        String sql = "SELECT "
                + "    d.department_id, "
                + "    d.name AS department_name, "
                + "    d.description, "
                + "    r.role_id, "
                + "    r.name AS role_name, "
                + "    rr.resource_id, "
                + "    res.name AS resource_name, "
                + "    rr.can_add, "
                + "    rr.can_view, "
                + "    rr.can_update, "
                + "    rr.can_delete "
                + "FROM "
                + "    Department d "
                + "LEFT JOIN "
                + "    Role r ON d.role_id = r.role_id "
                + "LEFT JOIN "
                + "    Resource_role rr ON r.role_id = rr.role_id "
                + "LEFT JOIN "
                + "    Resource res ON rr.resource_id = res.resource_id "
                + "WHERE d.department_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                DepartmentDetail detail = null;
                while (rs.next()) {
                    if (detail == null) {
                        // Lần đầu tiên, khởi tạo đối tượng với thông tin Department + Role
                        int deptId = rs.getInt("department_id");
                        String deptName = rs.getString("department_name");
                        String desc = rs.getString("description");
                        int roleId = rs.getInt("role_id");
                        String roleName = rs.getString("role_name");
                        detail = new DepartmentDetail(deptId, deptName, desc, roleId, roleName);
                    }
                    // Lấy thông tin Resource + 4 quyền
                    int resId = rs.getInt("resource_id");
                    if (resId > 0) { // Nếu Resource tồn tại (LEFT JOIN có thể trả resource_id=null)
                        String resName = rs.getString("resource_name");
                        boolean canAdd = rs.getBoolean("can_add");
                        boolean canView = rs.getBoolean("can_view");
                        boolean canUpdate = rs.getBoolean("can_update");
                        boolean canDelete = rs.getBoolean("can_delete");
                        ResourcePerm rp = new ResourcePerm(resId, resName, canAdd, canView, canUpdate, canDelete);
                        detail.getPermissions().add(rp);
                    }
                }
                return detail; // Nếu không tìm thấy 1 dòng nào, detail vẫn null → trả về null
            }
        }
    }

    public Department getDepartmentById(int departmentId) throws SQLException {
        String sql = "SELECT d.Department_id, d.Name AS DeptName, d.Role_id, r.Name AS RoleName "
                + "FROM Department d "
                + "LEFT JOIN Role r ON d.Role_id = r.Role_id "
                + "WHERE d.Department_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Department dept = new Department();
                    dept.setDepartment_id(rs.getInt("Department_id"));
                    dept.setName(rs.getString("DeptName"));
                    dept.setRole_id(rs.getInt("Role_id"));
                    dept.setName(rs.getString("RoleName"));
                    return dept;
                }
            }
        }
        return null;
    }

    public Department getDepartmentByRoleId(int roleId) throws SQLException {
        String sql = "SELECT Department_id, Name, Description, Role_id FROM Department WHERE Role_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Department d = new Department();
                    d.setDepartment_id(rs.getInt("Department_id"));
                    d.setName(rs.getString("Name"));
                    d.setDescription(rs.getString("Description"));
                    d.setRole_id(rs.getInt("Role_id"));
                    return d;
                }
            }
        }
        return null;
    }

    public boolean updateDepartment(Department dept) throws SQLException {
        // Lấy Department hiện tại trong DB
        Department current = getDepartmentById(dept.getDepartment_id());
        if (current == null) {
            // Department không tồn tại
            return false;
        }

        // Nếu roleId có thay đổi
        if (current.getRole_id() != dept.getRole_id()) {
            Department other = getDepartmentByRoleId(dept.getRole_id());
            if (other != null && other.getDepartment_id() != dept.getDepartment_id()) {
                // Role này đã gán cho 1 Department khác → không được phép cập nhật
                return false;
            }
        }

        String sql = "UPDATE Department SET Name = ?, Description = ?, Role_id = ? WHERE Department_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dept.getName());
            ps.setString(2, dept.getDescription());
            ps.setInt(3, dept.getRole_id());
            ps.setInt(4, dept.getDepartment_id());
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean isDepartmentNameExists(String departmentName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM department WHERE Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
