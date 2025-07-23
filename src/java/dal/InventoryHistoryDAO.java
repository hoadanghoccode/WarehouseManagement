package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.InventoryHistory;
import model.MaterialInfo;

public class InventoryHistoryDAO extends DBContext {

    public List<InventoryHistory> getFilteredInventoryHistories(int materialId, int unitId, String startDate, String endDate, 
                                                               String transactionType, String sortBy, int page, int pageSize) {
        List<InventoryHistory> inventoryHistories = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT imd.Inventory_Material_date AS transactionDate, " +
            "SUM(CASE WHEN q.Quality_name = 'available' THEN imd.Ending_qty ELSE 0 END) AS availableQty, " +
            "SUM(CASE WHEN q.Quality_name = 'notAvailable' THEN imd.Ending_qty ELSE 0 END) AS notAvailableQty, " +
            "SUM(imd.Import_qty) AS Import_qty, " +
            "SUM(imd.Export_qty) AS Export_qty, " +
            "GROUP_CONCAT(imd.Note) AS Note " +
            "FROM InventoryMaterialDaily imd " +
            "JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id " +
            "JOIN Quality q ON md.Quality_id = q.Quality_id " +
            "JOIN Materials m ON md.Material_id = m.Material_id " +
            "WHERE md.Material_id = ? AND m.Unit_id = ? "
        );

        // Apply date range filter only if dates are provided
        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date <= ? ");
        }

        // Apply transaction type filter
        if (transactionType != null && !transactionType.equals("all")) {
            if (transactionType.equals("import")) {
                sql.append("AND imd.Import_qty > 0 AND imd.Export_qty = 0 ");
            } else if (transactionType.equals("export")) {
                sql.append("AND imd.Export_qty > 0 AND imd.Import_qty = 0 ");
            }
        }

        sql.append("GROUP BY imd.Inventory_Material_date ");

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "date_asc":
                    sql.append("ORDER BY imd.Inventory_Material_date ASC ");
                    break;
                case "import_desc":
                    sql.append("ORDER BY SUM(imd.Import_qty) DESC ");
                    break;
                case "export_desc":
                    sql.append("ORDER BY SUM(imd.Export_qty) DESC ");
                    break;
                default:
                    sql.append("ORDER BY imd.Inventory_Material_date DESC ");
                    break;
            }
        }

        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, materialId);
            stmt.setInt(paramIndex++, unitId);
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setString(paramIndex++, endDate);
            }
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex++, (page - 1) * pageSize);

            System.out.println("Executing query: " + sql.toString());
            System.out.println("Parameters: materialId=" + materialId + ", unitId=" + unitId + 
                              ", startDate=" + startDate + ", endDate=" + endDate + 
                              ", pageSize=" + pageSize + ", offset=" + ((page - 1) * pageSize));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InventoryHistory history = new InventoryHistory();
                    history.setTransactionDate(rs.getDate("transactionDate").toLocalDate());
                    history.setAvailableQty(rs.getDouble("availableQty"));
                    history.setNotAvailableQty(rs.getDouble("notAvailableQty"));
                    history.setImportQty(rs.getDouble("Import_qty"));
                    history.setExportQty(rs.getDouble("Export_qty"));
                    history.setNote(rs.getString("Note") != null ? rs.getString("Note") : "N/A");
                    inventoryHistories.add(history);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Retrieved " + inventoryHistories.size() + " records");
        return inventoryHistories;
    }

    public int getTotalRecords(int materialId, int unitId, String startDate, String endDate, String transactionType) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(DISTINCT imd.Inventory_Material_date) AS total " +
            "FROM InventoryMaterialDaily imd " +
            "JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id " +
            "JOIN Materials m ON md.Material_id = m.Material_id " +
            "WHERE md.Material_id = ? AND m.Unit_id = ? "
        );

        // Apply date range filter only if dates are provided
        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date <= ? ");
        }

        // Apply transaction type filter
        if (transactionType != null && !transactionType.equals("all")) {
            if (transactionType.equals("import")) {
                sql.append("AND imd.Import_qty > 0 AND imd.Export_qty = 0 ");
            } else if (transactionType.equals("export")) {
                sql.append("AND imd.Export_qty > 0 AND imd.Import_qty = 0 ");
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, materialId);
            stmt.setInt(paramIndex++, unitId);
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setString(paramIndex++, endDate);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalHistoricalImportQty(int materialId, int unitId) {
        String sql = """
            SELECT COALESCE(SUM(imd.Import_qty), 0) AS totalImport
            FROM InventoryMaterialDaily imd
            JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id
            JOIN Materials m ON md.Material_id = m.Material_id
            WHERE md.Material_id = ? 
            AND m.Unit_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            stmt.setInt(2, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("totalImport");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTotalHistoricalImportQty: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getTotalHistoricalExportQty(int materialId, int unitId) {
        String sql = """
            SELECT COALESCE(SUM(imd.Export_qty), 0) AS totalExport
            FROM InventoryMaterialDaily imd
            JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id
            JOIN Materials m ON md.Material_id = m.Material_id
            WHERE md.Material_id = ? 
            AND m.Unit_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            stmt.setInt(2, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("totalExport");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTotalHistoricalExportQty: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    public Map<String, Double> getTotalImportExportByPeriod(int materialId, int unitId, String totalPeriod, String totalStartDate, String totalEndDate) {
        Map<String, Double> totals = new HashMap<>();
        totals.put("totalImport", 0.0);
        totals.put("totalExport", 0.0);

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "SUM(imd.Import_qty) AS totalImport, " +
            "SUM(imd.Export_qty) AS totalExport " +
            "FROM InventoryMaterialDaily imd " +
            "JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id " +
            "JOIN Materials m ON md.Material_id = m.Material_id " +
            "WHERE md.Material_id = ? AND m.Unit_id = ? "
        );

        // Apply period filter only if not "all"
        if ("custom_total".equals(totalPeriod) && totalStartDate != null && !totalStartDate.isEmpty() && totalEndDate != null && !totalEndDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date >= ? AND imd.Inventory_Material_date <= ? ");
        } else if (!"all".equals(totalPeriod)) {
            int days = switch (totalPeriod) {
                case "30" -> 30;
                case "180" -> 180;
                default -> 7;
            };
            sql.append("AND imd.Inventory_Material_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) ");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, materialId);
            stmt.setInt(paramIndex++, unitId);
            if ("custom_total".equals(totalPeriod) && totalStartDate != null && !totalStartDate.isEmpty() && totalEndDate != null && !totalEndDate.isEmpty()) {
                stmt.setString(paramIndex++, totalStartDate);
                stmt.setString(paramIndex++, totalEndDate);
            } else if (!"all".equals(totalPeriod)) {
                int days = switch (totalPeriod) {
                    case "30" -> 30;
                    case "180" -> 180;
                    default -> 7;
                };
                stmt.setInt(paramIndex++, days);
            }

            System.out.println("Executing total query: " + sql.toString());
            System.out.println("Parameters: materialId=" + materialId + ", unitId=" + unitId + 
                              ", totalPeriod=" + totalPeriod + ", totalStartDate=" + totalStartDate + 
                              ", totalEndDate=" + totalEndDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totals.put("totalImport", rs.getDouble("totalImport") != 0 ? rs.getDouble("totalImport") : 0.0);
                    totals.put("totalExport", rs.getDouble("totalExport") != 0 ? rs.getDouble("totalExport") : 0.0);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return totals;
    }

    public InventoryHistory getLatestHistoryByDate(int materialId, int unitId, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder(
            "SELECT imd.Inventory_Material_date AS transactionDate, " +
            "SUM(CASE WHEN q.Quality_name = 'available' THEN imd.Ending_qty ELSE 0 END) AS availableQty, " +
            "SUM(CASE WHEN q.Quality_name = 'notAvailable' THEN imd.Ending_qty ELSE 0 END) AS notAvailableQty, " +
            "SUM(imd.Import_qty) AS Import_qty, " +
            "SUM(imd.Export_qty) AS Export_qty, " +
            "GROUP_CONCAT(imd.Note) AS Note " +
            "FROM InventoryMaterialDaily imd " +
            "JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id " +
            "JOIN Quality q ON md.Quality_id = q.Quality_id " +
            "JOIN Materials m ON md.Material_id = m.Material_id " +
            "WHERE md.Material_id = ? AND m.Unit_id = ? "
        );

        // Apply date range filter only if dates are provided
        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date >= ? ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND imd.Inventory_Material_date <= ? ");
        }

        sql.append("GROUP BY imd.Inventory_Material_date ORDER BY imd.Inventory_Material_date DESC LIMIT 1");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            stmt.setInt(paramIndex++, materialId);
            stmt.setInt(paramIndex++, unitId);
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setString(paramIndex++, endDate);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InventoryHistory history = new InventoryHistory();
                    history.setTransactionDate(rs.getDate("transactionDate").toLocalDate());
                    history.setAvailableQty(rs.getDouble("availableQty"));
                    history.setNotAvailableQty(rs.getDouble("notAvailableQty"));
                    history.setImportQty(rs.getDouble("Import_qty"));
                    history.setExportQty(rs.getDouble("Export_qty"));
                    history.setNote(rs.getString("Note") != null ? rs.getString("Note") : "N/A");
                    return history;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public double getTotalImportQty(int materialId) {
        String sql = "SELECT SUM(Quantity) FROM Import_note_detail WHERE Material_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalExportQty(int materialId) {
        String sql = "SELECT SUM(Quantity) FROM Export_note_detail WHERE Material_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public MaterialInfo getMaterialInfo(int materialId) {
        String sql = """
            SELECT 
                m.Name AS materialName,
                c.Name AS categoryName,
                u.Name AS unitName,
                SUM(md.Quantity) AS availableQty
            FROM Materials m
            JOIN Category c ON m.Category_id = c.Category_id
            JOIN Material_detail md ON m.Material_id = md.Material_id
            JOIN Units u ON m.Unit_id = u.Unit_id
            JOIN Quality q ON md.Quality_id = q.Quality_id
            WHERE m.Material_id = ?
              AND q.Quality_name = 'available'
            GROUP BY m.Name, c.Name, u.Name
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MaterialInfo info = new MaterialInfo();
                info.setMaterialName(rs.getString("materialName"));
                info.setCategoryName(rs.getString("categoryName"));
                info.setSubUnitName(rs.getString("unitName")); // Changed to unitName
                info.setAvailableQty(rs.getDouble("availableQty"));
                return info;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean isMaterialExists(int materialId) {
        String sql = "SELECT COUNT(*) FROM Materials WHERE Material_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUnitExists(int unitId) {
        String sql = "SELECT COUNT(*) FROM Units WHERE Unit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
   
    public static void main(String[] args) {
      LocalDate fromLocal = LocalDate.of(2025, 7, 20);
        LocalDate toLocal = LocalDate.of(2025, 7, 20);

        java.sql.Date from = java.sql.Date.valueOf(fromLocal);
        java.sql.Date to = java.sql.Date.valueOf(toLocal);


}
    }
