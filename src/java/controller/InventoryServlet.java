package controller;

import dal.InventoryDAO;
import model.MaterialInventory;
import model.Category;
import model.Quality;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import java.math.BigDecimal;

public class InventoryServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        System.out.println("InventoryServlet - Received request with action: " + (action != null ? action : "null"));

        try {
            if ("getLatestDetails".equals(action)) {
                handleGetLatestDetails(request, response);
            } else {
                handleInventoryList(request, response);
            }
        } catch (Exception e) {
            System.err.println("InventoryServlet - Unexpected error: " + e.getMessage());
            e.printStackTrace();
            if ("getLatestDetails".equals(action)) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                MaterialInventory errorResponse = new MaterialInventory();
                errorResponse.setNote("An unexpected error occurred: " + e.getMessage());
                out.write(gson.toJson(errorResponse));
                out.flush();
            } else {
                request.setAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
                request.getRequestDispatcher("/inventory.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void handleInventoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = parseIntParameter(request.getParameter("categoryId"), 0);
        int qualityId = parseIntParameter(request.getParameter("qualityId"), 0);
        String searchTerm = request.getParameter("searchTerm");
        String sortBy = request.getParameter("sortBy");

        System.out.println("InventoryServlet - Parameters: categoryId=" + categoryId +
                         ", qualityId=" + qualityId +
                         ", searchTerm=" + (searchTerm != null ? searchTerm : "null") +
                         ", sortBy=" + (sortBy != null ? sortBy : "null"));

        List<MaterialInventory> inventoryList = inventoryDAO.getInventory(categoryId, qualityId, searchTerm, sortBy);
        List<Category> categoryList = inventoryDAO.getActiveCategories();
        List<Quality> qualityList = inventoryDAO.getActiveQualities();

        System.out.println("InventoryServlet - inventoryList size: " + inventoryList.size());
        System.out.println("InventoryServlet - categoryList size: " + categoryList.size());
        System.out.println("InventoryServlet - qualityList size: " + qualityList.size());

        request.setAttribute("inventoryList", inventoryList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("qualityList", qualityList);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("qualityId", qualityId);
        request.setAttribute("searchTerm", searchTerm != null ? searchTerm : "");
        request.setAttribute("sortBy", sortBy != null ? sortBy : "available_qty DESC");

        // Calculate summary for stats
        BigDecimal totalAvailableQty = inventoryList.stream()
                .map(MaterialInventory::getAvailableQty)
                .filter(qty -> qty != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNotAvailableQty = inventoryList.stream()
                .map(MaterialInventory::getNotAvailableQty)
                .filter(qty -> qty != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        request.setAttribute("summary", new MaterialInventory(totalAvailableQty, totalNotAvailableQty));

        System.out.println("InventoryServlet - Forwarding to inventory.jsp");
        request.getRequestDispatcher("/inventory.jsp").forward(request, response);
    }

    private void handleGetLatestDetails(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int materialId = parseIntParameter(request.getParameter("materialId"), 0);
        int subUnitId = parseIntParameter(request.getParameter("subUnitId"), 0);

        System.out.println("InventoryServlet - Fetching details for materialId=" + materialId +
                         ", subUnitId=" + subUnitId);

        MaterialInventory inventory = inventoryDAO.getLatestInventoryDetails(materialId, subUnitId);
        if (inventory == null) {
            System.out.println("InventoryServlet - No data found for materialId=" + materialId + ", subUnitId=" + subUnitId);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(gson.toJson(inventory != null ? inventory : new MaterialInventory()));
        out.flush();
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