<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dal.Import_noteDAO, model.Import_note, model.Import_note_detail, java.util.List" %>
<%@ page import="dal.UserDAO, model.Users" %>
<%@ page import="dal.WarehouseDAO, model.Warehouse" %>
<%@ page import="dal.MaterialDAO, model.Material" %>
<%@ page import="dal.QualityDAO, model.Quality" %>
<%@ page import="dal.Import_noteDAO" %>
<%@ page import="model.Import_note_transaction" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    int importNoteId = 0;
    try { importNoteId = Integer.parseInt(request.getParameter("importNoteId")); } catch(Exception ignored) {}

    Import_noteDAO inDao = new Import_noteDAO();
    Import_note in = inDao.getImportNoteById(importNoteId);
    List<Import_note_detail> details = inDao.getImportNoteDetailsByImportNoteId(importNoteId);

    UserDAO uDao = new UserDAO();
    WarehouseDAO wDao = new WarehouseDAO();
    MaterialDAO mDao = new MaterialDAO();
    QualityDAO qDao = new QualityDAO();

    Users user = (in != null) ? uDao.getUserById(in.getUserId()) : null;
    Warehouse warehouse = (in != null) ? wDao.getWarehouseById(in.getWarehouseId()) : null;
%>
<%
    if (details != null && !details.isEmpty()) {
        for (Import_note_detail ind : details) {
            Material material = mDao.getMaterialById(ind.getMaterialId());
            request.setAttribute("material", material); 
%>
<c:set var="materialImage" value="${material.image}" />
<%      }
    }
%>
<link rel="stylesheet" href="css/materiallist.css" />
<style>
    /* detail-specific overrides */
    .detail-table p {
        color: #1f2937;
        margin-bottom: .75rem;
    }
    .detail-table strong {
        font-weight: 600;
    }
    .row.image-info {
        margin-bottom: 1.5rem;
    }
    .material-image {
        max-height: 50px;
        max-width: 50px;
    }
    .transaction-table {
        margin-top: 1rem;
        border: 1px solid #e5e7eb;
    }
    .quantity-input {
        width: 100px;
    }
    .detail-separator {
        border-bottom: 1px solid #e5e7eb;
        margin: 1rem 0;
    }
    .material-detail-table tbody tr[data-id] > td,
    .detail-separator {
        border-bottom: 2px solid #000 !important;
    }
    
</style>

<div class="row image-info">
    <div class="div-md-12 detail-table">
        <% if (in != null) { %>
        <p><strong>ID:</strong> <%= in.getImportNoteId() %></p>
        <p><strong>Order ID:</strong> <%= in.getOrderId() %></p>
        <p><strong>User Name:</strong> <%= user != null ? user.getFullName() : "N/A" %></p>
        <p><strong>Warehouse Name:</strong> <%= warehouse != null ? warehouse.getName() : "N/A" %></p>
        <p><strong>Created At:</strong> <%= in.getCreatedAt() %></p>
        <p><strong>Imported:</strong> <%= in.isImported() ? "Yes" : "No" %></p>
        <p><strong>Imported At:</strong> <%= in.getImportedAt() != null ? in.getImportedAt() : "N/A" %></p>
        <% } else { %>
        <p class="text-muted">No import note details available.</p>
        <% } %>
    </div>
</div>

<div class="row">
    <div class="col-12">
        <table class="table material-detail-table">
            <thead>
                <tr>
                    <th>Detail ID</th>
                    <th>Material Image</th>
                    <th>Material Name</th>
                    <th>Quantity</th>
                    <th>Quality Name</th>
                    <th>Imported</th>
                </tr>
            </thead>
            <tbody>
                <% if (details != null && !details.isEmpty()) {
                    for (Import_note_detail ind : details) {
                        Material material = mDao.getMaterialById(ind.getMaterialId());
                        request.setAttribute("material", material); 
                        Quality quality = qDao.getQualityById(ind.getQualityId());
                        List<Import_note_transaction> transactions = inDao.getTransactionsByDetailId(ind.getImportNoteDetailId());
                        double totalTransactionQty = inDao.getTotalTransactionQuantity(ind.getImportNoteDetailId());
                %>
                <tr>
                    <td><%= ind.getImportNoteDetailId() %></td>
                    <td>
                        <c:if test="${not empty material.image}">
                            <img src="${material.image}" alt="Material Image" class="material-image">
                        </c:if>
                        <c:if test="${empty material.image}">
                            <span>No Image</span>
                        </c:if>
                    </td>
                    <td><%= material != null ? material.getName() : "N/A" %></td>
                    <td><%= ind.getQuantity() %></td>
                    <td><%= quality != null ? quality.getQualityName() : "N/A" %></td>
                    <td><%= ind.isImported() ? "Yes" : "No" %></td>
                </tr>
                <!-- Hiển thị transaction ngay dưới bản ghi -->
                <% if (transactions != null && !transactions.isEmpty()) { %>
                <tr>
                    <td colspan="6">
                        <table class="table transaction-table">
                            <thead><tr><th>Transaction ID</th><th>Quantity</th><th>Date</th></tr></thead>
                            <tbody>
                                <% for (Import_note_transaction trans : transactions) { %>
                                <tr>
                                    <td><%= trans.getImportNoteTransactionId() %></td>
                                    <td><%= trans.getQuantity() %></td>
                                    <td><%= trans.getCreatedAt() %></td>
                                </tr>
                                <% } %>
                                <tr><td colspan="3">Total Imported: <%= totalTransactionQty %></td></tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <% } %>
                <tr><td colspan="6" class="detail-separator"></td></tr>
                    <% } } else { %>
                <tr><td colspan="6" class="text-center text-muted">No import note details available.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>