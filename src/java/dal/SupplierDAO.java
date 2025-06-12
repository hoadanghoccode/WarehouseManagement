/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ADMIN
 */
public class SupplierDAO extends DBContext{
    
     public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Suppliers";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("Supplier_id"));
                supplier.setName(rs.getString("Name"));
                supplier.setStatus(rs.getString("Status"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return suppliers;
    }
     
     public Supplier getSupplierById(int sid) {
        String query = "SELECT * FROM Suppliers WHERE Supplier_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("Supplier_id"));
                supplier.setName(rs.getString("Name"));
                supplier.setStatus(rs.getString("Status"));
                return supplier;
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
     
     public static void main(String[] args) {
        SupplierDAO dao = new SupplierDAO();
         System.out.println(dao.getSupplierById(1));
    }

}
