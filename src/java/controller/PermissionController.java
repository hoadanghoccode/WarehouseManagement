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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import model.Permission;

/**
 *
 * @author PC
 */
public class PermissionController extends HttpServlet {

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
            out.println("<title>Servlet PermissionController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PermissionController hello " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        String userIdParam = request.getParameter("userId");
//        int userId;
//        if (userIdParam != null) {
//            try {
//                userId = Integer.parseInt(userIdParam);
//            } catch (NumberFormatException e) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId");
//                return;
//            }
//        } else {
//            HttpSession session = request.getSession(false);
//            if (session != null && session.getAttribute("userId") != null) {
//                Object uid = session.getAttribute("userId");
//                try {
//                    userId = Integer.parseInt(uid.toString());
//                } catch (NumberFormatException e) {
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId in session");
//                    return;
//                }
//            } else {
//                response.sendRedirect(request.getContextPath() + "/login.jsp");
//                return;
//            }
//        }

        try {
            AuthenDAO dao = new AuthenDAO();
//            String permissions = dao.getPermissionJson(userId);
//            String permissions = dao.getPermissionJson(1);
//
//            request.setAttribute("permissions", permissions);
//            request.getRequestDispatcher("/authentication.jsp").forward(request, response);
            // Lấy list permission
            List<Permission> list = dao.getPermission();
// Đẩy thẳng List<Permission> vào request
            request.setAttribute("permissionsList", list);
            System.out.println("DEBUG: permissions count = " + list.size());
            System.out.println("DEBUG: permissions = " + list);
// Forward về JSP
            request.getRequestDispatcher("/authentication.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException("Không thể truy vấn permission", ex);
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
        // Lấy userId động: ưu tiên request parameter, rồi session
//        String userIdParam = request.getParameter("userId");
//        int userId;
//        if (userIdParam != null && !userIdParam.isEmpty()) {
//            userId = Integer.parseInt(userIdParam);
//        } else {
//            HttpSession session = request.getSession(false);
//            if (session != null && session.getAttribute("userId") != null) {
//                userId = Integer.parseInt(session.getAttribute("userId").toString());
//            } else {
//                // không có user, chuyển hướng về login hoặc báo lỗi
//                response.sendRedirect(request.getContextPath() + "/login.jsp");
//                return;
//            }
//        }

        // Tiếp tục xử lý các permId, checkbox…
        String[] permIds = request.getParameterValues("permId");
        String[] createIds = request.getParameterValues("canCreate");
        String[] readIds = request.getParameterValues("canRead");
        String[] updateIds = request.getParameterValues("canUpdate");
        String[] deleteIds = request.getParameterValues("canDelete");

        // Chuyển sang Set<Integer>…
        Set<Integer> creates = toIntSet(createIds);
        Set<Integer> reads = toIntSet(readIds);
        Set<Integer> updates = toIntSet(updateIds);
        Set<Integer> deletes = toIntSet(deleteIds);

        AuthenDAO dao = new AuthenDAO();
        try {
            for (String sid : permIds) {
                int pid = Integer.parseInt(sid);
                boolean canCreate = creates.contains(pid);
                boolean canRead = reads.contains(pid);
                boolean canUpdate = updates.contains(pid);
                boolean canDelete = deletes.contains(pid);
                dao.updatePermission(pid, canCreate, canRead, canUpdate, canDelete);
            }
        } catch (SQLException ex) {
            throw new ServletException("Lỗi cập nhật permission", ex);
        }

        response.sendRedirect(request.getContextPath() + "/permission");
    }

    // Utility: chuyển mảng String[] thành Set<Integer>
    private Set<Integer> toIntSet(String[] arr) {
        Set<Integer> s = new HashSet<>();
        if (arr != null) {
            for (String v : arr) {
                try {
                    s.add(Integer.parseInt(v));
                } catch (NumberFormatException e) {
                    /* bỏ qua */ }
            }
        }
        return s;
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