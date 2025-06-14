<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>
<%@ page import="model.MaterialDetail" %>
<%@ page import="dal.MaterialDAO" %>
<%@ page import="model.Category" %>
<%@ page import="model.Supplier" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Materials List</title>
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
    <!-- Custom CSS -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .main-layout { display: flex; min-height: 100vh; }
        .main-content { flex: 1; margin-left: 260px; padding: 0; background-color: #f8f9fa; min-height: 100vh; transition: margin-left 0.3s ease; }
        .container { max-width: none; padding: 30px; margin: 0; }
        .title { font-size: 28px; font-weight: 600; color: #1f2937; margin-bottom: 16px; }
        .stats-info { margin-bottom: 16px; color: #374151; display: flex; align-items: center; gap: 8px; background-color: #dbeafe; padding: 12px 16px; border-radius: 8px; font-size: 14px; }
        .stats-info i { color: #3b82f6; }
        .stats-info strong { color: #1f2937; }
        .table-container { overflow-x: auto; background-color: white; border-radius: 12px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08); }
        table.table { width: 100%; border-collapse: collapse; min-width: 600px; }
        table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; vertical-align: middle; }
        table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; position: sticky; top: 0; z-index: 2; }
        table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
        table.table tbody tr:hover { background-color: #eef2ff; }
        .action-buttons { display: flex; gap: 8px; }
        .no-data { text-align: center; padding: 24px; background-color: #f3f4f6; border-radius: 12px; font-size: 16px; color: #6b7280; }
        .modal .modal-image { width: 200px; height: auto; float: left; margin-right: 20px; }
        .modal .detail-table { float: right; width: calc(100% - 220px); }
        .material-detail-table { margin-top: 20px; clear: both; }
        .modal-image-placeholder { width: 200px; height: 150px; background-color: #f3f4f6; border: 1px solid #e5e7eb; text-align: center; padding-top: 60px; color: #6b7280; }

        @media (max-width: 768px) {
            .main-content { margin-left: 0; }
            .title { font-size: 24px; }
            table.table { min-width: 600px; }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <div class="main-layout">
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
            <div class="container">
                <h1 class="title">Materials List</h1>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="list-material" method="get" id="filterForm" style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" name="search" value="${search}" placeholder="Search by name..." class="search-input" />
                            </div>
                            <select class="form-select" id="categoryFilter" name="categoryId">
                                <option value="">All Categories</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.categoryId}" ${categoryId == category.categoryId ? 'selected' : ''}>
                                        ${category.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <select class="form-select" id="supplierFilter" name="supplierId">
                                <option value="">All Suppliers</option>
                                <c:forEach items="${suppliers}" var="supplier">
                                    <option value="${supplier.supplierId}" ${supplierId == supplier.supplierId ? 'selected' : ''}>
                                        ${supplier.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <select class="form-select" id="statusFilter" name="status">
                                <option value="">All Status</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                            <button type="submit" class="btn btn-primary" style="padding: 6px 12px;">Filter</button>
                        </form>
                    </div>
                    <a href="add-material" class="btn btn-success">
                        <i class="fas fa-plus"></i> Add Material
                    </a>
                </div>

                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th class="col-md-1">#</th>
                                <th class="col-md-5">Name</th>
                                <th class="col-md-2">Category</th>
                                <th class="col-md-2">Supplier</th>
                                <th class="col-md-2">Status</th>
                                <th class="col-md-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${materials}" varStatus="status">
                                <tr>
                                    <td><strong>${status.index + 1 + (page - 1) * 5}</strong></td>
                                    <td>${material.name}</td>
                                    <td>
                                        <c:set var="category" value="${material.categoryId}" />
                                        <c:forEach var="cat" items="${categories}">
                                            <c:if test="${cat.categoryId == category}">${cat.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:set var="supplier" value="${material.supplierId}" />
                                        <c:forEach var="sup" items="${suppliers}">
                                            <c:if test="${sup.supplierId == supplier}">${sup.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <span class="badge ${material.status == 'active' ? 'bg-success' : 'bg-danger'}">
                                            ${material.status}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <button class="btn btn-info btn-sm view-detail" data-id="${material.materialId}" title="View">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <a href="update-material?id=${material.materialId}" class="btn btn-primary btn-sm" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <!-- soft-delete trigger -->
                                            <button class="btn btn-danger btn-sm btn-delete" data-id="${material.materialId}" title="Deactivate">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="list-material?page=1&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="list-material?page=${page-1}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </span>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${totalPages <= 7}">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == page}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="list-material?page=${i}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${page <= 4}">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="list-material?page=${i}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                            <a href="list-material?page=${totalPages}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${totalPages}</a>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${page >= totalPages - 3}">
                                        <a href="list-material?page=1&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">1</a>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                        </c:if>
                                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="list-material?page=${i}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="list-material?page=1&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                        <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="list-material?page=${i}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="list-material?page=${totalPages}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="list-material?page=${page+1}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="list-material?page=${totalPages}&search=${search}&categoryId=${categoryId}&supplierId=${supplierId}&status=${status}" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages}
                        <c:if test="${not empty totalMaterials}">
                            (${totalMaterials} total materials)
                        </c:if>
                    </div>
                </c:if>

                <!-- Detail Modal (unchanged) -->
                <div class="modal fade" id="materialDetailModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="materialDetailModalLabel">Material Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="materialDetailContent">
                                <p>Loading...</p>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- Delete Confirm Modal -->
    <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Confirm Deactivation</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            Are you sure you want to deactivate this material?
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button class="btn btn-danger" id="confirmDeleteButton">Deactivate</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Toast Container -->
    <div class="position-fixed bottom-0 end-0 p-3" style="z-index:1080;">
      <div id="actionToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body" id="actionToastBody"></div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>
    </div>

    <!-- JS thư viện -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    $(function(){
      var deleteId = null;

      // View detail
      $('.view-detail').on('click', function(){
        var materialId = $(this).data('id');
        $('#materialDetailContent').load('getMaterial.jsp?materialId=' + materialId);
        new bootstrap.Modal(document.getElementById('materialDetailModal')).show();
      });

      // Open confirm modal
      $('.btn-delete').on('click', function(){
        deleteId = $(this).data('id');
        new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
      });

      // On confirm, redirect to delete
      $('#confirmDeleteButton').on('click', function(){
        window.location.href = 'delete-material?id=' + deleteId;
      });

      // Show toast if redirected with success/error
      const params = new URLSearchParams(window.location.search);
      if (params.has('success') || params.has('error')) {
        const isSuccess = params.has('success');
        const msg = params.get(isSuccess ? 'success' : 'error');
        const toastEl = document.getElementById('actionToast');
        toastEl.classList.remove('text-bg-success','text-bg-danger');
        toastEl.classList.add(isSuccess ? 'text-bg-success' : 'text-bg-danger');
        document.getElementById('actionToastBody').textContent = msg;
        new bootstrap.Toast(toastEl).show();
        history.replaceState(null,'',location.pathname);
      }
    });
    </script>
</body>
</html>