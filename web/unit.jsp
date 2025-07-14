<%@ page import="java.util.List" %>
<%@ page import="model.Unit" %>
<%@ page import="dal.UnitDAO" %>
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
    <title>Unit List</title>
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
            max-width: 1200px;
            margin: 0 auto;
            padding: 24px;
        }

        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 32px;
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
            margin-bottom: 32px;
            gap: 12px;
        }

        .search-container {
            display: flex;
            gap: 12px;
            align-items: center;
            position: relative;
            flex-wrap: wrap;
        }

        .search-input {
            padding: 8px 16px 8px 40px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            width: 400px;
            min-width: 250px;
            background-color: white;
            transition: all 0.2s;
            z-index: 10;
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
            z-index: 11;
        }

        .form-select, .form-control {
            padding: 8px 30px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
            transition: all 0.2s;
            min-width: 150px;
        }

        .form-select:focus, .form-control:focus {
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

        .btn-danger {
            background-color: #ef4444;
            color: white;
        }

        .btn-danger:hover {
            background-color: #dc2626;
        }

        .btn-disabled {
            background-color: #d1d5db;
            color: #6b7280;
            cursor: not-allowed;
        }

        .table-container {
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        table.table {
            width: 100%;
            border-collapse: collapse;
        }

        table.table th {
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

        table.table td {
            padding: 16px;
            border-bottom: 1px solid #f3f4f6;
            vertical-align: middle;
            font-size: 14px;
        }

        table.table tbody tr:nth-child(even) {
            background-color: #f9fafb;
        }

        table.table tbody tr:hover {
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

        .no-data-icon {
            font-size: 48px;
            color: #d1d5db;
            margin-bottom: 16px;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 32px;
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

        .modal .detail-table {
            width: 100%;
            font-size: 13px;
        }

        .modal .detail-table th,
        .modal .detail-table td {
            padding: 8px;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
            }

            .title {
                font-size: 20px;
            }

            .header-actions {
                flex-direction: column;
                gap: 16px;
                align-items: stretch;
            }

            .search-container {
                flex-direction: column;
                gap: 10px;
            }

            .search-input, .form-select, .form-control {
                width: 100%;
                min-width: 200px;
            }

            .action-buttons {
                flex-direction: column;
            }

            table.table {
                min-width: 600px;
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
                <h1 class="title">Unit List</h1>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="${pageContext.request.contextPath}/unit" method="get" id="filterForm" style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" name="search" value="${search}" placeholder="Search by name..." class="search-input form-control" />
                            </div>
                            <select class="form-select" id="statusFilter" name="status">
                                <option value="">All Statuses</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                            <button type="submit" class="btn btn-primary">Filter</button>
                        </form>
                    </div>
                    <div>
                        <c:if test="${perms['Unit_ADD']}">
                            <button class="btn btn-success btn-add-unit" title="Add Unit">
                                <i class="fas fa-plus"></i> Add Unit
                            </button>
                        </c:if>
                    </div>
                </div>

                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th class="col-md-1">#</th>
                                <th class="col-md-4">Name</th>
                                <th class="col-md-3">Status</th>
                                <th class="col-md-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="unit" items="${units}" varStatus="status">
                                <tr>
                                    <td><strong>${status.index + 1 + (currentPage - 1) * 5}</strong></td>
                                    <td>${not empty unit.name ? unit.name : 'N/A'}</td>
                                    <td>
                                        <span class="badge ${unit.status == 'active' ? 'bg-success' : 'bg-danger'}">
                                            ${unit.status == 'active' ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <c:if test="${perms['Unit_VIEW']}">
                                                <button class="btn btn-info btn-sm view-detail" data-id="${unit.unitId}" title="View Details">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${perms['Unit_UPDATE']}">
                                                <button class="btn btn-primary btn-sm btn-edit-unit" data-id="${unit.unitId}" data-has-dependencies="${unit.hasDependencies}" title="Edit" ${unit.hasDependencies ? 'disabled class="btn-disabled"' : ''}>
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${perms['Unit_DELETE']}">
                                                <button class="btn btn-danger btn-sm btn-delete" data-id="${unit.unitId}" data-status="${unit.status}" data-has-dependencies="${unit.hasDependencies}" title="${unit.status == 'active' ? 'Deactivate' : 'Permanent Delete'}" ${unit.hasDependencies ? 'disabled class="btn-disabled"' : ''}>
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

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/unit?page=1&search=${search}&status=${status}" title="First Page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/unit?page=${currentPage-1}&search=${search}&status=${status}" title="Previous Page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="First Page">
                                    <i class="fas fa-angle-double-left"></i>
                                </span>
                                <span class="disabled" title="Previous Page">
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
                                            <a href="${pageContext.request.contextPath}/unit?page=${i}&search=${search}&status=${status}">${i}</a>
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
                                                    <a href="${pageContext.request.contextPath}/unit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                            <a href="${pageContext.request.contextPath}/unit?page=${totalPages}&search=${search}&status=${status}">${totalPages}</a>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${currentPage >= totalPages - 3}">
                                        <a href="${pageContext.request.contextPath}/unit?page=1&search=${search}&status=${status}">1</a>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                        </c:if>
                                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == currentPage}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/unit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/unit?page=1&search=${search}&status=${status}">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                        <c:forEach begin="${currentPage - 2}" end="${currentPage + 2}" var="i">
                                            <c:choose>
                                                <c:when test="${i == currentPage}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/unit?page=${i}&search=${search}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="${pageContext.request.contextPath}/unit?page=${totalPages}&search=${search}&status=${status}">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/unit?page=${currentPage+1}&search=${search}&status=${status}" title="Next Page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/unit?page=${totalPages}&search=${search}&status=${status}" title="Last Page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span class="disabled" title="Next Page">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                                <span class="disabled" title="Last Page">
                                    <i class="fas fa-angle-double-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${currentPage} / ${totalPages}
                        <c:if test="${not empty totalUnits}">
                            (${totalUnits} units)
                        </c:if>
                    </div>
                </c:if>

                <!-- Modal add/edit -->
                <div class="modal fade" id="unitModal" tabindex="-1" aria-labelledby="unitModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-md">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="unitModalLabel">Unit</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="unitModalBody">
                                <!-- Nội dung động sẽ được tải qua AJAX -->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" id="saveUnitBtn" style="display:none;">Save</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal confirm xóa -->
                <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-labelledby="deleteConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteConfirmModalLabel">Confirm Action</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteConfirmMessage">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" class="btn btn-danger" id="confirmDeleteButton">Confirm</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Toast thông báo -->
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
            const unitModal = new bootstrap.Modal(document.getElementById('unitModal'));
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
            const toast = new bootstrap.Toast(document.getElementById('liveToast'));

            // add
            $('.btn-add-unit').click(function() {
                $('#unitModalLabel').text('Add Unit');
                $('#saveUnitBtn').data('action', 'add').show();
                $.ajax({
                    url: '${pageContext.request.contextPath}/unit?action=add',
                    method: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        if (data.success) {
                            let html = '<form id="unitForm">';
                            html += '<div class="mb-3"><label class="form-label">Name</label><input type="text" class="form-control" name="name" required></div>';
                            html += '<div class="mb-3"><label class="form-label">Status</label><select class="form-select" name="status" required><option value="active">Active</option><option value="inactive">Inactive</option></select></div>';
                            html += '</form>';
                            $('#unitModalBody').html(html);
                        } else {
                            $('#unitModalBody').html('<p class="text-danger">Cannot load add unit form!</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: Unable to load data. Status: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#unitModalBody').html('<p class="text-danger">' + errorMessage + '</p>');
                        console.error('Lỗi AJAX: ', status, error, xhr.responseText);
                    }
                });
                unitModal.show();
            });

            // edit
            $('.btn-edit-unit').click(function() {
                const hasDependencies = $(this).data('has-dependencies') === true;
                if (hasDependencies) {
                    $('#toastMessage').text('Cannot edit unit: It is used in transactions');
                    toast.show();
                    return;
                }
                const unitId = $(this).data('id');
                $('#unitModalLabel').text('Edit Unit');
                $('#saveUnitBtn').data('action', 'edit').data('id', unitId).show();
                $.ajax({
                    url: '${pageContext.request.contextPath}/unit?action=edit&id=' + unitId,
                    method: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        if (data.success && data.unit) {
                            let unit = data.unit;
                            let html = '<form id="unitForm">';
                            html += '<input type="hidden" name="id" value="' + unit.unitId + '">';
                            html += '<div class="mb-3"><label class="form-label">Name</label><input type="text" class="form-control" name="name" value="' + (unit.name || '') + '" required></div>';
                            html += '<div class="mb-3"><label class="form-label">Status</label><select class="form-select" name="status" required><option value="active"' + (unit.status == 'active' ? ' selected' : '') + '>Active</option><option value="inactive"' + (unit.status == 'inactive' ? ' selected' : '') + '>Inactive</option></select></div>';
                            html += '</form>';
                            $('#unitModalBody').html(html);
                        } else {
                            $('#unitModalBody').html('<p class="text-danger">' + (data.message || 'Cannot load edit unit form!') + '</p>');
                            $('#toastMessage').text(data.message || 'Cannot load edit unit form!');
                            toast.show();
                            unitModal.hide();
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: Unable to load data. Status: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#unitModalBody').html('<p class="text-danger">' + errorMessage + '</p>');
                        $('#toastMessage').text(errorMessage);
                        toast.show();
                        unitModal.hide();
                        console.error('Lỗi AJAX: ', status, error, xhr.responseText);
                    }
                });
                unitModal.show();
            });

            // view
            $('.view-detail').click(function() {
                const unitId = $(this).data('id');
                $('#unitModalLabel').text('View Unit Details');
                $('#saveUnitBtn').hide();
                $.ajax({
                    url: '${pageContext.request.contextPath}/unit?action=view&id=' + unitId,
                    method: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        if (data.success && data.unit) {
                            let unit = data.unit;
                            let html = '<div class="row"><div class="col-md-12"><table class="table detail-table">';
                            html += '<tr><th>Name</th><td>' + (unit.name || 'N/A') + '</td></tr>';
                            html += '<tr><th>Status</th><td>' + (unit.status == 'active' ? 'Active' : 'Inactive') + '</td></tr>';
                            html += '<tr><th>Created At</th><td>' + (unit.createdAt || 'N/A') + '</td></tr>';
                            html += '<tr><th>Updated At</th><td>' + (unit.updatedAt || 'N/A') + '</td></tr>';
                            html += '</table></div></div>';
                            $('#unitModalBody').html(html);
                        } else {
                            $('#unitModalBody').html('<p class="text-danger">Cannot load unit details!</p>');
                        }
                    },
                    error: function(xhr, status, error) {
                        $('#unitModalBody').html('<p class="text-danger">Error loading data</p>');
                        console.error('Lỗi AJAX: ', status, error, xhr.responseText);
                    }
                });
                unitModal.show();
            });

            // Lưu 
            $('#saveUnitBtn').click(function() {
                const action = $(this).data('action');
                const unitId = $(this).data('id');
                const url = '${pageContext.request.contextPath}/unit?action=' + action + (action === 'edit' ? '&id=' + unitId : '');
                const formData = $('#unitForm').serialize();
                $.ajax({
                    url: url,
                    method: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function(data) {
                        $('#toastMessage').text(data.message);
                        toast.show();
                        if (data.success) {
                            unitModal.hide();
                            setTimeout(() => location.reload(), 1000);
                        } else {
                            unitModal.hide();
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#toastMessage').text(errorMessage);
                        toast.show();
                        unitModal.hide();
                        console.error('Lỗi AJAX: ', status, error, xhr.responseText);
                    }
                });
            });

            // Xóa 
            $('.btn-delete').click(function() {
                const hasDependencies = $(this).data('has-dependencies') === true;
                if (hasDependencies) {
                    $('#toastMessage').text('Cannot delete unit: It is used in transactions');
                    toast.show();
                    return;
                }
                const unitId = $(this).data('id');
                const status = $(this).data('status');
                let message = status === 'active' 
                    ? 'The unit\'s status will be changed to Inactive. Do you want to continue?' 
                    : 'Do you want to permanently delete this unit?';
                $('#deleteConfirmMessage').text(message);
                $('#confirmDeleteButton').data('id', unitId).data('action', status === 'active' ? 'deactivate' : 'permanentDelete');
                deleteModal.show();
            });

            // Xử lý xác nhận xóa
            $('#confirmDeleteButton').click(function() {
                const unitId = $(this).data('id');
                const action = $(this).data('action');
                $.ajax({
                    url: '${pageContext.request.contextPath}/unit',
                    method: 'POST',
                    data: { action: action, id: unitId },
                    dataType: 'json',
                    success: function(data) {
                        $('#toastMessage').text(data.message);
                        toast.show();
                        if (data.success) {
                            deleteModal.hide();
                            setTimeout(() => location.reload(), 1000);
                        } else {
                            deleteModal.hide();
                        }
                    },
                    error: function(xhr, status, error) {
                        let errorMessage = 'Error: ' + status;
                        if (xhr.responseText) errorMessage += '. Response: ' + xhr.responseText;
                        $('#toastMessage').text(errorMessage);
                        toast.show();
                        deleteModal.hide();
                        console.error('Lỗi AJAX: ', status, error, xhr.responseText);
                    }
                });
            });

            // Xử lý lọc và tìm kiếm với AJAX
            $('#filterForm').on('submit', function(e) {
                e.preventDefault();
                var search = $('input[name="search"]').val();
                var status = $('#statusFilter').val();
                $.get('${pageContext.request.contextPath}/unit', {
                    search: search,
                    status: status,
                    page: 1
                }, function(data) {
                    $('table tbody').html($(data).find('table tbody').html());
                    $('.pagination').html($(data).find('.pagination').html());
                    $('div[style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;"]').html($(data).find('div[style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;"]').html());
                });
            });

            // Hiển thị toast nếu chuyển hướng với thông báo thành công/lỗi
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