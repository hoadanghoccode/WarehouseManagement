/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.UserDAO;
import model.Users;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class UserDetailServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet UserDetailServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserDetailServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
            return;
        }

        try {
            int userId = Integer.parseInt(idRaw);
            UserDAO dao = new UserDAO();
            Users user = dao.getUserById(userId);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");

    try {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        int gender = Integer.parseInt(request.getParameter("gender"));
        String phone = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        int branchId = Integer.parseInt(request.getParameter("branchId"));
        String dob = request.getParameter("dob");

        java.sql.Date dateOfBirth = java.sql.Date.valueOf(dob);

        Users user = new Users(userId, 0, branchId, fullName, "", password, gender, phone, address, dateOfBirth);

        UserDAO dao = new UserDAO();
        dao.updateUser(user);

        response.sendRedirect("userdetail?id=" + userId);
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed");
    }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
//aa
}
