<%-- 
    Document   : index
    Created on : Jul 16, 2025
    Author     : Grok
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="model.Users" %>
<%
    Users u = (Users) session.getAttribute("USER");
    if (u == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>Warehouse Management - Welcome</title>
    <link rel="icon" href="img/logo.png" type="image/png">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <!-- Themefy CSS -->
    <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
    <!-- Swiper Slider CSS -->
    <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
    <!-- Select2 CSS -->
    <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
    <!-- Nice Select CSS -->
    <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
    <!-- Owl Carousel CSS -->
    <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
    <!-- Gijgo CSS -->
    <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
    <!-- Date Picker CSS -->
    <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
    <!-- Datatable CSS -->
    <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
    <!-- Text Editor CSS -->
    <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
    <!-- Morris CSS -->
    <link rel="stylesheet" href="vendors/morris/morris.css" />
    <!-- Material Icon CSS -->
    <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
    <!-- Menu CSS -->
    <link rel="stylesheet" href="css/metisMenu.css" />
    <!-- Style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <!-- Custom CSS for Welcome Section -->
    <style>
        .welcome-section {
            font-family: 'Poppins', sans-serif;
            text-align: center;
            padding: 40px 20px;
            max-width: 800px;
            margin: 0 auto;
        }
        .welcome-card {
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 40px;
            margin: 20px;
        }
        .welcome-card h1 {
            font-size: 2.5rem;
            font-weight: 600;
            color: #1a3c6d;
            margin-bottom: 20px;
        }
        .welcome-card p {
            font-size: 1.2rem;
            color: #555;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        .btn-explore {
            background: #1a3c6d;
            color: #fff;
            padding: 12px 30px;
            font-size: 1.1rem;
            border-radius: 50px;
            text-decoration: none;
            transition: background 0.3s ease, transform 0.3s ease;
        }
        .btn-explore:hover {
            background: #2a5c9d;
            transform: translateY(-3px);
        }
        .icon {
            font-size: 3rem;
            color: #1a3c6d;
            margin-bottom: 20px;
        }
        @media (max-width: 576px) {
            .welcome-card h1 {
                font-size: 1.8rem;
            }
            .welcome-card p {
                font-size: 1rem;
            }
            .btn-explore {
                padding: 10px 20px;
                font-size: 1rem;
            }
        }
    </style>
</head>
<body class="crm_body_bg">
    <!-- Sidebar -->
    <%@ include file="sidebar.jsp" %>

    <!-- Main Content -->
    <section class="main_content dashboard_part">
        <!-- Navbar -->
        <%@ include file="navbar.jsp" %>

        <!-- Welcome Section -->
        <div class="main_content_iner">
            <div class="welcome-section">
                <div class="welcome-card">
                    <i class="fas fa-warehouse icon"></i>
                    <h1>Welcome, <%= u.getFullName() %>!</h1>
                    <p>
                        Discover the power of efficient inventory control with our Warehouse Management System. 
                        Streamline your operations, track inventory in real-time, and optimize your workflow with ease.
                    </p>
                    <a href="dashboard.jsp" class="btn-explore">Explore Dashboard</a>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer_part">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="footer_iner text-center">
                            <p>2025 © Influence - Designed by <a href="/WarehouseManagement/index.jsp"> Directory</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Scripts -->
    <script src="js/jquery1-3.4.1.min.js"></script>
    <script src="js/popper1.min.js"></script>
    <script src="js/bootstrap1.min.js"></script>
    <script src="js/metisMenu.js"></script>
    <script src="vendors/count_up/jquery.waypoints.min.js"></script>
    <script src="vendors/chartlist/Chart.min.js"></script>
    <script src="vendors/count_up/jquery.counterup.min.js"></script>
    <script src="vendors/swiper_slider/js/swiper.min.js"></script>
    <script src="vendors/niceselect/js/jquery.nice-select.min.js"></script>
    <script src="vendors/owl_carousel/js/owl.carousel.min.js"></script>
    <script src="vendors/gijgo/gijgo.min.js"></script>
    <script src="vendors/datatable/js/jquery.dataTables.min.js"></script>
    <script src="vendors/datatable/js/dataTables.responsive.min.js"></script>
    <script src="vendors/datatable/js/dataTables.buttons.min.js"></script>
    <script src="vendors/datatable/js/buttons.flash.min.js"></script>
    <script src="vendors/datatable/js/jszip.min.js"></script>
    <script src="vendors/datatable/js/pdfmake.min.js"></script>
    <script src="vendors/datatable/js/vfs_fonts.js"></script>
    <script src="vendors/datatable/js/buttons.html5.min.js"></script>
    <script src="vendors/datatable/js/buttons.print.min.js"></script>
    <script src="vendors/datepicker/datepicker.js"></script>
    <script src="vendors/datepicker/datepicker.en.js"></script>
    <script src="vendors/datepicker/datepicker.custom.js"></script>
    <script src="js/chart.min.js"></script>
    <script src="vendors/progressbar/jquery.barfiller.js"></script>
    <script src="vendors/tagsinput/tagsinput.js"></script>
    <script src="vendors/text_editor/summernote-bs4.js"></script>
    <script src="vendors/am_chart/amcharts.js"></script>
    <script src="vendors/apex_chart/apexcharts.js"></script>
    <script src="vendors/apex_chart/apex_realestate.js"></script>
    <script src="vendors/chart_am/core.js"></script>
    <script src="vendors/chart_am/charts.js"></script>
    <script src="vendors/chart_am/animated.js"></script>
    <script src="vendors/chart_am/kelly.js"></script>
    <script src="vendors/chart_am/chart-custom.js"></script>
    <script src="js/custom.js"></script>
    <script src="vendors/apex_chart/bar_active_1.js"></script>
    <script src="vendors/apex_chart/apex_chart_list.js"></script>
    <!-- Font Awesome JS for icons -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
</body>
</html>