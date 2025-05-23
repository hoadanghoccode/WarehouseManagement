
package dal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import model.Users;
import model.Role;
import java.util.ArrayList;
import java.util.List;
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
            ResultSet rs = stmt.executeQuery();
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
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            stmt.setDate(11, currentDate);
            stmt.setDate(12, currentDate);
            stmt.setBoolean(13, user.isStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
                if (groupId > 0) {
                    assignUserToGroup(user.getUserId(), groupId);
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
            for (int groupId : groupIds) {
                assignUserToGroup(user.getUserId(), groupId);
            }
        } catch (SQLException e) {
            System.err.println("Error updating user with ID " + user.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
        conn = connection;
        conn.setAutoCommit(false); 

        String sqlGroup = "DELETE FROM Group_has_User WHERE User_id = ?";
        stmt = conn.prepareStatement(sqlGroup);
        stmt.setInt(1, userId);
        stmt.executeUpdate();
        stmt.close();

        String sqlUser = "DELETE FROM Users WHERE User_id = ?";
        stmt = conn.prepareStatement(sqlUser);
        stmt.setInt(1, userId);
        stmt.executeUpdate();

        conn.commit(); 
    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
                ex.printStackTrace();
            }
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
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
                e.printStackTrace();
            }
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

    public List<Branch> getAllBranches() {
        List<Branch> list = new ArrayList<>();
        String sql = "SELECT Branch_id, Name FROM Branch";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
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

    public List<Group> getGroupsByRoleId(int roleId) {
        List<Group> list = new ArrayList<>();
        String sql = "SELECT Group_id, Name, Role_id FROM `Group` WHERE Role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Group(
                    rs.getInt("Group_id"),
                    rs.getString("Name"),
                    rs.getInt("Role_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching groups for Role_id " + roleId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Users> getUsers(int page, int pageSize, String searchQuery, Integer branchId, Integer roleId) {
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
        sql.append(" GROUP BY u.User_id, u.Role_id, u.Branch_id, u.Full_name, u.Email, u.Password, u.Gender, " +
                   "u.Phone_number, u.Address, u.Date_of_birth, u.Image, u.Created_at, u.Updated_at, u.Status, " +
                   "u.Reset_Password_Token, u.Reset_Password_Expiry, r.Name, b.Name " +
                   "ORDER BY u.Updated_at DESC LIMIT ? OFFSET ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (branchId != null) {
                stmt.setInt(paramIndex++, branchId);
            }
            if (roleId != null) {
                stmt.setInt(paramIndex++, roleId);
            }
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex++, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public int getTotalUsers(String searchQuery, Integer branchId, Integer roleId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Users WHERE 1=1");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (Full_name LIKE ? OR Email LIKE ? OR Phone...");
        }
        if (branchId != null) {
            sql.append(" AND Branch_id = ?");
        }
        if (roleId != null) {
            sql.append(" AND Role_id = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
                stmt.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (branchId != null) {
                stmt.setInt(paramIndex++, branchId);
            }
            if (roleId != null) {
                stmt.setInt(paramIndex++, roleId);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
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
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
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
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                groups.add(rs.getString("Name"));
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
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                groupIds.add(rs.getInt("Group_id"));
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
    
    public Users checkLogin(String email,String pass){
        try{
            String query = "select * from users where email = ? and password = ?";
            conn = new DBContext().getConnection();
            
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while(rs.next()){
                Users u = new Users(
                    rs.getInt("User_id"),
                    rs.getInt("Role_id"),
                    rs.getInt("Branch_id"),
                    rs.getString("Full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("gender"),
                    rs.getString("phone_Number"),
                    rs.getString("address"),
                    rs.getDate("date_Of_Birth")
                );
                return u;
            }
        } catch (Exception e){
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
}

