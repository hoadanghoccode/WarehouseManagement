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
import model.SubCategory;

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
                List<Material> materials = materialDAO.getAllMaterials();
                request.setAttribute("materials", materials);
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
                List<SubCategory> subCategories = materialDAO.getAllSubCategories();
                request.setAttribute("subCategories", subCategories);
                request.getRequestDispatcher("/addMaterial.jsp").forward(request, response);
                break;

            case "update":
                materialId = Integer.parseInt(request.getParameter("id"));
                material = materialDAO.getMaterialById(materialId);
                subCategories = materialDAO.getAllSubCategories();
                request.setAttribute("material", material);
                request.setAttribute("subCategories", subCategories);
                request.getRequestDispatcher("/updateMaterial.jsp").forward(request, response);
                break;

            case "delete":
                materialId = Integer.parseInt(request.getParameter("id"));
                materialDAO.deleteMaterial(materialId);
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
                newMaterial.setSubCategoryId(Integer.parseInt(request.getParameter("subCategoryId")));
                newMaterial.setName(request.getParameter("name"));
                newMaterial.setUnitOfCalculation(request.getParameter("unitOfCalculation"));
                newMaterial.setInventoryQuantity(Integer.parseInt(request.getParameter("inventoryQuantity")));
                materialDAO.addMaterial(newMaterial);
                response.sendRedirect("material?action=list");
                break;

            case "update":
                Material updatedMaterial = new Material();
                updatedMaterial.setMaterialId(Integer.parseInt(request.getParameter("materialId")));
                updatedMaterial.setSubCategoryId(Integer.parseInt(request.getParameter("subCategoryId")));
                updatedMaterial.setName(request.getParameter("name"));
                updatedMaterial.setUnitOfCalculation(request.getParameter("unitOfCalculation"));
                updatedMaterial.setInventoryQuantity(Integer.parseInt(request.getParameter("inventoryQuantity")));
                materialDAO.updateMaterial(updatedMaterial);
                response.sendRedirect("material?action=list");
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }
}