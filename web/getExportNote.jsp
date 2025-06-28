<%@ page import="java.util.List" %>
<%@ page import="model.ExportNoteDetail" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    int exportNoteId = Integer.parseInt(request.getParameter("exportNoteId"));
    dal.ExportNoteDAO dao = new dal.ExportNoteDAO();
    model.ExportNote exportNote = dao.getAllExportNotes(null, null, null, 1).stream()
        .filter(note -> note.getExportNoteId() == exportNoteId)
        .findFirst()
        .orElse(null);
    if (exportNote != null) {
        List<ExportNoteDetail> details = dao.getExportNoteDetailsByNoteId(exportNoteId);
        exportNote.setDetails(details);
        request.setAttribute("exportNote", exportNote);
    }
%>
<div class="container">
    <c:choose>
        <c:when test="${not empty exportNote}">
            <h6 class="mb-3">Export Note Information</h6>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th scope="row">User Name</th>
                        <td>${exportNote.userName != null ? exportNote.userName : 'N/A'}</td>
                    </tr>
                    <tr>
                        <th scope="row">Warehouse Name</th>
                        <td>${exportNote.warehouseName != null ? exportNote.warehouseName : 'N/A'}</td>
                    </tr>
                    <tr>
                        <th scope="row">Created At</th>
                        <td><fmt:formatDate value="${exportNote.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                    </tr>
                    <tr>
                        <th scope="row">Exported</th>
                        <td><span class="badge ${exportNote.exported ? 'bg-success' : 'bg-danger'}">${exportNote.exported ? 'Yes' : 'No'}</span></td>
                    </tr>
                    <tr>
                        <th scope="row">Exported At</th>
                        <td>${exportNote.exportedAt != null ? exportNote.exportedAt : 'N/A'}</td>
                    </tr>
                </tbody>
            </table>

            <h6 class="mt-4 mb-3">Export Note Details</h6>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Material Name</th>
                            <th>SubUnit Name</th>
                            <th>Quantity</th>
                            <th>Quality</th>
                            <th>Available Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${exportNote.details}">
                            <tr>
                                <td>${detail.materialName}</td>
                                <td>${detail.subUnitName}</td>
                                <td><fmt:formatNumber value="${detail.quantity}" pattern="#,##0.00" /></td>
                                <td>${detail.qualityName}</td>
                                <td><fmt:formatNumber value="${detail.availableQuantity}" pattern="#,##0.00" /></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty exportNote.details}">
                            <tr>
                                <td colspan="5" class="text-center">No details available.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <p class="text-danger">Export note not found.</p>
        </c:otherwise>
    </c:choose>
</div>