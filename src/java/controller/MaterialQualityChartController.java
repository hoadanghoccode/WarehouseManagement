/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import com.google.gson.Gson;
import dal.MaterialDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author duong
 */
@WebServlet(name="MaterialQualityChartController", urlPatterns={"/materialqualitychart"})
public class MaterialQualityChartController extends HttpServlet {
    private MaterialDAO materialDAO = new MaterialDAO();
   
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
            out.println("<title>Servlet MaterialQualityChartController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MaterialQualityChartController at " + request.getContextPath () + "</h1>");
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
       String materialIdStr = request.getParameter("materialId");

    // In ra để kiểm tra xem có truyền được không
System.out.println("👉 Received materialId: " + materialIdStr);

if (materialIdStr == null || materialIdStr.isEmpty()) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.getWriter().write("{\"error\":\"Material ID is missing\"}");
    return;
}

try {
    int materialId = Integer.parseInt(materialIdStr);

    System.out.println("✅ Parsed materialId = " + materialId);

    Map<String, Double> data = materialDAO.getQualityDistributionByMaterialId(materialId);

    // In ra kết quả map trả về từ DB
    for (Map.Entry<String, Double> entry : data.entrySet()) {
        System.out.println("📊 " + entry.getKey() + ": " + entry.getValue());
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new Gson().toJson(data));
} catch (NumberFormatException e) {
    e.printStackTrace(); // In lỗi ra log luôn
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.getWriter().write("{\"error\":\"Invalid material ID\"}");
}
    }
    /** 
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
