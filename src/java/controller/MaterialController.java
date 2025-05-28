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
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;
import model.Unit;
import model.Category;

@WebServlet(name = "MaterialController", urlPatterns = {"/material"})
public class MaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        MaterialDAO materialDAO = new MaterialDAO();
        switch (action) {
            case "list":
                // Xử lý phân trang
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

                Integer quantityMin = null;
                Integer quantityMax = null;
                if (quantityMinParam != null && !quantityMinParam.isEmpty()) {
                    try {
                        quantityMin = Integer.parseInt(quantityMinParam);
                    } catch (NumberFormatException e) {
                        quantityMin = null;
                    }
                }
                if (quantityMaxParam != null && !quantityMaxParam.isEmpty()) {
                    try {
                        quantityMax = Integer.parseInt(quantityMaxParam);
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

                request.setAttribute("materials", materials);
                request.setAttribute("categories", categories);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalMaterials", totalMaterials);
                request.setAttribute("search", search);
                request.setAttribute("categoryFilter", categoryFilter);
                request.setAttribute("quantityMin", quantityMinParam);
                request.setAttribute("quantityMax", quantityMaxParam);
                request.getRequestDispatcher("/materialList.jsp").forward(request, response);
                break;

            case "detail":
                int materialId = Integer.parseInt(request.getParameter("id"));
                Material material = materialDAO.getMaterialById(materialId);
                if (material != null) {
                    request.setAttribute("material", material);
                    request.getRequestDispatcher("/materialDetail.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
                }
                break;

            case "add":
                List<Category> categoriesAdd = materialDAO.getAllCategories();
                List<Unit> units = materialDAO.getAllUnits();
                request.setAttribute("categories", categoriesAdd);
                request.setAttribute("units", units);
                request.getRequestDispatcher("/addMaterial.jsp").forward(request, response);
                break;

            case "update":
                int updateMaterialId = Integer.parseInt(request.getParameter("id"));
                Material updateMaterial = materialDAO.getMaterialById(updateMaterialId);
                List<Category> updateCategories = materialDAO.getAllCategories();
                List<Unit> updateUnits = materialDAO.getAllUnits();
                if (updateMaterial != null) {
                    request.setAttribute("material", updateMaterial);
                    request.setAttribute("categories", updateCategories);
                    request.setAttribute("units", updateUnits);
                    request.getRequestDispatcher("/updateMaterial.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
                }
                break;

            case "delete":
                int deleteMaterialId = Integer.parseInt(request.getParameter("id"));
                materialDAO.deleteMaterial(deleteMaterialId);
                response.sendRedirect("material?action=list");
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        MaterialDAO materialDAO = new MaterialDAO();

        switch (action) {
            case "add":
                Material newMaterial = new Material();
                newMaterial.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                newMaterial.setUnitId(Integer.parseInt(request.getParameter("unitId")));
                newMaterial.setName(request.getParameter("name"));
                newMaterial.setDescription(request.getParameter("description"));
                newMaterial.setInventoryQuantity(Integer.parseInt(request.getParameter("inventoryQuantity")));
                newMaterial.setPrice(Double.parseDouble(request.getParameter("price")));
                newMaterial.setImage(request.getParameter("image"));
                newMaterial.setQuality(request.getParameter("quality"));
                newMaterial.setStatus("active"); // Default status
                materialDAO.addMaterial(newMaterial);
                response.sendRedirect("material?action=list");
                break;

            case "update":
                Material updatedMaterial = new Material();
                updatedMaterial.setMaterialId(Integer.parseInt(request.getParameter("materialId")));
                updatedMaterial.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                updatedMaterial.setUnitId(Integer.parseInt(request.getParameter("unitId")));
                updatedMaterial.setName(request.getParameter("name"));
                updatedMaterial.setDescription(request.getParameter("description"));
                updatedMaterial.setInventoryQuantity(Integer.parseInt(request.getParameter("inventoryQuantity")));
                updatedMaterial.setPrice(Double.parseDouble(request.getParameter("price")));
                updatedMaterial.setImage(request.getParameter("image"));
                updatedMaterial.setQuality(request.getParameter("quality"));
                updatedMaterial.setStatus(request.getParameter("status"));
                materialDAO.updateMaterial(updatedMaterial);
                response.sendRedirect("material?action=list");
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }
}