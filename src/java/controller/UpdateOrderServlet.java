package controller;

import com.google.gson.Gson;
import dal.MaterialDAO;
import dal.OrderDAO;
import dal.SupplierDAO;
import dal.UnitDAO;
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
import model.Material;
import model.Order;
import model.OrderDetail;
import model.Supplier;
import model.Unit;
import model.Users;

/**
 * UpdateOrderServlet - Handles order updates
 *
 * @author ADMIN
 */
public class UpdateOrderServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
            UnitDAO unitDAO = new UnitDAO();
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

            // Set material and unit names for order details
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Material material = materialDAO.getMaterialById(orderDetail.getMaterialId());
                if (material != null) {
                    orderDetail.setMaterialName(material.getName());
                    orderDetail.setMaterialImage(material.getImage());
                    
                    Unit unit = unitDAO.getUnitById(material.getUnitId());
                    if (unit != null) {
                        orderDetail.setUnitName(unit.getName());
                    }
                }
            }

            // Lấy tất cả materials với thông tin unit name (giống như CreateOrderServlet)
            List<Material> allMaterials = materialDAO.getAllMaterials();
            for (Material material : allMaterials) {
                Unit unit = unitDAO.getUnitById(material.getUnitId());
                if (unit != null) {
                    material.setUnitName(unit.getName());
                }
            }

            // Set attributes for JSP
            request.setAttribute("order", order);
            request.setAttribute("suppliers", supplierList);
            request.setAttribute("allMaterialsJson", gson.toJson(allMaterials));

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

            // Lấy và validate supplier (giống như CreateOrderServlet)
            int supplierId = -1;
            if (!"purchase".equals(orderType) && supplierRaw != null && !supplierRaw.trim().isEmpty()) {
                try {
                    supplierId = Integer.parseInt(supplierRaw);
                } catch (NumberFormatException e) {
                    returnWithError(request, response, orderId, "Invalid supplier");
                    return;
                }
            } else if (!"purchase".equals(orderType) && supplierRaw == null) {
                supplierId = 1;
            }

            // 2. Lấy danh sách các item
            String[] materialIds = request.getParameterValues("material[]");
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

            // 4. Xử lý gộp các item trùng materialId (giống như CreateOrderServlet)
            List<OrderDetail> consolidatedDetails = consolidateOrderItems(materialIds, quantities, orderType, orderId);

            if (consolidatedDetails.isEmpty()) {
                returnWithError(request, response, orderId, "No valid order items found");
                return;
            }

            // 5. Update order fields
            existingOrder.setType(orderType);
            existingOrder.setNote(note);
            existingOrder.setSupplier(supplierId);
            existingOrder.setUserId(userId);
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
            int orderId = (orderIdParam != null && !orderIdParam.trim().isEmpty())
                    ? Integer.parseInt(orderIdParam) : -1;
            returnWithError(request, response, orderId, "Invalid number format in form data");
        } catch (Exception e) {
            e.printStackTrace();
            String orderIdParam = request.getParameter("orderId");
            int orderId = (orderIdParam != null && !orderIdParam.trim().isEmpty())
                    ? Integer.parseInt(orderIdParam) : -1;
            returnWithError(request, response, orderId, "Error updating order: " + e.getMessage());
        }
    }

    // Sử dụng cùng logic consolidate như CreateOrderServlet
    private List<OrderDetail> consolidateOrderItems(String[] materialIds, String[] quantities, String orderType, int orderId) throws Exception {
        // Sử dụng Map với key là materialId để gộp các item trùng nhau
        Map<Integer, OrderDetail> consolidatedMap = new HashMap<>();

        for (int i = 0; i < materialIds.length; i++) {
            try {
                int materialId = Integer.parseInt(materialIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                if (quantity <= 0) {
                    throw new Exception("Quantity must be greater than 0 for item " + (i + 1));
                }

                if (consolidatedMap.containsKey(materialId)) {
                    // Nếu đã tồn tại item này, cộng thêm số lượng
                    OrderDetail existingDetail = consolidatedMap.get(materialId);
                    existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                } else {
                    // Tạo OrderDetail mới
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(orderId);
                    detail.setMaterialId(materialId);
                    detail.setQuantity(quantity);
                    detail.setQualityId("exportToRepair".equals(orderType) ? 2 : 1);

                    consolidatedMap.put(materialId, detail);
                }

            } catch (NumberFormatException ex) {
                throw new Exception("Invalid number in order item " + (i + 1));
            }
        }

        // Chuyển từ Map về List
        return new ArrayList<>(consolidatedMap.values());
    }

    // Sử dụng cùng logic returnWithError như CreateOrderServlet
    private void returnWithError(HttpServletRequest request, HttpServletResponse response,
            int orderId, String message) throws ServletException, IOException {

        SupplierDAO supplierDAO = new SupplierDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        UnitDAO unitDAO = new UnitDAO();
        Gson gson = new Gson();

        // Lấy lại tất cả materials với unit name (giống như CreateOrderServlet)
        List<Material> allMaterials = materialDAO.getAllMaterials();
        for (Material material : allMaterials) {
            Unit unit = unitDAO.getUnitById(material.getUnitId());
            if (unit != null) {
                material.setUnitName(unit.getName());
            }
        }

        request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
        request.setAttribute("allMaterialsJson", gson.toJson(allMaterials));

        // Nếu có orderId, load lại order data để hiển thị form
        if (orderId > 0) {
            try {
                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    // Set material and unit names for order details
                    for (OrderDetail orderDetail : order.getOrderDetails()) {
                        Material material = materialDAO.getMaterialById(orderDetail.getMaterialId());
                        if (material != null) {
                            orderDetail.setMaterialName(material.getName());
                            orderDetail.setMaterialImage(material.getImage());
                            
                            Unit unit = unitDAO.getUnitById(material.getUnitId());
                            if (unit != null) {
                                orderDetail.setUnitName(unit.getName());
                            }
                        }
                    }
                    request.setAttribute("order", order);
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