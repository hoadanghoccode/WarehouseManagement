<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="
    dal.MaterialDAO,
    dal.SubUnitDAO,
    dal.QualityDAO,
    model.Material,
    model.MaterialDetail,
    model.Category,
    model.SubUnit,
    model.Quality
" %>
<%
    int materialId = 0;
    try { materialId = Integer.parseInt(request.getParameter("materialId")); }
    catch(Exception ignored){}

    MaterialDAO mDao    = new MaterialDAO();
    SubUnitDAO  suDao   = new SubUnitDAO();
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
</style>

<!-- Row ảnh + info -->
<div class="row image-info">
  <!-- ảnh -->
  <div class="col-md-6 d-flex align-items-center justify-content-center">
    <% if (m != null && m.getImage()!=null && !m.getImage().isEmpty()) { %>
      <img src="<%=m.getImage()%>" class="modal-image"
           onerror="this.style.display='none';document.getElementById('no-img').style.display='flex';">
      <div id="no-img" class="modal-image-placeholder" style="display:none;">
        Không load được ảnh
      </div>
    <% } else { %>
      <div class="modal-image-placeholder">
        Không load được ảnh
      </div>
    <% } %>
  </div>

  <!-- thông tin -->
  <div class="col-md-6 detail-table">
    <% if (m != null) { %>
      <p><strong>ID:</strong> <%= m.getMaterialId() %></p>
      <p><strong>Name:</strong> <%= m.getName() %></p>
      <p><strong>Category:</strong>
        <%
          for (Category c : mDao.getAllCategories()) {
            if (c.getCategoryId() == m.getCategoryId()) {
              out.print(c.getName()); break;
            }
          }
        %>
      </p>
      <p><strong>Created At:</strong> <%= m.getCreateAt() %></p>
      <p><strong>Status:</strong>
        <% if ("active".equalsIgnoreCase(m.getStatus())) { %>
          <span class="badge bg-success">active</span>
        <% } else { %>
          <span class="badge bg-danger">inactive</span>
        <% } %>
      </p>
    <% } else { %>
      <p class="text-muted">No material details available.</p>
    <% } %>
  </div>
</div>

<!-- Row bảng detail -->
<div class="row">
  <div class="col-12">
    <table class="table material-detail-table">
      <thead>
        <tr>
          <th>Detail ID</th>
          <th>SubUnit</th>
          <th>Quality</th>
          <th>Quantity</th>
          <th>Last Updated</th>
        </tr>
      </thead>
      <tbody>
        <%
          if (details != null && !details.isEmpty()) {
            for (MaterialDetail md : details) {
              SubUnit su = suDao.getSubUnitById(md.getSubUnitId());
              Quality qu = qDao.getQualityById(md.getQualityId());
        %>
        <tr>
          <td><%= md.getMaterialDetailId() %></td>
          <td><%= (su!=null? su.getName() : "N/A") %></td>
          <td>
            <% if (qu != null && "available".equalsIgnoreCase(qu.getQualityName())) { %>
              <span class="badge bg-success">available</span>
            <% } else { %>
              <span class="badge bg-danger"><%= (qu!=null? qu.getQualityName(): "N/A") %></span>
            <% } %>
          </td>
          <td><%= md.getQuantity() %></td>
          <td><%= md.getLastUpdated() %></td>
        </tr>
        <%    }
          } else { %>
        <tr>
          <td colspan="5" class="text-center text-muted">No material details available.</td>
        </tr>
        <% } %>
      </tbody>
    </table>
  </div>
</div>
