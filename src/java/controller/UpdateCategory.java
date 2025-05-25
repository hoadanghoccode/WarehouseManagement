/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Category;

/**
 *
 * @author ADMIN
 */
public class UpdateCategory extends HttpServlet {

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
            out.println("<title>Servlet UpdateCategory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateCategory at " + request.getContextPath() + "</h1>");
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
        // Lấy category đang sửa
        int id = Integer.parseInt(request.getParameter("cid"));
        CategoryDAO dao = new CategoryDAO();
        Category current = dao.getCategoryById(id);

        // Lấy tất cả categories để hiển thị dropdown
        List<Category> allCategories = dao.getIndentedCategories();

        request.setAttribute("currentCategory", current);
        request.setAttribute("allCategories", allCategories);
        request.getRequestDispatcher("updatecategory.jsp").forward(request, response);

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
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int parentId = request.getParameter("parentId") != null ? Integer.parseInt(request.getParameter("parentId")) : 0;

        // Lấy DAO
        CategoryDAO dao = new CategoryDAO();

        if (parentId != 0 && dao.isCircularParent(id, parentId)) {
            request.getSession().setAttribute("error", "Không thể cập nhật: vòng lặp phân cấp!");
            response.sendRedirect("updatecategory?cid=" + id);
            return;
        }

        Category parentCategory = parentId == 0 ? null : dao.getCategoryById(parentId);
        if (parentCategory != null && parentCategory.getParentId() != null) {
            request.getSession().setAttribute("error", "Không thể cập nhật: vượt quá 2 cấp phân loại (chỉ cha và con).");
            response.sendRedirect("updatecategory?cid=" + id);
            return;
        }

        // Nếu hợp lệ → tiếp tục cập nhật
        Category updated = new Category();
        updated.setCategoryId(id);
        updated.setName(name);
        if (parentId == 0) {
            updated.setParentId(null);
        } else {
            updated.setParentId(dao.getCategoryById(parentId));
        }
        dao.updateCategory(updated);
        request.setAttribute("successMessage", true);
        request.setAttribute("categoryName", name);
        request.getRequestDispatcher("updatecategory.jsp").forward(request, response);
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
