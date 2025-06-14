<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="page" value="changepassword" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style1.css" />
    <style>
        body {
            margin: 0;
            background-color: #f0f2f5;
            font-family: Arial, sans-serif;
        }
        .main-section {
            display: flex;
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
            gap: 24px;
        }
        .account-settings {
            width: 280px;
            background-color: #fff;
            border-radius: 8px;
            padding: 16px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .account-settings h2 {
            font-size: 18px;
            margin-bottom: 16px;
        }
        .account-settings ul {
            list-style: none;
            padding: 0;
        }
        .account-settings li {
            padding: 10px 12px;
            border-radius: 6px;
            margin-bottom: 8px;
            cursor: pointer;
        }
        .account-settings li.active,
        .account-settings li:hover {
            background-color: #e4e6eb;
            font-weight: bold;
        }
        .profile-box {
            flex: 1;
            background-color: #fff;
            border-radius: 8px;
            padding: 24px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .profile-box h2 {
            font-size: 22px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        label {
            font-weight: 500;
            margin-bottom: 6px;
            color: #050505;
        }
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border-radius: 6px;
            border: 1px solid #ccd0d5;
            background-color: #f5f6f7;
            margin-bottom: 16px;
        }
        .forgot-link {
            display: inline-block;
            font-size: 14px;
            color: #1877f2;
            text-decoration: none;
            margin-bottom: 20px;
        }
        .forgot-link:hover {
            text-decoration: underline;
        }
        .checkbox-row {
            display: flex;
            align-items: center;
            font-size: 14px;
            margin-bottom: 20px;
        }
        .checkbox-row input {
            margin-right: 8px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            font-weight: bold;
            background-color: #1877f2;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #165ecc;
        }
        .message {
            text-align: center;
            font-size: 14px;
            margin-top: 16px;
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
<%@ include file="sidebar.jsp" %>
<section class="main_content dashboard_part">
    <%@ include file="navbar.jsp" %>
    <div class="main-section">
        <div class="profile-box">
            <h2>Change Password</h2>
            <p class="text-muted">Your password must be at least 8 characters long and include at least one letter, one number, and one special character (!@#$...)</p>
            <form method="post" action="changepassword">
                <label for="currentPassword">Current password:</label>
                <input type="password" name="currentPassword" id="currentPassword" required>

                <label for="newPassword">New password:</label>
                <input type="password" name="newPassword" id="newPassword" required>

                <label for="confirmPassword">Re-enter new password:</label>
                <input type="password" name="confirmPassword" id="confirmPassword" required>

                <a class="forgot-link" href="resetpassword.jsp">Forgot your password?</a>

                <div class="checkbox-row">
                    <input type="checkbox" id="logoutOthers" name="logoutOthers" checked>
                    <label for="logoutOthers">Log out of other devices. Choose this if someone else has used your account.</label>
                </div>

                <input type="submit" value="Change Password">

                <div class="message">
                    <p class="error">${error}</p>
                    <p class="success">${success}</p>
                </div>
            </form>
        </div>
    </div>
</section>
</body>
</html>