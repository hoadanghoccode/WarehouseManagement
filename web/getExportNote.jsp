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
%>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Material Name</th>
                <th>SubUnit Name</th>
                <th>Quantity</th>
                <th>Quality</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="detail" items="${details}">
                <tr>
                    <td>${detail.materialName}</td>
                    <td>${detail.subUnitName}</td>
                    <td><fmt:formatNumber value="${detail.quantity}" pattern="#,##0.00" /></td>
                    <td>${detail.qualityName}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty details}">
                <tr>
                    <td colspan="4" class="text-center">No details available.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>