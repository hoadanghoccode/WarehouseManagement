package controller;

import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import model.Group;

public class UserListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        UserDAO dao = new UserDAO();
        String action = request.getParameter("action");

        // Handle fetching groups for a role via AJAX
        if ("getGroups".equals(action)) {
            String roleIdRaw = request.getParameter("roleId");
            response.setContentType("application/json");
            try {
                int roleId = Integer.parseInt(roleIdRaw);
                List<Group> groups = dao.getGroupsByRoleId(roleId);
                // Manually build JSON string
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < groups.size(); i++) {
                    Group group = groups.get(i);
                    json.append("{\"groupId\":").append(group.getGroupId())
                        .append(",\"name\":\"").append(escapeJson(group.getName())).append("\"}");
                    if (i < groups.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");
                response.getWriter().write(json.toString());
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid role ID\"}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Server error\"}");
            }
            return;
        }

        // Handle user listing and deletion
        String searchQuery = request.getParameter("search");
        String branchIdRaw = request.getParameter("branchId");
        String roleIdRaw = request.getParameter("roleId");
        String pageRaw = request.getParameter("page");
        Integer branchId = (branchIdRaw != null && !branchIdRaw.isEmpty()) ? Integer.parseInt(branchIdRaw) : null;
        Integer roleId = (roleIdRaw != null && !roleIdRaw.isEmpty()) ? Integer.parseInt(roleIdRaw) : null;
        int page = (pageRaw != null && !pageRaw.isEmpty()) ? Integer.parseInt(pageRaw) : 1;
        int pageSize = 5;

        if ("delete".equals(action)) {
            String idRaw = request.getParameter("id");
            if (idRaw != null && !idRaw.isEmpty()) {
                try {
                    int userId = Integer.parseInt(idRaw);
                    dao.deleteUser(userId);
                    response.sendRedirect("userlist");
                    return;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid user ID format: " + idRaw);
                    e.printStackTrace();
                }
            }
        }

        int totalUsers = dao.getTotalUsers(searchQuery, branchId, roleId);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        List<Users> users = dao.getUsers(page, pageSize, searchQuery, branchId, roleId);

        request.setAttribute("users", users);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", searchQuery);
        request.setAttribute("selectedBranchId", branchId);
        request.setAttribute("selectedRoleId", roleId);
        request.setAttribute("branches", dao.getAllBranches());
        request.setAttribute("roles", dao.getAllRoles());

        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            try {
                // Validate and parse form data
                String fullName = request.getParameter("fullName");
                String password = request.getParameter("password");
                int gender = Integer.parseInt(request.getParameter("gender"));
                String email = request.getParameter("email");
                String phoneNumber = request.getParameter("phoneNumber");
                String address = request.getParameter("address");
                String dobRaw = request.getParameter("dateOfBirth");
                int branchId = Integer.parseInt(request.getParameter("branchId"));
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                String groupIdRaw = request.getParameter("groupId");
                String image = request.getParameter("image");
                boolean status = Boolean.parseBoolean(request.getParameter("status"));

                // Basic input validation
                if (fullName == null || fullName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                    throw new ServletException("Full name, email, and password are required.");
                }
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    throw new ServletException("Invalid email format.");
                }

                int groupId = (groupIdRaw != null && !groupIdRaw.isEmpty()) ? Integer.parseInt(groupIdRaw) : 0;
                java.sql.Date dateOfBirth = null;
                if (dobRaw != null && !dobRaw.isEmpty()) {
                    try {
                        dateOfBirth = java.sql.Date.valueOf(dobRaw);
                    } catch (IllegalArgumentException e) {
                        throw new ServletException("Invalid date of birth format. Use YYYY-MM-DD.");
                    }
                }

                java.sql.Date createdAt = new java.sql.Date(System.currentTimeMillis());
                java.sql.Date updatedAt = new java.sql.Date(System.currentTimeMillis());

                // Create user object
                Users user = new Users(0, roleId, branchId, fullName, email, password, gender, phoneNumber, address,
                        dateOfBirth, image, createdAt, updatedAt, status, null, null);

                UserDAO dao = new UserDAO();
                if (dao.emailExists(email, 0)) {
                    request.setAttribute("error", "Email already exists.");
                    request.setAttribute("users", dao.getUsers(1, 5, null, null, null));
                    request.setAttribute("branches", dao.getAllBranches());
                    request.setAttribute("roles", dao.getAllRoles());
                    request.getRequestDispatcher("userlist.jsp").forward(request, response);
                    return;
                }

                // Save user and assign to group
                dao.createUser(user, groupId);
                response.sendRedirect("userlist");
            } catch (Exception e) {
                System.err.println("Error creating user: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", e.getMessage() != null ? e.getMessage() : "Creation failed. Please check the input data.");
                UserDAO dao = new UserDAO();
                request.setAttribute("users", dao.getUsers(1, 5, null, null, null));
                request.setAttribute("branches", dao.getAllBranches());
                request.setAttribute("roles", dao.getAllRoles());
                request.getRequestDispatcher("userlist.jsp").forward(request, response);
            }
        }
    }

    // Helper method to escape JSON strings (basic escaping for quotes and backslashes)
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\\\"").replace("\\", "\\\\");
    }

    @Override
    public String getServletInfo() {
        return "User List Servlet";
    }
}