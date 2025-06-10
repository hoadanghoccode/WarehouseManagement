<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Unit List</title>
    <link rel="stylesheet" type="text/css" href="css/permissionlist.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" href="img/logo.png" type="image/png">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
    <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
    <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
    <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
    <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
    <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
    <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
    <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
    <link rel="stylesheet" href="vendors/morris/morris.css" />
    <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
    <link rel="stylesheet" href="css/metisMenu.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />

    <style>
        * { box-sizing: border-box; }
        body { font-family: "Segoe UI", Tahoma, sans-serif; background-color: #f3f4f6; margin: 0; padding: 0; color: #374151; }
        .container { padding-top: 24px; padding-bottom: 24px; max-width: 1200px; margin: 0 auto; }
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
        .modal-content { background-color: white; margin: 6% auto; padding: 24px 32px; border-radius: 4px; width: 90%; max-width: 480px; position: relative; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); font-size: 14px; }
        .modal .close { position: absolute; top: 12px; right: 18px; font-size: 24px; cursor: pointer; color: #9ca3af; transition: color 0.2s; }
        .modal .close:hover { color: #374151; }
        .detail-item { margin-bottom: 14px; display: flex; gap: 8px; }
        .detail-item strong { color: #1f2937; width: 110px; }
        .form-group { margin-bottom: 16px; }
        .error { color: red; font-size: 14px; }
        @media (max-width: 768px) { .title { font-size: 24px; } table.table { min-width: 600px; } .modal-content { padding: 20px; } }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true"/>
    <section class="main_content dashboard_part">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <h1 class="title">Unit List</h1>
            <div class="row g-2 mb-3 align-items-center">
                <div class="col-md-4">
                    <input type="text" id="searchInput" class="form-control" placeholder="Search by name..." oninput="filterUnits()">
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="activeFilter" onchange="filterUnits()">
                        <option value="">All Status</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-success w-100" data-bs-toggle="modal" data-bs-target="#editModal"><i class="fas fa-plus"></i> Add Unit</button>
                </div>
            </div>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger">${errorMsg}</div>
            </c:if>
            <div class="table-container mb-4">
                <table class="table" id="unitsTable">
                    <thead>
                        <tr>
                            <th style="width: 40px">#</th>
                            <th>Name</th>
                            <th>Active</th>
                            <th style="width: 140px">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty units}">
                                <c:forEach var="unit" items="${units}" varStatus="status">
                                    <tr data-index="${status.index + 1}" data-unit-id="${unit.unitId}">
                                        <td class="row-number"><strong>${status.index + 1}</strong></td>
                                        <td class="unit-name">${unit.name}</td>
                                        <td class="unit-status">
                                            <c:choose>
                                                <c:when test="${unit.active}">
                                                    <span style="color: #065f46; background-color: #d1fae5; display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 13px; font-weight: 500;">Active</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #991b1b; background-color: #fee2e2; display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 13px; font-weight: 500;">Inactive</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <a href="unit?action=detail&id=${unit.unitId}" class="btn btn-info btn-sm" title="View"><i class="fas fa-eye"></i></a>
                                                <a href="unit?action=edit&id=${unit.unitId}" class="btn btn-primary btn-sm" title="Edit"><i class="fas fa-edit"></i></a>
                                                <form action="unit" method="POST" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa đơn vị này?')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${unit.unitId}">
                                                    <button type="submit" class="btn btn-danger btn-sm" title="Delete"><i class="fas fa-trash"></i></button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="no-data">No units found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="pagination" id="pagination"></div>

            <!-- Modal for Edit/Add -->
            <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editModalLabel">${action == 'edit' ? 'Edit Unit' : 'Add New Unit'}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="unit" method="POST" id="unitForm">
                                <input type="hidden" name="action" id="modalAction" value="${action == 'edit' ? 'update' : 'add'}">
                                <input type="hidden" id="editUnitId" name="unitId" value="${unit != null ? unit.unitId : ''}">
                                <div class="form-group">
                                    <label for="name">Name:</label>
                                    <input type="text" class="form-control" id="name" name="name" value="${unit != null ? unit.name : ''}" required>
                                </div>
                                <div class="form-group">
                                    <label for="isActive">Active:</label>
                                    <select class="form-control" id="isActive" name="isActive" required>
                                        <option value="true" ${unit != null && unit.active ? 'selected' : ''}>Yes</option>
                                        <option value="false" ${unit != null && !unit.active ? 'selected' : ''}>No</option>
                                    </select>
                                </div>
                                <div id="errorMsg" class="error">${errorMsg}</div>
                                <button type="submit" class="btn btn-primary">Save</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal for Details -->
            <div class="modal fade" id="unitModal" tabindex="-1" aria-labelledby="unitModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="unitModalLabel">Unit Details</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="detail-item"><strong>ID:</strong> <span id="modal-unit-id">${unit != null ? unit.unitId : ''}</span></div>
                            <div class="detail-item"><strong>Name:</strong> <span id="modal-unit-name">${unit != null ? unit.name : ''}</span></div>
                            <div class="detail-item"><strong>Active:</strong> <span id="modal-unit-active">${unit != null && unit.active ? 'Active' : 'Inactive'}</span></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function filterUnits() {
            const search = $('#searchInput').val().toLowerCase();
            const status = $('#activeFilter').val();

            $('#unitsTable tbody tr').each(function () {
                const name = $(this).find('.unit-name').text().toLowerCase();
                const isActive = $(this).find('.unit-status span').text().trim() === 'Active';
                const statusVal = isActive ? 'true' : 'false';

                const matchName = name.includes(search);
                const matchStatus = !status || status === statusVal;

                $(this).toggle(matchName && matchStatus);
            });
        }

        $(document).ready(function () {
            // Filter units on input change
            $('#searchInput, #activeFilter').on('input change', filterUnits);

            // Show modals based on action
            <c:if test="${action == 'edit'}">
                var editModal = new bootstrap.Modal(document.getElementById('editModal'));
                editModal.show();
            </c:if>
            <c:if test="${action == 'detail'}">
                var unitModal = new bootstrap.Modal(document.getElementById('unitModal'));
                unitModal.show();
            </c:if>

            // Clear modal data when closed
            $('#editModal, #unitModal').on('hidden.bs.modal', function () {
                // Reset form fields
                $('#unitForm')[0].reset();
                $('#modalAction').val('add');
                $('#editUnitId').val('');
                $('#editModalLabel').text('Add New Unit');
                $('#errorMsg').text('');
                // Clear detail modal
                $('#modal-unit-id').text('');
                $('#modal-unit-name').text('');
                $('#modal-unit-active').text('');
                // Remove backdrop
                $('.modal-backdrop').remove();
            });

            // Handle Add Unit button
            $('.btn-success').on('click', function () {
                $('#unitForm')[0].reset();
                $('#modalAction').val('add');
                $('#editUnitId').val('');
                $('#editModalLabel').text('Add New Unit');
                $('#errorMsg').text('');
                var editModal = new bootstrap.Modal(document.getElementById('editModal'));
                editModal.show();
            });
        });
    </script>
</body>
</html>