<%-- 
    Document   : createorder
    Created on : Jun 13, 2025, 12:06:28 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

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
        <link rel="stylesheet" type="text/css" href="css/createorder.css" />

    </head>
    <body>
        <%@ include file="navbar.jsp" %>
        <%@ include file="sidebar.jsp" %>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>



        <div class="main-content">
            <div class="header-actions">
                <a href="orderlist" class="back-btn">
                    <i class="fas fa-arrow-left"></i>
                    Back
                </a>
            </div>
            <div class="page-header">
                <h1></i> Create New Order</h1>
                <p>Fill in the details below to create a new order</p>
            </div>

            <div class="order-form-container">
                <form id="orderForm" action="createorder" method="post">
                    <!-- Order Basic Information -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-info-circle"></i>
                            Order Information
                        </h3>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="orderType">Order Type <span class="required">*</span></label>
                                <select class="form-control" id="orderType" name="orderType" required>
                                    <option value="">Select Order Type</option>
                                    <option value="import">Import</option>
                                    <option value="export">Export</option>
                                    <option value="exportToRepair">Repair</option>
                                </select>
                            </div>


                            <div class="form-group">
                                <label for="status">Supplier <span class="required">*</span></label>
                                <select class="form-control" id="supplier" name="supplier">
                                    <option value="">Select Supplier</option>
                                    <c:forEach var="supplier" items="${suppliers}">
                                        <option value=${supplier.id}>${supplier.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="note">Note</label>
                            <textarea class="form-control" id="note" name="note" rows="3" placeholder="Additional notes or comments..."></textarea>
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
                            <div class="no-items-message" id="noItemsMessage">
                                <i class="fas fa-info-circle" style="font-size: 24px; margin-bottom: 10px;"></i>
                                <p>No items added yet. Click "Add Item" to start adding items to your order.</p>
                            </div>
                        </div>
                    </div>

                    <!-- Form Actions -->
                    <div class="form-actions">
                        <button type="button" class="cancel-btn" onclick="cancelOrder()">
                            <i class="fas fa-times"></i>
                            Cancel
                        </button>
                        <button type="submit" class="submit-btn">
                            <i class="fas fa-check"></i>
                            Create Order
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
        <!--<script src="js/bootstrap.bundle.min.js"></script>-->

        <script>
                        const categories = ${categoriesJson};
                        const units = ${unitsJson};
        </script>
        <script src="js/createorder.js"></script>
    </body>
</html>