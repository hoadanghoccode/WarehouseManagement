package controller;

import dal.MaterialDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;

@WebServlet(name = "ListMaterialController", urlPatterns = {"/list-material"})
public class ListMaterialController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        String search = request.getParameter("search");
        Integer categoryId = parseIntOrNull(request.getParameter("categoryId"));
        Integer supplierId = parseIntOrNull(request.getParameter("supplierId"));
        String status = request.getParameter("status");

        List<Material> materials = dao.getAllMaterials(search, categoryId, supplierId, status);

        int page = getPageNumber(request);
        int totalSize = materials.size();
        int totalPages = (totalSize % DEFAULT_PAGE_SIZE == 0) ? (totalSize / DEFAULT_PAGE_SIZE) : (totalSize / DEFAULT_PAGE_SIZE + 1);
        if (page > totalPages && totalPages > 0) page = totalPages;

        int start = (page - 1) * DEFAULT_PAGE_SIZE;
        int end = Math.min(start + DEFAULT_PAGE_SIZE, totalSize);
        List<Material> pagedMaterials = materials.subList(start, end);

        request.setAttribute("materials", pagedMaterials);
        request.setAttribute("search", search);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("supplierId", supplierId);
        request.setAttribute("status", status);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categories", dao.getAllCategories());
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.getRequestDispatcher("materialList.jsp").forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) return 1;
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}