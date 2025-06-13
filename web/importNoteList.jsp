<%@ page import="java.util.List" %>
<%@ page import="model.Import_note" %>
<%@ page import="dal.Import_noteDAO" %>
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
                <h1 class="title">Import Notes List</h1>

                <div class="header-actions">
                    <div class="search-container">
                        <form action="import-note-list" method="get" id="filterForm" style="display: flex; gap: 12px; align-items: center;">
                            <select name="imported" class="form-select">
                                <option value="">All</option>
                                <option value="true" ${param.imported == 'true' ? 'selected' : ''}>Imported</option>
                                <option value="false" ${param.imported == 'false' ? 'selected' : ''}>Not Imported</option>
                            </select>
                            <select name="sortOrder" class="form-select">
                                <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Oldest First</option>
                                <option value="desc" ${param.sortOrder == 'desc' ? 'selected' : ''}>Newest First</option>
                            </select>
                            <button type="submit" class="btn btn-primary" style="padding: 6px 12px;">Filter</button>
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
                                <th class="col-md-2">Warehouse Name</th>
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
                                    <td><strong>${loop.index + 1}</strong></td>
                                    <td>${importNote.orderId}</td>
                                    <td>${user != null ? user.fullName : 'N/A'}</td>
                                    <td>${warehouse != null ? warehouse.name : 'N/A'}</td>
                                    <td>${importNote.createdAt}</td>
                                    <td>${importNote.imported ? 'Yes' : 'No'}</td>
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
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="importNoteModal" tabindex="-1" aria-labelledby="importNoteModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="importNoteModalLabel">Import Note Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="importNoteContent">
                    <p>Loading...</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="inventoryModal" tabindex="-1" aria-labelledby="inventoryModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="inventoryModalLabel">Add to Inventory</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="inventoryContent">
                    <p>Loading...</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="addButton">Add</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            $('.view-detail').on('click', function(e) {
                e.preventDefault();
                var importNoteId = $(this).data('id');
                $('#importNoteContent').html('<p>Loading...</p>');
                $('#importNoteContent').load('getImportNote.jsp?importNoteId=' + importNoteId);
                new bootstrap.Modal(document.getElementById('importNoteModal')).show();
            });

            $('.add-to-inventory').on('click', function(e) {
                e.preventDefault();
                var importNoteId = $(this).data('id');
                $('#inventoryContent').html('<p>Loading...</p>');
                $('#inventoryContent').load('importNoteToInventory.jsp?importNoteId=' + importNoteId);
                new bootstrap.Modal(document.getElementById('inventoryModal')).show();
            });

            $('#inventoryModal').on('click', '.checkbox-all', function() {
                $('.checkbox-item').prop('checked', this.checked);
            });

            $('#inventoryModal').on('click', '.checkbox-item', function() {
                if ($('.checkbox-item:checked').length === $('.checkbox-item').length) {
                    $('.checkbox-all').prop('checked', true);
                } else {
                    $('.checkbox-all').prop('checked', false);
                }
            });

            $('#addButton').on('click', function() {
                alert('Add to inventory functionality to be implemented.');
            });
        });
    </script>
</body>
</html>