package controller;

import com.google.gson.Gson;
import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;

@WebServlet(name = "DetailMaterialController", urlPatterns = {"/detail-material"})
public class DetailMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        String unitIdParam = request.getParameter("unitId");
        MaterialDAO materialDAO = new MaterialDAO();
        Material material = materialDAO.getMaterialByIdWithDetails(materialId);

        // Nếu có unitId, ta có thể tùy chọn logic sau (hiện tại để đơn giản, bỏ qua unitId)
        // Ghi trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(material));
    }
}
