package controller;

import dal.BackOrderDAO;
import model.BackOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONObject;

@WebServlet(name = "BackOrderServlet", urlPatterns = {"/backorder"})
public class BackOrderServlet extends HttpServlet {

    private BackOrderDAO dao;

    public BackOrderServlet() {
        this.dao = new BackOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String backOrderId = request.getParameter("backOrderId");
            if (backOrderId != null) {
                // Xử lý yêu cầu AJAX để lấy chi tiết backorder
                BackOrder backOrder = dao.getBackOrderById(Integer.parseInt(backOrderId));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                JSONObject json = new JSONObject();
                if (backOrder != null) {
                    json.put("success", true);
                    json.put("material", backOrder.getMaterialName());
                    json.put("unit", backOrder.getSubUnitName());
                    json.put("quantity", String.format("%.2f", backOrder.getRemainingQuantity()));
                    json.put("availableQuantity", String.format("%.2f", backOrder.getAvailableQuantity()));
                    json.put("status", backOrder.getStatus());
                    json.put("note", backOrder.getNote() != null ? backOrder.getNote().replaceFirst("Priority: (LOW|MEDIUM|HIGH)\\.\\s*", "") : "");
                    json.put("priority", extractPriority(backOrder.getNote()));
                } else {
                    json.put("success", false);
                    json.put("message", "BackOrder not found.");
                }
                out.print(json.toString());
                out.flush();
                return;
            }

            // Xử lý danh sách backorder
            String search = request.getParameter("search");
            String status = request.getParameter("status");
            String sortBy = request.getParameter("sortBy");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

            List<BackOrder> backOrders = dao.getAllBackOrders(search, status, sortBy, page);
            int totalBackOrders = dao.getTotalBackOrders(search, status);
            int totalPages = (int) Math.ceil((double) totalBackOrders / 5);
            int[] stats = dao.getBackOrderStats(search);

            request.setAttribute("backOrders", backOrders);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("stats", stats);
            request.setAttribute("search", search != null ? search : "");
            request.setAttribute("status", status != null ? status : "");
            request.setAttribute("sortBy", sortBy != null ? sortBy : "");

            request.getRequestDispatcher("/backorder.jsp").forward(request, response);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page number or BackOrder ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String action = request.getParameter("action");
        try {
            int backOrderId = Integer.parseInt(request.getParameter("backOrderId"));
            if ("update".equals(action)) {
                String priority = request.getParameter("priority");
                String note = request.getParameter("note");
                String updatedNote = (priority != null ? "Priority: " + priority + ". " : "") + (note != null ? note : "");
                dao.updateBackOrder(backOrderId, null, priority, updatedNote); // Không thay đổi status
                jsonResponse.put("success", true);
                jsonResponse.put("message", "BackOrder updated successfully.");
            } else if ("export".equals(action)) {
                boolean success = dao.exportBackOrder(backOrderId);
                if (success) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "BackOrder exported successfully.");
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to export: Insufficient quantity or invalid status.");
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid action.");
            }
        } catch (SQLException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid BackOrder ID: " + e.getMessage());
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }

    private String extractPriority(String note) {
        if (note != null) {
            if (note.contains("Priority: HIGH")) return "HIGH";
            if (note.contains("Priority: MEDIUM")) return "MEDIUM";
            if (note.contains("Priority: LOW")) return "LOW";
        }
        return "LOW"; // Default priority
    }

    @Override
    public void destroy() {
    }
}