/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dal.CategoryDAO;
import dal.MaterialDAO;
import dal.OrderDAO;
import dal.SubUnitDAO;
import dal.SupplierDAO;
import dal.UnitDAO; // Thêm import cho UnitDAO
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Category;
import model.Material;
import model.Order;
import model.OrderDetail;
import model.SubUnit;
import model.Supplier;
import model.Units; // Thêm import cho Units model
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

        String cateId = request.getParameter("category");
        Gson gson = new Gson();
        MaterialDAO materialDAO = new MaterialDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SubUnitDAO subUnitDAO = new SubUnitDAO();
        UnitDAO unitDAO = new UnitDAO(); // Thêm UnitDAO

        List<Supplier> supplierList = supplierDAO.getAllSuppliers();
        List<Category> categoryList = categoryDAO.getAllSubCategory();
        List<SubUnit> subUnitList = subUnitDAO.getAllSubUnits("active");
        List<Units> unitList = unitDAO.getAllUnitsWithSubUnit("active"); // Lấy tất cả units

        if (cateId != null && !cateId.equals(cateId.trim())) {
            int categoryId = Integer.parseInt(cateId);

            List<Material> materialList = materialDAO.getAllMaterialsByCategoryId(categoryId);

            request.setAttribute("categoriesJson", gson.toJson(categoryList));
            request.setAttribute("subUnitsJson", gson.toJson(subUnitList));
            request.setAttribute("unitsJson", gson.toJson(unitList)); // Gửi units thay vì subunits
            request.setAttribute("suppliers", supplierList);
            request.setAttribute("materials", materialList);
            request.getRequestDispatcher("createorder.jsp").forward(request, response);
        }

        request.setAttribute("suppliers", supplierList);
        request.setAttribute("categoriesJson", gson.toJson(categoryList));
        request.setAttribute("subUnitsJson", gson.toJson(subUnitList));
        request.setAttribute("unitsJson", gson.toJson(unitList)); // Gửi units thay vì subunits
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
            if (supplierRaw != null && !supplierRaw.trim().isEmpty()) {
                try {
                    supplierId = Integer.parseInt(supplierRaw);
                } catch (NumberFormatException e) {
                    returnWithError(request, response, "Invalid supplier");
                    return;
                }
            } else {
                returnWithError(request, response, "You must select a Supplier");
                return;
            }

            // 2. Lấy danh sách các item
            String[] materialIds = request.getParameterValues("material[]");
            String[] unitIds = request.getParameterValues("unit[]");
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
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // 4. Xử lý gộp các item trùng materialId và unitId (đã convert về subunit)
            List<OrderDetail> consolidatedDetails = consolidateOrderItems(materialIds, unitIds, quantities, orderType);

            if (consolidatedDetails.isEmpty()) {
                returnWithError(request, response, "No valid order items found");
                return;
            }

            order.setOrderDetails(consolidatedDetails);

            // 5. Lưu vào DB
            OrderDAO dao = new OrderDAO();
            boolean success = dao.createOrder(order);

            if (success) {
                response.sendRedirect("orderlist?success=true");
            } else {
                returnWithError(request, response, "Failed to create order due to database issue");
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnWithError(request, response, "Error: " + e.getMessage());
        }
    }

    private List<OrderDetail> consolidateOrderItems(String[] materialIds, String[] unitIds,
            String[] quantities, String orderType) throws Exception {

        // Sử dụng Map với key là "materialId_subUnitId" để gộp các item trùng nhau
        Map<String, OrderDetail> consolidatedMap = new HashMap<>();
        UnitDAO unitDAO = new UnitDAO();

        for (int i = 0; i < materialIds.length; i++) {
            try {
                int materialId = Integer.parseInt(materialIds[i]);
                int selectedUnitId = Integer.parseInt(unitIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                if (quantity <= 0) {
                    throw new Exception("Quantity must be greater than 0 for item " + (i + 1));
                }

                // Kiểm tra xem selectedUnitId là SubUnit hay Unit cha
                int finalSubUnitId = selectedUnitId;
                int finalQuantity = quantity;
                
                // Lấy thông tin unit để kiểm tra
                Units selectedUnit = unitDAO.getUnitById(selectedUnitId);
                if (selectedUnit != null && selectedUnit.getFactor() > 1) {
                    // Đây là Unit cha, cần convert về SubUnit
                    finalSubUnitId = selectedUnit.getSubUnitId();
                    finalQuantity = (int)(quantity * selectedUnit.getFactor());
                }

                // Tạo key để nhận diện item trùng nhau (dùng subUnitId sau khi convert)
                String key = materialId + "_" + finalSubUnitId;

                if (consolidatedMap.containsKey(key)) {
                    // Nếu đã tồn tại item này, cộng thêm số lượng
                    OrderDetail existingDetail = consolidatedMap.get(key);
                    existingDetail.setQuantity(existingDetail.getQuantity() + finalQuantity);
                } else {
                    // Tạo OrderDetail mới
                    OrderDetail detail = new OrderDetail();
                    detail.setMaterialId(materialId);
                    detail.setSubUnitId(finalSubUnitId); // Lưu SubUnit ID
                    detail.setQuantity(finalQuantity); // Lưu quantity đã convert
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

    private void returnWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        SupplierDAO supplierDAO = new SupplierDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SubUnitDAO subUnitDAO = new SubUnitDAO();
        UnitDAO unitDAO = new UnitDAO(); // Thêm UnitDAO
        Gson gson = new Gson();

        request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
        request.setAttribute("categoriesJson", gson.toJson(categoryDAO.getAllSubCategory()));
        request.setAttribute("subUnitsJson", gson.toJson(subUnitDAO.getAllSubUnits("active")));
        request.setAttribute("unitsJson", gson.toJson(unitDAO.getAllUnitsWithSubUnit("active"))); // Gửi units
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