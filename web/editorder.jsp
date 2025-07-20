<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Order - Order #${order.orderId}</title>

        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <link rel="stylesheet" href="css/metisMenu.css">
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <link rel="stylesheet" type="text/css" href="css/createorder.css" />
    </head>
    <body>
        <%@ include file="navbar.jsp" %>
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="header-actions">
                <a href="orderlist" class="back-btn">
                    <i class="fas fa-arrow-left"></i>
                    Back
                </a>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <div class="page-header">
                <h1><i class="fas fa-edit"></i> Update Order #${order.orderId}</h1>
                <p>Update the details below to modify the order</p>
            </div>

            <div class="order-form-container">
                <form id="orderForm" action="editorder" method="post">
                    <!-- Hidden field for order ID -->
                    <input type="hidden" name="orderId" value="${order.orderId}" />

                    <!-- Order Basic Information -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-info-circle"></i>
                            Order Information
                        </h3>

                        <div class="form-row col-md-6">
                            <div class="form-group">
                                <label for="orderType">Order Type <span class="required">*</span></label>
                                <select class="form-control" id="orderType" name="orderType" required onchange="toggleSupplierField()">
                                    <option value="">Select Order Type</option>
                                    <option value="import" ${order.type == 'import' ? 'selected' : ''}>Import</option>
                                    <option value="export" ${order.type == 'export' ? 'selected' : ''}>Export</option>
                                    <!--Xử lý purchase của b Giang-->
                                    <option value="purchase" ${order.type == 'purchase' ? 'selected' : ''}>Purchase</option>
                                </select>
                            </div>

                            <!--                            <div class="form-group">
                                                            <label for="supplier">Supplier <span class="required">*</span></label>
                                                            <select class="form-control" id="supplier" name="supplier" required>
                                                                <option value="">Select Supplier</option>
                            <c:forEach var="supplier" items="${suppliers}">
                                <option value="${supplier.supplierId}" ${order.supplier == supplier.supplierId ? 'selected' : ''}>${supplier.name}</option>
                            </c:forEach>
                        </select>
                    </div>-->

                        </div>

                        <div class="form-group col-md-6">
                            <label for="note">Note</label>
                            <textarea class="form-control" id="note" name="note" rows="3" placeholder="Additional notes or comments...">${order.note}</textarea>
                        </div>
                    </div>

                    <!-- Order Items Section -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-boxes"></i>
                            Order Items
                        </h3>

                        <button type="button" class="add-item-btn" onclick="addOrderItem()">
                            <i class="fas fa-plus"></i>
                            Add Item
                        </button>

                        <div id="orderItemsContainer">
                            <!-- Existing order items will be loaded here -->
                            <c:choose>
                                <c:when test="${empty order.orderDetails}">
                                    <div class="no-items-message" id="noItemsMessage">
                                        <i class="fas fa-info-circle" style="font-size: 24px; margin-bottom: 10px;"></i>
                                        <p>No items in this order. Click "Add Item" to start adding items.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-items-message" id="noItemsMessage" style="display: none;">
                                        <i class="fas fa-info-circle" style="font-size: 24px; margin-bottom: 10px;"></i>
                                        <p>No items added yet. Click "Add Item" to start adding items to your order.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Form Actions -->
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelOrder()">
                            <i class="fas fa-times"></i>
                            Cancel
                        </button>
                        <button type="submit" class="submit-btn">
                            <i class="fas fa-save"></i>
                            Update Order
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Modal -->
        <div id="cancelModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Confirm Cancellation</h2>
                    <span class="close" onclick="hideCancelModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <div class="warning-box">
                        <div class="warning-header">
                            <i class="fas fa-exclamation-triangle warning-icon"></i>
                            <span class="warning-title">Warning</span>
                        </div>
                        <div class="warning-content">
                            Are you sure you want to cancel this order? All unsaved changes will be lost.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" onclick="confirmCancel()">
                        <i class="fas fa-check"></i>
                        Yes, Cancel Order
                    </button>
                    <button type="button" class="btn btn-gray" onclick="hideCancelModal()">
                        <i class="fas fa-arrow-left"></i>
                        Continue Editing
                    </button>
                </div>
            </div>
        </div>

        <!-- JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

        <script>
                        const allMaterials = ${allMaterialsJson};

                        const existingOrderDetails = [
            <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                        {
                        materialId: ${detail.materialId},
                                materialName: '${detail.materialName}',
                                subUnitName: '${detail.unitName}',
                                quantity: ${detail.quantity}
                        }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
                        ];
                        console.log(existingOrderDetails);
        </script>
        <!--Xử lý purchase của b Giang-->
        <script>
            function toggleSupplierField() {
                const orderType = document.getElementById('orderType').value;
                const supplierField = document.getElementById('supplier');
                if (orderType === 'purchase') {
                    supplierField.disabled = true;
                    supplierField.value = '';
                } else {
                    supplierField.disabled = false;
                }
            }
            window.onload = toggleSupplierField;
        </script>
        <script src="js/editorder.js"></script>
    </body>
</html>