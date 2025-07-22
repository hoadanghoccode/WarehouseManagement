/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.ExportNoteDAO;
import dal.Import_noteDAO;
import dal.InventoryDAO;
import dal.InventoryHistoryDAO;
import dal.MaterialDAO;
import dal.TransactionHistoryDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import model.InventoryAlert;
import model.Material;
import model.MaterialTransactionHistory;

/**
 *
 * @author duong
 */
public class DirectorDashboardController extends HttpServlet {

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
            out.println("<title>Servlet DirectorDashboardController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DirectorDashboardController at " + request.getContextPath() + "</h1>");
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
        try {
            int pageSize = 6;

            String globalSearch = request.getParameter("globalSearch");
            if (globalSearch != null && !globalSearch.trim().isEmpty()) {
                globalSearch = globalSearch.trim();

                // === GLOBAL SEARCH MODE ===
                InventoryDAO inventoryDAO = new InventoryDAO();
                TransactionHistoryDAO txnDAO = new TransactionHistoryDAO();
                MaterialDAO materialDAO = new MaterialDAO();

                List<Material> globalNewMaterials = materialDAO.getNewMaterialsToday(globalSearch);
                List<Material> globalUpdatedMaterials = materialDAO.getUpdatedMaterialsToday(globalSearch);
                List<InventoryAlert> globalAlerts = inventoryDAO.getInventoryAlertsAdvanced(globalSearch, null, null);
                List<MaterialTransactionHistory> globalTxnList = txnDAO.getTransactions(null, null, globalSearch, null, null, 0, pageSize);

                request.setAttribute("newMaterials", globalNewMaterials);
                request.setAttribute("updatedMaterials", globalUpdatedMaterials);
                request.setAttribute("inventoryAlerts", globalAlerts);
                request.setAttribute("transactionList", globalTxnList);

                request.setAttribute("totalNewPages", 1);
                request.setAttribute("currentNewPage", 1);
                request.setAttribute("totalUpdatedPages", 1);
                request.setAttribute("currentUpdatedPage", 1);
                request.setAttribute("totalPages", 1);
                request.setAttribute("currentPage", 1);
                request.setAttribute("totalTxnPages", 1);
                request.setAttribute("currentTxnPage", 1);

                request.setAttribute("globalSearch", globalSearch);

                Date today = Date.valueOf(LocalDate.now());
                Import_noteDAO importDAO = new Import_noteDAO();
                ExportNoteDAO exportDAO = new ExportNoteDAO();
                InventoryHistoryDAO usageDAO = new InventoryHistoryDAO();
                UserDAO userDAO = new UserDAO();

                request.setAttribute("totalMaterials", materialDAO.getTotalMaterialCount());
                request.setAttribute("importToday", importDAO.getImportToday());
                request.setAttribute("exportToday", exportDAO.getExportToday());
                request.setAttribute("totalTxnToday", txnDAO.getTotalTransactionsToday(today));
                request.setAttribute("materials", materialDAO.getAllMaterials());

                request.getRequestDispatcher("directordashboard.jsp").forward(request, response);
                return;
            }

            // === ALERT FILTER ===
            String alertSearch = request.getParameter("alertSearch");
            String alertMaterialIdStr = request.getParameter("alertMaterialId");
            String alertPageStr = request.getParameter("alertPage");
            int alertPage = (alertPageStr != null) ? Integer.parseInt(alertPageStr) : 1;
            Integer alertMaterialId = (alertMaterialIdStr != null && !alertMaterialIdStr.isEmpty())
                    ? Integer.parseInt(alertMaterialIdStr) : null;

            InventoryDAO inventoryDAO = new InventoryDAO();
            List<InventoryAlert> allAlerts = inventoryDAO.getInventoryAlertsAdvanced(alertSearch, alertMaterialId, null);
            int totalAlertPages = (int) Math.ceil((double) allAlerts.size() / pageSize);
            int alertFrom = (alertPage - 1) * pageSize;
            int alertTo = Math.min(alertFrom + pageSize, allAlerts.size());
            List<InventoryAlert> pagedAlerts = allAlerts.subList(alertFrom, alertTo);

            // === TRANSACTION FILTER ===
            String txnSearch = request.getParameter("txnSearch");
            String txnMaterialIdStr = request.getParameter("txnMaterialId");
            String txnFromDateStr = request.getParameter("txnfromDate");
            String txnToDateStr = request.getParameter("txntoDate");
            String txnPageStr = request.getParameter("txnPage");
            int txnPage = (txnPageStr != null) ? Integer.parseInt(txnPageStr) : 1;
            Integer txnMaterialId = (txnMaterialIdStr != null && !txnMaterialIdStr.isEmpty()) ? Integer.parseInt(txnMaterialIdStr) : null;
            Date txnFromDate = (txnFromDateStr != null && !txnFromDateStr.isEmpty()) ? Date.valueOf(txnFromDateStr) : null;
            Date txnToDate = (txnToDateStr != null && !txnToDateStr.isEmpty()) ? Date.valueOf(txnToDateStr) : null;
            System.out.println("From: " + txnFromDate);
            System.out.println("To  : " + txnToDate);
            TransactionHistoryDAO txnDAO = new TransactionHistoryDAO();
            List<MaterialTransactionHistory> txnList = txnDAO.getTransactions(txnFromDate, txnToDate, txnSearch, txnMaterialId, null,
                    (txnPage - 1) * pageSize, pageSize);
            int totalTxn = txnDAO.countTransactions(txnFromDate, txnToDate, txnSearch, txnMaterialId, null);
            int totalTxnPages = (int) Math.ceil((double) totalTxn / pageSize);

            // === NEW MATERIAL FILTER ===
            String newSearch = request.getParameter("newMaterialSearch");
            String newPageStr = request.getParameter("newPage");
            int newPage = (newPageStr != null) ? Integer.parseInt(newPageStr) : 1;

            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> allNewMaterials = materialDAO.getNewMaterialsToday(newSearch);
            int totalNewPages = (int) Math.ceil((double) allNewMaterials.size() / pageSize);
            int newFromIndex = (newPage - 1) * pageSize;
            int newToIndex = Math.min(newFromIndex + pageSize, allNewMaterials.size());
            List<Material> pagedNewMaterials = allNewMaterials.subList(newFromIndex, newToIndex);

            // === UPDATED MATERIAL FILTER ===
            String updatedSearch = request.getParameter("updatedMaterialSearch");
            String updatedPageStr = request.getParameter("updatedPage");
            int updatedPage = (updatedPageStr != null) ? Integer.parseInt(updatedPageStr) : 1;

            List<Material> allUpdatedMaterials = materialDAO.getUpdatedMaterialsToday(updatedSearch);
            int totalUpdatedPages = (int) Math.ceil((double) allUpdatedMaterials.size() / pageSize);
            int updatedFromIndex = (updatedPage - 1) * pageSize;
            int updatedToIndex = Math.min(updatedFromIndex + pageSize, allUpdatedMaterials.size());
            List<Material> pagedUpdatedMaterials = allUpdatedMaterials.subList(updatedFromIndex, updatedToIndex);

            // === Other Metrics ===
            Date today = Date.valueOf(LocalDate.now());
            Import_noteDAO importDAO = new Import_noteDAO();
            ExportNoteDAO exportDAO = new ExportNoteDAO();
            InventoryHistoryDAO usageDAO = new InventoryHistoryDAO();
            UserDAO userDAO = new UserDAO();

            request.setAttribute("totalMaterials", materialDAO.getTotalMaterialCount());
            request.setAttribute("importToday", importDAO.getImportToday());
            request.setAttribute("exportToday", exportDAO.getExportToday());
            request.setAttribute("totalTxnToday", txnDAO.getTotalTransactionsToday(today));
            request.setAttribute("materials", materialDAO.getAllMaterials());

            // === Set Attributes ===
            request.setAttribute("inventoryAlerts", pagedAlerts);
            request.setAttribute("totalPages", totalAlertPages);
            request.setAttribute("currentPage", alertPage);
            request.setAttribute("alertSearch", alertSearch);
            request.setAttribute("alertMaterialId", alertMaterialId);

            request.setAttribute("transactionList", txnList);
            request.setAttribute("totalTxnPages", totalTxnPages);
            request.setAttribute("currentTxnPage", txnPage);
            request.setAttribute("txnSearch", txnSearch);
            request.setAttribute("txnMaterialId", txnMaterialId);
            request.setAttribute("txnfromDate", txnFromDateStr);
            request.setAttribute("txntoDate", txnToDateStr);

            request.setAttribute("newMaterials", pagedNewMaterials);
            request.setAttribute("totalNewPages", totalNewPages);
            request.setAttribute("currentNewPage", newPage);
            request.setAttribute("newMaterialSearch", newSearch);

            request.setAttribute("updatedMaterials", pagedUpdatedMaterials);
            request.setAttribute("totalUpdatedPages", totalUpdatedPages);
            request.setAttribute("currentUpdatedPage", updatedPage);
            request.setAttribute("updatedMaterialSearch", updatedSearch);

            request.getRequestDispatcher("directordashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());

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
