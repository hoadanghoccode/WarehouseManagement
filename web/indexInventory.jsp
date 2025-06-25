<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Management Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 12px 20px;
            border-radius: 8px;
            margin: 2px 0;
            transition: all 0.3s ease;
        }
        .sidebar .nav-link:hover,
        .sidebar .nav-link.active {
            background: rgba(255, 255, 255, 0.1);
            color: white;
            transform: translateX(5px);
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .stat-card-success {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        .stat-card-warning {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        .stat-card-info {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        .table-container {
            background: white;
            border-radius: 15px;
            overflow: hidden;
        }
        .navbar-brand {
            font-weight: bold;
            color: #667eea !important;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 25px;
            padding: 8px 20px;
        }
        .progress {
            height: 8px;
            border-radius: 10px;
        }
        .alert-custom {
            border-left: 4px solid #667eea;
            border-radius: 0 10px 10px 0;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-warehouse me-2"></i>
                WarehouseHub
            </a>
            <div class="navbar-nav ms-auto">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle me-1"></i>
                        John Manager
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Settings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0">
                <div class="sidebar">
                    <div class="p-3">
                        <h6 class="text-white-50 text-uppercase mb-3">Navigation</h6>
                        <nav class="nav flex-column">
                            <a class="nav-link active" href="#dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>
                                Dashboard
                            </a>
                            <a class="nav-link" href="#inventory">
                                <i class="fas fa-boxes me-2"></i>
                                Inventory
                            </a>
                            <a class="nav-link" href="#orders">
                                <i class="fas fa-shopping-cart me-2"></i>
                                Orders
                            </a>
                            <a class="nav-link" href="#receiving">
                                <i class="fas fa-truck-loading me-2"></i>
                                Receiving
                            </a>
                            <a class="nav-link" href="#shipping">
                                <i class="fas fa-shipping-fast me-2"></i>
                                Shipping
                            </a>
                            <a class="nav-link" href="#reports">
                                <i class="fas fa-chart-bar me-2"></i>
                                Reports
                            </a>
                            <a class="nav-link" href="#settings">
                                <i class="fas fa-cog me-2"></i>
                                Settings
                            </a>
                        </nav>
                    </div>
                </div>
            </div>

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
                           
                            </button>
                            <button class="btn btn-outline-primary">
                                <i class="fas fa-download me-1"></i>
                                Export
                            </button>
                        </div>
                    </div>

                    <!-- Stats Cards -->
                    <div class="row mb-4">
                        <div class="col-xl-3 col-md-6 mb-3">
                            <div class="card stat-card">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h6 class="card-title text-white-50">Total Items</h6>
                                            <h3 class="mb-0">12,847</h3>
                                            <small class="text-white-50">
                                                <i class="fas fa-arrow-up me-1"></i>
                                                +5.2% from last month
                                            </small>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-boxes fa-2x opacity-75"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6 mb-3">
                            <div class="card stat-card-success">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h6 class="card-title text-white-50">Orders Today</h6>
                                            <h3 class="mb-0">247</h3>
                                            <small class="text-white-50">
                                                <i class="fas fa-arrow-up me-1"></i>
                                                +12.5% from yesterday
                                            </small>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-shopping-cart fa-2x opacity-75"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6 mb-3">
                            <div class="card stat-card-warning">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h6 class="card-title text-white-50">Low Stock Items</h6>
                                            <h3 class="mb-0">23</h3>
                                            <small class="text-white-50">
                                                <i class="fas fa-exclamation-triangle me-1"></i>
                                                Requires attention
                                            </small>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-exclamation-triangle fa-2x opacity-75"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6 mb-3">
                            <div class="card stat-card-info">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <div>
                                            <h6 class="card-title text-white-50">Revenue Today</h6>
                                            <h3 class="mb-0">$45,892</h3>
                                            <small class="text-white-50">
                                                <i class="fas fa-arrow-up me-1"></i>
                                                +8.1% from yesterday
                                            </small>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-dollar-sign fa-2x opacity-75"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Alerts -->
                    <div class="row mb-4">
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
                    </div>

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
<select id="materialSelectPie" class="form-select mb-3" onchange="drawPieChart()">
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

  

                    <div class="row">
                        <!-- Recent Orders -->
                        <div class="col-lg-8 mb-4">
                            <div class="card table-container">
                                <div class="card-header bg-white">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">Recent Orders</h5>
                                        <a href="#orders" class="btn btn-sm btn-outline-primary">View All</a>
                                    </div>
                                </div>
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Customer</th>
                                                    <th>Items</th>
                                                    <th>Status</th>
                                                    <th>Total</th>
                                                    <th>Date</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td><strong>#ORD-2024-001</strong></td>
                                                    <td>ABC Electronics</td>
                                                    <td>15</td>
                                                    <td><span class="badge bg-success">Shipped</span></td>
                                                    <td>$2,450.00</td>
                                                    <td>2024-01-15</td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><strong>#ORD-2024-002</strong></td>
                                                    <td>Tech Solutions Inc</td>
                                                    <td>8</td>
                                                    <td><span class="badge bg-warning">Processing</span></td>
                                                    <td>$1,890.00</td>
                                                    <td>2024-01-15</td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><strong>#ORD-2024-003</strong></td>
                                                    <td>Global Retail Co</td>
                                                    <td>25</td>
                                                    <td><span class="badge bg-info">Preparing</span></td>
                                                    <td>$3,750.00</td>
                                                    <td>2024-01-14</td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><strong>#ORD-2024-004</strong></td>
                                                    <td>Metro Supplies</td>
                                                    <td>12</td>
                                                    <td><span class="badge bg-danger">Delayed</span></td>
                                                    <td>$1,200.00</td>
                                                    <td>2024-01-14</td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td><strong>#ORD-2024-005</strong></td>
                                                    <td>Prime Distribution</td>
                                                    <td>30</td>
                                                    <td><span class="badge bg-success">Delivered</span></td>
                                                    <td>$4,200.00</td>
                                                    <td>2024-01-13</td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Warehouse Activity -->
                        <div class="col-lg-4 mb-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">Warehouse Activity</h5>
                                </div>
                                <div class="card-body">
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between mb-1">
                                            <small>Storage Capacity</small>
                                            <small>78%</small>
                                        </div>
                                        <div class="progress">
                                            <div class="progress-bar bg-primary" style="width: 78%"></div>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between mb-1">
                                            <small>Orders Processed</small>
                                            <small>92%</small>
                                        </div>
                                        <div class="progress">
                                            <div class="progress-bar bg-success" style="width: 92%"></div>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between mb-1">
                                            <small>Staff Efficiency</small>
                                            <small>85%</small>
                                        </div>
                                        <div class="progress">
                                            <div class="progress-bar bg-info" style="width: 85%"></div>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between mb-1">
                                            <small>Equipment Usage</small>
                                            <small>67%</small>
                                        </div>
                                        <div class="progress">
                                            <div class="progress-bar bg-warning" style="width: 67%"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Quick Actions -->
                            <div class="card mt-4">
                                <div class="card-header">
                                    <h5 class="mb-0">Quick Actions</h5>
                                </div>
                                <div class="card-body">
                                    <div class="d-grid gap-2">
                                        <button class="btn btn-outline-primary btn-sm">
                                            <i class="fas fa-plus me-2"></i>Add New Item
                                        </button>
                                        <button class="btn btn-outline-success btn-sm">
                                            <i class="fas fa-truck me-2"></i>Receive Shipment
                                        </button>
                                        <button class="btn btn-outline-info btn-sm">
                                            <i class="fas fa-barcode me-2"></i>Scan Inventory
                                        </button>
                                        <button class="btn btn-outline-warning btn-sm">
                                            <i class="fas fa-chart-line me-2"></i>Generate Report
                                        </button>
                                    </div>
                                </div>
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
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Simple JavaScript for interactivity
        document.addEventListener('DOMContentLoaded', function() {
            // Add click handlers for navigation
            const navLinks = document.querySelectorAll('.sidebar .nav-link');
            navLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    navLinks.forEach(l => l.classList.remove('active'));
                    this.classList.add('active');
                });
            });

            // Auto-refresh stats (simulation)
            setInterval(function() {
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
            const values = data.map(item => item.totalQuantity);

            const ctx = document.getElementById("importChart").getContext('2d');
            if (window.myChart) {
                window.myChart.destroy();
            }

            window.myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Số lượng nhập',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.7)'
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        } catch (err) {
            console.error("Lỗi khi load biểu đồ:", err);
        }
    }

    // Vẽ mặc định
    drawChart();

    // Khi chọn vật tư
    document.getElementById("materialSelect").addEventListener("change", function () {
        const selected = this.value;
        drawChart(selected);
    });
</script>

<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<!-- Script vẽ biểu đồ -->
<script>
    async function drawChart(materialId = "") {
        try {
            const url = '${pageContext.request.contextPath}/importchart' + (materialId ? '?materialId=' + materialId : '');
            const res = await fetch(url);
            const data = await res.json();

            const labels = data.map(item => "VT-" + item.materialId);
            const values = data.map(item => item.totalQuantity);

            const ctx = document.getElementById("importChart").getContext('2d');
            if (window.myChart) window.myChart.destroy();

            window.myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Số lượng nhập',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.7)'
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        } catch (err) {
            console.error("❌ Lỗi khi load biểu đồ cột:", err);
        }
    }

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
            if (window.pieChart) window.pieChart.destroy();

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
