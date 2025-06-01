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
import model.Supplier;

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
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, c.Name AS Category_name, p.Name AS Parent_category_name, mu.Unit_id, u.Name AS Unit_name, mu.price, mi.Quantity, s.Id AS Supplier_id, s.Name AS Supplier_name FROM Material m JOIN Category c ON m.Category_id = c.Category_id LEFT JOIN Category p ON c.Parent_id = p.Category_id LEFT JOIN Material_Unit_Price mu ON m.Material_id = mu.Material_id LEFT JOIN Unit u ON mu.Unit_id = u.Unit_id LEFT JOIN MaterialInventory mi ON m.Material_id = mi.Material_id AND mu.Unit_id = mi.Unit_id LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setCategoryId(rs.getInt("Category_id"));
                material.setName(rs.getString("Name"));
                material.setStatus(rs.getString("Status"));
                material.setCategoryName(rs.getString("Category_name"));
                material.setParentCategoryName(rs.getString("Parent_category_name"));
                material.setUnitId(rs.getInt("Unit_id"));
                material.setUnitName(rs.getString("Unit_name"));
                material.setPrice(rs.getDouble("price"));
                material.setQuantity(rs.getDouble("Quantity"));
                material.setSupplierId(rs.getInt("Supplier_id"));
                material.setSupplierName(rs.getString("Supplier_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public List<Material> getMaterialsByPage(int page, int pageSize, String search, String categoryFilter, Double quantityMin, Double quantityMax) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, c.Name AS Category_name, p.Name AS Parent_category_name, mu.Unit_id, u.Name AS Unit_name, mu.price, mi.Quantity, s.Id AS Supplier_id, s.Name AS Supplier_name FROM Material m JOIN Category c ON m.Category_id = c.Category_id LEFT JOIN Category p ON c.Parent_id = p.Category_id LEFT JOIN Material_Unit_Price mu ON m.Material_id = mu.Material_id LEFT JOIN Unit u ON mu.Unit_id = u.Unit_id LEFT JOIN MaterialInventory mi ON m.Material_id = mi.Material_id AND mu.Unit_id = mi.Unit_id LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE (? IS NULL OR m.Name LIKE ? OR u.Name LIKE ? OR c.Name LIKE ? OR (p.Name LIKE ?) OR s.Name LIKE ?) AND (? IS NULL OR c.Name = ? OR (p.Name = ?)) AND (? IS NULL OR mi.Quantity >= ?) AND (? IS NULL OR mi.Quantity <= ?) LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ps.setString(5, "%" + search + "%");
            ps.setString(6, "%" + search + "%");
            ps.setString(7, categoryFilter);
            ps.setString(8, categoryFilter);
            ps.setString(9, categoryFilter);
            ps.setObject(10, quantityMin);
            ps.setObject(11, quantityMin);
            ps.setObject(12, quantityMax);
            ps.setObject(13, quantityMax);
            ps.setInt(14, pageSize);
            ps.setInt(15, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setName(rs.getString("Name"));
                    material.setStatus(rs.getString("Status"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setUnitName(rs.getString("Unit_name"));
                    material.setPrice(rs.getDouble("price"));
                    material.setQuantity(rs.getDouble("Quantity"));
                    material.setSupplierId(rs.getInt("Supplier_id"));
                    material.setSupplierName(rs.getString("Supplier_name"));
                    materials.add(material);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public int getTotalMaterials(String search, String categoryFilter, Double quantityMin, Double quantityMax) {
        String query = "SELECT COUNT(*) FROM Material m JOIN Category c ON m.Category_id = c.Category_id LEFT JOIN Category p ON c.Parent_id = p.Category_id LEFT JOIN Material_Unit_Price mu ON m.Material_id = mu.Material_id LEFT JOIN Unit u ON mu.Unit_id = u.Unit_id LEFT JOIN MaterialInventory mi ON m.Material_id = mi.Material_id AND mu.Unit_id = mi.Unit_id LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE (? IS NULL OR m.Name LIKE ? OR u.Name LIKE ? OR c.Name LIKE ? OR (p.Name LIKE ?) OR s.Name LIKE ?) AND (? IS NULL OR c.Name = ? OR (p.Name = ?)) AND (? IS NULL OR mi.Quantity >= ?) AND (? IS NULL OR mi.Quantity <= ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ps.setString(5, "%" + search + "%");
            ps.setString(6, "%" + search + "%");
            ps.setString(7, categoryFilter);
            ps.setString(8, categoryFilter);
            ps.setString(9, categoryFilter);
            ps.setObject(10, quantityMin);
            ps.setObject(11, quantityMin);
            ps.setObject(12, quantityMax);
            ps.setObject(13, quantityMax);
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
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, c.Name AS Category_name, p.Name AS Parent_category_name, mu.Unit_id, u.Name AS Unit_name, mu.price, mi.Quantity, s.Id AS Supplier_id, s.Name AS Supplier_name FROM Material m JOIN Category c ON m.Category_id = c.Category_id LEFT JOIN Category p ON c.Parent_id = p.Category_id LEFT JOIN Material_Unit_Price mu ON m.Material_id = mu.Material_id LEFT JOIN Unit u ON mu.Unit_id = u.Unit_id LEFT JOIN MaterialInventory mi ON m.Material_id = mi.Material_id AND mu.Unit_id = mi.Unit_id LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE m.Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setName(rs.getString("Name"));
                    material.setStatus(rs.getString("Status"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setUnitName(rs.getString("Unit_name"));
                    material.setPrice(rs.getDouble("price"));
                    material.setQuantity(rs.getDouble("Quantity"));
                    material.setSupplierId(rs.getInt("Supplier_id"));
                    material.setSupplierName(rs.getString("Supplier_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return material;
    }

    public void addMaterial(Material material) {
        String queryMaterial = "INSERT INTO Material (Category_id, Name, Status) VALUES (?, ?, ?)";
        String queryUnitPrice = "INSERT INTO Material_Unit_Price (Material_id, Unit_id, price) VALUES (?, ?, ?)";
        String queryInventory = "INSERT INTO MaterialInventory (Material_id, Unit_id, Quantity) VALUES (?, ?, ?)";
        String querySupplierMaterial = "INSERT INTO SupplierMaterial (SupplierId, MaterialId) VALUES (?, ?) ON DUPLICATE KEY UPDATE SupplierId = SupplierId";
        try {
            try (PreparedStatement psMaterial = connection.prepareStatement(queryMaterial, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psMaterial.setInt(1, material.getCategoryId());
                psMaterial.setString(2, material.getName());
                psMaterial.setString(3, material.getStatus());
                psMaterial.executeUpdate();
                ResultSet rs = psMaterial.getGeneratedKeys();
                if (rs.next()) {
                    int materialId = rs.getInt(1);
                    try (PreparedStatement psUnitPrice = connection.prepareStatement(queryUnitPrice)) {
                        psUnitPrice.setInt(1, materialId);
                        psUnitPrice.setInt(2, material.getUnitId());
                        psUnitPrice.setDouble(3, material.getPrice());
                        psUnitPrice.executeUpdate();
                    }
                    try (PreparedStatement psInventory = connection.prepareStatement(queryInventory)) {
                        psInventory.setInt(1, materialId);
                        psInventory.setInt(2, material.getUnitId());
                        psInventory.setDouble(3, material.getQuantity());
                        psInventory.executeUpdate();
                    }
                    if (material.getSupplierId() != 0) {
                        try (PreparedStatement psSupplierMaterial = connection.prepareStatement(querySupplierMaterial)) {
                            psSupplierMaterial.setInt(1, material.getSupplierId());
                            psSupplierMaterial.setInt(2, materialId);
                            psSupplierMaterial.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaterial(Material material) {
        String queryMaterial = "UPDATE Material SET Category_id = ?, Name = ?, Status = ? WHERE Material_id = ?";
        String queryUnitPrice = "INSERT INTO Material_Unit_Price (Material_id, Unit_id, price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE price = VALUES(price)";
        String queryInventory = "INSERT INTO MaterialInventory (Material_id, Unit_id, Quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE Quantity = VALUES(Quantity)";
        String querySupplierMaterial = "INSERT INTO SupplierMaterial (SupplierId, MaterialId) VALUES (?, ?) ON DUPLICATE KEY UPDATE SupplierId = VALUES(SupplierId)";
        try {
            try (PreparedStatement psMaterial = connection.prepareStatement(queryMaterial)) {
                psMaterial.setInt(1, material.getCategoryId());
                psMaterial.setString(2, material.getName());
                psMaterial.setString(3, material.getStatus());
                psMaterial.setInt(4, material.getMaterialId());
                psMaterial.executeUpdate();
            }
            try (PreparedStatement psUnitPrice = connection.prepareStatement(queryUnitPrice)) {
                psUnitPrice.setInt(1, material.getMaterialId());
                psUnitPrice.setInt(2, material.getUnitId());
                psUnitPrice.setDouble(3, material.getPrice());
                psUnitPrice.executeUpdate();
            }
            try (PreparedStatement psInventory = connection.prepareStatement(queryInventory)) {
                psInventory.setInt(1, material.getMaterialId());
                psInventory.setInt(2, material.getUnitId());
                psInventory.setDouble(3, material.getQuantity());
                psInventory.executeUpdate();
            }
            if (material.getSupplierId() != 0) {
                try (PreparedStatement psSupplierMaterial = connection.prepareStatement(querySupplierMaterial)) {
                    psSupplierMaterial.setInt(1, material.getSupplierId());
                    psSupplierMaterial.setInt(2, material.getMaterialId());
                    psSupplierMaterial.executeUpdate();
                }
            } else {
                String deleteQuery = "DELETE FROM SupplierMaterial WHERE MaterialId = ?";
                try (PreparedStatement psDelete = connection.prepareStatement(deleteQuery)) {
                    psDelete.setInt(1, material.getMaterialId());
                    psDelete.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMaterial(int materialId) throws SQLException {
        String queryMaterial = "UPDATE Material SET Status = 'inactive' WHERE Material_id = ?";
        String querySupplierMaterial = "DELETE FROM SupplierMaterial WHERE MaterialId = ?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement psMaterial = connection.prepareStatement(queryMaterial)) {
                psMaterial.setInt(1, materialId);
                psMaterial.executeUpdate();
            }
            try (PreparedStatement psSupplierMaterial = connection.prepareStatement(querySupplierMaterial)) {
                psSupplierMaterial.setInt(1, materialId);
                psSupplierMaterial.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        String query = "SELECT Unit_id, Name, Status FROM Unit";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setUnitId(rs.getInt("Unit_id"));
                unit.setName(rs.getString("Name"));
                unit.setStatus(rs.getString("Status"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("Category_id"));
                category.setName(rs.getString("Name"));
                int parentId = rs.getInt("Parent_id");
                if (!rs.wasNull()) {
                    category.setParentId(new CategoryDAO().getCategoryById(parentId));
                } else {
                    category.setParentId(null);
                }
                category.setStatus(rs.getString("Status"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT Id, Name, Status FROM Supplier";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("Id"));
                supplier.setName(rs.getString("Name"));
                supplier.setStatus(rs.getString("Status"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}