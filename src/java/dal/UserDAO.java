package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import model.Users;
import model.Role;
import java.util.ArrayList;
import java.util.List;
import model.Departmentt;
import model.Department;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;

public class UserDAO extends DBContext {

    public Users getUserById(int userId) {
        String sql = "SELECT u.*, r.Name AS Role_name, dhu.Department_id, d.Name AS Department_name "
                + "FROM Users u "
                + "LEFT JOIN Role r ON u.Role_id = r.Role_id "
                + "LEFT JOIN Department_has_User dhu ON u.User_id = dhu.User_id "
                + "LEFT JOIN Department d ON dhu.Department_id = d.Department_id "
                + "WHERE u.User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users(
                            rs.getInt("User_id"),
                            rs.getInt("Role_id"),
                            rs.getString("Full_name"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getInt("Gender") == 1,
                            rs.getString("Phone_number"),
                            rs.getString("Address"),
                            rs.getDate("Date_of_birth"),
                            rs.getString("Image"),
                            rs.getTimestamp("Created_at"),
                            rs.getTimestamp("Updated_at"),
                            rs.getInt("Status") == 1,
                            rs.getString("Reset_Password_Token"),
                            rs.getTimestamp("Reset_Password_Expiry")
                    );
                    user.setRoleName(rs.getString("Role_name") != null ? rs.getString("Role_name") : "");
                    user.setDepartmentId(rs.getInt("Department_id"));
                    user.setDepartmentName(rs.getString("Department_name") != null ? rs.getString("Department_name") : "");
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createUser(Users user, int departmentId) {
        String sql = "INSERT INTO Users (Role_id, Full_name, Email, Password, Gender, Phone_number, Address, Date_of_birth, Image, Created_at, Updated_at, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            stmt.setInt(1, user.getRoleId());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.isGender() ? 1 : 0);
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAddress());
            stmt.setDate(8, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setString(9, user.getImage());
            stmt.setTimestamp(10, currentTimestamp);
            stmt.setTimestamp(11, currentTimestamp);
            stmt.setInt(12, user.isStatus() ? 1 : 0);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                    if (departmentId > 0) {
                        assignUserToDepartment(user.getUserId(), departmentId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(Users user, int departmentId) {
        if (departmentId > 0) {
            String checkSql = "SELECT COUNT(*) FROM Department WHERE Department_id = ? AND Role_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, departmentId);
                checkStmt.setInt(2, user.getRoleId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new IllegalArgumentException("Department does not belong to the selected role");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error validating department ID " + departmentId + " for role ID " + user.getRoleId() + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Invalid department for role");
            }
        }
        String sql = "UPDATE Users SET Full_name=?, Password=?, Gender=?, Phone_number=?, Address=?, Date_of_birth=?, Image=?, Updated_at=?, Status=?, Role_id=? "
                + "WHERE User_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.isGender() ? 1 : 0);
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setDate(6, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setString(7, user.getImage());
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(9, user.isStatus() ? 1 : 0);
            stmt.setInt(10, user.getRoleId());
            stmt.setInt(11, user.getUserId());
            stmt.executeUpdate();
            removeUserFromAllDepartments(user.getUserId());
            if (departmentId > 0) {
                assignUserToDepartment(user.getUserId(), departmentId);
            }
        } catch (SQLException e) {
            System.err.println("Error updating user with ID " + user.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        PreparedStatement stmt = null;
        try {
            connection.setAutoCommit(false);
            String sqlDepartment = "DELETE FROM Department_has_User WHERE User_id = ?";
            stmt = connection.prepareStatement(sqlDepartment);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            stmt.close();
            String sqlUser = "DELETE FROM Users WHERE User_id = ?";
            stmt = connection.prepareStatement(sqlUser);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
                ex.printStackTrace();
            }
            System.err.println("Error deleting user with ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT Role_id, Name, Description FROM Role";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Role(
                        rs.getInt("Role_id"),
                        rs.getString("Name"),
                        rs.getString("Description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching roles: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Departmentt> getAllDepartments() {
        List<Departmentt> list = new ArrayList<>();
        String sql = "SELECT Department_id, Name, Description, Role_id FROM Department";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Departmentt(
                        rs.getInt("Department_id"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("Role_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching departments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Departmentt> getDepartmentsByRoleId(int roleId) {
        List<Departmentt> departments = new ArrayList<>();
        String sql = "SELECT Department_id, Name, Description FROM Department WHERE Role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    departments.add(new Departmentt(
                            rs.getInt("Department_id"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            roleId
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching departments for role ID " + roleId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return departments;
    }

    public Integer getUserDepartmentId(int userId) {
        String sql = "SELECT Department_id FROM Department_has_User WHERE User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Department_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching department ID for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Users> getUsers(int page, int pageSize, String searchQuery, Integer departmentId, Integer roleId, Boolean status, String sortOrder) {
        List<Users> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT u.*, r.Name AS Role_name, dhu.Department_id, d.Name AS Department_name "
                + "FROM Users u "
                + "LEFT JOIN Role r ON u.Role_id = r.Role_id "
                + "LEFT JOIN Department_has_User dhu ON u.User_id = dhu.User_id "
                + "LEFT JOIN Department d ON dhu.Department_id = d.Department_id "
                + "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (u.Full_name LIKE ? OR u.Email LIKE ? OR u.Phone_number LIKE ? OR u.Address LIKE ?)");
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 0; i < 4; i++) {
                params.add(searchPattern);
            }
        }
        if (departmentId != null) {
            sql.append(" AND dhu.Department_id = ?");
            params.add(departmentId);
        }
        if (roleId != null) {
            sql.append(" AND u.Role_id = ?");
            params.add(roleId);
        }
        if (status != null) {
            sql.append(" AND u.Status = ?");
            params.add(status ? 1 : 0);
        }
        sql.append(" ORDER BY u.Updated_at ")
                .append(sortOrder != null && sortOrder.equalsIgnoreCase("asc") ? "ASC" : "DESC")
                .append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            System.out.println("Executing query: " + sql.toString());
            System.out.println("Parameters: " + params.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users(
                            rs.getInt("User_id"),
                            rs.getInt("Role_id"),
                            rs.getString("Full_name"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getInt("Gender") == 1,
                            rs.getString("Phone_number"),
                            rs.getString("Address"),
                            rs.getDate("Date_of_birth"),
                            rs.getString("Image"),
                            rs.getTimestamp("Created_at"),
                            rs.getTimestamp("Updated_at"),
                            rs.getInt("Status") == 1,
                            rs.getString("Reset_Password_Token"),
                            rs.getTimestamp("Reset_Password_Expiry")
                    );
                    user.setRoleName(rs.getString("Role_name") != null ? rs.getString("Role_name") : "");
                    user.setDepartmentId(rs.getInt("Department_id"));
                    user.setDepartmentName(rs.getString("Department_name") != null ? rs.getString("Department_name") : "");
                    users.add(user);
                }
                System.out.println("Retrieved " + users.size() + " users.");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public int getTotalUsers(String searchQuery, Integer departmentId, Integer roleId, Boolean status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT u.User_id) FROM Users u");
        sql.append(" LEFT JOIN Department_has_User dhu ON u.User_id = dhu.User_id");
        sql.append(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (u.Full_name LIKE ? OR u.Email LIKE ? OR u.Phone_number LIKE ? OR u.Address LIKE ?)");
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 0; i < 4; i++) {
                params.add(searchPattern);
            }
        }
        if (departmentId != null) {
            sql.append(" AND dhu.Department_id = ?");
            params.add(departmentId);
        }
        if (roleId != null) {
            sql.append(" AND u.Role_id = ?");
            params.add(roleId);
        }
        if (status != null) {
            sql.append(" AND u.Status = ?");
            params.add(status ? 1 : 0);
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            System.out.println("Executing count query: " + sql.toString());
            System.out.println("Parameters: " + params.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean emailExists(String email, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ? AND User_id != ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, excludeUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence for " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String getUserDepartment(int userId) {
        String sql = "SELECT d.Name "
                + "FROM Department d "
                + "JOIN Department_has_User dhu ON d.Department_id = dhu.Department_id "
                + "WHERE dhu.User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name") != null ? rs.getString("Name") : "";
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching department for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    private void assignUserToDepartment(int userId, int departmentId) {
        String sql = "INSERT INTO Department_has_User (Department_id, User_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error assigning user ID " + userId + " to department ID " + departmentId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeUserFromAllDepartments(int userId) {
        String sql = "DELETE FROM Department_has_User WHERE User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing user ID " + userId + " from all departments: " + e.getMessage());
            e.printStackTrace();
        }
    }
     //Update department,image in userlist
    public void updateUserDepartment(int userId, int departmentId) {
    try {
        removeUserFromAllDepartments(userId);
        if (departmentId > 0) {
            assignUserToDepartment(userId, departmentId);
        }
    } catch (Exception e) {
        System.err.println("Error updating department for user ID " + userId + ": " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Failed to update department");
    }
}
   
    public void updateUserImage(int userId, String imageUrl) {
        String sql = "UPDATE Users SET Image = ? WHERE User_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imageUrl);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No user found with ID " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error updating image for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update user image");
        }
    }

    /**
     *
     * @author duong
     */
    Connection conn = null;

    /**
     *
     * @author duong
     */
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Users checkLogin(String email, String plainPasswordInput) {
        try {
            String query = "SELECT * FROM users WHERE email = ? AND status = 1";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(plainPasswordInput, hashedPassword)) {
                    return new Users(
                            rs.getInt("user_id"),
                            rs.getInt("role_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("gender"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getDate("date_of_birth"),
                            rs.getString("image"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at"),
                            1 == rs.getInt("status"),
                            rs.getString("reset_password_token"),
                            rs.getTimestamp("reset_password_expiry")
                    );
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkUserByEmail(String email) {
        try {
            String query = "select * from users where email = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace(); //a
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            String query = "UPDATE Users SET Password = ? WHERE User_id = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace(); //a
            return false;
        }

    }

    public void insertResetToken(String email) {
        String token = UUID.randomUUID().toString();
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000); // 30 phút
        try {
            String query = "UPDATE Users SET Reset_Password_Token = ?, Reset_Password_Expiry = ? WHERE Email = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ps.setTimestamp(2, expiry);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updatePasswordbyEmail(String email, String newPassword) {
        try {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            String query = "UPDATE Users SET Password = ? WHERE Email = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace(); //a
            return false;
        }

    }

    public Users getUserByEmail(String email) {
        try {
            String query = "SELECT * FROM users WHERE LOWER(TRIM(email)) = ? AND status = 1";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("date_of_birth"),
                        rs.getString("image"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        (1 == rs.getInt("status")),
                        rs.getString("reset_password_token"),
                        rs.getTimestamp("reset_password_expiry")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Users getUserByID(int id) {
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("date_of_birth"),
                        rs.getString("image"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("reset_password_token"),
                        rs.getTimestamp("reset_password_expiry")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveResetToken(int userId, String token, LocalDateTime expiryTime) {
        String sql = "UPDATE users SET reset_password_token = ?, reset_password_expiry = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(expiryTime));
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Users getUserByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_password_token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("date_of_birth"),
                        rs.getString("image"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("reset_password_token"),
                        rs.getTimestamp("reset_password_expiry")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearResetToken(int userId) {
        String sql = "UPDATE users SET reset_password_token = NULL, reset_password_expiry = NULL WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lưu yêu cầu reset vào ResetRequest
    public boolean saveResetRequest(int userId, String email, String token, LocalDateTime expiry) {
        String sql = "INSERT INTO ResetRequest (User_id, Email, Token, Expiry_time, Status, Request_time) "
                + "VALUES (?, ?, ?, ?, 'Pending', NOW())";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, email);
            ps.setString(3, token);
            ps.setTimestamp(4, Timestamp.valueOf(expiry));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateResetToken(int userId, String token, LocalDateTime expiryTime) {
        try {
            String sql = "UPDATE Users SET Reset_Password_Token = ?, Reset_Password_Expiry = ?, Updated_at = NOW() WHERE user_id = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(expiryTime));
            ps.setInt(3, userId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Users> getAllUsersWithResetRequest() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.*, r.Name AS Role_name, dhu.Department_id, d.Name AS Department_name "
                + "FROM Users u "
                + "LEFT JOIN Role r ON u.Role_id = r.Role_id "
                + "LEFT JOIN Department_has_User dhu ON u.User_id = dhu.User_id "
                + "LEFT JOIN Department d ON dhu.Department_id = d.Department_id "
                + "WHERE u.Reset_Password_Token IS NOT NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("User_id"),
                        rs.getInt("Role_id"),
                        rs.getString("Full_name"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getInt("Gender") == 1,
                        rs.getString("Phone_number"),
                        rs.getString("Address"),
                        rs.getDate("Date_of_birth"),
                        rs.getString("Image"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at"),
                        rs.getInt("Status") == 1,
                        rs.getString("Reset_Password_Token"),
                        rs.getTimestamp("Reset_Password_Expiry")
                );
                user.setRoleName(rs.getString("Role_name"));
                user.setDepartmentId(rs.getInt("Department_id"));
                user.setDepartmentName(rs.getString("Department_name"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
