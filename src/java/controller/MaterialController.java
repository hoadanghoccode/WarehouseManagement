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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Material;
import model.Unit;
import model.Category;
import model.Supplier;

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
                break;

            case "add":
                List<Category> categoriesAdd = materialDAO.getAllCategories();
                List<Unit> units = materialDAO.getAllUnits();
                List<Supplier> addSuppliers = materialDAO.getAllSuppliers();
                request.setAttribute("categories", categoriesAdd);
                request.setAttribute("units", units);
                request.setAttribute("suppliers", addSuppliers);
                request.getRequestDispatcher("/addMaterial.jsp").forward(request, response);
                break;

            case "update":
                int updateMaterialId = Integer.parseInt(request.getParameter("id"));
                Material updateMaterial = materialDAO.getMaterialById(updateMaterialId);
                List<Category> updateCategories = materialDAO.getAllCategories();
                List<Unit> updateUnits = materialDAO.getAllUnits();
                List<Supplier> updateSuppliers = materialDAO.getAllSuppliers();
                if (updateMaterial != null) {
                    request.setAttribute("material", updateMaterial);
                    request.setAttribute("categories", updateCategories);
                    request.setAttribute("units", updateUnits);
                    request.setAttribute("suppliers", updateSuppliers);
                    request.getRequestDispatcher("/updateMaterial.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
                }
                break;

            case "delete":
                int deleteMaterialId = Integer.parseInt(request.getParameter("id"));
            {
                try {
                    materialDAO.deleteMaterial(deleteMaterialId);
                } catch (SQLException ex) {
                    Logger.getLogger(MaterialController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
                newMaterial.setName(request.getParameter("name"));
                newMaterial.setUnitId(Integer.parseInt(request.getParameter("unitId")));
                newMaterial.setPrice(Double.parseDouble(request.getParameter("price")));
                newMaterial.setQuantity(Double.parseDouble(request.getParameter("quantity")));
                String supplierIdStr = request.getParameter("supplierId");
                newMaterial.setSupplierId(supplierIdStr != null && !supplierIdStr.isEmpty() ? Integer.parseInt(supplierIdStr) : 0);
                newMaterial.setStatus("active");
                materialDAO.addMaterial(newMaterial);
                response.sendRedirect("material?action=list");
                break;

            case "update":
                Material updatedMaterial = new Material();
                updatedMaterial.setMaterialId(Integer.parseInt(request.getParameter("materialId")));
                updatedMaterial.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                updatedMaterial.setName(request.getParameter("name"));
                updatedMaterial.setUnitId(Integer.parseInt(request.getParameter("unitId")));
                updatedMaterial.setPrice(Double.parseDouble(request.getParameter("price")));
                updatedMaterial.setQuantity(Double.parseDouble(request.getParameter("quantity")));
                String updateSupplierIdStr = request.getParameter("supplierId");
                updatedMaterial.setSupplierId(updateSupplierIdStr != null && !updateSupplierIdStr.isEmpty() ? Integer.parseInt(updateSupplierIdStr) : 0);
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