package dal;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.InventoryAuditDetail;
import model.InventoryItem;

public class AuditInventoryDAO {

    private Connection conn;

    public AuditInventoryDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    // Lấy danh sách vật tư kiểm kê tồn kho (theo thiết kế query của bạn)
    public List<InventoryItem> getInventoryForAudit() throws SQLException {
        List<InventoryItem> result = new ArrayList<>();
        String sql = "SELECT m.Material_id, m.Name AS Material_name, su.Name AS Unit_name, "
                + "SUM(CASE WHEN q.Quality_name = 'available' THEN md.Quantity ELSE 0 END) AS Available_qty, "
                + "SUM(CASE WHEN q.Quality_name = 'notAvailable' THEN md.Quantity ELSE 0 END) AS NotAvailable_qty "
                + "FROM Materials m "
                + "JOIN Material_detail md ON md.Material_id = m.Material_id "
                + "JOIN SubUnits su ON su.SubUnit_id = md.SubUnit_id "
                + "JOIN Quality q ON q.Quality_id = md.Quality_id "
                + "GROUP BY m.Material_id, m.Name, su.Name "
                + "ORDER BY m.Material_id, su.Name";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InventoryItem item = new InventoryItem();
                item.setMaterialId(rs.getInt("Material_id"));
                item.setMaterialName(rs.getString("Material_name"));
                item.setUnitName(rs.getString("Unit_name"));
                item.setAvailableQty(rs.getDouble("Available_qty"));
                item.setNotAvailableQty(rs.getDouble("NotAvailable_qty"));
                result.add(item);
            }
        }
        return result;
    }
    
     // Lưu phiếu kiểm kê (InventoryAudit) và list chi tiết
    public void saveInventoryAudit(int userId, java.sql.Date auditDate, List<InventoryAuditDetail> details) throws SQLException {
        String auditSql = "INSERT INTO InventoryAudit (Created_by, Audit_date, Status) VALUES (?, ?, 'draft')";
        String detailSql = "INSERT INTO InventoryAuditDetail (Inventory_audit_id, Material_detail_id, System_qty, Actual_qty, Difference, Reason) VALUES (?, ?, ?, ?, ?, ?)";

        conn.setAutoCommit(false); // Transaction
        try (PreparedStatement auditStmt = conn.prepareStatement(auditSql, Statement.RETURN_GENERATED_KEYS)) {
            // 1. Insert master
            auditStmt.setInt(1, userId);
            auditStmt.setDate(2, auditDate);
            auditStmt.executeUpdate();
            ResultSet rs = auditStmt.getGeneratedKeys();
            int auditId = 0;
            if (rs.next()) {
                auditId = rs.getInt(1);
            }
            rs.close();

            // 2. Insert details
            try (PreparedStatement detailStmt = conn.prepareStatement(detailSql)) {
                for (InventoryAuditDetail d : details) {
                    detailStmt.setInt(1, auditId);
                    detailStmt.setInt(2, d.getMaterialDetailId());
                    detailStmt.setDouble(3, d.getSystemQty());
                    detailStmt.setDouble(4, d.getActualQty());
                    detailStmt.setDouble(5, d.getDifference());
                    detailStmt.setString(6, d.getReason());
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

    // Hàm lấy Material_detail_id từ materialId (bạn có thể tối ưu tuỳ table thiết kế)
    public int getMaterialDetailIdByMaterialId(int materialId) throws SQLException {
        String sql = "SELECT Material_detail_id FROM Material_detail WHERE Material_id = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

}
