package controller;

/**
 *
 * @author legia
 */

import dal.MaterialDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;
import com.google.gson.Gson;

@WebServlet(name = "DetailMaterialController", urlPatterns = {"/detail-material"})
public class DetailMaterialController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        String idParam = request.getParameter("id");
        String unitIdParam = request.getParameter("unitId"); 

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing material ID");
            return;
        }

        try {
            int materialId = Integer.parseInt(idParam);
            int unitId = unitIdParam != null && !unitIdParam.isEmpty() ? Integer.parseInt(unitIdParam) : -1;

            List<Material> materials = materialDAO.getMaterialsByPage(1, Integer.MAX_VALUE, "", "", null, null)
                    .stream()
                    .filter(m -> m.getMaterialId() == materialId)
                    .toList();

            if (materials.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
                return;
            }

            Material material = unitId != -1
                    ? materials.stream().filter(m -> m.getUnitId() == unitId).findFirst().orElse(materials.get(0))
                    : materials.get(0);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();
            String json = gson.toJson(material);
            response.getWriter().write(json);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid material ID or unit ID");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}