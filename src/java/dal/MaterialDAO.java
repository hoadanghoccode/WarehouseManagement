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
import model.SubCategory;

public class MaterialDAO {

    private Connection connection;

    public MaterialDAO() {
        try {
            connection = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.Material_id, m.Name, m.Unit_of_calculation, m.Inventory_quantity, "
                + "m.Sub_category_id, s.Name AS Sub_category_name, c.Name AS Category_name "
                + "FROM Material m "
                + "JOIN Sub_category s ON m.Sub_category_id = s.Sub_category_id "
                + "JOIN Category c ON s.Category_id = c.Category_id";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setName(rs.getString("Name"));
                material.setUnitOfCalculation(rs.getString("Unit_of_calculation"));
                material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                material.setSubCategoryId(rs.getInt("Sub_category_id"));
                material.setSubCategoryName(rs.getString("Sub_category_name"));
                material.setCategoryName(rs.getString("Category_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public Material getMaterialById(int materialId) {
        Material material = null;
        String query = "SELECT m.Material_id, m.Name, m.Unit_of_calculation, m.Inventory_quantity, "
                + "m.Sub_category_id, s.Name AS Sub_category_name, c.Name AS Category_name "
                + "FROM Material m "
                + "JOIN Sub_category s ON m.Sub_category_id = s.Sub_category_id "
                + "JOIN Category c ON s.Category_id = c.Category_id "
                + "WHERE m.Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setName(rs.getString("Name"));
                    material.setUnitOfCalculation(rs.getString("Unit_of_calculation"));
                    material.setInventoryQuantity(rs.getInt("Inventory_quantity"));
                    material.setSubCategoryId(rs.getInt("Sub_category_id"));
                    material.setSubCategoryName(rs.getString("Sub_category_name"));
                    material.setCategoryName(rs.getString("Category_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return material;
    }

    public void addMaterial(Material material) {
        String query = "INSERT INTO Material (Sub_category_id, Name, Unit_of_calculation, Inventory_quantity) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getSubCategoryId());
            ps.setString(2, material.getName());
            ps.setString(3, material.getUnitOfCalculation());
            ps.setInt(4, material.getInventoryQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaterial(Material material) {
        String query = "UPDATE Material SET Sub_category_id = ?, Name = ?, Unit_of_calculation = ?, "
                + "Inventory_quantity = ? WHERE Material_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, material.getSubCategoryId());
            ps.setString(2, material.getName());
            ps.setString(3, material.getUnitOfCalculation());
            ps.setInt(4, material.getInventoryQuantity());
            ps.setInt(5, material.getMaterialId());
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

    public List<SubCategory> getAllSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        String query = "SELECT Sub_category_id, Name FROM Sub_category";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setSubCategoryId(rs.getInt("Sub_category_id"));
                subCategory.setName(rs.getString("Name"));
                subCategories.add(subCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subCategories;
    }

    public int countMaterialByCategoryId(int cid) {
        String query = "SELECT COUNT(*) FROM Material WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {                
                return rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
}
