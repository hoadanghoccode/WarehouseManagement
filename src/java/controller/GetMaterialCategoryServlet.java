package controller;
import com.google.gson.Gson;
import dal.MaterialDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;

/**
 * GetMaterialCategoryServlet - Returns category information for a material
 * @author ADMIN
 */
public class GetMaterialCategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            String materialIdParam = request.getParameter("materialId");
            
            if (materialIdParam == null || materialIdParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Material ID is required\"}");
                return;
            }
            
            int materialId = Integer.parseInt(materialIdParam);
            MaterialDAO materialDAO = new MaterialDAO();
            Material material = materialDAO.getMaterialById(materialId);
            
            if (material != null) {
                Gson gson = new Gson();
                // Return toàn bộ material object
                out.print(gson.toJson(material));
                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Material not found\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Invalid material ID format\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\":\"Server error: " + e.getMessage() + "\"}");
            }
        }
    }
    
    @Override
    public String getServletInfo() {
        return "GetMaterialCategoryServlet - Returns category information for a material";
    }
}