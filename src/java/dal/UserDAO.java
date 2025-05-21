package dal;

import java.sql.*;
import model.Users;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBContext {

    public Users getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE User_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(
                    rs.getInt("User_id"),
                    rs.getInt("Role_id"),
                    rs.getInt("Branch_id"),
                    rs.getString("Full_name"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getInt("Gender"),
                    rs.getString("Phone_number"),
                    rs.getString("Address"),
                    rs.getDate("Date_of_birth")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(Users user) {
        String sql = "UPDATE Users SET Full_name=?, Password=?, Gender=?, Phone_number=?, Address=?, Date_of_birth=?, Branch_id=? WHERE User_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getGender());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setDate(6, new java.sql.Date(user.getDateOfBirth().getTime()));
            stmt.setInt(7, user.getBranchId());
            stmt.setInt(8, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllBranchIds() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT Branch_id FROM Branch";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getInt("Branch_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Users> getUsers(int page, int pageSize, String searchQuery) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Full_name LIKE ? OR Email LIKE ? ORDER BY Date_of_birth DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new Users(
                    rs.getInt("User_id"),
                    rs.getInt("Role_id"),
                    rs.getInt("Branch_id"),
                    rs.getString("Full_name"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getInt("Gender"),
                    rs.getString("Phone_number"),
                    rs.getString("Address"),
                    rs.getDate("Date_of_birth")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int getTotalUsers(String searchQuery) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Full_name LIKE ? OR Email LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}