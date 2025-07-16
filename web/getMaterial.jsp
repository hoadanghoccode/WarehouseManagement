<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="
    dal.MaterialDAO,
    dal.QualityDAO,
    model.Material,
    model.MaterialDetail,
    model.Quality
" %>
<%
    int materialId = 0;
    try { materialId = Integer.parseInt(request.getParameter("materialId")); }
    catch(Exception ignored){}

    MaterialDAO mDao    = new MaterialDAO();
    QualityDAO  qDao    = new QualityDAO();

    Material    m       = mDao.getMaterialById(materialId);
    java.util.List<MaterialDetail> details = mDao.getMaterialDetailsByMaterialId(materialId);
%>

<style>
  /* Info text đen, label bold */
  .detail-table p { color: #000; margin-bottom: .5rem; }
  .detail-table strong { font-weight: 600; }

  /* Ảnh + placeholder */
  .modal-image {
    width: 100%; max-width: 300px; display: block; margin: 0 auto;
    object-fit: contain;
  }
  .modal-image-placeholder {
    width: 100%; max-width: 300px; height: 200px;
    background: #f3f4f6; border: 1px solid #e5e7eb;
    display: flex; align-items: center; justify-content: center;
    color: #6b7280; font-style: italic; margin: 0 auto;
  }

  /* Khoảng cách giữa row ảnh-info và bảng */
  .row.image-info { margin-bottom: 1.5rem; }

  /* Ẩn bảng mặc định */
  .material-detail-table { display: none; }
</style>

<!-- Row ảnh + info -->
<div class="row image-info">
  <div class="col-md-6 d-flex align-items-center justify-content-center">
    <% if (m != null && m.getImage()!=null && !m.getImage().isEmpty()) { %>
      <img src="<%=m.getImage()%>" class="modal-image"
           onerror="this.style.display='none';document.getElementById('no-img').style.display='flex';">
      <div id="no-img" class="modal-image-placeholder" style="display:none;">
        Không load được ảnh
      </div>
    <% } else { %>
      <div class="modal-image-placeholder">Không load được ảnh</div>
    <% } %>
  </div>
  <div class="col-md-6 detail-table">
    <% if (m != null) { %>
      <p><strong>Name:</strong> <%= m.getName() %></p>
      <p><strong>Category:</strong> <%= m.getCategoryName() %></p>
      <p><strong>Unit:</strong> <%= m.getUnitName() %></p>
      <p><strong>Created At:</strong> <%= m.getCreateAt() %></p>
      <p><strong>Status:</strong>
        <% if ("active".equalsIgnoreCase(m.getStatus())) { %>
          <span class="badge bg-success">active</span>
        <% } else { %>
          <span class="badge bg-danger"><%= m.getStatus() %></span>
        <% } %>
      </p>
    <% } else { %>
      <p class="text-muted">Material not found.</p>
    <% } %>
  </div>
</div>

<!-- Nút hiển thị bảng -->
<button type="button" class="btn btn-primary" id="showInventoryBtn">Show Quantity in Inventory</button>

<div class="row">
  <div class="col-12">
    <table class="table material-detail-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Quality</th>
          <th>Quantity</th>
          <th>Last Updated</th>
        </tr>
      </thead>
      <tbody>
        <%
          if (details != null && !details.isEmpty()) {
            int index = 1; 
            for (MaterialDetail md : details) {
              Quality qu = qDao.getQualityById(md.getQualityId());
        %>
        <tr>
          <td><%= index ++ %></td>
          <td>
            <% if (qu != null && "available".equalsIgnoreCase(qu.getQualityName())) { %>
              <span class="badge bg-success">available</span>
            <% } else { %>
              <span class="badge bg-danger"><%= qu!=null?qu.getQualityName():"N/A" %></span>
            <% } %>
          </td>
          <td><%= md.getQuantity() %></td>
          <td><%= md.getLastUpdated() %></td>
        </tr>
        <%    }
          } else { %>
        <tr>
          <td colspan="4" class="text-center text-muted">No material details available.</td>
        </tr>
        <% } %>
      </tbody>
    </table>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
  $(function(){
    $('#showInventoryBtn').on('click', function(){
      $('.material-detail-table').toggle();
    });
  });
</script>