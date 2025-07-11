package controller;

import dal.MaterialDAO;
import dal.UnitDAO;
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
import java.sql.Date;
import java.time.LocalDate;


@WebServlet(name = "AddMaterialController", urlPatterns = {"/add-material"})
@MultipartConfig
public class AddMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        CategoryDAO cateDao = new CategoryDAO();
        request.setAttribute("units", new UnitDAO().getAllUnits());
        request.setAttribute("categories", dao.getAllCategories());
        request.setAttribute("parentCategories", cateDao.getAllParentCategoryWithActiveSubs("active"));
        request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO cateDao = new CategoryDAO();
        request.setAttribute("parentCategories", cateDao.getAllParentCategoryWithActiveSubs("active"));
        String name = request.getParameter("name").trim();
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        String image = null;
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream fileContent = filePart.getInputStream()) {
                image = CloudinaryController.uploadToCloudinary(fileContent);
            } catch (Exception e) {
                request.setAttribute("error", "Image upload failed: " + e.getMessage());
                MaterialDAO dao = new MaterialDAO();
                request.setAttribute("units", new UnitDAO().getAllUnits());
                request.setAttribute("categories", dao.getAllCategories());
                request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
                return;
            }
        }
        String status = "new"; 

        String error = validateInput(name, categoryId, unitId, image, status);
        if (error != null) {
            request.setAttribute("error", error);
            MaterialDAO dao = new MaterialDAO();
            request.setAttribute("units", new UnitDAO().getAllUnits());
            request.setAttribute("categories", dao.getAllCategories());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
            return;
        }

        MaterialDAO dao = new MaterialDAO();
        Material material = new Material();
        material.setName(name);
        material.setCategoryId(categoryId);
        material.setUnitId(unitId);
        material.setImage(image);
        material.setCreateAt(Date.valueOf(LocalDate.now()));
        material.setStatus(status);
        if (dao.insertMaterial(material)) {
            response.sendRedirect("list-material?success=Material added successfully");
        } else {
            request.setAttribute("error", "Cannot add material.");
            request.setAttribute("units", new UnitDAO().getAllUnits());
            request.setAttribute("categories", dao.getAllCategories());
            request.getRequestDispatcher("addMaterial.jsp").forward(request, response);
        }
    }

    private String validateInput(String name, int categoryId, int unitId, String image, String status) {
        if (name == null || name.isEmpty()) return "Name cannot be empty.";
        if (name.length() > 250) return "Name must not exceed 250 characters.";
        if (categoryId <= 0) return "Invalid category.";
        if (unitId <= 0) return "Invalid unit.";
        if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("new"))) return "Status must be 'active', 'inactive', or 'new'.";
        if (image != null && image.length() > 500) return "Image URL must not exceed 500 characters.";
        return null;
    }
}