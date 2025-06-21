/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.time.Instant;
import java.sql.Date;
import model.Users;
import util.CloudinaryService;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author duong
 */
@WebServlet(name="UpdateUserInfoController", urlPatterns={"/updateuserinfo"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class UpdateUserInfoController extends HttpServlet {
   
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
            out.println("<title>Servlet UpdateUserInfoController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserInfoController at " + request.getContextPath () + "</h1>");
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
        processRequest(request, response);
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
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("USER") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Users user = (Users) session.getAttribute("USER");
    UserDAO userDAO = new UserDAO();

    try {
        // Xử lý ảnh đại diện (nếu có)
        Part imagePart = request.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            InputStream inputStream = imagePart.getInputStream();
            String uploadedImageUrl = CloudinaryService.uploadToCloudinary(inputStream);
            user.setImage(uploadedImageUrl);
        }

        // Lấy các giá trị text từ form
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String dateOfBirth = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");

        if (fullName != null) user.setFullName(fullName);
        if (email != null) user.setEmail(email);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (address != null) user.setAddress(address);
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
                user.setDateOfBirth(new java.sql.Date(utilDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (gender != null) user.setGender(Boolean.parseBoolean(gender));

        // Cập nhật DB và session
        userDAO.updateUser(user, 0);
        session.setAttribute("USER", user);
        response.sendRedirect("viewuserinfo");

    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed: " + e.getMessage());
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

}
