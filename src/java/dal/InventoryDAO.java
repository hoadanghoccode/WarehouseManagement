package dal;

import model.Category;
import model.MaterialInventory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends DBContext {

    public List<MaterialInventory> getInventory(int warehouseId, int categoryId, String searchTerm, String sortBy) {
        List<MaterialInventory> inventoryList = new ArrayList<>();
        String sql = buildInventoryQuery(warehouseId, categoryId, searchTerm, sortBy);
        List<Object> params = buildInventoryParameters(warehouseId, categoryId, searchTerm);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaterialInventory inventory = new MaterialInventory(
                        rs.getInt("material_id"),
                        rs.getInt("category_id"),
                        rs.getString("material_name"),
                        rs.getString("unit_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getTimestamp("last_updated"),
                        rs.getInt("warehouse_id")
                    );
                    inventory.setCategoryName(rs.getString("category_name"));
                    inventoryList.add(inventory);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
            e.printStackTrace();
        }
        return inventoryList;
    }

    private String buildInventoryQuery(int warehouseId, int categoryId, String searchTerm, String sortBy) {
        StringBuilder sql = new StringBuilder(
            "SELECT md.material_id, m.category_id, m.name AS material_name, u.name AS unit_name, " +
            "md.quantity, md.last_updated, COALESCE(i.warehouse_id, 0) AS warehouse_id, c.name AS category_name " +
            "FROM material_detail md " +
            "JOIN materials m ON md.material_id = m.material_id " +
            "JOIN units u ON md.unit_id = u.unit_id " +
            "LEFT JOIN import_note_detail ind ON md.material_id = ind.material_id " +
            "LEFT JOIN import_note i ON ind.import_note_id = i.import_note_id " +
            "JOIN category c ON m.category_id = c.category_id " +
            "WHERE c.is_Active = TRUE AND m.is_Active = TRUE "
        );

        if (warehouseId > 0) {
            sql.append("AND i.warehouse_id = ? ");
        }
        if (categoryId > 0) {
            sql.append("AND m.category_id = ? ");
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append("AND (m.material_id LIKE ? OR m.name LIKE ? OR c.name LIKE ?) ");
        }

        String safeSortBy = "md.quantity ASC";
        if (sortBy != null && sortBy.matches("^(material_id|material_name|quantity) (ASC|DESC)$")) {
            safeSortBy = sortBy;
        }
        sql.append("ORDER BY ").append(safeSortBy);

        return sql.toString();
    }

    private List<Object> buildInventoryParameters(int warehouseId, int categoryId, String searchTerm) {
        List<Object> params = new ArrayList<>();
        if (warehouseId > 0) {
            params.add(warehouseId);
        }
        if (categoryId > 0) {
            params.add(categoryId);
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
        }
        return params;
    }

    public List<Category> getActiveCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT Category_id, Name FROM Category WHERE is_Active = TRUE ORDER BY Name ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("Category_id"));
                category.setName(rs.getString("Name"));
                category.setStatus("Active");
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
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
}