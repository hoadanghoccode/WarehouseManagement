<%-- 
    Document   : materialList
    Created on : May 21, 2025, 12:55:17 AM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Materials List</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
    <style>
        .container {
            padding: 24px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }
        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
        }
        .header-actions {
            display: flex;
            gap: 12px;
            align-items: center;
        }
        .search-container {
            position: relative;
            width: 300px;
        }
        .search-icon {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca3af;
        }
        .search-input {
            width: 100%;
            padding: 8px 12px 8px 36px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
        }
        .form-select, .form-control {
            padding: 8px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
        }
        .btn {
            padding: 8px 16px;
            border-radius: 8px;
            text-decoration: none;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }
        .btn-success {
            background-color: #10b981;
        }
        .btn-success:hover {
            background-color: #059669;
        }
        .btn-outline {
            background-color: transparent;
            border: 1px solid #9ca3af;
            color: #374151;
        }
        .btn-outline:hover {
            background-color: #f3f4f6;
        }
        .stats-info {
            margin-bottom: 16px;
            color: #6b7280;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .table-container {
            overflow-x: auto;
        }
        .table {
            width: 100%;
            border-collapse: collapse;
        }
        .table th, .table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
        }
        .table th {
            background-color: #f3f4f6;
            font-weight: 600;
            color: #1e293b;
        }
        .action-buttons {
            display: flex;
            gap: 8px;
        }
        .btn-info {
            background-color: #3b82f6;
        }
        .btn-info:hover {
            background-color: #2563eb;
        }
        .btn-primary {
            background-color: #6366f1;
        }
        .btn-primary:hover {
            background-color: #4f46e5;
        }
        .btn-danger {
            background-color: #ef4444;
        }
        .btn-danger:hover {
            background-color: #dc2626;
        }
        .no-data {
            text-align: center;
            padding: 24px;
            background-color: #f3f4f6;
            border-radius: 12px;
        }
        .no-data-icon {
            font-size: 48px;
            color: #9ca3af;
            margin-bottom: 16px;
        }
        .pagination {
            display: flex;
            justify-content: center;
            gap: 8px;
            margin-top: 24px;
        }
        .pagination a, .pagination span {
            padding: 8px 12px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            color: #374151;
            text-decoration: none;
        }
        .pagination .current {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
        }
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
        }
        .modal-content {
            background-color: white;
            margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            width: 70%;
            max-width: 500px;
            position: relative;
        }
        .close {
            position: absolute;
            top: 10px;
            right: 15px;
            font-size: 24px;
            cursor: pointer;
            color: #9ca3af;
        }
        .close:hover {
            color: #374151;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1 class="title">Materials List</h1>
            <div class="header-actions">
                <div class="search-container">
                    <form action="material" method="get" id="filterForm">
                        <input type="hidden" name="action" value="list">
                        <input type="hidden" name="page" value="${currentPage}">
                        <div style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" id="searchInput" name="search" value="${search}" placeholder="Search materials..." class="search-input" oninput="submitForm()" />
                            </div>
                            <select class="form-select" id="categoryFilter" name="categoryFilter" onchange="submitForm()">
                                <option value="">All Categories</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.name}" ${categoryFilter == category.name ? 'selected' : ''}>${category.name}</option>
                                </c:forEach>
                            </select>
                            <div class="quantity-filter">
                                <input type="number" step="0.01" class="form-control" id="quantityFilterMin" name="quantityMin" placeholder="Min" value="${quantityMin}" oninput="submitForm()">
                                <input type="number" step="0.01" class="form-control" id="quantityFilterMax" name="quantityMax" placeholder="Max" value="${quantityMax}" oninput="submitForm()">
                            </div>
                        </div>
                    </form>
                </div>
                <a href="material?action=add" class="btn btn-success">
                    <i class="fas fa-plus"></i> Add Material
                </a>
            </div>
        </div>

        <c:if test="${not empty materials}">
            <div class="stats-info">
                <i class="fas fa-info-circle"></i>
                <c:choose>
                    <c:when test="${not empty search || not empty categoryFilter || not empty quantityMin || not empty quantityMax}">
                        Found <strong>${totalMaterials}</strong> materials matching your filters
                    </c:when>
                    <c:otherwise>
                        Showing <strong>${materials.size()}</strong> / <strong>${totalMaterials}</strong> materials
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty materials}">
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Unit</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Supplier</th>
                                <th>Category</th>
                                <th>Parent Category</th>
                                <th>Active</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${materials}" varStatus="status">
                                <tr>
                                    <td><strong>${(currentPage - 1) * pageSize + status.index + 1}</strong></td>
                                    <td>${material.name}</td>
                                    <td>${material.unitName != null ? material.unitName : '-'}</td>
                                    <td>${material.price != 0 ? material.price : '-'}</td>
                                    <td>${material.quantity != 0 ? material.quantity : '-'}</td>
                                    <td>${material.supplierName != null ? material.supplierName : '-'}</td>
                                    <td>${material.categoryName}</td>
                                    <td>${material.parentCategoryName != null ? material.parentCategoryName : '-'}</td>
                                    <td>${material.status == 'active' ? 'true' : 'false'}</td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="#" class="btn btn-info view-detail" data-id="${material.materialId}" title="View">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="material?action=update&id=${material.materialId}" class="btn btn-primary" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="material?action=delete&id=${material.materialId}" class="btn btn-danger" title="Delete" onclick="return confirm('Are you sure you want to delete this material?')">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    <div class="no-data-icon">
                        <i class="fas fa-folder-open"></i>
                    </div>
                    <h3>No materials found</h3>
                    <c:choose>
                        <c:when test="${not empty search || not empty categoryFilter || not empty quantityMin || not empty quantityMax}">
                            <p>No materials match your filters</p>
                            <a href="material?action=list" class="btn btn-primary">View all materials</a>
                        </c:when>
                        <c:otherwise>
                            <p>No materials in the system yet</p>
                            <a href="material?action=add" class="btn btn-success">Add first material</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="material?action=list&page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">
                            <i class="fas fa-angle-double-left"></i>
                        </a>
                        <a href="material?action=list&page=${currentPage-1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">
                            <i class="fas fa-angle-left"></i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span style="opacity: 0.3; cursor: not-allowed;">
                            <i class="fas fa-angle-double-left"></i>
                        </span>
                        <span style="opacity: 0.3; cursor: not-allowed;">
                            <i class="fas fa-angle-left"></i>
                        </span>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${totalPages <= 7}">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="material?action=list&page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${currentPage <= 4}">
                                <c:forEach begin="1" end="5" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${totalPages > 6}">
                                    <span style="padding: 8px 4px;">...</span>
                                    <a href="material?action=list&page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${totalPages}</a>
                                </c:if>
                            </c:when>
                            <c:when test="${currentPage >= totalPages - 3}">
                                <a href="material?action=list&page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">1</a>
                                <c:if test="${totalPages > 6}">
                                    <span style="padding: 8px 4px;">...</span>
                                </c:if>
                                <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <a href="material?action=list&page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">1</a>
                                <span style="padding: 8px 4px;">...</span>
                                <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <span style="padding: 8px 4px;">...</span>
                                <a href="material?action=list&page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">${totalPages}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="material?action=list&page=${currentPage+1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">
                            <i class="fas fa-angle-right"></i>
                        </a>
                        <a href="material?action=list&page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty categoryFilter}'>&categoryFilter=${categoryFilter}</c:if><c:if test='${not empty quantityMin}'>&quantityMin=${quantityMin}</c:if><c:if test='${not empty quantityMax}'>&quantityMax=${quantityMax}</c:if>">
                            <i class="fas fa-angle-double-right"></i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span style="opacity: 0.3; cursor: not-allowed;">
                            <i class="fas fa-angle-right"></i>
                        </span>
                        <span style="opacity: 0.3; cursor: not-allowed;">
                            <i class="fas fa-angle-double-right"></i>
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>

    <div id="materialModal" class="modal">
        <div class="modal-content">
            <span class="close">×</span>
            <h2>Material Details</h2>
            <p><strong>ID:</strong> <span id="modalMaterialId"></span></p>
            <p><strong>Name:</strong> <span id="modalName"></span></p>
            <p><strong>Unit:</strong> <span id="modalUnitName"></span></p>
            <p><strong>Price:</strong> <span id="modalPrice"></span></p>
            <p><strong>Quantity:</strong> <span id="modalQuantity"></span></p>
            <p><strong>Supplier:</strong> <span id="modalSupplierName"></span></p>
            <p><strong>Status:</strong> <span id="modalStatus"></span></p>
            <p><strong>Category:</strong> <span id="modalCategoryName"></span></p>
            <p><strong>Parent Category:</strong> <span id="modalParentCategoryName"></span></p>
        </div>
    </div>

    <script>
        function submitForm() {
            document.getElementsByName("page")[0].value = 1;
            document.getElementById("filterForm").submit();
        }

        document.querySelectorAll('.view-detail').forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const materialId = this.getAttribute('data-id');
                fetch('material?action=detail&id=' + materialId, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                })
                .then(response => response.json())
                .then(data => {
                    document.getElementById('modalMaterialId').textContent = data.materialId;
                    document.getElementById('modalName').textContent = data.name;
                    document.getElementById('modalUnitName').textContent = data.unitName ? data.unitName : '-';
                    document.getElementById('modalPrice').textContent = data.price != 0 ? data.price : '-';
                    document.getElementById('modalQuantity').textContent = data.quantity != 0 ? data.quantity : '-';
                    document.getElementById('modalSupplierName').textContent = data.supplierName ? data.supplierName : 'No Supplier';
                    document.getElementById('modalStatus').textContent = data.status;
                    document.getElementById('modalCategoryName').textContent = data.categoryName;
                    document.getElementById('modalParentCategoryName').textContent = data.parentCategoryName ? data.parentCategoryName : '-';
                    document.getElementById('materialModal').style.display = 'block';
                })
                .catch(error => console.error('Error:', error));
            });
        });

        document.querySelector('.close').addEventListener('click', function() {
            document.getElementById('materialModal').style.display = 'none';
        });

        window.addEventListener('click', function(event) {
            if (event.target == document.getElementById('materialModal')) {
                document.getElementById('materialModal').style.display = 'none';
            }
        });
    </script>
</body>
</html>