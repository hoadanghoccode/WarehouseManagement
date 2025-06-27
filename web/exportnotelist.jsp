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
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" href="css/metisMenu.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .main-layout {
            display: flex;
            min-height: 100vh;
        }
        .main-content {
            flex: 1;
            margin-left: 260px;
            padding: 0;
            background-color: #f8f9fa;
            min-height: 100vh;
            transition: margin-left 0.3s ease;
        }
        .container {
            max-width: none;
            padding: 30px;
            margin: 0;
        }
        .title {
            font-size: 28px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
        }
        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }
        .search-container form {
            display: flex;
            gap: 12px;
            align-items: center;
        }
        .search-container .search-icon {
            position: absolute;
            top: 50%;
            left: 12px;
            transform: translateY(-50%);
            color: #6b7280;
        }
        .search-container .search-input {
            padding-left: 36px;
            height: 38px;
            border-radius: 6px;
            border: 1px solid #d1d5db;
        }
        .table-container {
            overflow-x: auto;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
        }
        table.table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
        }
        table.table th, table.table td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
            font-size: 14px;
            vertical-align: middle;
        }
        table.table th {
            background-color: #f3f4f6;
            font-weight: 600;
            color: #1f2937;
            position: sticky;
            top: 0;
            z-index: 2;
        }
        table.table tbody tr:nth-child(even) {
            background-color: #f9fafb;
        }
        table.table tbody tr:hover {
            background-color: #eef2ff;
        }
        .action-buttons {
            display: flex;
            gap: 8px;
        }
        .no-data {
            text-align: center;
            padding: 24px;
            background-color: #f3f4f6;
            border-radius: 12px;
            font-size: 16px;
            color: #6b7280;
        }
        .pagination {
            display: flex;
            gap: 8px;
            justify-content: center;
            margin-top: 16px;
        }
        .pagination a, .pagination span {
            padding: 6px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            color: #333;
            text-decoration: none;
        }
        .pagination .current {
            background: #3b82f6;
            color: #fff;
            border-color: #3b82f6;
        }
        .pagination span:not(.current) {
            opacity: 0.3;
            cursor: not-allowed;
        }
        .modal-dialog {
            max-width: 800px;
        }
        .modal-body {
            max-height: 70vh;
            overflow-y: auto;
        }
        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
            }
            .title {
                font-size: 24px;
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
                <h1 class="title">Exported Notes List</h1>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="/WarehouseManagement/exportnotelist" method="get" id="filterForm">
                            <div style="display: flex; gap: 12px; align-items: center;">
                                <div style="position: relative;">
                                    <i class="fas fa-search search-icon"></i>
                                    <input type="text" name="search" value="${search}" placeholder="Search..." class="search-input" />
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
                                <button type="submit" class="btn btn-primary" style="padding: 6px 12px;">Filter</button>
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
                                    <td>${exportNote.exported ? 'Yes' : 'No'}</td>
                                    <td>
                                        <div class="action-buttons">
                                            <button class="btn btn-info btn-sm view-detail" data-id="${exportNote.exportNoteId}" title="View">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <c:if test="${not exportNote.exported}">
                                                <button class="btn btn-success btn-sm export-from-inventory" data-id="${exportNote.exportNoteId}" title="Export from Inventory">
                                                    <i class="fas fa-shopping-cart"></i>
                                                </button>
                                            </c:if>
                                            <%-- Hiển thị nút View Backorder dựa trên hasBackOrder --%>
                                            <c:if test="${exportNote.hasBackOrder}">
                                                <a href="/WarehouseManagement/backorder?exportNoteId=${exportNote.exportNoteId}" class="btn btn-warning btn-sm" title="View Backorder">
                                                    <i class="fas fa-box"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty exportNotes}">
                                <tr>
                                    <td colspan="7" class="no-data">No exported notes found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPages > 1}">
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
                                <span title="First page"><i class="fas fa-angle-double-left"></i></span>
                                <span title="Previous page"><i class="fas fa-angle-left"></i></span>
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
                                <span title="Next page"><i class="fas fa-angle-right"></i></span>
                                <span title="Last page"><i class="fas fa-angle-double-right"></i></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages}
                        <c:if test="${not empty exportNotes}"> (${totalNotes} total exported notes)</c:if>
                    </div>
                </c:if>

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
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function () {
            $('.view-detail').on('click', function () {
                var id = $(this).data('id');
                $('#exportNoteContent').load('/WarehouseManagement/getExportNote.jsp?exportNoteId=' + id, function (response, status, xhr) {
                    if (status === "error") {
                        $('#exportNoteContent').html('<p>Error loading details.</p>');
                    } else {
                        new bootstrap.Modal(document.getElementById('exportNoteModal')).show();
                    }
                });
            });

            $('.export-from-inventory').on('click', function () {
                var id = $(this).data('id');
                $('#inventoryContent').load('/WarehouseManagement/exportNoteToInventory.jsp?exportNoteId=' + id, function (response, status, xhr) {
                    if (status === "error") {
                        $('#inventoryContent').html('<p>Error loading inventory details.</p>');
                    } else {
                        new bootstrap.Modal(document.getElementById('inventoryModal')).show();
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
                    alert('Export Note ID not found!');
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
                    $('#inventoryWarning').text('Please select at least one item to export.').show();
                    setTimeout(() => $('#inventoryWarning').hide(), 4000);
                    return;
                }

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
                        exportNoteId,
                        detailIds,
                        quantities,
                        materialIds,
                        subUnitIds,
                        qualityIds
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.success) {
                            alert('Export successful! ' + (res.backOrderMessage || ''));
                            $('#inventoryModal').modal('hide');
                            location.reload();
                        } else {
                            $('#inventoryWarning').text(res.message).show();
                            setTimeout(() => $('#inventoryWarning').hide(), 4000);
                        }
                    },
                    error: function (xhr) {
                        alert('Error: ' + xhr.responseText);
                    }
                });
            });
        });
    </script>
</body>
</html>