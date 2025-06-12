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
        <title>Order Detail - Order #${order.orderId}</title>
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
                                        <c:set var="grandTotal" value="0" />
                                        <c:forEach var="detail" items="${order.orderDetails}">
                                            <c:set var="totalPrice" value="${detail.quantity * detail.unitPrice}" />
                                            <c:set var="grandTotal" value="${grandTotal + totalPrice}" />
                                            <div class="order-item">
                                                <div class="item-image">
                                                    <i class="fas fa-cube" style="color: #64748b;"></i>
                                                </div>
                                                <div class="item-details">
                                                    <div class="item-name">
                                                        <c:choose>
                                                            <c:when test="${detail.materialId != null}">
                                                                Material ID: ${detail.materialId}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Material: N/A
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                    <h5>Hello</h5>
                                                    <div class="item-specs">
                                                        <c:choose>
                                                            <c:when test="${detail.unitId != null}">
                                                                Unit: ${detail.unitId}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Unit: N/A
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                                <div class="item-quantity">
                                                    <fmt:formatNumber value="${detail.quantity}" type="number"/> x 
                                                    <fmt:formatNumber value="${detail.unitPrice}" type="currency" currencySymbol="₫"/>
                                                </div>
                                                <div class="item-price">
                                                    <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="₫"/>
                                                </div>
                                            </div>
                                        </c:forEach>

                                        <div class="total-section">
                                            <div class="total-row">
                                                <span>Subtotal</span>
                                                <span><fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/></span>
                                            </div>
                                            <div class="total-row">
                                                <span>Tax</span>
                                                <span>₫0.00</span>
                                            </div>
                                            <div class="total-row grand-total">
                                                <span>Total</span>
                                                <span><fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/></span>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="error-state">
                                            <p>No order details available for this order.</p>
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
                                        User Information
                                    </h3>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${owner != null}">
                                            <div class="customer-info">
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${owner.image != null}">
                                                            <img src="${empty owner.image || owner.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : owner.image}"
                                                                 alt="User Avatar" class="avatar">
                                                        </c:when>
                                                        <c:otherwise>
                                                            U
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="customer-details">
                                                    <h5>
                                                        <c:choose>
                                                            <c:when test="${owner.fullName != null && owner.fullName.trim().length() > 0}">
                                                                ${owner.fullName}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Unknown User
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </h5>

                                                    
                                                    <c:if test="${owner.email != null && owner.email.trim().length() > 0}">
                                                        <p style="color: #64748b; font-size: 14px;">
                                                            <i class="fas fa-envelope"></i> ${owner.email}
                                                        </p>
                                                    </c:if>
                                                    <c:if test="${owner.phoneNumber != null && owner.phoneNumber.trim().length() > 0}">
                                                        <p style="color: #64748b; font-size: 14px;">
                                                            <i class="fas fa-phone"></i> ${owner.phoneNumber}
                                                        </p>
                                                    </c:if>
                                                    <c:if test="${owner.address != null && owner.address.trim().length() > 0}">
                                                        <p style="color: #64748b; font-size: 14px;">
                                                            <i class="fas fa-map-marker-alt"></i> ${owner.address}
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

                                        <div class="info-row">
                                            <span class="info-label">Customer ID</span>
                                            <span class="info-value">${order.userId}</span>
                                        </div>

                                        <c:if test="${order.warehouseId != null}">
                                            <div class="info-row">
                                                <span class="info-label">Warehouse ID</span>
                                                <span class="info-value">${order.warehouseId}</span>
                                            </div>
                                        </c:if>

                                        <div class="info-row">
                                            <span class="info-label">Order Type</span>
                                            <span class="info-value">
                                                <c:choose>
                                                    <c:when test="${order.type == 0}">
                                                        <span class="status-badge type-import">Import</span>
                                                    </c:when>
                                                    <c:when test="${order.type == 1}">
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
                                                    <c:when test="${order.supplier != null && order.supplier.trim().length() > 0}">
                                                        ${order.supplier}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color: #6c757d; font-style: italic;">N/A</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>

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
        <script src="vendors/datatable/js/dataTables.responsive.min.js"></script>
        <script src="vendors/datatable/js/dataTables.buttons.min.js"></script>
        <script src="js/metisMenu.js"></script>
        <script src="js/custom.js"></script>
    </body>
</html>