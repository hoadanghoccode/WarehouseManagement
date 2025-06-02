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

        List<Material> materials = materialDAO.getMaterialsByPage(1, Integer.MAX_VALUE, null, null, null, null);
        int totalMaterials = materialDAO.getTotalMaterials(null, null, null, null);

        List<Category> categories = materialDAO.getAllCategories();
        List<Unit> units = materialDAO.getAllUnits();          
        List<Supplier> suppliers = materialDAO.getAllSuppliers();

        request.setAttribute("materials", materials);
        request.setAttribute("categories", categories);
        request.setAttribute("units", units);                  
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("totalMaterials", totalMaterials);

        request.getRequestDispatcher("/materialList.jsp").forward(request, response);
    }
}