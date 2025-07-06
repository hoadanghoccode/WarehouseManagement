/*
 * Click .netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click .netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */
import dal.MaterialDAO;
import dal.PurchaseOrderDAO;
import dal.QualityDAO;
import dal.UnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.PurchaseOrders;
import model.Users;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "PurchaseOrderDetailController", urlPatterns = {"/purchase-order-detail"})
public class PurchaseOrderDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("USER");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int purchaseOrderId = parseIntOrNull(request.getParameter("id"));
        if (purchaseOrderId == 0) {
            response.sendRedirect("list-purchase-order?error=Invalid Purchase Order ID");
            return;
        }

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        PurchaseOrders purchaseOrder = dao.getPurchaseOrderById(purchaseOrderId);
        if (purchaseOrder == null) {
            response.sendRedirect("list-purchase-order?error=Purchase Order not found");
            return;
        }

        request.setAttribute("purchaseOrder", purchaseOrder);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("warehouses", dao.getAllWarehouses());
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.setAttribute("materials", new MaterialDAO().getAllMaterials(null, null, null, null));
        request.setAttribute("units", new UnitDAO().getAllUnits(false));
        request.setAttribute("qualities", new QualityDAO().getAllQualities());
        request.getRequestDispatcher("purchaseOrderDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int purchaseOrderId = parseIntOrNull(request.getParameter("id"));
        String action = request.getParameter("action");
        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        if (purchaseOrderId == 0) {
            response.sendRedirect("list-purchase-order?error=Invalid Purchase Order ID");
            return;
        }

        PurchaseOrders purchaseOrder = dao.getPurchaseOrderById(purchaseOrderId);
        if (purchaseOrder == null) {
            response.sendRedirect("list-purchase-order?error=Purchase Order not found");
            return;
        }

        if (!"pending".equals(purchaseOrder.getStatus())) {
            response.sendRedirect("purchase-order-detail?id=" + purchaseOrderId + "&error=Action only allowed for pending status");
            return;
        }

        String message = "";
        boolean success = false;
        if ("approve".equals(action)) {
            success = dao.updatePurchaseOrderStatus(purchaseOrderId, "approved");
            message = success ? "Purchase order approved successfully!" : "Failed to approve purchase order.";
        } else if ("reject".equals(action)) {
            success = dao.updatePurchaseOrderStatus(purchaseOrderId, "rejected");
            message = success ? "Purchase order rejected successfully!" : "Failed to reject purchase order.";
        } else {
            response.sendRedirect("purchase-order-detail?id=" + purchaseOrderId + "&error=Invalid action");
            return;
        }

        response.sendRedirect("purchase-order-detail?id=" + purchaseOrderId + (success ? "&success=" + message : "&error=" + message));
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