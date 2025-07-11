package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Category;
import model.Material;
import model.MaterialDetail;
import model.MaterialStatistic;

public class MaterialDAO extends DBContext {

    // Get all materials with search and filter options
    public List<Material> getAllMaterials(String search, Integer categoryId, Integer unitId, String status) {
        List<Material> list = new ArrayList<>();
        String query = "SELECT m.*, c.Name AS categoryName, u.Name AS unitName FROM Materials m JOIN Category c ON m.Category_id = c.Category_id JOIN Units u ON m.Unit_id = u.Unit_id WHERE 1=1";
        if (search != null && !search.isEmpty()) {
            query += " AND m.Name LIKE ?";
        }
        if (categoryId != null) {
            query += " AND m.Category_id = ?";
        }
        if (unitId != null) {
            query += " AND m.Unit_id = ?";
        }
        if (status != null && !status.isEmpty()) {
            query += " AND m.Status = ?";
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
            if (unitId != null) {
                ps.setInt(paramIndex++, unitId);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setCategoryId(rs.getInt("Category_id"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setName(rs.getString("Name"));
                m.setImage(rs.getString("Image"));
                m.setCreateAt(rs.getDate("Create_at"));
                m.setStatus(rs.getString("Status"));
                m.setCategoryName(rs.getString("categoryName"));
                m.setUnitName(rs.getString("unitName"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("getAllMaterials error: " + e.getMessage());
        }
        return list;
    }

    // Get material by ID
    public Material getMaterialById(int materialId) {
        String query = """
          SELECT m.*, 
                 c.Name     AS categoryName, 
                 u.Name     AS unitName
            FROM Materials m
            JOIN Category c ON m.Category_id = c.Category_id
            JOIN Units    u ON m.Unit_id     = u.Unit_id
           WHERE m.Material_id = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Material m = new Material();
                    m.setMaterialId(rs.getInt("Material_id"));
                    m.setCategoryId(rs.getInt("Category_id"));
                    m.setUnitId(rs.getInt("Unit_id"));
                    m.setName(rs.getString("Name"));
                    m.setImage(rs.getString("Image"));
                    m.setCreateAt(rs.getDate("Create_at"));
                    m.setStatus(rs.getString("Status"));
                    // set thêm tên category + unit
                    m.setCategoryName(rs.getString("categoryName"));
                    m.setUnitName(rs.getString("unitName"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.err.println("getMaterialById error: " + e.getMessage());
        }
        return null;
    }

    // Insert new material
    public boolean insertMaterial(Material material) {
        String query = "INSERT INTO Materials (Category_id, Unit_id, Name, Image, Create_at, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getUnitId());
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

     public boolean updateMaterial(Material material) {
        String query = "UPDATE Materials SET Category_id = ?, SupplierId = ?, Name = ?, Image = ?, Status = ?, Last_updated = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, material.getCategoryId());
            ps.setInt(2, material.getSupplierId());
            ps.setString(3, material.getName());
            ps.setString(4, material.getImage());
            ps.setString(5, material.getStatus());
            ps.setTimestamp(6, Timestamp.from(Instant.now())); 
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

    //Bạn Minh cần hàm này nha
    public boolean updateStatusMaterial(int materialId) {
        String query = "UPDATE Materials SET Status = 'active', Last_updated = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            ps.setInt(2, materialId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("updateMaterial error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMaterial(int materialId) {
        String query = "UPDATE Materials SET Status = 'inactive', Last_updated = ? WHERE Material_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            ps.setInt(2, materialId);

            if (isMaterialInOrderWithStatus(materialId, "pending") || 
                isMaterialInPendingImportOrExport(materialId) ||
                isMaterialInPendingPurchaseOrder(materialId)) {
                return false;
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
        String query = """
          SELECT Material_detail_id,
                 Material_id,
                 Quality_id,
                 Quantity,
                 Last_updated
            FROM Material_detail
           WHERE Material_id = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialDetail md = new MaterialDetail();
                    md.setMaterialDetailId(rs.getInt("Material_detail_id"));
                    md.setMaterialId(rs.getInt("Material_id"));
                    md.setQualityId(rs.getInt("Quality_id"));
                    md.setQuantity(rs.getDouble("Quantity"));
                    // nếu bạn muốn đầy đủ giờ phút, có thể chuyển sang Timestamp trong model
                    md.setLastUpdated(rs.getDate("Last_updated"));
                    list.add(md);
                }
            }
        } catch (SQLException e) {
            System.err.println("getMaterialDetailsByMaterialId error: " + e.getMessage());
        }
        return list;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        CategoryDAO cDao = new CategoryDAO();
        return cDao.getAllCategories();
    }

    public int getMaterialIdByName(String name) {
        String sql = "SELECT Material_id FROM Materials WHERE LOWER(Name) = LOWER(?) LIMIT 1";
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

    //Bạn Minh sửa hàm này nha
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
                material.setImage(rs.getString("Image"));
                material.setUnitId(rs.getInt("Unit_id"));
                return material;
            }
        } catch (SQLException e) {
            System.out.println("getMaterialIdByName error: " + e.getMessage());
        }
        return null;
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

    public List<Material> getAllMaterialsByCategoryId(int categoryId) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM Materials WHERE Category_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setMaterialId(rs.getInt("Material_id"));
                    material.setCategoryId(rs.getInt("Category_id"));
                    material.setUnitId(rs.getInt("Unit_id"));
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
        String query = "SELECT 1 FROM Order_detail od JOIN Orders o ON od.Order_id = o.Order_id WHERE od.Material_id = ? AND o.Status = ? LIMIT 1";

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

    // Check nếu tôn tại Material trong Import/Export Detail với Imported/Exported = false
    public boolean isMaterialInPendingImportOrExport(int materialId) {
        boolean isPending = false;

        String importQuery = "SELECT 1 FROM Import_note_detail ind JOIN Import_note i ON ind.Import_note_id = i.Import_note_id WHERE ind.Material_id = ? AND i.imported = false LIMIT 1";
        try (PreparedStatement psImport = connection.prepareStatement(importQuery)) {
            psImport.setInt(1, materialId);
            try (ResultSet rsImport = psImport.executeQuery()) {
                if (rsImport.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("isMaterialInPendingImportOrExport (import) error: " + e.getMessage());
        }

        String exportQuery = "SELECT 1 FROM Export_note_detail endt JOIN Export_note en ON endt.Export_note_id = en.Export_note_id WHERE endt.Material_id = ? AND en.exported = false LIMIT 1";
        try (PreparedStatement psExport = connection.prepareStatement(exportQuery)) {
            psExport.setInt(1, materialId);
            try (ResultSet rsExport = psExport.executeQuery()) {
                if (rsExport.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("isMaterialInPendingImportOrExport (export) error: " + e.getMessage());
        }
        return isPending;
    }

    public boolean isMaterialInPendingPurchaseOrder(int materialId) {
        String query = "SELECT 1 FROM PurchaseOrder_detail pod JOIN PurchaseOrders po ON pod.PurchaseOrder_id = po.PurchaseOrder_id WHERE pod.Material_id = ? AND po.Status = 'pending' LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("isMaterialInPendingPurchaseOrder error: " + e.getMessage());
        }
        return false;
    }

    public Map<String, Double> getQualityDistributionByMaterialId(int materialId) {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT q.Quality_name, SUM(md.Quantity) AS total FROM Material_detail md JOIN Quality q ON q.Quality_id = md.Quality_id WHERE md.Material_id = ? GROUP BY q.Quality_name";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("Quality_name"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public int getTotalMaterialCount() {
        String sql = "SELECT COUNT(*) FROM Materials WHERE Status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalInventoryQuantity() {
        String sql = "SELECT SUM(Quantity) FROM Material_detail";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM Materials WHERE status = 'new' OR status = 'active'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setCategoryId(rs.getInt("Category_id"));
                m.setImage(rs.getString("Image"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Material> getNewMaterialsToday() {
        List<Material> list = new ArrayList<>();
    String sql = """
        SELECT 
            m.Material_id,
            m.Name AS materialName,
            m.Image,
            m.Create_at,
            c.Name AS categoryName,
            u.Name AS unitName
        FROM materials m
        JOIN category c ON m.Category_id = c.Category_id
        JOIN units u ON m.Unit_id = u.Unit_id
        WHERE DATE(m.Create_at) = CURDATE()
        ORDER BY m.Create_at DESC
    """;

    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Material m = new Material();
            m.setMaterialId(rs.getInt("Material_id"));
            m.setName(rs.getString("materialName"));
            m.setImage(rs.getString("Image"));
            m.setCreateAt(rs.getDate("Create_at"));
            m.setCategoryName(rs.getString("categoryName"));
            m.setUnitName(rs.getString("unitName")); 
            list.add(m);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
    }

    public List<MaterialStatistic> getMaterialMonthlyStats(int materialId, Date from, Date to) {
        List<MaterialStatistic> list = new ArrayList<>();
        String sql = """
      SELECT 
           result.material_id,
           result.month,
           result.year,
           SUM(result.totalImport) AS totalImport,
           SUM(result.totalExport) AS totalExport,
           MAX(result.stock) AS stock,
           u.name AS unitName
       FROM (
           -- NHẬP
           SELECT 
               m.material_id,
               MONTH(n.imported_at) AS month,
               YEAR(n.imported_at) AS year,
               SUM(CASE WHEN i.imported = 1 THEN i.quantity ELSE 0 END) AS totalImport,
               0 AS totalExport,
               0 AS stock
           FROM materials m
           JOIN import_note_detail i ON m.material_id = i.material_id
           JOIN import_note n ON i.import_note_id = n.import_note_id
           WHERE m.material_id = ?
             AND n.imported_at BETWEEN ? AND ?
           GROUP BY m.material_id, MONTH(n.imported_at), YEAR(n.imported_at)
       
           UNION ALL
       
           -- TỒN
           SELECT 
               m.material_id,
               MONTH(md.last_updated) AS month,
               YEAR(md.last_updated) AS year,
               0 AS totalImport,
               0 AS totalExport,
               SUM(md.quantity) AS stock
           FROM materials m
           JOIN material_detail md ON m.material_id = md.material_id
           WHERE m.material_id = ?
             AND md.last_updated BETWEEN ? AND ?
           GROUP BY m.material_id, MONTH(md.last_updated), YEAR(md.last_updated)
       
           UNION ALL
       
           -- XUẤT
           SELECT 
               m.material_id,
               MONTH(ex.exported_at) AS month,
               YEAR(ex.exported_at) AS year,
               0 AS totalImport,
               SUM(e.quantity) AS totalExport,
               0 AS stock
           FROM materials m
           JOIN export_note_detail e ON m.material_id = e.material_id
           JOIN export_note ex ON e.export_note_id = ex.export_note_id
           WHERE m.material_id = ?
             AND ex.exported_at BETWEEN ? AND ?
           GROUP BY m.material_id, MONTH(ex.exported_at), YEAR(ex.exported_at)
       ) AS result
       JOIN materials m2 ON m2.material_id = result.material_id
       JOIN units u ON m2.unit_id = u.unit_id
       GROUP BY result.material_id, result.month, result.year
       ORDER BY result.year, result.month
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId); // import
            ps.setDate(2, from);
            ps.setDate(3, to);
            ps.setInt(4, materialId); // stock
            ps.setDate(5, from);
            ps.setDate(6, to);
            ps.setInt(7, materialId); // export
            ps.setDate(8, from);
            ps.setDate(9, to);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    if (rs.wasNull()) {
                        continue;
                    }
                    MaterialStatistic stat = new MaterialStatistic();
                    stat.setMaterialId(rs.getInt("material_id"));
                    stat.setMonth(month);
                    stat.setTotalImport(rs.getDouble("totalImport"));
                    stat.setTotalExport(rs.getDouble("totalExport"));
                    stat.setStock(rs.getDouble("stock"));
                    stat.setUnitName(rs.getString("unitName"));
                    list.add(stat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public List<Material> getMaterialsInDateRange(Date fromDate, Date toDate, Integer materialIdFilter) {
        List<Material> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT m.*, 
               c.Name AS categoryName, 
               u.Name AS unitName
        FROM materials m
        JOIN category c ON m.category_id = c.category_id
        JOIN units u ON m.unit_id = u.unit_id
        WHERE 1=1
    """);

        if (fromDate != null) {
            sql.append(" AND DATE(m.create_at) >= ? ");
        }
        if (toDate != null) {
            sql.append(" AND DATE(m.create_at) <= ? ");
        }
        if (materialIdFilter != null) {
            sql.append(" AND m.material_id = ? ");
        }
        sql.append(" ORDER BY m.create_at DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (fromDate != null) {
                ps.setDate(index++, fromDate);
            }
            if (toDate != null) {
                ps.setDate(index++, toDate);
            }
            if (materialIdFilter != null) {
                ps.setInt(index++, materialIdFilter);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("material_id"));
                m.setName(rs.getString("name"));
                m.setImage(rs.getString("image"));
                m.setCreateAt(rs.getDate("create_at"));
                m.setStatus(rs.getString("status"));
                m.setCategoryId(rs.getInt("category_id"));
                m.setCategoryName(rs.getString("categoryName"));
                m.setUnitId(rs.getInt("unit_id"));
                m.setUnitName(rs.getString("unitName"));
                list.add(m);
            }
        } catch (Exception e) {
            System.out.println("❌ Không có vật tư mới.");
            e.printStackTrace();
        }
        return list;
    }


    public int getMaterialCountInDateRange(Date fromDate, Date toDate) {
        String sql = "SELECT COUNT(*) FROM materials WHERE DATE(last_updated) BETWEEN ? AND ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getActiveMaterialCount() {
        String sql = """
        SELECT COUNT(DISTINCT material_id)
        FROM material_detail
        WHERE quantity > 0
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getActiveMaterialCountInRange(Date fromDate, Date toDate) {
        String sql = """
        SELECT COUNT(DISTINCT material_id)
        FROM material_detail
        WHERE quantity > 0
          AND DATE(last_updated) BETWEEN ? AND ?
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void main(String[] args) {
//        // Bước 1: Chọn khoảng ngày muốn test
//        LocalDate fromLocal = LocalDate.of(2025, 7, 1);
//        LocalDate toLocal = LocalDate.of(2025, 7, 2);
//        Date fromDate = Date.valueOf(fromLocal);
//        Date toDate = Date.valueOf(toLocal);
//
//        // Bước 2: Gọi DAO
//        MaterialDAO dao = new MaterialDAO();
//        List<Material> newMaterials = dao.getMaterialsInDateRange(fromDate, toDate, null);
//
//        // Bước 3: In kết quả
//        System.out.println("📌 Danh sách vật tư mới từ " + fromDate + " đến " + toDate);
//        if (newMaterials.isEmpty()) {
//            System.out.println("❌ Không có vật tư mới.");
//        } else {
//            for (Material m : newMaterials) {
//                System.out.println("✅ ID: " + m.getMaterialId()
//                        + ", Tên: " + m.getName()
//                        + ", Danh mục: " + m.getCategoryName()
//                        + ", Nhà cung cấp: " + m.getSupplierName()
//                        + ", Đơn vị: " + m.getUnitName()
//                        + ", Ngày tạo: " + m.getCreateAt()
//                        + ", Trạng thái: " + m.getStatus());
//            }
//        }

        MaterialDAO dao = new MaterialDAO();

//        List<Material> newMaterials = dao.getMaterialsInDateRange(fromDate, toDate, null);
 
        // Bước 3: In kết quả
//        System.out.println("📌 Danh sách vật tư mới từ " + fromDate + " đến " + toDate);
//        if (newMaterials.isEmpty()) {
//            System.out.println("❌ Không có vật tư mới.");
//        } else {
//            for (Material m : newMaterials) {
//                System.out.println(m);
//            }
//        }

//        int materialId = 1; // ID vật tư cần test
//        int year = 2025;
//
//        LocalDate fromLocal = LocalDate.of(year, 1, 1);
//        LocalDate toLocal = LocalDate.of(year, 12, 31);
//
//        Date fromDate = Date.valueOf(fromLocal);
//        Date toDate = Date.valueOf(toLocal);
//
//        List<MaterialStatistic> stats = dao.getMaterialMonthlyStats(materialId, fromDate, toDate);
//
//        System.out.println("📊 Monthly Stats for materialId = " + materialId + " in year " + year);
//        for (MaterialStatistic stat : stats) {
//            System.out.printf("Tháng %02d | Nhập: %.2f | Xuất: %.2f | Tồn: %.2f | Đơn vị: %s%n",
//                    stat.getMonth(), stat.getTotalImport(), stat.getTotalExport(), stat.getStock(), stat.getUnitName());
////        }
//            // Kiểm tra tổng số vật tư


    List<Material> newMaterials = dao.getNewMaterialsToday();

    System.out.println("📦 New Materials Added Today: " + newMaterials.size());
    for (Material m : newMaterials) {
        System.out.println("- " + m.getName() + " | Đơn vị: " + m.getUnitName() + 
                           " | Ngày tạo: " + m.getCreateAt());
        }
    }
}
