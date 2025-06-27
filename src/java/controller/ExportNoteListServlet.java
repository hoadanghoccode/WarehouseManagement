package controller;

import dal.ExportNoteDAO;
import model.ExportNote;
import model.ExportNoteDetail;
import model.BackOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ExportNoteListServlet extends HttpServlet {

    private ExportNoteDAO dao;

    public ExportNoteListServlet() {
        this.dao = new ExportNoteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String search = request.getParameter("search");
            String exported = request.getParameter("exported");
            String sortOrder = request.getParameter("sortOrder");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

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
            if (viewDetailId != null) {
                request.setAttribute("viewDetailId", viewDetailId);
                request.getRequestDispatcher("/getExportNote.jsp").forward(request, response);
                return;
            }

            String exportNoteId = request.getParameter("exportNoteId");
            if (exportNoteId != null) {
                request.setAttribute("exportNoteId", exportNoteId);
                request.getRequestDispatcher("/exportNoteToInventory.jsp").forward(request, response);
                return;
            }

            request.getRequestDispatcher("/exportnotelist.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String action = request.getParameter("action");
        System.out.println("Received action: " + action); // Debug

        try {
            if ("create".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                int userId = Integer.parseInt(request.getParameter("userId"));
                Integer warehouseId = request.getParameter("warehouseId") != null && !request.getParameter("warehouseId").isEmpty()
                        ? Integer.parseInt(request.getParameter("warehouseId")) : null;
                String customerName = request.getParameter("customerName");
                String[] materialIds = request.getParameterValues("materialId");
                String[] subUnitIds = request.getParameterValues("subUnitId");
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
                    exportNote.setExported(true);
                    exportNote.setExportedAt(new java.sql.Date(System.currentTimeMillis()));
                    int exportNoteId = dao.addExportNote(exportNote);

                    List<ExportNoteDetail> details = new ArrayList<>();
                    for (int i = 0; i < materialIds.length; i++) {
                        int materialId = Integer.parseInt(materialIds[i]);
                        int subUnitId = Integer.parseInt(subUnitIds[i]);
                        double quantity = Double.parseDouble(quantities[i]);
                        int qualityId = Integer.parseInt(qualityIds[i]);

                        int materialDetailId = dao.getMaterialDetailId(materialId, subUnitId, qualityId);
                        if (!dao.checkInventoryAvailability(materialDetailId, quantity)) {
                            jsonResponse.put("success", false);
                            jsonResponse.put("message", "Insufficient inventory for Material ID: " + materialId);
                            break;
                        }

                        ExportNoteDetail detail = new ExportNoteDetail();
                        detail.setExportNoteId(exportNoteId);
                        detail.setMaterialId(materialId);
                        detail.setSubUnitId(subUnitId);
                        detail.setQuantity(quantity);
                        detail.setQualityId(qualityId);
                        details.add(detail);

                        dao.addExportNoteDetail(detail);
                        dao.updateInventoryMaterialDaily(materialDetailId, quantity);
                    }
                    exportNote.setDetails(details);

                    if (jsonResponse.has("success") && !jsonResponse.getBoolean("success")) {
                    } else {
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "Export note created successfully.");
                        jsonResponse.put("redirect", "exportnotelist?page=1");
                    }
                }
            } else if ("export".equals(action)) {
                int exportNoteId = Integer.parseInt(request.getParameter("exportNoteId"));
                System.out.println("ExportNoteId: " + exportNoteId); // Debug
                String[] detailIdsArray = request.getParameterValues("detailIds");
                String[] quantitiesArray = request.getParameterValues("quantities");
                String[] materialIdsArray = request.getParameterValues("materialIds");
                String[] subUnitIdsArray = request.getParameterValues("subUnitIds");
                String[] qualityIdsArray = request.getParameterValues("qualityIds");

                if (detailIdsArray == null || detailIdsArray.length == 0) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "No items selected for export.");
                } else {
                    List<Integer> detailIds = new ArrayList<>();
                    List<Double> quantities = new ArrayList<>();
                    List<Integer> materialIds = new ArrayList<>();
                    List<Integer> subUnitIds = new ArrayList<>();
                    List<Integer> qualityIds = new ArrayList<>();

                    for (int i = 0; i < detailIdsArray.length; i++) {
                        try {
                            detailIds.add(Integer.parseInt(detailIdsArray[i]));
                            quantities.add(Double.parseDouble(quantitiesArray[i]));
                            materialIds.add(Integer.parseInt(materialIdsArray[i]));
                            subUnitIds.add(Integer.parseInt(subUnitIdsArray[i]));
                            qualityIds.add(Integer.parseInt(qualityIdsArray[i]));
                        } catch (NumberFormatException e) {
                            jsonResponse.put("success", false);
                            jsonResponse.put("message", "Invalid data format for index " + i + ": " + e.getMessage());
                            break;
                        }
                    }

                    if (jsonResponse.has("success") && !jsonResponse.getBoolean("success")) {
                    } else {
                        System.out.println("DetailIds: " + detailIds); // Debug
                        dao.markAsExported(exportNoteId, detailIds, quantities, materialIds, subUnitIds, qualityIds, request);
                        String backOrderMessage = (String) request.getAttribute("backOrderMessage");
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "Export processed successfully.");
                        if (backOrderMessage != null) {
                            jsonResponse.put("backOrderMessage", backOrderMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace(); // Debug
        }

        String responseJson = jsonResponse.toString();
        System.out.println("Final Response: " + responseJson); // Debug
        out.write(responseJson);
        out.flush();
        out.close();
    }

    @Override
    public void destroy() {
        // Cleanup (handled by DBContext in DAO)
    }
}
