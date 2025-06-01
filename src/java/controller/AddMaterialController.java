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

@WebServlet(name = "AddMaterialController", urlPatterns = {"/add-material"})
public class AddMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        List<Category> categories = materialDAO.getAllCategories();
        List<Unit> units = materialDAO.getAllUnits();
        List<Supplier> suppliers = materialDAO.getAllSuppliers();
        request.setAttribute("categories", categories);
        request.setAttribute("units", units);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("/addMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
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
        response.sendRedirect("list-material");
    }
}