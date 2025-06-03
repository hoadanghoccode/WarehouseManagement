package controller;

import dal.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteMaterialController", urlPatterns = {"/delete-material"})
public class DeleteMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        int materialId = Integer.parseInt(request.getParameter("id"));
        materialDAO.deleteMaterial(materialId);
        // Redirect with a parameter to trigger a success toast
        response.sendRedirect("list-material?message=deleted");
    }
}
