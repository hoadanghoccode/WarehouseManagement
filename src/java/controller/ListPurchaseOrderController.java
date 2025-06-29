/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */
import dal.PurchaseOrderDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.PurchaseOrders;
import model.Users;

@WebServlet(name = "ListPurchaseOrderController", urlPatterns = {"/list-purchase-order"})
public class ListPurchaseOrderController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        String usernameSearch = request.getParameter("usernameSearch");
        Integer warehouseId = parseIntOrNull(request.getParameter("warehouseId"));
        Integer supplierId = parseIntOrNull(request.getParameter("supplierId"));
        String status = request.getParameter("status");

        int page = getPageNumber(request);
        List<PurchaseOrders> purchaseOrders = dao.getAllPurchaseOrders(usernameSearch, warehouseId, supplierId, status, page, DEFAULT_PAGE_SIZE);

        int totalSize = dao.countPurchaseOrders(usernameSearch, warehouseId, supplierId, status);
        int totalPages = (totalSize % DEFAULT_PAGE_SIZE == 0) ? (totalSize / DEFAULT_PAGE_SIZE) : (totalSize / DEFAULT_PAGE_SIZE + 1);
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("USER");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("currentUser", currentUser);
        request.setAttribute("purchaseOrders", purchaseOrders);
        request.setAttribute("usernameSearch", usernameSearch);
        request.setAttribute("warehouseId", warehouseId);
        request.setAttribute("supplierId", supplierId);
        request.setAttribute("status", status);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalSize", totalSize);
        request.setAttribute("warehouses", dao.getAllWarehouses());
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.getRequestDispatcher("purchaseOrderList.jsp").forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
