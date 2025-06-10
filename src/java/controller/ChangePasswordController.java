/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author duong
 */
@WebServlet(name = "ChangePasswordController", urlPatterns = {"/changepassword"})
public class ChangePasswordController extends HttpServlet {

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
            out.println("<title>Servlet ChangePasswordController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePasswordController at " + request.getContextPath() + "</h1>");
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
         HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("changepassword.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Users u = (Users) request.getSession().getAttribute("USER");

        if (u == null) {
            response.sendRedirect("login");
            return;
        }

        String current = request.getParameter("currentPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!newPass.matches(passwordRegex)) {
            request.setAttribute("error", "Password must be at least 8 characters, include a letter, a number, and a special character.");
            request.getRequestDispatcher("changepassword.jsp").forward(request, response);
            return;
        }

        if (!newPass.equals(confirm)) {
            request.setAttribute("error", "New password does not match!!");
            request.getRequestDispatcher("changepassword.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        Users userFromDB = userDAO.getUserByEmail(u.getEmail());

        if (userFromDB == null) {
            request.setAttribute("error", "User not found!");
            request.getRequestDispatcher("changepassword.jsp").forward(request, response);
            return;
        }

        String hashedPassword = userFromDB.getPassword();

        if (!BCrypt.checkpw(current, hashedPassword)) {
            request.setAttribute("error", "Current password is wrong");
            request.getRequestDispatcher("changepassword.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(false); 

        if (session == null || session.getAttribute("USER") == null) {
            response.sendRedirect("login");
            return;
        }

        boolean updated = userDAO.updatePassword(u.getUserId(), newPass);
        if (updated) {
            HttpSession newSession = request.getSession(true);
            request.setAttribute("success", "Change password is successfully, please login again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error! Please try again");
            request.getRequestDispatcher("changepassword.jsp").forward(request, response);
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
