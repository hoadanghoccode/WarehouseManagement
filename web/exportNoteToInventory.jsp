<%@ page import="java.util.List" %>
<%@ page import="model.ExportNoteDetail" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    int exportNoteId = Integer.parseInt(request.getParameter("exportNoteId"));
    dal.ExportNoteDAO dao = new dal.ExportNoteDAO();
    List<ExportNoteDetail> details = dao.getExportNoteDetailsByNoteId(exportNoteId);
    request.setAttribute("details", details);
    request.setAttribute("exportNoteId", exportNoteId);
%>
<div class="table-responsive">
    <input type="hidden" name="exportNoteId" value="${exportNoteId}">
    <table class="table table-striped">
        <thead>
            <tr>
                <th><input type="checkbox" class="checkbox-all"></th>
                <th>Material Name</th>
                <th>SubUnit Name</th>
                <th>Quantity</th>
                <th>Quality</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="detail" items="${details}">
                <tr>
                    <td><input type="checkbox" class="checkbox-item" value="${detail.exportNoteDetailId}"></td>
                    <td>${detail.materialName}</td>
                    <td>${detail.subUnitName}</td>
                    <td><fmt:formatNumber value="${detail.quantity}" pattern="#,##0.00" /></td>
                    <td>${detail.qualityName}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty details}">
                <tr>
                    <td colspan="5" class="text-center">No details available for export.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>