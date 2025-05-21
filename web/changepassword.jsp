<%-- 
    Document   : changepassword
    Created on : May 21, 2025, 11:15:47 AM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Users" %>
<%
    Users u = (Users) session.getAttribute("user");
    if (u == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Password</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background-color: #fff;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 500;
            color: #333;
        }

        input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        input[type="submit"] {
            width: 100%;
            background-color: #007bff;
            color: white;
            padding: 12px;
            font-size: 16px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .message {
            text-align: center;
            font-size: 14px;
            margin-top: 10px;
        }

        .error {
            color: #dc3545;
        }

        .success {
            color: #28a745;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Change Password</h2>
        <form method="post" action="changepassword">
        <label>Current password:</label>
    <input type="password" name="currentPassword" required>

    <label>New password:</label>
    <input type="password" name="newPassword" required>

    <label>New password:</label>
    <input type="password" name="confirmPassword" required>

    <input type="submit" value="Change Password">

    <p style="color:red">${error}</p>
    <p style="color:green">${success}</p>
</form>
</body>
</html>
