/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MaterialDAO;
import dal.OrderDAO;
import dal.SubUnitDAO;
import dal.SupplierDAO;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private SubUnitDAO subUnitDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderDAO = new OrderDAO();
            userDAO = new UserDAO();
            supplierDAO = new SupplierDAO();
            materialDAO = new MaterialDAO();
            subUnitDAO = new SubUnitDAO();
            LOGGER.info("OrderDetailController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize OrderDetailController", e);
            throw new ServletException("Failed to initialize controller", e);
        }
    }

    /**
     * Inner class để lưu thông tin stock availability
     */
    public static class StockInfo {
        private int materialId;
        private String materialName;
        private int subUnitId;
        private String subUnitName;
        private int qualityId;
        private double requestedQuantity;
        private double availableQuantity;
        private boolean hasStock;

        public StockInfo(int materialId, String materialName, int subUnitId, String subUnitName, 
                        int qualityId, double requestedQuantity, double availableQuantity) {
            this.materialId = materialId;
            this.materialName = materialName;
            this.subUnitId = subUnitId;
            this.subUnitName = subUnitName;
            this.qualityId = qualityId;
            this.requestedQuantity = requestedQuantity;
            this.availableQuantity = availableQuantity;
            this.hasStock = availableQuantity >= requestedQuantity;
        }

        // Getters
        public int getMaterialId() { return materialId; }
        public String getMaterialName() { return materialName; }
        public int getSubUnitId() { return subUnitId; }
        public String getSubUnitName() { return subUnitName; }
        public int getQualityId() { return qualityId; }
        public double getRequestedQuantity() { return requestedQuantity; }
        public double getAvailableQuantity() { return availableQuantity; }
        public boolean hasStock() { return hasStock; }
    }

    /**
     * Kiểm tra stock availability cho export order
     */
    private List<StockInfo> checkExportStockAvailability(int orderId) throws Exception {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new Exception("Order not found");
        }

        List<StockInfo> stockInfoList = new ArrayList<>();
        
        String getMaterialStockQuery = """
            SELECT md.Quantity as available_quantity 
            FROM Material_detail md 
            WHERE md.Material_id = ? AND md.SubUnit_id = ? AND md.Quality_id = ?
            """;

        try (PreparedStatement ps = orderDAO.getConnection().prepareStatement(getMaterialStockQuery)) {
            for (OrderDetail detail : order.getOrderDetails()) {
                ps.setInt(1, detail.getMaterialId());
                ps.setInt(2, detail.getSubUnitId());
                ps.setInt(3, detail.getQualityId() > 0 ? detail.getQualityId() : 1);

                try (ResultSet rs = ps.executeQuery()) {
                    double availableQuantity = 0;
                    if (rs.next()) {
                        availableQuantity = rs.getDouble("available_quantity");
                    }

                    // Lấy tên material và subunit
                    String materialName = detail.getMaterialName();
                    if (materialName == null || materialName.trim().isEmpty()) {
                        var material = materialDAO.getMaterialIdBy(detail.getMaterialId());
                        materialName = material != null ? material.getName() : "Unknown Material";
                    }

                    String subUnitName = detail.getSubUnitName();
                    if (subUnitName == null || subUnitName.trim().isEmpty()) {
                        var subunit = subUnitDAO.getSubUnitById(detail.getSubUnitId());
                        subUnitName = subunit != null ? subunit.getName() : "Unknown Unit";
                    }

                    StockInfo stockInfo = new StockInfo(
                        detail.getMaterialId(),
                        materialName,
                        detail.getSubUnitId(),
                        subUnitName,
                        detail.getQualityId() > 0 ? detail.getQualityId() : 1,
                        detail.getQuantity(),
                        availableQuantity
                    );
                    
                    stockInfoList.add(stockInfo);
                }
            }
        }

        return stockInfoList;
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdParam = request.getParameter("oid");
        String checkStock = request.getParameter("checkStock");

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
                        } else {
                            od.setMaterialName("Unknown Material");
                        }
                    }

                    if (od.getSubUnitId() > 0) {
                        var subunit = subUnitDAO.getSubUnitById(od.getSubUnitId());
                        if (subunit != null) {
                            od.setSubUnitName(subunit.getName());
                        } else {
                            od.setSubUnitName("Unknown Unit");
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Error loading detail for OrderDetail ID: " + od.getOrderDetailId(), ex);
                    od.setMaterialName("N/A");
                    od.setSubUnitName("N/A");
                }
            }

            // Kiểm tra stock cho export order nếu được yêu cầu
            if ("true".equals(checkStock) && 
                ("export".equals(order.getType()) || "exportToRepair".equals(order.getType()))) {
                
                try {
                    List<StockInfo> stockInfoList = checkExportStockAvailability(orderId);
                    request.setAttribute("stockInfoList", stockInfoList);
                    
                    // Kiểm tra xem có item nào thiếu stock không
                    boolean hasInsufficientStock = stockInfoList.stream()
                        .anyMatch(stock -> !stock.hasStock());
                    
                    request.setAttribute("hasInsufficientStock", hasInsufficientStock);
                    request.setAttribute("showStockModal", true);
                    
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error checking stock availability", e);
                    request.setAttribute("errorMessage", "Unable to check stock availability");
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
    @Override
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

            // Kiểm tra quyền admin
            HttpSession session = request.getSession(false);
            Users currentUser = (Users) session.getAttribute("USER");
            if (currentUser == null || currentUser.getRoleId() != 1) {
                response.sendRedirect("login.jsp");
                return;
            }

            boolean success = false;
            boolean successUpdateNote = false;
            
            if (!adminNote.trim().equals("")) {
                successUpdateNote = orderDAO.updateOrderNote(orderId, adminNote);
            }
            
            String newStatus = "";

            if ("approve".equals(action)) {
                if (order.getType().equalsIgnoreCase("import")) {
                    // Import order - approve normally
                    success = orderDAO.approveOrderAndCreateImportNote(orderId, currentUser.getUserId());
                } else if ("export".equals(order.getType()) || "exportToRepair".equals(order.getType())) {
                    // Export order - use partial export method
                    success = orderDAO.approveOrderAndCreateExportNote(orderId, currentUser.getUserId(), 
                              order.getUserName()!= null ? order.getUserName(): "");
                }
            } else if ("checkStock".equals(action)) {
                // Redirect to check stock availability
                response.sendRedirect("orderdetail?oid=" + orderId + "&checkStock=true");
                return;
            } else if ("reject".equals(action)) {
                newStatus = "rejected";
                success = orderDAO.updateOrderStatus(orderId, newStatus);
            }

            if (success || successUpdateNote) {
                request.setAttribute("successMessage", "Order has been updated successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to update order status. Please try again.");
            }

            // Redirect về trang detail để hiển thị kết quả
            response.sendRedirect("orderdetail?oid=" + orderId);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order status", e);
            response.sendRedirect("orderlist");
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