package dal;

import model.Category;
import model.MaterialInventory;
import model.Quality;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends DBContext {

    public List<MaterialInventory> getInventory(int categoryId, int qualityId, String searchTerm, String sortBy, String startDate, String endDate) {
        List<MaterialInventory> inventoryList = new ArrayList<>();
        String sql = buildInventoryQuery(categoryId, qualityId, searchTerm, sortBy, startDate, endDate);
        List<Object> params = buildInventoryParameters(categoryId, qualityId, searchTerm, startDate, endDate);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaterialInventory inventory = new MaterialInventory();
                    inventory.setMaterialId(rs.getInt("material_id"));
                    inventory.setCategoryId(rs.getInt("category_id"));
                    inventory.setUnitId(rs.getInt("unit_id"));
                    inventory.setMaterialName(rs.getString("material_name"));
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventory.setUnitName(rs.getString("unit_name"));
                    inventory.setImage(rs.getString("image"));
                    inventory.setAvailableQty(rs.getBigDecimal("available_qty") != null ? rs.getBigDecimal("available_qty") : BigDecimal.ZERO);
                    inventory.setNotAvailableQty(rs.getBigDecimal("not_available_qty") != null ? rs.getBigDecimal("not_available_qty") : BigDecimal.ZERO);
                    inventory.setImportQty(rs.getBigDecimal("total_import") != null ? rs.getBigDecimal("total_import") : BigDecimal.ZERO);
                    inventory.setExportQty(rs.getBigDecimal("total_export") != null ? rs.getBigDecimal("total_export") : BigDecimal.ZERO);
                    inventory.setInventoryDate(rs.getDate("last_updated"));
                    inventory.setNote(rs.getString("note"));
                    inventoryList.add(inventory);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
            e.printStackTrace();
        }
        return inventoryList;
    }

    private String buildInventoryQuery(int categoryId, int qualityId, String searchTerm, String sortBy, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder(
            "SELECT m.Material_id AS material_id, m.Category_id AS category_id, m.Unit_id AS unit_id, " +
            "m.Name AS material_name, c.Name AS category_name, u.Name AS unit_name, m.Image AS image, " +
            "(SELECT COALESCE(SUM(imd.Ending_qty), 0) " +
            " FROM InventoryMaterialDaily imd " +
            " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
            " JOIN Quality q ON md2.Quality_id = q.Quality_id " +
            " WHERE md2.Material_id = m.Material_id AND q.Quality_name = 'available' " +
            " AND imd.Inventory_Material_date = (SELECT MAX(imd2.Inventory_Material_date) " +
            "   FROM InventoryMaterialDaily imd2 " +
            "   JOIN Material_detail md3 ON imd2.Material_detail_id = md3.Material_detail_id " +
            "   WHERE md3.Material_id = m.Material_id " +
            (startDate != null && !startDate.isEmpty() ? " AND imd2.Inventory_Material_date >= ? " : "") +
            (endDate != null && !endDate.isEmpty() ? " AND imd2.Inventory_Material_date <= ? " : "") +
            ")) AS available_qty, " +
            "(SELECT COALESCE(SUM(imd.Ending_qty), 0) " +
            " FROM InventoryMaterialDaily imd " +
            " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
            " JOIN Quality q ON md2.Quality_id = q.Quality_id " +
            " WHERE md2.Material_id = m.Material_id AND q.Quality_name = 'notAvailable' " +
            " AND imd.Inventory_Material_date = (SELECT MAX(imd2.Inventory_Material_date) " +
            "   FROM InventoryMaterialDaily imd2 " +
            "   JOIN Material_detail md3 ON imd2.Material_detail_id = md3.Material_detail_id " +
            "   WHERE md3.Material_id = m.Material_id " +
            (startDate != null && !startDate.isEmpty() ? " AND imd2.Inventory_Material_date >= ? " : "") +
            (endDate != null && !endDate.isEmpty() ? " AND imd2.Inventory_Material_date <= ? " : "") +
            ")) AS not_available_qty, " +
            "(SELECT COALESCE(SUM(imd.Import_qty), 0) " +
            " FROM InventoryMaterialDaily imd " +
            " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
            " WHERE md2.Material_id = m.Material_id " +
            (startDate != null && !startDate.isEmpty() ? " AND imd.Inventory_Material_date >= ? " : "") +
            (endDate != null && !endDate.isEmpty() ? " AND imd.Inventory_Material_date <= ? " : "") +
            ") AS total_import, " +
            "(SELECT COALESCE(SUM(imd.Export_qty), 0) " +
            " FROM InventoryMaterialDaily imd " +
            " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
            " WHERE md2.Material_id = m.Material_id " +
            (startDate != null && !startDate.isEmpty() ? " AND imd.Inventory_Material_date >= ? " : "") +
            (endDate != null && !endDate.isEmpty() ? " AND imd.Inventory_Material_date <= ? " : "") +
            ") AS total_export, " +
            "MAX(imd_last.Inventory_Material_date) AS last_updated, " +
            "NULL AS note " +
            "FROM Materials m " +
            "JOIN Material_detail md ON m.Material_id = md.Material_id " +
            "JOIN Units u ON m.Unit_id = u.Unit_id " +
            "JOIN Category c ON m.Category_id = c.Category_id " +
            "JOIN Quality q ON md.Quality_id = q.Quality_id " +
            "LEFT JOIN InventoryMaterialDaily imd_last ON md.Material_detail_id = imd_last.Material_detail_id " +
            "WHERE m.Status = 'active' AND u.Status = 'active' AND c.Status = 'active' "
        );

        if (categoryId > 0) {
            sql.append(" AND m.Category_id = ? ");
        }
        if (qualityId > 0) {
            sql.append(" AND md.Quality_id = ? ");
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (CAST(m.Material_id AS CHAR) LIKE ? OR m.Name LIKE ? OR c.Name LIKE ? OR u.Name LIKE ?) ");
        }

        sql.append(" GROUP BY m.Material_id, m.Category_id, m.Unit_id, m.Name, c.Name, u.Name, m.Image ");

        String safeSortBy = "available_qty DESC";
        if (sortBy != null && sortBy.matches("^(material_id|material_name|available_qty|not_available_qty|import_qty|export_qty) (ASC|DESC)$")) {
            safeSortBy = sortBy.replace("material_name", "m.Name")
                              .replace("available_qty", "available_qty")
                              .replace("not_available_qty", "not_available_qty")
                              .replace("import_qty", "total_import")
                              .replace("export_qty", "total_export");
        }
        sql.append(" ORDER BY ").append(safeSortBy);

        return sql.toString();
    }

    private List<Object> buildInventoryParameters(int categoryId, int qualityId, String searchTerm, String startDate, String endDate) {
        List<Object> params = new ArrayList<>();
        // Parameters for available_qty subquery
        if (startDate != null && !startDate.isEmpty()) params.add(startDate);
        if (endDate != null && !endDate.isEmpty()) params.add(endDate);
        // Parameters for not_available_qty subquery
        if (startDate != null && !startDate.isEmpty()) params.add(startDate);
        if (endDate != null && !endDate.isEmpty()) params.add(endDate);
        // Parameters for total_import subquery
        if (startDate != null && !startDate.isEmpty()) params.add(startDate);
        if (endDate != null && !endDate.isEmpty()) params.add(endDate);
        // Parameters for total_export subquery
        if (startDate != null && !startDate.isEmpty()) params.add(startDate);
        if (endDate != null && !endDate.isEmpty()) params.add(endDate);
        // Main query parameters
        if (categoryId > 0) {
            params.add(categoryId);
        }
        if (qualityId > 0) {
            params.add(qualityId);
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            for (int i = 0; i < 4; i++) {
                params.add("%" + searchTerm + "%");
            }
        }
        return params;
    }

    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i) instanceof Integer) {
                stmt.setInt(i + 1, (Integer) params.get(i));
            } else {
                stmt.setString(i + 1, (String) params.get(i));
            }
        }
    }

    public MaterialInventory getLatestInventoryDetails(int materialId, int unitId, String startDate, String endDate) {
        MaterialInventory inventory = null;
        String sql = "SELECT m.Material_id AS material_id, m.Category_id AS category_id, m.Unit_id AS unit_id, " +
                     "m.Name AS material_name, c.Name AS category_name, u.Name AS unit_name, m.Image AS image, " +
                     "(SELECT COALESCE(SUM(imd.Ending_qty), 0) " +
                     " FROM InventoryMaterialDaily imd " +
                     " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
                     " JOIN Quality q ON md2.Quality_id = q.Quality_id " +
                     " WHERE md2.Material_id = m.Material_id AND q.Quality_name = 'available' " +
                     " AND imd.Inventory_Material_date = (SELECT MAX(imd2.Inventory_Material_date) " +
                     "   FROM InventoryMaterialDaily imd2 " +
                     "   JOIN Material_detail md3 ON imd2.Material_detail_id = md3.Material_detail_id " +
                     "   WHERE md3.Material_id = m.Material_id " +
                     (startDate != null && !startDate.isEmpty() ? " AND imd2.Inventory_Material_date >= ? " : "") +
                     (endDate != null && !endDate.isEmpty() ? " AND imd2.Inventory_Material_date <= ? " : "") +
                     ")) AS available_qty, " +
                     "(SELECT COALESCE(SUM(imd.Ending_qty), 0) " +
                     " FROM InventoryMaterialDaily imd " +
                     " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
                     " JOIN Quality q ON md2.Quality_id = q.Quality_id " +
                     " WHERE md2.Material_id = m.Material_id AND q.Quality_name = 'notAvailable' " +
                     " AND imd.Inventory_Material_date = (SELECT MAX(imd2.Inventory_Material_date) " +
                     "   FROM InventoryMaterialDaily imd2 " +
                     "   JOIN Material_detail md3 ON imd2.Material_detail_id = md3.Material_detail_id " +
                     "   WHERE md3.Material_id = m.Material_id " +
                     (startDate != null && !startDate.isEmpty() ? " AND imd2.Inventory_Material_date >= ? " : "") +
                     (endDate != null && !endDate.isEmpty() ? " AND imd2.Inventory_Material_date <= ? " : "") +
                     ")) AS not_available_qty, " +
                     "(SELECT COALESCE(SUM(imd.Import_qty), 0) " +
                     " FROM InventoryMaterialDaily imd " +
                     " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
                     " WHERE md2.Material_id = m.Material_id " +
                     (startDate != null && !startDate.isEmpty() ? " AND imd.Inventory_Material_date >= ? " : "") +
                     (endDate != null && !endDate.isEmpty() ? " AND imd.Inventory_Material_date <= ? " : "") +
                     ") AS total_import, " +
                     "(SELECT COALESCE(SUM(imd.Export_qty), 0) " +
                     " FROM InventoryMaterialDaily imd " +
                     " JOIN Material_detail md2 ON imd.Material_detail_id = md2.Material_detail_id " +
                     " WHERE md2.Material_id = m.Material_id " +
                     (startDate != null && !startDate.isEmpty() ? " AND imd.Inventory_Material_date >= ? " : "") +
                     (endDate != null && !endDate.isEmpty() ? " AND imd.Inventory_Material_date <= ? " : "") +
                     ") AS total_export, " +
                     "MAX(imd.Inventory_Material_date) AS inventory_date, " +
                     "imd.Note AS note " +
                     "FROM Materials m " +
                     "JOIN Material_detail md ON m.Material_id = md.Material_id " +
                     "JOIN Units u ON m.Unit_id = u.Unit_id " +
                     "JOIN Category c ON m.Category_id = c.Category_id " +
                     "LEFT JOIN InventoryMaterialDaily imd ON md.Material_detail_id = imd.Material_detail_id " +
                     "AND imd.Inventory_Material_date = (SELECT MAX(imd2.Inventory_Material_date) " +
                     " FROM InventoryMaterialDaily imd2 " +
                     " JOIN Material_detail md2 ON imd2.Material_detail_id = md2.Material_detail_id " +
                     " WHERE md2.Material_id = m.Material_id " +
                     (startDate != null && !startDate.isEmpty() ? " AND imd2.Inventory_Material_date >= ? " : "") +
                     (endDate != null && !endDate.isEmpty() ? " AND imd2.Inventory_Material_date <= ? " : "") +
                     ") " +
                     "WHERE m.Material_id = ? AND m.Unit_id = ? " +
                     "GROUP BY m.Material_id, m.Category_id, m.Unit_id, m.Name, c.Name, u.Name, m.Image, imd.Note";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            // Parameters for available_qty subquery
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            // Parameters for not_available_qty subquery
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            // Parameters for total_import subquery
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            // Parameters for total_export subquery
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            // Parameters for the last_updated subquery
            if (startDate != null && !startDate.isEmpty()) stmt.setString(paramIndex++, startDate);
            if (endDate != null && !endDate.isEmpty()) stmt.setString(paramIndex++, endDate);
            // Main query parameters
            stmt.setInt(paramIndex++, materialId);
            stmt.setInt(paramIndex++, unitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inventory = new MaterialInventory();
                    inventory.setMaterialId(rs.getInt("material_id"));
                    inventory.setCategoryId(rs.getInt("category_id"));
                    inventory.setUnitId(rs.getInt("unit_id"));
                    inventory.setMaterialName(rs.getString("material_name"));
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventory.setUnitName(rs.getString("unit_name"));
                    inventory.setImage(rs.getString("image"));
                    inventory.setAvailableQty(rs.getBigDecimal("available_qty") != null ? rs.getBigDecimal("available_qty") : BigDecimal.ZERO);
                    inventory.setNotAvailableQty(rs.getBigDecimal("not_available_qty") != null ? rs.getBigDecimal("not_available_qty") : BigDecimal.ZERO);
                    inventory.setImportQty(rs.getBigDecimal("total_import") != null ? rs.getBigDecimal("total_import") : BigDecimal.ZERO);
                    inventory.setExportQty(rs.getBigDecimal("total_export") != null ? rs.getBigDecimal("total_export") : BigDecimal.ZERO);
                    inventory.setInventoryDate(rs.getDate("inventory_date"));
                    inventory.setNote(rs.getString("note") != null ? rs.getString("note") : "No recent transactions");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching latest inventory details: " + e.getMessage());
            e.printStackTrace();
        }
        return inventory;
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT Category_id, Name, Status FROM Category WHERE Status = 'active' ORDER BY Name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("Category_id"));
                category.setName(rs.getString("Name"));
                category.setStatus(rs.getString("Status"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    public List<Quality> getActiveQualities() {
        List<Quality> qualities = new ArrayList<>();
        String sql = "SELECT Quality_id, Quality_name, Status FROM Quality WHERE Status = 'active' ORDER BY Quality_name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Quality quality = new Quality();
                quality.setQualityId(rs.getInt("Quality_id"));
                quality.setQualityName(rs.getString("Quality_name"));
                quality.setStatus(rs.getString("Status"));
                qualities.add(quality);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching qualities: " + e.getMessage());
            e.printStackTrace();
        }
        return qualities;
    }
}