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
        String[] detailIds = request.getParameterValues("detailIds");
        String[] quantities = request.getParameterValues("quantities"); 

        List<Integer> detailIdList = new ArrayList<>();
        List<Double> quantityList = new ArrayList<>();
        if (detailIds != null) {
            for (int i = 0; i < detailIds.length; i++) {
                detailIdList.add(Integer.parseInt(detailIds[i]));
                quantityList.add(Double.parseDouble(quantities[i])); 
            }
        }

        boolean success = true;
        String message = null;
        Import_noteDAO dao = new Import_noteDAO();
        Connection conn = null;

        try {
            conn = dao.getConnection();
            conn.setAutoCommit(false);

            for (int i = 0; i < detailIdList.size(); i++) {
                int detailId = detailIdList.get(i);
                double quantityToAdd = quantityList.get(i);
                Import_note_detail det = dao.getUnimportedDetail(detailId);
                if (det == null) {
                    success = false;
                    message = (message != null ? message + "\n" : "") + "Detail ID " + detailId + " is invalid or already imported.";
                    continue;
                }

                double originalQuantity = det.getQuantity();
                double totalTransactionQuantity = dao.getTotalTransactionQuantity(detailId);
                double remainingQuantity = originalQuantity - totalTransactionQuantity;

                if (quantityToAdd <= 0) {
                    success = false;
                    message = (message != null ? message + "\n" : "") + "The quantity to add must be greater than 0 for detail ID " + detailId;
                    continue;
                }
                if (quantityToAdd > remainingQuantity) {
                    success = false;
                    message = (message != null ? message + "\n" : "") + "The quantity to add (" + quantityToAdd + ") exceeds the remaining quantity (" + remainingQuantity + ") for detail ID " + detailId;
                    continue;
                }

                int mId = det.getMaterialId();
                int qId = det.getQualityId();

                Integer mdId = dao.findMaterialDetailId(mId, qId);
                if (mdId == null) {
                    dao.insertMaterialDetail(mId, qId, quantityToAdd);
                    mdId = dao.findMaterialDetailId(mId, qId);
                } else {
                    double oldQty = dao.getCurrentQuantity(mdId);
                    dao.updateMaterialDetail(mdId, oldQty + quantityToAdd);
                }

                dao.updateInventoryMaterialDaily(mId, qId, quantityToAdd);
                dao.insertMaterialTransactionHistory(mdId, detailId, "Imported from import note detail");
                dao.insertImportNoteTransaction(detailId, mId, qId, quantityToAdd);

                if (totalTransactionQuantity + quantityToAdd >= originalQuantity) {
                    dao.markDetailImported(detailId);
                }
            }

            if (!dao.hasRemainingDetails(importNoteId)) {
                dao.markNoteImported(importNoteId);
            }

            if (!success) {
                throw new SQLException("There are errors during the processing of details.");
            }

            conn.commit();

        } catch (SQLException e) {
            success = false;
            message = (message != null ? message : "");
            System.out.println("SQLException: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
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
                out.print("{\"success\":true,\"message\":\"Successfully added to inventory!\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"" + 
                          (message != null ? message.replace("\"", "\\\"").replace("\n", "\\n") : "Undefined error") + "\"}");
            }
            out.flush();
        }
    }
}