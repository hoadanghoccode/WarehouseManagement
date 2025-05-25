<%-- 
    Document   : adminresetrequest
    Created on : May 23, 2025, 12:45:06 PM
    Author     : duong
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="dal.UserDAO" %>
<%@ page import="model.Users" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Reset Password</title>
    <style>
        body {
            font-family: Arial;
            max-width: 500px;
            margin: auto;
            padding-top: 50px;
        }
        input, button {
            display: block;
            margin: 10px 0;
            padding: 8px;
            width: 100%;
        }
        .error {
            color: red;
        }
        .success {
            color: green;
        }
    </style>
</head>
<body>

    <h2>Admin: Reset User Password</h2>

    <!-- Thông báo lỗi hoặc thành công -->
    <%
        String mess = (String) request.getAttribute("message");
        if (mess != null) {
    %>
        <p class="<%= mess.contains("success") ? "success" : "error" %>"><%= mess %></p>
    <%
        }
    %>

    <form method="post" action="adminresetpassword">
        <label>Email:</label>
        <input type="email" name="email" readonly value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" />

        <label>New Password:</label>
        <input type="password" name="password" required />

        <label>Confirm Password:</label>
        <input type="password" name="confirm_password" required />

        <button type="submit">Reset Password</button>
    </form>

</body>
</html>
