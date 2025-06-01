/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */

import dal.MaterialDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;
import model.Category;
import model.Supplier;
import model.Unit;

@WebServlet(name = "ListMaterialController", urlPatterns = {"/list-material"})
public class ListMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();

        int page = 1;
        int pageSize = 5;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Lấy danh sách material của trang hiện tại (có phân trang)
        List<Material> materials = materialDAO.getMaterialsByPage(page, pageSize, null, null, null, null);
        int totalMaterials = materialDAO.getTotalMaterials(null, null, null, null);
        int totalPages = (int) Math.ceil((double) totalMaterials / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        // Lấy danh sách Category, Unit, Supplier
        List<Category> categories = materialDAO.getAllCategories();
        List<Unit> units = materialDAO.getAllUnits();          
        List<Supplier> suppliers = materialDAO.getAllSuppliers();

        // Đưa vào request
        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("units", units);                  
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalMaterials", totalMaterials);

        // Forward sang JSP
        request.getRequestDispatcher("/materialList.jsp").forward(request, response);
    }
}
