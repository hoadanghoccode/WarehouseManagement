package controller;

import com.google.gson.Gson;
import dal.CategoryDAO;
import dal.MaterialDAO;
import dal.OrderDAO;
import dal.SubUnitDAO;
import dal.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Order;
import model.OrderDetail;
import model.SubUnit;
import model.Supplier;
import model.Users;

/**
 * UpdateOrderServlet - Handles order updates
 * @author ADMIN
 */
public class UpdateOrderServlet extends HttpServlet {

    /**
     * Handles the HTTP GET method - Display update form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get order ID from parameter
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                request.setAttribute("error", "Order ID is required");
                request.getRequestDispatcher("orders.jsp").forward(request, response);
                return;
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            
            // Initialize DAOs
            OrderDAO orderDAO = new OrderDAO();
            SupplierDAO supplierDAO = new SupplierDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            SubUnitDAO subUnitDAO = new SubUnitDAO();
            MaterialDAO materialDAO = new MaterialDAO();
            Gson gson = new Gson();
            
            // Get order details
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("orders.jsp").forward(request, response);
                return;
            }
            
            // Get supporting data
            List<Supplier> supplierList = supplierDAO.getAllSuppliers();
            List<Category> categoryList = categoryDAO.getAllSubCategory();
            List<SubUnit> unitList = subUnitDAO.getAllSubUnits();
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                orderDetail.setMaterialName(materialDAO.getMaterialById(orderDetail.getMaterialId()).getName());
            }
            
            // Set attributes for JSP
            request.setAttribute("order", order);
            request.setAttribute("categoriesJson", gson.toJson(categoryList));
            request.setAttribute("unitsJson", gson.toJson(unitList));
            request.setAttribute("suppliers", supplierList);
            
            // Forward to update form
            request.getRequestDispatcher("editorder.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID format");
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading order: " + e.getMessage());
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP POST method - Process order update
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get order ID
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                request.setAttribute("error", "Order ID is required");
                doGet(request, response);
                return;
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            
            // Get form parameters
            String orderType = request.getParameter("orderType");
            String supplierParam = request.getParameter("supplier");
            String note = request.getParameter("note");
            
            // Validate required fields
            if (orderType == null || orderType.trim().isEmpty()) {
                request.setAttribute("error", "Order type is required");
                doGet(request, response);
                return;
            }
            
            if (supplierParam == null || supplierParam.trim().isEmpty()) {
                request.setAttribute("error", "Supplier is required");
                doGet(request, response);
                return;
            }
            
            int supplierId = Integer.parseInt(supplierParam);
            
            // Get order items
            String[] categories = request.getParameterValues("category[]");
            String[] materials = request.getParameterValues("material[]");
            String[] units = request.getParameterValues("unit[]");
            String[] quantities = request.getParameterValues("quantity[]");
            
            // Validate order items
            if (categories == null || categories.length == 0) {
                request.setAttribute("error", "At least one order item is required");
                doGet(request, response);
                return;
            }
            
            // Validate arrays have same length
            if (categories.length != materials.length || 
                materials.length != units.length || 
                units.length != quantities.length) {
                request.setAttribute("error", "Invalid order items data");
                doGet(request, response);
                return;
            }
            
            // Create order details list
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (int i = 0; i < materials.length; i++) {
                if (materials[i] != null && !materials[i].trim().isEmpty() &&
                    units[i] != null && !units[i].trim().isEmpty() &&
                    quantities[i] != null && !quantities[i].trim().isEmpty()) {
                    
                    try {
                        OrderDetail detail = new OrderDetail();
                        detail.setOrderId(orderId);
                        detail.setMaterialId(Integer.parseInt(materials[i]));
                        detail.setSubUnitId(Integer.parseInt(units[i]));
                        detail.setQuantity(Integer.parseInt(quantities[i]));
                        detail.setQualityId(1); // Default quality ID
                        
                        orderDetails.add(detail);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid data in order item " + (i + 1));
                        doGet(request, response);
                        return;
                    }
                }
            }
            
            if (orderDetails.isEmpty()) {
                request.setAttribute("error", "At least one valid order item is required");
                doGet(request, response);
                return;
            }
            
            // Get current user ID from session
             HttpSession session = request.getSession(false);
            Users user = (Users) session.getAttribute("USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Create updated order object
            OrderDAO orderDAO = new OrderDAO();
            Order existingOrder = orderDAO.getOrderById(orderId);
            if (existingOrder == null) {
                request.setAttribute("error", "Order not found");
                doGet(request, response);
                return;
            }
            
            // Update order fields
            existingOrder.setType(orderType);
            existingOrder.setSupplier(supplierId);
            existingOrder.setNote(note != null ? note.trim() : "");
            existingOrder.setOrderDetails(orderDetails);
            
            // Update order in database
            boolean success = orderDAO.updateOrder(existingOrder);
            
            if (success) {
                // Redirect to orders list with success message
                response.sendRedirect("orderdetail?oid=" + orderId +"&success=Order updated successfully");
            } else {
                request.setAttribute("error", "Failed to update order. Please try again.");
                doGet(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format in form data");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating order: " + e.getMessage());
            doGet(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "UpdateOrderServlet - Handles order updates";
    }
}