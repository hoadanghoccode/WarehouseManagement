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

@WebServlet(name = "UpdateMaterialController", urlPatterns = {"/update-material"})
public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        int materialId = Integer.parseInt(request.getParameter("id"));
        Material material = materialDAO.getMaterialById(materialId);
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
        Material updatedMaterial = new Material();
        updatedMaterial.setMaterialId(Integer.parseInt(request.getParameter("materialId")));
        updatedMaterial.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        updatedMaterial.setName(request.getParameter("name"));
        updatedMaterial.setUnitId(Integer.parseInt(request.getParameter("unitId")));
        updatedMaterial.setPrice(Double.parseDouble(request.getParameter("price")));
        updatedMaterial.setQuantity(Double.parseDouble(request.getParameter("quantity")));
        String supplierIdStr = request.getParameter("supplierId");
        updatedMaterial.setSupplierId(supplierIdStr != null && !supplierIdStr.isEmpty() ? Integer.parseInt(supplierIdStr) : 0);
        updatedMaterial.setStatus(request.getParameter("status"));
        materialDAO.updateMaterial(updatedMaterial);
        response.sendRedirect("list-material");
    }
}