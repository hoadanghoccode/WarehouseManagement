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
import java.math.BigDecimal;
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

@WebServlet(name = "UpdateMaterialController", urlPatterns = {"/update-material", "/update-material-status"})
public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        int materialId = Integer.parseInt(request.getParameter("id"));
        Material material = materialDAO.getMaterialByIdWithDetails(materialId);
        List<Category> categories = materialDAO.getAllCategories();
        List<Unit> units = materialDAO.getAllUnits();
        List<Supplier> suppliers = materialDAO.getAllSuppliers();
        if (material != null) {
            request.setAttribute("material", material);
            request.setAttribute("categories", categories);
            request.setAttribute("units", units);
            request.setAttribute("suppliers", suppliers);
            request.getRequestDispatcher("/updateMaterial.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            Material updatedMaterial = materialDAO.getMaterialByIdWithDetails(materialId);
            if (updatedMaterial != null) {
                updatedMaterial.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                updatedMaterial.setName(request.getParameter("name"));
                updatedMaterial.setStatus(request.getParameter("status"));

                materialDAO.updateMaterial(updatedMaterial);

                int unitId = Integer.parseInt(request.getParameter("unitId"));
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                materialDAO.updateMaterialUnitPrice(materialId, unitId, price);

                BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
                materialDAO.updateMaterialInventory(materialId, unitId, quantity);

                materialDAO.deleteSupplierMaterial(materialId);
                String supplierIdStr = request.getParameter("supplierId");
                if (supplierIdStr != null && !supplierIdStr.isEmpty() && !supplierIdStr.equals("0")) {
                    int supplierId = Integer.parseInt(supplierIdStr);
                    materialDAO.addSupplierMaterial(supplierId, materialId);
                }
            }
        } else if ("status".equals(action)) {
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            String status = request.getParameter("status");
            Material material = materialDAO.getMaterialByIdWithDetails(materialId);
            if (material != null) {
                material.setStatus(status);
                materialDAO.updateMaterial(material);
            }
        }
        response.sendRedirect("list-material");
    }
}