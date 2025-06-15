/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.UserDAO;
import dal.DepartmentDAO;
import dal.EmailDAO;
import model.Users;
import model.Department;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import java.util.regex.Pattern;
import util.ResetService;

/**
 *
 * @author PC
 */
public class CreateUserController extends HttpServlet {

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
            out.println("<title>Servlet CreateUserController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateUserController at " + request.getContextPath() + "</h1>");
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách phòng ban để hiển thị lên form tạo user
        UserDAO userDAO = new UserDAO();
        request.setAttribute("departments", userDAO.getAllDepartments());
        request.getRequestDispatcher("userlist.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String genderStr = request.getParameter("gender");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String image = request.getParameter("image");
            String statusStr = request.getParameter("status");
            String departmentIdStr = request.getParameter("departmentId");

            // Required field checks
            if (fullName == null || fullName.trim().isEmpty()) {
                response.setStatus(400);
                out.print("{\"field\":\"fullName\", \"error\":\"Họ tên không được để trống!\"}");
                return;
            }

            if (email == null || email.trim().isEmpty()) {
                response.setStatus(400);
                out.print("{\"field\":\"email\", \"error\":\"Email không được để trống!\"}");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                response.setStatus(400);
                out.print("{\"field\":\"password\", \"error\":\"Mật khẩu không được để trống!\"}");
                return;
            }

            if (genderStr == null || (!genderStr.equals("0") && !genderStr.equals("1"))) {
                response.setStatus(400);
                out.print("{\"field\":\"gender\", \"error\":\"Giới tính không hợp lệ!\"}");
                return;
            }

            if (departmentIdStr == null || departmentIdStr.trim().isEmpty()) {
                response.setStatus(400);
                out.print("{\"field\":\"departmentId\", \"error\":\"Phòng ban là bắt buộc!\"}");
                return;
            }

            if (statusStr == null || statusStr.trim().isEmpty()) {
                response.setStatus(400);
                out.print("{\"field\":\"status\", \"error\":\"Trạng thái là bắt buộc!\"}");
                return;
            }

            int departmentId = Integer.parseInt(departmentIdStr);
            boolean gender = "1".equals(genderStr);
            boolean status = "1".equals(statusStr) || "true".equalsIgnoreCase(statusStr);
            Date dateOfBirth = null;

            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                dateOfBirth = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthStr).getTime());
            }

            // Validate password
            if (password.length() < 6) {
                response.setStatus(400);
                out.print("{\"field\":\"password\", \"error\":\"Mật khẩu phải có ít nhất 6 ký tự!\"}");
                return;
            }
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$";
            if (!Pattern.matches(passwordPattern, password)) {
                response.setStatus(400);
                out.print("{\"field\":\"password\", \"error\":\"Mật khẩu phải gồm chữ hoa, thường, số và ký tự đặc biệt!\"}");
                return;
            }

            // Validate email format
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            if (!Pattern.matches(emailRegex, email)) {
                response.setStatus(400);
                out.print("{\"field\":\"email\", \"error\":\"Email không đúng định dạng!\"}");
                return;
            }

            // Validate phone (optional field)
            String phoneRegex = "^(03|05|07|08|09)\\d{8}$";
            if (phoneNumber != null && !phoneNumber.isEmpty() && !Pattern.matches(phoneRegex, phoneNumber)) {
                response.setStatus(400);
                out.print("{\"field\":\"phoneNumber\", \"error\":\"Số điện thoại phải là đầu số Việt Nam và đủ 10 số!\"}");
                return;
            }

            // Check email uniqueness
            UserDAO userDAO = new UserDAO();
            if (userDAO.checkUserByEmail(email)) {
                response.setStatus(409);
                out.print("{\"field\":\"email\", \"error\":\"Email đã tồn tại trong hệ thống!\"}");
                return;
            }

            // Check department exists
            DepartmentDAO departmentDAO = new DepartmentDAO();
            Department dept = departmentDAO.getDepartmentById(departmentId);
            if (dept == null) {
                response.setStatus(404);
                out.print("{\"field\":\"departmentId\", \"error\":\"Phòng ban không tồn tại!\"}");
                return;
            }

            int roleId = dept.getRole_id();

            // Create user
            Users user = new Users();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setGender(gender);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setDateOfBirth(dateOfBirth);
            user.setImage(image);
            user.setStatus(status);
            user.setRoleId(roleId);

            userDAO.createUser(user, departmentId);

            EmailDAO emailDAO = new EmailDAO();
            String template = emailDAO.getTemplateBodyByType("NEW_USER_ACCOUNT");
            if (template != null) {
                Map<String, String> values = new HashMap<>();
                values.put("user_name", fullName);
                values.put("user_email", email);
                values.put("password", password); // Nếu muốn gửi password cho user
                values.put("login_url", "http://localhost:8080/WarehouseManagement/login");
//                values.put("support_email", "support@yourdomain.com");
//                values.put("year", String.valueOf(Year.now().getValue()));
//                values.put("System Name", "Warehouse Management");

                String emailContent = emailDAO.fillTemplate(template, values);

                ResetService resetService = new ResetService();
                resetService.sendEmail(email, emailContent, fullName, "Welcome to Directory – Your account details inside");
            }

            response.setStatus(200);
            out.print("{\"message\":\"Tạo user thành công!\"}");

        } catch (NumberFormatException e) {
            response.setStatus(400);
            out.print("{\"field\":\"departmentId\", \"error\":\"Phòng ban không hợp lệ!\"}");
        } catch (Exception e) {
            response.setStatus(500);
            out.print("{\"error\":\"Lỗi máy chủ: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
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
