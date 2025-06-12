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

@WebServlet(name = "UpdateMaterialController", urlPatterns = {"/update-material"})
public class UpdateMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO materialDAO = new MaterialDAO();

        // Fetch the single Material with details (including supplier)
        Material material = materialDAO.getMaterialByIdWithDetails(materialId);

        // All categories, units, suppliers for the form
        List<Category> categories = materialDAO.getAllCategories();
        List<Unit> units = materialDAO.getAllUnits();
        List<Supplier> suppliers = materialDAO.getAllSuppliers();

        request.setAttribute("material", material);
        request.setAttribute("categories", categories);
        request.setAttribute("units", units);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("/updateMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        String status = request.getParameter("status");
        String[] unitIds = request.getParameterValues("unitIds");

        if (status == null || status.isEmpty()) {
            request.setAttribute("error", "Status is required.");
            doGet(request, response);
            return;
        }

        if (unitIds == null || unitIds.length == 0) {
            request.setAttribute("error", "At least one unit must be selected.");
            doGet(request, response);
            return;
        }

        // Validate price/quantity for each selected unit
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

        // Fetch the material object (with supplier) again
        Material material = materialDAO.getMaterialByIdWithDetails(materialId);
        material.setStatus(status);
        materialDAO.updateMaterial(material);

        // Update unit-price and inventory
        for (String unitIdStr : unitIds) {
            int unitId = Integer.parseInt(unitIdStr);
            BigDecimal price = new BigDecimal(request.getParameter("price_" + unitId));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity_" + unitId));

            materialDAO.updateMaterialUnitPrice(materialId, unitId, price);
            materialDAO.updateMaterialInventory(materialId, unitId, quantity);
        }

        // Redirect and show a success toast in the list page
        response.sendRedirect("list-material?message=updated");
    }
}
