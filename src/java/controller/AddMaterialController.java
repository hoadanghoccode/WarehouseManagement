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

@WebServlet(name = "AddMaterialController", urlPatterns = {"/add-material"})
public class AddMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        request.setAttribute("categories", dao.getAllCategories());
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        String name = request.getParameter("name").trim();
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        String image = request.getParameter("image");
        Date createAt = new Date(System.currentTimeMillis());
        String status = "active";

        // Validation
        String error = validateInput(name, categoryId, supplierId);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("categories", dao.getAllCategories());
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
            return;
        }

        Material material = new Material(0, categoryId, supplierId, name, image, createAt, status);
        if (dao.insertMaterial(material)) {
            response.sendRedirect("list-material");
        } else {
            request.setAttribute("error", "Failed to add material. Please try again.");
            request.setAttribute("categories", dao.getAllCategories());
            request.setAttribute("suppliers", dao.getAllSuppliers());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
        }
    }

    private String validateInput(String name, int categoryId, int supplierId) {
        if (name == null || name.isEmpty()) return "Name cannot be empty.";
        if (name.length() > 250) return "Name must not exceed 250 characters.";
        if (categoryId <= 0) return "Invalid category.";
        if (supplierId <= 0) return "Invalid supplier.";
        return null;
    }
}