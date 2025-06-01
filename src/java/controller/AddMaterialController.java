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
        newMaterial.setStatus("active");

        // Thêm vào Material
        int materialId = materialDAO.addMaterial(newMaterial);

        // Thêm giá và đơn vị
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        materialDAO.addMaterialUnitPrice(materialId, unitId, price);

        // Thêm số lượng vào MaterialInventory
        BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
        materialDAO.addMaterialInventory(materialId, unitId, quantity);

        // Liên kết với Supplier (nếu có)
        String supplierIdStr = request.getParameter("supplierId");
        if (supplierIdStr != null && !supplierIdStr.isEmpty() && !supplierIdStr.equals("0")) {
            int supplierId = Integer.parseInt(supplierIdStr);
            materialDAO.addSupplierMaterial(supplierId, materialId);
        }

        response.sendRedirect("list-material");
    }
}