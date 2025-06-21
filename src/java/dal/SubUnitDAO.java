package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SubUnit;

public class SubUnitDAO extends DBContext {

    public List<SubUnit> getAllSubUnits(boolean onlyActive) {
        List<SubUnit> subUnits = new ArrayList<>();
        String sql = "SELECT SubUnit_id, Name, Status, Created_at, Updated_at FROM SubUnits" +
                     (onlyActive ? " WHERE Status = 'active'" : "");
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subUnits.add(new SubUnit(
                    rs.getInt("SubUnit_id"),
                    rs.getString("Name"),
                    "active".equals(rs.getString("Status")),
                    rs.getTimestamp("Created_at"),
                    rs.getTimestamp("Updated_at")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error fetching subunits: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
        }
        return subUnits;
    }

    public List<SubUnit> getAllSubUnits(String status) {
        List<SubUnit> subUnits = new ArrayList<>();
        String sql = "SELECT SubUnit_id, Name, Status, Created_at, Updated_at FROM SubUnits";
        if (status != null && !status.isEmpty()) {
            sql += " WHERE Status = ?";
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (status != null && !status.isEmpty()) {
                stmt.setString(1, status);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subUnits.add(new SubUnit(
                        rs.getInt("SubUnit_id"),
                        rs.getString("Name"),
                        "active".equals(rs.getString("Status")),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    ));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error fetching subunits with status " + status + ": {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
        }
        return subUnits;
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
                        "active".equals(rs.getString("Status")),
                        rs.getTimestamp("Created_at"),
                        rs.getTimestamp("Updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error fetching subunit ID {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnitId, e.getMessage(), e.getSQLState(), e.getErrorCode()});
        }
        return null;
    }

    public boolean createSubUnit(SubUnit subUnit) {
        String sql = "INSERT INTO SubUnits (Name, Status, Created_at, Updated_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, subUnit.getName());
            stmt.setString(2, subUnit.getIsActive() ? "active" : "inactive");
            stmt.setTimestamp(3, subUnit.getCreatedAt());
            stmt.setTimestamp(4, subUnit.getUpdatedAt());
            int affectedRows = stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    subUnit.setSubUnitId(rs.getInt(1));
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error creating subunit: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
            return false;
        }
    }

    public boolean updateSubUnit(SubUnit subUnit) {
        String sql = "UPDATE SubUnits SET Name = ?, Status = ?, Updated_at = ? WHERE SubUnit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subUnit.getName());
            stmt.setString(2, subUnit.getIsActive() ? "active" : "inactive");
            stmt.setTimestamp(3, subUnit.getUpdatedAt());
            stmt.setInt(4, subUnit.getSubUnitId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                Logger.getLogger(SubUnitDAO.class.getName()).log(Level.WARNING, "No rows updated for SubUnit_id: {0}", subUnit.getSubUnitId());
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error updating subunit ID {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnit.getSubUnitId(), e.getMessage(), e.getSQLState(), e.getErrorCode()});
            return false;
        }
    }

    public boolean deleteSubUnit(int subUnitId) {
        try {
            connection.setAutoCommit(false); // Start transaction
            try {
                // Delete associated units
                String deleteUnitsSql = "DELETE FROM Units WHERE SubUnit_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(deleteUnitsSql)) {
                    stmt.setInt(1, subUnitId);
                    int deletedUnits = stmt.executeUpdate();
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.INFO, "Deleted {0} Units records for SubUnit_id: {1}", new Object[]{deletedUnits, subUnitId});
                }

                // Deactivate the subunit
                String sql = "UPDATE SubUnits SET Status = 'inactive', Updated_at = ? WHERE SubUnit_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                    stmt.setInt(2, subUnitId);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        connection.commit(); // Commit transaction
                        Logger.getLogger(SubUnitDAO.class.getName()).log(Level.INFO, "Deactivated SubUnit_id: {0}", subUnitId);
                        return true;
                    } else {
                        connection.rollback(); // Rollback if no rows affected
                        Logger.getLogger(SubUnitDAO.class.getName()).log(Level.WARNING, "No subunit found to deactivate for SubUnit_id: {0}", subUnitId);
                        return false;
                    }
                }
            } catch (SQLException e) {
                try {
                    connection.rollback(); // Rollback on error
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.WARNING, "Transaction rolled back for SubUnit_id: {0}", subUnitId);
                } catch (SQLException rollbackEx) {
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error rolling back transaction: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{rollbackEx.getMessage(), rollbackEx.getSQLState(), rollbackEx.getErrorCode()});
                }
                Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error deactivating subunit ID {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnitId, e.getMessage(), e.getSQLState(), e.getErrorCode()});
                return false;
            } finally {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                } catch (SQLException e) {
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error restoring auto-commit: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error initiating transaction for SubUnit_id {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnitId, ex.getMessage(), ex.getSQLState(), ex.getErrorCode()});
            return false;
        }
    }

    public boolean permanentDeleteSubUnit(int subUnitId) {
        try {
            connection.setAutoCommit(false); // Start transaction
            try {
                // Delete associated units
                String deleteUnitsSql = "DELETE FROM Units WHERE SubUnit_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(deleteUnitsSql)) {
                    stmt.setInt(1, subUnitId);
                    int deletedUnits = stmt.executeUpdate();
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.INFO, "Deleted {0} Units records for SubUnit_id: {1}", new Object[]{deletedUnits, subUnitId});
                }

                // Permanently delete the subunit
                String sql = "DELETE FROM SubUnits WHERE SubUnit_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, subUnitId);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        connection.commit(); // Commit transaction
                        Logger.getLogger(SubUnitDAO.class.getName()).log(Level.INFO, "Permanently deleted SubUnit_id: {0}", subUnitId);
                        return true;
                    } else {
                        connection.rollback(); // Rollback if no rows affected
                        Logger.getLogger(SubUnitDAO.class.getName()).log(Level.WARNING, "No subunit found to permanently delete for SubUnit_id: {0}", subUnitId);
                        return false;
                    }
                }
            } catch (SQLException e) {
                try {
                    connection.rollback(); // Rollback on error
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.WARNING, "Transaction rolled back for SubUnit_id: {0}", subUnitId);
                } catch (SQLException rollbackEx) {
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error rolling back transaction: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{rollbackEx.getMessage(), rollbackEx.getSQLState(), rollbackEx.getErrorCode()});
                }
                Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error permanently deleting subunit ID {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnitId, e.getMessage(), e.getSQLState(), e.getErrorCode()});
                return false;
            } finally {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                } catch (SQLException e) {
                    Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error restoring auto-commit: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error initiating transaction for SubUnit_id {0}: {1}, SQLState: {2}, ErrorCode: {3}", new Object[]{subUnitId, ex.getMessage(), ex.getSQLState(), ex.getErrorCode()});
            return false;
        }
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
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(SubUnitDAO.class.getName()).log(Level.SEVERE, "Error checking duplicate subunit name: {0}, SQLState: {1}, ErrorCode: {2}", new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
        }
        return false;
    }
}