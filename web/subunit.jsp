<%@ page import="java.util.List" %>
<%@ page import="model.SubUnit" %>
<%@ page import="dal.SubUnitDAO" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    @SuppressWarnings("unchecked")
    Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (perms == null) {
        perms = new HashMap<>();
    }
    request.setAttribute("perms", perms);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SubUnits List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="icon" href="img/logo.png" type="image/png">
    <link rel="stylesheet" href="css/metisMenu.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f8fafc;
            color: #334155;
        }

        .main-layout {
            display: flex;
            min-height: 100vh;
        }

        .main-content {
            flex: 1;
            margin-left: 260px;
            padding: 0;
            background-color: #f8fafc;
            min-height: 100vh;
            transition: margin-left 0.3s ease;
        }

        .container {
            max-width: none;
            padding: 24px;
            margin: 0;
        }

        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 16px;
        }

        .stats-info {
            background-color: #eff6ff;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            color: #1e40af;
            font-size: 14px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .stats-info i {
            color: #3b82f6;
        }

        .stats-info strong {
            color: #1e293b;
        }

        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            gap: 12px;
        }

        .search-container {
            position: relative;
            display: flex;
            gap: 12px;
            align-items: center;
        }

        .search-input {
            padding: 8px 16px 8px 40px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            width: 300px;
            background-color: white;
            transition: all 0.2s;
        }

        .search-input:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .search-icon {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: #6b7280;
            font-size: 14px;
        }

        .form-select {
            padding: 8px 30px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
            transition: all 0.2s;
        }

        .form-select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s;
        }

        .btn-primary {
            background-color: #3b82f6;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2563eb;
        }

        .btn-success {
            background-color: #10b981;
            color: white;
        }

        .btn-success:hover {
            background-color: #059669;
        }

        .btn-info {
            background-color: #06b6d4;
            color: white;
        }

        .btn-info:hover {
            background-color: #0891b2;
        }

        .btn-danger {
            background-color: #ef4444;
            color: white;
        }

        .btn-danger:hover {
            background-color: #dc2626;
        }

        .table-container {
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
        }

        .table th {
            background-color: #f8fafc;
            padding: 16px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            color: #374151;
            border-bottom: 1px solid #e5e7eb;
            position: sticky;
            top: 0;
            z-index: 2;
        }

        .table td {
            padding: 16px;
            border-bottom: 1px solid #f3f4f6;
            vertical-align: middle;
        }

        .table tr:hover {
            background-color: #f9fafb;
        }

        .action-buttons {
            display: flex;
            gap: 8px;
        }

        .action-buttons .btn {
            padding: 6px 12px;
            font-size: 12px;
        }

        .no-data {
            text-align: center;
            padding: 64px 32px;
            color: #6b7280;
            background-color: #f3f4f6;
            border-radius: 12px;
            font-size: 16px;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 20px;
        }

        .pagination a,
        .pagination span {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            text-decoration: none;
            color: #374151;
            font-size: 14px;
            transition: all 0.2s;
        }

        .pagination a:hover {
            background-color: #f3f4f6;
        }

        .pagination .current {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
        }

        .pagination .disabled {
            opacity: 0.3;
            cursor: not-allowed;
        }

        .modal-image-placeholder {
            width: 200px;
            height: 150px;
            background-color: #f3f4f6;
            border: 1px solid #e5e7eb;
            text-align: center;
            padding-top: 60px;
            color: #6b7280;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
            }

            .header-actions {
                flex-direction: column;
                gap: 16px;
                align-items: stretch;
            }

            .search-input, .form-select {
                width: 100%;
            }

            .action-buttons {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <div class="main-layout">
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
            <div class="container">
                <h1 class="title">SubUnits List</h1>

                <div class="stats-info">
                    <i class="fas fa-info-circle"></i>
                    Total SubUnits: <strong>${totalSubUnits}</strong>
                </div>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="${pageContext.request.contextPath}/subunit" method="get" id="filterForm" style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" name="search" value="${search}" placeholder="Search by name..." class="search-input form-control" />
                            </div>
                            <select class="form-select" id="statusFilter" name="status">
                                <option value="">All Status</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                            <button type="submit" class="btn btn-primary" style="padding: 6px 12px;">Filter</button>
                        </form>
                    </div>
                    <div>
                        <c:if test="${perms['Unit_ADD']}">
                            <button class="btn btn-success btn-add-subunit" title="Add SubUnit">
                                <i class="fas fa-plus"></i> Add SubUnit
                            </button>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/unit" class="btn btn-info" title="Go to Units">
                            <i class="fas fa-list"></i> Go to Units
                        </a>
                    </div>
                </div>

                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th class="col-md-1">#</th>
                                <th class="col-md-5">Name</th>
                                <th class="col-md-2">Status</th>
                                <th class="col-md-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty subUnits}">
                                <tr>
                                    <td colspan="4" class="no-data">No subunits found</td>
                                </tr>
                            </c:if>
                            <c:forEach var="subUnit" items="${subUnits}" varStatus="status">
                                <tr>
                                    <td><strong>${status.index + 1 + (page - 1) * 5}</strong></td>
                                    <td>${not empty subUnit.name ? subUnit.name : 'N/A'}</td>
                                    <td>
                                        <span class="badge ${subUnit.isActive ? 'bg-success' : 'bg-danger'}">
                                            ${subUnit.isActive ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <c:if test="${perms['Unit_VIEW']}">
                                                <button class="btn btn-info btn-sm view-detail" data-id="${subUnit.subUnitId}" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${perms['Unit_UPDATE']}">
                                                <button class="btn btn-primary btn-sm btn-edit-subunit" data-id="${subUnit.subUnitId}" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${perms['Unit_DELETE']}">
                                                <button class="btn btn-danger btn-sm btn-delete" data-id="${subUnit.subUnitId}" title="Deactivate" data-status="${subUnit.isActive ? 'active' : 'inactive'}">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPages > 0}">
                    <div class="pagination">
                        <!-- First Page -->
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="${pageContext.request.contextPath}/subunit?page=1&search=${search}&status=${status}" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <!-- Previous Page -->
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="${pageContext.request.contextPath}/subunit?page=${page-1}&search=${search}&status=${status}" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <!-- Page Numbers -->
                        <c:choose>
                            <c:when test="${totalPages <= 5}">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == page}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/subunit?page=${i}&search=${search}&status=${status}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${page <= 3}">
                                        <c:forEach begin="1" end="4" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/subunit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span>...</span>
                                        <a href="${pageContext.request.contextPath}/subunit?page=${totalPages}&search=${search}&status=${status}">${totalPages}</a>
                                    </c:when>
                                    <c:when test="${page >= totalPages - 2}">
                                        <a href="${pageContext.request.contextPath}/subunit?page=1&search=${search}&status=${status}">1</a>
                                        <span>...</span>
                                        <c:forEach begin="${totalPages - 3}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/subunit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/subunit?page=1&search=${search}&status=${status}">1</a>
                                        <span>...</span>
                                        <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/subunit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span>...</span>
                                        <a href="${pageContext.request.contextPath}/subunit?page=${totalPages}&search=${search}&status=${status}">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <!-- Next Page -->
                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="${pageContext.request.contextPath}/subunit?page=${page+1}&search=${search}&status=${status}" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>

                        <!-- Last Page -->
                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="${pageContext.request.contextPath}/subunit?page=${totalPages}&search=${search}&status=${status}" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages} (${totalSubUnits} total subunits)
                    </div>
                </c:if>

                <!-- Modals -->
                <div class="modal fade" id="subUnitModal" tabindex="-1" aria-labelledby="subUnitModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="subUnitModalLabel">SubUnit</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="subUnitModalBody">
                                <!-- Dynamic content will be loaded here via AJAX -->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" id="saveSubUnitBtn" style="display:none;">Save</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="subUnitDetailModal" tabindex="-1" aria-labelledby="subUnitDetailModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="subUnitDetailModalLabel">SubUnit Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="subUnitDetailContent">
                                <!-- Dynamic content will be loaded here via AJAX -->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-labelledby="deleteConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteConfirmModalLabel">Confirm Action</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteConfirmMessage">
                                <!-- Dynamic message will be loaded here -->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" class="btn btn-danger" id="confirmDeleteButton">Confirm</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="editConfirmModal" tabindex="-1" aria-labelledby="editConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editConfirmModalLabel">Confirm Status Change</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="editConfirmMessage">
                                Changing status to Inactive will permanently delete associated units. Do you want to continue?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" class="btn btn-primary" id="confirmEditButton">Confirm</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Toast for notifications -->
                <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                    <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="toast-header">
                            <strong class="me-auto">Notification</strong>
                            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                        <div class="toast-body" id="toastMessage"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            const subUnitModal = new bootstrap.Modal(document.getElementById('subUnitModal'));
            const detailModal = new bootstrap.Modal(document.getElementById('subUnitDetailModal'));
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
            const editConfirmModal = new bootstrap.Modal(document.getElementById('editConfirmModal'));
            const toast = new bootstrap.Toast(document.getElementById('liveToast'));

            // Add SubUnit
            $('.btn-add-subunit').click(function() {
                $('#subUnitModalLabel').text('Add SubUnit');
                $('#saveSubUnitBtn').data('action', 'add').show();
                $.ajax({
                    url: '${pageContext.request.contextPath}/subunit',
                    method: 'GET',
                    data: { action: 'add' },
                    dataType: 'json',
                    success: function(data) {
                        if (data.success) {
                            let html = '<form id="subUnitForm">';
                            html += '<div class="mb-3"><label class="form-label">Name</label><input type="text" class="form-control" name="name" required></div>';
                            html += '<div class="mb-3"><label class="form-label">Status</label><select class="form-select" name="status" required><option value="active">Active</option><option value="inactive">Inactive</option></select></div>';
                            html += '</form>';
                            $('#subUnitModalBody').html(html);
                        } else {
                            $('#subUnitModalBody').html('<p class="text-danger">Cannot load add subunit form!</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: Cannot load data. Status: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#subUnitModalBody').html('<p class="text-danger">' + errorMessage + '</p>');
                        console.error('AJAX Error: ', status, error, xhr.responseText);
                    }
                });
                subUnitModal.show();
            });

            // Edit SubUnit
            $('.btn-edit-subunit').click(function() {
                const subUnitId = $(this).data('id');
                $('#subUnitModalLabel').text('Edit SubUnit');
                $('#saveSubUnitBtn').data('action', 'edit').data('id', subUnitId).show();
                $.ajax({
                    url: '${pageContext.request.contextPath}/subunit',
                    method: 'GET',
                    data: { action: 'edit', id: subUnitId },
                    dataType: 'json',
                    success: function(data) {
                        if (data.success && data.subUnit) {
                            let subUnit = data.subUnit;
                            let html = '<form id="subUnitForm">';
                            html += '<input type="hidden" name="id" value="' + subUnit.subUnitId + '">';
                            html += '<div class="mb-3"><label class="form-label">Name</label><input type="text" class="form-control" name="name" value="' + (subUnit.name || '') + '" required></div>';
                            html += '<div class="mb-3"><label class="form-label">Status</label><select class="form-select" name="status" required><option value="active"' + (subUnit.isActive ? ' selected' : '') + '>Active</option><option value="inactive"' + (!subUnit.isActive ? ' selected' : '') + '>Inactive</option></select></div>';
                            html += '</form>';
                            $('#subUnitModalBody').html(html);
                        } else {
                            $('#subUnitModalBody').html('<p class="text-danger">Cannot load edit subunit form!</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: Cannot load data. Status: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#subUnitModalBody').html('<p class="text-danger">' + errorMessage + '</p>');
                        console.error('AJAX Error: ', status, error, xhr.responseText);
                    }
                });
                subUnitModal.show();
            });

            // View SubUnit
            $('.view-detail').click(function() {
                const subUnitId = $(this).data('id');
                $('#subUnitDetailModalLabel').text('SubUnit Details');
                $.ajax({
                    url: '${pageContext.request.contextPath}/subunit',
                    method: 'GET',
                    data: { action: 'view', id: subUnitId },
                    dataType: 'json',
                    success: function(data) {
                        if (data.success) {
                            let html = '<div class="row"><div class="col-md-12"><table class="table detail-table">';
                            html += '<tr><th>Name</th><td>' + (data.name || 'N/A') + '</td></tr>';
                            html += '<tr><th>Status</th><td>' + (data.status || 'N/A') + '</td></tr>';
                            html += '<tr><th>Created At</th><td>' + (data.createdAt || 'N/A') + '</td></tr>';
                            html += '<tr><th>Updated At</th><td>' + (data.updatedAt || 'N/A') + '</td></tr>';
                            html += '</table></div></div>';
                            $('#subUnitDetailContent').html(html);
                        } else {
                            $('#subUnitDetailContent').html('<p class="text-danger">Cannot load subunit details!</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#subUnitDetailContent').html('<p class="text-danger">Error loading data</p>');
                        console.error('AJAX Error:', status, error, xhr.responseText);
                    }
                });
                detailModal.show();
            });

            // Save SubUnit
            $('#saveSubUnitBtn').click(function() {
                const action = $(this).data('action');
                const subUnitId = $(this).data('id');
                const formData = $('#subUnitForm').serialize();
                const url = '${pageContext.request.contextPath}/subunit?action=' + action + (action === 'edit' ? '&id=' + subUnitId : '');

                $.ajax({
                    url: url,
                    method: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function(data) {
                        if (data.success) {
                            $('#toastMessage').text(data.message);
                            toast.show();
                            subUnitModal.hide();
                            setTimeout(() => location.reload(), 1000);
                        } else if (data.requiresConfirmation) {
                            $('#confirmEditButton').data('action', action).data('id', subUnitId).data('formData', formData);
                            editConfirmModal.show();
                        } else {
                            $('#toastMessage').text(data.message || 'Operation failed');
                            toast.show();
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#toastMessage').text('An error occurred: ' + status);
                        toast.show();
                        console.error('AJAX Error: ', status, error, xhr.responseText);
                    }
                });
            });

            // Confirm Edit (Status Change to Inactive)
            $('#confirmEditButton').click(function() {
                const action = $(this).data('action');
                const subUnitId = $(this).data('id');
                const formData = $(this).data('formData');
                const url = '${pageContext.request.contextPath}/subunit?action=' + action + (action === 'edit' ? '&id=' + subUnitId : '') + '&confirmed=true';

                $.ajax({
                    url: url,
                    method: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function(data) {
                        $('#toastMessage').text(data.message);
                        toast.show();
                        if (data.success) {
                            editConfirmModal.hide();
                            subUnitModal.hide();
                            setTimeout(() => location.reload(), 1000);
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#toastMessage').text('An error occurred: ' + status);
                        toast.show();
                        console.error('AJAX Error: ', status, error, xhr.responseText);
                    }
                });
            });

            // Delete SubUnit
            $('.btn-delete').click(function() {
                const subUnitId = $(this).data('id');
                const status = $(this).data('status');
                let message = status === 'active' 
                    ? 'The associated units will be permanently deleted, and the status will change to Inactive. Do you want to continue?'
                    : 'Do you want to permanently delete this subunit and its associated units?';
                $('#deleteConfirmMessage').text(message);
                $('#confirmDeleteButton').data('id', subUnitId).data('action', status === 'active' ? 'delete' : 'permanentDelete');
                deleteModal.show();
            });

            $('#confirmDeleteButton').click(function() {
                const subUnitId = $(this).data('id');
                const action = $(this).data('action');
                $.ajax({
                    url: '${pageContext.request.contextPath}/subunit',
                    method: 'POST',
                    data: { action: action, id: subUnitId },
                    dataType: 'json',
                    success: function(data) {
                        $('#toastMessage').text(data.message);
                        toast.show();
                        if (data.success) {
                            deleteModal.hide();
                            setTimeout(() => location.reload(), 1000);
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'An error occurred: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#toastMessage').text(errorMessage);
                        toast.show();
                        console.error('AJAX Error: ', status, error, xhr.responseText);
                    }
                });
            });

            // Handle filtering and search
            $('#filterForm').on('submit', function(e) {
                e.preventDefault();
                const search = $('input[name="search"]').val();
                const status = $('#statusFilter').val();
                window.location.href = '${pageContext.request.contextPath}/subunit?page=1&search=' + encodeURIComponent(search) + '&status=' + encodeURIComponent(status);
            });

            // Show toast if redirected with success/error
            const params = new URLSearchParams(window.location.search);
            if (params.has('success') || params.has('error')) {
                const isSuccess = params.has('success');
                const msg = params.get(isSuccess ? 'success' : 'error');
                const toastEl = document.getElementById('liveToast');
                toastEl.classList.remove('text-bg-success', 'text-bg-danger');
                toastEl.classList.add(isSuccess ? 'text-bg-success' : 'text-bg-danger');
                document.getElementById('toastMessage').textContent = msg;
                new bootstrap.Toast(toastEl).show();
                history.replaceState(null, '', location.pathname);
            }
        });
    </script>
</body>
</html>