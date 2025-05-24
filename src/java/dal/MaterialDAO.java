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
        String query = "SELECT m.Material_id, m.Name, m.Unit_of_calculation, m.Inventory_quantity, " +
                      "m.Category_id, c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "m.Unit_id, u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setName(rs.getString("Name"));
                material.setUnitOfCalculation(rs.getString("Unit_of_calculation"));
                material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                material.setCategoryId(rs.getInt("Category_id"));
                material.setCategoryName(rs.getString("Category_name"));
                material.setParentCategoryName(rs.getString("Parent_category_name"));
                material.setUnitId(rs.getInt("Unit_id"));
                material.setUnitName(rs.getString("Unit_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    // Phương thức mới: Lấy danh sách Material theo trang
    public List<Material> getMaterialsByPage(int page, int pageSize) {
        List<Material> materials = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String query = "SELECT m.Material_id, m.Name, m.Unit_of_calculation, m.Inventory_quantity, " +
                      "m.Category_id, c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "m.Unit_id, u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setName(rs.getString("Name"));
                    material.setUnitOfCalculation(rs.getString("Unit_of_calculation"));
                    material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setUnitName(rs.getString("Unit_name"));
                    materials.add(material);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    // Phương thức mới: Lấy tổng số Material
    public int getTotalMaterials() {
        String query = "SELECT COUNT(*) FROM Material";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Material getMaterialById(int materialId) {
        Material material = null;
        String query = "SELECT m.Material_id, m.Name, m.Unit_of_calculation, m.Inventory_quantity, " +
                      "m.Category_id, c.Name AS Category_name, p.Name AS Parent_category_name, " +
                      "m.Unit_id, u.Name AS Unit_name " +
                      "FROM Material m " +
                      "JOIN Category c ON m.Category_id = c.Category_id " +
                      "LEFT JOIN Category p ON c.Parent_id = p.Category_id " +
                      "JOIN Unit u ON m.Unit_id = u.Unit_id " +
                      "WHERE m.Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setName(rs.getString("Name"));
                    material.setUnitOfCalculation(rs.getString("Unit_of_calculation"));
                    material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setCategoryName(rs.getString("Category_name"));
                    material.setParentCategoryName(rs.getString("Parent_category_name"));
                    material.setUnitId(rs.getInt("Unit_id"));
                    material.setUnitName(rs.getString("Unit_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return material;
    }

    public void addMaterial(Material material) {
        String query = "INSERT INTO Material (Category_id, Unit_id, Name, Unit_of_calculation, Inventory_quantity) " +
                      "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getUnitId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getUnitOfCalculation());
            ps.setInt(5, material.getInventoryQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaterial(Material material) {
        String query = "UPDATE Material SET Category_id = ?, Unit_id = ?, Name = ?, Unit_of_calculation = ?, " +
                      "Inventory_quantity = ? WHERE Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getUnitId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getUnitOfCalculation());
            ps.setInt(5, material.getInventoryQuantity());
            ps.setInt(6, material.getMaterialId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMaterial(int materialId) {
        String query = "DELETE FROM Material WHERE Material_id = ?";
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
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> allCategories = new ArrayList<>();
        
        List<Category> parentCategories = categoryDAO.getAllParentCategory();
        List<Category> subCategories = categoryDAO.getAllSubCategory();
        
        allCategories.addAll(parentCategories);
        allCategories.addAll(subCategories);
        return allCategories;
    }
}