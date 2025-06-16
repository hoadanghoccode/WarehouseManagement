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

    Users user = (in != null) ? new UserDAO().getUserById(in.getUserId()) : null;
    Warehouse warehouse = (in != null) ? new WarehouseDAO().getWarehouseById(in.getWarehouseId()) : null;
%>

<style>
    .detail-table p { color: #000; margin-bottom: .5rem; }
    .detail-table strong { font-weight: 600; }
    .row.image-info { margin-bottom: 1.5rem; }
    .checkbox-column { width: 40px; }
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
                    <th class="checkbox-column">
                        <input type="checkbox" class="checkbox-all" />
                    </th>
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
                        Material material = new MaterialDAO().getMaterialById(ind.getMaterialId());
                        SubUnit subUnit     = new SubUnitDAO().getSubUnitById(ind.getSubUnitId());
                        Quality quality     = new QualityDAO().getQualityById(ind.getQualityId());
                        boolean done        = ind.isImported();
                %>
                <tr>
                    <td>
                        <input type="checkbox"
                               class="checkbox-item"
                               data-id="<%= ind.getImportNoteDetailId() %>"
                               <%= done ? "disabled" : "" %>
                        />
                    </td>
                    <td><%= ind.getImportNoteDetailId() %></td>
                    <td><%= material != null ? material.getName() : "N/A" %></td>
                    <td><%= subUnit  != null ? subUnit.getName()  : "N/A" %></td>
                    <td><%= ind.getQuantity() %></td>
                    <td><%= quality  != null ? quality.getQualityName() : "N/A" %></td>
                    <td><%= done ? "Yes" : "No" %></td>
                </tr>
                <%  }
                   } else { %>
                <tr>
                    <td colspan="7" class="text-center text-muted">
                        No import note details available.
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

<!--        <button type="button" class="btn btn-primary" id="addButton">
            Add to Inventory
        </button>-->
    </div>
</div>

<!-- Toast Container -->
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
  <div id="successToast" class="toast align-items-center text-bg-success border-0" role="alert"
       aria-live="assertive" aria-atomic="true">
    <div class="d-flex">
      <div class="toast-body">Thêm vào kho thành công!</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto"
              data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  </div>
</div>

<!-- Error Toast Container -->
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
  <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert"
       aria-live="assertive" aria-atomic="true">
    <div class="d-flex">
      <div class="toast-body" id="errorToastBody">Đã xảy ra lỗi!</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto"
              data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  </div>
</div>

<script>
$(function(){
    // Select/Unselect all checkbox (chỉ ảnh hưởng tới các item chưa disabled)
    $('.checkbox-all').on('click', function(){
        $('.checkbox-item:enabled').prop('checked', this.checked);
    });
    $('.checkbox-item').on('click', function(){
        var allChecked = $('.checkbox-item:enabled').length === $('.checkbox-item:enabled:checked').length;
        $('.checkbox-all').prop('checked', allChecked);
    });

    // Xử lý nút Add
    $('#addButton').on('click', function(){
        var detailIds = $('.checkbox-item:checked').map(function(){
            return $(this).data('id');
        }).get();

        if (detailIds.length === 0) {
            $('#errorToastBody').text('Vui lòng chọn ít nhất 1 mục để thêm vào kho.');
            var errorToast = new bootstrap.Toast(document.getElementById('errorToast'));
            errorToast.show();
            return;
        }

        $.ajax({
            url: 'import-note-to-inventory',
            type: 'POST',
            data: {
                importNoteId: <%= importNoteId %>,
                detailIds: detailIds
            },
            traditional: true,
            dataType: 'json'
        }).done(function(res){
            if (res.success) {
                // Hiện toast
                var toastEl = document.getElementById('successToast');
                var bsToast = new bootstrap.Toast(toastEl);
                bsToast.show();
                // Sau 1.5s reload page
                setTimeout(function(){
                    location.reload();
                }, 1500);
            } else {
                alert('Lỗi: ' + (res.message || 'Không thể hoàn thành'));
            }
        }).fail(function(){
            alert('Đã có lỗi khi gửi yêu cầu lên server.');
        });
    });
});
</script>
