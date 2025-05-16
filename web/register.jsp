<%-- 
    Document   : register
    Created on : May 16, 2025, 4:40:13 PM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">


<!-- Mirrored from demo.dashboardpack.com/directory-html/resister.html by HTTrack Website Copier/3.x [XR&CO'2014], Fri, 16 May 2025 08:23:10 GMT -->
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
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
    
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }
        .register-container {
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
    <div class="register-container">
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-12 d-flex justify-content-center">
                    <div class="modal-content cs_modal">
                        <div class="modal-header justify-content-center theme_bg_1">
                            <h5 class="modal-title text_white">Register</h5>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div>
                                    <input type="text" class="form-control" placeholder="Full Name">
                                </div>
                                <div>
                                    <input type="email" class="form-control" placeholder="Enter your email">
                                </div>
                                <div>
                                    <input type="password" class="form-control" placeholder="Password">
                                </div>
                                <div class="cs_check_box mb-3">
                                    <input type="checkbox" id="check_box" class="common_checkbox">
                                    <label class="form-label" for="check_box">
                                        Keep me up to date
                                    </label>
                                </div>
                                <a href="#" class="btn_1 full_width text-center">Sign Up</a>
                                <p class="text-center mt-3">Already have an account? <a href="login.jsp">Log in</a></p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="js/jquery1-3.4.1.min.js"></script>
    <script src="js/bootstrap1.min.js"></script>
</body>
