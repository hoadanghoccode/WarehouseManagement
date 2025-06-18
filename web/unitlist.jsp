<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
        <title>Unit Management</title>
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
            * {
                box-sizing: border-box;
            }
            body {
                font-family: "Segoe UI", Tahoma, sans-serif;
                background-color: #f3f4f6;
                margin: 0;
                padding: 0;
                color: #374151;
            }
            .container {
                padding-top: 24px;
                padding-bottom: 24px;
                max-width: 1200px;
                margin: 0 auto;
            }
            .title {
                font-size: 28px;
                font-weight: 600;
                color: #1f2937;
                margin-bottom: 16px;
            }
            .stats-info {
                margin-bottom: 16px;
                color: #374151;
                display: flex;
                align-items: center;
                gap: 8px;
                background-color: #dbeafe;
                padding: 12px 16px;
                border-radius: 8px;
                font-size: 14px;
            }
            .stats-info i {
                color: #3b82f6;
            }
            .stats-info strong {
                color: #1f2937;
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

            .action-buttons .btn{
                padding: 6px 12px;
                font-size: 12px;
                border-radius: 10px;
            }
            .no-data {
                text-align: center;
                padding: 24px;
                background-color: #f3f4f6;
                border-radius: 12px;
                font-size: 16px;
                color: #9ca3af;
            }
            .modal-content {
                background-color: white;
                margin: 6% auto;
                padding: 24px 32px;
                border-radius: 4px;
                width: 90%;
                max-width: 480px;
                position: relative;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                font-size: 14px;
            }
            .modal .close {
                position: absolute;
                top: 12px;
                right: 18px;
                font-size: 24px;
                cursor: pointer;
                color: #9ca3af;
                transition: color 0.2s;
            }
            .modal .close:hover {
                color: #374151;
            }
            .detail-item {
                margin-bottom: 14px;
                display: flex;
                gap: 8px;
            }
            .detail-item strong {
                color: #1f2937;
                width: 110px;
            }
            .form-group {
                margin-bottom: 16px;
            }
            .error {
                color: red;
                font-size: 14px;
            }
            .nav-tabs .nav-link {
                font-weight: 500;
                color: #6b7280;
            }
            .nav-tabs .nav-link.active {
                color: #1f2937;
                border-bottom: 2px solid #4f46e5;
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
            .warning-box {
                background-color: #fef2f2;
                border: 1px solid #fecaca;
                border-radius: 8px;
                padding: 16px;
                margin: 16px 0;
            }
            .warning-header {
                display: flex;
                align-items: center;
                gap: 8px;
                color: #991b1b;
                font-weight: 500;
                margin-bottom: 8px;
            }
            .warning-icon {
                font-size: 18px;
            }
            .warning-content {
                color: #374151;
                font-size: 14px;
            }
            .message-box {
                position: relative;
                padding: 16px;
                margin-bottom: 16px;
                border-radius: 8px;
                font-size: 14px;
            }
            .message-box.success {
                background-color: #d1fae5;
                border: 1px solid #34d399;
                color: #065f46;
            }
            .message-box.error {
                background-color: #fee2e2;
                border: 1px solid #f87171;
                color: #991b1b;
            }
            .message-box .btn-close {
                position: absolute;
                top: 12px;
                right: 16px;
                font-size: 16px;
                cursor: pointer;
                color: #9ca3af;
                border: none;
                padding: 0;
            }
            .message-box .btn-close:hover {
                color: #374151;
            }
            .btn btn-success w-100{
                border-radius: 10px;
            }
            @media (max-width: 768px) {
                .title {
                    font-size: 24px;
                }
                table.table {
                    min-width: 600px;
                }
                .modal-content {
                    padding: 20px;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="sidebar.jsp" flush="true"/>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container">
                <h1 class="title">Unit Management</h1>

                <!-- Display Success or Error Messages -->
                <c:if test="${not empty sessionScope.successMsg}">
                    <div class="message-box success">
                        ${sessionScope.successMsg}
                        <button type="button" class="btn-close" onclick="clearMessages()"></button>
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.errorMsg}">
                    <div class="message-box error">
                        ${sessionScope.errorMsg}
                        <button type="button" class="btn-close" onclick="clearMessages()"></button>
                    </div>
                </c:if>

                <!-- Tabs -->
                <ul class="nav nav-tabs" id="unitTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link ${activeTab == 'conversions-tab' ? 'active' : ''}" id="conversions-tab" data-bs-toggle="tab" data-bs-target="#conversions" type="button" role="tab" aria-controls="conversions" aria-selected="${activeTab == 'conversions-tab' ? 'true' : 'false'}">Unit Conversions</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link ${activeTab == 'subunits-tab' ? 'active' : ''}" id="subunits-tab" data-bs-toggle="tab" data-bs-target="#subunits" type="button" role="tab" aria-controls="subunits" aria-selected="${activeTab == 'subunits-tab' ? 'true' : 'false'}">Subunits</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link ${activeTab == 'units-tab' ? 'active' : ''}" id="units-tab" data-bs-toggle="tab" data-bs-target="#units" type="button" role="tab" aria-controls="units" aria-selected="${activeTab == 'units-tab' ? 'true' : 'false'}">Units</button>
                    </li>
                </ul>

                <div class="tab-content" id="unitTabsContent">
                    <!-- Unit Conversions Tab -->
                    <div class="tab-pane fade ${activeTab == 'conversions-tab' ? 'show active' : ''}"
                         id="conversions" role="tabpanel" aria-labelledby="conversions-tab">
                        <div class="row g-2 mb-3 align-items-center">
                            <div class="col-md-4">
                                <input type="text" id="conversionSearchInput" class="form-control" placeholder="Search by unit/subunit..." oninput="filterUnits('conversion')">
                            </div>
                            <div class="col-md-3"></div>
                            <c:if test="${perms['Unit_ADD']}"> 
                                <div class="col-md-3">
                                    <button class="btn btn-success w-100" data-bs-toggle="modal" data-bs-target="#conversionEditModal" onclick="openAddConversionModal()"><i class="fas fa-plus"></i> Add Conversion</button>
                                </div>
                            </c:if>
                        </div>
                        <div class="table-container mb-4">
                            <table class="table" id="conversionsTable">
                                <thead>
                                    <tr>
                                        <th style="width: 40px">#</th>
                                        <th>Unit</th>
                                        <th>Factor</th>
                                        <th>Subunit</th>
                                        <th style="width: 140px">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty conversions}">
                                            <c:forEach var="conversion" items="${conversions}" varStatus="status">
                                                <tr data-index="${status.index + 1}" data-conversion-id="${conversion.unitConversionId}">
                                                    <td class="row-number"><strong>${status.index + 1}</strong></td>
                                                    <td class="conversion-unit">
                                                        <c:forEach var="unit" items="${units}">
                                                            <c:if test="${unit.unitId == conversion.unitId}">${unit.name}</c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td class="conversion-factor">${conversion.factor}</td>
                                                    <td class="conversion-subunit">
                                                        <c:forEach var="subunit" items="${subunits}">
                                                            <c:if test="${subunit.subUnitId == conversion.subUnitId}">${subunit.name}</c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <div class="action-buttons">
                                                            <c:if test="${perms['Unit_VIEW']}"> 
                                                                <a href="unit?entity=conversion&action=detail&id=${conversion.unitConversionId}&activeTab=conversions-tab" class="btn btn-info btn-sm" title="View"><i class="fas fa-eye"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_UPDATE']}"> 
                                                                <a href="unit?entity=conversion&action=edit&id=${conversion.unitConversionId}&activeTab=conversions-tab" class="btn btn-primary btn-sm" title="Edit"><i class="fas fa-edit"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_DELETE']}"> 
                                                                <button type="button" class="btn btn-danger btn-sm" title="Delete" onclick="showDeleteConfirmModal('conversion', '${conversion.unitConversionId}')"><i class="fas fa-trash"></i></button>
                                                                </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" class="no-data">No unit conversions found</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="pagination" id="conversionPagination"></div>
                    </div>

                    <!-- Subunits Tab -->
                    <div class="tab-pane fade ${activeTab == 'subunits-tab' ? 'show active' : ''}"
                         id="subunits" role="tabpanel" aria-labelledby="subunits-tab">
                        <div class="row g-2 mb-3 align-items-center">
                            <div class="col-md-4">
                                <input type="text" id="subunitSearchInput" class="form-control" placeholder="Search by name..." oninput="filterUnits('subunit')">
                            </div>
                            <div class="col-md-3">
                                <select class="form-select" id="subunitActiveFilter" onchange="filterUnits('subunit')">
                                    <option value="">All Status</option>
                                    <option value="active">Active</option>
                                    <option value="inactive">Inactive</option>
                                </select>
                            </div>
                            <c:if test="${perms['Unit_ADD']}"> 
                                <div class="col-md-3">
                                    <button class="btn btn-success w-100" data-bs-toggle="modal" data-bs-target="#subunitEditModal" onclick="openAddSubunitModal()"><i class="fas fa-plus"></i> Add Subunit</button>
                                </div>
                            </c:if>
                        </div>
                        <div class="table-container mb-4">
                            <table class="table" id="subunitsTable">
                                <thead>
                                    <tr>
                                        <th style="width: 40px">#</th>
                                        <th>Name</th>
                                        <th>Status</th>
                                        <th style="width: 140px">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty subunits}">
                                            <c:forEach var="subunit" items="${subunits}" varStatus="status">
                                                <tr data-index="${status.index + 1}" data-subunit-id="${subunit.subUnitId}">
                                                    <td class="row-number"><strong>${status.index + 1}</strong></td>
                                                    <td class="subunit-name">${subunit.name}</td>
                                                    <td class="subunit-status">
                                                        <span style="color: ${subunit.status == 'active' ? '#065f46' : '#991b1b'}; background-color: ${subunit.status == 'active' ? '#d1fae5' : '#fee2e2'}; display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 13px; font-weight: 500;">${subunit.status == 'active' ? 'Active' : 'Inactive'}</span>
                                                    </td>
                                                    <td>
                                                        <div class="action-buttons">
                                                            <c:if test="${perms['Unit_VIEW']}"> 
                                                                <a href="unit?entity=subunit&action=detail&id=${subunit.subUnitId}&activeTab=subunits-tab" class="btn btn-info btn-sm" title="View"><i class="fas fa-eye"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_UPDATE']}"> 
                                                                <a href="unit?entity=subunit&action=edit&id=${subunit.subUnitId}&activeTab=subunits-tab" class="btn btn-primary btn-sm" title="Edit"><i class="fas fa-edit"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_DELETE']}"> 
                                                                <button type="button" class="btn btn-danger btn-sm" title="Delete" onclick="checkDeleteStatus('subunit', '${subunit.subUnitId}', '${subunit.status}')"><i class="fas fa-trash"></i></button>
                                                                </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="4" class="no-data">No subunits found</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="pagination" id="subunitPagination"></div>
                    </div>

                    <!-- Units Tab -->
                    <div class="tab-pane fade ${activeTab == 'units-tab' ? 'show active' : ''}"
                         id="units" role="tabpanel" aria-labelledby="units-tab">
                        <div class="row g-2 mb-3 align-items-center">
                            <div class="col-md-4">
                                <input type="text" id="unitSearchInput" class="form-control" placeholder="Search by name..." oninput="filterUnits('unit')">
                            </div>
                            <div class="col-md-3">
                                <select class="form-select" id="unitActiveFilter" onchange="filterUnits('unit')">
                                    <option value="">All Status</option>
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </select>
                            </div>
                            <c:if test="${perms['Unit_ADD']}"> 
                                <div class="col-md-3">
                                    <button class="btn btn-success w-100" data-bs-toggle="modal" data-bs-target="#unitEditModal" onclick="openAddUnitModal()"><i class="fas fa-plus"></i> Add Unit</button>
                                </div>
                            </c:if>
                        </div>
                        <div class="table-container mb-4">
                            <table class="table" id="unitsTable">
                                <thead>
                                    <tr>
                                        <th style="width: 40px">#</th>
                                        <th>Name</th>
                                        <th>Status</th>
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
                                                            <c:if test="${perms['Unit_VIEW']}"> 
                                                                <a href="unit?entity=unit&action=detail&id=${unit.unitId}&activeTab=units-tab" class="btn btn-info btn-sm" title="View"><i class="fas fa-eye"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_UPDATE']}"> 
                                                                <a href="unit?entity=unit&action=edit&id=${unit.unitId}&activeTab=units-tab" class="btn btn-primary btn-sm" title="Edit"><i class="fas fa-edit"></i></a>
                                                                </c:if>
                                                                <c:if test="${perms['Unit_DELETE']}"> 
                                                                <button type="button" class="btn btn-danger btn-sm" title="Delete" onclick="checkDeleteStatus('unit', '${unit.unitId}', '${unit.active}')"><i class="fas fa-trash"></i></button>
                                                                </c:if>
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
                        <div class="pagination" id="unitPagination"></div>
                    </div>
                </div>

                <!-- Unit Edit/Add Modal -->
                <div class="modal fade" id="unitEditModal" tabindex="-1" aria-labelledby="unitEditModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="unitEditModalLabel"></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="unit" method="POST" id="unitForm">
                                    <input type="hidden" name="entity" value="unit">
                                    <input type="hidden" name="action" id="unitModalAction">
                                    <input type="hidden" id="editUnitId" name="unitId">
                                    <input type="hidden" id="originalUnitStatus" name="originalStatus">
                                    <div class="form-group">
                                        <label for="unitName">Name:</label>
                                        <input type="text" class="form-control" id="unitName" name="name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="unitIsActive">Status:</label>
                                        <select class="form-control" id="unitIsActive" name="isActive" required>
                                            <option value="true">Active</option>
                                            <option value="false">Inactive</option>
                                        </select>
                                    </div>
                                    <div id="unitErrorMsg" class="error"></div>
                                    <button type="submit" class="btn btn-primary" id="unitSubmitBtn">Save</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Unit Details Modal -->
                <div class="modal fade" id="unitDetailModal" tabindex="-1" aria-labelledby="unitDetailModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="unitDetailModalLabel">Unit Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="detail-item"><strong>ID:</strong> <span id="modal-unit-id"></span></div>
                                <div class="detail-item"><strong>Name:</strong> <span id="modal-unit-name"></span></div>
                                <div class="detail-item"><strong>Status:</strong> <span id="modal-unit-status"></span></div>
                                <div class="detail-item"><strong>Created At:</strong> <span id="modal-unit-created"></span></div>
                                <div class="detail-item"><strong>Updated At:</strong> <span id="modal-unit-updated"></span></div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Unit Deactivation Confirmation Modal -->
                <div class="modal fade" id="unitConfirmModal" tabindex="-1" aria-labelledby="unitConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="unitConfirmModalLabel">Confirm Unit Deactivation</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Are you sure you want to deactivate this unit? Deactivating this unit will cause the conversions associated with them to be deleted.</p>
                                <c:if test="${countConversions > 0}">
                                    <div class="warning-box">
                                        <div class="warning-header">
                                            <i class="fas fa-exclamation-triangle warning-icon"></i>
                                            <span class="warning-title">Warning</span>
                                        </div>
                                        <div class="warning-content">
                                            This unit has <strong>${countConversions}</strong> related unit conversions.
                                            <br/><br/>
                                            <span style="color: #991b1b;">
                                                All of these unit conversions will be deleted.
                                            </span>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            <div class="modal-footer">
                                <form action="unit" method="POST" id="unitConfirmForm">
                                    <input type="hidden" name="entity" value="unit">
                                    <input type="hidden" name="action" value="confirmDeactivateUnit">
                                    <input type="hidden" id="confirmUnitId" name="unitId">
                                    <input type="hidden" id="confirmUnitNewStatus" name="newStatus" value="false">
                                    <button type="submit" class="btn btn-primary">Yes, Deactivate Unit</button>
                                </form>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No, Keep Current</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Subunit Edit/Add Modal -->
                <div class="modal fade" id="subunitEditModal" tabindex="-1" aria-labelledby="subunitEditModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="subunitEditModalLabel"></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="unit" method="POST" id="subunitForm">
                                    <input type="hidden" name="entity" value="subunit">
                                    <input type="hidden" name="action" id="subunitModalAction">
                                    <input type="hidden" id="editSubUnitId" name="subUnitId">
                                    <input type="hidden" id="originalSubunitStatus" name="originalStatus">
                                    <div class="form-group">
                                        <label for="subunitName">Name:</label>
                                        <input type="text" class="form-control" id="subunitName" name="name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="subunitStatus">Status:</label>
                                        <select class="form-control" id="subunitStatus" name="status" required>
                                            <option value="active">Active</option>
                                            <option value="inactive">Inactive</option>
                                        </select>
                                    </div>
                                    <div id="subunitErrorMsg" class="error"></div>
                                    <button type="submit" class="btn btn-primary" id="subunitSubmitBtn">Save</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Subunit Details Modal -->
                <div class="modal fade" id="subunitDetailModal" tabindex="-1" aria-labelledby="subunitDetailModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="subunitDetailModalLabel">Subunit Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="detail-item"><strong>ID:</strong> <span id="modal-subunit-id"></span></div>
                                <div class="detail-item"><strong>Name:</strong> <span id="modal-subunit-name"></span></div>
                                <div class="detail-item"><strong>Status:</strong> <span id="modal-subunit-status"></span></div>
                                <div class="detail-item"><strong>Created At:</strong> <span id="modal-subunit-created"></span></div>
                                <div class="detail-item"><strong>Updated At:</strong> <span id="modal-subunit-updated"></span></div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Subunit Deactivation Confirmation Modal -->
                <div class="modal fade" id="subunitConfirmModal" tabindex="-1" aria-labelledby="subunitConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="subunitConfirmModalLabel">Confirm Subunit Deactivation</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Are you sure you want to deactivate this subunit? Deactivating this subunit will cause the conversions associated with them to be deleted.</p>
                                <c:if test="${countConversions > 0}">
                                    <div class="warning-box">
                                        <div class="warning-header">
                                            <i class="fas fa-exclamation-triangle warning-icon"></i>
                                            <span class="warning-title">Warning</span>
                                        </div>
                                        <div class="warning-content">
                                            This subunit has <strong>${countConversions}</strong> related unit conversions.
                                            <br/><br/>
                                            <span style="color: #991b1b;">
                                                All of these unit conversions will be deleted.
                                            </span>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            <div class="modal-footer">
                                <form action="unit" method="POST" id="subunitConfirmForm">
                                    <input type="hidden" name="entity" value="subunit">
                                    <input type="hidden" name="action" value="confirmDeactivateSubunit">
                                    <input type="hidden" id="confirmSubUnitId" name="subUnitId">
                                    <input type="hidden" id="confirmNewStatus" name="newStatus" value="inactive">
                                    <button type="submit" class="btn btn-primary">Yes, Deactivate Subunit</button>
                                </form>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No, Keep Current</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Unit Conversion Edit/Add Modal -->
                <div class="modal fade" id="conversionEditModal" tabindex="-1" aria-labelledby="conversionEditModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="conversionEditModalLabel"></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="unit" method="POST" id="conversionForm">
                                    <input type="hidden" name="entity" value="conversion">
                                    <input type="hidden" name="action" id="conversionModalAction">
                                    <input type="hidden" id="editUnitConversionId" name="unitConversionId">
                                    <div class="form-group">
                                        <label for="conversionUnitId">Unit:</label>
                                        <select class="form-control" id="conversionUnitId" name="unitId" required>
                                            <option value="">Select Unit</option>
                                            <c:forEach var="unit" items="${units}">
                                                <c:if test="${unit.active}">
                                                    <option value="${unit.unitId}">${unit.name}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="conversionSubUnitId">Subunit:</label>
                                        <select class="form-control" id="conversionSubUnitId" name="subUnitId" required>
                                            <option value="">Select Subunit</option>
                                            <c:forEach var="subunit" items="${subunits}">
                                                <c:if test="${subunit.status == 'active'}">
                                                    <option value="${subunit.subUnitId}">${subunit.name}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="conversionFactor">Factor:</label>
                                        <input type="number" step="0.0001" class="form-control" id="conversionFactor" name="factor" required min="0.0001">
                                    </div>
                                    <div id="conversionErrorMsg" class="error"></div>
                                    <button type="submit" class="btn btn-primary" id="conversionSubmitBtn">Save</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Unit Conversion Details Modal -->
                <div class="modal fade" id="conversionDetailModal" tabindex="-1" aria-labelledby="conversionDetailModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="conversionDetailModalLabel">Unit Conversion Details</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="detail-item"><strong>ID:</strong> <span id="modal-conversion-id"></span></div>
                                <div class="detail-item"><strong>Unit:</strong> <span id="modal-conversion-unit"></span></div>
                                <div class="detail-item"><strong>Factor:</strong> <span id="modal-conversion-factor"></span></div>
                                <div class="detail-item"><strong>Subunit:</strong> <span id="modal-conversion-subunit"></span></div>
                                <div class="detail-item"><strong>Created At:</strong> <span id="modal-conversion-created"></span></div>
                                <div class="detail-item"><strong>Updated At:</strong> <span id="modal-conversion-updated"></span></div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Delete Confirmation Modal -->
                <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-labelledby="deleteConfirmModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteConfirmModalLabel">Confirm Deletion</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p id="deleteConfirmMessage"></p>
                            </div>
                            <div class="modal-footer">
                                <form action="unit" method="POST" id="deleteConfirmForm">
                                    <input type="hidden" name="entity" id="deleteEntity">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" id="deleteId">
                                    <button type="submit" class="btn btn-danger">Yes, Delete</button>
                                </form>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Active Entity Deletion Error Modal -->
                <div class="modal fade" id="activeDeleteErrorModal" tabindex="-1" aria-labelledby="activeDeleteErrorModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="activeDeleteErrorModalLabel">Deletion Not Allowed</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p id="activeDeleteErrorMessage"></p>
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
        <script src="vendors/datatable/js/jquery.dataTables.min.js"></script>
        <script>
                                                                    function clearMessages() {
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'POST',
                                                                            data: {action: 'clearMessages'},
                                                                            success: function () {
                                                                                console.log('Session messages cleared');
                                                                                window.location.reload();
                                                                            },
                                                                            error: function (xhr, status, error) {
                                                                                console.error('Error clearing session messages: ' + error);
                                                                                window.location.reload();
                                                                            }
                                                                        });
                                                                    }

                                                                    // Check deletion status for units and subunits
                                                                    function checkDeleteStatus(entity, id, status) {
                                                                        if (entity === 'unit' && status === 'true' || entity === 'subunit' && status === 'active') {
                                                                            let entityName = entity === 'unit' ? 'Unit' : 'Subunit';
                                                                            $('#activeDeleteErrorMessage').text(`${entityName} Is active cannot be deleted.`);
                                                                            var errorModal = new bootstrap.Modal(document.getElementById('activeDeleteErrorModal'));
                                                                            errorModal.show();
                                                                        } else {
                                                                            showDeleteConfirmModal(entity, id);
                                                                        }
                                                                    }

                                                                    // Show custom delete confirmation modal
                                                                    function showDeleteConfirmModal(entity, id) {
                                                                        let entityName;
                                                                        switch (entity) {
                                                                            case 'unit':
                                                                                entityName = 'Unit';
                                                                                break;
                                                                            case 'subunit':
                                                                                entityName = 'Subunit';
                                                                                break;
                                                                            case 'conversion':
                                                                                entityName = 'Unit Conversion';
                                                                                break;
                                                                            default:
                                                                                entityName = 'Item';
                                                                        }
                                                                        $('#deleteConfirmMessage').text(`Are you sure you want to delete this ${entityName}?`);
                                                                        $('#deleteEntity').val(entity);
                                                                        $('#deleteId').val(id);
                                                                        var deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
                                                                        deleteModal.show();
                                                                    }

            <c:if test="${not empty action && not empty entity}">
                <c:choose>
                    <c:when test="${entity == 'unit'}">
                        <c:choose>
                            <c:when test="${action == 'edit'}">
                                                                    var editModal = new bootstrap.Modal(document.getElementById('unitEditModal'));
                                                                    $('#unitEditModalLabel').text('Edit Unit');
                                                                    $('#unitModalAction').val('update');
                                                                    $('#editUnitId').val('${unit.unitId}');
                                                                    $('#unitName').val('${unit.name}');
                                                                    $('#unitIsActive').val('${unit.active}');
                                                                    $('#originalUnitStatus').val('${unit.active}');
                                                                    $('#unitSubmitBtn').text('Update');
                                                                    editModal.show();
                            </c:when>
                            <c:when test="${action == 'detail'}">
                                                                    var detailModal = new bootstrap.Modal(document.getElementById('unitDetailModal'));
                                                                    $('#modal-unit-id').text('${unit.unitId}');
                                                                    $('#modal-unit-name').text('${unit.name}');
                                                                    $('#modal-unit-status').text(${unit.active} ? 'Active' : 'Inactive');
                                                                    $('#modal-unit-created').text('<fmt:formatDate value="${unit.createdAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    $('#modal-unit-updated').text('<fmt:formatDate value="${unit.updatedAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    detailModal.show();
                            </c:when>
                            <c:when test="${action == 'confirmDeactivateUnit'}">
                                                                    var confirmModal = new bootstrap.Modal(document.getElementById('unitConfirmModal'));
                                                                    $('#confirmUnitId').val('${unit.unitId}');
                                                                    confirmModal.show();
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:when test="${entity == 'subunit'}">
                        <c:choose>
                            <c:when test="${action == 'edit'}">
                                                                    var editModal = new bootstrap.Modal(document.getElementById('subunitEditModal'));
                                                                    $('#subunitEditModalLabel').text('Edit Subunit');
                                                                    $('#subunitModalAction').val('update');
                                                                    $('#editSubUnitId').val('${subunit.subUnitId}');
                                                                    $('#subunitName').val('${subunit.name}');
                                                                    $('#subunitStatus').val('${subunit.status}');
                                                                    $('#originalSubunitStatus').val('${subunit.status}');
                                                                    $('#subunitSubmitBtn').text('Update');
                                                                    editModal.show();
                            </c:when>
                            <c:when test="${action == 'detail'}">
                                                                    var detailModal = new bootstrap.Modal(document.getElementById('subunitDetailModal'));
                                                                    $('#modal-subunit-id').text('${subunit.subUnitId}');
                                                                    $('#modal-subunit-name').text('${subunit.name}');
                                                                    $('#modal-subunit-status').text('${subunit.status == "active" ? "Active" : "Inactive"}');
                                                                    $('#modal-subunit-created').text('<fmt:formatDate value="${subunit.createdAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    $('#modal-subunit-updated').text('<fmt:formatDate value="${subunit.updatedAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    detailModal.show();
                            </c:when>
                            <c:when test="${action == 'confirmDeactivateSubunit'}">
                                                                    var confirmModal = new bootstrap.Modal(document.getElementById('subunitConfirmModal'));
                                                                    $('#confirmSubUnitId').val('${subunit.subUnitId}');
                                                                    confirmModal.show();
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:when test="${entity == 'conversion'}">
                        <c:choose>
                            <c:when test="${action == 'edit'}">
                                                                    var editModal = new bootstrap.Modal(document.getElementById('conversionEditModal'));
                                                                    $('#conversionEditModalLabel').text('Edit Unit Conversion');
                                                                    $('#conversionModalAction').val('update');
                                                                    $('#editUnitConversionId').val('${conversion.unitConversionId}');
                                                                    $('#conversionUnitId').val('${conversion.unitId}');
                                                                    $('#conversionSubUnitId').val('${conversion.subUnitId}');
                                                                    $('#conversionFactor').val('${conversion.factor}');
                                                                    $('#conversionSubmitBtn').text('Update');
                                                                    editModal.show();
                            </c:when>
                            <c:when test="${action == 'detail'}">
                                                                    var detailModal = new bootstrap.Modal(document.getElementById('conversionDetailModal'));
                                                                    $('#modal-conversion-id').text('${conversion.unitConversionId}');
                                                                    $('#modal-conversion-unit').text(getUnitName(${conversion.unitId}));
                                                                    $('#modal-conversion-factor').text(${conversion.factor});
                                                                    $('#modal-conversion-subunit').text(getSubUnitName(${conversion.subUnitId}));
                                                                    $('#modal-conversion-created').text('<fmt:formatDate value="${conversion.createdAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    $('#modal-conversion-updated').text('<fmt:formatDate value="${conversion.updatedAt}" pattern="dd-MM-yyyy HH:mm:ss"/>');
                                                                    detailModal.show();
                            </c:when>
                        </c:choose>
                    </c:when>
                </c:choose>
            </c:if>

                                                                    // Validate unit form submission
                                                                    $('#unitForm').on('submit', function (e) {
                                                                        e.preventDefault();
                                                                        let name = $('#unitName').val().trim();
                                                                        let status = $('#unitIsActive').val();
                                                                        let originalStatus = $('#originalUnitStatus').val();
                                                                        let unitId = $('#editUnitId').val();
                                                                        if (!name) {
                                                                            $('#unitErrorMsg').text('Please enter the unit name.');
                                                                            return;
                                                                        }
                                                                        // Check for duplicate unit name
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'GET',
                                                                            data: {
                                                                                entity: 'unit',
                                                                                action: 'checkDuplicate',
                                                                                name: name,
                                                                                id: unitId
                                                                            },
                                                                            success: function (response) {
                                                                                if (response === 'exists') {
                                                                                    $('#unitErrorMsg').text('Unit name already exists.');
                                                                                } else {
                                                                                    $('#unitForm')[0].submit();
                                                                                }
                                                                            }
                                                                        });
                                                                    });

                                                                    // Validate subunit form submission
                                                                    $('#subunitForm').on('submit', function (e) {
                                                                        e.preventDefault();
                                                                        let name = $('#subunitName').val().trim();
                                                                        let status = $('#subunitStatus').val();
                                                                        let originalStatus = $('#originalSubunitStatus').val();
                                                                        let subUnitId = $('#editSubUnitId').val();
                                                                        if (!name) {
                                                                            $('#subunitErrorMsg').text('Please enter the subunit name.');
                                                                            return;
                                                                        }
                                                                        // Check for duplicate subunit name
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'GET',
                                                                            data: {
                                                                                entity: 'subunit',
                                                                                action: 'checkDuplicate',
                                                                                name: name,
                                                                                id: subUnitId
                                                                            },
                                                                            success: function (response) {
                                                                                if (response === 'exists') {
                                                                                    $('#subunitErrorMsg').text('Subunit name already exists.');
                                                                                } else {
                                                                                    $('#subunitForm')[0].submit();
                                                                                }
                                                                            }
                                                                        });
                                                                    });

                                                                    // Validate conversion form submission
                                                                    $('#conversionForm').on('submit', function (e) {
                                                                        e.preventDefault();
                                                                        let unitId = $('#conversionUnitId').val();
                                                                        let subUnitId = $('#conversionSubUnitId').val();
                                                                        let factor = parseFloat($('#conversionFactor').val());
                                                                        let conversionId = $('#editUnitConversionId').val();
                                                                        if (!unitId || !subUnitId) {
                                                                            $('#conversionErrorMsg').text('Please select valid unit and subunit.');
                                                                            return;
                                                                        } else if (unitId === subUnitId) {
                                                                            $('#conversionErrorMsg').text('Unit and subunit cannot be the same.');
                                                                            return;
                                                                        } else if (isNaN(factor) || factor <= 0) {
                                                                            $('#conversionErrorMsg').text('Please provide a valid factor greater than 0.');
                                                                            return;
                                                                        }
                                                                        // Check for duplicate conversion
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'GET',
                                                                            data: {
                                                                                entity: 'conversion',
                                                                                action: 'checkDuplicate',
                                                                                unitId: unitId,
                                                                                subUnitId: subUnitId,
                                                                                id: conversionId
                                                                            },
                                                                            success: function (response) {
                                                                                console.log('Duplicate check response:', response); // Debugging
                                                                                if (response.trim() === 'exists') {
                                                                                    $('#conversionErrorMsg').text('This pair of units has been converted.');
                                                                                } else {
                                                                                    $('#conversionErrorMsg').text('');
                                                                                    $('#conversionForm')[0].submit();
                                                                                }
                                                                            },
                                                                            error: function (xhr, status, error) {
                                                                                console.error('Error checking duplicate conversion:', error, xhr.responseText); // Debugging
                                                                                $('#conversionErrorMsg').text('Lỗi khi kiểm tra quy đổi trùng lặp: ' + error);
                                                                            }
                                                                        });
                                                                    });
                                                                    
                                                                    function getUnitName(unitId) {
            <c:forEach var="unit" items="${units}">
                                                                        if (unitId == ${unit.unitId})
                                                                            return "${unit.name}";
            </c:forEach>
                                                                        return "Unknown";
                                                                    }

                                                                    function getSubUnitName(subUnitId) {
            <c:forEach var="subunit" items="${subunits}">
                                                                        if (subUnitId == ${subunit.subUnitId})
                                                                            return "${subunit.name}";
            </c:forEach>
                                                                        return "Unknown";
                                                                    }

                                                                    function openAddUnitModal() {
                                                                        $('#unitEditModalLabel').text('Add Unit');
                                                                        $('#unitModalAction').val('add');
                                                                        $('#editUnitId').val('');
                                                                        $('#unitName').val('');
                                                                        $('#unitIsActive').val('true');
                                                                        $('#originalUnitStatus').val('');
                                                                        $('#unitSubmitBtn').text('Save');
                                                                        $('#unitErrorMsg').text('');
                                                                    }

                                                                    function openAddSubunitModal() {
                                                                        $('#subunitEditModalLabel').text('Add Subunit');
                                                                        $('#subunitModalAction').val('add');
                                                                        $('#editSubUnitId').val('');
                                                                        $('#subunitName').val('');
                                                                        $('#subunitStatus').val('active');
                                                                        $('#originalSubunitStatus').val('');
                                                                        $('#subunitSubmitBtn').text('Save');
                                                                        $('#subunitErrorMsg').text('');
                                                                    }

                                                                    function openAddConversionModal() {
                                                                        $('#conversionEditModalLabel').text('Add Unit Conversion');
                                                                        $('#conversionModalAction').val('add');
                                                                        $('#editUnitConversionId').val('');
                                                                        $('#conversionUnitId').val('');
                                                                        $('#conversionSubUnitId').val('');
                                                                        $('#conversionFactor').val('');
                                                                        $('#conversionSubmitBtn').text('Save');
                                                                        $('#conversionErrorMsg').text('');
                                                                    }

                                                                    function checkUnitConversions(unitId) {
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'GET',
                                                                            data: {
                                                                                entity: 'unit',
                                                                                action: 'checkConversions',
                                                                                id: unitId
                                                                            },
                                                                            success: function (response) {
                                                                                var parser = new DOMParser();
                                                                                var doc = parser.parseFromString(response, 'text/html');
                                                                                var countConversions = $(doc).find('#countConversions').val() || 0;
                                                                                var unitIdFromResponse = $(doc).find('#confirmUnitId').val() || unitId;
                                                                                $('#confirmUnitId').val(unitIdFromResponse);
                                                                                var confirmModal = new bootstrap.Modal(document.getElementById('unitConfirmModal'));
                                                                                var warningBox = $('#unitConfirmModal .warning-box');
                                                                                if (countConversions > 0) {
                                                                                    warningBox.show();
                                                                                    warningBox.find('.warning-content strong').text(countConversions);
                                                                                } else {
                                                                                    warningBox.hide();
                                                                                }
                                                                                confirmModal.show();
                                                                            },
                                                                            error: function (xhr, status, error) {
                                                                                $('#unitErrorMsg').text('Error checking conversions: ' + error);
                                                                            }
                                                                        });
                                                                    }

                                                                    function checkSubUnitConversions(subUnitId) {
                                                                        $.ajax({
                                                                            url: 'unit',
                                                                            type: 'GET',
                                                                            data: {
                                                                                entity: 'subunit',
                                                                                action: 'checkConversions',
                                                                                id: subUnitId
                                                                            },
                                                                            success: function (response) {
                                                                                var parser = new DOMParser();
                                                                                var doc = parser.parseFromString(response, 'text/html');
                                                                                var countConversions = $(doc).find('#countConversions').val() || 0;
                                                                                var subUnitIdFromResponse = $(doc).find('#confirmSubUnitId').val() || subUnitId;
                                                                                $('#confirmSubUnitId').val(subUnitIdFromResponse);
                                                                                var confirmModal = new bootstrap.Modal(document.getElementById('subunitConfirmModal'));
                                                                                var warningBox = $('#subunitConfirmModal .warning-box');
                                                                                if (countConversions > 0) {
                                                                                    warningBox.show();
                                                                                    warningBox.find('.warning-content strong').text(countConversions);
                                                                                } else {
                                                                                    warningBox.hide();
                                                                                }
                                                                                confirmModal.show();
                                                                            },
                                                                            error: function (xhr, status, error) {
                                                                                $('#subunitErrorMsg').text('Error checking conversions: ' + error);
                                                                            }
                                                                        });
                                                                    }

                                                                    const paginationState = {
                                                                        conversion: {pageSize: 5, currentPage: 1, allRows: [], filteredRows: []},
                                                                        subunit: {pageSize: 5, currentPage: 1, allRows: [], filteredRows: []},
                                                                        unit: {pageSize: 5, currentPage: 1, allRows: [], filteredRows: []}
                                                                    };

                                                                    window.addEventListener("DOMContentLoaded", function () {
                                                                        paginationState.conversion.allRows = Array.from(document.querySelectorAll("#conversionsTable tbody tr:not(.no-data)"));
                                                                        paginationState.conversion.filteredRows = [...paginationState.conversion.allRows];
                                                                        paginationState.subunit.allRows = Array.from(document.querySelectorAll("#subunitsTable tbody tr:not(.no-data)"));
                                                                        paginationState.subunit.filteredRows = [...paginationState.subunit.allRows];
                                                                        paginationState.unit.allRows = Array.from(document.querySelectorAll("#unitsTable tbody tr:not(.no-data)"));
                                                                        paginationState.unit.filteredRows = [...paginationState.unit.allRows];
                                                                        updateTable('conversion');
                                                                        updatePagination('conversion');
                                                                        updateTable('subunit');
                                                                        updatePagination('subunit');
                                                                        updateTable('unit');
                                                                        updatePagination('unit');
                                                                        document.querySelectorAll("#conversionSearchInput").forEach(el => {
                                                                            el.addEventListener("input", () => filterUnits('conversion'));
                                                                        });
                                                                        document.querySelectorAll("#subunitSearchInput, #subunitActiveFilter").forEach(el => {
                                                                            el.addEventListener("input", () => filterUnits('subunit'));
                                                                            el.addEventListener("change", () => filterUnits('subunit'));
                                                                        });
                                                                        document.querySelectorAll("#unitSearchInput, #unitActiveFilter").forEach(el => {
                                                                            el.addEventListener("input", () => filterUnits('unit'));
                                                                            el.addEventListener("change", () => filterUnits('unit'));
                                                                        });

                                                                        // Add event listeners to modal close buttons
                                                                        document.querySelectorAll('.modal .btn-close, .modal .close, .modal').forEach(element => {
                                                                            element.addEventListener('click', function (event) {
                                                                                if (event.target === element || event.target.classList.contains('btn-close') || event.target.classList.contains('close')) {
                                                                                    updateUrlToUnit();
                                                                                }
                                                                            });
                                                                        });

                                                                        document.querySelectorAll('.modal .btn-secondary').forEach(button => {
                                                                            button.addEventListener('click', updateUrlToUnit);
                                                                        });
                                                                    });

                                                                    function filterUnits(entity) {
                                                                        let searchInput, statusFilter, nameClass, statusClass;
                                                                        const state = paginationState[entity];
                                                                        if (entity === 'unit') {
                                                                            searchInput = '#unitSearchInput';
                                                                            statusFilter = '#unitActiveFilter';
                                                                            nameClass = '.unit-name';
                                                                            statusClass = '.unit-status';
                                                                        } else if (entity === 'subunit') {
                                                                            searchInput = '#subunitSearchInput';
                                                                            statusFilter = '#subunitActiveFilter';
                                                                            nameClass = '.subunit-name';
                                                                            statusClass = '.subunit-status';
                                                                        } else if (entity === 'conversion') {
                                                                            searchInput = '#conversionSearchInput';
                                                                        }
                                                                        const search = document.querySelector(searchInput).value.trim().toLowerCase();
                                                                        const status = entity !== 'conversion' ? document.querySelector(statusFilter).value.trim().toLowerCase() : '';
                                                                        state.filteredRows = state.allRows.filter(row => {
                                                                            if (entity === 'conversion') {
                                                                                const unit = row.querySelector('.conversion-unit')?.textContent.trim().toLowerCase() || '';
                                                                                const subunit = row.querySelector('.conversion-subunit')?.textContent.trim().toLowerCase() || '';
                                                                                return unit.includes(search) || subunit.includes(search);
                                                                            } else {
                                                                                const name = row.querySelector(nameClass)?.textContent.trim().toLowerCase() || '';
                                                                                const statusText = row.querySelector(statusClass)?.textContent.trim().toLowerCase() || '';
                                                                                const matchName = name.includes(search);
                                                                                let matchStatus = true;
                                                                                if (status !== '') {
                                                                                    if (entity === 'unit') {
                                                                                        matchStatus = (status === 'true' && statusText === 'active') ||
                                                                                                (status === 'false' && statusText === 'inactive');
                                                                                    } else if (entity === 'subunit') {
                                                                                        matchStatus = statusText === status;
                                                                                    }
                                                                                }
                                                                                return matchName && matchStatus;
                                                                            }
                                                                        });
                                                                        state.currentPage = 1;
                                                                        updateTable(entity);
                                                                        updatePagination(entity);
                                                                    }

                                                                    function updateTable(entity) {
                                                                        const state = paginationState[entity];
                                                                        let tableId = entity === 'unit' ? '#unitsTable' : entity === 'subunit' ? '#subunitsTable' : '#conversionsTable';
                                                                        state.allRows.forEach(row => {
                                                                            row.style.display = 'none';
                                                                        });
                                                                        const start = (state.currentPage - 1) * state.pageSize;
                                                                        const end = Math.min(start + state.pageSize, state.filteredRows.length);
                                                                        const rowsToShow = state.filteredRows.slice(start, end);
                                                                        rowsToShow.forEach((row, index) => {
                                                                            row.style.display = '';
                                                                            const rowNumberCell = row.querySelector('.row-number strong');
                                                                            rowNumberCell.textContent = start + index + 1;
                                                                        });
                                                                    }

                                                                    function updatePagination(entity) {
                                                                        const state = paginationState[entity];
                                                                        const paginationId = entity === 'unit' ? '#unitPagination' : entity === 'subunit' ? '#subunitPagination' : '#conversionPagination';
                                                                        const pagination = document.querySelector(paginationId);
                                                                        pagination.innerHTML = '';
                                                                        const totalPages = Math.ceil(state.filteredRows.length / state.pageSize);
                                                                        if (totalPages <= 1)
                                                                            return;
                                                                        if (state.currentPage > 1) {
                                                                            const first = document.createElement('a');
                                                                            first.href = '#';
                                                                            first.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                                                                            first.addEventListener('click', e => {
                                                                                e.preventDefault();
                                                                                state.currentPage = 1;
                                                                                updatePagination(entity);
                                                                                updateTable(entity);
                                                                            });
                                                                            pagination.appendChild(first);
                                                                            const prev = document.createElement('a');
                                                                            prev.href = '#';
                                                                            prev.innerHTML = '<i class="fas fa-angle-left"></i>';
                                                                            prev.addEventListener('click', e => {
                                                                                e.preventDefault();
                                                                                state.currentPage--;
                                                                                updatePagination(entity);
                                                                                updateTable(entity);
                                                                            });
                                                                            pagination.appendChild(prev);
                                                                        } else {
                                                                            const disabledFirst = document.createElement('span');
                                                                            disabledFirst.className = 'text-muted me-2';
                                                                            disabledFirst.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                                                                            pagination.appendChild(disabledFirst);
                                                                            const disabledPrev = document.createElement('span');
                                                                            disabledPrev.className = 'text-muted me-2';
                                                                            disabledPrev.innerHTML = '<i class="fas fa-angle-left"></i>';
                                                                            pagination.appendChild(disabledPrev);
                                                                        }
                                                                        if (totalPages <= 7) {
                                                                            for (let i = 1; i <= totalPages; i++) {
                                                                                if (i === state.currentPage) {
                                                                                    const current = document.createElement('span');
                                                                                    current.className = 'current';
                                                                                    current.textContent = i;
                                                                                    pagination.appendChild(current);
                                                                                } else {
                                                                                    const pageLink = document.createElement('a');
                                                                                    pageLink.href = '#';
                                                                                    pageLink.textContent = i;
                                                                                    pageLink.addEventListener('click', e => {
                                                                                        e.preventDefault();
                                                                                        state.currentPage = i;
                                                                                        updatePagination(entity);
                                                                                        updateTable(entity);
                                                                                    });
                                                                                    pagination.appendChild(pageLink);
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if (state.currentPage <= 4) {
                                                                                for (let i = 1; i <= 5; i++) {
                                                                                    if (i === state.currentPage) {
                                                                                        const current = document.createElement('span');
                                                                                        current.className = 'current';
                                                                                        current.textContent = i;
                                                                                        pagination.appendChild(current);
                                                                                    } else {
                                                                                        const pageLink = document.createElement('a');
                                                                                        pageLink.href = '#';
                                                                                        pageLink.textContent = i;
                                                                                        pageLink.addEventListener('click', e => {
                                                                                            e.preventDefault();
                                                                                            state.currentPage = i;
                                                                                            updatePagination(entity);
                                                                                            updateTable(entity);
                                                                                        });
                                                                                        pagination.appendChild(pageLink);
                                                                                    }
                                                                                }
                                                                                if (totalPages > 6) {
                                                                                    const dots = document.createElement('span');
                                                                                    dots.textContent = '...';
                                                                                    pagination.appendChild(dots);
                                                                                    const last = document.createElement('a');
                                                                                    last.href = '#';
                                                                                    last.textContent = totalPages;
                                                                                    last.addEventListener('click', e => {
                                                                                        e.preventDefault();
                                                                                        state.currentPage = totalPages;
                                                                                        updatePagination(entity);
                                                                                        updateTable(entity);
                                                                                    });
                                                                                    pagination.appendChild(last);
                                                                                }
                                                                            } else if (state.currentPage >= totalPages - 3) {
                                                                                const first = document.createElement('a');
                                                                                first.href = '#';
                                                                                first.textContent = '1';
                                                                                first.addEventListener('click', e => {
                                                                                    e.preventDefault();
                                                                                    state.currentPage = 1;
                                                                                    updatePagination(entity);
                                                                                    updateTable(entity);
                                                                                });
                                                                                pagination.appendChild(first);
                                                                                if (totalPages > 6) {
                                                                                    const dots = document.createElement('span');
                                                                                    dots.textContent = '...';
                                                                                    pagination.appendChild(dots);
                                                                                }
                                                                                for (let i = totalPages - 4; i <= totalPages; i++) {
                                                                                    if (i === state.currentPage) {
                                                                                        const current = document.createElement('span');
                                                                                        current.className = 'current';
                                                                                        current.textContent = i;
                                                                                        pagination.appendChild(current);
                                                                                    } else {
                                                                                        const pageLink = document.createElement('a');
                                                                                        pageLink.href = '#';
                                                                                        pageLink.textContent = i;
                                                                                        pageLink.addEventListener('click', e => {
                                                                                            e.preventDefault();
                                                                                            state.currentPage = i;
                                                                                            updatePagination(entity);
                                                                                            updateTable(entity);
                                                                                        });
                                                                                        pagination.appendChild(pageLink);
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                const first = document.createElement('a');
                                                                                first.href = '#';
                                                                                first.textContent = '1';
                                                                                first.addEventListener('click', e => {
                                                                                    e.preventDefault();
                                                                                    state.currentPage = 1;
                                                                                    updatePagination(entity);
                                                                                    updateTable(entity);
                                                                                });
                                                                                pagination.appendChild(first);
                                                                                const dots1 = document.createElement('span');
                                                                                dots1.textContent = '...';
                                                                                pagination.appendChild(dots1);
                                                                                for (let i = state.currentPage - 1; i <= state.currentPage + 1; i++) {
                                                                                    if (i === state.currentPage) {
                                                                                        const current = document.createElement('span');
                                                                                        current.className = 'current';
                                                                                        current.textContent = i;
                                                                                        pagination.appendChild(current);
                                                                                    } else {
                                                                                        const pageLink = document.createElement('a');
                                                                                        pageLink.href = '#';
                                                                                        pageLink.textContent = i;
                                                                                        pageLink.addEventListener('click', e => {
                                                                                            e.preventDefault();
                                                                                            state.currentPage = i;
                                                                                            updatePagination(entity);
                                                                                            updateTable(entity);
                                                                                        });
                                                                                        pagination.appendChild(pageLink);
                                                                                    }
                                                                                }
                                                                                const dots2 = document.createElement('span');
                                                                                dots2.textContent = '...';
                                                                                pagination.appendChild(dots2);
                                                                                const last = document.createElement('a');
                                                                                last.href = '#';
                                                                                last.textContent = totalPages;
                                                                                last.addEventListener('click', e => {
                                                                                    e.preventDefault();
                                                                                    state.currentPage = totalPages;
                                                                                    updatePagination(entity);
                                                                                    updateTable(entity);
                                                                                });
                                                                                pagination.appendChild(last);
                                                                            }
                                                                        }
                                                                        if (state.currentPage < totalPages) {
                                                                            const next = document.createElement('a');
                                                                            next.href = '#';
                                                                            next.innerHTML = '<i class="fas fa-angle-right"></i>';
                                                                            next.addEventListener('click', e => {
                                                                                e.preventDefault();
                                                                                state.currentPage++;
                                                                                updatePagination(entity);
                                                                                updateTable(entity);
                                                                            });
                                                                            pagination.appendChild(next);
                                                                            const lastBtn = document.createElement('a');
                                                                            lastBtn.href = '#';
                                                                            lastBtn.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                                                                            lastBtn.addEventListener('click', e => {
                                                                                e.preventDefault();
                                                                                state.currentPage = totalPages;
                                                                                updatePagination(entity);
                                                                                updateTable(entity);
                                                                            });
                                                                            pagination.appendChild(lastBtn);
                                                                        } else {
                                                                            const disabledNext = document.createElement('span');
                                                                            disabledNext.innerHTML = '<i class="fas fa-angle-right"></i>';
                                                                            pagination.appendChild(disabledNext);
                                                                            const disabledLast = document.createElement('span');
                                                                            disabledLast.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                                                                            pagination.appendChild(disabledLast);
                                                                        }
                                                                    }

                                                                    
                                                                    function updateUrlToUnit() {
                                                                        const newUrl = window.location.origin + '/WarehouseManagement/unit';
                                                                        history.replaceState({}, '', newUrl);
                                                                    }

                                                                    // Lưu trạng thái tab hiện tại vào sessionStorage
                                                                    document.querySelectorAll('#unitTabs .nav-link').forEach(tab => {
                                                                        tab.addEventListener('click', function () {
                                                                            sessionStorage.setItem('activeTab', this.id);
                                                                        });
                                                                    });

                                                                    window.addEventListener('DOMContentLoaded', function () {
                                                                        const activeTabId = sessionStorage.getItem('activeTab') || 'conversions-tab';
                                                                        const activeTab = document.getElementById(activeTabId);
                                                                        if (activeTab) {
                                                                            const tab = new bootstrap.Tab(activeTab);
                                                                            tab.show();
                                                                        }

                                                                        
                                                                        const tabId = activeTabId.split('-')[0]; 
                                                                        updateTable(tabId);
                                                                        updatePagination(tabId);
                                                                    });
        </script>

        <!-- Hidden input to pass countConversions for JSP rendering -->
        <input type="hidden" id="countConversions" value="${countConversions}">
    </body>
</html>