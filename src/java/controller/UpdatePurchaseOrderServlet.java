/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */
import dal.MaterialDAO;
import dal.PurchaseOrderDAO;
import dal.QualityDAO;
import dal.SubUnitDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.PurchaseOrders;
import model.PurchaseOrderDetail;
import model.Users;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UpdatePurchaseOrderServlet", urlPatterns = {"/update-purchase-order"})
public class UpdatePurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("USER");
        if (user == null) {
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
        request.setAttribute("warehouses", dao.getAllWarehouses());
        request.setAttribute("suppliers", dao.getAllSuppliers());
        request.setAttribute("materials", new MaterialDAO().getAllMaterials(null, null, null, null));
        request.setAttribute("subUnits", new SubUnitDAO().getAllSubUnits(false));
        request.setAttribute("qualities", new QualityDAO().getAllQualities());
        request.getRequestDispatcher("editPurchaseOrder.jsp").forward(request, response);
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
        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        if (purchaseOrderId == 0) {
            response.sendRedirect("list-purchase-order?error=Invalid Purchase Order ID");
            return;
        }

        String supplierIdRaw = request.getParameter("supplierId");
        String note = request.getParameter("note");
        String[] detailIds = request.getParameterValues("detailId");
        String[] prices = request.getParameterValues("price");

        Integer supplierId = parseIntOrNull(supplierIdRaw);
        if (supplierId == null || supplierId == 0) {
            response.sendRedirect("update-purchase-order?id=" + purchaseOrderId + "&error=Supplier is required");
            return;
        }

        List<PurchaseOrderDetail> details = new ArrayList<>();
        if (detailIds != null && prices != null && detailIds.length == prices.length) {
            for (int i = 0; i < detailIds.length; i++) {
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setPurchaseOrderDetailId(parseIntOrNull(detailIds[i]));
                try {
                    if (prices[i] == null || prices[i].trim().isEmpty()) {
                        response.sendRedirect("update-purchase-order?id=" + purchaseOrderId + "&error=Price is required for all details");
                        return;
                    }
                    double price = Double.parseDouble(prices[i]);
                    if (price <= 0) {
                        response.sendRedirect("update-purchase-order?id=" + purchaseOrderId + "&error=Price must be greater than zero");
                        return;
                    }
                    detail.setPrice(price);
                } catch (NumberFormatException e) {
                    response.sendRedirect("update-purchase-order?id=" + purchaseOrderId + "&error=Invalid price format");
                    return;
                }
                details.add(detail);
            }
        } else {
            response.sendRedirect("update-purchase-order?id=" + purchaseOrderId + "&error=Invalid detail data");
            return;
        }

        PurchaseOrders po = dao.getPurchaseOrderById(purchaseOrderId);
        if (po == null) {
            response.sendRedirect("list-purchase-order?error=Purchase Order not found");
            return;
        }

        po.setSupplierId(supplierId);
        po.setNote(note);

        boolean success = dao.updatePurchaseOrder(po, details);
        response.sendRedirect("list-purchase-order" + (success ? "?success=Purchase order updated successfully" : "?error=Failed to update purchase order"));
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