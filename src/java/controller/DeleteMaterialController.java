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
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO dao = new MaterialDAO();
        dao.deleteMaterial(materialId);
        response.sendRedirect("list-material");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}