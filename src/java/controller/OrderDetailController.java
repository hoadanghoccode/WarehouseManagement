/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MaterialDAO;
import dal.OrderDAO;
import dal.SubUnitDAO;
import dal.SupplierDAO;
import dal.UnitDAO;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.OrderDetail;
import model.Supplier;
import model.Users;

/**
 * Controller xử lý hiển thị chi tiết đơn hàng
 *
 * @author ADMIN
 */
@WebServlet(name = "OrderDetailController", urlPatterns = {"/orderdetail"})
public class OrderDetailController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderDetailController.class.getName());

    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private SupplierDAO supplierDAO;
    private MaterialDAO materialDAO;
    private UnitDAO unitDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderDAO = new OrderDAO();
            userDAO = new UserDAO();
            supplierDAO = new SupplierDAO();
            materialDAO = new MaterialDAO();
            unitDAO = new UnitDAO();
            LOGGER.info("OrderDetailController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize OrderDetailController", e);
            throw new ServletException("Failed to initialize controller", e);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdParam = request.getParameter("oid");

        try {
            // Validation: Kiểm tra parameter có tồn tại không
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                LOGGER.warning("Missing order ID parameter");
                handleError(request, response, "Order ID is required", "ERROR_MISSING_PARAMETER");
                return;
            }

            // Validation: Parse và validate order ID
            int orderId;
            try {
                orderId = Integer.parseInt(orderIdParam.trim());
                if (orderId <= 0) {
                    LOGGER.warning("Invalid order ID: " + orderId);
                    handleError(request, response, "Invalid Order ID. Must be a positive number.", "ERROR_INVALID_PARAMETER");
                    return;
                }
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid order ID format: " + orderIdParam);
                handleError(request, response, "Order ID must be a valid number", "ERROR_INVALID_FORMAT");
                return;
            }

            HttpSession session = request.getSession(false);
            Users currentUser = (Users) session.getAttribute("USER");
            if (currentUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Business Logic: Lấy thông tin order từ database
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                handleError(request, response, "Order not found with ID: " + orderId, "ERROR_ORDER_NOT_FOUND");
                return;
            }

            // Lấy thông tin user (owner của order)
            Users user = null;
            if (order.getUserId() > 0) {
                user = userDAO.getUserById(order.getUserId());
                if (user == null) {
                    LOGGER.warning("User not found for order ID: " + orderId + ", User ID: " + order.getUserId());
                }
            }

            Supplier supplier = supplierDAO.getSupplierById(order.getSupplier());

            List<OrderDetail> orderDetailList = order.getOrderDetails();
            for (OrderDetail od : orderDetailList) {
                try {
                    if (od.getMaterialId() > 0) {

                        var material = materialDAO.getMaterialIdBy(od.getMaterialId());
                        if (material != null) {
                            od.setMaterialName(material.getName());
                            od.setMaterialImage(material.getImage());
                            od.setUnitName(unitDAO.getUnitById(material.getUnitId()).getName());
                        } else {
                            od.setMaterialName("Unknown Material");
                        }
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Error loading detail for OrderDetail ID: " + od.getOrderDetailId(), ex);
                    od.setMaterialName("N/A");
                }
            }

            // Set attributes và forward đến JSP
            request.setAttribute("order", order);
            request.setAttribute("owner", user);
            request.setAttribute("supplier", supplier);
            request.setAttribute("detail", orderDetailList);
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("success", true);

            request.getRequestDispatcher("orderdetail.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in OrderDetailController.doGet() for order ID: " + orderIdParam, e);
            handleError(request, response, "An unexpected error occurred while retrieving order details", "ERROR_SYSTEM");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String orderIdParam = request.getParameter("orderId");
        String adminNote = request.getParameter("adminNote");

        try {
            if (action == null || orderIdParam == null) {
                response.sendRedirect("orderlist");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            Order order = orderDAO.getOrderById(orderId);

            HttpSession session = request.getSession(false);
            Users currentUser = (Users) session.getAttribute("USER");
            if (currentUser == null || currentUser.getRoleId() != 2) {
                response.sendRedirect("login.jsp");
                return;
            }

            boolean success = false;
            boolean successUpdateNote = false;
            String message = "";

            if (adminNote != null && !adminNote.trim().isEmpty()) {
                String trimmedNote = adminNote.trim();

                if (trimmedNote.length() > 256) {
                    session.setAttribute("errorMessage", "Admin note must be less than 256 characters.");
                    response.sendRedirect("orderdetail?oid=" + orderId);
                    return;
                }

                if (!trimmedNote.matches("^[\\w\\s.,-]*$")) {
                    session.setAttribute("errorMessage", "Admin note contains invalid characters.");
                    response.sendRedirect("orderdetail?oid=" + orderId);
                    return;
                }

                successUpdateNote = orderDAO.updateOrderNote(orderId, trimmedNote);
            }

            if ("approve".equals(action)) {
                if (order.getType().equalsIgnoreCase("import")) {
                    success = orderDAO.approveOrderAndCreateImportNote(orderId, order.getUserId());
                    if (success) {
                        session.setAttribute("successMessage", "Import order approved successfully!");
                        response.sendRedirect("categorylist");
                        return;
                    } else {
                        message = "Failed to approve import order. Please try again.";
                    }
                } else if ("export".equals(order.getType())) {
                    success = orderDAO.approveOrderAndCreateExportNote(orderId, order.getUserId());
                    if (success) {
                        message = "Export order approved successfully!";
                    } else {
                        message = "Failed to approve export order. Please try again.";
                    }
                } else if ("purchase".equals(order.getType())) {
                    success = orderDAO.approveOrderAndCreatePurchaseNote(orderId, order.getUserId());
                    if (success) {
                        message = "Purchase order approved successfully!";
                    } else {
                        message = "Failed to approve purchase order. Please try again.";
                    }
                }
            } else if ("reject".equals(action)) {
                String newStatus = "rejected";
                success = orderDAO.updateOrderStatus(orderId, newStatus);
                if (success) {
                    message = "Order rejected successfully!";
                } else {
                    message = "Failed to reject order. Please try again.";
                }
            }

            if (success || successUpdateNote) {
                session.setAttribute("successMessage", message);
            } else {
                session.setAttribute("errorMessage", message.isEmpty() ? "Operation failed. Please try again." : message);
            }

            response.sendRedirect("orderlist");
            return;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order status", e);
            request.getSession().setAttribute("errorMessage", "An unexpected error occurred while processing the order.");
            response.sendRedirect("orderlist");
            return;
        }
    }

    /**
     * Xử lý các trường hợp lỗi
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
            String errorMessage, String errorCode)
            throws ServletException, IOException {

        if (response.isCommitted()) {
            return;
        }

        request.setAttribute("error", true);
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("errorCode", errorCode);

        LOGGER.warning("DetailOrderController Error - " + errorCode + ": " + errorMessage);

        request.getRequestDispatcher("orderdetail.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "DetailOrderController - Handles order detail display functionality";
    }
}
