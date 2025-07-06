<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dal.Import_noteDAO, model.Import_note, model.Import_note_detail, java.util.List" %>
<%@ page import="dal.UserDAO, model.Users" %>
<%@ page import="dal.WarehouseDAO, model.Warehouse" %>
<%@ page import="dal.MaterialDAO, model.Material" %>
<%@ page import="dal.QualityDAO, model.Quality" %>
<%
    int importNoteId = 0;
    try {
        importNoteId = Integer.parseInt(request.getParameter("importNoteId"));
    } catch (Exception ignored) {}

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
</style>

<!-- Thông tin Import Note -->
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

<!-- Bảng chi tiết, không còn checkbox -->
<div class="row">
  <div class="col-12">
    <table class="table material-detail-table">
      <thead>
        <tr>
          <th>Detail ID</th>
          <th>Material Name</th>
          <th>Quantity</th>
          <th>Quality Name</th>
          <th>Imported</th>
        </tr>
      </thead>
      <tbody>
        <% if (details != null && !details.isEmpty()) {
             for (Import_note_detail ind : details) {
               Material material = new MaterialDAO().getMaterialById(ind.getMaterialId());
               Quality quality = new QualityDAO().getQualityById(ind.getQualityId());
        %>
        <tr data-id="<%= ind.getImportNoteDetailId() %>">
          <td><%= ind.getImportNoteDetailId() %></td>
          <td><%= material != null ? material.getName() : "N/A" %></td>
          <td><%= ind.getQuantity() %></td>
          <td><%= quality != null ? quality.getQualityName() : "N/A" %></td>
          <td><%= ind.isImported() ? "Yes" : "No" %></td>
        </tr>
        <%   }
           } else { %>
        <tr>
          <td colspan="5" class="text-center text-muted">
            No import note details available.
          </td>
        </tr>
        <% } %>
      </tbody>
    </table>

    <!-- Chỉ 1 nút Add to Inventory -->
    <button type="button" class="btn btn-primary" id="addButton">
      Add to Inventory
    </button>
  </div>
</div>

<!-- Toasts (nếu chưa có trong trang cha) -->
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
  $(function(){
    $('#addButton').on('click', function(){
      var detailIds = $('table.material-detail-table tbody tr[data-id]')
                        .map(function(){ return $(this).data('id'); })
                        .get();
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
          detailIds: detailIds
        },
        traditional: true,
        dataType: 'json',
        success: function(res) {
          if (res.success) {
            new bootstrap.Toast($('#successToast')[0]).show();
            setTimeout(function(){ location.reload(); }, 1500);
          } else {
            $('#errorToastBody').text(res.message || 'Đã xảy ra lỗi!');
            new bootstrap.Toast($('#errorToast')[0]).show();
          }
        },
        error: function(xhr) {
          var msg = xhr.responseJSON?.message || 'Không thể kết nối server';
          $('#errorToastBody').text('Lỗi server: ' + msg);
          new bootstrap.Toast($('#errorToast')[0]).show();
        }
      });
    });
  });
</script>