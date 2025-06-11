package controller;

import dal.InventoryDAO;
import model.Category;
import model.MaterialInventory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InventoryServlet extends HttpServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int warehouseId = parseIntParameter(request.getParameter("warehouseId"), 0);
            int categoryId = parseIntParameter(request.getParameter("categoryId"), 0);
            String searchTerm = request.getParameter("searchTerm");
            String sortBy = request.getParameter("sortBy");

            List<MaterialInventory> inventoryList = inventoryDAO.getInventory(
                warehouseId, categoryId, searchTerm, sortBy
            );
            List<Category> categoryList = inventoryDAO.getActiveCategories();

            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("warehouseId", warehouseId);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("sortBy", sortBy);

            request.getRequestDispatcher("/inventory.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMsg", "An unexpected error occurred.");
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
                return defaultValue;
            }
        }
        return defaultValue;
    }
}