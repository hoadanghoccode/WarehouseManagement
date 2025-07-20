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
    try {
        importNoteId = Integer.parseInt(request.getParameter("importNoteId"));
    } catch (Exception ignored) {}

    Import_noteDAO inDao = new Import_noteDAO();
    Import_note in = inDao.getImportNoteById(importNoteId);
    List<Import_note_detail> details = inDao.getImportNoteDetailsByImportNoteId(importNoteId);

    UserDAO uDao = new UserDAO();
    Users user = (in != null) ? uDao.getUserById(in.getUserId()) : null;
    Warehouse warehouse = (in != null) ? new WarehouseDAO().getWarehouseById(in.getWarehouseId()) : null;
%>
<%
    if (details != null && !details.isEmpty()) {
        for (Import_note_detail ind : details) {
            Material material = new MaterialDAO().getMaterialById(ind.getMaterialId());
            request.setAttribute("material", material); 
%>
<c:set var="materialImage" value="${material.image}" />
<%      }
    }
%>
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

<!-- Import Note Information -->
<div class="row image-info">
    <div class="col-md-12 detail-table">
        <% if (in != null) { %>
        <p style="display: none;"><strong>ID:</strong> <%= in.getImportNoteId() %></p>
        <p style="display: none;"><strong>Order ID:</strong> <%= in.getOrderId() %></p>
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

<!-- Details Table -->
<div class="row">
    <div class="col-12">
        <table class="table material-detail-table">
            <thead>
                <tr>
                    <th>ID</th> 
                    <th style="display: none;">Detail ID</th> 
                    <th>Material Image</th>
                    <th>Material Name</th>
                    <th>Quantity</th>
                    <th>Quality</th>
                    <th>Imported</th>
                    <th>Quantity to Add</th>
                </tr>
            </thead>
            <tbody>
                <% if (details != null && !details.isEmpty()) {
                     int index = 1; 
                     for (Import_note_detail ind : details) {
                       Material material = new MaterialDAO().getMaterialById(ind.getMaterialId());
                       request.setAttribute("material", material);
                       Quality quality = new QualityDAO().getQualityById(ind.getQualityId());
                       List<Import_note_transaction> transactions = inDao.getTransactionsByDetailId(ind.getImportNoteDetailId());
                       double totalTransactionQty = inDao.getTotalTransactionQuantity(ind.getImportNoteDetailId());
                       double remainingQty = ind.getQuantity() - totalTransactionQty;
                %>
                <tr data-id="<%= ind.getImportNoteDetailId() %>">
                    <td><%= index++ %></td> 
                    <td style="display: none;"><%= ind.getImportNoteDetailId() %></td> 
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
                    <td>
                        <% if (!ind.isImported()) { %>
                        <input type="number" class="quantity-input form-control" name="quantity_<%= ind.getImportNoteDetailId() %>" value="<%= remainingQty > 0 ? remainingQty : 0 %>" min="0" max="<%= remainingQty %>" <%= ind.isImported() ? "disabled" : "" %>>
                        <% } else { %>
                        N/A
                        <% } %>
                    </td>
                </tr>
                <% if (transactions != null && !transactions.isEmpty()) { %>
                <tr>
                    <td colspan="7">
                        <table class="table transaction-table">
                            <thead>
                                <tr>
                                    <th>ID</th> 
                                    <th style="display: none;">Transaction ID</th>
                                    <th>User Imported</th>
                                    <th>Quantity</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% int transIndex = 1; %>
                                <% for (Import_note_transaction trans : transactions) { 
                                    Users importUser = uDao.getUserById(trans.getUserDoImportId());
                                %>
                                <tr>
                                    <td><%= transIndex++ %></td> 
                                    <td style="display: none;"><%= trans.getImportNoteTransactionId() %></td> 
                                    <td><%= importUser != null ? importUser.getFullName() : "N/A" %></td>
                                    <td><%= trans.getQuantity() %></td>
                                    <td><%= trans.getCreatedAt() %></td>
                                </tr>
                                <% } %>
                                <tr><td colspan="4">Total Imported: <%= totalTransactionQty %></td></tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <% } %>
                <tr><td colspan="7" class="detail-separator"></td></tr>
                <% } } else { %>
                <tr>
                    <td colspan="7" class="text-center text-muted">
                        No import note details available.
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <!-- Single Add to Inventory Button -->
        <button type="button" class="btn btn-primary" id="addButton">
            Add to Inventory
        </button>
    </div>
</div>

<!-- Toasts -->
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
    <div id="successToast" class="toast align-items-center text-bg-success border-0" role="alert"
         aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">Thêm vào kho thành công!</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto"
                    data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
    <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert"
         aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body" id="errorToastBody">Đã xảy ra lỗi!</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto"
                    data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    $(function () {
        $('#addButton').on('click', function () {
            var detailIds = [];
            var quantities = [];

            $('table.material-detail-table tbody tr[data-id]').each(function () {
                var detailId = $(this).data('id');
                var qtyInput = $(this).find('input[name="quantity_' + detailId + '"]');
                if (qtyInput.length && !qtyInput.is(':disabled')) {
                    var qty = parseFloat(qtyInput.val()) || 0;
                    if (qty > 0) {
                        detailIds.push(detailId);
                        quantities.push(qty);
                    }
                }
            });

            if (!detailIds.length) {
                $('#errorToastBody').text('Không có mục nào để thêm vào kho.');
                new bootstrap.Toast($('#errorToast')[0]).show();
                return;
            }

            $.ajax({
                url: 'import-note-to-inventory',
                type: 'POST',
                data: {
                    importNoteId: <%= importNoteId %>,
                    detailIds: detailIds,
                    quantities: quantities
                },
                traditional: true,
                dataType: 'json',
                success: function (res) {
                    if (res.success) {
                        new bootstrap.Toast($('#successToast')[0]).show();
                        setTimeout(function () {
                            location.reload();
                        }, 1500);
                    } else {
                        $('#errorToastBody').text(res.message.replace(/\\n/g, '\n'));
                        new bootstrap.Toast($('#errorToast')[0]).show();
                    }
                },
                error: function (xhr) {
                    var msg = xhr.responseJSON?.message || 'Không thể kết nối server';
                    $('#errorToastBody').text('Lỗi server: ' + msg);
                    new bootstrap.Toast($('#errorToast')[0]).show();
                }
            });
        });
    });
</script>