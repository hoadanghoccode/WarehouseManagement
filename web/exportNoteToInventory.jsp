<%@ page import="java.util.List" %>
<%@ page import="model.ExportNoteDetail" %>
<%@ page import="dal.ExportNoteDAO" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String exportNoteIdParam = request.getParameter("exportNoteId");
    if (exportNoteIdParam != null && !exportNoteIdParam.isEmpty()) {
        int exportNoteId = Integer.parseInt(exportNoteIdParam);
        ExportNoteDAO dao = new ExportNoteDAO();
        List<ExportNoteDetail> details = dao.getExportNoteDetailsByNoteId(exportNoteId);
        for (ExportNoteDetail detail : details) {
            int materialDetailId = dao.getMaterialDetailId(detail.getMaterialId(), detail.getSubUnitId(), detail.getQualityId());
            double availableQty = dao.getAvailableQuantity(materialDetailId);
            detail.setAvailableQuantity(availableQty);
        }
        request.setAttribute("details", details);
        request.setAttribute("exportNoteId", exportNoteId);
    }
%>

<div class="table-responsive">
    <input type="hidden" name="exportNoteId" value="${exportNoteId}" id="hiddenExportNoteId">
    
    <c:if test="${not empty details}">
        <table class="table table-striped table-bordered align-middle">
            <thead class="table-light">
                <tr>
                    <th style="width: 50px;">
                        <input type="checkbox" class="checkbox-all" id="checkboxAll">
                    </th>
                    <th>Material Name</th>
                    <th>SubUnit Name</th>
                    <th>Requested Quantity</th>
                    <th>Available Quantity</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detail" items="${details}" varStatus="loop">
                    <tr class="${detail.availableQuantity >= detail.quantity ? '' : 'table-warning'}">
                        <td>
                            <input type="checkbox" class="checkbox-item"
                                   value="${detail.exportNoteDetailId}"
                                   data-material-id="${detail.materialId}"
                                   data-subunit-id="${detail.subUnitId}"
                                   data-quality-id="${detail.qualityId}"
                                   data-requested-quantity="${detail.quantity}"
                                   data-available-quantity="${detail.availableQuantity}"
                                   ${detail.availableQuantity >= detail.quantity ? '' : 'disabled'}>
                        </td>
                        <td>${detail.materialName}</td>
                        <td>${detail.subUnitName}</td>
                        <td>
                            <fmt:formatNumber value="${detail.quantity}" pattern="#,##0.00" />
                        </td>
                        <td>
                            <fmt:formatNumber value="${detail.availableQuantity}" pattern="#,##0.00" />
                            <c:if test="${detail.availableQuantity < detail.quantity}">
                                <br><small class="text-warning">Insufficient stock</small>
                            </c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${detail.availableQuantity >= detail.quantity}">
                                    <span class="badge bg-success">Available</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning">Low Stock</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="mt-3">
            <small class="text-muted">
                <i class="fas fa-info-circle"></i> 
                Items with insufficient stock are disabled and highlighted in yellow.
            </small>
        </div>
    </c:if>
    
    <c:if test="${empty details}">
        <div class="text-center text-muted p-4">
            <i class="fas fa-inbox fa-3x mb-3"></i>
            <p>No details available for export.</p>
        </div>
    </c:if>
    
    <div id="inventoryWarning" class="alert alert-warning mt-3" style="display: none;"></div>
</div>

<script>
$(document).ready(function() {
    console.log("Export note inventory content loaded");

    $('#inventoryModal').off('click', '.checkbox-all');
    $('#inventoryModal').off('click', '.checkbox-item');

    $('#inventoryModal').on('click', '.checkbox-all', function() {
        console.log("Checkbox all clicked:", this.checked);
        $('.checkbox-item:not(:disabled)').prop('checked', this.checked);
    });

    $('#inventoryModal').on('click', '.checkbox-item', function() {
        console.log("Individual checkbox clicked");
        var totalCheckboxes = $('.checkbox-item:not(:disabled)').length;
        var checkedCheckboxes = $('.checkbox-item:not(:disabled):checked').length;
        $('.checkbox-all').prop('checked', totalCheckboxes === checkedCheckboxes);
    });
});
</script>