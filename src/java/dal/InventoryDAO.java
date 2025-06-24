package dal;

import model.Category;
import model.MaterialInventory;
import model.Quality;
import model.Supplier;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends DBContext {

    public List<MaterialInventory> getInventory(int categoryId, int supplierId, int qualityId, String searchTerm, String sortBy) {
        List<MaterialInventory> inventoryList = new ArrayList<>();
        String sql = buildInventoryQuery(categoryId, supplierId, qualityId, searchTerm, sortBy);
        List<Object> params = buildInventoryParameters(categoryId, supplierId, qualityId, searchTerm);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaterialInventory inventory = new MaterialInventory();
                    inventory.setMaterialId(rs.getInt("material_id"));
                    inventory.setCategoryId(rs.getInt("category_id"));
                    inventory.setSupplierId(rs.getInt("supplier_id"));
                    inventory.setSubUnitId(rs.getInt("subunit_id"));
                    inventory.setMaterialName(rs.getString("material_name"));
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventory.setSupplierName(rs.getString("supplier_name"));
                    inventory.setSubUnitName(rs.getString("subunit_name"));
                    inventory.setAvailableQty(rs.getBigDecimal("available_qty") != null ? rs.getBigDecimal("available_qty") : BigDecimal.ZERO);
                    inventory.setNotAvailableQty(rs.getBigDecimal("not_available_qty") != null ? rs.getBigDecimal("not_available_qty") : BigDecimal.ZERO);
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

    private String buildInventoryQuery(int categoryId, int supplierId, int qualityId, String searchTerm, String sortBy) {
        StringBuilder sql = new StringBuilder(
            "SELECT m.Material_id AS material_id, m.Category_id AS category_id, m.SupplierId AS supplier_id, md.SubUnit_id AS subunit_id, " +
            "m.Name AS material_name, c.Name AS category_name, s.Name AS supplier_name, su.Name AS subunit_name, " +
            "SUM(CASE WHEN q.Quality_name = 'available' THEN md.Quantity ELSE 0 END) AS available_qty, " +
            "SUM(CASE WHEN q.Quality_name = 'notAvailable' THEN md.Quantity ELSE 0 END) AS not_available_qty, " +
            "MAX(md.Last_updated) AS last_updated, NULL AS note " +
            "FROM Materials m " +
            "JOIN Material_detail md ON m.Material_id = md.Material_id " +
            "JOIN SubUnits su ON md.SubUnit_id = su.SubUnit_id " +
            "JOIN Category c ON m.Category_id = c.Category_id " +
            "JOIN Suppliers s ON m.SupplierId = s.Supplier_id " +
            "JOIN Quality q ON md.Quality_id = q.Quality_id " +
            "WHERE m.Status = 'active' AND su.Status = 'active' AND c.Status = 'active' AND s.Status = 'active' "
        );

        if (categoryId > 0) {
            sql.append(" AND m.Category_id = ? ");
        }
        if (supplierId > 0) {
            sql.append(" AND m.SupplierId = ? ");
        }
        if (qualityId > 0) {
            sql.append(" AND md.Quality_id = ? ");
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (CAST(m.Material_id AS CHAR) LIKE ? OR m.Name LIKE ? OR c.Name LIKE ? OR s.Name LIKE ? OR su.Name LIKE ?) ");
        }

        sql.append(" GROUP BY m.Material_id, m.Category_id, m.SupplierId, md.SubUnit_id, m.Name, c.Name, s.Name, su.Name ");

        String safeSortBy = "available_qty DESC";
        if (sortBy != null && sortBy.matches("^(material_id|material_name|available_qty|not_available_qty) (ASC|DESC)$")) {
            safeSortBy = sortBy.replace("material_name", "m.Name").replace("available_qty", "available_qty").replace("not_available_qty", "not_available_qty");
        }
        sql.append(" ORDER BY ").append(safeSortBy);

        return sql.toString();
    }

    private List<Object> buildInventoryParameters(int categoryId, int supplierId, int qualityId, String searchTerm) {
        List<Object> params = new ArrayList<>();
        if (categoryId > 0) {
            params.add(categoryId);
        }
        if (supplierId > 0) {
            params.add(supplierId);
        }
        if (qualityId > 0) {
            params.add(qualityId);
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            for (int i = 0; i < 5; i++) {
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

    public MaterialInventory getLatestInventoryDetails(int materialId, int subUnitId) {
        MaterialInventory inventory = null;
        String sql = "SELECT imd.Material_detail_id, m.Material_id AS material_id, m.Category_id AS category_id, m.SupplierId AS supplier_id, imd.SubUnit_id AS subunit_id, " +
                     "m.Name AS material_name, c.Name AS category_name, s.Name AS supplier_name, su.Name AS subunit_name, " +
                     "imd.Ending_qty AS closing_qty, imd.Import_qty AS import_qty, imd.Export_qty AS export_qty, " +
                     "imd.Inventory_Material_date AS inventory_date, imd.Note AS note, " +
                     "(SELECT md.Quantity FROM Material_detail md JOIN Quality q ON md.Quality_id = q.Quality_id " +
                     "WHERE md.Material_id = m.Material_id AND md.SubUnit_id = imd.SubUnit_id AND q.Quality_name = 'notAvailable' LIMIT 1) AS damaged_quantity " +
                     "FROM InventoryMaterialDaily imd " +
                     "JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id " +
                     "JOIN Materials m ON md.Material_id = m.Material_id " +
                     "JOIN SubUnits su ON md.SubUnit_id = su.SubUnit_id " +
                     "JOIN Category c ON m.Category_id = c.Category_id " +
                     "JOIN Suppliers s ON m.SupplierId = s.Supplier_id " +
                     "WHERE m.Material_id = ? AND imd.SubUnit_id = ? " +
                     "ORDER BY imd.Inventory_Material_date DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            stmt.setInt(2, subUnitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inventory = new MaterialInventory();
                    inventory.setMaterialId(rs.getInt("material_id"));
                    inventory.setCategoryId(rs.getInt("category_id"));
                    inventory.setSupplierId(rs.getInt("supplier_id"));
                    inventory.setSubUnitId(rs.getInt("subunit_id"));
                    inventory.setMaterialName(rs.getString("material_name"));
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventory.setSupplierName(rs.getString("supplier_name"));
                    inventory.setSubUnitName(rs.getString("subunit_name"));
                    inventory.setAvailableQty(rs.getBigDecimal("closing_qty") != null ? rs.getBigDecimal("closing_qty") : BigDecimal.ZERO);
                    inventory.setNotAvailableQty(rs.getBigDecimal("damaged_quantity") != null ? rs.getBigDecimal("damaged_quantity") : BigDecimal.ZERO);
                    inventory.setImportQty(rs.getBigDecimal("import_qty") != null ? rs.getBigDecimal("import_qty") : BigDecimal.ZERO);
                    inventory.setExportQty(rs.getBigDecimal("export_qty") != null ? rs.getBigDecimal("export_qty") : BigDecimal.ZERO);
                    inventory.setInventoryDate(rs.getDate("inventory_date"));
                    inventory.setNote(rs.getString("note"));
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

    public List<Supplier> getActiveSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT Supplier_id, Name, Status FROM Suppliers WHERE Status = 'active' ORDER BY Name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("Supplier_id"));
                supplier.setName(rs.getString("Name"));
                supplier.setStatus(rs.getString("Status"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching suppliers: " + e.getMessage());
            e.printStackTrace();
        }
        return suppliers;
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