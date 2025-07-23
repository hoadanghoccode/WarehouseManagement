/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.DBContext;
import model.MaterialTransactionHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import model.Material;

/**
 *
 * @author duong
 */
public class TransactionHistoryDAO {

       
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public MaterialTransactionHistory getLatestTransaction() {
    MaterialTransactionHistory m = null;

    String sql = """
        SELECT 
            mth.Material_transaction_history_id AS materialTransactionHistoryId,
            mth.Transaction_date,
            mth.Note,
            
            COALESCE(ind.Material_id, exd.Material_id) AS materialId,
            m.name AS materialName,
            m.unit_id,
            u.name AS unitName,
            
            COALESCE(ind.Quality_id, exd.Quality_id) AS qualityId,
            q.quality_name AS qualityName,
            
            COALESCE(ind.Quantity, exd.Quantity) AS quantity

        FROM materialtransactionhistory mth
        LEFT JOIN import_note_detail ind ON mth.Import_note_detail_id = ind.Import_note_detail_id
        LEFT JOIN export_note_detail exd ON mth.Export_note_detail_id = exd.Export_note_detail_id
        LEFT JOIN materials m ON m.material_id = COALESCE(ind.material_id, exd.material_id)
        LEFT JOIN units u ON u.unit_id = m.unit_id
        LEFT JOIN quality q ON q.quality_id = COALESCE(ind.quality_id, exd.quality_id)

        ORDER BY mth.transaction_date DESC
        LIMIT 1;
    """;

    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            m = new MaterialTransactionHistory();
            m.setMaterialTransactionHistoryId(rs.getInt("materialTransactionHistoryId"));
            m.setTransactionDate(rs.getDate("Transaction_date"));
            m.setNote(rs.getString("Note"));
            m.setMaterialId(rs.getInt("materialId"));
            m.setMaterialName(rs.getString("materialName"));
            m.setUnitId(rs.getInt("unit_id"));
            m.setUnitName(rs.getString("unitName"));
            m.setQualityId(rs.getInt("qualityId"));
            m.setQualityName(rs.getString("qualityName"));
            m.setQuantity(rs.getDouble("quantity"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return m;
}
    
   public MaterialTransactionHistory getLatestTransaction(Date from, Date to) {
      MaterialTransactionHistory m = null;

    String sql = """
        SELECT 
            mth.Material_transaction_history_id AS materialTransactionHistoryId,
            mth.Transaction_date,
            mth.Note,
            
            md.Material_id,
            m.Name AS materialName,
            m.Unit_id,
            u.Name AS unitName,
            
            md.Quality_id,
            q.Quality_name AS qualityName,
            md.Quantity

        FROM materialtransactionhistory mth
        LEFT JOIN material_detail md ON mth.Material_detail_id = md.Material_detail_id
        LEFT JOIN materials m ON md.Material_id = m.Material_id
        LEFT JOIN units u ON m.Unit_id = u.Unit_id
        LEFT JOIN quality q ON md.Quality_id = q.Quality_id
        WHERE mth.Transaction_date BETWEEN ? AND ?
        ORDER BY mth.Transaction_date DESC
        LIMIT 1
    """;

    try (
        Connection conn = new DBContext().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setDate(1, from);
        ps.setDate(2, to);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            m = new MaterialTransactionHistory();
            m.setMaterialTransactionHistoryId(rs.getInt("materialTransactionHistoryId"));
            m.setTransactionDate(rs.getDate("Transaction_date"));
            m.setNote(rs.getString("Note"));
            m.setMaterialId(rs.getInt("Material_id"));
            m.setMaterialName(rs.getString("materialName"));
            m.setUnitId(rs.getInt("Unit_id"));
            m.setUnitName(rs.getString("unitName"));
            m.setQualityId(rs.getInt("Quality_id"));
            m.setQualityName(rs.getString("qualityName"));
            m.setQuantity(rs.getDouble("Quantity"));
        }
    } catch (Exception e) {
        System.err.println("❌ Không có giao dịch nào trong khoảng ngày đã chọn.");
        e.printStackTrace();
    }

    return m;
}
   
   public List<Material> getRecentlyTransacted(Date from, Date to) {
    List<Material> list = new ArrayList<>();

    String sql = "SELECT m.Material_id, m.Name, MAX(o.Created_at) as LastTransacted " +
             "FROM Materials m " +
             "JOIN Order_detail od ON m.Material_id = od.Material_id " +
             "JOIN Orders o ON o.Order_id = od.Order_id " +
             "WHERE o.Type IN ('import', 'export', 'exportToRepair') " +
             "AND DATE(o.Created_at) BETWEEN ? AND ? " +
             "GROUP BY m.Material_id, m.Name " +
             "ORDER BY LastTransacted DESC";

    try (Connection conn = new DBContext().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDate(1, new java.sql.Date(from.getTime()));
        ps.setDate(2, new java.sql.Date(to.getTime()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Material m = new Material();
            m.setMaterialId(rs.getInt("Material_id"));
            m.setName(rs.getString("Name"));
            list.add(m);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
   
   public int getTotalTransactionsToday(Date today) {
    int total = 0;

    String importSql = "SELECT COUNT(*) AS cnt FROM Import_note WHERE DATE(Imported_at) = ?";
    String exportSql = "SELECT COUNT(*) AS cnt FROM Export_note WHERE DATE(Exported_at) = ?";

    try (Connection conn = new DBContext().getConnection()) {

        // Count Import
        try (PreparedStatement ps = conn.prepareStatement(importSql)) {
            ps.setDate(1, new java.sql.Date(today.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total += rs.getInt("cnt");
            }
        }

        // Count Export
        try (PreparedStatement ps = conn.prepareStatement(exportSql)) {
            ps.setDate(1, new java.sql.Date(today.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total += rs.getInt("cnt");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
   
   public List<MaterialTransactionHistory> getTransactions(Date fromDate, Date toDate, String searchKeyword,
                                                        Integer materialIdFilter, Integer qualityIdFilter,
                                                        int offset, int pageSize) {
    List<MaterialTransactionHistory> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder("""
        SELECT 
            mth.Material_transaction_history_id,
            mth.Transaction_date,
            mth.Note,
            md.Material_id,
            m.Name AS materialName,
            m.Unit_id,
            u.Name AS unitName,
            md.Quality_id,
            q.Quality_name AS qualityName,
            md.Quantity
        FROM materialtransactionhistory mth
        LEFT JOIN material_detail md ON mth.Material_detail_id = md.Material_detail_id
        LEFT JOIN materials m ON md.Material_id = m.Material_id
        LEFT JOIN units u ON m.Unit_id = u.Unit_id
        LEFT JOIN quality q ON md.Quality_id = q.Quality_id
        WHERE 1=1
    """);

    if (fromDate != null) sql.append(" AND DATE(mth.Transaction_date) >= ? ");
    if (toDate != null) sql.append(" AND DATE(mth.Transaction_date) <= ? ");
    if (searchKeyword != null && !searchKeyword.isEmpty()) sql.append(" AND m.Name LIKE ? ");
    if (materialIdFilter != null) sql.append(" AND md.Material_id = ? ");
    if (qualityIdFilter != null) sql.append(" AND md.Quality_id = ? ");

    sql.append(" ORDER BY mth.Transaction_date DESC LIMIT ? OFFSET ? ");

    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int idx = 1;
        if (fromDate != null) ps.setDate(idx++, fromDate);
        if (toDate != null) ps.setDate(idx++, toDate);
        if (searchKeyword != null && !searchKeyword.isEmpty()) ps.setString(idx++, "%" + searchKeyword + "%");
        if (materialIdFilter != null) ps.setInt(idx++, materialIdFilter);
        if (qualityIdFilter != null) ps.setInt(idx++, qualityIdFilter);
        ps.setInt(idx++, pageSize);
        ps.setInt(idx, offset);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            MaterialTransactionHistory m = new MaterialTransactionHistory();
            m.setMaterialTransactionHistoryId(rs.getInt("Material_transaction_history_id"));
            m.setTransactionDate(rs.getDate("Transaction_date"));
            m.setNote(rs.getString("Note"));
            m.setMaterialId(rs.getInt("Material_id"));
            m.setMaterialName(rs.getString("materialName"));
            m.setUnitId(rs.getInt("Unit_id"));
            m.setUnitName(rs.getString("unitName"));
            m.setQualityId(rs.getInt("Quality_id"));
            m.setQualityName(rs.getString("qualityName"));
            m.setQuantity(rs.getDouble("Quantity"));
            list.add(m);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
 public int countTransactions(Date fromDate, Date toDate, String searchKeyword,
                             Integer materialIdFilter, Integer qualityIdFilter) {
    StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) FROM materialtransactionhistory mth
        LEFT JOIN material_detail md ON mth.Material_detail_id = md.Material_detail_id
        LEFT JOIN materials m ON md.Material_id = m.Material_id
        WHERE 1=1
    """);

    if (fromDate != null) sql.append(" AND mth.Transaction_date >= ? ");
    if (toDate != null) sql.append(" AND mth.Transaction_date <= ? ");
    if (searchKeyword != null && !searchKeyword.isEmpty()) sql.append(" AND m.Name LIKE ? ");
    if (materialIdFilter != null) sql.append(" AND md.Material_id = ? ");
    if (qualityIdFilter != null) sql.append(" AND md.Quality_id = ? ");

    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int idx = 1;
        if (fromDate != null) ps.setDate(idx++, fromDate);
        if (toDate != null) ps.setDate(idx++, toDate);
        if (searchKeyword != null && !searchKeyword.isEmpty()) ps.setString(idx++, "%" + searchKeyword + "%");
        if (materialIdFilter != null) ps.setInt(idx++, materialIdFilter);
        if (qualityIdFilter != null) ps.setInt(idx++, qualityIdFilter);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
    public static void main(String[] args) {
  TransactionHistoryDAO dao = new TransactionHistoryDAO();

        // Thiết lập dữ liệu kiểm thử
        Date fromDate = Date.valueOf(LocalDate.of(2025, 7, 1));
        Date toDate = Date.valueOf(LocalDate.of(2025, 7, 20));
        String searchKeyword = ""; // thử "" hoặc "Gạch"
        Integer materialIdFilter = null; // hoặc ví dụ: 1
        Integer qualityIdFilter = null;  // hoặc ví dụ: 2
        int offset = 0;
        int pageSize = 10;

        System.out.println("🔍 Đang kiểm tra transaction list...");
        List<MaterialTransactionHistory> list = dao.getTransactions(fromDate, toDate, searchKeyword,
                materialIdFilter, qualityIdFilter, offset, pageSize);

        System.out.println("📌 Tổng số transaction trả về: " + list.size());
        for (MaterialTransactionHistory m : list) {
            System.out.println("🧾 ID: " + m.getMaterialTransactionHistoryId()
                    + " | Date: " + m.getTransactionDate()
                    + " | Material: " + m.getMaterialName()
                    + " | Qty: " + m.getQuantity()
                    + " | Unit: " + m.getUnitName()
                    + " | Quality: " + m.getQualityName()
                    + " | Note: " + m.getNote());
        }

        System.out.println("\n🔢 Đang đếm tổng giao dịch...");
        int total = dao.countTransactions(fromDate, toDate, searchKeyword,
                materialIdFilter, qualityIdFilter);
        System.out.println("🧮 Tổng giao dịch đếm được: " + total);
    }
}

 