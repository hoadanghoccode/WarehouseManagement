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

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT Material_id, Name FROM Material";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("getAllMaterials error: " + e.getMessage());
        }
        return list;
    }

    public List<Unit> getUnitsByMaterialId(int materialId) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT u.Unit_id, u.Name, u.Status "
                + "FROM Material_Unit_Price p "
                + "JOIN Unit u ON p.Unit_id = u.Unit_id "
                + "WHERE p.Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setUnitId(rs.getInt("Unit_id"));
                u.setName(rs.getString("Name"));
                u.setStatus(rs.getString("Status"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.out.println("getUnitsByMaterialId error: " + e.getMessage());
        }
        return list;
    }

    public BigDecimal getCurrentQuantity(int materialId, int unitId) {
        String sql = "SELECT Quantity FROM MaterialInventory WHERE Material_id = ? AND Unit_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.setInt(2, unitId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("Quantity");
            }
        } catch (SQLException e) {
            System.out.println("getCurrentQuantity error: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public int addMaterial(Material material) {
        String sql = "INSERT INTO Material (Category_id, Name, Status) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, material.getCategoryId());
            ps.setString(2, material.getName());
            ps.setString(3, material.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("addMaterial error: " + e.getMessage());
        }
        return 0;
    }

    public void updateMaterial(Material material) {
        String sql = "UPDATE Material SET Category_id = ?, Name = ?, Status = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, material.getCategoryId());
            ps.setString(2, material.getName());
            ps.setString(3, material.getStatus());
            ps.setInt(4, material.getMaterialId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateMaterial error: " + e.getMessage());
        }
    }

    public void deleteMaterial(int materialId) {
        String sql = "UPDATE Material SET Status = 'inactive' WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteMaterial error: " + e.getMessage());
        }
    }

    public void addMaterialUnitPrice(int materialId, int unitId, BigDecimal price) {
        String sql = "INSERT INTO Material_Unit_Price (Material_id, Unit_id, price) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE price = VALUES(price)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.setInt(2, unitId);
            ps.setBigDecimal(3, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("addMaterialUnitPrice error: " + e.getMessage());
        }
    }

    public void updateMaterialUnitPrice(int materialId, int unitId, BigDecimal price) {
        String sql = "UPDATE Material_Unit_Price SET price = ? WHERE Material_id = ? AND Unit_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBigDecimal(1, price);
            ps.setInt(2, materialId);
            ps.setInt(3, unitId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateMaterialUnitPrice error: " + e.getMessage());
        }
    }

    public void deleteMaterialUnitPrice(int materialId) {
        String sql = "DELETE FROM Material_Unit_Price WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteMaterialUnitPrice error: " + e.getMessage());
        }
    }

    public void addMaterialInventory(int materialId, int unitId, BigDecimal quantity) {
        String sql = "INSERT INTO MaterialInventory (Material_id, Unit_id, Quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE Quantity = VALUES(Quantity)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.setInt(2, unitId);
            ps.setBigDecimal(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("addMaterialInventory error: " + e.getMessage());
        }
    }

    public void updateMaterialInventory(int materialId, int unitId, BigDecimal quantity) {
        String sql = "UPDATE MaterialInventory SET Quantity = ? WHERE Material_id = ? AND Unit_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBigDecimal(1, quantity);
            ps.setInt(2, materialId);
            ps.setInt(3, unitId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateMaterialInventory error: " + e.getMessage());
        }
    }

    public void deleteMaterialInventory(int materialId) {
        String sql = "DELETE FROM MaterialInventory WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteMaterialInventory error: " + e.getMessage());
        }
    }

    public void addSupplierMaterial(int supplierId, int materialId) {
        String sql = "INSERT INTO SupplierMaterial (SupplierId, MaterialId) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE SupplierId = VALUES(SupplierId)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, supplierId);
            ps.setInt(2, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("addSupplierMaterial error: " + e.getMessage());
        }
    }

    public void deleteSupplierMaterial(int materialId) {
        String sql = "DELETE FROM SupplierMaterial WHERE MaterialId = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteSupplierMaterial error: " + e.getMessage());
        }
    }

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


    public boolean isMaterialInOrderWithStatus(int materialId, String status) {
        String query = "SELECT 1 "
                + "FROM Order_detail od "
                + "JOIN Orders o ON od.Order_id = o.Order_id "
                + "WHERE od.Material_id = ? AND o.Status = ? "
                + "LIMIT 1";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, materialId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isMaterialInPendingImport(int materialId) {
        String query = "SELECT 1 "
                + "FROM Import_note_detail ind "
                + "JOIN Import_note i ON ind.Import_note_id = i.Import_note_id "
                + "WHERE ind.Material_id = ? AND i.Status = 'pending' "
                + "LIMIT 1";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isMaterialInPendingExport(int materialId) {
        String query = "SELECT 1 "
                + "FROM Export_note_detail endt "
                + "JOIN Export_note en ON endt.Export_note_id = en.Export_note_id "
                + "WHERE endt.Material_id = ? AND en.Status = 'pending' "
                + "LIMIT 1";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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

    public List<Material> getAllMaterialsByCategoryId(int categoryId) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM Material WHERE Category_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setName(rs.getString("Name"));
                    material.setStatus(rs.getString("Status"));
                    materials.add(material);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }

    public static void main(String[] args) {
        MaterialDAO dao = new MaterialDAO();
        System.out.println(dao.getAllMaterialsByCategoryId(2));
    }
}

