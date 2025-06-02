<%-- 
    Document   : adminresetlist
    Created on : Jun 1, 2025, 11:42:35 PM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reset Password Requests - Admin</title>
    <html>
    <head>
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
        <link rel="stylesheet"six href="vendors/material_icon/material-icons.css" />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        table {
            width: 95%;
            margin: 0 auto 20px auto;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: center;
            font-size: 14px;
        }

        th {
            background-color: #343a40;
            color: #fff;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #e9ecef;
        }

        .btn {
            padding: 6px 12px;
            text-decoration: none;
            color: #fff;
            border-radius: 4px;
            cursor: pointer;
            border: none;
            font-size: 13px;
            transition: background-color 0.3s ease;
        }

        .approve {
            background-color: #28a745;
        }

        .approve:hover {
            background-color: #218838;
        }

        .reject {
            background-color: #dc3545;
        }

        .reject:hover {
            background-color: #c82333;
        }

        .message {
            text-align: center;
            font-size: 16px;
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 4px;
        }

        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
   <%@ include file="sidebar.jsp" %>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
<!-- Hiển thị message -->
<c:if test="${not empty param.success}">
    <div class="message success">${param.success}</div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="message error">${param.error}</div>
</c:if>

<table>
    <tr>
        <th>User ID</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Role ID</th>
        <th>Phone Number</th>
        <th>Address</th>
        <th>Action</th>
    </tr>

    <c:forEach var="user" items="${resetList}">
        <tr>
            <td>${user.userId}</td>
            <td>${user.fullName}</td>
            <td>${user.email}</td>
            <td>${user.roleId}</td>
            <td>${user.phoneNumber}</td>
            <td>${user.address}</td>
            <td>
                <form action="adminresetpassword" method="post" style="display:inline;">
                    <input type="hidden" name="token" value="${user.resetPasswordToken}">
                    <input type="hidden" name="userId" value="${user.userId}">
                    <button type="submit" name="action" value="approve" class="btn approve">Approve</button>
                </form>

                <form action="adminresetpassword" method="post" style="display:inline; margin-left:5px;">
                    <input type="hidden" name="token" value="${user.resetPasswordToken}">
                    <input type="hidden" name="userId" value="${user.userId}">
                    <button type="submit" name="action" value="reject" class="btn reject">Reject</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>