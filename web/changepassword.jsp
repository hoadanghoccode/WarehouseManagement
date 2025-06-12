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
        <meta charset="UTF-8">
        <title>Change Password</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="icon" href="img/logo.png" type="image/png">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <!-- themefy CSS -->
        <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
        <!-- swiper slider CSS -->
        <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
        <!-- owl carousel CSS -->
        <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
        <!-- gijgo css -->
        <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
        <!-- font awesome CSS -->
        <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
        <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
        <!-- date picker -->
        <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
        <!-- datatable CSS -->
        <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
        <!-- text editor css -->
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <!-- morris css -->
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <!-- metarial icon css -->
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">

        <style>
            * {
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, sans-serif;
            }

            body {
                margin: 0;
                background-color: #f5f6f7;
            }

            /* Sidebar */
            .sidebar {
                width: 280px;
                height: 100vh;
                background-color: #ffffff;
                padding: 20px;
                border-right: 1px solid #ddd;
            }

            .sidebar h2 {
                font-size: 16px;
                color: #606770;
                margin-bottom: 30px;
            }

            .sidebar ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .sidebar li {
                padding: 12px 15px;
                border-radius: 8px;
                margin-bottom: 8px;
                cursor: pointer;
                transition: background-color 0.2s ease;
            }

            .sidebar li.active,
            .sidebar li:hover {
                background-color: #e7f3ff;
                color: #1877f2;
                font-weight: bold;
            }

            /* Content */
            .content {
                flex: 1;
                padding: 40px 60px;
                background-color: #f5f6f7;
                height: 100vh;
                overflow-y: auto;
            }

            .content h2 {
                font-size: 28px;
                font-weight: bold;
                margin-bottom: 8px;
            }

            .content .desc {
                color: #606770;
                font-size: 15px;
                margin-bottom: 32px;
            }

            /* Form box */
            .form-box {
                background-color: #fff;
                border-radius: 12px;
                padding: 24px;
                box-shadow: 0 1px 2px rgba(0,0,0,0.1);
                max-width: 600px;
            }

            label {
                font-weight: 500;
                display: block;
                margin-bottom: 6px;
                color: #050505;
            }

            input[type="password"] {
                width: 100%;
                padding: 14px 12px;
                border-radius: 10px;
                border: 1px solid #ccd0d5;
                font-size: 15px;
                margin-bottom: 20px;
                background-color: #f5f6f7;
            }

            .forgot-link {
                display: inline-block;
                font-size: 14px;
                color: #1877f2;
                margin-bottom: 20px;
                text-decoration: none;
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
                border-radius: 10px;
                cursor: pointer;
                transition: background-color 0.2s ease;
            }

            input[type="submit"]:hover {
                background-color: #165ecc;
            }

            .message {
                text-align: center;
                margin-top: 15px;
                font-size: 14px;
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

          <div style="display: flex; flex-direction: row; height: 100vh;">


    <div class="content" style="flex: 1; display: flex; flex-direction: row;">


        <div style="width: 250px; padding: 20px; border-right: 1px solid #ddd; background-color: #fff;">
            <h2 style="font-size: 16px; color: #606770; margin-bottom: 30px;">Account Settings</h2>
            <ul style="list-style: none; padding: 0; margin: 0;">
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; background-color: #e7f3ff; color: #1877f2; font-weight: bold;">Password & Security</li>
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; cursor: pointer;">Personal Info</li>
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; cursor: pointer;">Your Data</li>
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; cursor: pointer;">Ad Preferences</li>
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; cursor: pointer;">Meta Pay</li>
                <li style="padding: 12px 15px; border-radius: 8px; margin-bottom: 8px; cursor: pointer;">Account</li>
            </ul>
        </div>

        <div style="flex: 1; padding: 40px 60px; background-color: #f5f6f7; overflow-y: auto;">
            <h2>Change Password</h2>
            <p class="desc">Your password must be at least 8 characters long and include at least one letter, one number, and one special character (!@#$...)</p>
            <div class="form-box">
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

    </div> 

</div> 
</html>