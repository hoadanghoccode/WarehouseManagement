/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import model.Order;
import model.Users;

/**
 *
 * @author duong
 */
@WebServlet(name = "OrderListController", urlPatterns = {"/orderlist"})
public class OrderListController extends HttpServlet {

    private static final int PAGE_SIZE = 3;

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
            out.println("<title>Servlet OrderListController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderListController at " + request.getContextPath() + "</h1>");
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

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String search = request.getParameter("search");
        String type = request.getParameter("type");
        String status = request.getParameter("status");
        String fromDateRaw = request.getParameter("fromDate");
        String toDateRaw = request.getParameter("toDate");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");

        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        int offset = (currentPage - 1) * PAGE_SIZE;

        String sortColumn = "Created_at";
        String sortDirection = "DESC";
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(" ");
            if (parts.length == 2) {
                sortColumn = parts[0];
                sortDirection = parts[1];
            }
        }

        OrderDAO dao = new OrderDAO();
        List<Order> orders;
        int totalOrders;
        int totalPages;
        Date fromDate = null, toDate = null;
        try {
            if (fromDateRaw != null && !fromDateRaw.isEmpty()) {
                fromDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(fromDateRaw).getTime());
            }
            if (toDateRaw != null && !toDateRaw.isEmpty()) {
                toDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(toDateRaw).getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (user.getRoleId() == 2) {
            orders = dao.getPagedOrderList(search, type, status, fromDate, toDate, offset, PAGE_SIZE, sortColumn, sortDirection);
            totalOrders = dao.countOrdersWithFilter(search, type, status, fromDate, toDate);
        } else {
            // Chỉ xem order của chính họ
            orders = dao.getAllOrdersByUserId(user.getUserId());
            totalOrders = orders.size();
        }
        totalPages = (int) Math.ceil((double) totalOrders / PAGE_SIZE);

        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("type", type);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);

        request.getRequestDispatcher("orderlist.jsp").forward(request, response);

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
