/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.NotificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;
import model.Users;

/**
 *
 * @author PC
 */
public class NotificationController extends HttpServlet {

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
            out.println("<title>Servlet NotificationController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NotificationController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Lấy danh sách notification (AJAX)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        NotificationDAO notificationDAO = new NotificationDAO();
        Users user = (Users) req.getSession().getAttribute("USER");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int userId = user.getUserId();

        int page = Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page"));
        int size = Integer.parseInt(req.getParameter("size") == null ? "10" : req.getParameter("size"));
        int offset = (page - 1) * size;

        try {
            List<Notification> notifications = notificationDAO.getUserNotifications(userId, offset, size);
            int unreadCount = notificationDAO.countUnreadNotifications(userId);

            // Trả JSON cho frontend
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.println("{");
            out.println("\"notifications\": [");
            for (int i = 0; i < notifications.size(); i++) {
                Notification n = notifications.get(i);
                out.println("  {");
                out.println("    \"id\": " + n.getId() + ",");
                out.println("    \"title\": \"" + n.getTitle() + "\",");
                out.println("    \"content\": \"" + n.getContent() + "\",");
                out.println("    \"link\": \"" + n.getLink() + "\",");
                out.println("    \"createdAt\": \"" + n.getCreatedAt() + "\",");
                out.println("    \"isRead\": " + n.isRead);
                out.print("  }");
                if (i < notifications.size() - 1) {
                    out.println(",");
                } else {
                    out.println();
                }
            }
            out.println("],");
            out.println("\"unreadCount\": " + unreadCount);
            out.println("}");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    // Đánh dấu đã đọc khi user click notification (AJAX POST)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NotificationDAO notificationDAO = new NotificationDAO();
        HttpSession session = req.getSession();
        Users user = (Users) req.getSession().getAttribute("USER");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int userId = user.getUserId();
        int notificationId = Integer.parseInt(req.getParameter("notificationId"));
        try {
            notificationDAO.markAsRead(userId, notificationId);
            resp.setStatus(200);
        } catch (SQLException e) {
            resp.setStatus(500);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
