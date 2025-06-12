/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.OrderDAO;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.Users;

/**
 * Controller xử lý hiển thị chi tiết đơn hàng
 * 
 * @author ADMIN
 */
@WebServlet(name = "OrderDetailController", urlPatterns = {"/orderdetail"})
public class OrderDetailController extends HttpServlet {

    private OrderDAO orderDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
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
            // Kiểm tra parameter có tồn tại không
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                handleError(request, response, "Order ID is required", "ERROR_MISSING_PARAMETER");
                return;
            }

            // Parse order ID
            int orderId;
            try {
                orderId = Integer.parseInt(orderIdParam.trim());
                if (orderId <= 0) {
                    handleError(request, response, "Invalid Order ID", "ERROR_INVALID_PARAMETER");
                    return;
                }
            } catch (NumberFormatException e) {
                handleError(request, response, "Order ID must be a valid number", "ERROR_INVALID_FORMAT");
                return;
            }

            // Lấy thông tin order từ database
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                handleError(request, response, "Order not found with ID: " + orderId, "ERROR_ORDER_NOT_FOUND");
                return;
            }
            
            Users user = userDAO.getUserById(order.getUserId());

            // Set attribute và forward đến JSP
            request.setAttribute("order", order);
            request.setAttribute("owner", user);
            request.setAttribute("success", true);
            request.getRequestDispatcher("orderdetail.jsp").forward(request, response);

        } catch (Exception e) {
            // Log lỗi
            System.err.println("Error in DetailOrderController.doGet(): " + e.getMessage());
            e.printStackTrace();
            
            handleError(request, response, "An unexpected error occurred while retrieving order details", "ERROR_SYSTEM");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Redirect POST requests to GET method
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
        request.setAttribute("order", null);
        
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