<%@ page import="java.util.List" %>
<%@ page import="model.ExportNoteDetail" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderDetail" %>
<%@ page import="model.ExportNoteTransaction" %>
<%@ page import="dal.ExportNoteDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String errorMessage = null;
    String exportNoteIdParam = request.getParameter("exportNoteId");
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger("exportNoteToInventory.jsp");
    ExportNoteDAO dao = new ExportNoteDAO();
    try {
        if (exportNoteIdParam == null || exportNoteIdParam.isEmpty()) {
            errorMessage = "Invalid or missing Export Note ID.";
            logger.warning("Invalid or missing exportNoteId parameter");
        } else {
            int exportNoteId = Integer.parseInt(exportNoteIdParam);
            model.ExportNote exportNote = dao.getAllExportNotes(null, null, null, 1).stream()
                .filter(note -> note.getExportNoteId() == exportNoteId)
                .findFirst()
                .orElse(null);
            if (exportNote == null) {
                errorMessage = "Export Note not found.";
                logger.warning("Export Note not found for ID: " + exportNoteId);
            } else {
                List<ExportNoteDetail> details = dao.getExportNoteDetailsByNoteId(exportNoteId);
                for (ExportNoteDetail detail : details) {
                    int materialDetailId = dao.getMaterialDetailId(detail.getMaterialId(), detail.getQualityId());
                    double availableQty = dao.getAvailableQuantity(materialDetailId);
                    detail.setAvailableQuantity(availableQty);
                }
                Order order = dao.getOrderByExportNoteId(exportNoteId);
                if (order != null) {
                    List<OrderDetail> orderDetails = dao.getOrderDetailsByOrderId(order.getOrderId());
                    order.setOrderDetails(orderDetails);
                }
                request.setAttribute("details", details);
                request.setAttribute("exportNoteId", exportNoteId);
                request.setAttribute("order", order);
            }
        }
    } catch (NumberFormatException e) {
        errorMessage = "Invalid Export Note ID format: " + e.getMessage();
        logger.severe("NumberFormatException: " + e.getMessage());
    } catch (SQLException e) {
        errorMessage = "Database error: " + e.getMessage();
        logger.severe("SQLException: " + e.getMessage());
    } catch (Exception e) {
        errorMessage = "Unexpected error retrieving Export Note: " + e.getMessage();
        logger.severe("Unexpected error: " + e.getMessage());
    }
    request.setAttribute("errorMessage", errorMessage);
%>
<div class="container">
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
        <div class="mt-3">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        </div>
    </c:if>

    <c:if test="${empty errorMessage}">
        <c:if test="${not empty order}">
            <h6 class="mb-3">Order Information</h6>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th scope="row">Order ID</th>
                        <td>${order.orderId}</td>
                    </tr>
                    <tr>
                        <th scope="row">User Name</th>
                        <td>${order.userName != null ? order.userName : 'N/A'}</td>
                    </tr>
                    <tr>
                        <th scope="row">Created At</th>
                        <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                    </tr>
                    <tr>
                        <th scope="row">Type</th>
                        <td>${order.type}</td>
                    </tr>
                    <tr>
                        <th scope="row">Status</th>
                        <td>${order.status}</td>
                    </tr>
                    <tr>
                        <th scope="row">Note</th>
                        <td>${order.note != null ? order.note : 'N/A'}</td>
                    </tr>
                </tbody>
            </table>

            <h6 class="mt-4 mb-3">Order Details</h6>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Material Name</th>
                            <th>Unit Name</th>
                            <th>Requested Quantity</th>
                            <th>Quality</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="orderDetail" items="${order.orderDetails}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty orderDetail.materialImage}">
                                            <img src="${orderDetail.materialImage}" alt="${orderDetail.materialName}" style="width: 50px; height: auto;" />
                                        </c:when>
                                        <c:otherwise>
                                            <div class="modal-image-placeholder">No Image</div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${orderDetail.materialName}</td>
                                <td>${orderDetail.unitName}</td>
                                <td><fmt:formatNumber value="${orderDetail.quantity}" pattern="#,##0.00" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${orderDetail.qualityId > 0}">
                                            <c:set var="qualityName" value="${orderDetail.qualityId == 1 ? 'Available' : 'Not Available'}" />
                                            ${qualityName}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty order.orderDetails}">
                            <tr>
                                <td colspan="5" class="text-center">No order details available.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </c:if>

        <h6 class="mt-4 mb-3">Export Note Details</h6>
        <div class="table-responsive">
            <form id="exportForm">
                <input type="hidden" name="exportNoteId" value="${exportNoteId}" id="hiddenExportNoteId">
                <input type="hidden" name="action" value="export">
                <input type="hidden" name="page" value="${param.page != null ? param.page : 1}">

                <c:if test="${not empty details}">
                    <table class="table table-striped table-bordered align-middle">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 50px;">
                                    <input type="checkbox" class="checkbox-all" id="checkboxAll">
                                </th>
                                <th>Image</th>
                                <th>Material Name</th>
                                <th>Unit Name</th>
                                <th>Requested Quantity</th>
                                <th>Exported Quantity</th>
                                <th>Remaining Quantity</th>
                                <th>Available Quantity</th>
                                <th style="min-width: 120px;">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="detail" items="${details}" varStatus="loop">
                                <c:set var="totalExported" value="0"/>
                                <c:forEach var="transaction" items="${detail.transactions}">
                                    <c:set var="totalExported" value="${totalExported + transaction.exportedQuantity}"/>
                                </c:forEach>
                                <c:set var="remainingQty" value="${detail.quantity - totalExported}"/>
                                <tr class="${detail.availableQuantity < remainingQty && !detail.exported ? 'table-warning' : ''}">
                                    <td>
                                        <c:if test="${not detail.exported && remainingQty > 0}">
                                            <input type="checkbox" class="checkbox-item"
                                                   name="detailIds"
                                                   value="${detail.exportNoteDetailId}"
                                                   data-material-id="${detail.materialId}"
                                                   data-unit-id="${detail.unitId}"
                                                   data-quality-id="${detail.qualityId}"
                                                   data-requested-quantity="${remainingQty}"
                                                   data-available-quantity="${detail.availableQuantity}"
                                                   <c:forEach var="transaction" items="${detail.transactions}">
                                                       <c:if test="${not transaction.exported}">
                                                           data-transaction-id="${transaction.exportNoteTransactionId}"
                                                       </c:if>
                                                   </c:forEach>
                                                   data-status="${detail.availableQuantity >= remainingQty ? 'Available' : 'Low Stock'}">
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty detail.materialImage}">
                                                <img src="${detail.materialImage}" alt="${detail.materialName}" style="width: 50px; height: auto;" />
                                            </c:when>
                                            <c:otherwise>
                                                <div class="modal-image-placeholder">No Image</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${detail.materialName}</td>
                                    <td>${detail.unitName}</td>
                                    <td><fmt:formatNumber value="${detail.quantity}" pattern="#,##0.00" /></td>
                                    <td><fmt:formatNumber value="${totalExported}" pattern="#,##0.00" /></td>
                                    <td><fmt:formatNumber value="${remainingQty}" pattern="#,##0.00" /></td>
                                    <td>
                                        <fmt:formatNumber value="${detail.availableQuantity}" pattern="#,##0.00" />
                                        <c:if test="${detail.availableQuantity < remainingQty && !detail.exported}">
                                            <br><small class="text-warning">Only ${detail.availableQuantity} units available for export</small>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${detail.exported}">
                                                <span class="badge bg-success">Exported</span>
                                            </c:when>
                                            <c:when test="${detail.availableQuantity >= remainingQty}">
                                                <span class="badge bg-success">Available</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">Low Stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <c:if test="${not empty detail.transactions}">
                                    <tr>
                                        <td colspan="9">
                                            <table class="table table-sm table-bordered mb-0">
                                                <thead>
                                                    <tr>
                                                        <th>Date</th>
                                                        <th>Exported Quantity</th>

                                                        <th>Status</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="transaction" items="${detail.transactions}">
    <c:if test="${transaction.exported}">
        <tr>
            <td><fmt:formatDate value="${transaction.createdAt}" pattern="dd/MM/yyyy"/></td>
            <td><fmt:formatNumber value="${transaction.exportedQuantity}" pattern="#,##0.00"/></td>

            <td>
                <span class="badge bg-success">Exported</span>
            </td>
        </tr>
    </c:if>
</c:forEach>

                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${empty detail.transactions}">
                                    <tr>
                                        <td colspan="9" class="text-muted">No export history</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="mt-3">
                        <small class="text-muted">
                            <i class="fas fa-info-circle"></i>
                            Items with insufficient stock are highlighted in yellow. Partial exports are allowed, and remaining quantities will be tracked.
                        </small>
                    </div>
                </c:if>

                <c:if test="${empty details}">
                    <div class="text-center text-muted p-4">
                        <i class="fas fa-inbox fa-3x mb-3"></i>
                        <p>No details available for export.</p>
                    </div>
                </c:if>

               
            </form>
        </div>
    </c:if>

    <script>
        $(document).ready(function() {
            console.log("Export note inventory content loaded");

            $('#checkboxAll').on('click', function() {
                console.log("Checkbox all clicked:", this.checked);
                $('.checkbox-item').prop('checked', this.checked);
                toggleExportButton();
            });

            $('.checkbox-item').on('click', function() {
                console.log("Individual checkbox clicked");
                var totalCheckboxes = $('.checkbox-item').length;
                var checkedCheckboxes = $('.checkbox-item:checked').length;
                $('#checkboxAll').prop('checked', totalCheckboxes === checkedCheckboxes);
                toggleExportButton();
            });

            function toggleExportButton() {
                console.log("Toggle export button, checked count:", $('.checkbox-item:checked').length);
                $('#exportButton').prop('disabled', $('.checkbox-item:checked').length === 0);
            }

            toggleExportButton();
        });
    </script>
</div>