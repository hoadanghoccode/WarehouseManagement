/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dal.AuthenDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.RolePermission;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import model.ResourcePerm;
import model.RoleData;
import model.RolePayload;

/**
 *
 * @author PC
 */
public class PermissionRoleController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AuthenDAO dao = new AuthenDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/permissionrole/data".equals(path)) {
            // --- TRẢ JSON NESTED ---
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try {
                List<RolePermission> flat = dao.getAllRolePermissions();
                Gson gson = new Gson();

                Map<Integer, RoleData> map = new LinkedHashMap<>();
                for (RolePermission rp : flat) {
                    RoleData rd = map.computeIfAbsent(rp.getRoleId(), id
                            -> new RoleData(rp.getRoleId(), rp.getRoleName())
                    );

                    // Nếu resourceName null (LEFT JOIN không có mapping) thì bỏ qua
                    if (rp.getResourceName() != null) {
                        rd.resources.add(new ResourcePerm(
                                rp.getResourceId(),
                                rp.getResourceName(),
                                rp.isCanAdd(),
                                rp.isCanView(),
                                rp.isCanUpdate(),
                                rp.isCanDelete()
                        ));
                    }
                }

                // Chuyển sang List
                List<RoleData> nested = new ArrayList<>(map.values());
                resp.getWriter().write(gson.toJson(nested));

            } catch (SQLException e) {
                Gson gson = new Gson();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(
                        gson.toJson(new ErrorResponse("Database error: " + e.getMessage()))
                );
            }

        } else {
            // --- FORWARD ĐẾN JSP ---
            req.getRequestDispatcher("/role.jsp")
                    .forward(req, resp);
        }
    }

    private static class ErrorResponse {

        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // parse JSON body thành List<RolePayload>
        List<RolePayload> roles = new Gson()
            .fromJson(request.getReader(), new TypeToken<List<RolePayload>>(){}.getType());

        try {
            new AuthenDAO().upsertRolePermissions(roles);
            response.setStatus(200);
            response.getWriter().write("{\"status\":\"ok\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"status\":\"error\",\"msg\":\"" + e.getMessage() + "\"}");
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
