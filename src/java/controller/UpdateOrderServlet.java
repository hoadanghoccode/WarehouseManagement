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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            List<SubUnit> unitList = subUnitDAO.getAllSubUnits("active");
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
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Get order ID
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                returnWithError(request, response, -1, "Order ID is required");
                return;
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            
            // 1. Lấy thông tin cơ bản
            String orderType = request.getParameter("orderType");
            String note = request.getParameter("note");
            String supplierRaw = request.getParameter("supplier");
            
            // Nếu không chọn loại đơn hàng
            if (orderType == null || orderType.trim().isEmpty()) {
                returnWithError(request, response, orderId, "You must select Order Type");
                return;
            }
            
            // Lấy session và kiểm tra đăng nhập
            HttpSession session = request.getSession(false);
            Users user = (Users) session.getAttribute("USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int userId = user.getUserId();
            
            // Lấy và validate supplier
            int supplierId = -1;
            // Xử lý purchase của b Giang
            if (!"purchase".equals(orderType) && supplierRaw != null && !supplierRaw.trim().isEmpty()) {
                try {
                    supplierId = Integer.parseInt(supplierRaw);
                } catch (NumberFormatException e) {
                    returnWithError(request, response, orderId, "Invalid supplier");
                    return;
                }
            } else if (!"purchase".equals(orderType)) {
                returnWithError(request, response, orderId, "You must select a Supplier");
                return;
            }
            
            // 2. Lấy danh sách các item
            String[] materialIds = request.getParameterValues("material[]");
            String[] unitIds = request.getParameterValues("unit[]");
            String[] quantities = request.getParameterValues("quantity[]");
            
            if (materialIds == null || materialIds.length == 0) {
                returnWithError(request, response, orderId, "You must add at least one order item");
                return;
            }
            
            // 3. Get existing order
            OrderDAO orderDAO = new OrderDAO();
            Order existingOrder = orderDAO.getOrderById(orderId);
            if (existingOrder == null) {
                returnWithError(request, response, orderId, "Order not found");
                return;
            }
            
            // 4. Xử lý gộp các item trùng materialId và unitId
            List<OrderDetail> consolidatedDetails = consolidateOrderItems(materialIds, unitIds, quantities, orderType, orderId);
            
            if (consolidatedDetails.isEmpty()) {
                returnWithError(request, response, orderId, "No valid order items found");
                return;
            }
            
            // 5. Update order fields
            existingOrder.setType(orderType);
            existingOrder.setNote(note);
            existingOrder.setSupplier(supplierId);
            existingOrder.setOrderDetails(consolidatedDetails);
            
            // 6. Lưu vào DB
            boolean success = orderDAO.updateOrder(existingOrder);
            
            if (success) {
                response.sendRedirect("orderdetail?oid=" + orderId + "&success=Order updated successfully");
            } else {
                returnWithError(request, response, orderId, "Failed to update order due to database issue");
            }
            
        } catch (NumberFormatException e) {
            String orderIdParam = request.getParameter("orderId");
            int orderId = (orderIdParam != null && !orderIdParam.trim().isEmpty()) ? 
                          Integer.parseInt(orderIdParam) : -1;
            returnWithError(request, response, orderId, "Invalid number format in form data");
        } catch (Exception e) {
            e.printStackTrace();
            String orderIdParam = request.getParameter("orderId");
            int orderId = (orderIdParam != null && !orderIdParam.trim().isEmpty()) ? 
                          Integer.parseInt(orderIdParam) : -1;
            returnWithError(request, response, orderId, "Error updating order: " + e.getMessage());
        }
    }
    
    private List<OrderDetail> consolidateOrderItems(String[] materialIds, String[] unitIds,
            String[] quantities, String orderType, int orderId) throws Exception {

        // Sử dụng Map với key là "materialId_unitId" để gộp các item trùng nhau
        Map<String, OrderDetail> consolidatedMap = new HashMap<>();

        for (int i = 0; i < materialIds.length; i++) {
            try {
                int materialId = Integer.parseInt(materialIds[i]);
                int unitId = Integer.parseInt(unitIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                if (quantity <= 0) {
                    throw new Exception("Quantity must be greater than 0 for item " + (i + 1));
                }

                // Tạo key để nhận diện item trùng nhau
                String key = materialId + "_" + unitId;

                if (consolidatedMap.containsKey(key)) {
                    // Nếu đã tồn tại item này, cộng thêm số lượng
                    OrderDetail existingDetail = consolidatedMap.get(key);
                    existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                } else {
                    // Tạo OrderDetail mới
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(orderId);
                    detail.setMaterialId(materialId);
                    detail.setSubUnitId(unitId);
                    detail.setQuantity(quantity);
                    detail.setQualityId("exportToRepair".equals(orderType) ? 2 : 1);

                    consolidatedMap.put(key, detail);
                }

            } catch (NumberFormatException ex) {
                throw new Exception("Invalid number in order item " + (i + 1));
            }
        }

        // Chuyển từ Map về List
        return new ArrayList<>(consolidatedMap.values());
    }
    
    private void returnWithError(HttpServletRequest request, HttpServletResponse response, 
                               int orderId, String message) throws ServletException, IOException {

        // Nếu có orderId, load lại order data để hiển thị form
        if (orderId > 0) {
            try {
                OrderDAO orderDAO = new OrderDAO();
                SupplierDAO supplierDAO = new SupplierDAO();
                CategoryDAO categoryDAO = new CategoryDAO();
                SubUnitDAO subUnitDAO = new SubUnitDAO();
                MaterialDAO materialDAO = new MaterialDAO();
                Gson gson = new Gson();

                Order order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    List<Supplier> supplierList = supplierDAO.getAllSuppliers();
                    List<Category> categoryList = categoryDAO.getAllSubCategory();
                    List<SubUnit> unitList = subUnitDAO.getAllSubUnits("active");
                    
                    for (OrderDetail orderDetail : order.getOrderDetails()) {
                        orderDetail.setMaterialName(materialDAO.getMaterialById(orderDetail.getMaterialId()).getName());
                    }

                    request.setAttribute("order", order);
                    request.setAttribute("suppliers", supplierList);
                    request.setAttribute("categoriesJson", gson.toJson(categoryList));
                    request.setAttribute("unitsJson", gson.toJson(unitList));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("error", message);
        request.getRequestDispatcher("editorder.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "UpdateOrderServlet - Handles order updates";
    }
}