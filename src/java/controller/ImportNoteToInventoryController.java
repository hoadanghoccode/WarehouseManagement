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
                Import_note_detail det = dao.getUnimportedDetail(detailId);
                if (det == null) {
                    message = "Chi tiết ID " + detailId + " không hợp lệ hoặc đã được import.";
                    continue;
                }

                int mId = det.getMaterialId();
                int suId = det.getSubUnitId();
                int qId = det.getQualityId();
                double qty = det.getQuantity();

                Integer mdId = dao.findMaterialDetailId(mId, suId, qId);
                if (mdId == null) {
                    dao.insertMaterialDetail(mId, suId, qId, qty);
                    mdId = dao.findMaterialDetailId(mId, suId, qId);
                } else {
                    double oldQty = dao.getCurrentQuantity(mdId);
                    dao.updateMaterialDetail(mdId, oldQty + qty);
                }

                dao.updateInventoryMaterialDaily(mId, suId, qId, qty);
                dao.insertMaterialTransactionHistory(mdId, detailId, "Imported from import note detail");
                dao.markDetailImported(detailId);
            }

            if (!dao.hasRemainingDetails(importNoteId)) {
                dao.markNoteImported(importNoteId);
            }

            conn.commit();
            success = true;

        } catch (SQLException e) {
            message = "Lỗi cơ sở dữ liệu: " + e.getMessage();
            System.out.println("SQLException: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    success = false;
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Close connection failed: " + ex.getMessage());
                }
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (success) {
                out.print("{\"success\":true,\"message\":\"Thêm vào kho thành công!\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"" + 
                          (message != null ? message.replace("\"", "\\\"") : "Lỗi không xác định") + "\"}");
            }
            out.flush();
        }
    }
}