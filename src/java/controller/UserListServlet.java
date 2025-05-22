package controller;

import dal.UserDAO;
import model.Users;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        int page = 1;
        int pageSize = 10;
        String searchQuery = "";
        Integer branchId = null;
        Integer roleId = null;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 on invalid input
            }
        }

        if (request.getParameter("search") != null) {
            searchQuery = request.getParameter("search");
        }
        if (request.getParameter("branchId") != null && !request.getParameter("branchId").isEmpty()) {
            try {
                branchId = Integer.parseInt(request.getParameter("branchId"));
            } catch (NumberFormatException e) {
                branchId = null;
            }
        }
        if (request.getParameter("roleId") != null && !request.getParameter("roleId").isEmpty()) {
            try {
                roleId = Integer.parseInt(request.getParameter("roleId"));
            } catch (NumberFormatException e) {
                roleId = null;
            }
        }

        UserDAO userDAO = new UserDAO();
        List<Users> users = userDAO.getUsers(page, pageSize, searchQuery, branchId, roleId);
        int totalUsers = userDAO.getTotalUsers(searchQuery, branchId, roleId);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("roles", userDAO.getAllRoles());
        request.setAttribute("branches", userDAO.getAllBranches()); // Use getAllBranches instead
        request.setAttribute("selectedBranchId", branchId);
        request.setAttribute("selectedRoleId", roleId);

        request.getRequestDispatcher("/userlist.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        if ("create".equals(action)) {
            try {
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                int gender = Integer.parseInt(request.getParameter("gender"));
                String phoneNumber = request.getParameter("phoneNumber");
                String address = request.getParameter("address");
                String dob = request.getParameter("dob");
                int branchId = Integer.parseInt(request.getParameter("branchId"));
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                boolean status = Boolean.parseBoolean(request.getParameter("status"));

                java.sql.Date dateOfBirth = java.sql.Date.valueOf(dob);

                Users user = new Users(0, roleId, branchId, fullName, email, password, gender, phoneNumber, address,
                        dateOfBirth, null, null, null, status, null, null);

                dao.createUser(user);
                response.sendRedirect(request.getContextPath() + "/userlist?page=1&search=" + 
                    (request.getParameter("search") != null ? request.getParameter("search") : "") +
                    "&branchId=" + (branchId != 0 ? branchId : "") +
                    "&roleId=" + (roleId != 0 ? roleId : ""));
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Create user failed");
            }
        } else if ("delete".equals(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                dao.deleteUser(userId);
                response.sendRedirect(request.getContextPath() + "/userlist?page=1&search=" + 
                    (request.getParameter("search") != null ? request.getParameter("search") : "") +
                    "&branchId=" + (request.getParameter("branchId") != null ? request.getParameter("branchId") : "") +
                    "&roleId=" + (request.getParameter("roleId") != null ? request.getParameter("roleId") : ""));
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Delete user failed");
            }
        } else {
            processRequest(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "User List Servlet";
    }
}