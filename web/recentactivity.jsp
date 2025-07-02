<%-- 
    Document   : recentactivity
    Created on : Jun 30, 2025, 11:36:27 AM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.MaterialTransactionHistory,model.Material,model.Users" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="recentTransactions" type="java.util.List" scope="request"/>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>


<h3>🔄 Giao dịch gần nhất</h3>
<ul>
    <c:forEach var="tx" items="${recentTransactions}">
        <li>
            ${tx.transactionDate} - Material ID: ${tx.materialId} - Quantity: ${tx.quantity} - ${tx.note}
        </li>
    </c:forEach>
</ul>

<h3>🆕 Vật tư mới thêm</h3>
<ul>
    <c:forEach var="mat" items="${newMaterials}">
        <li>${mat.materialName} (ID: ${mat.materialId}) - ${mat.createdAt}</li>
    </c:forEach>
</ul>

<h3>👤 Nhân viên thao tác gần nhất</h3>
<c:if test="${latestUser != null}">
    <p>${latestUser.fullName} - ${latestUser.email} (Cập nhật lúc: ${latestUser.updatedAt})</p>
</c:if>