package controller;

import dal.UserDAO;
import model.Departmentt;
import model.Role;
import model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            doCreateUser(request, response);
        } else {
            doListUsers(request, response);
        }
    }

    protected void doListUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        int page = 1;
        int pageSize = 5;
        String searchQuery = request.getParameter("search");
        Integer departmentId = null;
        Integer roleId = null;
        Boolean status = null;
        String sortOrder = "desc";

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            // Default to page 1
        }

        try {
            departmentId = Integer.parseInt(request.getParameter("departmentId"));
        } catch (NumberFormatException e) {
            // Default to null
        }

        try {
            roleId = Integer.parseInt(request.getParameter("roleId"));
        } catch (NumberFormatException e) {
            // Default to null
        }

        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isEmpty()) {
            status = Boolean.parseBoolean(statusParam);
        }

        if (request.getParameter("sortOrder") != null) {
            sortOrder = request.getParameter("sortOrder");
        }

        List<Users> users = dao.getUsers(page, pageSize, searchQuery, departmentId, roleId, status, sortOrder);
        int totalUsers = dao.getTotalUsers(searchQuery, departmentId, roleId, status);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        List<Departmentt> departments = dao.getAllDepartments();
        List<Role> roles = dao.getAllRoles();

        request.setAttribute("users", users);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", searchQuery);
        request.setAttribute("selectedDepartmentId", departmentId);
        request.setAttribute("selectedRoleId", roleId);
        request.setAttribute("selectedStatus", statusParam);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("departments", departments);
        request.setAttribute("roles", roles);

        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    protected void doCreateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            boolean gender = "1".equals(request.getParameter("gender"));
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            java.util.Date dateOfBirth = null;
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthStr);
            }
            int departmentId = Integer.parseInt(request.getParameter("departmentId"));
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            boolean status = "true".equals(request.getParameter("status"));

            Users user = new Users(0, roleId, fullName, email, password, gender, phoneNumber, address, dateOfBirth, "", null, null, status, null, null);
            UserDAO dao = new UserDAO();
            dao.createUser(user, departmentId);
            session.setAttribute("success", "User created successfully");
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Create failed: " + e.getMessage());
        }
        response.sendRedirect("userlist");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "User List Servlet";
    }
}