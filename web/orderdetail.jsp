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
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <!-- datatable CSS -->
        <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
        <!-- text editor css -->
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <!-- morris css -->
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <!-- metarial icon css -->
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <link rel="stylesheet" type="text/css" href="css/orderdetail.css" />
    </head>
    <body>
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
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
                            <button class="btn btn-primary">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-success">
                                <i class="fas fa-plus"></i> Create Order
                            </button>
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
                                                    <i class="fas fa-cube" style="color: #64748b;"></i>
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

                        <!-- Customer & Order Information -->
                        <div>
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
                                                <span class="info-label">Warehouse ID</span>
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
                                                    <c:otherwise>
                                                        <span class="status-badge">Type: ${order.type}</span>
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

                                        <div class="info-row">
                                            <span class="info-label">Supplier</span>
                                            <span class="info-value">
                                                <c:choose>
                                                    <c:when test="${order.supplier != null && order.supplier > 0}">
                                                        ${supplier.name}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color: #6c757d; font-style: italic;">N/A</span>
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

                                        <c:if test="${order.updatedAt != null}">
                                            <div class="info-row">
                                                <span class="info-label">Last Updated</span>
                                                <span class="info-value">
                                                    <fmt:formatDate value="${order.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
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

        <!-- Scripts -->
        <script src="vendors/jquery/jquery-3.2.1.min.js"></script>
        <script src="vendors/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="vendors/datatable/js/jquery.dataTables.min.js"></script>
        <script src="vendors/datatable/js/dataTables.responsive.dataTables.min.js"></script>
        <script src="vendors/datatable/js/dataTables.buttons.min.js"></script>
        <script src="js/metisMenu.js"></script>
        <script src="js/custom.js"></script>
    </body>
</html>