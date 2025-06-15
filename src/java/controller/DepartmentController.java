/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DepartmentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.DeptRoleResource;
import com.google.gson.Gson;
import model.ResourcePerm;
import model.RoleData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import model.DeptWithRole;
import model.Department;

/**
 *
 * @author PC
 */
public class DepartmentController extends HttpServlet {

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
            out.println("<title>Servlet DepartmentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DepartmentController at " + request.getContextPath() + "</h1>");
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
    private static final long serialVersionUID = 1L;
    private final DepartmentDAO dao = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/department/data".equals(path)) {
            // --- TRẢ JSON CHO TẤT CẢ DEPARTMENT ---
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try {
                // Bước 1: Lấy danh sách “phẳng” (flat) từ DAO
                List<DeptRoleResource> flatList = dao.getAllDepartmentsWithRoles();

                // Bước 2: Gom nhóm theo departmentId
                // Sử dụng LinkedHashMap để giữ thứ tự của department như trong SQL (ORDER BY)
                Map<Integer, DeptWithRole> map = new LinkedHashMap<>();

                for (DeptRoleResource row : flatList) {
                    Integer deptId = row.getDepartmentId();
                    String deptName = row.getDepartmentName();
                    String description = row.getDescription();
                    Integer roleId = row.getRoleId();      // có thể null
                    String roleName = row.getRoleName();    // có thể null
                    Integer resourceId = row.getResourceId();  // có thể null
                    String resourceName = row.getResourceName();// có thể null
                    Boolean canAdd = row.getCanAdd();      // có thể null
                    Boolean canView = row.getCanView();
                    Boolean canUpdate = row.getCanUpdate();
                    Boolean canDelete = row.getCanDelete();

                    // Nếu đây là department đầu tiên gặp phải, tạo DeptWithRole ban đầu
                    if (!map.containsKey(deptId)) {
                        RoleData initialRole = null;
                        if (roleId != null) {
                            initialRole = new RoleData(roleId, roleName);
                            // resources vẫn rỗng, sẽ thêm vào ở bước sau
                        }
                        DeptWithRole dwr = new DeptWithRole(deptId, description, deptName, initialRole);
                        map.put(deptId, dwr);
                    }

                    // Lấy DeptWithRole hiện tại
                    DeptWithRole current = map.get(deptId);

                    // Nếu department này có role (roleId != null), tiếp tục thêm resource
                    if (roleId != null) {
                        RoleData rd = current.getRole();
                        // Nếu trước đó chưa khởi tạo RoleData (do resourceId == null ở dòng đầu),
                        // thì tạo mới ở đây
                        if (rd == null) {
                            rd = new RoleData(roleId, roleName);
                            current = new DeptWithRole(deptId, description, deptName, rd);
                            map.put(deptId, current);
                        }
                        // Nếu resourceId != null, tạo ResourcePerm và add vào rd.resources
                        if (resourceId != null) {
                            ResourcePerm rp = new ResourcePerm(
                                    resourceId,
                                    resourceName,
                                    canAdd != null && canAdd,
                                    canView != null && canView,
                                    canUpdate != null && canUpdate,
                                    canDelete != null && canDelete
                            );
                            rd.getResources().add(rp);
                        }
                    }
                    // Nếu roleId == null: department chưa gán role, current.getRole() vẫn null
                }

                // Bước 3: Chuyển LinkedHashMap.values() thành List<DeptWithRole>
                List<DeptWithRole> result = new ArrayList<>(map.values());

                // Bước 4: Trả JSON ra client
                String json = new Gson().toJson(result);
                resp.getWriter().write(json);

            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, String> error = new LinkedHashMap<>();
                error.put("message", "Database error: " + e.getMessage());
                resp.getWriter().write(new Gson().toJson(error));
            }

        } else {
            // --- FORWARD ĐẾN JSP ĐỂ HIỂN THỊ GIAO DIỆN ---
            // Ví dụ JSP nằm ở: /department.jsp
            req.getRequestDispatcher("/department.jsp")
                    .forward(req, resp);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Đặt encoding để đọc được tiếng Việt
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String deptName = req.getParameter("departmentName");
        String deptDesc = req.getParameter("departmentDescription");
        String roleIdStr = req.getParameter("roleId");

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        // Validate cơ bản
        if (deptName == null || deptName.isEmpty() || roleIdStr == null || roleIdStr.isEmpty()) {
            result.put("success", false);
            result.put("message", "Invalid Name or Role");
            resp.getWriter().write(gson.toJson(result));
            return;
        }

        try {
            int roleId = Integer.parseInt(roleIdStr);
            
            // Kiểm tra xem tên department đã tồn tại chưa
            if (dao.isDepartmentNameExists(deptName)) {
                result.put("success", false);
                result.put("message", "Department name already exists in the system!");
                resp.getWriter().write(gson.toJson(result));
                return;
            }
            
            // Tạo object Department (giả sử bạn có 1 class model Department)
            Department dept = new Department();
            dept.setName(deptName);
            dept.setDescription(deptDesc);
            dept.setRole_id(roleId);

            // Gọi DAO để insert
            boolean created = dao.insertDepartment(dept);
            if (created) {
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "Department cannot be added");
            }
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Invalid RoleId.");

        } catch (SQLException e) {
            // Bắt lỗi duplicate key trên cột Role_id (MySQL error code = 1062)
            if (e.getErrorCode() == 1062 && e.getMessage().contains("department.Role_id")) {
                result.put("success", false);
                result.put("message", "This role has been assigned to another Department.");
            } else {
                result.put("success", false);
                result.put("message", "Lỗi DB: " + e.getMessage());
            }
        }

        resp.getWriter().write(gson.toJson(result));
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
