package controller;

import dal.CategoryDAO;
import dal.MaterialDAO;
import dal.UnitDAO;
import java.io.IOException;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Material;
import controller.CloudinaryController;

@WebServlet(name = "UpdateMaterialController", urlPatterns = {"/update-material"})
@MultipartConfig
public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO dao = new MaterialDAO();
        CategoryDAO cateDao = new CategoryDAO();
        Material material = dao.getMaterialById(materialId);
        request.setAttribute("material", material);
        request.setAttribute("categories", dao.getAllCategories());
        request.setAttribute("units", new UnitDAO().getAllUnits());
        request.setAttribute("parentCategories", cateDao.getAllParentCategoryWithActiveSubs("active"));
        request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO cateDao = new CategoryDAO();
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        MaterialDAO dao = new MaterialDAO();

        // Load existing material for status and pending checks
        Material existing = dao.getMaterialById(materialId);
        if (existing == null) {
            forwardWithError(request, response, "Material not found.", null);
            return;
        }
        
        // Disallow updates when status is 'active'
        if ("active".equalsIgnoreCase(existing.getStatus())) {
            forwardWithError(request, response, "Cannot update: Material status 'active' cannot be updated.", existing);
            return;
        }

        // Check for any pending orders/imports-exports/purchases
        boolean pendingOrder    = dao.isMaterialInOrderWithStatus(materialId, "pending");
        boolean pendingIE       = dao.isMaterialInPendingImportOrExport(materialId);
        boolean pendingPurchase = dao.isMaterialInPendingPurchaseOrder(materialId);

        // If material is 'inactive', only activate status
        if ("inactive".equalsIgnoreCase(existing.getStatus())) {
            if (pendingOrder || pendingIE || pendingPurchase) {
                StringBuilder err = new StringBuilder("Cannot activate: pending ");
                if (pendingOrder)    err.append("orders");
                if (pendingIE)       err.append(pendingOrder ? "/imports-exports" : "imports-exports");
                if (pendingPurchase) err.append(pendingOrder||pendingIE ? "/purchase orders" : "purchase orders");
                forwardWithError(request, response, err.toString(), existing);
                return;
            }
            // Activate material
            if (dao.updateStatusMaterial(materialId)) {
                response.sendRedirect("list-material?success=Material activated successfully");
            } else {
                forwardWithError(request, response, "Unknown error when activating material.", existing);
            }
            return;
        }

        // Set attributes for redisplay
        request.setAttribute("parentCategories", cateDao.getAllParentCategoryWithActiveSubs("active"));
        request.setAttribute("units", new UnitDAO().getAllUnits());
        request.setAttribute("categories", dao.getAllCategories());

        // Validate input
        String name = request.getParameter("name").trim();
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        String status = request.getParameter("status");
        String existingImage = request.getParameter("existingImage");
        String error = validateInput(name, categoryId, unitId, existingImage, status);
        if (error != null) {
            forwardWithError(request, response, error, existing);
            return;
        }

        // Handle image upload
        String image = existingImage;
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream fileContent = filePart.getInputStream()) {
                image = CloudinaryController.uploadToCloudinary(fileContent);
            } catch (Exception e) {
                forwardWithError(request, response, "Image upload failed: " + e.getMessage(), existing);
                return;
            }
        }

        // Update and persist
        existing.setName(name);
        existing.setCategoryId(categoryId);
        existing.setUnitId(unitId);
        existing.setImage(image);
        existing.setStatus(status);
        if (dao.updateMaterial(existing)) {
            response.sendRedirect("list-material?success=Material updated successfully");
        } else {
            forwardWithError(request, response, "Unknown error when updating material.", existing);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String errorMsg, Material material)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        if (material != null) {
            request.setAttribute("material", material);
        }
        request.getRequestDispatcher("updateMaterial.jsp").forward(request, response);
    }

    private String validateInput(String name, int categoryId, int unitId, String image, String status) {
        if (name == null || name.isEmpty())
            return "Name cannot be empty.";
        if (name.length() > 250)
            return "Name must not exceed 250 characters.";
        if (categoryId <= 0)
            return "Invalid category.";
        if (unitId <= 0)
            return "Invalid unit.";
        if (status == null || (!status.equals("active") && !status.equals("inactive") && !status.equals("new")))
            return "Status must be 'active', 'inactive', or 'new'.";
        if (image != null && image.length() > 500)
            return "Image URL must not exceed 500 characters.";
        return null;
    }
}
