package controller;

import dal.UserDAO;
import model.Users;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
            return;
        }

        try {
            int userId = Integer.parseInt(idRaw);
            if (userId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }

            UserDAO dao = new UserDAO();
            Users user = dao.getUserById(userId);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            request.setAttribute("user", user);
            request.setAttribute("branches", dao.getAllBranches());
            request.setAttribute("roles", dao.getAllRoles());
            request.setAttribute("groups", dao.getGroupsByRoleId(user.getRoleId()));
            request.setAttribute("userGroupIds", dao.getUserGroupIds(userId));
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid user ID format: " + idRaw);
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        } catch (Exception e) {
            System.err.println("Error fetching user details for ID " + idRaw + ": " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            if (userId <= 0) {
                throw new IllegalArgumentException("Invalid user ID");
            }

            String fullName = request.getParameter("fullName");
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name is required");
            }

            String password = request.getParameter("password");
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }

            String genderRaw = request.getParameter("gender");
            if (genderRaw == null || (!genderRaw.equals("1") && !genderRaw.equals("0"))) {
                throw new IllegalArgumentException("Invalid gender value");
            }
            int gender = Integer.parseInt(genderRaw);

            String phoneNumber = request.getParameter("phoneNumber");
            if (phoneNumber == null) {
                phoneNumber = "";
            }

            String address = request.getParameter("address");
            if (address == null) {
                address = "";
            }

            String dobRaw = request.getParameter("dateOfBirth");
            java.sql.Date dateOfBirth = null;
            if (dobRaw != null && !dobRaw.trim().isEmpty()) {
                try {
                    dateOfBirth = java.sql.Date.valueOf(dobRaw);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid date of birth format. Use YYYY-MM-DD.");
                }
            }

            String branchIdRaw = request.getParameter("branchId");
            if (branchIdRaw == null || branchIdRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch is required");
            }
            int branchId = Integer.parseInt(branchIdRaw);

            String roleIdRaw = request.getParameter("roleId");
            if (roleIdRaw == null || roleIdRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Role is required");
            }
            int roleId = Integer.parseInt(roleIdRaw);

            String[] groupIdsRaw = request.getParameterValues("groupIds");
            List<Integer> groupIds = new ArrayList<>();
            if (groupIdsRaw != null) {
                for (String groupId : groupIdsRaw) {
                    groupIds.add(Integer.parseInt(groupId));
                }
            }

            String image = request.getParameter("image");
            if (image == null) {
                image = "";
            }

            String statusRaw = request.getParameter("status");
            if (statusRaw == null || (!statusRaw.equals("true") && !statusRaw.equals("false"))) {
                throw new IllegalArgumentException("Invalid status value");
            }
            boolean status = Boolean.parseBoolean(statusRaw);

            UserDAO dao = new UserDAO();
            Users existingUser = dao.getUserById(userId);
            if (existingUser == null) {
                throw new IllegalArgumentException("User not found");
            }

            Users user = new Users(userId, roleId, branchId, fullName, existingUser.getEmail(), password, gender,
                    phoneNumber, address, dateOfBirth, image, null, new java.sql.Date(System.currentTimeMillis()),
                    status, null, null);

            dao.updateUser(user, groupIds);

            response.sendRedirect("userdetail?id=" + userId);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error updating user: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            UserDAO dao = new UserDAO();
            int userId = Integer.parseInt(request.getParameter("userId"));
            request.setAttribute("user", dao.getUserById(userId));
            request.setAttribute("branches", dao.getAllBranches());
            request.setAttribute("roles", dao.getAllRoles());
            request.setAttribute("groups", dao.getGroupsByRoleId(Integer.parseInt(request.getParameter("roleId"))));
            request.setAttribute("userGroupIds", dao.getUserGroupIds(userId));
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Update failed: " + e.getMessage());
            UserDAO dao = new UserDAO();
            int userId = Integer.parseInt(request.getParameter("userId"));
            request.setAttribute("user", dao.getUserById(userId));
            request.setAttribute("branches", dao.getAllBranches());
            request.setAttribute("roles", dao.getAllRoles());
            request.setAttribute("groups", dao.getGroupsByRoleId(Integer.parseInt(request.getParameter("roleId"))));
            request.setAttribute("userGroupIds", dao.getUserGroupIds(userId));
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "User Detail Servlet";
    }
}