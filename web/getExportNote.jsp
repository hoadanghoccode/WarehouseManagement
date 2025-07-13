<%@ page import="java.util.List" %>
<%@ page import="model.ExportNote" %>
<%@ page import="model.ExportNoteDetail" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderDetail" %>
<%@ page import="model.ExportNoteTransaction" %>
<%@ page import="dal.ExportNoteDAO" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    // NÊN lấy dữ liệu ở servlet, nhưng nếu bắt buộc thì làm như dưới (ví dụ: modal detail load qua AJAX)
    String noteIdParam = request.getParameter("exportNoteId");
    ExportNote exportNote = null;
    Order order = null;
    List<ExportNoteDetail> details = null;
    List<OrderDetail> orderDetails = null;
    List<ExportNoteTransaction> transactions = null;

    if (noteIdParam != null && !noteIdParam.isEmpty()) {
        int exportNoteId = Integer.parseInt(noteIdParam);
        ExportNoteDAO dao = new ExportNoteDAO();
        exportNote = dao.getAllExportNotes(null, null, null, 1).stream()
            .filter(note -> note.getExportNoteId() == exportNoteId)
            .findFirst()
            .orElse(null);
        if (exportNote != null) {
            details = dao.getExportNoteDetailsByNoteId(exportNoteId);
            for (ExportNoteDetail detail : details) {
                int materialDetailId = dao.getMaterialDetailId(detail.getMaterialId(), detail.getQualityId());
                double availableQty = dao.getAvailableQuantity(materialDetailId);
                detail.setAvailableQuantity(availableQty);
            }
            exportNote.setDetails(details);

            order = dao.getOrderByExportNoteId(exportNoteId);
            if (order != null) {
                orderDetails = dao.getOrderDetailsByOrderId(order.getOrderId());
                order.setOrderDetails(orderDetails);
            }
            transactions = dao.getAllExportTransactions(exportNoteId);
        }
    }

    request.setAttribute("exportNote", exportNote);
    request.setAttribute("order", order);
    request.setAttribute("transactions", transactions);
%>

<div class="container">
    <c:choose>
        <c:when test="${not empty exportNote}">
            <h6 class="mb-3">Export Note Information</h6>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th>User Name</th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty exportNote.userName}">
                                    <c:out value="${exportNote.userName}"/>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Warehouse Name</th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty exportNote.warehouseName}">
                                    <c:out value="${exportNote.warehouseName}"/>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Created At</th>
                        <td>
                            <fmt:formatDate value="${exportNote.createdAt}" pattern="dd/MM/yyyy"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Exported</th>
                        <td>
                            <span class="badge <c:out value="${exportNote.exported ? 'bg-success' : 'bg-danger'}"/>">
                                <c:choose>
                                    <c:when test="${exportNote.exported}">Yes</c:when>
                                    <c:otherwise>No</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <th>Exported At</th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty exportNote.exportedAt}">
                                    <fmt:formatDate value="${exportNote.exportedAt}" pattern="dd/MM/yyyy"/>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </tbody>
            </table>

            <c:if test="${not empty order}">
                <h6 class="mt-4 mb-3">Order Details</h6>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead class="table-light">
                            <tr>
                                <th>#</th>
                                <th>Image</th>
                                <th>Material Name</th>
                                <th>Unit Name</th>
                                <th>Requested Quantity</th>
                                <th>Quality</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="orderDetail" items="${order.orderDetails}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
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
                                    <td><fmt:formatNumber value="${orderDetail.quantity}" pattern="#,##0"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${orderDetail.qualityId > 0}">Available</c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty order.orderDetails}">
                                <tr>
                                    <td colspan="6" class="text-center">No order details available.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </c:if>

            
            
        </c:when>
        <c:otherwise>
            <p class="text-danger">Export note not found.</p>
        </c:otherwise>
    </c:choose>
</div>
