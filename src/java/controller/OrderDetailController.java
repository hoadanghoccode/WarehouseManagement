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
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
                    // Không return error, chỉ log warning vì order vẫn có thể hiển thị được
                }
            }
            
            Supplier supplier = supplierDAO.getSupplierById(order.getSupplier());
            
            List<OrderDetail> orderDetailList =  order.getOrderDetails();
            for (OrderDetail od : orderDetailList) {
                od.setMaterialName(materialDAO.getMaterialIdBy(od.getMaterialId()).getName());
                od.setSubUnitName(subUnitDAO.getSubUnitById(od.getSubUnitId()).getName());
            }

            // Set attributes và forward đến JSP
            request.setAttribute("order", order);
            request.setAttribute("owner", user);
            request.setAttribute("supplier", supplier);
            request.setAttribute("detail", orderDetailList);
            request.setAttribute("success", true);

            request.getRequestDispatcher("orderdetail.jsp").forward(request, response);

        } catch (Exception e) {
            // Log lỗi chi tiết
            LOGGER.log(Level.SEVERE, "Unexpected error in OrderDetailController.doGet() for order ID: " + orderIdParam, e);

            handleError(request, response, "An unexpected error occurred while retrieving order details", "ERROR_SYSTEM");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. Redirect POST requests to GET
     * method
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST method không được hỗ trợ cho việc xem chi tiết
        // Redirect về trang danh sách order
        response.sendRedirect(request.getContextPath() + "/orderlist");
    }

    /**
     * Xử lý các trường hợp lỗi
     *
     * @param request
     * @param response
     * @param errorMessage
     * @param errorCode
     * @throws ServletException
     * @throws IOException
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
            String errorMessage, String errorCode)
            throws ServletException, IOException {

        request.setAttribute("error", true);
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("errorCode", errorCode);
//        request.setAttribute("order", null);

        // Log error để debug
        System.err.println("DetailOrderController Error - Code: " + errorCode + ", Message: " + errorMessage);

        // Forward đến JSP để hiển thị lỗi
        request.getRequestDispatcher("orderdetail.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "DetailOrderController - Handles order detail display functionality";
    }
}
