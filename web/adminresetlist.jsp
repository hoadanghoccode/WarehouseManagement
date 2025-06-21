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
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    background-color: #f5f7fb;
                    margin: 0;
                    padding: 20px;
                    color: #333;
                }

                h2 {
                    text-align: center;
                    margin-bottom: 30px;
                    font-size: 28px;
                    font-weight: bold;
                }

                table {
                    width: 96%;
                    margin: 0 auto;
                    border-collapse: collapse;
                    background-color: #ffffff;
                    border-radius: 12px;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
                    overflow: hidden;
                }

                th, td {
                    padding: 14px 16px;
                    border-bottom: 1px solid #f0f0f0;
                    font-size: 15px;
                    text-align: center;
                    vertical-align: middle;
                }

                th {
                    background-color: #f2f4f7;
                    font-weight: 600;
                    text-transform: none; 
                    font-size: 14px;
                    color: #444;
                }

                tr:nth-child(even) {
                    background-color: #fafafa;
                }

                tr:hover {
                    background-color: #eef1f6;
                }

                .btn {
                    padding: 6px 14px;
                    font-size: 13px;
                    font-weight: 500;
                    border: none;
                    border-radius: 6px;
                    cursor: pointer;
                    transition: background-color 0.3s ease;
                }

                .approve {
                    background-color: #22c55e;
                    color: white;
                }

                .approve:hover {
                    background-color: #16a34a;
                }

                .reject {
                    background-color: #ef4444;
                    color: white;
                }

                .reject:hover {
                    background-color: #dc2626;
                }

                .message {
                    width: 95%;
                    margin: 0 auto 20px auto;
                    padding: 12px 16px;
                    border-radius: 8px;
                    font-size: 15px;
                    text-align: center;
                }

                .message.success {
                    background-color: #d1fae5;
                    color: #065f46;
                    border: 1px solid #10b981;
                }

                .message.error {
                    background-color: #fee2e2;
                    color: #991b1b;
                    border: 1px solid #ef4444;
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
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Action</th>
                    </tr>

                    <c:forEach var="user" items="${resetList}">
                        <tr>
                            <td>${user.userId}</td>
                            <td>${user.fullName}</td>
                            <td>${user.email}</td>
                            <td>${user.roleName}</td>
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