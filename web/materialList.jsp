<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>
<%@ page import="model.MaterialDetail" %>
<%@ page import="dal.MaterialDAO" %>
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
        .no-data { text-align: center; padding: 24px; background-color: #f3f4f6; border-radius: 12px; font-size: 16px; color: #9ca3af; }
        .modal .modal-image { width: 200px; height: auto; float: left; margin-right: 20px; }
        .modal .detail-table { float: right; width: calc(100% - 220px); }
        .material-detail-table { margin-top: 20px; clear: both; }

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
                                        <c:set var="category" value="${material.categoryId}"/>
                                        <c:forEach var="cat" items="${categories}">
                                            <c:if test="${cat.categoryId == category}">${cat.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:set var="supplier" value="${material.supplierId}"/>
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
                                            <a href="delete-material?id=${material.materialId}" class="btn btn-danger btn-sm" title="Delete"
                                               onclick="return confirm('Are you sure you want to deactivate this material?')">
                                                <i class="fas fa-trash"></i>
                                            </a>
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

                <!-- Detail Modal -->
                <div class="modal fade" id="materialDetailModal" tabindex="-1" aria-labelledby="materialDetailModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="materialDetailModalLabel">Material Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <img src="" class="modal-image" alt="Material Image" id="modalImage" onerror="this.onerror=null; this.src='images/default-image.jpg';">
                                <div class="detail-table">
                                    <p><strong>ID:</strong> <span id="modalMaterialId"></span></p>
                                    <p><strong>Name:</strong> <span id="modalName"></span></p>
                                    <p><strong>Category:</strong> <span id="modalCategory"></span></p>
                                    <p><strong>Supplier:</strong> <span id="modalSupplier"></span></p>
                                    <p><strong>Created At:</strong> <span id="modalCreateAt"></span></p>
                                    <p><strong>Status:</strong> <span id="modalStatus"></span></p>
                                </div>
                                <table class="table material-detail-table">
                                    <thead>
                                        <tr>
                                            <th>Detail ID</th>
                                            <th>SubUnit ID</th>
                                            <th>Quality ID</th>
                                            <th>Quantity</th>
                                            <th>Last Updated</th>
                                        </tr>
                                    </thead>
                                    <tbody id="materialDetailTableBody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Handle view detail button click
        document.querySelectorAll('.view-detail').forEach(btn => {
            btn.addEventListener('click', function(event) {
                event.preventDefault();
                let materialId = this.getAttribute('data-id');
                fetch(`detail-material?id=${materialId}`, {
                    headers: { 'Accept': 'application/json' }
                })
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(data => {
                    document.getElementById('modalMaterialId').textContent = data.materialId || 'N/A';
                    document.getElementById('modalName').textContent = data.name || 'N/A';
                    document.getElementById('modalCategory').textContent = data.categoryName || 'N/A';
                    document.getElementById('modalSupplier').textContent = data.supplierName || 'N/A';
                    document.getElementById('modalCreateAt').textContent = data.createAt || 'N/A';
                    document.getElementById('modalStatus').textContent = data.status || 'N/A';
                    document.getElementById('modalImage').src = data.image || 'images/default-image.jpg';

                    let detailTableBody = document.getElementById('materialDetailTableBody');
                    detailTableBody.innerHTML = '';
                    if (data.details && data.details.length > 0) {
                        data.details.forEach(detail => {
                            let row = `<tr>
                                <td>${detail.materialDetailId || 'N/A'}</td>
                                <td>${detail.subUnitId || 'N/A'}</td>
                                <td>${detail.qualityId || 'N/A'}</td>
                                <td>${detail.quantity || 'N/A'}</td>
                                <td>${detail.lastUpdated || 'N/A'}</td>
                            </tr>`;
                            detailTableBody.innerHTML += row;
                        });
                    } else {
                        detailTableBody.innerHTML = '<tr><td colspan="5">No details available</td></tr>';
                    }

                    new bootstrap.Modal(document.getElementById('materialDetailModal')).show();
                })
                .catch(error => {
                    console.error('Error fetching material detail:', error);
                    alert('Failed to load material details. Please try again.');
                });
            }, { once: false }); // Allow multiple clicks but prevent default behavior
        });
    </script>
</body>
</html>