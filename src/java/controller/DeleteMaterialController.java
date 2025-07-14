// DeleteMaterialController.java
package controller;

import dal.MaterialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "DeleteMaterialController", urlPatterns = {"/delete-material"})
public class DeleteMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        MaterialDAO dao = new MaterialDAO();
        boolean deleted = dao.deleteMaterial(materialId);

        if (deleted) {
            response.sendRedirect("list-material?success=Deactivated successfully");
        } else {
            response.sendRedirect("list-material?error=Cannot deactivate: pending orders/imports/exports or in purchase orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
