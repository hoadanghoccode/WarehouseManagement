// Controller to update material
package controller;

import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;

public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO dao = new MaterialDAO();
        Material material = dao.getMaterialById(materialId);
        request.setAttribute("material", material);
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        String name = request.getParameter("name").trim();
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        String image = request.getParameter("image");

        // Validation
        String error = validateInput(name, supplierId);
        if (error != null) {
            request.setAttribute("error", error);
            MaterialDAO dao = new MaterialDAO();
            Material material = dao.getMaterialById(materialId);
            request.setAttribute("material", material);
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
            return;
        }

        MaterialDAO dao = new MaterialDAO();
        Material material = dao.getMaterialById(materialId);
        material.setName(name);
        material.setSupplierId(supplierId);
        material.setImage(image);
        if (dao.updateMaterial(material)) {
            response.sendRedirect("list-material");
        } else {
            request.setAttribute("error", "Failed to update material. Please try again.");
            request.setAttribute("material", material);
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
        }
    }

    private String validateInput(String name, int supplierId) {
        if (name == null || name.isEmpty()) return "Name cannot be empty.";
        if (name.length() > 250) return "Name must not exceed 250 characters.";
        if (supplierId <= 0) return "Invalid supplier.";
        return null;
    }
}