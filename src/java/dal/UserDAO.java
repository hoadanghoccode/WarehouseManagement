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
import model.Branch;
import model.Group;

import java.util.UUID;

import model.Branch;
import model.Group;


public class UserDAO extends DBContext {

    public Users getUserById(int userId) {
        String sql = "SELECT u.*, r.Name AS Role_name, b.Name AS Branch_name, " +
                     "GROUP_CONCAT(g.Name SEPARATOR ', ') AS Group_names " +
                     "FROM Users u " +
                     "LEFT JOIN Role r ON u.Role_id = r.Role_id " +
                     "LEFT JOIN Branch b ON u.Branch_id = b.Branch_id " +
                     "LEFT JOIN Group_has_User gu ON u.User_id = gu.User_id " +
                     "LEFT JOIN `Group` g ON gu.Group_id = g.Group_id " +
                     "WHERE u.User_id = ? " +
                     "GROUP BY u.User_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users(
                        rs.getInt("User_id"),
                        rs.getInt("Role_id"),
                        rs.getInt("Branch_id"),
                        rs.getString("Full_name"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getInt("Gender"),
                        rs.getString("Phone_number"),
                        rs.getString("Address"),
                        rs.getDate("Date_of_birth"),
                        rs.getString("Image"),
                        rs.getDate("Created_at"),
                        rs.getDate("Updated_at"),
                        rs.getBoolean("Status"),
                        rs.getString("Reset_Password_Token"),
                        rs.getTimestamp("Reset_Password_Expiry")
                    );
                    user.setRoleName(rs.getString("Role_name") != null ? rs.getString("Role_name") : "");
                    user.setBranchName(rs.getString("Branch_name") != null ? rs.getString("Branch_name") : "");
                    user.setGroupNames(rs.getString("Group_names") != null ? rs.getString("Group_names") : "");
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createUser(Users user, int groupId) {
        String sql = "INSERT INTO Users (Role_id, Branch_id, Full_name, Email, Password, Gender, Phone_number, Address, Date_of_birth, Image, Created_at, Updated_at, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            stmt.setInt(1, user.getRoleId());
            stmt.setInt(2, user.getBranchId());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setInt(6, user.getGender());
            stmt.setString(7, user.getPhoneNumber());
            stmt.setString(8, user.getAddress());
            stmt.setDate(9, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setString(10, user.getImage());
            stmt.setDate(11, currentDate);
            stmt.setDate(12, currentDate);
            stmt.setBoolean(13, user.isStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                    if (groupId > 0) {
                        assignUserToGroup(user.getUserId(), groupId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(Users user, List<Integer> groupIds) {
        String sql = "UPDATE Users SET Full_name=?, Password=?, Gender=?, Phone_number=?, Address=?, Date_of_birth=?, Branch_id=?, Image=?, Updated_at=?, Status=?, Role_id=? " +
                     "WHERE User_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getGender());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setDate(6, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setInt(7, user.getBranchId());
            stmt.setString(8, user.getImage());
            stmt.setDate(9, new java.sql.Date(System.currentTimeMillis()));
            stmt.setBoolean(10, user.isStatus());
            stmt.setInt(11, user.getRoleId());
            stmt.setInt(12, user.getUserId());

            stmt.executeUpdate();

            removeUserFromAllGroups(user.getUserId());
            if (groupIds != null) {
                for (int groupId : groupIds) {
                    if (groupId > 0) {
                        assignUserToGroup(user.getUserId(), groupId);
                    }
                }
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

            // Delete from Group_has_User
            String sqlGroup = "DELETE FROM Group_has_User WHERE User_id = ?";
            stmt = connection.prepareStatement(sqlGroup);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            stmt.close();

            // Delete from Users
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
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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

    public List<Branch> getAllBranches() {
        List<Branch> list = new ArrayList<>();
        String sql = "SELECT Branch_id, Name FROM Branch";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Branch(
                    rs.getInt("Branch_id"),
                    rs.getString("Name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching branches: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Group> getAllGroups() {
        List<Group> list = new ArrayList<>();
        String sql = "SELECT Group_id, Name, Role_id FROM `Group`";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Group(
                    rs.getInt("Group_id"),
                    rs.getString("Name"),
                    rs.getInt("Role_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all groups: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Group> getGroupsByRoleId(int roleId) {
        List<Group> list = new ArrayList<>();
        String sql = "SELECT Group_id, Name, Role_id FROM `Group` WHERE Role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Group(
                        rs.getInt("Group_id"),
                        rs.getString("Name"),
                        rs.getInt("Role_id")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching groups for Role_id " + roleId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Users> getUsers(int page, int pageSize, String searchQuery, Integer branchId, Integer roleId, Integer groupId, String sortOrder) {
    List<Users> users = new ArrayList<>();
    StringBuilder sql = new StringBuilder(
        "SELECT u.*, r.Name AS Role_name, b.Name AS Branch_name, " +
        "GROUP_CONCAT(g.Name SEPARATOR ', ') AS Group_names " +
        "FROM Users u " +
        "LEFT JOIN Role r ON u.Role_id = r.Role_id " +
        "LEFT JOIN Branch b ON u.Branch_id = b.Branch_id " +
        "LEFT JOIN Group_has_User gu ON u.User_id = gu.User_id " +
        "LEFT JOIN `Group` g ON gu.Group_id = g.Group_id " +
        "WHERE 1=1"
    );
    if (searchQuery != null && !searchQuery.isEmpty()) {
        sql.append(" AND (u.Full_name LIKE ? OR u.Email LIKE ? OR u.Phone_number LIKE ? OR u.Address LIKE ?)");
    }
    if (branchId != null) {
        sql.append(" AND u.Branch_id = ?");
    }
    if (roleId != null) {
        sql.append(" AND u.Role_id = ?");
    }
    if (groupId != null) {
        sql.append(" AND u.User_id IN (SELECT User_id FROM Group_has_User WHERE Group_id = ?)");
    }
    sql.append(" GROUP BY u.User_id, u.Role_id, u.Branch_id, u.Full_name, u.Email, u.Password, u.Gender, " +
               "u.Phone_number, u.Address, u.Date_of_birth, u.Image, u.Created_at, u.Updated_at, u.Status, " +
               "u.Reset_Password_Token, u.Reset_Password_Expiry, r.Name, b.Name " +
               "ORDER BY u.Updated_at ").append(sortOrder != null && sortOrder.equalsIgnoreCase("asc") ? "ASC" : "DESC").append(" LIMIT ? OFFSET ?");

    try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(paramIndex++, searchPattern);
            stmt.setString(paramIndex++, searchPattern);
            stmt.setString(paramIndex++, searchPattern);
            stmt.setString(paramIndex++, searchPattern);
        }
        if (branchId != null) {
            stmt.setInt(paramIndex++, branchId);
        }
        if (roleId != null) {
            stmt.setInt(paramIndex++, roleId);
        }
        if (groupId != null) {
            stmt.setInt(paramIndex++, groupId);
        }
        stmt.setInt(paramIndex++, pageSize);
        stmt.setInt(paramIndex++, (page - 1) * pageSize);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Users user = new Users(
                    rs.getInt("User_id"),
                    rs.getInt("Role_id"),
                    rs.getInt("Branch_id"),
                    rs.getString("Full_name"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getInt("Gender"),
                    rs.getString("Phone_number"),
                    rs.getString("Address"),
                    rs.getDate("Date_of_birth"),
                    rs.getString("Image"),
                    rs.getDate("Created_at"),
                    rs.getDate("Updated_at"),
                    rs.getBoolean("Status"),
                    rs.getString("Reset_Password_Token"),
                    rs.getTimestamp("Reset_Password_Expiry")
                );
                user.setRoleName(rs.getString("Role_name") != null ? rs.getString("Role_name") : "");
                user.setBranchName(rs.getString("Branch_name") != null ? rs.getString("Branch_name") : "");
                user.setGroupNames(rs.getString("Group_names") != null ? rs.getString("Group_names") : "");
                users.add(user);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error fetching users: " + e.getMessage());
        e.printStackTrace();
    }
    return users;
}

    public int getTotalUsers(String searchQuery, Integer branchId, Integer roleId, Integer groupId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT u.User_id) FROM Users u");
        if (groupId != null) {
            sql.append(" JOIN Group_has_User gu ON u.User_id = gu.User_id");
        }
        sql.append(" WHERE 1=1");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (u.Full_name LIKE ? OR u.Email LIKE ? OR u.Phone_number LIKE ? OR u.Address LIKE ?)");
        }
        if (branchId != null) {
            sql.append(" AND u.Branch_id = ?");
        }
        if (roleId != null) {
            sql.append(" AND u.Role_id = ?");
        }
        if (groupId != null) {
            sql.append(" AND gu.Group_id = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String searchPattern = "%" + searchQuery + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            if (branchId != null) {
                stmt.setInt(paramIndex++, branchId);
            }
            if (roleId != null) {
                stmt.setInt(paramIndex++, roleId);
            }
            if (groupId != null) {
                stmt.setInt(paramIndex++, groupId);
            }

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

    public List<String> getUserGroups(int userId) {
        List<String> groups = new ArrayList<>();
        String sql = "SELECT g.Name " +
                     "FROM `Group` g " +
                     "JOIN Group_has_User gu ON g.Group_id = gu.Group_id " +
                     "WHERE gu.User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String groupName = rs.getString("Name");
                    if (groupName != null) {
                        groups.add(groupName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching groups for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return groups;
    }

    public List<Integer> getUserGroupIds(int userId) {
        List<Integer> groupIds = new ArrayList<>();
        String sql = "SELECT Group_id FROM Group_has_User WHERE User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groupIds.add(rs.getInt("Group_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching group IDs for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return groupIds;
    }

    private void assignUserToGroup(int userId, int groupId) {
        String sql = "INSERT INTO Group_has_User (Group_id, User_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error assigning user ID " + userId + " to group ID " + groupId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeUserFromAllGroups(int userId) {
        String sql = "DELETE FROM Group_has_User WHERE User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing user ID " + userId + " from all groups: " + e.getMessage());
            e.printStackTrace();
        }
    }
/**
 *
 * @author duong
 */
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Users checkLogin(String email, String pass) {
        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                Users u = new Users(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getInt("branch_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("gender"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("date_of_birth"),
                        rs.getString("image"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at"),
                        rs.getBoolean("status"),
                        rs.getString("reset_password_token"),
                        rs.getDate("reset_password_expiry")
                );
                return u;

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
            String query = "UPDATE Users SET Password = ? WHERE User_id = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newPassword);
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
            String query = "UPDATE Users SET Password = ? WHERE Email = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace(); //a
            return false;
        }

    }
    public Users getUserByEmail(String email) {
        try {
             String query = "SELECT * FROM users WHERE LOWER(TRIM(email)) = ?";
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, email);
        rs = ps.executeQuery();
        if (rs.next()) {
            return new Users(
                rs.getInt("user_id"),
                rs.getInt("role_id"),
                rs.getInt("branch_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getDate("date_of_birth"),
                rs.getString("image"),
                rs.getDate("created_at"),
                rs.getDate("updated_at"),
                rs.getBoolean("status"),
                rs.getString("reset_password_token"),
                rs.getDate("reset_password_expiry")
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
                rs.getInt("branch_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getDate("date_of_birth"),
                rs.getString("image"),
                rs.getDate("created_at"),
                rs.getDate("updated_at"),
                rs.getBoolean("status"),
                rs.getString("reset_password_token"),
                rs.getDate("reset_password_expiry")
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
                rs.getInt("branch_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("gender"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getDate("date_of_birth"),
                rs.getString("image"),
                rs.getDate("created_at"),
                rs.getDate("updated_at"),
                rs.getBoolean("status"),
                rs.getString("reset_password_token"),
                rs.getDate("reset_password_expiry")
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
}
