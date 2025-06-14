/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;

/**
 *
 * @author PC
 */
public class SupplierDAO extends DBContext {

    private Connection conn;

    public SupplierDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    public ArrayList<Supplier> getAllSuppliers() {
        ArrayList<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Status")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error getting suppliers: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Supplier> searchByName(ArrayList<Supplier> list, String keyword) {
        ArrayList<Supplier> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        for (Supplier s : list) {
            if (s.getName().toLowerCase().contains(keyword)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Supplier> getListByPage(ArrayList<Supplier> list, int start, int end) {
        List<Supplier> result = new ArrayList<>();
        for (int i = start; i < end && i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
    }

    public boolean existsSupplier(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM supplier WHERE Name = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, name);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean addSupplier(String name, String status) {
        String sql = "INSERT INTO supplier (Name, Status) VALUES (?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, status);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplierById(int supplierId, String newStatus) throws SQLException {
        String sql = "UPDATE supplier SET Status = ? WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, supplierId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Supplier> filterByStatus(ArrayList<Supplier> list, String status) {
        ArrayList<Supplier> result = new ArrayList<>();
        for (Supplier s : list) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                result.add(s);
            }
        }
        return result;
    }

}
