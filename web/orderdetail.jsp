<%-- 
    Document   : orderdetail
    Created on : Jun 9, 2025, 12:36:46 PM
    Author     : ADMIN
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Detail - Order #${order != null ? order.orderId : 'N/A'}</title>

        <!-- CSS Libraries -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <link rel="stylesheet" href="css/metisMenu.css">
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <link rel="stylesheet" type="text/css" href="css/orderdetail.css" />

    </head>

    <body>
        <!-- Include navbar and sidebar -->
        <%@ include file="navbar.jsp" %>
        <div class="main-layout">
            <%@ include file="sidebar.jsp" %>

            <div class="main-content">
                <div class="container">
                    <c:choose>
                        <c:when test="${error == true}">
                            <div class="card">
                                <div class="card-body">
                                    <div class="error-state">
                                        <i class="fas fa-exclamation-triangle error-icon"></i>
                                        <h3>Unable to display order details</h3>
                                        <p><strong>Error:</strong> ${errorMessage}</p>
                                        <c:if test="${errorCode != null}">
                                            <p><small>Error Code: ${errorCode}</small></p>
                                        </c:if>
                                        <div style="margin-top: 24px;">
                                            <a href="javascript:history.back()" class="btn btn-secondary">
                                                <i class="fas fa-arrow-left"></i> Go Back
                                            </a>
                                            <a href="orderlist" class="btn btn-primary">
                                                <i class="fas fa-list"></i> View All Orders
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${order != null}">

                            <!-- Page Header -->
                            <div class="page-header">
                                <div class="order-title">
                                    <h1 class="order-id">Order ID: ${order.orderId}</h1>
                                    <div class="order-date">
                                        <c:choose>
                                            <c:when test="${order.createdAt != null}">
                                                <fmt:formatDate value="${order.createdAt}" pattern="MMMM d, yyyy 'at' h:mm a"/>
                                            </c:when>
                                            <c:otherwise>
                                                Order Date: N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="status-badges" style="margin-right: 20px">
                                    <c:choose>
                                        <c:when test="${order.status == 'pending'}">
                                            <span class="status-badge status-payment-pending">Pending</span>
                                        </c:when>
                                        <c:when test="${order.status == 'approved'}">
                                            <span class="status-badge status-approved">Approved</span>
                                        </c:when>
                                        <c:when test="${order.status == 'rejected'}">
                                            <span class="status-badge status-rejected">Rejected</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge">${order.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="order-actions">
                                    <c:choose>
                                        <c:when test="${currentUser.userId == owner.userId && order.status == 'pending'}">
                                            <a href="editorder?orderId=${order.orderId}" class="btn btn-primary">
                                                <i class="fas fa-edit"></i> Edit Order
                                            </a>
                                        </c:when>
                                    </c:choose>

                                    <a href="createorder" class="create-order-btn">
                                        <i class="fas fa-plus"></i> Create Order
                                    </a>
                                </div>
                            </div>

                            <div class="content-grid">
                                <!-- Order Items -->
                                <div class="card order-items">
                                    <div class="card-header">
                                        <h3 class="card-title">
                                            <i class="fas fa-box"></i>
                                            Order Items
                                        </h3>
                                    </div>
                                    <div class="card-body">
                                        <c:choose>
                                            <c:when test="${order.orderDetails != null && !order.orderDetails.isEmpty()}">
                                                <c:set var="totalItems" value="0" />
                                                <c:set var="totalQuantity" value="0" />

                                                <c:forEach var="detail" items="${detail}">
                                                    <c:set var="totalItems" value="${totalItems + 1}" />
                                                    <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}" />

                                                    <div class="order-item">
                                                        <div class="item-image">
                                                            <c:choose>
                                                                <c:when test="${detail.materialImage != null}">
                                                                    <img src=${detail.materialImage} width="60px" height="60px"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <i class="fas fa-cube" style="color: #64748b;"></i>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div class="item-details">
                                                            <div class="item-name">
                                                                <c:choose>
                                                                    <c:when test="${detail.materialId != null && detail.materialId > 0}">
                                                                        Material ID: ${detail.materialId}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Material: N/A
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div class="item-name">
                                                                <c:choose>
                                                                    <c:when test="${detail.materialId != null && detail.materialId > 0}">
                                                                        ${detail.materialName}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Material: N/A
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div class="item-specs">
                                                                <c:choose>
                                                                    <c:when test="${detail.subUnitId != null && detail.subUnitId > 0}">
                                                                        Unit: ${detail.subUnitName}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Unit: N/A
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:if test="${detail.qualityId != null && detail.qualityId > 0}">
                                                                    | Quality: ${detail.qualityId == 1 ? "Available" : "Broken"}
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                        <div class="item-quantity">
                                                            <span class="quantity-label">Quantity:</span>
                                                            <span class="quantity-value">
                                                                <fmt:formatNumber value="${detail.quantity}" type="number"/> ${detail.subUnitName}
                                                            </span>
                                                        </div>
                                                    </div>
                                                </c:forEach>

                                                <!-- Order Summary Section -->
                                                <div class="order-summary">
                                                    <div class="summary-row">
                                                        <span class="summary-label">
                                                            <i class="fas fa-boxes"></i> Total Items
                                                        </span>
                                                        <span class="summary-value">
                                                            <fmt:formatNumber value="${totalItems}" type="number"/> items
                                                        </span>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="no-items">
                                                    <i class="fas fa-inbox" style="font-size: 48px; color: #dee2e6; margin-bottom: 16px;"></i>
                                                    <h5>No Items Found</h5>
                                                    <p style="color: #6c757d;">This order doesn't contain any items.</p>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <!-- Right side container -->
                                <div>
                                    <!-- Admin Actions Card - Only show for admin on pending orders -->
                                    <c:if test="${currentUser.roleId == 2 && order.status == 'pending'}">

                                        <div class="card" style="margin-bottom: 24px;">
                                            <div class="card-header">
                                                <h3 class="card-title">
                                                    <i class="fas fa-user-shield"></i> Admin Actions
                                                </h3>
                                            </div>
                                            <div class="card-body">
                                                <div style="margin-bottom: 20px;">
                                                    <label for="adminNote" style="display: block; margin-bottom: 8px; font-weight: 600; color: #374151;">
                                                        <i class="fas fa-sticky-note"></i> Admin Note (Optional)
                                                    </label>
                                                    <textarea 
                                                        name="adminNote" 
                                                        id="adminNote" 
                                                        rows="3" 
                                                        placeholder="Add a note about your decision (optional)..."
                                                        style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 8px; font-size: 14px; resize: vertical; font-family: inherit;"
                                                        ></textarea>
                                                </div>

                                                <div class="admin-actions">
                                                    <!-- Approve -->
                                                    <button type="button" class="btn btn-success" onclick="openApproveModal()">
                                                        <i class="fas fa-check-circle"></i> Approve Order
                                                    </button>

                                                    <!-- Reject -->
                                                    <button type="button" class="btn btn-danger" onclick="openRejectModal()">
                                                        <i class="fas fa-times-circle"></i> Reject Order
                                                    </button>

                                                </div>

                                                <div style="margin-top: 16px; padding: 12px; background: #f8fafc; border-radius: 8px; border-left: 4px solid #3b82f6;">
                                                    <small style="color: #64748b;">
                                                        <i class="fas fa-info-circle"></i> 
                                                        Once you approve or reject this order, the action cannot be undone. The order owner will be notified.
                                                    </small>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>

                                    <!-- Customer Information -->
                                    <div class="card">
                                        <div class="card-header">
                                            <h3 class="card-title">
                                                <i class="fas fa-user"></i>
                                                Owner Information
                                            </h3>
                                        </div>
                                        <div class="card-body">
                                            <c:choose>
                                                <c:when test="${owner != null}">
                                                    <div class="customer-info">
                                                        <div class="customer-avatar">
                                                            <c:choose>
                                                                <c:when test="${owner.image != null && !empty owner.image && owner.image != ''}">
                                                                    <img src="${owner.image}" alt="User Avatar" class="avatar" 
                                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                                                    <div style="display: none;">
                                                                        <i class="fas fa-user"></i>
                                                                    </div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <i class="fas fa-user"></i>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div class="customer-details">
                                                            <h5>
                                                                <c:choose>
                                                                    <c:when test="${owner.fullName != null && !empty owner.fullName && owner.fullName.trim().length() > 0}">
                                                                        ${owner.fullName}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Unknown User
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </h5>

                                                            <c:if test="${owner.email != null && !empty owner.email && owner.email.trim().length() > 0}">
                                                                <p style="color: #64748b; font-size: 14px;">
                                                                    <i class="fas fa-envelope"></i> ${owner.email}
                                                                </p>
                                                            </c:if>
                                                            <c:if test="${owner.phoneNumber != null && !empty owner.phoneNumber && owner.phoneNumber.trim().length() > 0}">
                                                                <p style="color: #64748b; font-size: 14px;">
                                                                    <i class="fas fa-phone"></i> ${owner.phoneNumber}
                                                                </p>
                                                            </c:if>

                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="customer-info">
                                                        <div class="customer-avatar">
                                                            <i class="fas fa-user-slash"></i>
                                                        </div>
                                                        <div class="customer-details">
                                                            <h5>User Not Found</h5>
                                                            <p>User information is not available</p>
                                                            <p style="color: #dc3545; font-size: 14px;">
                                                                <i class="fas fa-exclamation-triangle"></i> 
                                                                User ID ${order.userId} not found in system
                                                            </p>
                                                        </div>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                            <!-- Order Information Section -->
                                            <div class="info-section" style="margin-top: 20px; padding-top: 20px; border-top: 1px solid #e9ecef;">
                                                <h6 style="margin-bottom: 15px; font-weight: 600;">Order Details</h6>

                                                <div class="info-row">
                                                    <span class="info-label">Order ID</span>
                                                    <span class="info-value">#${order.orderId}</span>
                                                </div>

                                                <c:if test="${order.warehouseId != null && order.warehouseId > 0}">
                                                    <div class="info-row">
                                                        <span class="info-label">Warehouse </span>
                                                        <span class="info-value">Kho Hà Nội 1</span>
                                                    </div>
                                                </c:if>
                                                <div class="info-row">
                                                    <span class="info-label">Order Type</span>
                                                    <span class="info-value">
                                                        <c:choose>
                                                            <c:when test="${order.type == '0' || order.type == 'import'}">
                                                                <span class="status-badge type-import">Import</span>
                                                            </c:when>
                                                            <c:when test="${order.type == '1' || order.type == 'export'}">
                                                                <span class="status-badge type-export">Export</span>
                                                            </c:when>
                                                            <c:when test="${order.type == 'purchase'}">
                                                                <span class="status-badge type-purchase">Purchase</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="status-badge status-pending">Repair</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>

                                                <div class="info-row">
                                                    <span class="info-label">Status</span>
                                                    <span class="info-value">
                                                        <c:choose>
                                                            <c:when test="${order.status == 'pending'}">
                                                                <span class="status-badge status-pending">Pending</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'approved'}">
                                                                <span class="status-badge status-approved">Approved</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'rejected'}">
                                                                <span class="status-badge status-rejected">Rejected</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="status-badge">${order.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>

                                                <c:if test="${order.note != null && !empty order.note && order.note.trim().length() > 0}">
                                                    <div class="info-row">
                                                        <span class="info-label">Note</span>
                                                        <span class="info-value">${order.note}</span>
                                                    </div>
                                                </c:if>

                                                <c:if test="${order.createdAt != null}">
                                                    <div class="info-row">
                                                        <span class="info-label">Created Date</span>
                                                        <span class="info-value">
                                                            <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                        </span>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="card">
                                <div class="card-body">
                                    <div class="error-state">
                                        <i class="fas fa-search error-icon"></i>
                                        <h3>Order Not Found</h3>
                                        <p>The requested order could not be found or does not exist.</p>
                                        <div style="margin-top: 24px;">
                                            <a href="javascript:history.back()" class="btn btn-secondary">
                                                <i class="fas fa-arrow-left"></i> Go Back
                                            </a>
                                            <a href="orderlist" class="btn btn-primary">
                                                <i class="fas fa-list"></i> View All Orders
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Approve Order Modal -->
        <div id="approveOrderModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Approve Order</h2>
                    <span class="close" onclick="closeModal('approveOrderModal')">&times;</span>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to approve this order?</p>
                    <div class="warning-box">
                        <div class="warning-header">
                            <i class="fas fa-check-circle warning-icon" style="color: #10b981;"></i>
                            <span class="warning-title" style="color: #047857;">Confirmation</span>
                        </div>
                        <div class="warning-content" style="color: #047857;">
                            This action will approve the order and notify the order owner. This action cannot be undone.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <form action="orderdetail" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="approve"/>
                        <input type="hidden" name="orderId" value="${order.orderId}"/>
                        <input type="hidden" name="adminNote" id="approveAdminNote" value=""/>
                        <button type="submit" class="btn btn-blue">Yes, Approve Order</button>
                    </form>
                    <button type="button" class="btn btn-gray" onclick="closeModal('approveOrderModal')">Cancel</button>
                </div>
            </div>
        </div>

        <!-- Reject Order Modal -->
        <div id="rejectOrderModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Reject Order</h2>
                    <span class="close" onclick="closeModal('rejectOrderModal')">&times;</span>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to reject this order?</p>
                    <div class="warning-box">
                        <div class="warning-header">
                            <i class="fas fa-exclamation-triangle warning-icon" style="color: #ef4444;"></i>
                            <span class="warning-title" style="color: #dc2626;">Warning</span>
                        </div>
                        <div class="warning-content" style="color: #dc2626;">
                            This action will reject the order and notify the order owner. This action cannot be undone.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <form action="orderdetail" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="reject"/>
                        <input type="hidden" name="orderId" value="${order.orderId}"/>
                        <input type="hidden" name="adminNote" id="rejectAdminNote" value=""/>
                        <button type="submit" class="btn" style="background-color: #ef4444; color: white;">Yes, Reject Order</button>
                    </form>
                    <button type="button" class="btn btn-gray" onclick="closeModal('rejectOrderModal')">Cancel</button>
                </div>
            </div>
        </div>

        <!-- JavaScript Libraries -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="vendors/datatable/js/jquery.dataTables.min.js"></script>
        <script src="vendors/datatable/js/dataTables.responsive.min.js"></script>
        <script src="vendors/datatable/js/dataTables.buttons.min.js"></script>
        <script src="js/metisMenu.js"></script>
        <script src="js/custom.js"></script>


        <script src="js/orderdetail.js"></script>


    </body>
</html>