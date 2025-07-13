package controller;

import dal.ExportNoteDAO;
import model.ExportNote;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ExportNoteDetail;

public class ExportNoteListServlet extends HttpServlet {

    private ExportNoteDAO dao;

    public ExportNoteListServlet() {
        this.dao = new ExportNoteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            String search = request.getParameter("search");
            String exported = request.getParameter("exported");
            String sortOrder = request.getParameter("sortOrder") != null ? request.getParameter("sortOrder") : "desc";
            int page = request.getParameter("page") != null && !request.getParameter("page").isEmpty()
                    ? Integer.parseInt(request.getParameter("page")) : 1;

            if ("loadInventory".equals(action)) {
                String exportNoteId = request.getParameter("exportNoteId");
                if (exportNoteId == null || exportNoteId.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Export Note ID.");
                    return;
                }
                request.setAttribute("exportNoteId", exportNoteId);
                request.getRequestDispatcher("/exportNoteToInventory.jsp").forward(request, response);
                return;
            }

            List<ExportNote> exportNotes = dao.getAllExportNotes(search, exported, sortOrder, page);
            int totalNotes = dao.getTotalExportNotes(search, exported);
            int totalPages = (int) Math.ceil((double) totalNotes / 5);

            request.setAttribute("exportNotes", exportNotes);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalNotes", totalNotes);
            request.setAttribute("search", search);
            request.setAttribute("exported", exported);
            request.setAttribute("sortOrder", sortOrder);

            String viewDetailId = request.getParameter("viewDetailId");
            if (viewDetailId != null && !viewDetailId.isEmpty()) {
                request.setAttribute("viewDetailId", viewDetailId);
                request.getRequestDispatcher("/getExportNote.jsp").forward(request, response);
                return;
            }

            String exportNoteId = request.getParameter("exportNoteId");
            if (exportNoteId != null && !exportNoteId.isEmpty()) {
                request.setAttribute("exportNoteId", exportNoteId);
                request.getRequestDispatcher("/exportNoteToInventory.jsp").forward(request, response);
                return;
            }

            request.getRequestDispatcher("/exportnotelist.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page number format: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                int userId = Integer.parseInt(request.getParameter("userId"));
                Integer warehouseId = request.getParameter("warehouseId") != null && !request.getParameter("warehouseId").isEmpty()
                        ? Integer.parseInt(request.getParameter("warehouseId")) : null;
                String customerName = request.getParameter("customerName");
                String[] materialIds = request.getParameterValues("materialId");
                String[] unitIds = request.getParameterValues("unitId");
                String[] quantities = request.getParameterValues("quantity");
                String[] qualityIds = request.getParameterValues("qualityId");

                if (materialIds == null || materialIds.length == 0) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "No materials provided.");
                } else {
                    ExportNote exportNote = new ExportNote();
                    exportNote.setOrderId(orderId);
                    exportNote.setUserId(userId);
                    exportNote.setWarehouseId(warehouseId);
                    exportNote.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
                    exportNote.setCustomerName(customerName);
                    exportNote.setExported(false);
                    exportNote.setExportedAt(null);
                    int exportNoteId = dao.addExportNote(exportNote);

                    for (int i = 0; i < materialIds.length; i++) {
                        ExportNoteDetail detail = new ExportNoteDetail();
                        detail.setExportNoteId(exportNoteId);
                        detail.setMaterialId(Integer.parseInt(materialIds[i]));
                        detail.setUnitId(Integer.parseInt(unitIds[i]));
                        detail.setQuantity(Double.parseDouble(quantities[i]));
                        detail.setQualityId(Integer.parseInt(qualityIds[i]));
                        detail.setExported(false);
                        dao.addExportNoteDetail(detail);
                    }

                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Export note created successfully. Please export from inventory to complete.");
                    jsonResponse.put("redirect", buildRedirectUrl(request, 1));
                }
            } else if ("export".equals(action)) {
                int exportNoteId = Integer.parseInt(request.getParameter("exportNoteId"));
                String[] detailIdsArray = request.getParameterValues("detailIds");
                String[] quantitiesArray = request.getParameterValues("quantities");
                String[] materialIdsArray = request.getParameterValues("materialIds");
                String[] unitIdsArray = request.getParameterValues("unitIds");
                String[] qualityIdsArray = request.getParameterValues("qualityIds");
                String[] transactionIdsArray = request.getParameterValues("transactionIds");

                if ((detailIdsArray == null || detailIdsArray.length == 0) && (transactionIdsArray == null || transactionIdsArray.length == 0)) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "No items or transactions selected for export.");
                } else {
                    List<Integer> detailIds = new ArrayList<>();
                    List<Double> quantities = new ArrayList<>();
                    List<Integer> materialIds = new ArrayList<>();
                    List<Integer> unitIds = new ArrayList<>();
                    List<Integer> qualityIds = new ArrayList<>();
                    List<Integer> transactionIds = new ArrayList<>();

                    if (detailIdsArray != null) {
                        for (int i = 0; i < detailIdsArray.length; i++) {
                            detailIds.add(Integer.parseInt(detailIdsArray[i]));
                            quantities.add(Double.parseDouble(quantitiesArray[i]));
                            materialIds.add(Integer.parseInt(materialIdsArray[i]));
                            unitIds.add(Integer.parseInt(unitIdsArray[i]));
                            qualityIds.add(Integer.parseInt(qualityIdsArray[i]));
                        }
                    }

                    if (transactionIdsArray != null) {
                        for (String transactionId : transactionIdsArray) {
                            transactionIds.add(Integer.parseInt(transactionId));
                        }
                    }

                    dao.markAsExported(exportNoteId, detailIds, quantities, materialIds, unitIds, qualityIds, transactionIds, request);

                    String pendingMessage = (String) request.getAttribute("pendingMessage");
                    boolean success = !pendingMessage.contains("Invalid") && 
                                     !pendingMessage.contains("No inventory available") && 
                                     !pendingMessage.contains("Insufficient inventory") && 
                                     !pendingMessage.contains("does not belong to Export Note");
                    jsonResponse.put("success", success);
                    jsonResponse.put("message", pendingMessage);
                    if (success) {
                        jsonResponse.put("redirect", buildRedirectUrl(request, Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1")));
                    }
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid action.");
            }
        } catch (SQLException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Database error: " + e.getMessage());
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            java.util.logging.Logger.getLogger("ExportNoteListServlet").severe("Unexpected error: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
        out.close();
    }

    private String buildRedirectUrl(HttpServletRequest request, int page) {
        StringBuilder redirect = new StringBuilder("exportnotelist?page=").append(page);
        String search = request.getParameter("search");
        String exported = request.getParameter("exported");
        String sortOrder = request.getParameter("sortOrder");
        if (search != null && !search.trim().isEmpty()) {
            redirect.append("&search=").append(search);
        }
        if (exported != null && !exported.isEmpty()) {
            redirect.append("&exported=").append(exported);
        }
        if (sortOrder != null && !sortOrder.isEmpty()) {
            redirect.append("&sortOrder=").append(sortOrder);
        }
        return redirect.toString();
    }

    @Override
    public void destroy() {
        // Cleanup handled by DBContext
    }
}