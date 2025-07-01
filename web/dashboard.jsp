<%-- 
    Document   : dashboard
    Created on : Jun 30, 2025, 9:10:21 AM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="model.Users" %>
<%@ page import="model.MaterialTransactionHistory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<%
    Users u = (Users) session.getAttribute("USER");
    if (u == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html lang="zxx">


    <!-- Mirrored from demo.dashboardpack.com/directory-html/index.html by HTTrack Website Copier/3.x [XR&CO'2014], Fri, 16 May 2025 08:22:29 GMT -->
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
    </head>
    <body class="crm_body_bg">





        <!-- main content part here -->

        <!-- sidebar  -->
        <!-- sidebar part here -->
        <%@ include file="sidebar.jsp" %>

        <!-- sidebar part end -->
        <!--/ sidebar  -->


        <section class="main_content dashboard_part">
            <!-- menu  -->
            <%@ include file="navbar.jsp" %>

            <!--/ menu  -->
            <div class="main_content_iner ">
                <div class="container-fluid p-0 sm_padding_15px">
                    <div class="row justify-content-center">
                        <div class="col-12">
                            <div class="dashboard_header mb_50">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="dashboard_header_title">
                                            <p>Hi, <%= u.getFullName() %>!</p>
                                            <h3> Directory Dashboard</h3>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <form method="get" action="dashboard" class="row g-2 justify-content-center align-items-end mb-4">
                            <div class="col-auto">
                                <label for="fromDate" class="form-label">From</label>
                                <input type="date" id="fromDate" name="from" class="form-control" value="${param.from}">
                            </div>
                            <div class="col-auto">
                                <label for="toDate" class="form-label">To</label>
                                <input type="date" id="toDate" name="to" class="form-control" value="${param.to}">
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="fas fa-filter me-1"></i>Filter
                                </button>
                            </div>
                        </form>
                        
                        <!-- Main Content -->
                        <div class="col-md-9 col-lg-10">
                            <div class="p-4">
                                <!-- Header -->
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <div>
                                        <h2 class="mb-1">Warehouse Dashboard</h2>
                                        <p class="text-muted mb-0">Welcome back! Here's what's happening in your warehouse today.</p>
                                    </div>
                                    <div>
                                        <button  class="btn btn-primary me-2">
                                            <i class="fas fa-plus me-1"></i>
                                            <a href="createorder" style="text-decoration: none; color: white;" >New Order</a>
                                            <button class="btn btn-outline-primary">
                                                <i class="fas fa-download me-1"></i>
                                                <a href="exportnotelist" style="text-decoration: none; color: black;" > Export</a>
                                            </button>
                                    </div>
                                </div>

                                <!-- Stats Cards -->
                                <div class="container mt-4">
                                    <div class="row g-4">

                                        <!-- Tổng vật tư -->
                                        <div class="col-md-6 col-xl-3">
                                            <div class="card shadow-sm border-0">
                                                <div class="card-body d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h3 class="mb-1">${totalMaterials}</h3>
                                                        <p class="mb-0 text-muted">Total Material</p>
                                                    </div>
                                                    <i class="fas fa-cubes fa-2x text-primary"></i>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Tổng tồn kho -->
                                        <div class="col-md-6 col-xl-3">
                                            <div class="card shadow-sm border-0">
                                                <div class="card-body d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h3 class="mb-1">${totalInventory}</h3>
                                                        <p class="mb-0 text-muted">Total Inventory</p>
                                                    </div>
                                                    <i class="fas fa-warehouse fa-2x text-success"></i>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Đơn nhập hôm nay -->
                                        <div class="col-md-6 col-xl-3">
                                            <div class="card shadow-sm border-0">
                                                <div class="card-body d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h3 class="mb-1">${todayImports}</h3>
                                                        <p class="mb-0 text-muted">Import Today</p>
                                                    </div>
                                                    <i class="fas fa-truck-loading fa-2x text-warning"></i>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Đơn xuất hôm nay -->
                                        <div class="col-md-6 col-xl-3">
                                            <div class="card shadow-sm border-0">
                                                <div class="card-body d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h3 class="mb-1">${todayExports}</h3>
                                                        <p class="mb-0 text-muted">Export Today</p>
                                                    </div>
                                                    <i class="fas fa-dolly-flatbed fa-2x text-danger"></i>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <!-- Alerts -->
                            <!--                                <div class="row mb-4">
                                                                <div class="col-12">
                                                                    <div class="alert alert-warning alert-custom" role="alert">
                                                                        <div class="d-flex align-items-center">
                                                                            <i class="fas fa-exclamation-triangle me-3"></i>
                                                                            <div>
                                                                                <strong>Low Stock Alert:</strong> 23 items are running low on stock. 
                                                                                <a href="#inventory" class="alert-link">View inventory</a> to reorder.
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>-->

                            <!-- Charts and Tables Row -->

                            <!-- Charts and Tables Row -->
                            <!-- Dropdown chọn vật tư -->
                            <select id="materialSelect" class="form-select mb-3">
                                <option value="">-- Tất cả vật tư --</option>
                                <c:forEach var="m" items="${materials}">
                                    <option value="${m.materialId}">${m.name}</option>
                                </c:forEach>
                            </select>

                            <!-- Canvas biểu đồ -->
                            <canvas id="importChart" height="100"></canvas>

                            <hr class="my-4"/>

                            <h5 class="mt-4">🍩 Tình trạng sử dụng vật tư</h5>

                            <!-- Dropdown chọn vật tư -->
                            <select id="materialSelectPie" class="form-select mb-3">
                                <c:forEach var="m" items="${materials}" varStatus="loop">
                                    <option value="${m.materialId}" ${loop.index == 0 ? "selected" : ""}>
                                        ${m.name}
                                    </option>
                                </c:forEach>
                            </select>

                            <!-- Canvas biểu đồ pie -->
                            <style>
                                /* Container để căn giữa biểu đồ */
                                .chart-container {
                                    width: 100%;
                                    display: flex;
                                    justify-content: center;
                                    margin-top: 20px;
                                }

                                /* Canvas biểu đồ */
                                #materialPieChart {
                                    width: 500px !important;
                                    height: 500px !important;
                                }

                                /* Tăng kích thước chữ phần chú thích */
                                .chart-legend-text {
                                    font-size: 18px !important;
                                    font-weight: bold;
                                }
                            </style>

                            <div class="chart-container">
                                <canvas id="materialPieChart"></canvas>
                            </div>

                            <div class="card mb-4">
                                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">📌Recent Transaction</h5>
                                </div>
                                <div class="card-body p-0">
                                    <c:if test="${not empty latestTransaction}">
                                        <div class="table-responsive">
                                            <table class="table table-hover mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Material Name</th>
                                                        <th>SubUnit</th>
                                                        <th>Quality</th>
                                                        <th>Quantity</th>
                                                        <th>Date</th>
                                                        <th>Note</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td>${latestTransaction.materialTransactionHistoryId}</td>
                                                        <td>${latestTransaction.materialName}</td>
                                                        <td>${latestTransaction.subUnitName}</td>
                                                        <td>${latestTransaction.qualityName}</td>
                                                        <td>${latestTransaction.quantity}</td>
                                                        <td>${latestTransaction.transactionDate}</td>
                                                        <td>${latestTransaction.note}</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty latestTransaction}">
                                        <div class="p-3 text-muted">Không có giao dịch nào gần đây.</div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="card mb-4">
                                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">📌 New Material</h5>
                                </div>
                                <div class="card-body p-0">
                                    <c:if test="${not empty todayMaterials}">
                                        <p style="padding: 10px; font-weight: bold; color: green;">
                                        </p>
                                        <div class="table-responsive">
                                            <table class="table table-hover mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Material Name</th>
                                                        <th>Category</th>
                                                        <th>Supplier</th>
                                                        <th>Unit</th>
                                                        <th>Image</th>
                                                        <th>Created At</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="m" items="${todayMaterials}">

                                                        <tr>
                                                            <td>${m.materialId}</td>
                                                            <td>${m.name}</td>
                                                            <td>${m.categoryName}</td>
                                                            <td>${m.supplierName}</td>
                                                            <td>${m.unitName}</td>
                                                            <td><img src="${m.image}" alt="ảnh vật tư" style="height:40px;border-radius:4px;" /></td>
                                                            <td>${m.createAt}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty todayMaterials}">
                                        <div class="p-3 text-muted">Không có vật tư mới trong ngày hôm nay.</div>
                                    </c:if>
                                </div>
                            </div>

                            <div
                                <!-- Quick Actions -->
                                <div class="card mt-4">
                                    <div class="card-header">
                                        <h5 class="mb-0">Quick Actions</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="d-grid gap-2">
                                            <button class="btn btn-outline-primary btn-sm">
                                                <i class="fas fa-plus me-2"></i>New Import Note
                                            </button>
                                            <button class="btn btn-outline-primary btn-sm">
                                                <i class="fas fa-plus me-2"></i>New Export Note
                                            </button>
                                            <button class="btn btn-outline-warning btn-sm">
                                                <i class="fas fa-chart-line me-2"></i>Generate Report
                                            </button>
                                        </div>
                                    </div>
                                </div>


                                <!-- Low Stock Items -->
                                <div class="row">
                                    <div class="col-12">
                                        <div class="card table-container">
                                            <div class="card-header bg-white">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <h5 class="mb-0">Low Stock Items</h5>
                                                    <a href="#inventory" class="btn btn-sm btn-outline-danger">Reorder All</a>
                                                </div>
                                            </div>
                                            <div class="card-body p-0">
                                                <div class="table-responsive">
                                                    <table class="table table-hover mb-0">
                                                        <thead class="table-light">
                                                            <tr>
                                                                <th>SKU</th>
                                                                <th>Product Name</th>
                                                                <th>Category</th>
                                                                <th>Current Stock</th>
                                                                <th>Min. Required</th>
                                                                <th>Location</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td><strong>SKU-001</strong></td>
                                                                <td>Wireless Headphones</td>
                                                                <td>Electronics</td>
                                                                <td><span class="badge bg-danger">5</span></td>
                                                                <td>20</td>
                                                                <td>A-1-15</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-primary">Reorder</button>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><strong>SKU-045</strong></td>
                                                                <td>USB Cables</td>
                                                                <td>Accessories</td>
                                                                <td><span class="badge bg-warning">8</span></td>
                                                                <td>50</td>
                                                                <td>B-2-08</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-primary">Reorder</button>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><strong>SKU-123</strong></td>
                                                                <td>Laptop Chargers</td>
                                                                <td>Electronics</td>
                                                                <td><span class="badge bg-danger">3</span></td>
                                                                <td>15</td>
                                                                <td>A-3-22</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-primary">Reorder</button>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                            <script>
                                // Simple JavaScript for interactivity
                                document.addEventListener('DOMContentLoaded', function () {
                                    // Add click handlers for navigation
                                    const navLinks = document.querySelectorAll('.sidebar .nav-link');
                                    navLinks.forEach(link => {
                                        link.addEventListener('click', function (e) {
                                            e.preventDefault();
                                            navLinks.forEach(l => l.classList.remove('active'));
                                            this.classList.add('active');
                                        });
                                    });
                                    // Auto-refresh stats (simulation)
                                    setInterval(function () {
                                        const statsElements = document.querySelectorAll('.card h3');
                                        statsElements.forEach(element => {
                                            if (element.textContent.includes('$')) {
                                                const currentValue = parseFloat(element.textContent.replace(/[$,]/g, ''));
                                                const newValue = currentValue + Math.floor(Math.random() * 100);
                                                element.textContent = '$' + newValue.toLocaleString();
                                            }
                                        });
                                    }, 30000); // Update every 30 seconds
                                });
                            </script>

                            <!-- Chart.js + script gọi dữ liệu -->
                            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

                            <script>
                                async function drawChart(materialId = "") {
                                    try {
                                        const res = await fetch(
                                                '${pageContext.request.contextPath}/importchart' + (materialId ? '?materialId=' + materialId : '')
                                                );
                                        const data = await res.json();

                                        const labels = data.map(item => "VT-" + item.materialId);
                                        const importData = data.map(item => item.totalImport);
                                        const exportData = data.map(item => item.totalExport);
                                        const stockData = data.map(item => item.stock);

                                        const ctx = document.getElementById("importChart").getContext('2d');
                                        if (window.myChart)
                                            window.myChart.destroy();

                                        window.myChart = new Chart(ctx, {
                                            type: 'bar',
                                            data: {
                                                labels: labels,
                                                datasets: [
                                                    {
                                                        label: 'Nhập',
                                                        data: importData,
                                                        backgroundColor: 'rgba(75, 192, 192, 0.7)'
                                                    },
                                                    {
                                                        label: 'Xuất',
                                                        data: exportData,
                                                        backgroundColor: 'rgba(255, 99, 132, 0.7)'
                                                    },
                                                    {
                                                        label: 'Tồn',
                                                        data: stockData,
                                                        backgroundColor: 'rgba(255, 206, 86, 0.7)'
                                                    }
                                                ]
                                            },
                                            options: {
                                                responsive: true,
                                                scales: {
                                                    y: {
                                                        beginAtZero: true
                                                    }
                                                },
                                                plugins: {
                                                    legend: {
                                                        position: 'top'
                                                    },
                                                    title: {
                                                        display: true,
                                                        text: 'Thống kê vật tư'
                                                    }
                                                }
                                            }
                                        });
                                    } catch (err) {
                                        console.error("❌ Lỗi khi load biểu đồ:", err);
                                }
                                }

                                // Gọi khi load và khi chọn vật tư
                                window.onload = function () {
                                    drawChart();
                                    const select = document.getElementById("materialSelect");
                                    if (select) {
                                        select.addEventListener("change", function () {
                                            drawChart(this.value);
                                        });
                                    }
                                };
                            </script>

                            <!-- Chart.js CDN -->
                            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

                            <!-- Script vẽ biểu đồ -->
                            <script>
                                async function drawChart(materialId = "") {
                                    try {
                                        const url = '${pageContext.request.contextPath}/importchart' + (materialId ? '?materialId=' + materialId : '');
                                        const res = await fetch(url);
                                        if (!res.ok) {
                                            const errText = await res.text();
                                            console.error("❌ API Error:", errText);
                                            alert("Không lấy được dữ liệu biểu đồ.");
                                            return;
                                        }

                                        const data = await res.json();

                                        const labels = data.map(item => "VT-" + item.materialId);
                                        const importData = data.map(item => item.totalImport);
                                        const exportData = data.map(item => item.totalExport);
                                        const stockData = data.map(item => item.stock);

                                        const ctx = document.getElementById("importChart").getContext('2d');
                                        if (window.myChart)
                                            window.myChart.destroy();

                                        window.myChart = new Chart(ctx, {
                                            type: 'bar',
                                            data: {
                                                labels: labels,
                                                datasets: [
                                                    {
                                                        label: 'Nhập',
                                                        data: importData,
                                                        backgroundColor: 'rgba(75, 192, 192, 0.7)'
                                                    },
                                                    {
                                                        label: 'Xuất',
                                                        data: exportData,
                                                        backgroundColor: 'rgba(255, 99, 132, 0.7)'
                                                    },
                                                    {
                                                        label: 'Tồn',
                                                        data: stockData,
                                                        backgroundColor: 'rgba(255, 206, 86, 0.7)'
                                                    }
                                                ]
                                            },
                                            options: {
                                                responsive: true,
                                                scales: {
                                                    y: {
                                                        beginAtZero: true
                                                    }
                                                },
                                                plugins: {
                                                    legend: {
                                                        position: 'top'
                                                    },
                                                    title: {
                                                        display: true,
                                                        text: 'Thống kê vật tư'
                                                    }
                                                }
                                            }
                                        });
                                    } catch (err) {
                                        console.error("❌ Lỗi khi load biểu đồ:", err);
                                }
                                }
                            </script>
                            <script>
                                async function drawPieChart() {
                                    const select = document.getElementById("materialSelectPie");
                                    if (!select) {
                                        console.error("Không tìm thấy dropdown 'materialSelectPie'");
                                        return;
                                    }

                                    const materialId = select.value;
                                    console.log("📥 Selected materialId =", materialId);
                                    if (!materialId || materialId.trim() === "") {
                                        alert("Vui lòng chọn vật tư hợp lệ!");
                                        return;
                                    }

                                    try {
                                        const url = '${pageContext.request.contextPath}/materialqualitychart?materialId=' + materialId;
                                        console.log("📡 Fetching URL:", url);
                                        const res = await fetch(url);
                                        const data = await res.json();
                                        console.log("📦 Server response:", data);
                                        if (data.error) {
                                            alert("❌ Lỗi: " + data.error);
                                            return;
                                        }

                                        const ctx = document.getElementById("materialPieChart").getContext("2d");
                                        if (window.pieChart)
                                            window.pieChart.destroy();
                                        window.pieChart = new Chart(ctx, {
                                            type: 'pie',
                                            data: {
                                                labels: Object.keys(data),
                                                datasets: [{
                                                        label: 'Tình trạng',
                                                        data: Object.values(data),
                                                        backgroundColor: ['#36A2EB', '#FF6384']
                                                    }]
                                            },
                                            options: {
                                                responsive: true,
                                                plugins: {
                                                    legend: {
                                                        position: 'bottom'
                                                    }
                                                }
                                            }
                                        });
                                    } catch (e) {
                                        console.error("❌ Lỗi khi gọi API:", e);
                                        alert("Không thể vẽ biểu đồ tròn");
                                    }
                                }

                                window.onload = function () {
                                    // Vẽ biểu đồ cột
                                    drawChart();
                                    // Gán sự kiện thay đổi dropdown biểu đồ cột
                                    const selectBar = document.getElementById("materialSelect");
                                    if (selectBar) {
                                        selectBar.addEventListener("change", function () {
                                            drawChart(this.value);
                                        });
                                    }

                                    // Gán sự kiện và vẽ biểu đồ tròn
                                    const selectPie = document.getElementById("materialSelectPie");
                                    if (selectPie) {
                                        selectPie.addEventListener("change", drawPieChart);
                                    }

                                    drawPieChart();
                                };
                            </script>

                            </body>
                            </html>
