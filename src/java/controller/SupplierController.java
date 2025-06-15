/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;

/**
 *
 * @author PC
 */
@WebServlet(name = "SupplierController", urlPatterns = {"/supplier"})
public class SupplierController extends HttpServlet {

    private final SupplierDAO supplierDAO = new SupplierDAO();

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
            out.println("<title>Servlet SupplierController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SupplierController at " + request.getContextPath() + "</h1>");
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
        String search = request.getParameter("search");
        String status = request.getParameter("status"); // 🆕 thêm filter theo status

        // 1) Lấy toàn bộ suppliers
        ArrayList<Supplier> all = supplierDAO.getAllSuppliers();
        System.out.println("list ne" + all);

        // 2) Lọc nếu có search và status
        ArrayList<Supplier> filtered = all;

        if (search != null && !search.trim().isEmpty()) {
            filtered = supplierDAO.searchByName(filtered, search.trim());
        }

        if (status != null && !status.equals("all")) {
            filtered = supplierDAO.filterByStatus(filtered, status);  // 🆕 cần tạo hàm này trong DAO
        }

        // 3) Phân trang
        int pageSize = 5;
        int size = filtered.size();
        int numPages = (size + pageSize - 1) / pageSize;

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

        List<Supplier> pageList = supplierDAO.getListByPage(filtered, start, end);

        // 4) Đẩy sang JSP
        request.setAttribute("supplierList", pageList);
        request.setAttribute("search", search);
        request.setAttribute("status", status); // 🆕
        request.setAttribute("page", page);
        request.setAttribute("numPages", numPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalSuppliers", size);

        request.getRequestDispatcher("supplier.jsp").forward(request, response);
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
        String name = request.getParameter("name");
        String status = request.getParameter("status");

        response.setContentType("application/json;charset=UTF-8");
        try {
            if (supplierDAO.existsSupplier(name)) {
                response.getWriter().write("{\"success\":false,\"message\":\"Supplier already exists\"}");
            } else {
                boolean success = supplierDAO.addSupplier(name, status);
                if (success) {
                    response.getWriter().write("{\"success\":true}");
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"Insert failed\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
        } finally {
            out.flush();
            out.close(); // rất quan trọng
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Supplier Controller";
    }// </editor-fold>

}
