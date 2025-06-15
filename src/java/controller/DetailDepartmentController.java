/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dal.DepartmentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DepartmentDetail;
import model.Department;


/**
 *
 * @author PC
 */
public class DetailDepartmentController extends HttpServlet {

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
            out.println("<title>Servlet DetailDepartmentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DetailDepartmentController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private static final long serialVersionUID = 1L;
    private DepartmentDAO deptDao = new DepartmentDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Thiết lập để trả JSON UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("departmentId");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Missing departmentId parameter\"}");
            return;
        }

        try {
            int departmentId = Integer.parseInt(idStr);
            DepartmentDetail detail = deptDao.getDepartmentDetail(departmentId);

            if (detail == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Department with ID " + departmentId + " not found\"}");
            } else {
                // Return JSON of DepartmentDetail object
                String json = gson.toJson(detail);
                resp.getWriter().write(json);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid departmentId\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Chuẩn bị response JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();

        // 2) Lấy dữ liệu từ form
        String deptIdStr = req.getParameter("departmentId");
        String name = req.getParameter("departmentName");
        String description = req.getParameter("departmentDescription");
        String roleIdStr = req.getParameter("roleId");

//        // 3) Validate cơ bản
        if (name == null || name.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            roleIdStr == null || roleIdStr.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            result.put("success", false);
            result.put("message", "Name, description and Role are required.");
            out.write(gson.toJson(result));
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            result.put("success", false);
            result.put("message", "Invalid Role ID.");
            out.write(gson.toJson(result));
            return;
        }

        // 4) Tạo đối tượng Department
        Department dept = new Department();
        dept.setName(name.trim());
        dept.setDescription(description.trim());
        dept.setRole_id(roleId);

        try {
            // 5) Kiểm tra là Add hay Edit
            if (deptIdStr == null || deptIdStr.trim().isEmpty()) {
                // --- THÊM MỚI ---
                boolean inserted = deptDao.insertDepartment(dept);
                if (inserted) {
                    result.put("success", true);
                    result.put("message", "Department added successfully.");
                } else {
                    resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409
                    result.put("success", false);
                    result.put("message", "This role is already assigned to another department, cannot add.");
                }
            } else {
                // --- CẬP NHẬT ---
                int deptId;
                try {
                    deptId = Integer.parseInt(deptIdStr);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                    result.put("success", false);
                    result.put("message", "Invalid Department ID.");
                    out.write(gson.toJson(result));
                    return;
                }
                dept.setDepartment_id(deptId);
                boolean updated = deptDao.updateDepartment(dept);
                if (updated) {
                    result.put("success", true);
                    result.put("message", "Department updated successfully.");
                } else {
                    resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409
                    result.put("success", false);
                    result.put("message", "This role is already assigned to another department, cannot update.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Database error: " + e.getMessage());
        }

        out.write(gson.toJson(result));
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
