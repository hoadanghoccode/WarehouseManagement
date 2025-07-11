package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Unit;
import model.Units;

public class UnitDAO extends DBContext {

    //Giữ hàm này cho b Giang theo db mới nhé <3
    public List<Unit> getAllUnits() {
        List<Unit> list = new ArrayList<>();
        String query = "SELECT Unit_id, Name, Status FROM Units WHERE Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnitId(rs.getInt("Unit_id"));
                u.setName(rs.getString("Name"));
                u.setStatus(rs.getString("Status"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.out.println("getAllUnits error: " + e.getMessage());
        }
        return list;
    }
    
    public List<Units> getAllUnits(boolean onlyActive) {
        List<Units> units = new ArrayList<>();
        String sql = "SELECT Unit_id, Name, SubUnit_id, Factor, Status, Created_at, Updated_at FROM Units"
                + (onlyActive ? " WHERE Status = 'active'" : "");
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                units.add(new Units(
                        rs.getInt("Unit_id"),
                        rs.getString("Name"),
                        rs.getInt("SubUnit_id"),
                        rs.getDouble("Factor"),
                        "active".equals(rs.getString("Status")),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching units: " + e.getMessage());
        }
        return units;
    }

    //Bạn Minh cần hàm này nha
    public List<Unit> getAllUnits(String status) {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT * FROM Units";
        if (status != null && !status.isEmpty()) {
            sql += " WHERE Status = ?";
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (status != null && !status.isEmpty()) {
                stmt.setString(1, status);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    units.add(new Unit(
                            rs.getInt("Unit_id"),
                            rs.getString("Name"),
                            rs.getString("Status"),
                            rs.getTimestamp("Created_at"),
                            rs.getTimestamp("Updated_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching units with status " + status + ": " + e.getMessage());
        }
        return units;
    }

    //Bạn Minh sửa hàm này nha
    public Unit getUnitById(int unitId) {
        String sql = "SELECT * FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Unit(
                            rs.getInt("Unit_id"),
                            rs.getString("Name"),
                            rs.getString("Status"),
                            rs.getTimestamp("Created_at"),
                            rs.getTimestamp("Updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unit ID " + unitId + ": " + e.getMessage());
        }
        return null;
    }

    //B Minh cần hàm này nên đừng xóa
    public List<Units> getAllUnitsWithSubUnit(String status) {
        List<Units> units = new ArrayList<>();
        String sql = "SELECT u.Unit_id, u.Name, u.Status, u.SubUnit_id, s.Name as SubUnit_Name, "
                + "u.Factor, u.Created_at, u.Updated_at "
                + "FROM Units u "
                + "INNER JOIN SubUnits s ON u.SubUnit_id = s.SubUnit_id";

        if (status != null && !status.isEmpty()) {
            sql += " WHERE u.Status = ?";
        }

        sql += " ORDER BY u.Name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (status != null && !status.isEmpty()) {
                stmt.setString(1, status);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Units unit = new Units();
                    unit.setUnitId(rs.getInt("Unit_id"));
                    unit.setName(rs.getString("Name"));
                    unit.setSubUnitId(rs.getInt("SubUnit_id"));
                    unit.setSubUnitName(rs.getString("SubUnit_Name"));
                    unit.setFactor(rs.getDouble("Factor"));
                    unit.setIsActive(rs.getString("Status").equals("active"));
                    unit.setCreatedAt(rs.getTimestamp("Created_at"));
                    unit.setUpdatedAt(rs.getTimestamp("Updated_at"));

                    units.add(unit);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching units with status " + status + ": " + e.getMessage());
        }
        return units;
    }

    public boolean createUnit(Unit unit) {
        String sql = "INSERT INTO Units (Name, Status, Created_at, Updated_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.getStatus());
            stmt.setTimestamp(3, unit.getCreatedAt());
            stmt.setTimestamp(4, unit.getUpdatedAt());
            int affectedRows = stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    unit.setUnitId(rs.getInt(1));
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error creating unit: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUnit(Unit unit) {
        // Check for transaction dependencies
        if (hasTransactionDependencies(unit.getUnitId())) {
            System.err.println("Cannot update unit ID " + unit.getUnitId() + ": Unit is used in transactions");
            return false;
        }
        String sql = "UPDATE Units SET Name = ?, Status = ?, Updated_at = ? WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.getStatus());
            stmt.setTimestamp(3, unit.getUpdatedAt());
            stmt.setInt(4, unit.getUnitId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating unit ID " + unit.getUnitId() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUnit(int unitId) {
        // Check for transaction dependencies
        if (hasTransactionDependencies(unitId)) {
            System.err.println("Cannot deactivate unit ID " + unitId + ": Unit is used in transactions");
            return false;
        }
        String sql = "UPDATE Units SET Status = 'inactive', Updated_at = ? WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, unitId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deactivating unit ID " + unitId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean permanentDeleteUnit(int unitId) {
        // Check for transaction dependencies
        if (hasTransactionDependencies(unitId)) {
            System.err.println("Cannot permanently delete unit ID " + unitId + ": Unit is used in transactions");
            return false;
        }
        String sql = "DELETE FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error permanently deleting unit ID " + unitId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean isDuplicateUnitName(String name, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM Units WHERE Name = ?";
        if (excludeId != null) {
            sql += " AND Unit_id != ?";
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            if (excludeId != null) {
                stmt.setInt(2, excludeId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking duplicate unit name: " + e.getMessage());
        }
        return false;
    }

    public boolean hasTransactionDependencies(int unitId) {
        String sql = "SELECT COUNT(*) FROM Materials m " +
                     "LEFT JOIN Import_note_detail ind ON m.Material_id = ind.Material_id " +
                     "LEFT JOIN Export_note_detail end ON m.Material_id = end.Material_id " +
                     "LEFT JOIN Order_detail od ON m.Material_id = od.Material_id " +
                     "LEFT JOIN PurchaseOrder_detail pod ON m.Material_id = pod.Material_id " +
                     "WHERE m.Unit_id = ? " +
                     "AND (ind.Material_id IS NOT NULL OR end.Material_id IS NOT NULL OR od.Material_id IS NOT NULL OR pod.Material_id IS NOT NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.err.println("Unit ID " + unitId + " has " + count + " transaction dependencies");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking transaction dependencies for unit ID " + unitId + ": " + e.getMessage());
        }
        System.err.println("Unit ID " + unitId + " has no transaction dependencies");
        return false;
    }
}