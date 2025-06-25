/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AuditInventoryDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import model.InventoryAudit;
import java.util.List;

/**
 *
 * @author PC
 */
public class AuditListController extends HttpServlet {

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
            out.println("<title>Servlet AuditListController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuditListController at " + request.getContextPath() + "</h1>");
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
        // 1. Lấy user từ session
        Users user = (Users) request.getSession().getAttribute("USER");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int userId = user.getUserId();
        boolean isAdmin = (user.getRoleId() == 1); // Giả sử RoleId = 1 là Admin, tùy hệ thống bạn sửa lại

        // 2. Nhận filter từ query string
        String code = request.getParameter("code");
        String status = request.getParameter("status");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String createdBy = request.getParameter("createdBy");

        // 3. Phân trang
        int page = 1;
        int pageSize = 10;
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) {
                    page = 1;
                }
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        List<InventoryAudit> auditList = null;
        List<Users> userList = null;
        int totalAudit = 0, numPages = 0;
        try {
            AuditInventoryDAO auditDao = new AuditInventoryDAO();
            totalAudit = auditDao.countAuditList(code, status, dateFrom, dateTo, createdBy, isAdmin, userId);
            numPages = (int) Math.ceil((double) totalAudit / pageSize);
            auditList = auditDao.getAuditList(code, status, dateFrom, dateTo, createdBy, page, pageSize, isAdmin, userId);

            // Lấy danh sách user filter cho dropdown
            UserDAO userDao = new UserDAO();
            userList = auditDao.getAll();

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(500, "Internal Server Error: " + ex.getMessage());
            return;
        }

        // Gán attribute cho JSP
        request.setAttribute("auditList", auditList);
        request.setAttribute("userList", userList);
        request.setAttribute("totalAudit", totalAudit);
        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("numPages", numPages);

        request.setAttribute("code", code);
        request.setAttribute("status", status);
        request.setAttribute("dateFrom", dateFrom);
        request.setAttribute("dateTo", dateTo);
        request.setAttribute("createdBy", createdBy);

        // Forward tới JSP (chỉ chạy khi không có lỗi)
        request.getRequestDispatcher("/inventoryaudit.jsp").forward(request, response);
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
        processRequest(request, response);
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
