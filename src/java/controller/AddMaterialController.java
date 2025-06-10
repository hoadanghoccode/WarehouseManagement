package controller;

import dal.MaterialDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Material;
import model.Unit;
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
        List<Material> materials = materialDAO.getAllMaterials();

        request.setAttribute("categories", categories);
        request.setAttribute("units", units);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("/addMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();

        // Get form fields
        String materialIdStr = request.getParameter("materialId");
        String newName = request.getParameter("newName");
        String categoryIdStr = request.getParameter("categoryId");
        String supplierIdStr = request.getParameter("supplierId");
        String[] unitIds = request.getParameterValues("unitIds");

        // Validation
        if (materialIdStr == null || materialIdStr.isEmpty()) {
            request.setAttribute("error", "Please select a material or enter a new name.");
            doGet(request, response);
            return;
        }

        int materialId = Integer.parseInt(materialIdStr);
        if (materialId == 0 && (newName == null || newName.trim().isEmpty())) {
            request.setAttribute("error", "New material name is required if not selecting an existing material.");
            doGet(request, response);
            return;
        }

        if (categoryIdStr == null || categoryIdStr.isEmpty()) {
            request.setAttribute("error", "Category is required.");
            doGet(request, response);
            return;
        }

        if (supplierIdStr == null || supplierIdStr.isEmpty() || Integer.parseInt(supplierIdStr) <= 0) {
            request.setAttribute("error", "Supplier is required.");
            doGet(request, response);
            return;
        }

        if (unitIds == null || unitIds.length == 0) {
            request.setAttribute("error", "At least one unit must be selected.");
            doGet(request, response);
            return;
        }

        for (String unitIdStr : unitIds) {
            int unitId = Integer.parseInt(unitIdStr);
            String priceParam = request.getParameter("price_" + unitId);
            String quantityParam = request.getParameter("quantity_" + unitId);
            if (priceParam == null || priceParam.isEmpty() || quantityParam == null || quantityParam.isEmpty()) {
                request.setAttribute("error", "Price and quantity are required for all selected units.");
                doGet(request, response);
                return;
            }
        }

        int finalMaterialId;
        int categoryId = Integer.parseInt(categoryIdStr);
        int supplierId = Integer.parseInt(supplierIdStr);

        if (materialId == 0) {
            // The user entered a "new" name.
            // Check if that name already exists:
            int existingId = materialDAO.getMaterialIdByName(newName);
            if (existingId > 0) {
                // Already exists → use that existing ID instead of inserting a duplicate
                finalMaterialId = existingId;
            } else {
                // Truly new: insert with status = "active"
                Material material = new Material();
                material.setName(newName);
                material.setCategoryId(categoryId);
                material.setStatus("active"); // default
                finalMaterialId = materialDAO.addMaterial(material);
                if (finalMaterialId == 0) {
                    request.setAttribute("error", "Failed to add material.");
                    doGet(request, response);
                    return;
                }
            }
        } else {
            // User selected an existing material from dropdown
            finalMaterialId = materialId;
        }

        // Process units (update price/inventory or insert new)
        for (String unitIdStr : unitIds) {
            int unitId = Integer.parseInt(unitIdStr);
            BigDecimal price = new BigDecimal(request.getParameter("price_" + unitId));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity_" + unitId));

            // Check if this unit exists for the material
            List<Unit> existingUnits = materialDAO.getUnitsByMaterialId(finalMaterialId);
            boolean unitExists = existingUnits.stream()
                    .anyMatch(u -> u.getUnitId() == unitId);

            if (unitExists) {
                // Update price + add to existing quantity
                BigDecimal currentQuantity = materialDAO.getCurrentQuantity(finalMaterialId, unitId);
                BigDecimal newQuantity = currentQuantity.add(quantity);
                materialDAO.updateMaterialUnitPrice(finalMaterialId, unitId, price);
                materialDAO.updateMaterialInventory(finalMaterialId, unitId, newQuantity);
            } else {
                // Insert new price & inventory rows
                materialDAO.addMaterialUnitPrice(finalMaterialId, unitId, price);
                materialDAO.addMaterialInventory(finalMaterialId, unitId, quantity);
            }
        }

        // Link to supplier
        materialDAO.addSupplierMaterial(supplierId, finalMaterialId);

        // Redirect so that list page can show a success toast
        response.sendRedirect("list-material?message=added");
    }
}
