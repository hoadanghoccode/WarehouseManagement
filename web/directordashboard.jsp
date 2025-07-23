<%-- 
    Document   : directordashboard
    Created on : Jul 20, 2025, 3:06:07 AM
    Author     : duong
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <title>Director Dashboard</title>

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
            <div class="row">
                <div class="col-12 px-4">
                    <div class="d-flex justify-content-between align-items-center">

                        <div class="dashboard_header_title">
                            <p>Hi, <%= u.getFullName() %>!</p>
                            <h3>Director Dashboard</h3>
                        </div>

                        <!-- Thanh tìm ki?m -->
                        <form action="directordashboard" method="get" class="d-flex me-4" style="max-width: 300px;">
                            <input type="text" name="globalSearch" class="form-control me-2" placeholder="Search ..." value="${param.globalSearch}">
                            <button class="btn btn-outline-primary" type="submit">Search</button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="container mt-4">
                <div class="row row-cols-2 row-cols-md-4 row-cols-xl-7 g-3">
                    <!-- (KPIs remain unchanged) -->
                    <!-- Total Material -->
                    <div class="col">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <h3 class="mb-1">${totalMaterials}</h3>
                                    <p class="mb-0 text-muted">Total Material</p>
                                </div>
                                <i class="fas fa-cubes fa-2x text-primary"></i>
                            </div>
                        </div>
                    </div>
                    <!-- Import Today -->
                    <div class="col">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <h3 class="mb-1">${importToday}</h3>
                                    <p class="mb-0 text-muted">Import Today</p>
                                </div>
                                <i class="fas fa-truck-loading fa-2x text-warning"></i>
                            </div>
                        </div>
                    </div>
                    <!-- Export Today -->
                    <div class="col">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <h3 class="mb-1">${exportToday}</h3>
                                    <p class="mb-0 text-muted">Export Today</p>
                                </div>
                                <i class="fas fa-dolly-flatbed fa-2x text-danger"></i>
                            </div>
                        </div>
                    </div>
                    <!-- Transactions Today -->
                    <div class="col">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body d-flex justify-content-between align-items-center">
                                <div>
                                    <h3 class="mb-1">${totalTxnToday}</h3>
                                    <p class="mb-0 text-muted">Transactions Today</p>
                                </div>
                                <i class="fas fa-exchange-alt fa-2x text-secondary"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts and Alerts Row -->
                <div class="row mt-5">
                    <!-- Pie Chart (Left) -->
                    <div class="col-md-6">
                        <h4>Material Usage Status</h4>
                        <label for="materialId">Material</label>
                        <select id="materialSelectPie" class="form-select mb-3">

                            <c:forEach var="m" items="${materials}" varStatus="loop">
                                <option value="${m.materialId}" ${loop.index == 0 ? "selected" : ""}>${m.name}</option>
                            </c:forEach>
                        </select>
                        <div class="d-flex justify-content-center">
                            <div style="width: 100%; max-width: 400px; height: 400px;">
                                <canvas id="materialPieChart"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Inventory Alerts (Right) -->
                    <div class="col-md-6">
                        <h4>Inventory Alerts</h4>
                        <div class="card-body p-0">
                            <!-- Filter Form -->
                            <!-- Filter Form -->
                            <form method="get" action="directordashboard" class="mb-3">
                                <div class="row g-2 align-items-end">
                                    <!-- Material -->
                                    <div class="col-md-3">
                                        <label for="alertMaterialId">Material</label>
                                        <select name="alertMaterialId" class="form-control">
                                            <option value="">All</option>
                                            <c:forEach var="m" items="${materials}">
                                                <option value="${m.materialId}" ${m.materialId == alertMaterialId ? "selected" : ""}>
                                                    ${m.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>



                                    <!-- Button -->
                                    <div class="col-md-2 d-grid">
                                        <button type="submit" class="btn btn-primary mt-1">Filter</button>
                                    </div>
                                </div>
                            </form>
                            <!-- Inventory Alert Table -->
                            <div class="card">
                                <table class="table table-striped mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Material</th>
                                            <th>Unit</th>
                                            <th>Quantity</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty inventoryAlerts}">
                                                <c:forEach var="alert" items="${inventoryAlerts}">
                                                    <tr>
                                                        <td><i class="fas fa-exclamation-triangle text-danger me-2"></i>${alert.materialName}</td>
                                                        <td>${alert.unitName}</td>
                                                        <td>${alert.quantity}</td>
                                                        <td><span class="badge bg-danger">Low</span></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="4" class="text-center text-muted">No low inventory materials found.</td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav class="mt-3" aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="directordashboard?alertPage=${i}&alertSearch=${alertSearch}&alertMaterialId=${alertMaterialId}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>
                            </c:if>
                        </div>
                    </div>


                    <!-- Transaction Filters -->
                    <h4>Transaction Table</h4>
                    <form method="get" action="directordashboard" class="mb-3">
                        <div class="row g-2 align-items-end">

                            <!-- From Date -->
                            <div class="col-md-2">
                                <label for="txnFromDate">From</label>
                                <input type="date" class="form-control" name="txnfromDate" value="${txnfromDate}">
                            </div>

                            <!-- To Date -->
                            <div class="col-md-2">
                                <label for="txnToDate">To</label>
                                <input type="date" class="form-control" name="txntoDate" value="${txntoDate}">
                            </div>

                            <!-- Material -->
                            <div class="col-md-3">
                                <label for="txnMaterialId">Material</label>
                                <select name="txnMaterialId" class="form-control">
                                    <option value="">All</option>
                                    <c:forEach var="m" items="${materials}">
                                        <option value="${m.materialId}" ${m.materialId == txnMaterialId ? "selected" : ""}>${m.name}</option>
                                    </c:forEach>
                                </select>
                            </div>



                            <!-- Button -->
                            <div class="col-md-2 d-grid">
                                <button type="submit" class="btn btn-primary mt-1">Filter</button>
                            </div>

                        </div>
                    </form>

                    <!-- Transaction Table -->
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Material</th>
                                <th>Unit</th>
                                <th>Quality</th>
                                <th>Quantity</th>
                                <th>Date</th>
                                <th>Note</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty transactionList}">
                                    <c:forEach var="txn" items="${transactionList}">
                                        <tr>
                                            <td>${txn.materialName}</td>
                                            <td>${txn.unitName}</td>
                                            <td>${txn.qualityName}</td>
                                            <td>${txn.quantity}</td>
                                            <td><fmt:formatDate value="${txn.transactionDate}" pattern="yyyy-MM-dd"/></td>
                                            <td>${txn.note}</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center">No transactions found.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>

                    <!-- Transaction Paging -->
                    <div class="d-flex justify-content-center">
                        <c:forEach begin="1" end="${totalTxnPages}" var="i">
                            <a href="directordashboard?txnPage=${i}&txnSearch=${txnSearch}&txnMaterialId=${txnMaterialId}&txnFromDate=${fromDate}&txnToDate=${toDate}"
                               class="btn btn-sm ${i == currentTxnPage ? 'btn-dark' : 'btn-light'} mx-1">${i}</a>
                        </c:forEach>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <!-- New Materials Section -->
                            <div class="col-md-6">
                                <h4>New Materials Today</h4>
                                <form method="get" action="directordashboard" class="row g-2 mb-3">
                                    <div class="col-md-4 d-flex">
                                        <input type="text" class="form-control me-2" name="newMaterialSearch" placeholder="Search..." value="${newMaterialSearch}">
                                        <button type="submit" class="btn btn-primary">Search</button>
                                    </div>
                                </form>

                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th>Material</th>
                                            <th>Unit</th>
                                            <th>Category</th>
                                            <th>Image</th>
                                            <th>Created At</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty newMaterials}">
                                                <tr>
                                                    <td colspan="5" class="text-center text-muted">No new material today</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="m" items="${newMaterials}">
                                                    <tr>
                                                        <td>${m.name}</td>
                                                        <td>${m.unitName}</td>                  
                                                        <td>${m.categoryName}</td>
                                                        <td><img src="${m.image}" alt="${m.name}" width="50" height="50"></td>
                                                        <td><fmt:formatDate value="${m.createAt}" pattern="yyyy-MM-dd"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>

                                <!-- Pagination for New -->
                                <div class="d-flex justify-content-center">
                                    <c:forEach begin="1" end="${totalNewPages}" var="i">
                                        <a href="directordashboard?newPage=${i}&newFromDate=${newFromDate}&newToDate=${newToDate}&newMaterialSearch=${newMaterialSearch}"
                                           class="btn btn-sm ${i == currentNewPage ? 'btn-dark' : 'btn-light'} mx-1">${i}</a>
                                    </c:forEach>
                                </div>
                            </div>

                            <!-- Updated Materials Section -->
                            <div class="col-md-6">
                                <h4>Material Updated Today</h4>
                                <form method="get" action="directordashboard" class="row g-2 mb-3">
                                    <div class="col-md-4 d-flex">
                                        <input type="text" class="form-control me-2" name="updatedMaterialSearch" placeholder="Search..." value="${updatedMaterialSearch}">
                                        <button type="submit" class="btn btn-primary">Search</button>
                                    </div>
                                </form>

                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th>Material Name</th>
                                            <th>Unit</th>
                                            <th>Category</th>
                                            <th>Image</th>
                                            <th>Updated At</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty updatedMaterials}">
                                                <tr>
                                                    <td colspan="5" class="text-center text-muted">No updated material today</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="m" items="${updatedMaterials}">
                                                    <tr>
                                                        <td>${m.name}</td>
                                                        <td>${m.unitName}</td>
                                                        <td>${m.categoryName}</td>
                                                        <td><img src="${m.image}" alt="${m.name}" width="50" height="50"></td>
                                                        <td><fmt:formatDate value="${m.updatedAt}" pattern="yyyy-MM-dd"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>

                                <!-- Pagination for Updated -->
                                <div class="d-flex justify-content-center">
                                    <c:forEach begin="1" end="${totalUpdatedPages}" var="i">
                                        <a href="directordashboard?updatedPage=${i}&updatedFromDate=${updatedFromDate}&updatedToDate=${updatedToDate}&updatedMaterialSearch=${updatedMaterialSearch}"
                                           class="btn btn-sm ${i == currentUpdatedPage ? 'btn-dark' : 'btn-light'} mx-1">${i}</a>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>

                    <footer class="mt-5 bg-light py-3 border-top">
                        <div class="container text-center">
                            <p class="mb-1">&copy; Material Management System. All rights reserved.</p>
                            <p class="mb-0">
                                <a href="contact.jsp" class="me-3 text-dark"><i class="bi bi-envelope-fill me-1"></i>Contact</a>
                                <!--                                            <a href="/privacy" class="me-3 text-dark"><i class="bi bi-shield-lock-fill me-1"></i>Privacy</a>
                                                                            <a href="/terms" class="text-dark"><i class="bi bi-file-earmark-text-fill me-1"></i>Terms</a>-->
                            </p>
                        </div>
                    </footer>


                    <!-- Chart.js Config -->
                    <script>
                        async function drawPieChart() {
                            const materialId = document.getElementById("materialSelectPie").value;
                            if (!materialId) {
                                console.warn("\u2757 Ch?a ch?n v?t t? ?? v? pie chart");
                                return;
                            }

                            const url = "<%= request.getContextPath() %>/materialqualitychart?materialId=" + materialId;
                            try {
                                const res = await fetch(url);
                                const data = await res.json();

                                if (data.error) {
                                    alert("L?i d? li?u: " + data.error);
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
                                                label: 'Status',
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
                            const pieSel = document.getElementById("materialSelectPie");
                            drawPieChart();
                            pieSel?.addEventListener("change", drawPieChart);
                        });

                        const topExportedCtx = document.getElementById('topExportedChart');
                        new Chart(topExportedCtx, {
                            type: 'bar',
                            data: {
                                labels: [<c:forEach var="m" items="${topExported}">'${m.name}',</c:forEach>],
                                        datasets: [{
                                                label: 'Export Quantity',
                                                data: [<c:forEach var="m" items="${topExported}">${m.totalExported},</c:forEach>],
                                                backgroundColor: 'rgba(54, 162, 235, 0.5)'
                                            }]
                            },
                            options: {responsive: true, plugins: {legend: {display: false}}}
                        });
                    </script>
                    </body>
                    </html>

