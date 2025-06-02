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
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, s.Id AS Supplier_id, s.Name AS Supplier_name, s.Status AS Supplier_status FROM Material m LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setCategoryId(rs.getInt("Category_id"));
                material.setName(rs.getString("Name"));
                material.setStatus(rs.getString("Status"));
                material.setSupplierId(rs.getInt("Supplier_id"));
                material.setSupplierName(rs.getString("Supplier_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public List<Material> getMaterialsByPage(int page, int pageSize, String search, String categoryFilter) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, s.Id AS Supplier_id, s.Name AS Supplier_name, s.Status AS Supplier_status FROM Material m LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE (? IS NULL OR m.Name LIKE ?) AND (? IS NULL OR m.Category_id = (SELECT Category_id FROM Category WHERE Name = ?)) LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, categoryFilter);
            ps.setString(4, categoryFilter);
            ps.setInt(5, pageSize);
            ps.setInt(6, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setName(rs.getString("Name"));
                    material.setStatus(rs.getString("Status"));
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

    public int getTotalMaterials(String search, String categoryFilter) {
        String query = "SELECT COUNT(*) FROM Material m LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE (? IS NULL OR m.Name LIKE ?) AND (? IS NULL OR m.Category_id = (SELECT Category_id FROM Category WHERE Name = ?))";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, categoryFilter);
            ps.setString(4, categoryFilter);
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
        String query = "SELECT m.Material_id, m.Category_id, m.Name, m.Status, s.Id AS Supplier_id, s.Name AS Supplier_name, s.Status AS Supplier_status FROM Material m LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId LEFT JOIN Supplier s ON sm.SupplierId = s.Id WHERE m.Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setName(rs.getString("Name"));
                    material.setStatus(rs.getString("Status"));
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
        String querySupplierMaterial = "INSERT INTO SupplierMaterial (SupplierId, MaterialId) VALUES (?, ?) ON DUPLICATE KEY UPDATE SupplierId = VALUES(SupplierId)";
        try {
            try (PreparedStatement psMaterial = connection.prepareStatement(queryMaterial)) {
                psMaterial.setInt(1, material.getCategoryId());
                psMaterial.setString(2, material.getName());
                psMaterial.setString(3, material.getStatus());
                psMaterial.setInt(4, material.getMaterialId());
                psMaterial.executeUpdate();
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