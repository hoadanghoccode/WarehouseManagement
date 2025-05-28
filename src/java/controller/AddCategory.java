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
public class AddCategory extends HttpServlet {

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
            out.println("<title>Servlet AddCategory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddCategory at " + request.getContextPath() + "</h1>");
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
        CategoryDAO dao = new CategoryDAO();
        List<Category> parentCategories = dao.getAllParentCategory(null); // hoặc getAllParentCategories()
        request.setAttribute("allCategories", parentCategories);
        request.getRequestDispatcher("addcategory.jsp").forward(request, response);
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
        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");

        if (categoryName != null) {
            categoryName = categoryName.trim();
        }

        if (categoryName == null || categoryName.isEmpty()) {
            request.setAttribute("errorMessage", "Category name cannot be empty!");
            forwardWithData(request, response);
            return;
        }

        
        if (!categoryName.matches("^[\\p{L}\\s]+$")) {
            request.setAttribute("errorMessage", "Category name must only contain letters and spaces (no numbers or symbols).");
            forwardWithData(request, response);
            return;
        }

        Integer parentId = null;
        if (parentIdStr != null && !parentIdStr.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid parent category ID.");
                forwardWithData(request, response);
                return;
            }
        }

        CategoryDAO dao = new CategoryDAO();
        dao.insertCategory(categoryName, parentId);

        request.setAttribute("successMessage", "Category added successfully!");
        request.setAttribute("categoryName", categoryName);
        forwardWithData(request, response);
    }

    private void forwardWithData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO dao = new CategoryDAO();
        List<Category> parentCategories = dao.getAllParentCategory(null);
        request.setAttribute("allCategories", parentCategories);

        request.getRequestDispatcher("addcategory.jsp").forward(request, response);
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
