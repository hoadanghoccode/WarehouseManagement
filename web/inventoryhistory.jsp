<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory History</title>
    <!-- Retain all provided CSS links -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" href="img/logo.png" type="image/png">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
    <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
    <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
    <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
    <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
    <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
    <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
    <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
    <link rel="stylesheet" href="vendors/morris/morris.css" />
    <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
    <link rel="stylesheet" href="css/metisMenu.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
    <style>
        body {
            font-family: "Segoe UI", Tahoma, sans-serif;
            background-color: #f3f4f6;
            margin: 0;
            padding: 0;
            color: #374151;
        }
        .main_content {
            flex: 1;
            margin-left: 260px;
            padding: 0;
            background-color: #f8fafc;
            min-height: 100vh;
            transition: margin-left 0.3s ease;
        }
        .container {
            padding-top: 24px;
            padding-bottom: 24px;
            padding-left: 290px;
            max-width: 1404px;
            margin: 0 auto;
        }
        .title {
            font-size: 28px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
        }
        .subtitle {
            font-size: 16px;
            color: #6b7280;
            margin-bottom: 24px;
        }
        .breadcrumb-container {
            background-color: white;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 24px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.08);
        }
        .breadcrumb {
            background: none;
            padding: 0;
            margin: 0;
        }
        .breadcrumb-item a {
            color: #3b82f6;
            text-decoration: none;
        }
        .breadcrumb-item a:hover {
            text-decoration: underline;
        }
        .material-info-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            padding: 24px;
            margin-bottom: 24px;
            color: white;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        .material-name {
            font-size: 24px;
            font-weight: 600;
            margin-bottom: 8px;
        }
        .material-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 16px;
            margin-top: 16px;
        }
        .detail-item {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }
        .detail-label {
            font-size: 12px;
            opacity: 0.8;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .detail-value {
            font-size: 16px;
            font-weight: 500;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 16px;
            margin-bottom: 24px;
        }
        .stat-card {
            background-color: white;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .stat-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        }
        .stat-icon {
            font-size: 32px;
            margin-bottom: 12px;
        }
        .stat-icon.import {
            color: #22c55e;
        }
        .stat-icon.export {
            color: #ef4444;
        }
        .stat-value {
            font-size: 24px;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 4px;
        }
        .stat-label {
            font-size: 14px;
            color: #6b7280;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .filters-card {
            background-color: white;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 24px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.08);
        }
        .filters-title {
            font-size: 18px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .table-container {
            overflow-x: auto;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        table.table {
            width: 100%;
            border-collapse: collapse;
            min-width: 1000px;
            margin: 0;
        }
        table.table th, table.table td {
            padding: 16px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
            font-size: 14px;
            vertical-align: middle;
        }
        table.table th {
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            font-weight: 600;
            color: #1f2937;
            position: sticky;
            top: 0;
            z-index: 10;
        }
        table.table tbody tr:nth-child(even) {
            background-color: #f9fafb;
        }
        table.table tbody tr:hover {
            background-color: inherit;
        }
        .quantity-cell {
            font-weight: 600;
        }
        .date-badge {
            background: linear-gradient(135deg, #3b82f6, #1d4ed8);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
        }
        .no-data {
            text-align: center;
            padding: 40px;
            background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
            border-radius: 12px;
            font-size: 16px;
            color: #6b7280;
            margin: 20px;
        }
        .no-data i {
            font-size: 48px;
            margin-bottom: 16px;
            opacity: 0.5;
        }
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 32px;
        }
        .pagination a, .pagination span {
            padding: 10px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            text-decoration: none;
            color: #374151;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.2s;
            min-width: 44px;
            text-align: center;
        }
        .pagination a:hover {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
            transform: translateY(-1px);
        }
        .pagination .current {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
        }
        .form-select, .form-control {
            border-radius: 8px;
            padding: 12px;
            font-size: 14px;
            border: 2px solid #e5e7eb;
            transition: border-color 0.2s ease;
        }
        .form-select:focus, .form-control:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 0.2rem rgba(59, 130, 246, 0.25);
        }
        .btn {
            border-radius: 8px;
            padding: 12px 20px;
            font-weight: 500;
            transition: all 0.2s ease;
        }
        .btn:hover {
            transform: translateY(-1px);
        }
        .btn-primary {
            background: linear-gradient(135deg, #3b82f6, #1d4ed8);
            border: none;
        }
        .btn-secondary {
            background: linear-gradient(135deg, #6b7280, #4b5563);
            border: none;
        }
        .btn-outline-primary {
            border: 2px solid #3b82f6;
            color: #3b82f6;
            background: transparent;
        }
        .btn-outline-primary:hover {
            background: #3b82f6;
            color: white;
        }
        .alert {
            border-radius: 12px;
            border: none;
            padding: 16px 20px;
            margin-bottom: 24px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .alert-info {
            background: linear-gradient(135deg, #dbeafe, #bfdbfe);
            color: #1e40af;
        }
        @media (max-width: 768px) {
            .main_content {
                margin-left: 0;
                padding: 10px;
            }
            .container {
                padding: 10px;
            }
            .stats-grid {
                grid-template-columns: 1fr;
            }
            .material-details {
                grid-template-columns: 1fr;
            }
            .table-container {
                overflow-x: auto;
            }
            .filters-card .row {
                flex-direction: column;
            }
        }
        .custom-total-table-container {
            background-color: white;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 24px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            border-left: 4px solid #10b981;
        }
        .custom-total-title {
            font-size: 18px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .custom-total-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        .custom-total-table th,
        .custom-total-table td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
            font-size: 14px;
        }
        .custom-total-table th {
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            font-weight: 600;
            color: #1f2937;
        }
        .custom-total-table .custom-total-row {
            background-color: #f9fafb;
            font-weight: 600;
        }
        .custom-total-table .custom-total-row td {
            border-top: 2px solid #10b981;
            color: #1f2937;
        }
        .period-info-custom {
            background: linear-gradient(135deg, #d1fae5, #a7f3d0);
            color: #065f46;
            padding: 10px 16px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true" />
    <section class="main-layout">
        <%@include file="navbar.jsp" %>
        <div class="container">
            <!-- Breadcrumb -->
            <div class="breadcrumb-container">
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li style="margin-right: 10px"><a href="dashboard"><i class="fas fa-home"></i> Dashboard</a></li>
                        <text style="margin-right: 10px">/</text>
                        <li style="margin-right: 10px"><a href="inventory"><i class="fas fa-boxes"></i> Current Inventory</a></li>
                        <text style="margin-right: 10px">/</text>
                        <li class="breadcrumb-item active" aria-current="page"><i class="fas fa-history"></i> History</li>
                    </ol>
                </nav>
            </div>
            <h1 class="title">Inventory History</h1>
            <p class="subtitle">Track inventory changes and movements over time</p>
            <!-- Error Message -->
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <c:out value="${errorMsg}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <!-- Check for missing materialId or subUnitId -->
            <c:if test="${empty materialId or empty subUnitId}">
                <div class="alert alert-warning alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    Please select a material and subunit to view inventory history.
                    <a href="/WarehouseManagement/inventory" class="btn btn-primary btn-sm ms-2">Select Material</a>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <!-- Material Information Card -->
            <c:if test="${not empty materialId and not empty subUnitId}">
                <c:choose>
                    <c:when test="${not empty materialInfo}">
                        <div class="material-info-card">
                            <div class="material-name"><c:out value="${materialInfo.materialName}"/></div>
                            <div class="material-details">
                                <div class="detail-item">
                                    <span class="detail-label">Category</span>
                                    <span class="detail-value"><c:out value="${materialInfo.categoryName}"/></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Supplier</span>
                                    <span class="detail-value"><c:out value="${materialInfo.supplierName}"/></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Unit</span>
                                    <span class="detail-value"><c:out value="${materialInfo.subUnitName}"/></span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Current Available</span>
                                    <span class="detail-value">
                                        <fmt:formatNumber value="${materialInfo.availableQty}" pattern="#,##0.00"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger">Material not found.</div>
                    </c:otherwise>
                </c:choose>
                <!-- Statistics Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon import"><i class="fas fa-arrow-up"></i></div>
                        <div class="stat-value">
                            <fmt:formatNumber value="${dailyImportQty}" pattern="#,##0.00"/>
                        </div>
                        <div class="stat-label">Total Import</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon export"><i class="fas fa-arrow-down"></i></div>
                        <div class="stat-value">
                            <fmt:formatNumber value="${dailyExportQty}" pattern="#,##0.00"/>
                        </div>
                        <div class="stat-label">Total Export</div>
                    </div>
                </div>
                <!-- Filters -->
                <div class="filters-card">
                    <div class="filters-title">
                        <i class="fas fa-filter"></i> Filter Options
                    </div>
                    <form id="filterForm" method="get" action="/WarehouseManagement/inventoryhistory">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label">Date Range</label>
                                <select class="form-select" id="dateRange" name="dateRange" onchange="handleDateRangeChange()">
                                    <option value="7" ${dateRange == '7' ? 'selected' : ''}>Last 7 Days</option>
                                    <option value="30" ${dateRange == '30' ? 'selected' : ''}>Last 30 Days</option>
                                    <option value="180" ${dateRange == '180' ? 'selected' : ''}>Last 6 Months</option>
                                    <option value="365" ${dateRange == '365' ? 'selected' : ''}>Last Year</option>
                                    <option value="custom" ${dateRange == 'custom' ? 'selected' : ''}>Custom Range</option>
                                    <option value="all" ${dateRange == 'all' ? 'selected' : ''}>All Time</option>
                                </select>
                            </div>
                            <div class="col-md-3" id="startDateContainer" style="${dateRange == 'custom' ? '' : 'display: none;'}">
                                <label class="form-label">Start Date</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" value="${startDate}"/>
                            </div>
                            <div class="col-md-3" id="endDateContainer" style="${dateRange == 'custom' ? '' : 'display: none;'}">
                                <label class="form-label">End Date</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" value="${endDate}"/>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Transaction Type</label>
                                <select class="form-select" id="transactionType" name="transactionType">
                                    <option value="all" ${transactionType == 'all' ? 'selected' : ''}>All Transactions</option>
                                    <option value="import" ${transactionType == 'import' ? 'selected' : ''}>Import Only</option>
                                    <option value="export" ${transactionType == 'export' ? 'selected' : ''}>Export Only</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Sort By</label>
                                <select class="form-select" id="sortBy" name="sortBy">
                                    <option value="date_desc" ${sortBy == 'date_desc' ? 'selected' : ''}>Date (Newest First)</option>
                                    <option value="date_asc" ${sortBy == 'date_asc' ? 'selected' : ''}>Date (Oldest First)</option>
                                    <option value="import_desc" ${sortBy == 'import_desc' ? 'selected' : ''}>Import Quantity (High to Low)</option>
                                    <option value="export_desc" ${sortBy == 'export_desc' ? 'selected' : ''}>Export Quantity (High to Low)</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Total Period</label>
                                <select class="form-select" id="totalPeriod" name="totalPeriod" onchange="handleTotalPeriodChange()">
                                    <option value="7" ${totalPeriod == '7' ? 'selected' : ''}>Last 7 Days</option>
                                    <option value="30" ${totalPeriod == '30' ? 'selected' : ''}>Last 30 Days</option>
                                    <option value="180" ${totalPeriod == '180' ? 'selected' : ''}>Last 6 Months</option>
                                    <option value="custom_total" ${totalPeriod == 'custom_total' ? 'selected' : ''}>Custom Range</option>
                                    <option value="all" ${totalPeriod == 'all' ? 'selected' : ''}>All Time</option>
                                </select>
                            </div>
                            <div class="col-md-3" id="totalStartDateContainer" style="${totalPeriod == 'custom_total' ? '' : 'display: none;'}">
                                <label class="form-label">Total Start Date</label>
                                <input type="date" class="form-control" id="totalStartDate" name="totalStartDate" value="${totalStartDate}"/>
                            </div>
                            <div class="col-md-3" id="totalEndDateContainer" style="${totalPeriod == 'custom_total' ? '' : 'display: none;'}">
                                <label class="form-label">Total End Date</label>
                                <input type="date" class="form-control" id="totalEndDate" name="totalEndDate" value="${totalEndDate}"/>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-12 d-flex gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Apply Filters
                                </button>
                                <button type="button" class="btn btn-outline-primary" onclick="resetFilters()">
                                    <i class="fas fa-undo"></i> Reset
                                </button>
                                <button type="button" class="btn btn-secondary" onclick="exportHistory()">
                                    <i class="fas fa-download"></i> Export
                                </button>
                            </div>
                        </div>
                        <input type="hidden" name="materialId" value="${materialId}">
                        <input type="hidden" name="subUnitId" value="${subUnitId}">
                    </form>
                </div>
                <!-- Info Alert -->
                <div class="alert alert-info" role="alert">
                    <i class="fas fa-info-circle me-2"></i>
                    Showing inventory history for the selected material and filters. Use the date range and transaction type filters to refine your search.
                </div>
                <!-- Total Summary Table -->
                <div class="custom-total-table-container">
                    <div class="custom-total-title">
                        <i class="fas fa-calculator"></i> Total Summary
                    </div>
                    <div class="period-info-custom">
                        <i class="fas fa-calendar-alt"></i>
                        <span>
                            <c:choose>
                                <c:when test="${totalPeriod == 'all'}">Summary for All Time</c:when>
                                <c:when test="${totalPeriod == 'custom_total' && not empty totalStartDate && not empty totalEndDate}">
                                    Summary from <strong><c:out value="${totalStartDate}"/></strong> to <strong><c:out value="${totalEndDate}"/></strong>
                                </c:when>
                                <c:when test="${totalPeriod == '7'}">Summary for Last 7 Days</c:when>
                                <c:when test="${totalPeriod == '30'}">Summary for Last 30 Days</c:when>
                                <c:when test="${totalPeriod == '180'}">Summary for Last 6 Months</c:when>
                                <c:otherwise>Summary for Selected Period</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <table class="custom-total-table">
                        <thead>
                            <tr>
                                <th>Period</th>
                                <th>Available</th>
                                <th>Not Available</th>
                                <th>Total Import</th>
                                <th>Total Export</th>
                                <th>Net Change</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="custom-total-row">
                                <td>
                                    <c:choose>
                                        <c:when test="${totalPeriod == '7'}">Last 7 Days</c:when>
                                        <c:when test="${totalPeriod == '30'}">Last 30 Days</c:when>
                                        <c:when test="${totalPeriod == '180'}">Last 6 Months</c:when>
                                        <c:when test="${totalPeriod == 'custom_total'}">Custom Period</c:when>
                                        <c:otherwise>All Time</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="quantity-cell">
                                    <fmt:formatNumber value="${customLatestAvailable != null ? customLatestAvailable : 0}" pattern="#,##0.00"/>
                                </td>
                                <td class="quantity-cell">
                                    <fmt:formatNumber value="${customLatestNotAvailable != null ? customLatestNotAvailable : 0}" pattern="#,##0.00"/>
                                </td>
                                <td class="quantity-cell" style="color: #10b981;">
                                    <fmt:formatNumber value="${customTotalImport != null ? customTotalImport : 0}" pattern="#,##0.00"/>
                                </td>
                                <td class="quantity-cell" style="color: #ef4444;">
                                    <fmt:formatNumber value="${customTotalExport != null ? customTotalExport : 0}" pattern="#,##0.00"/>
                                </td>
                                <td class="quantity-cell" style="color: ${(customTotalImport - customTotalExport) >= 0 ? '#10b981' : '#ef4444'};">
                                    <fmt:formatNumber value="${(customTotalImport != null ? customTotalImport : 0) - (customTotalExport != null ? customTotalExport : 0)}" pattern="#,##0.00"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <!-- History Table -->
                <div class="table-container mb-4">
                    <table class="table" id="historyTable">
                        <thead>
                            <tr>
                                <th style="width: 50px">#</th>
                                <th>Date</th>
                                <th>Available</th>
                                <th>Not Available</th>
                                <th>Import</th>
                                <th>Export</th>
                            </tr>
                        </thead>
                        <tbody id="historyTableBody">
                            <c:choose>
                                <c:when test="${not empty historyList}">
                                    <c:forEach var="item" items="${historyList}" varStatus="status">
                                        <tr>
                                            <td><strong><c:out value="${(currentPage - 1) * 7 + status.index + 1}"/></strong></td>
                                            <td>
                                                <div class="date-badge">
                                                    <fmt:parseDate value="${item.transactionDate}" pattern="yyyy-MM-dd" var="parsedDate"/>
                                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy"/>
                                                </div>
                                            </td>
                                            <td class="quantity-cell">
                                                <fmt:formatNumber value="${item.availableQty}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="quantity-cell">
                                                <fmt:formatNumber value="${item.notAvailableQty}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="quantity-cell">
                                                <fmt:formatNumber value="${item.importQty}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="quantity-cell">
                                                <fmt:formatNumber value="${item.exportQty}" pattern="#,##0.00"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="no-data">
                                            <i class="fas fa-search"></i>
                                            <div>No history records found</div>
                                            <small>Try adjusting your filters or date range</small>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination" id="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="/WarehouseManagement/inventoryhistory?materialId=${materialId}&subUnitId=${subUnitId}&dateRange=${dateRange}&startDate=${startDate}&endDate=${endDate}&transactionType=${transactionType}&sortBy=${sortBy}&page=${currentPage - 1}&totalPeriod=${totalPeriod}&totalStartDate=${totalStartDate}&totalEndDate=${totalEndDate}">« Prev</a>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="/WarehouseManagement/inventoryhistory?materialId=${materialId}&subUnitId=${subUnitId}&dateRange=${dateRange}&startDate=${startDate}&endDate=${endDate}&transactionType=${transactionType}&sortBy=${sortBy}&page=${i}&totalPeriod=${totalPeriod}&totalStartDate=${totalStartDate}&totalEndDate=${totalEndDate}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <a href="/WarehouseManagement/inventoryhistory?materialId=${materialId}&subUnitId=${subUnitId}&dateRange=${dateRange}&startDate=${startDate}&endDate=${endDate}&transactionType=${transactionType}&sortBy=${sortBy}&page=${currentPage + 1}&totalPeriod=${totalPeriod}&totalStartDate=${totalStartDate}&totalEndDate=${totalEndDate}">Next »</a>
                        </c:if>
                    </div>
                </c:if>
            </c:if>
        </div>
    </section>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function handleDateRangeChange() {
            const dateRange = document.getElementById('dateRange').value;
            const startDateContainer = document.getElementById('startDateContainer');
            const endDateContainer = document.getElementById('endDateContainer');
            if (dateRange === 'custom') {
                startDateContainer.style.display = 'block';
                endDateContainer.style.display = 'block';
            } else {
                startDateContainer.style.display = 'none';
                endDateContainer.style.display = 'none';
            }
        }

        function handleTotalPeriodChange() {
            const totalPeriod = document.getElementById('totalPeriod').value;
            const totalStartDateContainer = document.getElementById('totalStartDateContainer');
            const totalEndDateContainer = document.getElementById('totalEndDateContainer');
            if (totalPeriod === 'custom_total') {
                totalStartDateContainer.style.display = 'block';
                totalEndDateContainer.style.display = 'block';
            } else {
                totalStartDateContainer.style.display = 'none';
                totalEndDateContainer.style.display = 'none';
            }
        }

        function resetFilters() {
            document.getElementById('dateRange').value = '7';
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            document.getElementById('transactionType').value = 'all';
            document.getElementById('sortBy').value = 'date_desc';
            document.getElementById('totalPeriod').value = '7';
            document.getElementById('totalStartDate').value = '';
            document.getElementById('totalEndDate').value = '';
            document.getElementById('filterForm').submit();
        }

        function exportHistory() {
            alert('Export functionality not implemented yet.');
        }
    </script>
</body>
</html>