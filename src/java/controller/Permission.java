/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.AuthenDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;



@WebServlet(name="PermissionServlet", urlPatterns={"/permission"})
public class Permission extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//         HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("userId") == null) {
//            // Chưa login, chuyển hướng về login
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }

//        String userId = session.getAttribute("userId").toString();
        try {
            AuthenDAO dao = new AuthenDAO();
            // Gọi DAO lấy danh sách Permission
            List<model.Permission> permissions = dao.getPermission();
            // Đưa vào request scope
            request.setAttribute("permissions", permissions);
            // forward đến JSP
            request.getRequestDispatcher("/authentication.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException("Không thể truy vấn permission", ex);
        }
    } 


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        processRequest(request, response);
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
