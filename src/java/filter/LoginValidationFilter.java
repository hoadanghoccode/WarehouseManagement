/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author duong
 */
@WebFilter(filterName = "LoginValidationFilter", urlPatterns = {"/login"})
public class LoginValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Kiểm tra email
        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("error", "Please enter email!");
            req.getRequestDispatcher("login.jsp").forward(req, res);
            return;
        }

        // Kiểm tra password
        if (password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Please enter password!");
            req.setAttribute("email", email);
            req.getRequestDispatcher("login.jsp").forward(req, res);
            return;
        }

        // Regex: ít nhất 8 ký tự, có số, chữ cái và ký tự đặc biệt
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\S]{8,}$";

        if (!password.matches(passwordRegex)) {
            req.setAttribute("error", "Password must be at least 8 characters, include a letter, a number, and a special character.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("login.jsp").forward(req, res);
            return;
        }

        // ✅ Cho phép tiếp tục
        chain.doFilter(request, response);
    }
}
