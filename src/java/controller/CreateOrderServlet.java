/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import java.sql.Date;
import java.time.LocalDate;
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
 *
 * @author ADMIN
 */
public class CreateOrderServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateOrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        Gson gson = new Gson();
        MaterialDAO materialDAO = new MaterialDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        UnitDAO unitDAO = new UnitDAO();

        List<Supplier> supplierList = supplierDAO.getAllSuppliers();

        // Lấy tất cả materials với thông tin unit name
        List<Material> allMaterials = materialDAO.getAllMaterials();
        for (Material material : allMaterials) {
            Unit unit = unitDAO.getUnitById(material.getUnitId());
            if (unit != null) {
                material.setUnitName(unit.getName());
            }
        }

        request.setAttribute("suppliers", supplierList);
        request.setAttribute("allMaterialsJson", gson.toJson(allMaterials));
        request.getRequestDispatcher("createorder.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy thông tin cơ bản
            String orderType = request.getParameter("orderType");
            String note = request.getParameter("note");
            String supplierRaw = request.getParameter("supplier");

            // Nếu không chọn loại đơn hàng
            if (orderType == null || orderType.trim().isEmpty()) {
                returnWithError(request, response, "You must select Order Type");
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
            if (!"purchase".equals(orderType) && supplierRaw != null && !supplierRaw.trim().isEmpty()) {
                try {
                    supplierId = Integer.parseInt(supplierRaw);
                } catch (NumberFormatException e) {
                    returnWithError(request, response, "Invalid supplier");
                    return;
                }
            } else if (!"purchase".equals(orderType) && supplierRaw == null) {
                supplierId = 1;
            }

            // 2. Lấy danh sách các item
            String[] materialIds = request.getParameterValues("material[]");
            String[] quantities = request.getParameterValues("quantity[]");

            if (materialIds == null || materialIds.length == 0) {
                returnWithError(request, response, "You must add at least one order item");
                return;
            }

            // 3. Tạo Order
            Order order = new Order();
            order.setType(orderType);
            order.setNote(note);
            order.setStatus("pending");
            order.setSupplier(supplierId);
            order.setUserId(userId);
            order.setWarehouseId(1);
            LocalDate localDate = LocalDate.now();
            Date sqlDate = Date.valueOf(localDate);
            order.setCreatedAt(sqlDate);

            // 4. Xử lý gộp các item trùng materialId
            List<OrderDetail> consolidatedDetails = consolidateOrderItems(materialIds, quantities, orderType);

            if (consolidatedDetails.isEmpty()) {
                returnWithError(request, response, "No valid order items found");
                return;
            }

            order.setOrderDetails(consolidatedDetails);

            // 5. Lưu vào DB
            OrderDAO dao = new OrderDAO();
            boolean success = dao.createOrder(order);

            if (success) {
                session.setAttribute("successMessage", "Order created successfully!");
                response.sendRedirect("orderlist");
                return;
            } else {
                returnWithError(request, response, "Failed to create order due to database issue");
            }

        } catch (Exception e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorMessage", "Failed to create order: " + e.getMessage());
            response.sendRedirect("orderlist");
            return;
        }
    }

    private List<OrderDetail> consolidateOrderItems(String[] materialIds, String[] quantities, String orderType) throws Exception {
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

    private void returnWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        SupplierDAO supplierDAO = new SupplierDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        UnitDAO unitDAO = new UnitDAO();
        Gson gson = new Gson();

        // Lấy lại tất cả materials với unit name
        List<Material> allMaterials = materialDAO.getAllMaterials();
        for (Material material : allMaterials) {
            Unit unit = unitDAO.getUnitById(material.getUnitId());
            if (unit != null) {
                material.setUnitName(unit.getName());
            }
        }

        request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
        request.setAttribute("allMaterialsJson", gson.toJson(allMaterials));
        request.setAttribute("error", message);

        request.getRequestDispatcher("createorder.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
