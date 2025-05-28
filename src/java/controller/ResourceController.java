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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Resource;

/**
 *
 * @author PC
 */
public class ResourceController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        AuthenDAO dao = new AuthenDAO();

        // 1) Lấy toàn bộ resources
        ArrayList<Resource> all = dao.getAllResources();

        // 2) Lọc nếu có search
        ArrayList<Resource> filtered = (search != null && !search.trim().isEmpty())
                ? dao.searchByName(all, search.trim())
                : all;

        // 3) Phân trang
        int pageSize = 5;                     // số bản ghi/trang (tùy chỉnh)
        int size = filtered.size();
        int numPages = (size + pageSize - 1) / pageSize;

        // trang hiện tại
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) {
                page = Integer.parseInt(p);
            }
        } catch (NumberFormatException ignored) {
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(page * pageSize, size);

        List<Resource> pageList = dao.getListByPage(filtered, start, end);

        // 4) Đẩy sang JSP
        request.setAttribute("resourceList", pageList);
        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("numPages", numPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalResources", size);

        request.getRequestDispatcher("authentication.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("resourceName");
        String desc = req.getParameter("description");

        resp.setContentType("application/json;charset=UTF-8");
        try {
            AuthenDAO dao = new AuthenDAO();
            if (dao.existsResource(name)) {
                resp.getWriter().write("{\"success\":false,\"message\":\"Resource already exists\"}");
            } else {
                boolean success = dao.addResource(name, desc);
                if (success) {
                    resp.getWriter().write("{\"success\":true}");
                } else {
                    resp.getWriter().write("{\"success\":false,\"message\":\"Insert failed\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
