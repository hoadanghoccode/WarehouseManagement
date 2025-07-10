/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.ExportNoteDAO;
import dal.Import_noteDAO;
import dal.MaterialDAO;
import dal.TransactionHistoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Material;
import model.MaterialTransactionHistory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author duong
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

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
            out.println("<title>Servlet DashboardController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardController at " + request.getContextPath() + "</h1>");
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

        String fromDateStr = request.getParameter("from");
        String toDateStr = request.getParameter("to");

        java.sql.Date fromDate = null;
        java.sql.Date toDate = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (fromDateStr != null && toDateStr != null && !fromDateStr.isEmpty() && !toDateStr.isEmpty()) {
            LocalDate parsedFrom = LocalDate.parse(fromDateStr, formatter);
            LocalDate parsedTo = LocalDate.parse(toDateStr, formatter);
            fromDate = java.sql.Date.valueOf(parsedFrom);
            toDate = java.sql.Date.valueOf(parsedTo);
        }

        MaterialDAO materialDAO = new MaterialDAO();
        Import_noteDAO importDAO = new Import_noteDAO();
        ExportNoteDAO exportDAO = new ExportNoteDAO();
        TransactionHistoryDAO transactionDAO = new TransactionHistoryDAO();

        List<Material> todayMaterials;
        MaterialTransactionHistory latestTransaction;
        int totalMaterials;
        int activeCount;
        int todayImports;
        int todayExports;
        if (fromDate != null && toDate != null) {
            todayMaterials = materialDAO.getMaterialsInDateRange(fromDate, toDate, null);
            latestTransaction = transactionDAO.getLatestTransaction(fromDate, toDate);
            totalMaterials = materialDAO.getMaterialCountInDateRange(fromDate, toDate);
            activeCount = materialDAO.getActiveMaterialCountInRange(fromDate, toDate);
            
            todayImports = importDAO.getImportNoteCountInDateRange(fromDate, toDate);
            todayExports = exportDAO.getExportNoteCountInDateRange(fromDate, toDate);
        } else {
            todayMaterials = materialDAO.getNewMaterialsToday();
            latestTransaction = transactionDAO.getLatestTransaction();
            totalMaterials = materialDAO.getTotalMaterialCount();
            activeCount = materialDAO.getActiveMaterialCount();
            todayImports = importDAO.getTodayImportNoteCount();
            todayExports = exportDAO.getTodayExportNoteCount();
        }

        List<Material> materials = materialDAO.getAllMaterials();

        request.setAttribute("materials", materials);
        request.setAttribute("todayMaterials", todayMaterials);
        request.setAttribute("latestTransaction", latestTransaction);
        request.setAttribute("totalMaterials", totalMaterials);
        request.setAttribute("activeCount", activeCount);
        request.setAttribute("todayImports", todayImports);
        request.setAttribute("todayExports", todayExports);
        System.out.println("✅ todayMaterials size = " + todayMaterials.size());
        System.out.println("✅ Filter Date: " + fromDate + " to " + toDate);
        System.out.println("✅ todayMaterials size = " + todayMaterials.size());
        System.out.println("✅ latestTransaction = " + (latestTransaction != null ? latestTransaction.getMaterialName() : "null"));
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
