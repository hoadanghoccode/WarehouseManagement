/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.TokenResetDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TokenResetPassword;
import model.Users;
import util.ResetService;

/**
 *
 * @author duong
 */
@WebServlet(name = "AdminResetPasswordController", urlPatterns = {"/adminresetpassword"})
public class AdminResetPasswordController extends HttpServlet {

    TokenResetDAO tokenDAO = new TokenResetDAO();
    UserDAO userDAO = new UserDAO();

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
        String token = request.getParameter("token");
        HttpSession session = request.getSession();
        if (token != null) {
            TokenResetPassword tokenResetPassword = tokenDAO.getTokenPassword(token);
            ResetService service = new ResetService();
            if (tokenResetPassword == null) {
                request.setAttribute("mess", "Token invalid");
                request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
                return;
            }

            if (tokenResetPassword.isIsUsed()) {
                request.setAttribute("mess", "Token is used");
                request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
                return;
            }

            if (service.isExpireTime(tokenResetPassword.getExpiryTime())) {
                request.setAttribute("mess", "Token is expiry time");
                request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
                return;
            }

            Users user = userDAO.getUserById(tokenResetPassword.getUserId());
            request.setAttribute("email", user.getEmail());
            session.setAttribute("token", tokenResetPassword.getToken());
            request.getRequestDispatcher("adminresetrequest.jsp").forward(request, response);

        } else {
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);

        }
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

// validate password
        if (!password.equals(confirmPassword)) {
            request.setAttribute("message", "Confirm password must match password");
            request.setAttribute("email", email);
            request.getRequestDispatcher("adminresetrequest.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String tokenStr = (String) session.getAttribute("token");

// ✅ Dùng đúng DAO như ảnh
        TokenResetDAO tokenDAO = new TokenResetDAO();
        TokenResetPassword tokenResetPassword = tokenDAO.getTokenPassword(tokenStr);

// kiểm tra token hợp lệ
        ResetService service = new ResetService();

        if (tokenResetPassword == null) {
            request.setAttribute("message", "Token invalid");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        if (tokenResetPassword.isIsUsed()) {
            request.setAttribute("message", "Token is used");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        if (service.isExpireTime(tokenResetPassword.getExpiryTime())) {
            request.setAttribute("message", "Token is expired");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        tokenResetPassword.setToken(tokenStr);
        tokenResetPassword.setIsUsed(true);

        UserDAO userDAO = new UserDAO();
        userDAO.updatePasswordbyEmail(email, password);

        tokenDAO.updateStatus(tokenResetPassword);

        request.getRequestDispatcher("login.jsp").forward(request, response);
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
