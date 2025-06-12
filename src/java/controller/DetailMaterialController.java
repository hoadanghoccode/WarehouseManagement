// Controller to handle material detail view (integrated into listMaterial.jsp)
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DetailMaterialController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Detail view is handled directly in listMaterial.jsp via modal
        response.sendRedirect("list-material");
    }
}