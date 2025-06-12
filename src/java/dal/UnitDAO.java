package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Units;

public class UnitDAO extends DBContext {

    public List<Units> getAllUnits() {
        List<Units> units = new ArrayList<>();
        String sql = "SELECT Unit_id, Name, is_Active FROM Units";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Units unit = new Units(
                    rs.getInt("Unit_id"),
                    rs.getString("Name"),
                    rs.getBoolean("is_Active")
                );
                units.add(unit);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all units: " + e.getMessage());
            e.printStackTrace();
        }
        return units;
    }

    public Units getUnitById(int unitId) {
        String sql = "SELECT Unit_id, Name, is_Active FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Units(
                        rs.getInt("Unit_id"),
                        rs.getString("Name"),
                        rs.getBoolean("is_Active")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unit with ID " + unitId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createUnit(Units unit) {
        String sql = "INSERT INTO Units (Name, is_Active) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, unit.getName());
            stmt.setBoolean(2, unit.isActive());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    unit.setUnitId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating unit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUnit(Units unit) {
        String sql = "UPDATE Units SET Name = ?, is_Active = ? WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unit.getName());
            stmt.setBoolean(2, unit.isActive());
            stmt.setInt(3, unit.getUnitId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating unit with ID " + unit.getUnitId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteUnit(int unitId) {
        String sql = "DELETE FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting unit with ID " + unitId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}