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
import model.Unit;
import model.Category;
import model.Supplier;

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

        String search = request.getParameter("search");
        String categoryFilter = request.getParameter("categoryFilter");
        String quantityMinParam = request.getParameter("quantityMin");
        String quantityMaxParam = request.getParameter("quantityMax");

        Double quantityMin = null;
        Double quantityMax = null;
        if (quantityMinParam != null && !quantityMinParam.isEmpty()) {
            try {
                quantityMin = Double.parseDouble(quantityMinParam);
            } catch (NumberFormatException e) {
                quantityMin = null;
            }
        }
        if (quantityMaxParam != null && !quantityMaxParam.isEmpty()) {
            try {
                quantityMax = Double.parseDouble(quantityMaxParam);
            } catch (NumberFormatException e) {
                quantityMax = null;
            }
        }

        List<Material> materials = materialDAO.getMaterialsByPage(page, pageSize, search, categoryFilter, quantityMin, quantityMax);
        int totalMaterials = materialDAO.getTotalMaterials(search, categoryFilter, quantityMin, quantityMax);
        int totalPages = (int) Math.ceil((double) totalMaterials / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Category> categories = materialDAO.getAllCategories();
        List<Supplier> suppliers = materialDAO.getAllSuppliers();

        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalMaterials", totalMaterials);
        request.setAttribute("search", search);
        request.setAttribute("categoryFilter", categoryFilter);
        request.setAttribute("quantityMin", quantityMinParam);
        request.setAttribute("quantityMax", quantityMaxParam);
        request.getRequestDispatcher("/materialList.jsp").forward(request, response);
    }
}