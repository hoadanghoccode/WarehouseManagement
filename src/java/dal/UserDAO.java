
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

public class UserDAO extends DBContext {

    public Users getUserById(int userId) {
        String sql = "SELECT u.*, r.Name as Role_name FROM Users u "
                + "LEFT JOIN Role r ON u.Role_id = r.Role_id WHERE u.User_id = ?";
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
                user.setRoleName(rs.getString("Role_name"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createUser(Users user) {
        String sql = "INSERT INTO Users (Role_id, Branch_id, Full_name, Email, Password, Gender, Phone_number, Address, Date_of_birth, Image, Created_at, Updated_at, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
            stmt.setDate(11, new java.sql.Date(new java.util.Date().getTime())); 
            stmt.setDate(12, new java.sql.Date(new java.util.Date().getTime())); 
            stmt.setBoolean(13, user.isStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Users user) {
        String sql = "UPDATE Users SET Full_name=?, Password=?, Gender=?, Phone_number=?, Address=?, Date_of_birth=?, Branch_id=?, Image=?, Updated_at=?, Status=?, Role_id=? WHERE User_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getGender());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setDate(6, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setInt(7, user.getBranchId());
            stmt.setString(8, user.getImage());
            stmt.setDate(9, new java.sql.Date(new java.util.Date().getTime())); // Updated_at
            stmt.setBoolean(10, user.isStatus());
            stmt.setInt(11, user.getRoleId());
            stmt.setInt(12, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllBranchIds() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT Branch_id FROM Branch";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getInt("Branch_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
            e.printStackTrace();
        }
        return list;
    }

    public List<Users> getUsers(int page, int pageSize, String searchQuery) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT u.*, r.Name as Role_name FROM Users u "
                + "LEFT JOIN Role r ON u.Role_id = r.Role_id "
                + "WHERE u.Full_name LIKE ? OR u.Email LIKE ? OR u.Phone_number LIKE ? OR u.Address LIKE ? "
                + "ORDER BY u.Updated_at DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setString(3, "%" + searchQuery + "%");
            stmt.setString(4, "%" + searchQuery + "%");
            stmt.setInt(5, pageSize);
            stmt.setInt(6, (page - 1) * pageSize);
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
                user.setRoleName(rs.getString("Role_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int getTotalUsers(String searchQuery) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Full_name LIKE ? OR Email LIKE ? OR Phone_number LIKE ? OR Address LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setString(3, "%" + searchQuery + "%");
            stmt.setString(4, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

