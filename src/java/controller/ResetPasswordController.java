package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */



import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import util.ResetService;
import model.Users;

/**
 *
 * @author duong
 */
@WebServlet(name = "ResetPasswordController", urlPatterns={"/resetpassword"})
public class ResetPasswordController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet ResetPasswordController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResetPasswordController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
    String email = request.getParameter("email");

    UserDAO userDAO = new UserDAO();
    Users user = userDAO.getUserByEmail(email);

    if (user == null) {
        request.setAttribute("message", "Email not exist");
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        return;
    }

    ResetService service = new ResetService();
    String token = service.generateToken();
    LocalDateTime expiryTime = service.expireDateTime();
System.out.println("Token created = " + token);
System.out.println("Saved token for userId = " + user.getUserId());
    // 💾 Lưu token và thời gian hết hạn vào bảng users
    boolean updated = userDAO.saveResetToken(user.getUserId(), token, expiryTime);
    if (!updated) {
        request.setAttribute("message", "Error saving token. Please try again.");
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        return;
    }

    // 📩 Gửi email
    String link = "http://localhost:8080/WarehouseManagement/adminresetpassword?token=" + token;
    String content = "<h1>Xin chào " + user.getFullName() + "</h1>"
                   + "<p>Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng click vào link bên dưới để tiếp tục:</p>"
                   + "<a href='" + link + "'>Click here to reset password</a>";

    boolean isSent = service.sendEmail(user.getEmail(), content, user.getFullName());
    if (!isSent) {
        request.setAttribute("message", "Cannot send reset email.");
        request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
        return;
    }

    request.setAttribute("message", "Reset link sent successfully. Please check your email.");
    request.getRequestDispatcher("resetpassword.jsp").forward(request, response);
    }
    

    

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
