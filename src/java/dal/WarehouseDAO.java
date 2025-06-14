package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Warehouse;

public class WarehouseDAO extends DBContext {

    // Get warehouse by ID
    public Warehouse getWarehouseById(int warehouseId) {
        String query = "SELECT * FROM Warehouse WHERE Warehouse_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, warehouseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Warehouse(
                    rs.getInt("Warehouse_id"),
                    rs.getString("Name"),
                    rs.getString("Address")
                );
            }
        } catch (SQLException e) {
            System.out.println("getWarehouseById error: " + e.getMessage());
        }
        return null;
    }

    // Get all warehouses
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String query = "SELECT * FROM Warehouse";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Warehouse(
                    rs.getInt("Warehouse_id"),
                    rs.getString("Name"),
                    rs.getString("Address")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getAllWarehouses error: " + e.getMessage());
        }
        return list;
    }
}