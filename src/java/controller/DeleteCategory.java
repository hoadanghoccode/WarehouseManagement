/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.MaterialDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

/**
 *
 * @author ADMIN
 */
public class DeleteCategory extends HttpServlet {

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
            out.println("<title>Servlet DeleteCategory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteCategory at " + request.getContextPath() + "</h1>");
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
        int cid = Integer.parseInt(request.getParameter("cid"));

        CategoryDAO cDao = new CategoryDAO();
        MaterialDAO mDao = new MaterialDAO();

        Category cate = cDao.getCategoryById(cid);
        int countMaterials = 0;

        if (cate.getParentId() == null) {
            Category parentCate = cDao.getParentCategoryById(cate.getCategoryId());
            for (Category subCate : parentCate.getSubCategories()) {
                countMaterials += mDao.countMaterialByCategoryId(subCate.getCategoryId());
            }
            request.setAttribute("countMaterial", countMaterials);
            request.setAttribute("countSub", parentCate.getSubCategoryCount());
            request.getRequestDispatcher("deletecategory.jsp").forward(request, response);
        } else {
            cate.setMaterialCount(mDao.countMaterialByCategoryId(cate.getCategoryId()));
            countMaterials = cate.getMaterialCount();
            request.setAttribute("countMaterial", countMaterials);

            request.getRequestDispatcher("deletecategory.jsp").forward(request, response);
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
        int cid = Integer.parseInt(request.getParameter("cid"));

        CategoryDAO dao = new CategoryDAO();
        dao.deleteCategory(cid);
        request.setAttribute("successMessage", "Category delete successfully!");
        response.sendRedirect("categorylist");
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
