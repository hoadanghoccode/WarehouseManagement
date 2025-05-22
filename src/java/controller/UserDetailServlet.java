package controller;

import dal.UserDAO;
import model.Users;
import java.io.IOException;
import java.sql.Date;
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
            request.setAttribute("branches", dao.getAllBranches()); // Use getAllBranches
            request.setAttribute("roles", dao.getAllRoles());
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        } catch (Exception e) {
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

            String dobRaw = request.getParameter("dob");
            if (dobRaw == null || dobRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Date of birth is required");
            }
            java.sql.Date dateOfBirth = java.sql.Date.valueOf(dobRaw);

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

            String image = request.getParameter("image");
            if (image == null) {
                image = "";
            }

            String statusRaw = request.getParameter("status");
            if (statusRaw == null || (!statusRaw.equals("true") && !statusRaw.equals("false"))) {
                throw new IllegalArgumentException("Invalid status value");
            }
            boolean status = Boolean.parseBoolean(statusRaw);

            Users user = new Users(userId, roleId, branchId, fullName, "", password, gender, phoneNumber, address,
                    dateOfBirth, image, null, null, status, null, null);

            UserDAO dao = new UserDAO();
            dao.updateUser(user);

            response.sendRedirect("userdetail?id=" + userId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "User Detail Servlet";
    }
}