/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.AuditInventoryDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.Order;
import model.OrderDetail;
import model.Users;
import model.InventoryAuditDetail;
import java.util.List;

       

/**
 *
 * @author PC
 */
public class ApproveAuditController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet ApproveAuditController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApproveAuditController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check quyền admin (giống approve)
        Users user = (Users) request.getSession().getAttribute("USER");
        if (user == null || user.getRoleId() != 2) {
            response.setStatus(403);
            response.getWriter().write("{\"msg\":\"Access denied!\"}");
            return;
        }

        String auditIdStr = request.getParameter("auditId");
        String rejectNote = request.getParameter("rejectNote"); // Lý do reject (có thể null)

        if (auditIdStr == null) {
            response.setStatus(400);
            response.getWriter().write("{\"msg\":\"Missing auditId!\"}");
            return;
        }

        int auditId = Integer.parseInt(auditIdStr);
        AuditInventoryDAO auditDao = new AuditInventoryDAO();

        try {
            // Nếu có rejectNote thì update cả note, không thì chỉ update status
            if (rejectNote != null && !rejectNote.trim().isEmpty()) {
                auditDao.updateAuditStatusAndNote(auditId, "rejected", rejectNote.trim());
            } else {
                auditDao.updateAuditStatus(auditId, "rejected");
            }

            response.setStatus(200);
            response.getWriter().write("{\"msg\":\"Inventory audit #" + auditId + " has been rejected!\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"msg\":\"Internal Server Error!\"}");
        }
    }
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}