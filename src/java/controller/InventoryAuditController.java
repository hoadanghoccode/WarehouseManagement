/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dal.AuditInventoryDAO;
import dal.InventoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import model.InventoryItem;
import java.util.List;
import model.InventoryAuditDetail;
import model.Users;

/**
 *
 * @author PC
 */
public class InventoryAuditController extends HttpServlet {

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
            out.println("<title>Servlet InventoryAuditController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventoryAuditController at " + request.getContextPath() + "</h1>");
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
            AuditInventoryDAO dao = new AuditInventoryDAO();
            List<InventoryItem> items = dao.getInventoryForAudit();
            request.setAttribute("inventoryList", items);
            request.getRequestDispatcher("inventoryaudit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Đọc dữ liệu từ Ajax gửi lên
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();

        // Parse dữ liệu
        Gson gson = new Gson();
        InventoryAuditItem[] items = gson.fromJson(json, InventoryAuditItem[].class);

        Users user = (Users) req.getSession().getAttribute("USER");
        HttpSession session = req.getSession(false);
            Users currentUser = (Users) session.getAttribute("USER");
            System.out.println("currentUser" + currentUser);
        if (currentUser == null) {
            // Trả về lỗi chưa đăng nhập
            resp.setStatus(401);
            resp.getWriter().write("{\"code\":\"E00\", \"msg\":\"Not logged in or session expired!\"}");
            return;
        }
        
        // (Bạn cần xác định userId kiểm kê, ví dụ lấy từ session)
        int userId = ((Users) req.getSession().getAttribute("USER")).getUserId(); // tuỳ cấu trúc hệ thống
        
      

        Date today = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());

        List<InventoryAuditDetail> detailList = new ArrayList<>();
        AuditInventoryDAO dao = new AuditInventoryDAO();
        try {
            for (InventoryAuditItem item : items) {
                int materialDetailId = dao.getMaterialDetailIdByMaterialId(item.materialId);
                double sysQty = Double.parseDouble(item.systemQty + "");
                double actualQty = Double.parseDouble(item.actualQty + "");
                double diff = actualQty - sysQty;
                String reason = item.reason;

                InventoryAuditDetail detail = new InventoryAuditDetail();
                detail.setMaterialDetailId(materialDetailId);
                detail.setSystemQty(sysQty);
                detail.setActualQty(actualQty);
                detail.setDifference(diff);
                detail.setReason(reason);
                detailList.add(detail);
            }
            dao.saveInventoryAudit(userId, sqlDate, detailList);

            resp.setStatus(200);
            resp.getWriter().write("{\"msg\":\"success\"}");
        } catch (Exception ex) {
            resp.setStatus(500);
            resp.getWriter().write("{\"msg\":\"error\"}");
            ex.printStackTrace();
        }
    }

    // Inner class để map dữ liệu json
    static class InventoryAuditItem {

        public int materialId;
        public double systemQty;
        public double actualQty;
        public String reason;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
