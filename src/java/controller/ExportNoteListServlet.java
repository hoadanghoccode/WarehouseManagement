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
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                int userId = Integer.parseInt(request.getParameter("userId"));
                Integer warehouseId = request.getParameter("warehouseId") != null && !request.getParameter("warehouseId").isEmpty() ? Integer.parseInt(request.getParameter("warehouseId")) : null;
                String customerName = request.getParameter("customerName");
                String[] materialIds = request.getParameterValues("materialId");
                String[] subUnitIds = request.getParameterValues("subUnitId");
                String[] quantities = request.getParameterValues("quantity");
                String[] qualityIds = request.getParameterValues("qualityId");

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
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Insufficient inventory for Material ID: " + materialId);
                        return;
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

                response.sendRedirect("exportnote?page=1");
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("SQL Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid input: " + e.getMessage());
            }
        } else if ("export".equals(action)) {
            response.setContentType("application/json");
            JSONObject jsonResponse = new JSONObject();
            try {
                int exportNoteId = Integer.parseInt(request.getParameter("exportNoteId"));
                String[] detailIdsArray = request.getParameterValues("detailIds");
                String[] quantitiesArray = request.getParameterValues("quantities");
                String[] materialIdsArray = request.getParameterValues("materialIds");
                String[] subUnitIdsArray = request.getParameterValues("subUnitIds");
                String[] qualityIdsArray = request.getParameterValues("qualityIds");
                String[] qualityTypesArray = request.getParameterValues("qualityTypes");

                List<Integer> detailIds = new ArrayList<>();
                List<Double> quantities = new ArrayList<>();
                List<Integer> materialIds = new ArrayList<>();
                List<Integer> subUnitIds = new ArrayList<>();
                List<Integer> qualityIds = new ArrayList<>();
                List<String> qualityTypes = new ArrayList<>();

                if (detailIdsArray != null) {
                    for (int i = 0; i < detailIdsArray.length; i++) {
                        detailIds.add(Integer.parseInt(detailIdsArray[i]));
                        quantities.add(Double.parseDouble(quantitiesArray[i]));
                        materialIds.add(Integer.parseInt(materialIdsArray[i]));
                        subUnitIds.add(Integer.parseInt(subUnitIdsArray[i]));
                        qualityIds.add(Integer.parseInt(qualityIdsArray[i]));
                        qualityTypes.add(qualityTypesArray[i]);
                    }
                }

                if (detailIds.isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "No items selected for export.");
                } else {
                    dao.markAsExported(exportNoteId, detailIds, quantities, materialIds, subUnitIds, qualityIds, qualityTypes, request);
                    String backOrderMessage = (String) request.getAttribute("backOrderMessage");
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Export processed successfully.");
                    if (backOrderMessage != null) {
                        jsonResponse.put("backOrderMessage", backOrderMessage);
                    }
                }
            } catch (SQLException e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Database error: " + e.getMessage());
            } catch (NumberFormatException e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid input: " + e.getMessage());
            }
            response.getWriter().write(jsonResponse.toString());
        }
    }

    @Override
    public void destroy() {
        // Cleanup (handled by DBContext in DAO)
    }
}