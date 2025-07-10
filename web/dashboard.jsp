<%-- 
    Document   : dashboard
    Created on : Jun 30, 2025, 9:10:21 AM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.Year" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                                                        <h3 class="mb-1">${activeCount} / ${totalMaterials}</h3>
                                                        <!-- Sửa tại đây -->
                                                        <p class="mb-0 text-muted text-nowrap" style="max-width: 160px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                            Active Materials in Stock
                                                        </p>
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
                            </div>

                            <div class="container mt-4">
                                <h2 class="mb-3">📊 Thống kê số lượng vật tư theo tháng</h2>

                                <!-- Dropdown chọn vật tư và năm -->
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label>Chọn vật tư:</label>
                                        <select id="materialSelectBar" class="form-select">
                                            <option value="" disabled selected>-- Chọn vật tư --</option>
                                            <c:forEach var="m" items="${materials}">
                                                <option value="${m.materialId}">${m.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label>Chọn năm:</label>
                                        <select id="yearSelect" class="form-select">
                                            <option value="" disabled selected>-- Chọn năm --</option>
                                            <% int currentYear = Year.now().getValue(); for (int y = currentYear; y >= 2020; y--) { %>
                                            <option value="<%= y %>"><%= y %></option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>

                                <!-- Canvas biểu đồ cột -->
                                <canvas id="materialChart" height="100"></canvas>

                                <hr class="my-5" />

                                <h5>🍩 Tình trạng sử dụng vật tư</h5>
                                <select id="materialSelectPie" class="form-select mb-3">
                                    <c:forEach var="m" items="${materials}" varStatus="loop">
                                        <option value="${m.materialId}" ${loop.index == 0 ? "selected" : ""}>
                                            ${m.name}
                                        </option>
                                    </c:forEach>
                                </select>

                                <!-- Thu nhỏ biểu đồ tại đây -->
                                <div class="d-flex justify-content-center">
                                    <div style="width: 400px; height: 400px;"> 
                                        <canvas id="materialPieChart"></canvas>
                                    </div>
                                </div>

                                <div>
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
                                                                <th>Material Name</th>
                                                                <th>Unit</th>
                                                                <th>Quality</th>
                                                                <th>Quantity</th>
                                                                <th>Date</th>
                                                                <th>Note</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                               
                                                                <td>${latestTransaction.materialName}</td>
                                                                <td>${latestTransaction.unitName}</td>
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
                                                                <th>Material Name</th>
                                                                <th>Category</th>
                                                                <th>Unit</th>
                                                                <th>Image</th>
                                                                <th>Created At</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="m" items="${todayMaterials}">

                                                                <tr>
                                                                    <td>${m.name}</td>
                                                                    <td>${m.categoryName}</td>
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
                                </div>






                                <script>
                                    // Biểu đồ cột: Thống kê nhập/xuất/tồn theo tháng

                                    async function loadChart() {
                                        const materialSelect = document.getElementById("materialSelectBar");
                                        const yearSelect = document.getElementById("yearSelect");

                                        if (!materialSelect || !yearSelect) {
                                            console.error("❌ Không tìm thấy select vật tư hoặc năm");
                                            return;
                                        }

                                        const selectedMaterialId = materialSelect.value?.trim();
                                        const selectedYear = yearSelect.value?.trim();

                                        if (!selectedMaterialId || !selectedYear) {
                                            console.warn("⛔ Vui lòng chọn vật tư và năm trước khi vẽ biểu đồ");
                                            return;
                                        }

                                        const contextPath = "<%= request.getContextPath() %>";
                                        const url = contextPath + "/material-monthly-chart?materialId=" + selectedMaterialId + "&year=" + selectedYear;
                                        console.log("📊 Final Fetching:", url);

                                        try {
                                            const res = await fetch(url);
                                            if (!res.ok) {
                                                const err = await res.text();
                                                console.error("❌ API Error:", err);
                                                return;
                                            }

                                            const data = await res.json();
                                            const labels = Array.from({length: 12}, (_, i) => "Tháng " + (i + 1));
                                            const importData = Array(12).fill(0);
                                            const exportData = Array(12).fill(0);
                                            const stockData = Array(12).fill(0);
                                            const unitName = data[0]?.unitName || '';

                                            data.forEach(stat => {
                                                const idx = stat.month - 1;
                                                importData[idx] = stat.totalImport || 0;
                                                exportData[idx] = stat.totalExport || 0;
                                                stockData[idx] = stat.stock || 0;
                                            });

                                            const chartCtx = document.getElementById("materialChart").getContext("2d");
                                            if (window.materialChart && typeof window.materialChart.destroy === "function") {
                                                window.materialChart.destroy();
                                            }

                                            window.materialChart = new Chart(chartCtx, {
                                                type: 'bar',
                                                data: {
                                                    labels,
                                                    datasets: [
                                                        {label: 'Nhập', data: importData, backgroundColor: 'rgba(75,192,192,0.7)'},
                                                        {label: 'Xuất', data: exportData, backgroundColor: 'rgba(255,99,132,0.7)'},
                                                        {label: 'Tồn', data: stockData, backgroundColor: 'rgba(255,206,86,0.7)'}
                                                    ]
                                                },
                                                options: {
                                                    responsive: true,
                                                    plugins: {
                                                        title: {
                                                            display: true,

                                                        }
                                                    },
                                                    scales: {
                                                        y: {
                                                            beginAtZero: true,
                                                            title: {
                                                                display: true,
                                                                text: "Số lượng vật tư"
                                                            },
                                                            ticks: {
                                                                callback: function (value) {
                                                                    return value.toLocaleString('vi-VN') + ' ' + unitName;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } catch (err) {
                                            console.error("❌ Lỗi khi fetch hoặc render biểu đồ:", err);
                                        }
                                    }

                                    // Biểu đồ tròn: Tình trạng vật tư
                                    async function drawPieChart() {
                                        const materialId = document.getElementById("materialSelectPie").value;
                                        if (!materialId) {
                                            console.warn("\u2757 Chưa chọn vật tư để vẽ pie chart");
                                            return;
                                        }

                                        const url = "<%= request.getContextPath() %>/materialqualitychart?materialId=" + materialId;
                                        try {
                                            const res = await fetch(url);
                                            const data = await res.json();

                                            if (data.error) {
                                                alert("Lỗi dữ liệu: " + data.error);
                                                return;
                                            }

                                            const ctx = document.getElementById("materialPieChart").getContext("2d");
                                            if (window.pieChart && typeof window.pieChart.destroy === "function") {
                                                window.pieChart.destroy();
                                            }

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
                                            console.error("\u274c Pie Chart API error:", e);
                                        }
                                    }

                                    // DOM loaded
                                    window.addEventListener("DOMContentLoaded", () => {
                                        const matSel = document.getElementById("materialSelectBar");
                                        const yearSel = document.getElementById("yearSelect");
                                        const pieSel = document.getElementById("materialSelectPie");

                                        if (matSel && matSel.options.length > 1 && !matSel.value)
                                            matSel.selectedIndex = 1;
                                        if (yearSel && yearSel.options.length > 1 && !yearSel.value)
                                            yearSel.selectedIndex = 1;

                                        loadChart();
                                        drawPieChart();

                                        matSel?.addEventListener("change", loadChart);
                                        yearSel?.addEventListener("change", loadChart);
                                        pieSel?.addEventListener("change", drawPieChart);
                                    });
                                </script>


                                </body>
                                </html>
