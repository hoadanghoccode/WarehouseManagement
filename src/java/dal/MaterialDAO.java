package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Material;
import model.MaterialDetail;
import model.Supplier;
import java.sql.Timestamp;
import java.time.Instant;

public class MaterialDAO extends DBContext {

    // Get all materials with search and filter options
    public List<Material> getAllMaterials(String search, Integer categoryId, Integer supplierId, String status) {
        List<Material> list = new ArrayList<>();
        String query = "SELECT * FROM Materials WHERE 1=1";
        if (search != null && !search.isEmpty()) {
            query += " AND Name LIKE ?";
        }
        if (categoryId != null) {
            query += " AND Category_id = ?";
        }
        if (supplierId != null) {
            query += " AND SupplierId = ?";
        }
        if (status != null && !status.isEmpty()) {
            query += " AND Status = ?";
        }

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                ps.setString(paramIndex++, "%" + search + "%");
            }
            if (categoryId != null) {
                ps.setInt(paramIndex++, categoryId);
            }
            if (supplierId != null) {
                ps.setInt(paramIndex++, supplierId);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setCategoryId(rs.getInt("Category_id"));
                m.setSupplierId(rs.getInt("SupplierId"));
                m.setName(rs.getString("Name"));
                m.setImage(rs.getString("Image"));
                m.setCreateAt(rs.getDate("Create_at"));
                m.setStatus(rs.getString("Status"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("getAllMaterials error: " + e.getMessage());
        }
        return list;
    }

    // Get material by ID
    public Material getMaterialById(int materialId) {
        String query = "SELECT * FROM Materials WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setCategoryId(rs.getInt("Category_id"));
                m.setSupplierId(rs.getInt("SupplierId"));
                m.setName(rs.getString("Name"));
                m.setImage(rs.getString("Image"));
                m.setCreateAt(rs.getDate("Create_at"));
                m.setStatus(rs.getString("Status"));
                return m;
            }
        } catch (SQLException e) {
            System.out.println("getMaterialById error: " + e.getMessage());
        }
        return null;
    }

    // Insert new material
    public boolean insertMaterial(Material material) {
        String query = "INSERT INTO Materials (Category_id, SupplierId, Name, Image, Create_at, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getSupplierId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getImage());
            ps.setDate(5, material.getCreateAt());
            ps.setString(6, material.getStatus());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("insertMaterial error: " + e.getMessage());
            return false;
        }
    }

    // Update material (all fields except Create_at, with Last_updated auto-set, and status)
    public boolean updateMaterial(Material material) {
        String query = "UPDATE Materials SET Category_id = ?, SupplierId = ?, Name = ?, Image = ?, Status = ?, Last_updated = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getSupplierId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getImage());
            ps.setString(5, material.getStatus());
            ps.setTimestamp(6, Timestamp.from(Instant.now())); // Auto-set Last_updated to current timestamp
            ps.setInt(7, material.getMaterialId());

            // Check if material is in pending order or pending import/export
            if (isMaterialInOrderWithStatus(material.getMaterialId(), "pending") || isMaterialInPendingImportOrExport(material.getMaterialId())) {
                return false; // Prevent update if material is in pending state
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("updateMaterial error: " + e.getMessage());
            return false;
        }
    }

    // Soft delete material (change status to inactive) with checks
    public boolean deleteMaterial(int materialId) {
        String query = "UPDATE Materials SET Status = 'inactive', Last_updated = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.from(Instant.now())); // Auto-set Last_updated to current timestamp
            ps.setInt(2, materialId);

            // Check if material is in pending order or pending import/export
            if (isMaterialInOrderWithStatus(materialId, "pending") || isMaterialInPendingImportOrExport(materialId)) {
                return false; // Prevent deletion if material is in pending state
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("deleteMaterial error: " + e.getMessage());
            return false;
        }
    }

    // Get material details by material ID
    public List<MaterialDetail> getMaterialDetailsByMaterialId(int materialId) {
        List<MaterialDetail> list = new ArrayList<>();
        String query = "SELECT * FROM Material_detail WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaterialDetail md = new MaterialDetail();
                md.setMaterialDetailId(rs.getInt("Material_detail_id"));
                md.setMaterialId(rs.getInt("Material_id"));
                md.setSubUnitId(rs.getInt("SubUnit_id"));
                md.setQualityId(rs.getInt("Quality_id"));
                md.setQuantity(rs.getDouble("Quantity"));
                md.setLastUpdated(rs.getDate("Last_updated"));
                list.add(md);
            }
        } catch (SQLException e) {
            System.out.println("getMaterialDetailsByMaterialId error: " + e.getMessage());
        }
        return list;
    }

    // Get all suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM Suppliers";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("Supplier_id"));
                s.setName(rs.getString("Name"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("getAllSuppliers error: " + e.getMessage());
        }
        return list;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        CategoryDAO cDao = new CategoryDAO();
        return cDao.getAllCategories();
    }
    
    public int getMaterialIdByName(String name) {
        String sql = "SELECT Material_id FROM Material WHERE LOWER(Name) = LOWER(?) LIMIT 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Material_id");
            }
        } catch (SQLException e) {
            System.out.println("getMaterialIdByName error: " + e.getMessage());
        }
        return 0;
    }

    // Minh thêm hàm để xử lí 
    public Material getMaterialIdBy(int mid) {
        String sql = "SELECT * FROM Materials WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, mid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("Material_id"));
                material.setName(rs.getString("Name"));
                return material;
            }
        } catch (SQLException e) {
            System.out.println("getMaterialIdByName error: " + e.getMessage());
        }
        return null;
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

    // Check if material is in pending import or export with imported/exported = false
    public boolean isMaterialInPendingImportOrExport(int materialId) {
        boolean isPending = false;

        // Check Import_note_detail for pending imports (imported = false)
        String importQuery = "SELECT 1 FROM Import_note_detail ind " +
                           "JOIN Import_note i ON ind.Import_note_id = i.Import_note_id " +
                           "WHERE ind.Material_id = ? AND i.imported = false LIMIT 1";
        try (PreparedStatement psImport = connection.prepareStatement(importQuery)) {
            psImport.setInt(1, materialId);
            try (ResultSet rsImport = psImport.executeQuery()) {
                if (rsImport.next()) {
                    return true; // Material is in a pending import
                }
            }
        } catch (SQLException e) {
            System.out.println("isMaterialInPendingImportOrExport (import) error: " + e.getMessage());
        }

        // Check Export_note_detail for pending exports (exported = false)
        String exportQuery = "SELECT 1 FROM Export_note_detail endt " +
                           "JOIN Export_note en ON endt.Export_note_id = en.Export_note_id " +
                           "WHERE endt.Material_id = ? AND en.exported = false LIMIT 1";
        try (PreparedStatement psExport = connection.prepareStatement(exportQuery)) {
            psExport.setInt(1, materialId);
            try (ResultSet rsExport = psExport.executeQuery()) {
                if (rsExport.next()) {
                    return true; // Material is in a pending export
                }
            }
        } catch (SQLException e) {
            System.out.println("isMaterialInPendingImportOrExport (export) error: " + e.getMessage());
        }
        // Return false if no pending records found
        return isPending; 
    }

    public int countMaterialByCategoryId(int cid) {
        String query = "SELECT COUNT(*) FROM Materials WHERE Category_id = ?";
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