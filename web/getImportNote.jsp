<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dal.Import_noteDAO, model.Import_note, model.Import_note_detail, java.util.List" %>
<%@ page import="dal.UserDAO, model.Users" %>
<%@ page import="dal.WarehouseDAO, model.Warehouse" %>
<%@ page import="dal.MaterialDAO, model.Material" %>
<%@ page import="dal.SubUnitDAO, model.SubUnit" %>
<%@ page import="dal.QualityDAO, model.Quality" %>
<%
    int importNoteId = 0;
    try { importNoteId = Integer.parseInt(request.getParameter("importNoteId")); } catch(Exception ignored) {}

    Import_noteDAO inDao = new Import_noteDAO();
    Import_note in = inDao.getImportNoteById(importNoteId);
    List<Import_note_detail> details = inDao.getImportNoteDetailsByImportNoteId(importNoteId);

    UserDAO uDao = new UserDAO();
    WarehouseDAO wDao = new WarehouseDAO();
    MaterialDAO mDao = new MaterialDAO();
    SubUnitDAO suDao = new SubUnitDAO();
    QualityDAO qDao = new QualityDAO();

    Users user = (in != null) ? uDao.getUserById(in.getUserId()) : null;
    Warehouse warehouse = (in != null) ? wDao.getWarehouseById(in.getWarehouseId()) : null;
%>

<style>
    .detail-table p { color: #000; margin-bottom: .5rem; }
    .detail-table strong { font-weight: 600; }
    .modal-image-placeholder {
        width: 100%; max-width: 300px; height: 200px;
        background: #f3f4f6; border: 1px solid #e5e7eb;
        display: flex; align-items: center; justify-content: center;
        color: #6b7280; font-style: italic; margin: 0 auto;
    }
    .row.image-info { margin-bottom: 1.5rem; }
</style>

<div class="row image-info">
    <div class="col-md-12 detail-table">
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
                    <th>Material Name</th>
                    <th>SubUnit Name</th>
                    <th>Quantity</th>
                    <th>Quality Name</th>
                    <th>Imported</th>
                </tr>
            </thead>
            <tbody>
                <% if (details != null && !details.isEmpty()) {
                    for (Import_note_detail ind : details) {
                        Material material = mDao.getMaterialById(ind.getMaterialId());
                        SubUnit subUnit = suDao.getSubUnitById(ind.getSubUnitId());
                        Quality quality = qDao.getQualityById(ind.getQualityId());
                %>
                    <tr>
                        <td><%= ind.getImportNoteDetailId() %></td>
                        <td><%= material != null ? material.getName() : "N/A" %></td>
                        <td><%= subUnit != null ? subUnit.getName() : "N/A" %></td>
                        <td><%= ind.getQuantity() %></td>
                        <td><%= quality != null ? quality.getQualityName() : "N/A" %></td>
                        <td><%= ind.isImported() ? "Yes" : "No" %></td>
                    </tr>
                <% } } else { %>
                    <tr><td colspan="6" class="text-center text-muted">No import note details available.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>