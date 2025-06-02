/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Material;
import model.Unit;
import model.Supplier;

public class MaterialDAO extends DBContext {

    /**
     * Phương thức cũ: Lấy danh sách vật liệu theo trang (có filter nội bộ).
     * Ta vẫn giữ nguyên vì có thể dùng cho các mục đích khác,
     * nhưng trong Controller ta sẽ không gọi đến nữa.
     */
    public List<Material> getMaterialsByPage(int page, int pageSize, String search, String categoryFilter,
            BigDecimal quantityMin, BigDecimal quantityMax) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.Material_id, m.Name, m.Status, c.Name AS categoryName, c.Parent_id, "
                + "p.Unit_id, u.Name AS unitName, p.price, i.Quantity, s.Name AS supplierName "
                + "FROM Material m "
                + "LEFT JOIN Category c ON m.Category_id = c.Category_id "
                + "LEFT JOIN Material_Unit_Price p ON m.Material_id = p.Material_id "
                + "LEFT JOIN Unit u ON p.Unit_id = u.Unit_id "
                + "LEFT JOIN MaterialInventory i ON m.Material_id = i.Material_id AND p.Unit_id = i.Unit_id "
                + "LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId "
                + "LEFT JOIN Supplier s ON sm.SupplierId = s.Id "
                + "AND (m.Name LIKE ? OR ? IS NULL) "
                + "AND (c.Name = ? OR ? IS NULL) "
                + "AND (i.Quantity >= ? OR ? IS NULL) "
                + "AND (i.Quantity <= ? OR ? IS NULL) "
                + "ORDER BY m.Material_id "
                + "LIMIT ? OFFSET ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + (search != null ? search : "") + "%");
            ps.setString(2, search);
            ps.setString(3, categoryFilter);
            ps.setString(4, categoryFilter);
            ps.setBigDecimal(5, quantityMin);
            ps.setBigDecimal(6, quantityMin);
            ps.setBigDecimal(7, quantityMax);
            ps.setBigDecimal(8, quantityMax);
            ps.setInt(9, pageSize);
            ps.setInt(10, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                m.setStatus(rs.getString("Status"));
                m.setCategoryName(rs.getString("categoryName"));
                m.setParentCategoryId(rs.getInt("Parent_id"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setUnitName(rs.getString("unitName"));
                m.setPrice(rs.getBigDecimal("price"));
                m.setQuantity(rs.getBigDecimal("Quantity"));
                m.setSupplierName(rs.getString("supplierName"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("getMaterialsByPage error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Phương thức mới: Lấy TOÀN BỘ danh sách Material (không phân trang).
     * Client‐side (JSP + JS) sẽ render lên bảng, rồi quản lý paging bằng JS.
     */
    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.Material_id, m.Name, m.Status, c.Name AS categoryName, c.Parent_id, "
                   + "p.Unit_id, u.Name AS unitName, p.price, i.Quantity, s.Name AS supplierName "
                   + "FROM Material m "
                   + "LEFT JOIN Category c ON m.Category_id = c.Category_id "
                   + "LEFT JOIN Material_Unit_Price p ON m.Material_id = p.Material_id "
                   + "LEFT JOIN Unit u ON p.Unit_id = u.Unit_id "
                   + "LEFT JOIN MaterialInventory i ON m.Material_id = i.Material_id AND p.Unit_id = i.Unit_id "
                   + "LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId "
                   + "LEFT JOIN Supplier s ON sm.SupplierId = s.Id "
                   + "ORDER BY m.Material_id";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                m.setStatus(rs.getString("Status"));
                m.setCategoryName(rs.getString("categoryName"));
                m.setParentCategoryId(rs.getInt("Parent_id"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setUnitName(rs.getString("unitName"));
                m.setPrice(rs.getBigDecimal("price"));
                m.setQuantity(rs.getBigDecimal("Quantity"));
                m.setSupplierName(rs.getString("supplierName"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("getAllMaterials error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Đếm tổng số vật liệu (giữ nguyên nếu có nhu cầu server‐side). 
     * Ở giải pháp A, ta không cần dùng đến, nhưng giữ lại để không phá vỡ các chỗ khác.
     */
    public int getTotalMaterials(String search, String categoryFilter, BigDecimal quantityMin, BigDecimal quantityMax) {
        String sql = "SELECT COUNT(DISTINCT m.Material_id) AS total "
                + "FROM Material m "
                + "LEFT JOIN Category c ON m.Category_id = c.Category_id "
                + "LEFT JOIN Material_Unit_Price p ON m.Material_id = p.Material_id "
                + "LEFT JOIN MaterialInventory i ON m.Material_id = i.Material_id AND p.Unit_id = i.Unit_id "
                + "AND (m.Name LIKE ? OR ? IS NULL) "
                + "AND (c.Name = ? OR ? IS NULL) "
                + "AND (i.Quantity >= ? OR ? IS NULL) "
                + "AND (i.Quantity <= ? OR ? IS NULL)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + (search != null ? search : "") + "%");
            ps.setString(2, search);
            ps.setString(3, categoryFilter);
            ps.setString(4, categoryFilter);
            ps.setBigDecimal(5, quantityMin);
            ps.setBigDecimal(6, quantityMin);
            ps.setBigDecimal(7, quantityMax);
            ps.setBigDecimal(8, quantityMax);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("getTotalMaterials error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Lấy danh sách Category (active).
     */
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setStatus(rs.getString("Status"));
                int parentId = rs.getInt("Parent_id");
                if (!rs.wasNull()) {
                    // Lấy parent category (đệ quy nếu cần)
                    cate.setParentId(new CategoryDAO().getCategoryById(parentId));
                } else {
                    cate.setParentId(null);
                }
                list.add(cate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách Unit (active).
     */
    public List<Unit> getAllUnits() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT * FROM Unit WHERE Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnitId(rs.getInt("Unit_id"));
                u.setName(rs.getString("Name"));
                u.setStatus(rs.getString("Status"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.out.println("getAllUnits error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy danh sách Supplier (active).
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("Id"));
                s.setName(rs.getString("Name"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("getAllSuppliers error: " + e.getMessage());
        }
        return list;
    }

    // ... (Giữ nguyên các method addMaterial, updateMaterial, deleteMaterial, addMaterialUnitPrice, etc.)

    /**
     * Lấy chi tiết vật liệu (bao gồm Unit, Price, Quantity, Supplier)
     */
    public Material getMaterialByIdWithDetails(int materialId) {
        String sql = "SELECT m.Material_id, m.Name, m.Status, c.Name AS categoryName, c.Parent_id, "
                   + "p.Unit_id, u.Name AS unitName, p.price, i.Quantity, s.Name AS supplierName "
                   + "FROM Material m "
                   + "LEFT JOIN Category c ON m.Category_id = c.Category_id "
                   + "LEFT JOIN Material_Unit_Price p ON m.Material_id = p.Material_id "
                   + "LEFT JOIN Unit u ON p.Unit_id = u.Unit_id "
                   + "LEFT JOIN MaterialInventory i ON m.Material_id = i.Material_id AND p.Unit_id = i.Unit_id "
                   + "LEFT JOIN SupplierMaterial sm ON m.Material_id = sm.MaterialId "
                   + "LEFT JOIN Supplier s ON sm.SupplierId = s.Id "
                   + "WHERE m.Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                m.setStatus(rs.getString("Status"));
                m.setCategoryName(rs.getString("categoryName"));
                m.setParentCategoryId(rs.getInt("Parent_id"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setUnitName(rs.getString("unitName"));
                m.setPrice(rs.getBigDecimal("price"));
                m.setQuantity(rs.getBigDecimal("Quantity"));
                m.setSupplierName(rs.getString("supplierName"));
                return m;
            }
        } catch (SQLException e) {
            System.out.println("getMaterialByIdWithDetails error: " + e.getMessage());
        }
        return null;
    }
}
