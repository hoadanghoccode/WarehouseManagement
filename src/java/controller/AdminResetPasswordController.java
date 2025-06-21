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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.sql.Timestamp;
import model.Users;
import util.ResetService;

/**
 *
 * @author duong
 */
@WebServlet(name = "AdminResetPasswordController", urlPatterns = {"/adminresetpassword"})
public class AdminResetPasswordController extends HttpServlet {

    UserDAO dao = new UserDAO();

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
            out.println("<title>Servlet AdminResetPasswordController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminResetPasswordController at " + request.getContextPath() + "</h1>");
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

        String tokenStr = request.getParameter("token");
        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));

        UserDAO dao = new UserDAO();
        Users user = dao.getUserById(userId);

        if (user == null || user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(tokenStr)) {
            response.sendRedirect("adminresetlist?error=Invalid token or user.");
            return;
        }
        ResetService service = new ResetService();

        if ("approve".equals(action)) {
            String newPassword = service.generateRandomPassword();

            dao.updatePasswordbyEmail(user.getEmail(), newPassword);
            dao.clearResetToken(user.getUserId());

            String content = "<h2>Password Reset Successful</h2>"
                    + "<p>Hello <b>" + user.getFullName() + "</b>,</p>"
                    + "<p>Your new password is: <b>" + newPassword + "</b></p>"
                    + "<p><a href='http://localhost:8080/WarehouseManagement/login'>Click here to login</a></p>";

            boolean sent = service.sendEmail(user.getEmail(), content, user.getFullName(), "Password Reset Notification");

            if (sent) {
                response.sendRedirect("adminresetlist?success=Approved and email sent successfully.");
            } else {
                response.sendRedirect("adminresetlist?error=Password approved but failed to send email.");
            }

        } else if ("reject".equals(action)) {
            dao.clearResetToken(user.getUserId());

            response.sendRedirect("adminresetlist?success=Reset request rejected successfully.");
        } else {
            response.sendRedirect("adminresetlist?error=Invalid action.");
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
  