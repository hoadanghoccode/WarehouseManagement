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
import java.io.InputStream;
import java.net.URL;
import model.Users;
import org.json.JSONObject;
import util.GoogleUtils;

/**
 *
 * @author duong
 */
@WebServlet(name="LoginGoogleController", urlPatterns={"/login-google"})
public class LoginGoogleController extends HttpServlet {
   
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
            out.println("<title>Servlet LoginGoogleController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginGoogleController at " + request.getContextPath () + "</h1>");
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
        String code = request.getParameter("code");
        System.out.println("Google callback received with code: " + (code != null ? "not null" : "null"));
        if (code == null || code.isEmpty()) {
            System.out.println("Code is null or empty, redirecting to login.jsp");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Lấy access token từ code
            String accessToken = GoogleUtils.getToken(code);
            System.out.println("ACCESS TOKEN = " + accessToken);
            // Lấy thông tin user từ Google
            String link = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
            InputStream is = new URL(link).openStream();
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = is.read()) != -1) {
                sb.append((char) i);
            }

            JSONObject userInfo = new JSONObject(sb.toString());
            String email = userInfo.getString("email").trim().toLowerCase();
            System.out.println("EMAIL FROM GOOGLE = " + email);
            // Kiểm tra email có tồn tại trong hệ thống không
            UserDAO dao = new UserDAO();
            Users user = dao.getUserByEmail(email);
            System.out.println("User found in database: " + (user != null ? "yes" : "no"));

            if (user == null) {
                System.out.println("User not found, redirecting to login.jsp with error");
                request.setAttribute("error", "Email not registered. Please contact admin.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("USER", user);
            System.out.println("User set in session, redirecting to index.jsp");
            response.sendRedirect("index.jsp");

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("login.jsp");
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
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
