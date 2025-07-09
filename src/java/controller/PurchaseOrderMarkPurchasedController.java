/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */
import dal.PurchaseOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import java.io.IOException;

@WebServlet(name = "PurchaseOrderMarkPurchasedController", urlPatterns = {"/mark-purchased"})
public class PurchaseOrderMarkPurchasedController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int purchaseOrderId = parseIntOrNull(request.getParameter("id"));
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (purchaseOrderId == 0) {
            response.sendRedirect("list-purchase-order?error=Invalid Purchase Order ID");
            return;
        }

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        boolean success = dao.updatePurchaseOrderToPurchased(purchaseOrderId, user.getUserId());
        response.sendRedirect("list-purchase-order" + (success ? "?success=Purchase order marked as purchased successfully" : "?error=Failed to mark purchase order as purchased"));
    }

    private int parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}