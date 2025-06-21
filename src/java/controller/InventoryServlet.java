package controller;

import dal.InventoryDAO;
import model.Category;
import model.MaterialInventory;
import model.Quality;
import model.Supplier;
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
            int categoryId = parseIntParameter(request.getParameter("categoryId"), 0);
            int supplierId = parseIntParameter(request.getParameter("supplierId"), 0);
            int qualityId = parseIntParameter(request.getParameter("qualityId"), 0);
            String searchTerm = request.getParameter("searchTerm");
            String sortBy = request.getParameter("sortBy");

            System.out.println("InventoryServlet - Parameters: categoryId=" + categoryId +
                             ", supplierId=" + supplierId +
                             ", qualityId=" + qualityId +
                             ", searchTerm=" + (searchTerm != null ? searchTerm : "null") +
                             ", sortBy=" + (sortBy != null ? sortBy : "null"));

            List<MaterialInventory> inventoryList = inventoryDAO.getInventory(categoryId, supplierId, qualityId, searchTerm, sortBy);
            List<Category> categoryList = inventoryDAO.getActiveCategories();
            List<Supplier> supplierList = inventoryDAO.getActiveSuppliers();
            List<Quality> qualityList = inventoryDAO.getActiveQualities();

            System.out.println("InventoryServlet - inventoryList size: " + inventoryList.size());
            System.out.println("InventoryServlet - categoryList size: " + categoryList.size());
            System.out.println("InventoryServlet - supplierList size: " + supplierList.size());
            System.out.println("InventoryServlet - qualityList size: " + qualityList.size());

            request.setAttribute("inventoryList", inventoryList);
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("supplierList", supplierList);
            request.setAttribute("qualityList", qualityList);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("supplierId", supplierId);
            request.setAttribute("qualityId", qualityId);
            request.setAttribute("searchTerm", searchTerm != null ? searchTerm : "");
            request.setAttribute("sortBy", sortBy != null ? sortBy : "available_qty DESC");

            System.out.println("InventoryServlet - Forwarding to inventory.jsp");
            request.getRequestDispatcher("/inventory.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("InventoryServlet - Unexpected error: " + e.getMessage());
            e.printStackTrace();
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