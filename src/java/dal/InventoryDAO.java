package dal;

import model.Category;
import model.MaterialInventory;
import model.Quality;
import model.SubUnit;
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

        System.out.println("SQL Query: " + sql);
        System.out.println("Parameters: " + params);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    MaterialInventory inventory = new MaterialInventory();
                    inventory.setMaterialId(rs.getInt("material_id"));
                    inventory.setCategoryId(rs.getInt("category_id"));
                    inventory.setSupplierId(rs.getInt("supplier_id"));
                    inventory.setSubUnitId(rs.getInt("subunit_id"));
                    inventory.setQualityId(rs.getInt("quality_id"));
                    inventory.setMaterialName(rs.getString("material_name"));
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventory.setSupplierName(rs.getString("supplier_name"));
                    inventory.setSubUnitName(rs.getString("subunit_name"));
                    inventory.setQualityName(rs.getString("quality_name") != null ? rs.getString("quality_name") : "N/A");
                    inventory.setClosingQty(rs.getInt("closing_qty"));
                    inventory.setOpeningQty(rs.getInt("opening_qty"));
                    inventory.setImportQty(rs.getInt("import_qty"));
                    inventory.setExportQty(rs.getInt("export_qty"));
                    inventory.setDamagedQuantity(rs.getBigDecimal("damaged_quantity") != null ? rs.getBigDecimal("damaged_quantity") : BigDecimal.ZERO);
                    inventory.setInventoryDate(rs.getDate("inventory_date"));
                    inventory.setNote(rs.getString("note"));
                    inventoryList.add(inventory);
                    rowCount++;
                }
                System.out.println("Rows fetched: " + rowCount);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Inventory List Size: " + inventoryList.size());
        return inventoryList;
    }

    private String buildInventoryQuery(int categoryId, int supplierId, int qualityId, String searchTerm, String sortBy) {
        StringBuilder sql = new StringBuilder(
            "SELECT imd.Material_id AS material_id, m.Category_id AS category_id, m.SupplierId AS supplier_id, imd.SubUnit_id AS subunit_id, md.Quality_id AS quality_id, " +
            "m.Name AS material_name, c.Name AS category_name, s.Name AS supplier_name, su.Name AS subunit_name, " +
            "q.Quality_name AS quality_name, imd.Closing_qty AS closing_qty, imd.Opening_qty AS opening_qty, imd.Import_qty AS import_qty, imd.Export_qty AS export_qty, md.Quantity AS damaged_quantity, " +
            "imd.Inventory_Material_date AS inventory_date, imd.Note AS note " +
            "FROM InventoryMaterialDaily imd " +
            "JOIN Materials m ON imd.Material_id = m.Material_id " +
            "JOIN SubUnits su ON imd.SubUnit_id = su.SubUnit_id " +
            "JOIN Category c ON m.Category_id = c.Category_id " +
            "JOIN Suppliers s ON m.SupplierId = s.Supplier_id " +
            "LEFT JOIN Material_detail md ON imd.Material_id = md.Material_id AND imd.SubUnit_id = md.SubUnit_id " +
            "LEFT JOIN Quality q ON md.Quality_id = q.Quality_id " +
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
            sql.append(" AND (CAST(m.Material_id AS CHAR) LIKE ? OR m.Name LIKE ? OR c.Name LIKE ? OR s.Name LIKE ? OR su.Name LIKE ? OR q.Quality_name LIKE ? OR CAST(imd.Closing_qty AS CHAR) LIKE ? OR CAST(imd.Inventory_Material_date AS CHAR) LIKE ?) ");
        }

        String safeSortBy = "imd.Closing_qty ASC";
        if (sortBy != null && sortBy.matches("^(material_id|material_name|closing_qty) (ASC|DESC)$")) {
            safeSortBy = sortBy.replace("material_name", "m.Name");
        }
        sql.append(" ORDER BY imd.Inventory_Material_date DESC, ").append(safeSortBy);

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
            for (int i = 0; i < 8; i++) {
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
            System.out.println("Categories fetched: " + categories.size());
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
            System.out.println("Suppliers fetched: " + suppliers.size());
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
            System.out.println("Qualities fetched: " + qualities.size());
        } catch (SQLException e) {
            System.err.println("Error fetching qualities: " + e.getMessage());
            e.printStackTrace();
        }
        return qualities;
    }

    public List<SubUnit> getActiveSubUnits() {
        List<SubUnit> subUnits = new ArrayList<>();
        String sql = "SELECT SubUnit_id, Name, Status FROM SubUnits WHERE Status = 'active' ORDER BY Name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SubUnit unit = new SubUnit();
                unit.setSubUnitId(rs.getInt("SubUnit_id"));
                unit.setName(rs.getString("Name"));
                unit.setStatus(rs.getString("Status"));
                subUnits.add(unit);
            }
            System.out.println("SubUnits fetched: " + subUnits.size());
        } catch (SQLException e) {
            System.err.println("Error fetching subunits: " + e.getMessage());
            e.printStackTrace();
        }
        return subUnits;
    }
}