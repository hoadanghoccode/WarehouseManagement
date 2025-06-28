<%@ page import="java.util.List" %>
<%@ page import="model.ExportNote" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exported Notes List</title>
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
            justify-content: flex-start;
            align-items: center;
            margin-bottom: 32px;
        }

        .search-container {
            display: flex;
            gap: 12px;
            align-items: center;
            flex-wrap: nowrap;
        }

        .search-input {
            padding: 8px 16px 8px 40px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            width: 300px;
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

        .search-input-container {
            position: relative;
        }

        .form-select, .form-control {
            padding: 8px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
            transition: all 0.2s;
            width: 150px;
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

        .btn-info {
            background-color: #06b6d4;
            color: white;
        }

        .btn-info:hover {
            background-color: #0891b2;
        }

        .btn-secondary {
            background-color: #6b7280;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #4b5563;
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
            min-width: 600px;
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

        .modal .modal-dialog {
            max-width: 800px;
        }

        .modal .modal-body {
            max-height: 70vh;
            overflow-y: auto;
        }

        .modal .modal-image {
            width: 200px;
            height: auto;
            float: left;
            margin-right: 20px;
        }

        .modal .detail-table {
            float: right;
            width: calc(100% - 220px);
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

        /* Confirmation Modal Styling */
        .confirm-modal {
            position: fixed;
            bottom: 20px;
            right: 20px;
            max-width: 400px;
            width: 90%;
            z-index: 1060; /* Higher than other modals (1050) */
            transition: opacity 0.3s ease-in-out, transform 0.3s ease-in-out;
        }
        .confirm-modal .modal-dialog {
    position: fixed;
    bottom: 20px;
    right: 20px;
    margin: 0;
    transform: none !important;
    pointer-events: auto;
    width: 100%;
    max-width: 400px;
}


        .confirm-modal.fade .modal-dialog {
            transform: translateY(50px);
            opacity: 0;
        }

        .confirm-modal.show .modal-dialog {
            transform: translateY(0);
            opacity: 1;
        }

        .confirm-modal .modal-content {
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .confirm-modal .modal-header {
            background-color: #f8fafc;
            border-bottom: 1px solid #e5e7eb;
        }

        .confirm-modal .modal-body {
            font-size: 14px;
            color: #334155;
        }

        .confirm-modal .modal-footer {
            border-top: 1px solid #e5e7eb;
            padding: 8px;
            text-align: right;
        }

        .confirm-modal .modal-footer .btn {
            padding: 6px 12px;
            font-size: 12px;
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
                flex-wrap: wrap;
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

            .confirm-modal {
                right: 10px;
                bottom: 10px;
                width: 95%;
            }
        }

        @media (min-width: 769px) {
            .search-container {
                max-width: 800px;
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
                <h1 class="title">Exported Notes List</h1>

                <div class="stats-info">
                    <i class="fas fa-info-circle"></i>
                    <span>Displaying <strong>${totalNotes}</strong> exported notes</span>
                </div>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="/WarehouseManagement/exportnotelist" method="get" id="filterForm">
                            <div style="display: flex; gap: 12px; align-items: center; flex-wrap: nowrap;">
                                <div class="search-input-container">
                                    <i class="fas fa-search search-icon"></i>
                                    <input class="search-input" type="text" name="search" value="${search}" placeholder="Search by order ID or user..." class="search-input form-control" />
                                </div>
                                <select name="exported" class="form-select">
                                    <option value="" ${empty param.exported ? 'selected' : ''}>All</option>
                                    <option value="true" ${param.exported == 'true' ? 'selected' : ''}>Exported</option>
                                    <option value="false" ${param.exported == 'false' ? 'selected' : ''}>Not Exported</option>
                                </select>
                                <select name="sortOrder" class="form-select">
                                    <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Oldest First</option>
                                    <option value="desc" ${param.sortOrder == 'desc' ? 'selected' : ''}>Newest First</option>
                                </select>
                                <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th class="col-md-1">#</th>
                                <th class="col-md-2">Order ID</th>
                                <th class="col-md-2">User Name</th>
                                <th class="col-md-2">Warehouse</th>
                                <th class="col-md-2">Created At</th>
                                <th class="col-md-2">Exported</th>
                                <th class="col-md-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="exportNote" items="${exportNotes}" varStatus="loop">
                                <tr>
                                    <td><strong>${(page-1)*5 + loop.index + 1}</strong></td>
                                    <td>${exportNote.orderId}</td>
                                    <td>${exportNote.userName != null ? exportNote.userName : 'N/A'}</td>
                                    <td>${exportNote.warehouseName != null ? exportNote.warehouseName : 'N/A'}</td>
                                    <td><fmt:formatDate value="${exportNote.createdAt}" pattern="dd/MM/yyyy" /></td>
                                    <td>
                                        <span class="badge ${exportNote.exported ? 'bg-success' : 'bg-danger'}">
                                            ${exportNote.exported ? 'Yes' : 'No'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <button class="btn btn-info btn-sm view-detail" data-id="${exportNote.exportNoteId}" title="View">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <c:if test="${not exportNote.exported}">
                                                <button class="btn btn-success btn-sm export-from-inventory" data-id="${exportNote.exportNoteId}" title="Export">
                                                    <i class="fas fa-file-export"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty exportNotes}">
                                <tr>
                                    <td colspan="7" class="no-data">
                                        <i class="fas fa-exclamation-circle no-data-icon"></i><br>
                                        No exported notes found.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <div class="pagination">
                    <c:choose>
                        <c:when test="${page > 1}">
                            <a href="/WarehouseManagement/exportnotelist?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="First page">
                                <i class="fas fa-angle-double-left"></i>
                            </a>
                            <a href="/WarehouseManagement/exportnotelist?page=${page-1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Previous page">
                                <i class="fas fa-angle-left"></i>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled" title="First page"><i class="fas fa-angle-double-left"></i></span>
                            <span class="disabled" title="Previous page"><i class="fas fa-angle-left"></i></span>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${totalPages <= 7}">
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                    <c:otherwise><a href="/WarehouseManagement/exportnotelist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a></c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${page <= 4}">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                            <c:otherwise><a href="/WarehouseManagement/exportnotelist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a></c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <c:if test="${totalPages > 6}"><span style="padding: 8px 4px;">...</span><a href="/WarehouseManagement/exportnotelist?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${totalPages}</a></c:if>
                                </c:when>
                                <c:when test="${page >= totalPages - 3}">
                                    <a href="/WarehouseManagement/exportnotelist?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">1</a>
                                    <c:if test="${totalPages > 6}"><span style="padding: 8px 4px;">...</span></c:if>
                                    <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                            <c:otherwise><a href="/WarehouseManagement/exportnotelist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a></c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <a href="/WarehouseManagement/exportnotelist?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">1</a>
                                    <span style="padding: 8px 4px;">...</span>
                                    <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                            <c:otherwise><a href="/WarehouseManagement/exportnotelist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a></c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <span style="padding: 8px 4px;">...</span>
                                    <a href="/WarehouseManagement/exportnotelist?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${totalPages}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${page < totalPages}">
                            <a href="/WarehouseManagement/exportnotelist?page=${page+1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Next page">
                                <i class="fas fa-angle-right"></i>
                            </a>
                            <a href="/WarehouseManagement/exportnotelist?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${not empty param.exported}'>&exported=${param.exported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Last page">
                                <i class="fas fa-angle-double-right"></i>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled" title="Next page"><i class="fas fa-angle-right"></i></span>
                            <span class="disabled" title="Last page"><i class="fas fa-angle-double-right"></i></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                    Page ${page} of ${totalPages}
                    <c:if test="${not empty exportNotes}"> (${totalNotes} total exported notes)</c:if>
                </div>

                <!-- Modal for Viewing Export Note Details -->
                <div class="modal fade" id="exportNoteModal" tabindex="-1" aria-labelledby="exportNoteModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exportNoteModalLabel">Export Note Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="exportNoteContent"><p>Loading...</p></div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal for Exporting from Inventory -->
                <div class="modal fade" id="inventoryModal" tabindex="-1" aria-labelledby="inventoryModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="inventoryModalLabel">Export from Inventory</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="inventoryContent"><p>Loading...</p></div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" id="exportButton">Export from Inventory</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Confirmation Modal -->
                <div class="modal fade confirm-modal" id="confirmExportModal" tabindex="-1"
     aria-labelledby="confirmExportModalLabel"
     aria-hidden="true"
     data-bs-backdrop="false"
     data-bs-keyboard="false">

                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="confirmExportModalLabel">Notification</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="confirmExportMessage"></div>
                            <div class="modal-footer" id="confirmExportFooter">
                                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" class="btn btn-primary btn-sm" id="confirmExportBtn">Confirm</button>
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
        $(document).ready(function () {
            // Initialize modals
            const exportNoteModal = new bootstrap.Modal(document.getElementById('exportNoteModal'));
            const inventoryModal = new bootstrap.Modal(document.getElementById('inventoryModal'));
            const confirmExportModal = new bootstrap.Modal(document.getElementById('confirmExportModal'), {
                backdrop: false // Allows interaction with underlying modals
            });

            // View details
            $('.view-detail').on('click', function () {
                var id = $(this).data('id');
                $.ajax({
                    url: '/WarehouseManagement/getExportNote.jsp?exportNoteId=' + id,
                    type: 'GET',
                    success: function (response) {
                        $('#exportNoteContent').html(response);
                        exportNoteModal.show();
                    },
                    error: function (xhr) {
                        $('#confirmExportMessage').text('Error loading details: ' + xhr.statusText);
                        $('#confirmExportFooter').hide();
                        confirmExportModal.show();
                    }
                });
            });

            // Export from inventory
            $('.export-from-inventory').on('click', function () {
                var id = $(this).data('id');
                $.ajax({
                    url: '/WarehouseManagement/exportNoteToInventory.jsp?exportNoteId=' + id,
                    type: 'GET',
                    success: function (response) {
                        $('#inventoryContent').html(response);
                        inventoryModal.show();
                    },
                    error: function (xhr) {
                        $('#confirmExportMessage').text('Error loading inventory details: ' + xhr.statusText);
                        $('#confirmExportFooter').hide();
                        confirmExportModal.show();
                    }
                });
            });

            $('#inventoryModal').on('click', '.checkbox-all', function () {
                $('.checkbox-item').prop('checked', this.checked);
            });

            $('#inventoryModal').on('click', '.checkbox-item', function () {
                $('.checkbox-all').prop('checked', $('.checkbox-item:checked').length === $('.checkbox-item').length);
            });

            $('#exportButton').on('click', function () {
    const exportNoteId = $('#inventoryContent').find('input[name="exportNoteId"]').val();

    if (!exportNoteId) {
        $('#confirmExportMessage').text('Export Note ID not found!');
        $('#confirmExportFooter').hide();
        confirmExportModal.show();
        return;
    }

    const selectedItems = [];
    $('#inventoryContent .checkbox-item:checked').each(function () {
        const $this = $(this);
        selectedItems.push({
            detailId: $this.val(),
            materialId: $this.data('material-id'),
            subUnitId: $this.data('subunit-id'),
            qualityId: $this.data('quality-id'),
            quantity: $this.data('requested-quantity')
        });
    });

    if (selectedItems.length === 0) {
        $('#confirmExportMessage').text('Please select at least one item to export.');
        $('#confirmExportFooter').hide();
        confirmExportModal.show();
        return;
    }

    $('#confirmExportMessage').text('Are you sure you want to export the selected items from inventory?');
    $('#confirmExportFooter').show();

    // GÁN DỮ LIỆU VÀO NÚT XÁC NHẬN
    $('#confirmExportBtn')
        .data('export-note-id', exportNoteId)
        .data('selected-items', selectedItems);

    confirmExportModal.show();
});


            $('#confirmExportBtn').on('click', function () {
                const exportNoteId = $(this).data('export-note-id');
                const selectedItems = $(this).data('selected-items');

                const detailIds = selectedItems.map(i => i.detailId);
                const quantities = selectedItems.map(i => i.quantity);
                const materialIds = selectedItems.map(i => i.materialId);
                const subUnitIds = selectedItems.map(i => i.subUnitId);
                const qualityIds = selectedItems.map(i => i.qualityId);

                $.ajax({
                    url: '/WarehouseManagement/exportnotelist',
                    type: 'POST',
                    traditional: true,
                    data: {
                        action: 'export',
                        exportNoteId: exportNoteId,
                        detailIds: detailIds,
                        quantities: quantities,
                        materialIds: materialIds,
                        subUnitIds: subUnitIds,
                        qualityIds: qualityIds
                    },
                    dataType: 'json',
                    success: function (res) {
                        $('#confirmExportMessage').text(res.message);
                        $('#confirmExportFooter').hide();
                        confirmExportModal.show();
                        if (res.success) {
                            setTimeout(() => {
                                confirmExportModal.hide();
                                inventoryModal.hide();
                                location.reload();
                            }, 1000);
                        }
                    },
                    error: function (xhr) {
                        $('#confirmExportMessage').text('Error: ' + xhr.responseText);
                        $('#confirmExportFooter').hide();
                        confirmExportModal.show();
                    }
                });
            });
        });
    </script>
</body>
</html>