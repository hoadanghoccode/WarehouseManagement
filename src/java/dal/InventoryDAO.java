package dal;

import model.Category;
import model.MaterialInventory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends DBContext {

    public List<MaterialInventory> getInventory(int categoryId, String searchTerm, String sortBy) {
        List<MaterialInventory> inventoryList = new ArrayList<>();
        String sql = buildInventoryQuery(categoryId, searchTerm, sortBy);
        List<Object> params = buildInventoryParameters(categoryId, searchTerm);

        System.out.println("SQL Query: " + sql);
        System.out.println("Parameters: " + params);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    MaterialInventory inventory = new MaterialInventory(
                        rs.getInt("material_id"),
                        rs.getInt("category_id"),
                        rs.getString("material_name"),
                        rs.getString("subunit_name"),
                        new java.math.BigDecimal(rs.getInt("closing_qty")), // Chuyển INT sang BigDecimal
                        rs.getDate("inventory_date"),
                        rs.getString("category_name")
                    );
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

    private String buildInventoryQuery(int categoryId, String searchTerm, String sortBy) {
        StringBuilder sql = new StringBuilder(
            "SELECT imd.material_id, m.category_id, m.name AS material_name, su.name AS subunit_name, " +
            "imd.closing_qty, imd.inventory_material_date AS inventory_date, c.name AS category_name " +
            "FROM inventorymaterialdaily imd " +
            "JOIN materials m ON imd.material_id = m.material_id " +
            "JOIN subunits su ON imd.subunit_id = su.subunit_id " +
            "JOIN category c ON m.category_id = c.category_id " +
            "WHERE m.status = 'active' AND su.status = 'active' AND c.status = 'active' "
        );

        if (categoryId > 0) {
            sql.append("AND m.category_id = ? ");
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append("AND (CAST(m.material_id AS CHAR) LIKE ? OR m.name LIKE ? OR c.name LIKE ? OR su.name LIKE ?) ");
        }

        String safeSortBy = "imd.closing_qty ASC";
        if (sortBy != null && sortBy.matches("^(material_id|material_name|closing_qty) (ASC|DESC)$")) {
            safeSortBy = sortBy;
        }
        sql.append("ORDER BY imd.inventory_material_date DESC, ").append(safeSortBy);

        return sql.toString();
    }

    private List<Object> buildInventoryParameters(int categoryId, String searchTerm) {
        List<Object> params = new ArrayList<>();
        if (categoryId > 0) {
            params.add(categoryId);
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
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
        String sql = "SELECT category_id, name, status FROM category WHERE status = 'active' ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setStatus(rs.getString("status"));
                categories.add(category);
            }
            System.out.println("Categories fetched: " + categories.size());
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }
}