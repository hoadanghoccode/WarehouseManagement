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
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import model.Users;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 *
 * @author duong
 */
@WebServlet(name="ViewUserInfoController", urlPatterns={"/viewuserinfo"})
public class ViewUserInfoController extends HttpServlet {
   
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
            out.println("<title>Servlet ViewUserInfoController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewUserInfoController at " + request.getContextPath () + "</h1>");
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
         HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Users sessionUser = (Users) session.getAttribute("USER");

        // (Tuỳ chọn) lấy mới nhất từ DB
        UserDAO userDAO = new UserDAO();
        Users latestUser = userDAO.getUserByID(sessionUser.getUserId());
        
        if (latestUser != null) {
            session.setAttribute("USER", latestUser); // Cập nhật lại trong session
            request.setAttribute("userInfo", latestUser); // Gửi tới JSP
            request.setAttribute("page", "profile");
            request.getRequestDispatcher("viewuserinfo.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp"); // hoặc lỗi 404
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
           HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("USER") == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }

    Users user = (Users) session.getAttribute("USER");
    UserDAO userDAO = new UserDAO();

    String field = request.getParameter("field");

    // Nếu là ảnh đại diện
    if ("image".equals(field)) {
        Part filePart = request.getPart("value");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            String savePath = uploadPath + File.separator + fileName;
            filePart.write(savePath);

            user.setImage(fileName);
            userDAO.updateUser(user, 0);
            session.setAttribute("USER", user); // Cập nhật lại avatar góc phải

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return;
    }

    // Nếu là các trường còn lại
    String value = request.getParameter("value");
    if (value == null || field == null || field.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    switch (field) {
        case "fullName":
            user.setFullName(value); break;
        case "email":
            user.setEmail(value); break;
        case "phoneNumber":
            user.setPhoneNumber(value); break;
        case "address":
            user.setAddress(value); break;
        case "dateOfBirth":
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                user.setDateOfBirth(new Date(utilDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            break;
        case "gender":
            user.setGender(Boolean.parseBoolean(value)); break;
    }

    userDAO.updateUser(user, 0);
    session.setAttribute("USER", user); // cập nhật session
    response.setStatus(HttpServletResponse.SC_OK);
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
