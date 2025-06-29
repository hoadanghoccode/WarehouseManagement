package controller;

import dal.Import_noteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Import_note_detail;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ImportNoteToInventoryController", urlPatterns = {"/import-note-to-inventory"})
public class ImportNoteToInventoryController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int importNoteId = Integer.parseInt(request.getParameter("importNoteId"));
        String[] arr = request.getParameterValues("detailIds");
        List<Integer> detailIds = new ArrayList<>();
        if (arr != null) {
            for (String s : arr) {
                detailIds.add(Integer.parseInt(s));
            }
        }

        boolean success = false;
        String message = null;
        Import_noteDAO dao = new Import_noteDAO();
        Connection conn = null;

        try {
            conn = dao.getConnection();
            conn.setAutoCommit(false);

            for (int detailId : detailIds) {
                // 1) Lấy detail chưa imported
                Import_note_detail det = dao.getUnimportedDetail(detailId);
                if (det == null) continue;

                int    mId  = det.getMaterialId();
                int    suId = det.getSubUnitId();
                int    qId  = det.getQualityId();
                double qty  = det.getQuantity();

                // 2) Kiểm tra tồn ở material_detail
                Integer mdId = dao.findMaterialDetailId(mId, suId, qId);
                if (mdId != null) {
                    // Lấy số lượng hiện tại và cộng thêm
                    double oldQty = dao.getCurrentQuantity(mdId);
                    dao.updateMaterialDetail(mdId, oldQty + qty);
                } else {
                    dao.insertMaterialDetail(mId, suId, qId, qty);
                }

                // *) Cập nhật InventoryMaterialDaily
                dao.updateInventoryMaterialDaily(mId, suId, qId, qty);
                
                dao.insertMaterialTransactionHistory(mdId, detailId, "Imported from import note detail");

                // 3) Đánh dấu detail đã imported
                dao.markDetailImported(detailId);
            }

            // 4) Nếu đã import hết tất cả detail thì đánh dấu import_note
            if (!dao.hasRemainingDetails(importNoteId)) {
                dao.markNoteImported(importNoteId);
            }

            conn.commit();
            success = true;

        } catch (SQLException e) {
            message = e.getMessage();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }

        // Trả JSON kết quả
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (success) {
                out.print("{\"success\":true}");
            } else {
                out.print("{\"success\":false,\"message\":\""
                        + (message != null ? message.replace("\"","\\\"") : "Lỗi xử lý")
                        + "\"}");
            }
        }
    }
}