/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.Users;
import com.google.gson.Gson;
import dal.AuditInventoryDAO;
import dal.InventoryDAO;
import dal.UserDAO;
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
import java.util.HashMap;

import model.InventoryItem;
import java.util.List;
import java.util.Map;

import model.Category;
import model.InventoryAuditDetail;
import model.Users;
import dal.CategoryDAO;

/**
 *
 * @author PC
 */
public class InventoryAuditController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Filter X: " + request.getRequestURI());
        AuditInventoryDAO dao = new AuditInventoryDAO();
        String action = request.getParameter("action");
        try {
            if (action != null && action.equals("listMaterial")) {
                // Lấy thêm categoryId nếu có
                String categoryIdStr = request.getParameter("categoryId");
                List<InventoryItem> items;
                if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                    try {
                        int categoryId = Integer.parseInt(categoryIdStr);
                        items = dao.getInventoryForAudit(categoryId);
                    } catch (NumberFormatException e) {
                        items = dao.getInventoryForAudit(); // fallback nếu lỗi
                    }
                } else {
                    items = dao.getInventoryForAudit();
                }
            
                // Lấy luôn parent category kèm sub active
                CategoryDAO cateDao = new CategoryDAO();
                List<Category> parentCategories = cateDao.getAllParentCategoryWithActiveSubs("active");
            
                // Gộp response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Gson gson = new Gson();
            
                // Gói 2 danh sách vào map để trả về JSON chung
                Map<String, Object> respData = new HashMap<>();
                respData.put("items", items);
                respData.put("parentCategories", parentCategories);
            
                response.getWriter().write(gson.toJson(respData));
                return;
            } else {
                // Forward tới JSP (truyền parentCategories cho modal)
                List<InventoryItem> items = dao.getInventoryForAudit();
                CategoryDAO cateDao = new CategoryDAO();
                request.setAttribute("parentCategories", cateDao.getAllParentCategoryWithActiveSubs("active"));
                System.out.println("data hahaha" + cateDao.getAllParentCategoryWithActiveSubs("active"));
                request.setAttribute("inventoryList", items);
                request.getRequestDispatcher("inventoryaudit.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"msg\":\"Error: " + e.getMessage() + "\"}");
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

        Gson gson = new Gson();
        InventoryAuditItem[] items = gson.fromJson(json, InventoryAuditItem[].class);

        HttpSession session = req.getSession(false);
        Users currentUser = (Users) session.getAttribute("USER");
        System.out.println("currentUser" + currentUser);
        if (currentUser == null) {
            resp.setStatus(401);
            resp.getWriter().write("{\"code\":\"E00\", \"msg\":\"Not logged in or session expired!\"}");
            return;
        }
        int userId = currentUser.getUserId();
        Date today = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());

        List<InventoryAuditDetail> detailList = new ArrayList<>();
        AuditInventoryDAO dao = new AuditInventoryDAO();
        try {
            for (InventoryAuditItem item : items) {
                // Lấy material_detail_id cho cả 2 quality
                
                int availableDetailId = dao.getMaterialDetailIdByMaterialIdAndQuality(item.materialId, 1);      // 1 = available
                int notAvailableDetailId = dao.getMaterialDetailIdByMaterialIdAndQuality(item.materialId, 2);   // 2 = notAvailable
                // Kiểm tra nếu không tồn tại material_detail_id thì bỏ qua hoặc báo lỗi
                if (availableDetailId == -1 || notAvailableDetailId == -1) {
                    resp.setStatus(400);
                    resp.getWriter().write("{\"msg\":\"Material detail not found for materialId " + item.materialId + "\"}");
                    return;
                }

                // Available
                InventoryAuditDetail detailAvail = new InventoryAuditDetail();
                detailAvail.setMaterialDetailId(availableDetailId);
                detailAvail.setSystemQty(item.availableSystem);
                detailAvail.setActualQty(item.availableActual);
                detailAvail.setDifference(item.availableActual - item.availableSystem);
                detailAvail.setReason(item.reason); // hoặc có thể tách riêng lý do cho từng loại nếu muốn
                detailAvail.setQualityId(1); // 1 = available
                detailList.add(detailAvail);
                // NotAvailable
                InventoryAuditDetail detailNotAvail = new InventoryAuditDetail();
                detailNotAvail.setMaterialDetailId(notAvailableDetailId);
                detailNotAvail.setSystemQty(item.notAvailableSystem);
                detailNotAvail.setActualQty(item.notAvailableActual);
                detailNotAvail.setDifference(item.notAvailableActual - item.notAvailableSystem);
                detailNotAvail.setReason(item.reason);
                detailNotAvail.setQualityId(2); // 2 = notAvailable
                detailList.add(detailNotAvail);
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
        public double availableSystem;    // số lượng tồn tốt hệ thống
        public double availableActual;    // số lượng kiểm thực tế còn tốt
        public double notAvailableSystem; // số lượng tồn lỗi/hỏng hệ thống
        public double notAvailableActual; // số lượng kiểm thực tế lỗi/hỏng
        public String reason;
    }

}