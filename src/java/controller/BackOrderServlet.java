package controller;

import dal.BackOrderDAO;
import dal.OrderDAO;
import model.BackOrder;
import model.Order;
import model.OrderDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class BackOrderServlet extends HttpServlet {

    private BackOrderDAO backOrderDAO;
    private OrderDAO orderDAO;

    public BackOrderServlet() {
        this.backOrderDAO = new BackOrderDAO();
        this.orderDAO = new OrderDAO();
    }

   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        String backOrderId = request.getParameter("backOrderId");
        if (backOrderId != null) {
            BackOrder backOrder = backOrderDAO.getBackOrderById(Integer.parseInt(backOrderId));
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
                json.put("priority", backOrder.getNote() != null ? backOrder.getNote() : "Low");
            } else {
                json.put("success", false);
                json.put("message", "BackOrder not found.");
            }
            out.print(json.toString());
            out.flush();
            return;
        }

        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sortBy = request.getParameter("sortBy");
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        List<BackOrder> backOrders = backOrderDAO.getAllBackOrders(search, status, sortBy, page);
        int totalBackOrders = backOrderDAO.getTotalBackOrders(search, status);
        int totalPages = (int) Math.ceil((double) totalBackOrders / 5);
        int[] stats = backOrderDAO.getBackOrderStats(search);

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
                backOrderDAO.updateBackOrder(backOrderId, null, priority);
                jsonResponse.put("success", true);
                jsonResponse.put("message", "BackOrder updated successfully.");
            } else if ("export".equals(action)) {
                BackOrder backOrder = backOrderDAO.getBackOrderById(backOrderId);
                if (backOrder == null || backOrder.getRemainingQuantity() <= 0 || !"PENDING".equals(backOrder.getStatus())) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Invalid BackOrder status or quantity.");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }

                double remainingQty = backOrder.getRemainingQuantity();
                double availableQty = backOrder.getAvailableQuantity();
                if (availableQty < remainingQty) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Insufficient quantity in inventory.");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }

                // Check session and get user
                HttpSession session = request.getSession(false);
                if (session == null) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Session expired. Please log in again.");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }
                model.Users currentUser = (model.Users) session.getAttribute("USER");
                if (currentUser == null) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "User not authenticated. Please log in.");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }
                int userId = currentUser.getUserId();

                // Create new Order
                Order newOrder = new Order();
                newOrder.setWarehouseId(1); 
                newOrder.setUserId(userId);
                newOrder.setType("export");
                newOrder.setNote("Export from BackOrder");
                newOrder.setStatus("pending");

                List<OrderDetail> orderDetails = new ArrayList<>();
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMaterialId(backOrder.getMaterialId());
                orderDetail.setQualityId(1); 
                orderDetail.setSubUnitId(backOrder.getSubUnitId());
                orderDetail.setQuantity((int) remainingQty); 
                orderDetails.add(orderDetail);
                newOrder.setOrderDetails(orderDetails);

                if (orderDAO.createOrder(newOrder)) {
                    backOrderDAO.updateBackOrder(backOrderId, "EXPORTED", null);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "BackOrder exported successfully and new Order created.");
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to create order.");
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

    @Override
    public void destroy() {
    }
}