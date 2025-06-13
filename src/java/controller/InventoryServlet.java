package controller;

import dal.InventoryDAO;
import model.Category;
import model.MaterialInventory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class InventoryServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số từ request
            int categoryId = parseIntParameter(request.getParameter("categoryId"), 0);
            String searchTerm = request.getParameter("searchTerm");
            String sortBy = request.getParameter("sortBy");

            // Log tham số
            System.out.println("InventoryServlet - Parameters: categoryId=" + categoryId + 
                             ", searchTerm=" + (searchTerm != null ? searchTerm : "null") + 
                             ", sortBy=" + (sortBy != null ? sortBy : "null"));

            // Gọi DAO để lấy dữ liệu
            List<MaterialInventory> inventoryList = inventoryDAO.getInventory(categoryId, searchTerm, sortBy);
            List<Category> categoryList = inventoryDAO.getActiveCategories();

            // Log kích thước danh sách
            System.out.println("InventoryServlet - inventoryList size: " + inventoryList.size());
            System.out.println("InventoryServlet - categoryList size: " + categoryList.size());

            // Đặt thuộc tính cho request
            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("searchTerm", searchTerm != null ? searchTerm : "");
            request.setAttribute("sortBy", sortBy != null ? sortBy : "closing_qty ASC");

            // Forward đến JSP
            System.out.println("InventoryServlet - Forwarding to inventory.jsp");
            request.getRequestDispatcher("/inventory.jsp").forward(request, response);
        } catch (Exception e) {
            // Log lỗi
            System.err.println("InventoryServlet - Unexpected error: " + e.getMessage());
            e.printStackTrace();
            // Đặt thông báo lỗi
            request.setAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/inventory.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private int parseIntParameter(String param, int defaultValue) {
        if (param != null && !param.trim().isEmpty()) {
            try {
                return Integer.parseInt(param);
            } catch (NumberFormatException e) {
                System.err.println("InventoryServlet - Invalid number format for parameter: " + param);
                return defaultValue;
            }
        }
        return defaultValue;
    }
}