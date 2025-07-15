package dal;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.InventoryAuditDetail;
import model.InventoryItem;
import model.InventoryAudit;
import model.OrderDetail;
import model.Users;
import model.Order;

public class AuditInventoryDAO {

    private Connection conn;

    public AuditInventoryDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    public List<Users> getAll() throws SQLException {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT User_id, Full_name FROM Users ORDER BY Full_name";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Users u = new Users();
            u.setUserId(rs.getInt("User_id"));
            u.setFullName(rs.getString("Full_name"));
            list.add(u);
        }
        rs.close();
        ps.close();
        return list;
    }

    // Lấy danh sách vật tư kiểm kê tồn kho (theo thiết kế query của bạn)
    public List<InventoryItem> getInventoryForAudit() throws SQLException {
        List<InventoryItem> result = new ArrayList<>();
        String sql = "SELECT \n"
                + "    m.Material_id, \n"
                + "    m.Name AS Material_name, \n"
                + "    m.Image AS Material_image, \n"
                + "    u.Name AS Unit_name,\n"
                + "    SUM(CASE WHEN q.Quality_name = 'available' THEN md.Quantity ELSE 0 END) AS Available_qty,\n"
                + "    SUM(CASE WHEN q.Quality_name = 'notAvailable' THEN md.Quantity ELSE 0 END) AS NotAvailable_qty\n"
                + "FROM Materials m\n"
                + "JOIN Material_detail md ON md.Material_id = m.Material_id\n"
                + "JOIN units u ON m.Unit_id = u.Unit_id\n"
                + "JOIN quality q ON q.Quality_id = md.Quality_id\n"
                + "GROUP BY m.Material_id, m.Name, m.Image, u.Name\n"
                + "ORDER BY m.Material_id, u.Name;";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InventoryItem item = new InventoryItem();
                item.setMaterialId(rs.getInt("Material_id"));
                item.setMaterialName(rs.getString("Material_name"));
                item.setMaterialImage(rs.getString("Material_image"));
                item.setUnitName(rs.getString("Unit_name"));
                item.setAvailableQty(rs.getDouble("Available_qty"));
                item.setNotAvailableQty(rs.getDouble("NotAvailable_qty"));
                result.add(item);
            }
        }
        return result;
    }

    public InventoryAudit getAuditById(int auditId) throws SQLException {
        String sql = "SELECT ia.*, u.Full_name AS Created_by_name "
                + "FROM InventoryAudit ia "
                + "JOIN Users u ON u.User_id = ia.Created_by "
                + "WHERE ia.Inventory_audit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, auditId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InventoryAudit ia = new InventoryAudit();
                    ia.setId(rs.getInt("Inventory_audit_id"));
                    ia.setAuditCode(rs.getString("Audit_code"));
                    ia.setCreatedBy(rs.getInt("Created_by"));
                    ia.setCreatedByName(rs.getString("Created_by_name"));
                    ia.setAuditDate(rs.getDate("Audit_date"));
                    ia.setStatus(rs.getString("Status"));
                    ia.setNote(rs.getString("Note"));
                    ia.setCreatedAt(rs.getTimestamp("Created_at"));
                    return ia;
                }
            }
        }
        return null;
    }

    public List<InventoryAuditDetail> getAuditDetailByAuditId(int auditId) throws SQLException {
        List<InventoryAuditDetail> list = new ArrayList<>();
        String sql = "SELECT iad.*, m.Name AS materialName "
                + "FROM InventoryAuditDetail iad "
                + "JOIN Material_detail md ON iad.Material_detail_id = md.Material_detail_id "
                + "JOIN Materials m ON md.Material_id = m.Material_id "
                + "WHERE iad.Inventory_audit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, auditId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryAuditDetail d = new InventoryAuditDetail();
                    d.setId(rs.getInt("Inventory_audit_detail_id"));
                    d.setAuditId(rs.getInt("Inventory_audit_id"));
                    d.setMaterialDetailId(rs.getInt("Material_detail_id"));
                    d.setSystemQty(rs.getDouble("System_qty"));
                    d.setActualQty(rs.getDouble("Actual_qty"));
                    d.setDifference(rs.getDouble("Difference"));
                    d.setReason(rs.getString("Reason"));
                    d.setMaterialName(rs.getString("materialName")); // add materialName field vào model nếu chưa có
                    list.add(d);
                }
            }
        }
        return list;
    }

    public String generateAuditCode(java.sql.Date auditDate) throws SQLException {
        // Đếm số phiếu đã tạo hôm nay để lấy số thứ tự tiếp theo
        String sql = "SELECT COUNT(*) FROM InventoryAudit WHERE Audit_date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, auditDate);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            // Format: AUDITyyyymmddNNN
            String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(auditDate);
            return String.format("AUDIT%s%03d", dateStr, count + 1);
        }
    }

    public void saveInventoryAudit(int userId, java.sql.Date auditDate, List<InventoryAuditDetail> details) throws SQLException {
        // 1. Sinh mã code tự động theo ngày + số thứ tự
        String auditCode = generateAuditCode(auditDate);

        String auditSql = "INSERT INTO InventoryAudit (Audit_code, Created_by, Audit_date, Status) VALUES (?, ?, ?, 'draft')";
        String detailSql = "INSERT INTO InventoryAuditDetail (Inventory_audit_id, Material_detail_id, Quality_id, System_qty, Actual_qty, Difference, Reason) VALUES (?, ?, ?, ?, ?, ?, ?)";

        conn.setAutoCommit(false); // Transaction
        try (PreparedStatement auditStmt = conn.prepareStatement(auditSql, Statement.RETURN_GENERATED_KEYS)) {
            // Insert master
            auditStmt.setString(1, auditCode);
            auditStmt.setInt(2, userId);
            auditStmt.setDate(3, auditDate);
            auditStmt.executeUpdate();
            ResultSet rs = auditStmt.getGeneratedKeys();
            int auditId = 0;
            if (rs.next()) {
                auditId = rs.getInt(1);
            }
            rs.close();

            // Insert details
            try (PreparedStatement detailStmt = conn.prepareStatement(detailSql)) {
                for (InventoryAuditDetail d : details) {
                    detailStmt.setInt(1, auditId);
                    detailStmt.setInt(2, d.getMaterialDetailId());
                    detailStmt.setInt(3, d.getQualityId());
                    detailStmt.setDouble(4, d.getSystemQty());
                    detailStmt.setDouble(5, d.getActualQty());
                    detailStmt.setDouble(6, d.getDifference());
                    detailStmt.setString(7, d.getReason());
                    System.out.println("Insert detail: " + d.getMaterialDetailId() + ", " + d.getSystemQty() + ", " + d.getActualQty() + ", " + d.getDifference() + ", " + d.getReason());
                    detailStmt.addBatch();
                }
                detailStmt.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Đếm tổng số bản ghi với filter
    public int countAuditList(String code, String status, String dateFrom, String dateTo, String createdBy, boolean isAdmin, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM InventoryAudit ia WHERE 1=1 ";
        List<Object> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            sql += " AND ia.Audit_code LIKE ?";
            params.add("%" + code + "%");
        }
        if (status != null && !"all".equalsIgnoreCase(status) && !status.isEmpty()) {
            sql += " AND ia.Status = ?";
            params.add(status);
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sql += " AND ia.Audit_date >= ?";
            params.add(Date.valueOf(dateFrom));
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sql += " AND ia.Audit_date <= ?";
            params.add(Date.valueOf(dateTo));
        }
        if (isAdmin) {
            // Admin có thể filter theo người tạo
            if (createdBy != null && !createdBy.isEmpty()) {
                sql += " AND ia.Created_by = ?";
                params.add(Integer.parseInt(createdBy));
            }
        } else {
            // Không phải admin thì chỉ cho phép xem của chính mình
            sql += " AND ia.Created_by = ?";
            params.add(userId);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // Lấy list phiếu kiểm kê với filter & phân trang
    public List<InventoryAudit> getAuditList(String code, String status, String dateFrom, String dateTo, String createdBy, int page, int pageSize, boolean isAdmin, int userId) throws SQLException {
        List<InventoryAudit> list = new ArrayList<>();
        String sql = "SELECT ia.*, u.Full_name AS Created_by_name "
                + "FROM InventoryAudit ia "
                + "JOIN Users u ON u.User_id = ia.Created_by "
                + "WHERE 1=1 ";
        List<Object> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            sql += " AND ia.Audit_code LIKE ?";
            params.add("%" + code + "%");
        }
        if (status != null && !"all".equalsIgnoreCase(status) && !status.isEmpty()) {
            sql += " AND ia.Status = ?";
            params.add(status);
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sql += " AND ia.Audit_date >= ?";
            params.add(Date.valueOf(dateFrom));
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sql += " AND ia.Audit_date <= ?";
            params.add(Date.valueOf(dateTo));
        }
        if (isAdmin) {
            // Admin có thể filter theo người tạo
            if (createdBy != null && !createdBy.isEmpty()) {
                sql += " AND ia.Created_by = ?";
                params.add(Integer.parseInt(createdBy));
            }
        } else {
            // Không phải admin thì chỉ cho phép xem của chính mình
            sql += " AND ia.Created_by = ?";
            params.add(userId);
        }

        sql += " ORDER BY ia.Audit_date DESC, ia.Inventory_audit_id DESC ";
        sql += " LIMIT ? OFFSET ?";

        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryAudit ia = new InventoryAudit();
                    ia.setId(rs.getInt("Inventory_audit_id"));
                    ia.setAuditCode(rs.getString("Audit_code"));
                    ia.setAuditDate(rs.getDate("Audit_date"));
                    ia.setCreatedBy(rs.getInt("Created_by"));
                    ia.setCreatedByName(rs.getString("Created_by_name"));
                    ia.setStatus(rs.getString("Status"));
                    ia.setNote(rs.getString("Note"));
                    ia.setCreatedAt(rs.getTimestamp("Created_at"));
                    list.add(ia);
                }
            }
        }
        return list;
    }

    // Hàm lấy Material_detail_id từ materialId (bạn có thể tối ưu tuỳ table thiết kế)
    public int getMaterialDetailIdByMaterialId(int materialId) throws SQLException {
        String sql = "SELECT Material_detail_id FROM Material_detail WHERE Material_id = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getFirstWarehouseId() throws SQLException {
        String sql = "SELECT Warehouse_id FROM Warehouse LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Warehouse_id");
            }
            throw new SQLException("No warehouse found!");
        }
    }

    public List<InventoryAuditDetail> getDetailsByAuditId(int auditId) throws SQLException {
        List<InventoryAuditDetail> list = new ArrayList<>();
        String sql = "SELECT iad.*, "
                + "md.Material_id, md.SubUnit_id, md.Quality_id "
                + "FROM InventoryAuditDetail iad "
                + "JOIN Material_detail md ON iad.Material_detail_id = md.Material_detail_id "
                + "WHERE iad.Inventory_audit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, auditId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryAuditDetail detail = new InventoryAuditDetail();
                    detail.setId(rs.getInt("Inventory_audit_detail_id"));
                    detail.setAuditId(rs.getInt("Inventory_audit_id"));
                    detail.setMaterialDetailId(rs.getInt("Material_detail_id"));
                    detail.setSystemQty(rs.getDouble("System_qty"));
                    detail.setActualQty(rs.getDouble("Actual_qty"));
                    detail.setDifference(rs.getDouble("Difference"));
                    detail.setReason(rs.getString("Reason"));
                    // Các trường bổ sung:
                    detail.setMaterialId(rs.getInt("Material_id"));
                    detail.setSubUnitId(rs.getInt("SubUnit_id"));
                    detail.setQualityId(rs.getInt("Quality_id"));
                    list.add(detail);
                }
            }
        }
        return list;
    }

    public void createAdjustmentOrdersAfterAuditApproved(int inventoryAuditId, int approvedByUserId) throws SQLException {
        AuditInventoryDAO auditDAO = new AuditInventoryDAO();
        OrderDAO orderDAO = new OrderDAO();
        // 1. Lấy danh sách chi tiết kiểm kê của phiếu kiểm kê vừa duyệt
        List<InventoryAuditDetail> details = auditDAO.getDetailsByAuditId(inventoryAuditId);

        // 2. Lấy warehouse id duy nhất
        int warehouseId = getFirstWarehouseId(); // Viết hàm này như hướng dẫn trước, hoặc gán cứng 1 nếu luôn là 1 kho.

        // 3. Chuẩn bị order nhập và order xuất (auto approve luôn)
        Order importOrder = null;
        Order exportOrder = null;
        List<OrderDetail> importDetails = new ArrayList<>();
        List<OrderDetail> exportDetails = new ArrayList<>();

        for (InventoryAuditDetail d : details) {
            double difference = d.getActualQty() - d.getSystemQty();
            if (difference > 0) {
                // Thừa vật tư → tạo order nhập kho để tăng tồn thực tế
                if (importOrder == null) {
                    importOrder = new Order();
                    importOrder.setWarehouseId(warehouseId);
                    importOrder.setUserId(approvedByUserId);
                    importOrder.setType("import");
                    importOrder.setStatus("approved");
                    importOrder.setNote("Auto adjustment (surplus) after inventory audit #" + inventoryAuditId);
                }
                OrderDetail od = new OrderDetail();
                od.setMaterialId(d.getMaterialId()); // Cần có getMaterialId trong model InventoryAuditDetail
                od.setQuantity((int) difference);
                od.setQualityId(d.getQualityId());
                importDetails.add(od);
            } else if (difference < 0) {
                // Thiếu vật tư → tạo order xuất kho để giảm tồn thực tế
                if (exportOrder == null) {
                    exportOrder = new Order();
                    exportOrder.setWarehouseId(warehouseId);
                    exportOrder.setUserId(approvedByUserId);
                    exportOrder.setType("export");
                    exportOrder.setStatus("approved");
                    exportOrder.setNote("Auto adjustment (deficit) after inventory audit #" + inventoryAuditId);
                }
                OrderDetail od = new OrderDetail();
                od.setMaterialId(d.getMaterialId());
                od.setQuantity((int) Math.abs(difference));
                od.setQualityId(d.getQualityId());
                exportDetails.add(od);
            }
        }

        // 4. Lưu các order và chi tiết vào DB
        Integer importOrderId = null;
        Integer exportOrderId = null;
        if (importOrder != null && !importDetails.isEmpty()) {
            importOrderId = orderDAO.insertOrder(importOrder);
            for (OrderDetail od : importDetails) {
                od.setOrderId(importOrderId);
                orderDAO.insertOrderDetail(od);
            }
            // Tạo Import_note và Import_note_detail
            orderDAO.approveOrderAndCreateImportNote(importOrderId, approvedByUserId);
        }
        if (exportOrder != null && !exportDetails.isEmpty()) {
            exportOrderId = orderDAO.insertOrder(exportOrder);
            for (OrderDetail od : exportDetails) {
                od.setOrderId(exportOrderId);
                orderDAO.insertOrderDetail(od);
            }
            // Tạo Export_note và Export_note_detail
            orderDAO.approveOrderAndCreateExportNote(exportOrderId, approvedByUserId);
        }
    }

    // Trong AuditInventoryDAO (hoặc MaterialDetailDAO nếu bạn tách riêng)
    public int getMaterialDetailIdByMaterialIdAndQuality(int materialId, int qualityId) throws SQLException {
        String sql = "SELECT Material_detail_id FROM Material_detail WHERE Material_id = ? AND Quality_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setInt(2, qualityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Material_detail_id");
                }
            }
        }
        // Nếu không tìm thấy thì trả về -1 hoặc throw exception tuỳ bạn
        return -1;
    }

    public void updateAuditStatus(int auditId, String status) throws SQLException {
        String sql = "UPDATE InventoryAudit SET Status = ? WHERE Inventory_audit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, auditId);
            ps.executeUpdate();
        }
    }

    public void updateAuditStatusAndNote(int auditId, String status, String note) throws SQLException {
        String sql = "UPDATE InventoryAudit SET Status = ?, Note = ? WHERE Inventory_audit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, note);
            ps.setInt(3, auditId);
            ps.executeUpdate();
        }
    }

}
