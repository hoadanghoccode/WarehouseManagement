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
        if (idRaw == null || idRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
            return;
        }

        try {
            int userId = Integer.parseInt(idRaw);
            UserDAO dao = new UserDAO();
            Users user = dao.getUserById(userId);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            request.setAttribute("user", user);
            request.setAttribute("branches", dao.getAllBranchIds());
            request.setAttribute("roles", dao.getAllRoles());
            request.getRequestDispatcher("userdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");
            int gender = Integer.parseInt(request.getParameter("gender"));
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String dob = request.getParameter("dob");
            int branchId = Integer.parseInt(request.getParameter("branchId"));
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            String image = request.getParameter("image");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            java.sql.Date dateOfBirth = java.sql.Date.valueOf(dob);

            Users user = new Users(userId, roleId, branchId, fullName, "", password, gender, phoneNumber, address,
                    dateOfBirth, image, null, null, status, null, null);

            UserDAO dao = new UserDAO();
            dao.updateUser(user);

            response.sendRedirect("userdetail?id=" + userId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}