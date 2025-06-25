/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import com.google.gson.Gson;
import dal.AuditInventoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.InventoryAudit;
import model.InventoryAuditDetail;
import java.util.List;

/**
 *
 * @author PC
 */
public class InventoryAuditDetailController extends HttpServlet {
   
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
            out.println("<title>Servlet InventoryAuditDetailController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventoryAuditDetailController at " + request.getContextPath () + "</h1>");
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
            String view = request.getParameter("view");
    String auditIdStr = request.getParameter("auditId");

    if ("detail".equals(view) && auditIdStr != null) {
        try {
            int auditId = Integer.parseInt(auditIdStr);

            AuditInventoryDAO dao = new AuditInventoryDAO();
            InventoryAudit audit = dao.getAuditById(auditId);
            List<InventoryAuditDetail> details = dao.getAuditDetailByAuditId(auditId);
            System.out.println("audit nè " + audit);
            System.out.println("details nè " + details);

            if (audit == null) {
                response.setStatus(404);
                response.getWriter().write("{\"msg\":\"Audit not found\"}");
                return;
            }

            // Đưa class AuditResponse ra ngoài hàm để Gson serialize ngon hơn
            AuditResponse auditResponse = new AuditResponse(audit, details);

            // Trả về JSON object gồm audit + list details
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Gson gson = new Gson();
            String jsonStr = gson.toJson(auditResponse);
            System.out.println("Json trả về: " + jsonStr); // Nên in log này

            response.getWriter().write(jsonStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"msg\":\"Internal Server Error\"}");
        }
    } else {
        response.setStatus(400);
        response.getWriter().write("{\"msg\":\"Missing or invalid parameters\"}");
    }
    } 

public static class AuditResponse {
    public InventoryAudit audit;
    public List<InventoryAuditDetail> details;
    public AuditResponse(InventoryAudit audit, List<InventoryAuditDetail> details) {
        this.audit = audit;
        this.details = details;
    }
}
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
