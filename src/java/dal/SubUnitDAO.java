/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SubUnit;

/**
 *
 * @author ADMIN
 */
public class SubUnitDAO extends DBContext {

    //Bạn Minh làm tạm hàm này trước để còn test chức năng
    public List<SubUnit> getAllSubUnits() {
        List<SubUnit> subUnits = new ArrayList<>();
        String query = "SELECT * FROM SubUnits";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SubUnit subUnit = new SubUnit();
                subUnit.setSubUnitId(rs.getInt("SubUnit_id"));
                subUnit.setName(rs.getString("Name"));
                subUnit.setStatus(rs.getString("Status"));
                subUnit.setCreatedAt(rs.getTimestamp("Created_at"));
                subUnit.setUpdatedAt(rs.getTimestamp("Updated_at"));
                subUnits.add(subUnit);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return subUnits;
    }

    public SubUnit getSubUnitById(int sUnitId) {
        String query = "SELECT * FROM SubUnits WHERE SubUnit_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, sUnitId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SubUnit subUnit = new SubUnit();
                subUnit.setSubUnitId(rs.getInt("SubUnit_id"));
                subUnit.setName(rs.getString("Name"));
                subUnit.setStatus(rs.getString("Status"));
                subUnit.setCreatedAt(rs.getTimestamp("Created_at"));
                subUnit.setUpdatedAt(rs.getTimestamp("Updated_at"));
                
                return subUnit;
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    public static void main(String[] args) {
        SubUnitDAO dao = new SubUnitDAO();
        
        System.out.println(dao.getSubUnitById(1));
    }

}
