package controller;

import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;
import java.sql.Date;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import controller.CloudinaryController;
import dal.CategoryDAO;
import java.io.InputStream;


@WebServlet(name = "AddMaterialController", urlPatterns = {"/add-material"})
@MultipartConfig
public class AddMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        CategoryDAO catedao = new CategoryDAO();
//        request.setAttribute("categories", dao.getAllCategories());
        request.setAttribute("categories", catedao.getAllSubCategory());
        request.setAttribute("parentCategories", catedao.getAllParentCategoryWithActiveSubs("active"));
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        CategoryDAO catedao = new CategoryDAO();
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String categoryStr = request.getParameter("categoryId");
        String supplierStr = request.getParameter("supplierId");
        int categoryId = -1;
        int supplierId = -1;
        String error = null;

        try {
            categoryId = Integer.parseInt(categoryStr);
        } catch (NumberFormatException e) {
            error = "Please select a category.";
        }

        try {
            supplierId = Integer.parseInt(supplierStr);
        } catch (NumberFormatException e) {
            if (error == null) error = "Please select a supplier.";
        }

        // Validate name
        if (error == null) {
            error = validateInput(name, categoryId, supplierId);
        }
        
        if (error != null) {
            request.setAttribute("error", error);
//            request.setAttribute("categories", dao.getAllCategories());
            request.setAttribute("categories", catedao.getAllSubCategory());
            request.setAttribute("parentCategories", catedao.getAllParentCategoryWithActiveSubs("active"));
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
            return;
        }
        
        String image = "";
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream fileContent = filePart.getInputStream()) {
                image = CloudinaryController.uploadToCloudinary(fileContent);
            } catch (Exception e) {
                request.setAttribute("error", "Image upload failed: " + e.getMessage());
//                request.setAttribute("categories", dao.getAllCategories());
                request.setAttribute("categories", catedao.getAllSubCategory());
                request.setAttribute("parentCategories", catedao.getAllParentCategoryWithActiveSubs("active"));
                request.setAttribute("suppliers", dao.getAllSuppliers());
                request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
                return;
            }
        }

        Date createAt = new Date(System.currentTimeMillis());
        String status = "active";

        Material material = new Material(0, categoryId, supplierId, name, image, createAt, status);
        if (dao.insertMaterial(material)) {
            response.sendRedirect("list-material?success=Material added successfully");
        } else {
            request.setAttribute("error", "Material name already exists.");
//            request.setAttribute("categories", dao.getAllCategories());
            request.setAttribute("categories", catedao.getAllSubCategory());
            request.setAttribute("parentCategories", catedao.getAllParentCategoryWithActiveSubs("active"));
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
        }
    }


    private String validateInput(String name, int categoryId, int supplierId) {
        if (name == null || name.trim().isEmpty()) return "Name cannot be empty.";
        if (name.length() > 250) return "Name must not exceed 250 characters.";
        if (categoryId <= 0) return "Invalid category.";
        if (supplierId <= 0) return "Invalid supplier.";
        return null;
    }
}