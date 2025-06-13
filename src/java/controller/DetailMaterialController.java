package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import dal.MaterialDAO;
import model.Material;
import model.MaterialDetail;

@WebServlet(name = "DetailMaterialController", urlPatterns = {"/detail-material"})
public class DetailMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get material ID from request parameter
        String materialIdStr = request.getParameter("id");
        if (materialIdStr == null || materialIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Material ID is required");
            return;
        }

        int materialId;
        try {
            materialId = Integer.parseInt(materialIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Material ID");
            return;
        }

        // Fetch material and its details
        MaterialDAO materialDAO = new MaterialDAO();
        Material material = materialDAO.getMaterialById(materialId);
        List<MaterialDetail> details = materialDAO.getMaterialDetailsByMaterialId(materialId);

        // Prepare response data
        if (material == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
            return;
        }

        // Create response object
        MaterialDetailResponse responseData = new MaterialDetailResponse();
        responseData.setMaterial(material);
        responseData.setDetails(details);

        // Convert to JSON
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(responseData));
    }

    // Inner class to structure the JSON response
    private static class MaterialDetailResponse {
        private Material material;
        private List<MaterialDetail> details;

        public Material getMaterial() { return material; }
        public void setMaterial(Material material) { this.material = material; }
        public List<MaterialDetail> getDetails() { return details; }
        public void setDetails(List<MaterialDetail> details) { this.details = details; }
    }
}