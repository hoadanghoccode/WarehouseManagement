<%-- 
    Document   : purchaseOrderDetail
    Created on : Jun 29, 2025, 2:33:44 AM
    Author     : legia
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Purchase Order Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
        <style>
            body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .main-layout { display: flex; min-height: 100vh; }
            .main-content { flex: 1; margin-left: 260px; padding: 0; background-color: #f8f9fa; min-height: 100vh; transition: margin-left 0.3s ease; }
            .container { max-width: none; padding: 30px; margin: 0; }
            .title { font-size: 28px; font-weight: 600; color: #1f2937; margin-bottom: 16px; }
            .card { background-color: white; border-radius: 12px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08); margin-bottom: 24px; }
            .card-header { background-color: #f3f4f6; padding: 16px; border-bottom: 1px solid #e5e7eb; font-weight: 600; font-size: 18px; }
            .card-body { padding: 16px; }
            .info-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #e5e7eb; }
            .info-label { font-weight: 500; color: #1f2937; width: 40%; }
            .info-value { color: #374151; width: 60%; }
            .status-badge { padding: 4px 8px; border-radius: 12px; font-size: 12px; }
            .bg-success { background-color: #d1fae5; color: #065f46; }
            .bg-warning { background-color: #fef3c7; color: #92400e; }
            .bg-danger { background-color: #fee2e2; color: #991b1b; }
            .bg-info { background-color: #e0f2fe; color: #1e40af; }
            .table-container { overflow-x: auto; }
            table.table { width: 100%; border-collapse: collapse; min-width: 600px; }
            table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; }
            table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; }
            table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
            table.table tbody tr:hover { background-color: #eef2ff; }
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
                    <a href="list-purchase-order" class="btn btn-secondary">Cancel</a>
                    <h1 class="title">Purchase Order Detail</h1>

                    <div class="card">
                        <div class="card-header">Purchase Order Information</div>
                        <div class="card-body">
                            <div class="info-row">
                                <span class="info-label">Purchase Order ID</span>
                                <span class="info-value">${purchaseOrder.purchaseOrderId}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Order ID</span>
                                <span class="info-value">${purchaseOrder.orderId}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Warehouse</span>
                                <span class="info-value">
                                    <c:forEach var="wh" items="${warehouses}">
                                        <c:if test="${wh.warehouseId == purchaseOrder.warehouseId}">${wh.name}</c:if>
                                    </c:forEach>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">User ID</span>
                                <span class="info-value">${purchaseOrder.userId}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Created At</span>
                                <span class="info-value">${purchaseOrder.createdAt}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Status</span>
                                <span class="info-value">
                                    <span class="status-badge ${purchaseOrder.status == 'approved' ? 'bg-success' : purchaseOrder.status == 'rejected' ? 'bg-danger' : purchaseOrder.status == 'pending' ? 'bg-warning' : purchaseOrder.status == 'purchased' ? 'bg-primary' : 'bg-info'}">
                                        ${purchaseOrder.status}
                                    </span>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Supplier</span>
                                <span class="info-value">
                                    <c:forEach var="sup" items="${suppliers}">
                                        <c:if test="${sup.supplierId == purchaseOrder.supplierId}">${sup.name}</c:if>
                                    </c:forEach>
                                    <c:if test="${empty purchaseOrder.supplierId}">N/A</c:if>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Note</span>
                                <span class="info-value">${purchaseOrder.note}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Last Updated</span>
                                <span class="info-value">${purchaseOrder.lastUpdated}</span>
                            </div>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">Purchase Order Details</div>
                        <div class="card-body">
                            <div class="table-container">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Purchase Order Detail ID</th>
                                            <th>Material Name</th>
                                            <th>Quality Name</th>
                                            <th>Quantity</th>
                                            <th>Price</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${empty purchaseOrder.purchaseOrderDetails}">
                                            <tr><td colspan="6">No details available.</td></tr>
                                        </c:if>
                                        <c:forEach var="detail" items="${purchaseOrder.purchaseOrderDetails}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${detail.purchaseOrderDetailId}</td>
                                                <td>${detail.materialName != null ? detail.materialName : 'N/A'}</td>
                                                <td>${detail.qualityName != null ? detail.qualityName : 'N/A'}</td>
                                                <td>${detail.quantity}</td>
                                                <td>${detail.price != null ? detail.price : 'N/A'}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <c:if test="${purchaseOrder.status == 'pending' && currentUser.roleId == 2}">
                        <div class="card">
                            <div class="card-header">Director Actions</div>
                            <div class="card-body">
                                <form id="actionForm" action="purchase-order-detail" method="post">
                                    <input type="hidden" name="id" value="${purchaseOrder.purchaseOrderId}">
                                    <input type="hidden" id="actionInput" name="action" value="">
                                    <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#confirmModal" data-action="approve">Approve</button>
                                    <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmModal" data-action="reject">Reject</button>
                                </form>
                            </div>
                        </div>
                    </c:if>

                    <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="confirmModalLabel">Confirm Action</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to <span id="confirmAction"></span> this purchase order?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    <button type="button" class="btn btn-primary" id="confirmButton">Confirm</button>
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
                                const action = $(this).data('action');
                                $('#confirmAction').text(action);
                                $('#actionInput').val(action);
                            });

                            $('#confirmButton').on('click', function () {
                                $('#actionForm').submit();
                            });
                        });
                    </script>
                </div>
            </div>
        </div>
    </body>
</html>