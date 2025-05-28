/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author legia
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Material;
import model.Unit;
import model.Category;

public class MaterialDAO extends DBContext {
    private Connection connection;

    public MaterialDAO() {
        try {
            connection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.Material_id, m.Category_id, m.Unit_id, m.Name, m.Description, m.Inventory_quantity, " +
                      "m.Price, m.Image, m.Quality, m.Status, m.Created_at, m.Updated_at, " +
                      "c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "WHERE m.Status = 'active' AND c.Status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setCategoryId(rs.getInt("Category_id"));
                material.setUnitId(rs.getInt("Unit_id"));
                material.setName(rs.getString("Name"));
                material.setDescription(rs.getString("Description"));
                material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                material.setPrice(rs.getDouble("Price"));
                material.setImage(rs.getString("Image"));
                material.setQuality(rs.getString("Quality"));
                material.setStatus(rs.getString("Status"));
                material.setCreatedAt(rs.getTimestamp("Created_at"));
                material.setUpdatedAt(rs.getTimestamp("Updated_at"));
                material.setCategoryName(rs.getString("Category_name"));
                material.setParentCategoryName(rs.getString("Parent_category_name"));
                material.setUnitName(rs.getString("Unit_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public List<Material> getMaterialsByPage(int page, int pageSize, String search, String categoryFilter, Integer quantityMin, Integer quantityMax) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.Material_id, m.Category_id, m.Unit_id, m.Name, m.Description, m.Inventory_quantity, " +
                      "m.Price, m.Image, m.Quality, m.Status, m.Created_at, m.Updated_at, " +
                      "c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "WHERE m.Status = 'active' AND c.Status = 'active' " +
                      "AND (? IS NULL OR m.Name LIKE ? OR u.Name LIKE ? OR c.Name LIKE ? OR (p.Name LIKE ?)) " +
                      "AND (? IS NULL OR c.Name = ? OR (p.Name = ?)) " +
                      "AND (? IS NULL OR m.Inventory_quantity >= ?) " +
                      "AND (? IS NULL OR m.Inventory_quantity <= ?) " +
                      "LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ps.setString(5, "%" + search + "%");
            ps.setString(6, categoryFilter);
            ps.setString(7, categoryFilter);
            ps.setString(8, categoryFilter);
            ps.setObject(9, quantityMin);
            ps.setObject(10, quantityMin);
            ps.setObject(11, quantityMax);
            ps.setObject(12, quantityMax);
            ps.setInt(13, pageSize);
            ps.setInt(14, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setName(rs.getString("Name"));
                    material.setDescription(rs.getString("Description"));
                    material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                    material.setPrice(rs.getDouble("Price"));
                    material.setImage(rs.getString("Image"));
                    material.setQuality(rs.getString("Quality"));
                    material.setStatus(rs.getString("Status"));
                    material.setCreatedAt(rs.getTimestamp("Created_at"));
                    material.setUpdatedAt(rs.getTimestamp("Updated_at"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitName(rs.getString("Unit_name"));
                    materials.add(material);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public int getTotalMaterials(String search, String categoryFilter, Integer quantityMin, Integer quantityMax) {
        String query = "SELECT COUNT(*) FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "WHERE m.Status = 'active' AND c.Status = 'active' " +
                      "AND (? IS NULL OR m.Name LIKE ? OR u.Name LIKE ? OR c.Name LIKE ? OR (p.Name LIKE ?)) " +
                      "AND (? IS NULL OR c.Name = ? OR (p.Name = ?)) " +
                      "AND (? IS NULL OR m.Inventory_quantity >= ?) " +
                      "AND (? IS NULL OR m.Inventory_quantity <= ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ps.setString(5, "%" + search + "%");
            ps.setString(6, categoryFilter);
            ps.setString(7, categoryFilter);
            ps.setString(8, categoryFilter);
            ps.setObject(9, quantityMin);
            ps.setObject(10, quantityMin);
            ps.setObject(11, quantityMax);
            ps.setObject(12, quantityMax);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Material getMaterialById(int materialId) {
        Material material = null;
        String query = "SELECT m.Material_id, m.Category_id, m.Unit_id, m.Name, m.Description, m.Inventory_quantity, " +
                      "m.Price, m.Image, m.Quality, m.Status, m.Created_at, m.Updated_at, " +
                      "c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "WHERE m.Material_id = ? AND m.Status = 'active' AND c.Status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setName(rs.getString("Name"));
                    material.setDescription(rs.getString("Description"));
                    material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                    material.setPrice(rs.getDouble("Price"));
                    material.setImage(rs.getString("Image"));
                    material.setQuality(rs.getString("Quality"));
                    material.setStatus(rs.getString("Status"));
                    material.setCreatedAt(rs.getTimestamp("Created_at"));
                    material.setUpdatedAt(rs.getTimestamp("Updated_at"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitName(rs.getString("Unit_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return material;
    }

    public void addMaterial(Material material) {
        String query = "INSERT INTO Material (Category_id, Unit_id, Name, Description, Inventory_quantity, Price, Image, Quality, Status, Created_at, Updated_at) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getUnitId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getDescription());
            ps.setInt(5, material.getInventoryQuantity());
            ps.setDouble(6, material.getPrice());
            ps.setString(7, material.getImage());
            ps.setString(8, material.getQuality());
            ps.setString(9, material.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaterial(Material material) {
        String query = "UPDATE Material SET Category_id = ?, Unit_id = ?, Name = ?, Description = ?, Inventory_quantity = ?, " +
                      "Price = ?, Image = ?, Quality = ?, Status = ?, Updated_at = NOW() WHERE Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getUnitId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getDescription());
            ps.setInt(5, material.getInventoryQuantity());
            ps.setDouble(6, material.getPrice());
            ps.setString(7, material.getImage());
            ps.setString(8, material.getQuality());
            ps.setString(9, material.getStatus());
            ps.setInt(10, material.getMaterialId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMaterial(int materialId) {
        String query = "UPDATE Material SET Status = 'inactive', Updated_at = NOW() WHERE Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        String query = "SELECT Unit_id, Name FROM Unit";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitId(rs.getInt("Unit_id"));
                unit.setName(rs.getString("Name"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT Category_id, Name, Parent_id FROM Category WHERE Status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("Category_id"));
                category.setName(rs.getString("Name"));
                category.setParentId(rs.getInt("Parent_id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}