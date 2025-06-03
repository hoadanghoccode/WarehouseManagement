<%-- 
    Document   : login
    Created on : May 16, 2025, 3:37:28 PM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="zxx">


    <!-- Mirrored from demo.dashboardpack.com/directory-html/login.html by HTTrack Website Copier/3.x [XR&CO'2014], Fri, 16 May 2025 08:23:10 GMT -->
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Directory</title>

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
            html, body {
                height: 100%;
                margin: 0;
            }
            .login-container {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                background-color: #f4f4f4;
            }
            .cs_modal {
                width: 100%;
                max-width: 400px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }
            .modal-body {
                padding: 30px;
            }
            .form-control {
                margin-bottom: 15px;
            }
        </style>
    </head>

    <body class="crm_body_bg">
        <div class="login-container">
            <div class="container-fluid">
                <div class="row justify-content-center">
                    <div class="col-lg-12 d-flex justify-content-center">
                        <div class="modal-content cs_modal">
                            <div class="modal-header justify-content-center theme_bg_1">
                                <h5 class="modal-title text_white">Log in</h5>
                            </div>
                            <div class="modal-body">

                                <c:if test="${not empty error}">
                                    <p style="color: red;">${error}</p>
                                </c:if>

                                <c:if test="${not empty success}">
                                    <p style="color: green;">${success}</p>
                                </c:if>

                                <%
                                    session.removeAttribute("success"); // sau khi hiển thị thì xóa để tránh hiển thị lại
                                %>

                                <form action="login" method="post">
                                    <div>
                                        <input type="text" name="email" class="form-control" placeholder="Enter your email" required>
                                    </div>
                                    <div>
                                        <input type="password" name="password" class="form-control" placeholder="Password" required>
                                    </div>                
                                    <button type="submit" class="btn_1 full_width text-center">Log in</button>
                                    <div class="text-center">
                                        <a href="resetpassword" class="pass_forget_btn">Reset Password?</a>
                                    </div>
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&access_type=online&redirect_uri=http://localhost:8080/WarehouseManagement/login-google&response_type=code&client_id=1016583625353-9670o6t0bql62u5cf43sjvc31d9r6dcr.apps.googleusercontent.com">
                                        <img src="https://developers.google.com/identity/images/btn_google_signin_dark_normal_web.png" alt="Login with Google"/>
                                    </a>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>


