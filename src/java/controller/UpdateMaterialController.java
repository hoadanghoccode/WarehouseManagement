package controller;

import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;

@WebServlet(name = "UpdateMaterialController", urlPatterns = {"/update-material"})
public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO dao = new MaterialDAO();
        Material material = dao.getMaterialById(materialId);
        request.setAttribute("material", material);
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.setAttribute("categories", dao.getAllCategories()); 
        request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        String name = request.getParameter("name").trim();
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        String image = request.getParameter("image");
        String status = request.getParameter("status");

        // Validation
        String error = validateInput(name, categoryId, supplierId, image, status);
        if (error != null) {
            request.setAttribute("error", error);
            MaterialDAO dao = new MaterialDAO();
            Material material = dao.getMaterialById(materialId);
            request.setAttribute("material", material);
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.setAttribute("categories", dao.getAllCategories());
            request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
            return;
        }

        MaterialDAO dao = new MaterialDAO();
        Material material = dao.getMaterialById(materialId);
        material.setName(name);
        material.setCategoryId(categoryId);
        material.setSupplierId(supplierId);
        material.setImage(image);
        material.setStatus(status); 
        if (dao.updateMaterial(material)) {
            response.sendRedirect("list-material?success=Material updated successfully");
        } else {
            request.setAttribute("error", "Cannot update material due to pending orders or imports/exports. Please try again.");
            request.setAttribute("material", material);
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.setAttribute("categories", dao.getAllCategories());
            request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
        }
    }

    private String validateInput(String name, int categoryId, int supplierId, String image, String status) {
        if (name == null || name.isEmpty()) return "Name cannot be empty.";
        if (name.length() > 250) return "Name must not exceed 250 characters.";
        if (categoryId <= 0) return "Invalid category.";
        if (supplierId <= 0) return "Invalid supplier.";
        if (status == null || (!status.equals("active") && !status.equals("inactive"))) return "Status must be 'active' or 'inactive'.";
        if (image != null && image.length() > 500) return "Image URL must not exceed 500 characters.";
        return null;
    }
}