<%@ page import="java.util.List" %>
<%@ page import="model.Import_note" %>
<%@ page import="model.Users" %>
<%@ page import="model.Warehouse" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Import Notes List</title>
    <!-- Bootstrap CSS (v5.3) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <!-- Menu CSS -->
    <link rel="stylesheet" href="css/metisMenu.css" />
    <!-- Style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
    <!-- Custom CSS -->
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
                <h1 class="title">Import Notes List</h1>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        ${errorMessage}
                    </div>
                </c:if>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="import-note-list" method="get" id="filterForm">
                            <div style="display: flex; gap: 12px; align-items: center;">
                                <div style="position: relative;">
                                    <i class="fas fa-search search-icon"></i>
                                    <input type="text" name="search" value="${search}" placeholder="Search by username..." class="search-input" />
                                </div>
                                <select name="imported" class="form-select">
                                    <option value="">All Status</option>
                                    <option value="true" ${param.imported == 'true' ? 'selected' : ''}>Imported</option>
                                    <option value="false" ${param.imported == 'false' ? 'selected' : ''}>Not Imported</option>
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
                                <th class="col-md-2" style="display: none;">Order ID</th>
                                <th class="col-md-2">User Name</th>
                                <th class="col-md-2">Warehouse</th>
                                <th class="col-md-2">Created At</th>
                                <th class="col-md-2">Imported</th>
                                <th class="col-md-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="importNote" items="${importNotes}" varStatus="loop">
                                <c:set var="user" value="${users[loop.index]}" />
                                <c:set var="warehouse" value="${warehouses[loop.index]}" />
                                <tr>
                                    <td><strong>${(page-1)*5 + loop.index + 1}</strong></td>
                                    <td style="display: none;">${importNote.orderId}</td>
                                    <td>${user != null ? user.fullName : 'N/A'}</td>
                                    <td>${warehouse != null ? warehouse.name : 'N/A'}</td>
                                    <td>${importNote.createdAt}</td>
                                    <td>
                                        <span class="badge ${importNote.imported ? 'bg-success' : 'bg-danger'}">
                                            ${importNote.imported ? 'Yes' : 'No'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <c:if test="${importNote.imported}">
                                                <button class="btn btn-info btn-sm view-detail" data-id="${importNote.importNoteId}" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${!importNote.imported}">
                                                <button class="btn btn-success btn-sm add-to-inventory" data-id="${importNote.importNoteId}" title="Add to Inventory">
                                                    <i class="fas fa-plus"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty importNotes}">
                                <tr>
                                    <td colspan="7" class="no-data">No import notes found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <%--<c:if test="${totalPages > 1}">--%>
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="import-note-list?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="import-note-list?page=${page-1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </span>
                                <span title="Previous page">
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
                                            <a href="import-note-list?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a>
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
                                                    <a href="import-note-list?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                            <a href="import-note-list?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${totalPages}</a>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${page >= totalPages - 3}">
                                        <a href="import-note-list?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">1</a>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                        </c:if>
                                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="import-note-list?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="import-note-list?page=1<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                        <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="import-note-list?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="import-note-list?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="import-note-list?page=${page+1}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="import-note-list?page=${totalPages}<c:if test='${not empty search}'>&search=${search}</c:if><c:if test='${param.imported != null}'>&imported=${param.imported}</c:if><c:if test='${param.sortOrder != null}'>&sortOrder=${param.sortOrder}</c:if>" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                                <span title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages}
                        <c:if test="${not empty importNotes}">
                            (${totalNotes} total import notes)
                        </c:if>
                    </div>
                <%--</c:if>--%>

                <!-- Import Note Detail Modal -->
                <div class="modal fade" id="importNoteModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Import Note Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="importNoteContent"><p>Loading...</p></div>
                            <div class="modal-footer">
                                <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Add to Inventory Modal -->
                <div class="modal fade" id="inventoryModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Add to Inventory</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="inventoryContent"><p>Loading...</p></div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- JS libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(function() {
            // View details
            $('.view-detail').on('click', function() {
                var id = $(this).data('id');
                $('#importNoteContent').load('getImportNote.jsp?importNoteId=' + id);
                new bootstrap.Modal(document.getElementById('importNoteModal')).show();
            });

            // Add to inventory
            $('.add-to-inventory').on('click', function() {
                var id = $(this).data('id');
                $('#inventoryContent').load('importNoteToInventory.jsp?importNoteId=' + id);
                new bootstrap.Modal(document.getElementById('inventoryModal')).show();
            });

            // Checkbox handling inside inventory modal
            $('#inventoryModal').on('click', '.checkbox-all', function() {
                $('.checkbox-item').prop('checked', this.checked);
            });
            $('#inventoryModal').on('click', '.checkbox-item', function() {
                $('.checkbox-all').prop('checked', $('.checkbox-item:checked').length === $('.checkbox-item').length);
            });

            // Execute add to inventory
            $('#addButton').on('click', function() {
                var importNoteId = $('#inventoryModal').find('input[name="importNoteId"]').val();
                var detailIds = [];
                $('.checkbox-item:checked').each(function() {
                    detailIds.push($(this).val());
                });
                $.post('import-note-to-inventory', {
                    importNoteId: importNoteId,
                    detailIds: detailIds
                }, function(resp) {
                    if (resp.success) {
                        location.reload();
                    } else {
                        alert('Error: ' + resp.message);
                    }
                }, 'json');
            });
        });
    </script>
</body>
</html>