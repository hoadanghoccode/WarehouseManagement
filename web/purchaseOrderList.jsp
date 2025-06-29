<%-- 
    Document   : purchaseOrderList
    Created on : Jun 29, 2025, 2:32:38 AM
    Author     : legia
--%>

<%@ page import="java.util.List" %>
<%@ page import="model.PurchaseOrders" %>
<%@ page import="model.Warehouse" %>
<%@ page import="model.Supplier" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Purchase Orders List</title>
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
            .no-data {
                text-align: center;
                padding: 24px;
                background-color: #f3f4f6;
                border-radius: 12px;
                font-size: 16px;
                color: #6b7280;
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
                    <h1 class="title">Purchase Orders List</h1>

                    <div class="header-actions">
                        <div class="search-container">
                            <form action="list-purchase-order" method="get" id="filterForm" style="display: flex; gap: 12px; align-items: center;">
                                <div style="position: relative;">
                                    <i class="fas fa-search search-icon"></i>
                                    <input type="text" name="usernameSearch" value="${usernameSearch}" placeholder="Search by username..." class="search-input" />
                                </div>
                                <select class="form-select" name="warehouseId" id="warehouseFilter">
                                    <option value="">All Warehouses</option>
                                    <c:forEach items="${warehouses}" var="warehouse">
                                        <option value="${warehouse.warehouseId}" ${warehouseId == warehouse.warehouseId ? 'selected' : ''}>
                                            ${warehouse.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <select class="form-select" name="supplierId" id="supplierFilter">
                                    <option value="">All Suppliers</option>
                                    <c:forEach items="${suppliers}" var="supplier">
                                        <option value="${supplier.supplierId}" ${supplierId == supplier.supplierId ? 'selected' : ''}>
                                            ${supplier.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <select class="form-select" name="status" id="statusFilter">
                                    <option value="">All Status</option>
                                    <option value="new" ${status == 'new' ? 'selected' : ''}>New</option>
                                    <option value="pending" ${status == 'pending' ? 'selected' : ''}>Pending</option>
                                    <option value="approved" ${status == 'approved' ? 'selected' : ''}>Approved</option>
                                    <option value="rejected" ${status == 'rejected' ? 'selected' : ''}>Rejected</option>
                                    <option value="purchased" ${status == 'purchased' ? 'selected' : ''}>Purchased</option>
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
                                    <th class="col-md-2">Warehouse</th>
                                    <th class="col-md-2">User Name</th>
                                    <th class="col-md-2">Supplier</th>
                                    <th class="col-md-2">Status</th>
                                    <th class="col-md-3">Note</th>
                                    <th class="col-md-2">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="po" items="${purchaseOrders}" varStatus="status">
                                    <tr>
                                        <td><strong>${status.index + 1 + (page - 1) * 5}</strong></td>
                                        <td>${po.orderId}</td>
                                        <td>
                                            <c:forEach var="wh" items="${warehouses}">
                                                <c:if test="${wh.warehouseId == po.warehouseId}">${wh.name}</c:if>
                                            </c:forEach>
                                        </td>
                                        <td>${po.fullName != null ? po.fullName : 'N/A'}</td>
                                        <td>
                                            <c:forEach var="sup" items="${suppliers}">
                                                <c:if test="${sup.supplierId == po.supplierId}">${sup.name}</c:if>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="badge ${po.status == 'approved' ? 'bg-success' : po.status == 'rejected' ? 'bg-danger' : po.status == 'pending' ? 'bg-warning' : po.status == 'purchased' ? 'bg-primary' : 'bg-info'}">
                                                ${po.status}
                                            </span>
                                        </td>
                                        <td>${po.note}</td>
                                        <td>
                                            <div class="action-buttons">
                                                <a href="purchase-order-detail?id=${po.purchaseOrderId}" class="btn btn-info btn-sm" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <c:if test="${po.status != 'approved' && po.status != 'purchased' && currentUser.roleId == 4}">
                                                    <a href="update-purchase-order?id=${po.purchaseOrderId}" class="btn btn-primary btn-sm" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                </c:if>
                                                <c:if test="${po.status == 'approved' && currentUser.roleId == 4}">
                                                    <button type="button" class="btn btn-success btn-sm" title="Mark as Purchased" data-bs-toggle="modal" data-bs-target="#confirmPurchasedModal" data-id="${po.purchaseOrderId}">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="pagination">
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="list-purchase-order?page=1&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="list-purchase-order?page=${page-1}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </span>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Previous page">
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
                                            <a href="list-purchase-order?page=${i}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${i}</a>
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
                                                    <a href="list-purchase-order?page=${i}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                            <a href="list-purchase-order?page=${totalPages}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${totalPages}</a>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${page >= totalPages - 3}">
                                        <a href="list-purchase-order?page=1&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">1</a>
                                        <c:if test="${totalPages > 6}">
                                            <span style="padding: 8px 4px;">...</span>
                                        </c:if>
                                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="list-purchase-order?page=${i}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="list-purchase-order?page=1&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                        <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}">
                                                    <span class="current">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="list-purchase-order?page=${i}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="list-purchase-order?page=${totalPages}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="list-purchase-order?page=${page+1}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="list-purchase-order?page=${totalPages}&usernameSearch=${usernameSearch}&warehouseId=${warehouseId}&supplierId=${supplierId}&status=${status}" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                                <span style="opacity: 0.3; cursor: not-allowed;" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages}
                        <c:if test="${not empty totalSize}">
                            (${totalSize} total purchase orders)
                        </c:if>
                    </div>

                    <div class="modal fade" id="confirmPurchasedModal" tabindex="-1" aria-labelledby="confirmPurchasedModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="confirmPurchasedModalLabel">Confirm Mark as Purchased</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to mark this purchase order as purchased?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    <button type="button" class="btn btn-primary" id="confirmPurchasedButton">Confirm</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="position-fixed top-0 end-0 p-3" style="z-index:1080;">
                        <div id="actionToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body" id="actionToastBody"></div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                            </div>
                        </div>
                    </div>

                    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script>
                        $(function () {
                            const params = new URLSearchParams(window.location.search);
                            if (params.has('success') || params.has('error')) {
                                const isSuccess = params.has('success');
                                const msg = params.get(isSuccess ? 'success' : 'error');
                                const toastEl = document.getElementById('actionToast');
                                toastEl.classList.remove('text-bg-success', 'text-bg-danger');
                                toastEl.classList.add(isSuccess ? 'text-bg-success' : 'text-bg-danger');
                                document.getElementById('actionToastBody').textContent = msg;
                                new bootstrap.Toast(toastEl).show();
                                history.replaceState(null, '', location.pathname);
                            }

                            $('[data-bs-toggle="modal"]').on('click', function () {
                                const id = $(this).data('id');
                                $('#confirmPurchasedButton').data('id', id);
                            });

                            $('#confirmPurchasedButton').on('click', function () {
                                const id = $(this).data('id');
                                const form = $('<form>', {
                                    'method': 'POST',
                                    'action': 'mark-purchased'
                                }).append($('<input>', {
                                    'type': 'hidden',
                                    'name': 'id',
                                    'value': id
                                }));
                                $('body').append(form);
                                form.submit();
                            });
                        });
                    </script>
                </div>
            </div>
        </div>
    </body>
</html>