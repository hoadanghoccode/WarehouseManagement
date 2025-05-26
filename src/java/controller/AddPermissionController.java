/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AuthenDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import java.sql.SQLException;
import org.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Permission;

/**
 *
 * @author PC
 */
public class AddPermissionController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddPermissionController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddPermissionController at " + "hello get" + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JSONObject json = new JSONObject();
        try {
            // 1) Đọc params (form-encoded hoặc AJAX gửi)
            String resourceName = req.getParameter("resourceName");
            String description = req.getParameter("description");
//            int roleId = Integer.parseInt(req.getParameter("roleId"));

            boolean canCreate = "true".equals(req.getParameter("canCreate"));
            boolean canRead = "true".equals(req.getParameter("canRead"));
            boolean canUpdate = "true".equals(req.getParameter("canUpdate"));
            boolean canDelete = "true".equals(req.getParameter("canDelete"));

            // 2) Tạo Resource mới
            AuthenDAO dao = new AuthenDAO();
            int resourceId = dao.insertAndGetId(resourceName, description);

            // 3) Tạo Permission mới
            Permission p = new Permission();
            p.setResourceId(resourceId);
//            p.setRoleId(roleId);
            p.setRoleId(1);

            p.setCanCreate(canCreate);
            p.setCanRead(canRead);
            p.setCanUpdate(canUpdate);
            p.setCanDelete(canDelete);
            int permissionId = dao.insertAndGetId(p);

            // 4) Build JSON response
            json.put("success", true);
            json.put("permissionId", permissionId);
            json.put("resourceId", resourceId);
            json.put("resourceName", resourceName);
            json.put("canCreate", canCreate);
            json.put("canRead", canRead);
            json.put("canUpdate", canUpdate);
            json.put("canDelete", canDelete);

            resp.getWriter().write(json.toString());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.put("success", false);
            json.put("error", e.getMessage());
            resp.getWriter().write(json.toString());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
