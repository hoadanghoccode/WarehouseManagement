/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.sql.Timestamp;
import model.Users;
import util.ResetService;

/**
 *
 * @author duong
 */
@WebServlet(name = "AdminResetPasswordController", urlPatterns = {"/adminresetpassword"})
public class AdminResetPasswordController extends HttpServlet {


    UserDAO userDAO = new UserDAO();

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
            out.println("<title>Servlet AdminResetPasswordController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminResetPasswordController at " + request.getContextPath() + "</h1>");
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
         HttpSession session = request.getSession();
    String tokenStr = request.getParameter("token");

    if (tokenStr == null || tokenStr.isEmpty()) {
        request.setAttribute("message", "Missing token");
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        return;
    }

    UserDAO dao = new UserDAO();
    Users user = dao.getUserByResetToken(tokenStr);

    System.out.println("TOKEN FROM REQUEST = " + tokenStr);
    System.out.println("User from DB = " + user);

    if (user == null) {
        request.setAttribute("message", "User not found with this token.");
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        return;
    }

    ResetService service = new ResetService();
Timestamp ts = new Timestamp(user.getResetPasswordExpiry().getTime());
LocalDateTime resetTime = ts.toLocalDateTime();

System.out.println("DB Expiry Time = " + resetTime); // thời gian từ DB
System.out.println("Now = " + LocalDateTime.now());  // thời gian hiện tại
if (service.isExpireTime(resetTime)) {
    request.setAttribute("message", "Token expired.");
    request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
    return;

}

    // ✅ Tạo mật khẩu mới
    String newPassword = service.generateRandomPassword();
    dao.updatePasswordbyEmail(user.getEmail(), newPassword);
    dao.clearResetToken(user.getUserId());

    // ✅ Gửi email chứa mật khẩu mới
    String content = "<h2>Password Reset Successful</h2>"
            + "<p>Hello <b>" + user.getFullName() + "</b>,</p>"
            + "<p>Your new password is: <b>" + newPassword + "</b></p>"
            + "<p><a href='http://localhost:8080/WarehouseManagement/login.jsp'>Click here to login</a></p>";

    service.sendEmail(user.getEmail(), content, user.getFullName());

    request.setAttribute("message", "A new password has been sent to your email.");
    request.getRequestDispatcher("login.jsp").forward(request, response);
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
//        String email = request.getParameter("email");
//    String password = request.getParameter("password");
//    String confirmPassword = request.getParameter("confirm_password");
//
//    // Bước 1: Validate confirm password
//    if (!password.equals(confirmPassword)) {
//        request.setAttribute("message", "Confirm password must match password");
//        request.setAttribute("email", email);
//        request.getRequestDispatcher("adminresetrequest.jsp").forward(request, response);
//        return;
//    }
//
//    // Bước 2: Lấy token từ session
//    HttpSession session = request.getSession();
//    String tokenStr = (String) session.getAttribute("token");
//System.out.println("TOKEN FROM REQUEST = " + tokenStr);
//    if (tokenStr == null || tokenStr.trim().isEmpty()) {
//        request.setAttribute("message", "Missing token");
//        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
//        return;
//    }
//
//    // Bước 3: Lấy user theo token
//    UserDAO userDAO = new UserDAO();
//    Users user = userDAO.getUserByResetToken(tokenStr);
//    System.out.println("User from DB = " + user);
//    if (user == null) {
//        request.setAttribute("message", "Invalid token or user not found.");
//        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
//        return;
//    }
//
//    // Bước 4: Kiểm tra token có hết hạn không
//    ResetService service = new ResetService();
//    Date expiry = user.getResetPasswordExpiry();
//    if (expiry == null || service.isExpireTime(expiry.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
//        request.setAttribute("message", "Token is expired");
//        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
//        return;
//    }
//
//    // Bước 5: Cập nhật mật khẩu mới
//    userDAO.updatePasswordbyEmail(email, password);
//
//    // Bước 6: Xóa token trong DB
//    userDAO.clearResetToken(user.getUserId());
//
//    // Bước 7: Gửi mail thông báo mật khẩu mới
//    String name = user.getFullName();
//    String content = "<h2>Password Changed Successfully</h2>"
//            + "<p>Hello <b>" + name + "</b>,</p>"
//            + "<p>Your password has been changed successfully.</p>"
//            + "<p>New password: <b>" + password + "</b></p>"
//            + "<p><a href='http://localhost:8080/WarehouseMangement/login.jsp'>Click here to login</a></p>";
//
//    ResetService mailService = new ResetService();
//    mailService.sendEmail(email, content, name);
//
//    request.getRequestDispatcher("login.jsp").forward(request, response);
HttpSession session = request.getSession();
        String tokenStr = (String) session.getAttribute("token");
        Integer userId = (Integer) session.getAttribute("userId");

        UserDAO userDAO = new UserDAO();
        Users user = userDAO.getUserByID(userId);
        ResetService service = new ResetService();

        if (user == null || tokenStr == null || tokenStr.isEmpty()) {
            request.setAttribute("message", "Invalid session or token");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        // Kiểm tra token hết hạn
        if (service.isExpireTime(user.getResetPasswordExpiry().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())) {
            request.setAttribute("message", "Token is expired");
            request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
            return;
        }

        // Tạo password mới ngẫu nhiên
        String newPassword = service.generateRandomPassword();
        userDAO.updatePasswordbyEmail(user.getEmail(), newPassword);

        // Gửi email thông báo password mới
        String content = "<h2>Password Reset Successfully</h2>"
                + "<p>Hello <b>" + user.getFullName() + "</b>,</p>"
                + "<p>Your new password is: <b>" + newPassword + "</b></p>"
                + "<p>You can log in and change your password later if needed.</p>"
                + "<p><a href='http://localhost:8080/WarehouseManagement/login.jsp'>Login here</a></p>";

        service.sendEmail(user.getEmail(), content, user.getFullName());

        // Xóa token sau khi sử dụng
        userDAO.clearResetToken(user.getUserId());

        request.setAttribute("message", "New password has been sent to user's email");
        request.getRequestDispatcher("login.jsp").forward(request, response);
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
