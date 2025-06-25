<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
    @SuppressWarnings("unchecked")
    Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (perms == null) { perms = new HashMap<>(); }
    request.setAttribute("perms", perms);
%>
<%
    Users u = (Users) session.getAttribute("USER");
    if (u != null) {
        System.out.println("[DEBUG] USER in session: " + u.toString());
    } else {
        System.out.println("[DEBUG] USER in session is null");
    }
    int roleId = (u != null) ? u.getRoleId() : 0;
    System.out.println("[DEBUG] USER in ID: " + u.getRoleId());
    String roleName = (u != null) ? u.getRoleName() : "";
    session.setAttribute("roleId", roleId);
    session.setAttribute("roleName", roleName);
    pageContext.setAttribute("roleId", roleId);
    pageContext.setAttribute("roleName", roleName);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Inventory Audit List</title>
        <link rel="stylesheet" href="css/permissionlist.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="icon" href="img/logo.png" type="image/png">
        <link rel="stylesheet" href="css/style1.css" />
        <style>
            /* Tổng thể filter header */
            .filter-row {
                width: 100%;
                min-width: 0;
                flex-wrap: nowrap !important;
                gap: 12px;
            }
            .filter-form {
                flex-wrap: nowrap !important;
                width: 100%;
                min-width: 0;
            }
            .search-box input {
                border-left: none !important;
                border-radius: 0 4px 4px 0 !important;
            }
            .input-group-text {
                background: #fff !important;
                border-right: none !important;
            }
            .filter-form .form-control,
            .filter-form .form-select {
                min-width: 130px;
                max-width: 220px;
                border-radius: 4px !important;
                font-size: 15px;
            }
            .filter-form .form-control[type="date"] {
                max-width: 160px;
            }
            .filter-form button[type="submit"] {
                min-width: 90px;
                font-weight: 500;
                border-radius: 6px;
            }
            .btn-success#addAuditBtn {
                font-weight: 500;
                border-radius: 6px;
                min-width: 140px;
                padding-left: 18px;
                padding-right: 18px;
            }
            @media (max-width: 900px) {
                .header .filter-row, .header .filter-form {
                    flex-wrap: wrap !important;
                    gap: 8px;
                }
                .btn-success#addAuditBtn {
                    margin-top: 10px;
                    width: 100%;
                }
            }
        </style>

    </head>
    <body>
        <jsp:include page="sidebar.jsp" flush="true"/>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container">

                <!-- Bootstrap Alert Container -->
                <div class="bootstrap-alert-container" id="alertContainer"></div>

                <div class="header d-flex align-items-center justify-content-between flex-wrap mb-4" style="gap: 20px;">
                    <h1 class="title mb-0">Inventory Audit List</h1>
                    <div class="d-flex flex-grow-1 align-items-center filter-row" style="gap: 12px; min-width: 0;">
                        <form action="${pageContext.request.contextPath}/auditlist" method="get" class="d-flex flex-grow-1 align-items-center filter-form" style="gap: 12px; min-width: 0;">
                            <!-- Search -->
                            <div class="input-group search-box flex-grow-1" style="min-width: 200px;">
                                <span class="input-group-text bg-white"><i class="fa fa-search"></i></span>
                                <input type="text" name="code" value="${param.code}" placeholder="Search by audit code..." class="form-control border-start-0"/>
                            </div>
                            <!-- Date from -->
                            <input type="date" name="dateFrom" class="form-control" value="${param.dateFrom}" placeholder="From date" style="min-width: 140px;">
                            <!-- Date to -->
                            <input type="date" name="dateTo" class="form-control" value="${param.dateTo}" placeholder="To date" style="min-width: 140px;">
                            <!-- Status -->
                            <select name="status" class="form-select" style="min-width: 120px;">
                                <option value="all" ${empty param.status || param.status=='all' ? 'selected' : ''}>All Status</option>
                                <option value="draft" ${param.status=='draft' ? 'selected' : ''}>Draft</option>
                                <option value="completed" ${param.status=='completed' ? 'selected' : ''}>Completed</option>
                            </select>
                            <!-- Creator -->
                            <select name="createdBy" class="form-select" style="min-width: 120px;">
                                <option value="">All Creator</option>
                                <c:forEach var="u" items="${userList}">
                                    <option value="${u.userId}" ${param.createdBy == u.userId ? 'selected' : ''}>${u.fullName}</option>
                                </c:forEach>
                            </select>
                            <!-- Filter Button -->
                            <button type="submit" class="btn btn-primary px-4">Filter</button>
                        </form>
                        <c:if test="${perms['InventoryAudit_ADD']}">
                            <button id="btnOpenAuditModal" class="btn btn-success ms-2" style="white-space: nowrap;">
                                <i class="fa fa-plus me-1"></i> Add Audit
                            </button>
                        </c:if>
                    </div>
                </div>

                <!-- Audit modal -->
                <div class="modal fade" id="auditModal" tabindex="-1">

                    <div class="modal-dialog modal-xl modal-dialog-scrollable">
                        <div class="modal-content">
                            <div class="modal-header bg-info text-white">
                                <h5 class="modal-title"><i class="fa fa-clipboard-check"></i> Inventory Audit Record</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="auditModalBody">
                                <form id="inventoryAuditForm">
                                    <c:forEach var="item" items="${inventoryList}" varStatus="st">
                                        <div class="border rounded p-3 mb-3 inventory-item-row">
                                            <div class="row align-items-center">
                                                <div class="col-2"><b>ID:</b> ${item.materialId}</div>
                                                <div class="col-4"><b>Name:</b> ${item.materialName}
                                                    <c:if test="${not empty item.materialImage}">
                                                        <img src="${item.materialImage}" alt="Material Image" style="max-width:40px;max-height:40px;margin-left:8px;vertical-align:middle;"/>
                                                    </c:if>
                                                </div>
                                                <div class="col-3">
                                                    <b>System Quantity:</b>
                                                    <span class="sys-qty">${item.availableQty}</span>
                                                </div>
                                                <div class="col-3">
                                                    <input type="number" step="0.01" min="0"
                                                           class="form-control actual-qty-input"
                                                           placeholder="Enter actual quantity"
                                                           data-sysqty="${item.availableQty}"
                                                           name="actualQty_${item.materialId}" required="">
                                                </div>
                                            </div>
                                            <div class="row mt-2 d-none reason-row">
                                                <div class="col-12">
                                                    <input type="text" class="form-control reason-input"
                                                           name="reason_${item.materialId}"
                                                           placeholder="Enter reason for discrepancy" required="">
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" id="btnSaveAudit">Save Audit</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal chi tiết phiếu kiểm kê -->
                <div class="modal fade" id="auditDetailModal" tabindex="-1" aria-labelledby="auditDetailLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg modal-dialog-scrollable">
                        <div class="modal-content">
                            <div class="modal-header bg-info text-white">
                                <h5 class="modal-title" id="auditDetailLabel"><i class="fas fa-search"></i> Audit Detail</h5>

                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body" id="auditDetailBody">
                                <!-- Nội dung chi tiết sẽ được load bằng AJAX -->
                                <div class="text-center text-secondary"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>
                            </div>                           
                            <div class="modal-footer" id="auditDetailFooter">
                                <c:if test="${sessionScope.roleId == 2 && audit.status == 'draft'}">
                                    <button type="button" class="btn btn-danger" id="btnRejectAudit">
                                        <i class="fas fa-times"></i> Reject
                                    </button>
                                    <button type="button" class="btn btn-success" id="btnApproveAudit">
                                        <i class="fas fa-check"></i> Approve
                                    </button>
                                </c:if>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Modal Confirm Approve -->
                <div class="modal fade" id="confirmApproveModal" tabindex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirm Approval</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                Are you sure you want to approve this audit record?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" id="confirmApproveBtn" class="btn btn-success">Approve</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Stats -->
                <c:if test="${not empty auditList}">
                    <div class="stats-info">
                        <i class="fas fa-info-circle"></i>
                        <c:choose>
                            <c:when test="${not empty param.code}">
                                Found <strong>${totalAudit}</strong> for code "<strong>${param.code}</strong>"
                            </c:when>
                            <c:otherwise>
                                Showing <strong>${auditList.size()}</strong>
                                / <strong>${totalAudit}</strong> records
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <!-- Table -->
                <c:choose>
                    <c:when test="${not empty auditList}">
                        <div class="table-container">
                            <table class="table table-striped align-middle">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Audit code</th>
                                        <th>Created By</th>
                                        <th>Date</th>                                        
                                        <th>Status</th>
                                            <c:if test="${perms['InventoryAudit_VIEW']}">
                                            <th>Detail</th>
                                            </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="audit" items="${auditList}" varStatus="st">
                                        <tr>
                                            <td><strong>${(page-1)*pageSize + st.index + 1}</strong></td>
                                            <td>${audit.auditCode}</td>
                                            <td>${audit.createdByName}</td>

                                            <td>${audit.auditDate}</td>
                                            <td>
                                                <span class="badge ${audit.status == 'completed' ? 'bg-success' : 'bg-secondary'}">${audit.status}</span>
                                            </td>
                                            <!--<td>${audit.note}</td>-->
                                            <c:if test="${perms['InventoryAudit_VIEW']}">
                                                <td>
                                                    <button type="button"
                                                            class="btn btn-info btn-sm btn-view-audit"
                                                            data-id="${audit.id}"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#auditDetailModal">
                                                        <i class="fas fa-eye"></i> View
                                                    </button>
                                                </td>

                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-data">
                            <i class="fas fa-folder-open fa-3x"></i>
                            <h3>No audit record found</h3>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Pagination (reuse style Supplier page) -->
                <c:if test="${numPages > 1}">
                    <div class="pagination">
                        <!-- First / Prev -->
                        <c:url var="firstUrl" value="/auditlist">
                            <c:param name="page" value="1"/>
                            <c:if test="${not empty param.code}"><c:param name="code" value="${param.code}"/></c:if>
                            <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
                            <c:if test="${not empty param.dateFrom}"><c:param name="dateFrom" value="${param.dateFrom}"/></c:if>
                            <c:if test="${not empty param.dateTo}"><c:param name="dateTo" value="${param.dateTo}"/></c:if>
                            <c:if test="${not empty param.createdBy}"><c:param name="createdBy" value="${param.createdBy}"/></c:if>
                        </c:url>
                        <c:url var="prevUrl" value="/auditlist">
                            <c:param name="page" value="${page-1}"/>
                            <c:if test="${not empty param.code}"><c:param name="code" value="${param.code}"/></c:if>
                            <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
                            <c:if test="${not empty param.dateFrom}"><c:param name="dateFrom" value="${param.dateFrom}"/></c:if>
                            <c:if test="${not empty param.dateTo}"><c:param name="dateTo" value="${param.dateTo}"/></c:if>
                            <c:if test="${not empty param.createdBy}"><c:param name="createdBy" value="${param.createdBy}"/></c:if>
                        </c:url>
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="${firstUrl}" title="First"><i class="fas fa-angle-double-left"></i></a>
                                <a href="${prevUrl}"  title="Prev" ><i class="fas fa-angle-left"></i></a>
                                </c:when>
                                <c:otherwise>
                                <span class="disabled"><i class="fas fa-angle-double-left"></i></span>
                                <span class="disabled"><i class="fas fa-angle-left"></i></span>
                                </c:otherwise>
                            </c:choose>

                        <!-- Page numbers -->
                        <c:forEach begin="1" end="${numPages}" var="i">
                            <c:url var="pageUrl" value="/auditlist">
                                <c:param name="page" value="${i}"/>
                                <c:if test="${not empty param.code}"><c:param name="code" value="${param.code}"/></c:if>
                                <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
                                <c:if test="${not empty param.dateFrom}"><c:param name="dateFrom" value="${param.dateFrom}"/></c:if>
                                <c:if test="${not empty param.dateTo}"><c:param name="dateTo" value="${param.dateTo}"/></c:if>
                                <c:if test="${not empty param.createdBy}"><c:param name="createdBy" value="${param.createdBy}"/></c:if>
                            </c:url>
                            <c:choose>
                                <c:when test="${i == page}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageUrl}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <!-- Next / Last -->
                        <c:url var="nextUrl" value="/auditlist">
                            <c:param name="page" value="${page+1}"/>
                            <c:if test="${not empty param.code}"><c:param name="code" value="${param.code}"/></c:if>
                            <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
                            <c:if test="${not empty param.dateFrom}"><c:param name="dateFrom" value="${param.dateFrom}"/></c:if>
                            <c:if test="${not empty param.dateTo}"><c:param name="dateTo" value="${param.dateTo}"/></c:if>
                            <c:if test="${not empty param.createdBy}"><c:param name="createdBy" value="${param.createdBy}"/></c:if>
                        </c:url>
                        <c:url var="lastUrl" value="/auditlist">
                            <c:param name="page" value="${numPages}"/>
                            <c:if test="${not empty param.code}"><c:param name="code" value="${param.code}"/></c:if>
                            <c:if test="${not empty param.status}"><c:param name="status" value="${param.status}"/></c:if>
                            <c:if test="${not empty param.dateFrom}"><c:param name="dateFrom" value="${param.dateFrom}"/></c:if>
                            <c:if test="${not empty param.dateTo}"><c:param name="dateTo" value="${param.dateTo}"/></c:if>
                            <c:if test="${not empty param.createdBy}"><c:param name="createdBy" value="${param.createdBy}"/></c:if>
                        </c:url>
                        <c:choose>
                            <c:when test="${page < numPages}">
                                <a href="${nextUrl}" title="Next"><i class="fas fa-angle-right"></i></a>
                                <a href="${lastUrl}" title="Last"><i class="fas fa-angle-double-right"></i></a>
                                </c:when>
                                <c:otherwise>
                                <span class="disabled"><i class="fas fa-angle-right"></i></span>
                                <span class="disabled"><i class="fas fa-angle-double-right"></i></span>
                                </c:otherwise>
                            </c:choose>
                    </div>
                    <div class="page-info">
                        Page ${page} of ${numPages} (${totalAudit} total)
                    </div>
                </c:if>

            </div>
        </section>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            $(document).ready(function () {
                var currentAuditId = null;
                $('.btn-view-audit').on('click', function () {
                    currentAuditId = $(this).data('id');
                    var auditId = $(this).data('id');
                    console.log('auditId ne', auditId);
                    $('#auditDetailBody').html('<div class="text-center text-secondary"><i class="fas fa-spinner fa-spin"></i> Loading...</div>');
                    $.ajax({
                        url: '${pageContext.request.contextPath}/inventoryauditdetail?view=detail&auditId=' + auditId,
                        type: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            // data should be an object containing audit info & detail list
                            var html = '<div class="mb-3"><b>Audit Code:</b> ' + data.audit.auditCode + '</div>';
                            html += '<div class="mb-3"><b>Created By nè:</b> ' + data.audit.createdByName + '</div>';
                            html += '<div class="mb-3"><b>Audit Date:</b> ' + data.audit.auditDate + '</div>';
                            html += '<hr>';
                            html += '<h6>Audit Material Details:</h6>';
                            html += '<table class="table table-bordered">';
                            html += '<thead><tr><th>#</th><th>Material Name</th><th>System Quantity</th><th>Actual Quantity</th><th>Difference</th><th>Reason</th></tr></thead><tbody>';
                            data.details.forEach(function (item, idx) {
                                html += '<tr>';
                                html += '<td>' + (idx + 1) + '</td>';
                                html += '<td>' + item.materialName + '</td>';
                                html += '<td>' + item.systemQty + '</td>';
                                html += '<td>' + item.actualQty + '</td>';
                                html += '<td>' + item.difference + '</td>';
                                html += '<td>' + (item.reason ? item.reason : '') + '</td>';
                                html += '</tr>';
                            });
                            html += '</tbody></table>';
                            $('#auditDetailBody').html(html);
                        },
                        error: function (xhr) {
                            $('#auditDetailBody').html('<div class="alert alert-danger">Failed to load audit details!</div>');
                        }
                    });
                });

                // Gắn sự kiện Approve
                $('#auditDetailModal').on('click', '#btnApproveAudit', function () {
                    if (!currentAuditId) {
                        showAlert(false, "Cannot find Audit ID!");
                        return;
                    }
                    $('#confirmApproveModal').modal('show');
                });
                // Xác nhận approve trong modal
                $('#confirmApproveBtn').click(function () {
                    if (!currentAuditId) return;
                    $.ajax({
                        url: '${pageContext.request.contextPath}/approveaudit',
                        type: 'POST',
                        data: {auditId: currentAuditId},
                        success: function (data) {
                            showAlert(true, 'Approved successfully!');
                            $('#auditDetailModal').modal('hide');
                            $('#confirmApproveModal').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            showAlert(false, 'Approve failed!');
                            $('#confirmApproveModal').modal('hide');
                        }
                    });
                });
            });
        </script>
        <script>
            // Show the audit modal
            $('#btnOpenAuditModal').click(function () {
                $('#auditModal').modal('show');
            });

            // When entering actual quantity, check for discrepancy
            $('#auditModal').on('input', '.actual-qty-input', function () {
                var actual = parseFloat($(this).val()) || 0;
                var sys = parseFloat($(this).data('sysqty')) || 0;
                var $block = $(this).closest('.inventory-item-row');
                if (actual !== sys) {
                    $block.find('.reason-row').removeClass('d-none');
                    $block.find('.reason-input').attr('required', true);
                } else {
                    $block.find('.reason-row').addClass('d-none');
                    $block.find('.reason-input').val('').removeAttr('required');
                }
            });

            // Save audit (collect data, send via ajax, handle on backend)
            $('#btnSaveAudit').click(function () {
                let data = [];
                let valid = true;
                $('#inventoryAuditForm .inventory-item-row').each(function () {
                    let $row = $(this);

                    // Xóa lỗi cũ
                    $row.find('.is-invalid').removeClass('is-invalid');
                    $row.find('.invalid-feedback').remove();

                    let materialId = $row.find('.actual-qty-available-input').attr('name').split('_')[1];
                    let sysAvailable = parseFloat($row.find('.sys-qty-available').text()) || 0;
                    let actualAvailable = $row.find('.actual-qty-available-input').val();
                    let sysNotAvailable = parseFloat($row.find('.sys-qty-notavailable').text()) || 0;
                    let actualNotAvailable = $row.find('.actual-qty-notavailable-input').val();
                    let reason = $row.find('.reason-input').val();

                    // Validate actual available
                    if (actualAvailable === '') {
                        valid = false;
                        let $input = $row.find('.actual-qty-available-input');
                        $input.addClass('is-invalid');
                        $input.after('<div class="invalid-feedback">Required</div>');
                    }
                    // Validate actual not available
                    if (actualNotAvailable === '') {
                        valid = false;
                        let $input = $row.find('.actual-qty-notavailable-input');
                        $input.addClass('is-invalid');
                        $input.after('<div class="invalid-feedback">Required</div>');
                    }
                    // Validate reason if visible
                    if ($row.find('.reason-input').is(':visible') && reason.trim() === '') {
                        valid = false;
                        let $input = $row.find('.reason-input');
                        $input.addClass('is-invalid');
                        $input.after('<div class="invalid-feedback">Required</div>');
                    }

                    data.push({
                        materialId: materialId,
                        availableSystem: sysAvailable,
                        availableActual: parseFloat(actualAvailable) || 0,
                        notAvailableSystem: sysNotAvailable,
                        notAvailableActual: parseFloat(actualNotAvailable) || 0,
                        reason: reason
                    });
                });
                if (!valid) {
                    // Không submit nếu có lỗi
                    return;
                }
                // Send to backend via ajax (adjust url to your servlet)
                $.ajax({
                    url: 'inventoryaudit', // Create a servlet to handle this data
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        showAlert(true, 'Saved successfully!');
                        $('#auditModal').modal('hide');
                    },
                    error: function () {
                        showAlert(false, 'An error occurred!');
                    }
                });
            });
        </script>
        <script>
            $('#btnOpenAuditModal').click(function () {
                $('#auditModalBody').html('<div class="text-center text-secondary"><i class="fas fa-spinner fa-spin"></i> Loading...</div>');
                $.ajax({
                    url: '${pageContext.request.contextPath}/inventoryaudit?action=listMaterial',
                    type: 'GET',
                    dataType: 'json',
                    success: function (data) {
                        var html = '<form id="inventoryAuditForm">';
                        html += '<table class="table table-bordered align-middle text-center">';
                        html += '<thead class="table-light">';
                        html += '<tr>';
                        html += '<th style="width: 60px;">ID</th>';
                        html += '<th style="width: 70px;">Image</th>';
                        html += '<th>Name</th>';
                        html += '<th style="width: 110px;">Available (System)</th>';
                        html += '<th style="width: 90px;">Available (Actual)</th>';
                        html += '<th style="width: 110px;">Not Available (System)</th>';
                        html += '<th style="width: 90px;">Not Available (Actual)</th>';
                        html += '<th style="width: 220px;">Reason</th>';
                        html += '</tr>';
                        html += '</thead><tbody>';
                        data.forEach(function (item, idx) {
                            html += '<tr class="inventory-item-row">';
                            html += '<td>' + item.materialId + '</td>';
                            html += '<td>';
                            if (item.materialImage) {
                                html += '<img src="' + item.materialImage + '" alt="Material Image" style="max-width:40px;max-height:40px;vertical-align:middle;"/>';
                            }
                            html += '</td>';
                            html += '<td>' + item.materialName + '</td>';
                            html += '<td><span class="sys-qty-available">' + item.availableQty + '</span></td>';
                            html += '<td>';
                            html += '<input type="number" step="0.01" min="0" class="form-control actual-qty-available-input" style="max-width: 80px; margin: 0 auto; padding: 2px 4px; font-size: 14px;" placeholder="Actual" data-sysqty="' + item.availableQty + '" name="actualAvailable_' + item.materialId + '">';
                            html += '</td>';
                            html += '<td><span class="sys-qty-notavailable">' + item.notAvailableQty + '</span></td>';
                            html += '<td>';
                            html += '<input type="number" step="0.01" min="0" class="form-control actual-qty-notavailable-input" style="max-width: 80px; margin: 0 auto; padding: 2px 4px; font-size: 14px;" placeholder="Actual" data-sysqty="' + item.notAvailableQty + '" name="actualNotAvailable_' + item.materialId + '">';
                            html += '</td>';
                            html += '<td>';
                            html += '<input type="text" class="form-control reason-input d-none" style="min-width: 180px; padding: 2px 6px; font-size: 14px;" name="reason_' + item.materialId + '" placeholder="Enter reason for discrepancy">';
                            html += '</td>';
                            html += '</tr>';
                        });
                        html += '</tbody></table></form>';
                        $('#auditModalBody').html(html);
                    },
                    error: function (xhr) {
                        $('#auditModalBody').html('<div class="alert alert-danger">Failed to load inventory!</div>');
                    }
                });
                $('#auditModal').modal('show');
            });

            // Show/hide reason input if actual differs from system (for both available and not available)
            $('#auditModal').on('input', '.actual-qty-available-input, .actual-qty-notavailable-input', function () {
                var $row = $(this).closest('tr');
                var sysAvailable = parseFloat($row.find('.sys-qty-available').text()) || 0;
                var actualAvailable = parseFloat($row.find('.actual-qty-available-input').val()) || 0;
                var sysNotAvailable = parseFloat($row.find('.sys-qty-notavailable').text()) || 0;
                var actualNotAvailable = parseFloat($row.find('.actual-qty-notavailable-input').val()) || 0;
                var $reason = $row.find('.reason-input');
                if (actualAvailable !== sysAvailable || actualNotAvailable !== sysNotAvailable) {
                    $reason.removeClass('d-none').attr('required', true);
                } else {
                    $reason.addClass('d-none').val('').removeAttr('required');
                }
            });
        </script>
        <script>
            /**
             * Hàm showAlert(status, message):
             *   - status = true  → alert màu xanh (alert-success)
             *   - status = false → alert màu đỏ  (alert-danger)
             * alert sẽ nằm cố định bên phải, tự đóng sau 4s
             */
            function showAlert(status, message) {
                // Xóa alert cũ nếu có
                const existingAlert = document.querySelector('.custom-alert');
                if (existingAlert) {
                    existingAlert.remove();
                }
                console.log('here', status);
                // Tạo alert mới
                const alertDiv = document.createElement('div');
                if (status === true) {
                    alertDiv.className = `alert alert-success alert-dismissible fade show custom-alert`;

                } else {
                    alertDiv.className = `alert alert-danger alert-dismissible fade show custom-alert`;

                }
                alertDiv.setAttribute('role', 'alert');
                alertDiv.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    min-width: 300px;
                    z-index: 9999;
                    padding: 1rem;
                    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
                    border-radius: 0.375rem;
                `;

                // Thêm icon
                const icon = document.createElement('i');

                icon.className = `fas ${status ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2`;
                alertDiv.appendChild(icon);

                // Thêm message
                const messageText = document.createTextNode(message);
                alertDiv.appendChild(messageText);



                // Thêm vào body
                document.body.appendChild(alertDiv);

                // Animation khi hiện alert
                setTimeout(() => {
                    alertDiv.style.opacity = '1';
                }, 100);

//                 Tự động đóng sau 4s
                setTimeout(() => {
                    if (alertDiv && document.body.contains(alertDiv)) {
                        alertDiv.classList.remove('show');
                        setTimeout(() => alertDiv.remove(), 150);
                    }
                }, 4000);
            }
        </script>

    </body>
</html>
