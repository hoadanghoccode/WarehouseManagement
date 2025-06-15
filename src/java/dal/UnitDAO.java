package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Units;
import model.SubUnit;
import model.UnitConversion;

public class UnitDAO extends DBContext {

    public List<Units> getAllUnits(boolean onlyActive) {
        List<Units> units = new ArrayList<>();
        String sql = "SELECT Unit_id, Name, Status, Created_at, Updated_at FROM Units" +
                     (onlyActive ? " WHERE Status = 'active'" : "");
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Units unit = new Units(
                    rs.getInt("Unit_id"),
                    rs.getString("Name"),
                    rs.getString("Status").equals("active"),
                    rs.getTimestamp("Created_at"),
                    rs.getTimestamp("Updated_at")
                );
                units.add(unit);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all units: " + e.getMessage());
            e.printStackTrace();
        }
        return units;
    }

    // Fetch all units (default to all, including inactive)
    public List<Units> getAllUnits() {
        return getAllUnits(false);
    }

    public Units getUnitById(int unitId) {
        String sql = "SELECT Unit_id, Name, Status, Created_at, Updated_at FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Units(
                        rs.getInt("Unit_id"),
                        rs.getString("Name"),
                        rs.getString("Status").equals("active"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
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
        String sql = "INSERT INTO Units (Name, Status) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.isActive() ? "active" : "inactive");
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
        String sql = "UPDATE Units SET Name = ?, Status = ? WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.isActive() ? "active" : "inactive");
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

    // Fetch all subunits, optionally only active ones
    public List<SubUnit> getAllSubUnits(boolean onlyActive) {
        List<SubUnit> subUnits = new ArrayList<>();
        String sql = "SELECT SubUnit_id, Name, Status, Created_at, Updated_at FROM SubUnits" +
                     (onlyActive ? " WHERE Status = 'active'" : "");
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SubUnit subUnit = new SubUnit(
                    rs.getInt("SubUnit_id"),
                    rs.getString("Name"),
                    rs.getString("Status"),
                    rs.getTimestamp("Created_at"),
                    rs.getTimestamp("Updated_at")
                );
                subUnits.add(subUnit);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all subunits: " + e.getMessage());
            e.printStackTrace();
        }
        return subUnits;
    }

    // Fetch all subunits (default to all, including inactive)
    public List<SubUnit> getAllSubUnits() {
        return getAllSubUnits(false);
    }

    public SubUnit getSubUnitById(int subUnitId) {
        String sql = "SELECT SubUnit_id, Name, Status, Created_at, Updated_at FROM SubUnits WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subUnitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SubUnit(
                        rs.getInt("SubUnit_id"),
                        rs.getString("Name"),
                        rs.getString("Status"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching subunit with ID " + subUnitId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createSubUnit(SubUnit subUnit) {
        String sql = "INSERT INTO SubUnits (Name, Status) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, subUnit.getName());
            stmt.setString(2, subUnit.getStatus());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    subUnit.setSubUnitId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating subunit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSubUnit(SubUnit subUnit) {
        String sql = "UPDATE SubUnits SET Name = ?, Status = ? WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subUnit.getName());
            stmt.setString(2, subUnit.getStatus());
            stmt.setInt(3, subUnit.getSubUnitId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating subunit with ID " + subUnit.getSubUnitId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteSubUnit(int subUnitId) {
        String sql = "DELETE FROM SubUnits WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subUnitId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subunit with ID " + subUnitId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<UnitConversion> getAllUnitConversions() {
        List<UnitConversion> conversions = new ArrayList<>();
        String sql = "SELECT UnitConversion_id, Unit_id, SubUnit_id, Factor, Created_at, Updated_at FROM UnitConversion";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UnitConversion conversion = new UnitConversion(
                    rs.getInt("UnitConversion_id"),
                    rs.getInt("Unit_id"),
                    rs.getInt("SubUnit_id"),
                    rs.getDouble("Factor"),
                    rs.getTimestamp("Created_at"),
                    rs.getTimestamp("Updated_at")
                );
                conversions.add(conversion);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all unit conversions: " + e.getMessage());
            e.printStackTrace();
        }
        return conversions;
    }

    public UnitConversion getUnitConversionById(int unitConversionId) {
        String sql = "SELECT UnitConversion_id, Unit_id, SubUnit_id, Factor, Created_at, Updated_at FROM UnitConversion WHERE UnitConversion_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitConversionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UnitConversion(
                        rs.getInt("UnitConversion_id"),
                        rs.getInt("Unit_id"),
                        rs.getInt("SubUnit_id"),
                        rs.getDouble("Factor"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unit conversion with ID " + unitConversionId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createUnitConversion(UnitConversion conversion) {
        String sql = "INSERT INTO UnitConversion (Unit_id, SubUnit_id, Factor) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, conversion.getUnitId());
            stmt.setInt(2, conversion.getSubUnitId());
            stmt.setDouble(3, conversion.getFactor());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    conversion.setUnitConversionId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating unit conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUnitConversion(UnitConversion conversion) {
        String sql = "UPDATE UnitConversion SET Unit_id = ?, SubUnit_id = ?, Factor = ? WHERE UnitConversion_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, conversion.getUnitId());
            stmt.setInt(2, conversion.getSubUnitId());
            stmt.setDouble(3, conversion.getFactor());
            stmt.setInt(4, conversion.getUnitConversionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating unit conversion with ID " + conversion.getUnitConversionId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteUnitConversion(int unitConversionId) {
        String sql = "DELETE FROM UnitConversion WHERE UnitConversion_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitConversionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting unit conversion with ID " + unitConversionId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void deleteUnitConversionsBySubUnitId(int subUnitId) {
        String sql = "DELETE FROM UnitConversion WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subUnitId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting unit conversions for subunit ID " + subUnitId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUnitConversionsByUnitId(int unitId) {
        String sql = "DELETE FROM UnitConversion WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting unit conversions for unit ID " + unitId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<UnitConversion> getUnitConversionsBySubUnitId(int subUnitId) {
        List<UnitConversion> conversions = new ArrayList<>();
        String sql = "SELECT UnitConversion_id, Unit_id, SubUnit_id, Factor, Created_at, Updated_at FROM UnitConversion WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subUnitId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UnitConversion conversion = new UnitConversion(
                        rs.getInt("UnitConversion_id"),
                        rs.getInt("Unit_id"),
                        rs.getInt("SubUnit_id"),
                        rs.getDouble("Factor"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    );
                    conversions.add(conversion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unit conversions for subunit ID " + subUnitId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return conversions;
    }

    public List<UnitConversion> getUnitConversionsByUnitId(int unitId) {
        List<UnitConversion> conversions = new ArrayList<>();
        String sql = "SELECT UnitConversion_id, Unit_id, SubUnit_id, Factor, Created_at, Updated_at FROM UnitConversion WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UnitConversion conversion = new UnitConversion(
                        rs.getInt("UnitConversion_id"),
                        rs.getInt("Unit_id"),
                        rs.getInt("SubUnit_id"),
                        rs.getDouble("Factor"),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    );
                    conversions.add(conversion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unit conversions for unit ID " + unitId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return conversions;
    }
    
    public boolean isDuplicateConversion(int unitId, int subUnitId, Integer excludeId) {
    String sql = "SELECT COUNT(*) FROM UnitConversion WHERE Unit_id = ? AND SubUnit_id = ?";
    if (excludeId != null) {
        sql += " AND UnitConversion_id != ?";
    }
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, unitId);
        stmt.setInt(2, subUnitId);
        if (excludeId != null) {
            stmt.setInt(3, excludeId);
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error checking duplicate conversion: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
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
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error checking duplicate unit name: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

public boolean isDuplicateSubUnitName(String name, Integer excludeId) {
    String sql = "SELECT COUNT(*) FROM SubUnits WHERE Name = ?";
    if (excludeId != null) {
        sql += " AND SubUnit_id != ?";
    }
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, name);
        if (excludeId != null) {
            stmt.setInt(2, excludeId);
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error checking duplicate subunit name: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

}