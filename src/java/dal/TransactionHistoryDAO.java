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
            
            COALESCE(ind.SubUnit_id, exd.SubUnit_id) AS subUnitId,
            su.name AS subUnitName,
            
            COALESCE(ind.Quality_id, exd.Quality_id) AS qualityId,
            q.quality_name AS qualityName,
            
            COALESCE(ind.Quantity, exd.Quantity) AS quantity

        FROM materialtransactionhistory mth
        LEFT JOIN import_note_detail ind ON mth.Import_note_detail_id = ind.Import_note_detail_id
        LEFT JOIN export_note_detail exd ON mth.Export_note_detail_id = exd.Export_note_detail_id
        LEFT JOIN materials m ON m.Material_id = COALESCE(ind.Material_id, exd.Material_id)
        LEFT JOIN subunits su ON su.SubUnit_id = COALESCE(ind.SubUnit_id, exd.SubUnit_id)
        LEFT JOIN quality q ON q.Quality_id = COALESCE(ind.Quality_id, exd.Quality_id)

        ORDER BY mth.Transaction_date DESC
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
            m.setSubUnitId(rs.getInt("subUnitId"));
            m.setSubUnitName(rs.getString("subUnitName"));
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
            md.SubUnit_id,
            su.Name AS subUnitName,
            md.Quality_id,
            q.Quality_name AS qualityName,
            md.Quantity
        FROM materialtransactionhistory mth
        LEFT JOIN material_detail md ON mth.Material_detail_id = md.Material_detail_id
        LEFT JOIN materials m ON md.Material_id = m.Material_id
        LEFT JOIN subunits su ON md.SubUnit_id = su.SubUnit_id
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
            m.setSubUnitId(rs.getInt("SubUnit_id"));
            m.setSubUnitName(rs.getString("subUnitName"));
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
    public static void main(String[] args) {
        // Chọn khoảng thời gian muốn test
        LocalDate fromLocal = LocalDate.of(2025, 6, 30);
        LocalDate toLocal = LocalDate.of(2025, 7, 1);

        Date fromDate = Date.valueOf(fromLocal);
        Date toDate = Date.valueOf(toLocal);

        TransactionHistoryDAO dao = new TransactionHistoryDAO();
        MaterialTransactionHistory latest = dao.getLatestTransaction(fromDate, toDate);

        // Kiểm tra kết quả
        if (latest != null) {
            System.out.println("✅ Giao dịch mới nhất:");
            System.out.println("➤ ID: " + latest.getMaterialTransactionHistoryId());
            System.out.println("➤ Ngày giao dịch: " + latest.getTransactionDate());
            System.out.println("➤ Vật tư: " + latest.getMaterialName());
            System.out.println("➤ SubUnit: " + latest.getSubUnitName());
            System.out.println("➤ Quality: " + latest.getQualityName());
            System.out.println("➤ Số lượng: " + latest.getQuantity());
            System.out.println("➤ Ghi chú: " + latest.getNote());
        } else {
            System.out.println("❌ Không có giao dịch nào trong khoảng ngày đã chọn.");
        }
    }
    

}

