<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>
<%@ page import="model.Supplier" %>
<%@ page import="model.Category" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Material</title>
    <!-- Bootstrap CSS (v5.3) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <!-- menu css -->
    <link rel="stylesheet" href="css/metisMenu.css" />
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .main-layout { display: flex; min-height: 100vh; }
        .main-content { flex: 1; margin-left: 260px; background-color: #f8f9fa; padding: 0; min-height: 100vh; transition: margin-left 0.3s ease; }
        .container { max-width: none; padding: 30px; margin: 0; }
        .title { font-size: 28px; font-weight: 600; color: #1f2937; margin-bottom: 16px; }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; font-weight: 500; color: #1f2937; margin-bottom: 8px; }
        .form-group input, .form-group select {
            width: 100%; padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 8px; font-size: 14px; color: #374151; background-color: #fff;
        }
        .form-group input[readonly] { background-color: #f3f4f6; cursor: not-allowed; }
        .btn { padding: 10px 20px; border-radius: 8px; text-decoration: none; color: white; display: inline-flex; align-items: center; gap: 6px; cursor: pointer; font-size: 14px; border: none; transition: background-color 0.2s; }
        .btn-primary { background-color: #6366f1; }
        .btn-primary:hover { background-color: #4f46e5; }
        .btn-secondary { background-color: #6b7280; }
        .btn-secondary:hover { background-color: #4b5563; }
        .error { color: #ef4444; font-size: 14px; margin-top: 4px; display: block; }
        .general-error { color: #ef4444; font-size: 14px; margin-bottom: 16px; padding: 8px; background-color: #fee2e2; border-radius: 8px; }
        @media (max-width: 768px) {
            .main-content { margin-left: 0; }
            .title { font-size: 24px; }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <div class="main-layout">
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
            <div class="container">
                <h1 class="title">Update Material</h1>

                <c:if test="${not empty error}">
                    <div class="general-error">${error}</div>
                </c:if>
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success">${param.success}</div>
                </c:if>

                <form action="update-material" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="materialId" value="${material.materialId}" />
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" class="form-control" value="${material.name}" required />
                        <c:if test="${not empty param.error and param.error.contains('Name')}">
                            <span class="error">Name ${param.error.contains('empty') ? 'cannot be empty' : 'must not exceed 250 characters.'}</span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="categoryId">Category</label>
                        <select id="categoryId" name="categoryId" class="form-control" required>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}" ${material.categoryId == category.categoryId ? 'selected' : ''}>
                                    ${category.name}
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty param.error and param.error.contains('category')}">
                            <span class="error">Invalid category.</span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="supplierId">Supplier</label>
                        <select id="supplierId" name="supplierId" class="form-control" required>
                            <c:forEach items="${suppliers}" var="supplier">
                                <option value="${supplier.supplierId}" ${material.supplierId == supplier.supplierId ? 'selected' : ''}>
                                    ${supplier.name}
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty param.error and param.error.contains('supplier')}">
                            <span class="error">Invalid supplier.</span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="imageFile">Upload New Image (optional)</label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/*" class="form-control">
                        <small class="text-muted">Leave blank to keep the current image.</small>
                        <div class="mt-2">
                            <strong>Current Image:</strong><br>
                            <img src="${material.image}" alt="Material Image" style="max-height: 150px;">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" class="form-control" required>
                            <option value="active" ${material.status == 'active' ? 'selected' : ''}>Active</option>
                            <option value="inactive" ${material.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                        <c:if test="${not empty param.error and param.error.contains('Status')}">
                            <span class="error">Status must be 'active' or 'inactive'.</span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="createAt">Created At</label>
                        <input type="text" id="createAt" name="createAt" class="form-control" value="${material.createAt}" readonly />
                    </div>
                    <div class="header-actions">
                        <button type="submit" class="btn btn-primary">Update Material</button>
                        <a href="list-material" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Toast Container -->
    <div class="position-fixed bottom-0 end-0 p-3" style="z-index:1080;">
      <div id="updateToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body"></div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>
    </div>

    <!-- JS thư viện -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    document.addEventListener('DOMContentLoaded', function(){
      var toastEl = document.getElementById('updateToast');
      var bodyEl  = toastEl.querySelector('.toast-body');
      var err     = '<c:out value="${error}" default=""/>';
      var ok      = '<c:out value="${param.success}" default=""/>';
      if (err) {
        toastEl.classList.add('text-bg-danger');
        bodyEl.textContent = err;
        new bootstrap.Toast(toastEl).show();
      } else if (ok) {
        toastEl.classList.remove('text-bg-danger');
        toastEl.classList.add('text-bg-success');
        bodyEl.textContent = ok;
        new bootstrap.Toast(toastEl).show();
      }
    });
    </script>
</body>
</html>
