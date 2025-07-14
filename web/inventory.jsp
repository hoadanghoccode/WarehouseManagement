<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Current Inventory</title>
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
        .stats-grid {
            display: flex;
            gap: 16px;
            margin-bottom: 24px;
        }
        .stat-card {
            background-color: white;
            border-radius: 8px;
            padding: 16px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.08);
            flex: 1;
            text-align: center;
        }
        .stat-icon {
            font-size: 24px;
            margin-bottom: 8px;
        }
        .stat-icon.usable {
            color: #22c55e;
        }
        .stat-icon.not-usable {
            color: #ef4444;
        }
        .stat-value {
            font-size: 24px;
            font-weight: 600;
            color: #1f2937;
        }
        .stat-label {
            font-size: 14px;
            color: #6b7280;
            text-transform: uppercase;
        }
        .table-container {
            overflow-x: auto;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
        }
        table.table {
            width: 100%;
            border-collapse: collapse;
            min-width: 900px; /* Adjusted for additional image column */
        }
        table.table th, table.table td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
            font-size: 14px;
            vertical-align: middle;
        }
        table.table th {
            background-color: #f3f4f6;
            font-weight: 600;
            color: #1f2937;
        }
        table.table tbody tr:nth-child(even) {
            background-color: #f9fafb;
        }
        table.table tbody tr:hover {
            background-color: #eef2ff;
        }
        .action-buttons {
            display: flex;
            gap: 8px;
        }
        .action-buttons .btn {
            padding: 6px 12px;
            font-size: 12px;
            border-radius: 10px;
        }
        .no-data {
            text-align: center;
            padding: 16px;
            background-color: #f3f4f6;
            border-radius: 8px;
            font-size: 16px;
            color: #6b7280;
        }
        .modal-content {
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        .modal-header {
            background-color: #f3f4f6;
            border-bottom: 1px solid #e5e7eb;
        }
        .modal-title {
            font-size: 18px;
            font-weight: 600;
            color: #1f2937;
        }
        .modal-body {
            padding: 24px;
        }
        .detail-item {
            margin-bottom: 14px;
            display: flex;
            gap: 8px;
        }
        .detail-item strong {
            color: #333;
            width: 120px;
            font-weight: 600;
        }
        .error {
            color: #dc3545;
            font-size: 14px;
        }
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 32px;
        }
        .pagination a, .pagination span {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            text-decoration: none;
            color: #374151;
            font-size: 14px;
            transition: all 0.2s;
        }
        .pagination a:hover {
            background-color: #f3f4f6;
        }
        .pagination .current {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
        }
        .form-select, .form-control {
            border-radius: 6px;
            padding: 8px;
            font-size: 14px;
            border: 1px solid #d1d5db;
        }
        .form-select:focus, .form-control:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 0.2rem rgba(59, 130, 246, 0.25);
        }
        .input-group .btn-search {
            background-color: #0f3151;
            padding: 8px 16px;
            font-size: 14px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 6px;
            transition: background-color 0.2s ease, transform 0.2s ease;
        }
        .input-group .btn-search:hover {
            background-color: #2563eb;
            transform: translateY(-1px);
        }
        .input-group .btn-search:active {
            transform: translateY(0);
        }
        .material-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 4px;
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
                flex-direction: column;
            }
            .row {
                flex-direction: column;
            }
            .table-container {
                overflow-x: auto;
            }
            .action-buttons {
                flex-direction: column;
                gap: 4px;
            }
            .input-group .btn-search {
                padding: 6px 12px;
                font-size: 12px;
            }
            .material-image {
                width: 40px;
                height: 40px;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true" />
    <section class="main-content">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <h1 class="title">Current Inventory</h1>
            <p class="subtitle">Manage and monitor your current stock levels in real-time</p>

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon usable"><i class="fas fa-check-circle"></i></div>
                    <div class="stat-value" id="usableItems"><fmt:formatNumber value="${summary.availableQty}" pattern="#,##0.00"/></div>
                    <div class="stat-label">Usable Materials</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon not-usable"><i class="fas fa-times-circle"></i></div>
                    <div class="stat-value" id="notUsableItems"><fmt:formatNumber value="${summary.notAvailableQty}" pattern="#,##0.00"/></div>
                    <div class="stat-label">Not Usable Materials</div>
                </div>
            </div>

            <!-- Filters -->
            <form action="${pageContext.request.contextPath}/inventory" method="get" id="filterForm">
                <div class="row g-3 mb-3">
                    <div class="col-md-3">
                        <select class="form-select" id="categoryFilter" name="categoryId">
                            <option value="0" ${categoryId == 0 ? 'selected' : ''}>All Categories</option>
                            <c:forEach var="category" items="${categoryList}">
                                <option value="${category.categoryId}" ${category.categoryId == categoryId ? 'selected' : ''}><c:out value="${category.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" id="qualityFilter" name="qualityId">
                            <option value="0" ${qualityId == 0 ? 'selected' : ''}>All Qualities</option>
                            <c:forEach var="quality" items="${qualityList}">
                                <option value="${quality.qualityId}" ${quality.qualityId == qualityId ? 'selected' : ''}><c:out value="${quality.qualityName}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" id="sortByFilter" name="sortBy">
                            <option value="material_id ASC" ${sortBy == 'material_id ASC' ? 'selected' : ''}>Material ID (Asc)</option>
                            <option value="material_id DESC" ${sortBy == 'material_id DESC' ? 'selected' : ''}>Material ID (Desc)</option>
                            <option value="material_name ASC" ${sortBy == 'material_name ASC' ? 'selected' : ''}>Material Name (Asc)</option>
                            <option value="material_name DESC" ${sortBy == 'material_name DESC' ? 'selected' : ''}>Material Name (Desc)</option>
                            <option value="available_qty ASC" ${sortBy == 'available_qty ASC' ? 'selected' : ''}>Available Qty (Asc)</option>
                            <option value="available_qty DESC" ${sortBy == 'available_qty DESC' ? 'selected' : ''}>Available Qty (Desc)</option>
                            <option value="not_available_qty ASC" ${sortBy == 'not_available_qty ASC' ? 'selected' : ''}>Not Available Qty (Asc)</option>
                            <option value="not_available_qty DESC" ${sortBy == 'not_available_qty DESC' ? 'selected' : ''}>Not Available Qty (Desc)</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <div class="input-group">
                            <input type="text" id="searchInput" name="searchTerm" class="form-control" placeholder="Search..." value="${searchTerm}">
                            <button style="margin-left: 5px;" class="btn btn-primary btn-md rounded-pill btn-search" type="submit">
                                <i class="fas fa-search"></i> Search
                            </button>
                        </div>
                    </div>
                </div>
            </form>

            <!-- Error Message -->
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <c:out value="${errorMsg}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Table -->
            <div class="table-container mb-4">
                <table class="table" id="inventoryTable">
                    <thead>
                        <tr>
                            <th style="width: 40px">#</th>
                            <th style="width: 80px">Image</th>
                            <th>Material</th>
                            <th>Category</th>
                            <th>Unit</th>
                            <th>Available</th>
                            <th>Not Available</th>
                            <th style="width: 100px">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty inventoryList}">
                                <c:forEach var="item" items="${inventoryList}" varStatus="status">
                                    <tr data-material-id="${item.materialId}" data-unit-id="${item.unitId}">
                                        <td class="row-number"><strong>${status.index + 1}</strong></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.image}">
                                                    <img src="${item.image}" alt="${item.materialName}" class="material-image" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="img/default-material.png" alt="No Image" class="material-image" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><c:out value="${item.materialName}"/></td>
                                        <td><c:out value="${item.categoryName}"/></td>
                                        <td><c:out value="${item.unitName}"/></td>
                                        <td><fmt:formatNumber value="${item.availableQty}" pattern="#,##0.00"/></td>
                                        <td><fmt:formatNumber value="${item.notAvailableQty}" pattern="#,##0.00"/></td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn btn-info btn-sm" data-bs-toggle="modal" data-bs-target="#detailModal"
                                                        onclick="viewDetails(${item.materialId}, ${item.unitId})">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <a href="inventoryhistory?materialId=${item.materialId}&unitId=${item.unitId}" class="btn btn-warning btn-sm">
                                                    <i class="fas fa-history"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" class="no-data">No inventory items found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <div class="pagination" id="pagination"></div>

            <!-- Detail Modal -->
            <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="detailModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="detailModalLabel">
                                <i class="fas fa-info-circle me-2"></i>
                                Inventory Details
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="detail-item">
                                <strong>Image:</strong>
                                <span>
                                    <c:choose>
                                        <c:when test="${not empty item.image}">
                                            <img src="${item.image}" alt="Material Image" class="material-image" />
                                        </c:when>
                                        <c:otherwise>
                                            <img src="img/default-material.png" alt="No Image" class="material-image" />
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="detail-item">
                                <strong>Material:</strong>
                                <span id="modal-material-name"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Category:</strong>
                                <span id="modal-category-name"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Unit:</strong>
                                <span id="modal-unit-name"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Available Qty:</strong>
                                <span id="modal-available-qty"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Not Available Qty:</strong>
                                <span id="modal-not-available-qty"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Import Qty:</strong>
                                <span id="modal-import-qty"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Export Qty:</strong>
                                <span id="modal-export-qty"></span>
                            </div>
                            <div class="detail-item">
                                <strong>Last Updated:</strong>
                                <span id="modal-inventory-date"></span>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                <i class="fas fa-times me-2"></i>Close
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let pageSize = 5;
        let currentPage = 1;
        let allRows = [];

        window.addEventListener("DOMContentLoaded", function () {
            allRows = Array.from(document.querySelectorAll("#inventoryTable tbody tr:not(.no-data)"));
            updatePagination();
            updateTable();
            console.log("Inventory table loaded with " + allRows.length + " rows");
        });

        function updateTable() {
            if (allRows.length === 0) {
                console.log("No rows to display");
                return;
            }

            allRows.forEach((row) => {
                row.style.display = "none";
            });

            const start = (currentPage - 1) * pageSize;
            const end = Math.min(start + pageSize, allRows.length);
            const rowsToShow = allRows.slice(start, end);

            rowsToShow.forEach((row, index) => {
                row.style.display = "";
                const rowNumberCell = row.querySelector(".row-number strong");
                rowNumberCell.textContent = start + index + 1;
            });

            console.log(`Displaying rows ${start + 1} to ${end} of ${allRows.length}`);
        }

        function updatePagination() {
            const totalPages = Math.ceil(allRows.length / pageSize);
            const pagination = document.getElementById("pagination");
            pagination.innerHTML = "";

            if (totalPages <= 1) {
                console.log("Only one page, hiding pagination");
                return;
            }

            console.log(`Rendering pagination: currentPage=${currentPage}, totalPages=${totalPages}`);

            if (currentPage > 1) {
                const first = document.createElement("a");
                first.href = "#";
                first.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                first.addEventListener("click", (event) => {
                    event.preventDefault();
                    currentPage = 1;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(first);

                const prev = document.createElement("a");
                prev.href = "#";
                prev.innerHTML = '<i class="fas fa-angle-left"></i>';
                prev.addEventListener("click", (event) => {
                    event.preventDefault();
                    currentPage--;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(prev);
            } else {
                const disabledFirst = document.createElement("span");
                disabledFirst.className = "text-muted me-2";
                disabledFirst.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                pagination.appendChild(disabledFirst);

                const disabledPrev = document.createElement("span");
                disabledPrev.className = "text-muted me-2";
                disabledPrev.innerHTML = '<i class="fas fa-angle-left"></i>';
                pagination.appendChild(disabledPrev);
            }

            if (totalPages <= 7) {
                for (let i = 1; i <= totalPages; i++) {
                    if (i === currentPage) {
                        const current = document.createElement("span");
                        current.className = "px-2 mx-1 bg-primary text-white rounded current";
                        current.textContent = i;
                        pagination.appendChild(current);
                    } else {
                        const pageLink = document.createElement("a");
                        pageLink.href = "#";
                        pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                        pageLink.textContent = i;
                        pageLink.addEventListener("click", (event) => {
                            event.preventDefault();
                            currentPage = i;
                            updatePagination();
                            updateTable();
                        });
                        pagination.appendChild(pageLink);
                    }
                }
            } else {
                if (currentPage <= 4) {
                    for (let i = 1; i <= 5; i++) {
                        if (i === currentPage) {
                            const current = document.createElement("span");
                            current.className = "px-2 mx-1 bg-primary text-white rounded current";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (event) => {
                                event.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }
                    const dots = document.createElement("span");
                    dots.className = "px-2 mx-1 text-muted";
                    dots.textContent = "...";
                    pagination.appendChild(dots);

                    const last = document.createElement("a");
                    last.href = "#";
                    last.className = "px-2 mx-1 text-decoration-none text-dark";
                    last.textContent = totalPages;
                    last.addEventListener("click", (event) => {
                        event.preventDefault();
                        currentPage = totalPages;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(last);
                } else if (currentPage >= totalPages - 3) {
                    const first = document.createElement("a");
                    first.href = "#";
                    first.className = "px-2 mx-1 text-decoration-none text-dark";
                    first.textContent = "1";
                    first.addEventListener("click", (event) => {
                        event.preventDefault();
                        currentPage = 1;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(first);

                    const dots = document.createElement("span");
                    dots.className = "px-2 mx-1 text-muted";
                    dots.textContent = "...";
                    pagination.appendChild(dots);

                    for (let i = totalPages - 4; i <= totalPages; i++) {
                        if (i === currentPage) {
                            const current = document.createElement("span");
                            current.className = "px-2 mx-1 bg-primary text-white rounded current";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (event) => {
                                event.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }
                } else {
                    const first = document.createElement("a");
                    first.href = "#";
                    first.className = "px-2 mx-1 text-decoration-none text-dark";
                    first.textContent = "1";
                    first.addEventListener("click", (event) => {
                        event.preventDefault();
                        currentPage = 1;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(first);

                    const dots1 = document.createElement("span");
                    dots1.className = "px-2 mx-1 text-muted";
                    dots1.textContent = "...";
                    pagination.appendChild(dots1);

                    for (let i = currentPage - 1; i <= currentPage + 1; i++) {
                        if (i === currentPage) {
                            const current = document.createElement("span");
                            current.className = "px-2 mx-1 bg-primary text-white rounded current";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (event) => {
                                event.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }

                    const dots2 = document.createElement("span");
                    dots2.className = "px-2 mx-1 text-muted";
                    dots2.textContent = "...";
                    pagination.appendChild(dots2);

                    const last = document.createElement("a");
                    last.href = "#";
                    last.className = "px-2 mx-1 text-decoration-none text-dark";
                    last.textContent = totalPages;
                    last.addEventListener("click", (event) => {
                        event.preventDefault();
                        currentPage = totalPages;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(last);
                }
            }

            if (currentPage < totalPages) {
                const next = document.createElement("a");
                next.href = "#";
                next.innerHTML = '<i class="fas fa-angle-right"></i>';
                next.className = "ms-2 text-decoration-none text-dark";
                next.addEventListener("click", (event) => {
                    event.preventDefault();
                    currentPage++;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(next);

                const lastBtn = document.createElement("a");
                lastBtn.href = "#";
                lastBtn.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                lastBtn.className = "ms-2 text-decoration-none text-dark";
                lastBtn.addEventListener("click", (event) => {
                    event.preventDefault();
                    currentPage = totalPages;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(lastBtn);
            } else {
                const disabledNext = document.createElement("span");
                disabledNext.className = "ms-2 text-muted";
                disabledNext.innerHTML = '<i class="fas fa-angle-right"></i>';
                pagination.appendChild(disabledNext);

                const disabledLast = document.createElement("span");
                disabledLast.className = "ms-2 text-muted";
                disabledLast.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                pagination.appendChild(disabledLast);
            }
        }

        function viewDetails(materialId, unitId) {
            console.log(`viewDetails - Fetching data for materialId=${materialId}, unitId=${unitId}`);
            $.ajax({
                url: '${pageContext.request.contextPath}/inventory',
                method: 'GET',
                data: {
                    action: 'getLatestDetails',
                    materialId: materialId,
                    unitId: unitId
                },
                success: function (response) {
                    console.log("viewDetails - AJAX success, response:", response);
                    const modalBody = document.querySelector(".modal-body");
                    const existingAlert = modalBody.querySelector(".alert");
                    if (existingAlert) existingAlert.remove();

                    if (response && (response.materialId || response.materialName)) {
                        const imageElement = modalBody.querySelector(".detail-item img");
                        if (response.image) {
                            imageElement.src = response.image;
                            imageElement.alt = response.materialName || 'Material Image';
                        } else {
                            imageElement.src = 'img/default-material.png';
                            imageElement.alt = 'No Image';
                        }
                        document.getElementById("modal-material-name").textContent = response.materialName || 'N/A';
                        document.getElementById("modal-category-name").textContent = response.categoryName || 'N/A';
                        document.getElementById("modal-unit-name").textContent = response.unitName || 'N/A';
                        document.getElementById("modal-available-qty").textContent = response.availableQty ? parseFloat(response.availableQty).toFixed(2) : '0.00';
                        document.getElementById("modal-not-available-qty").textContent = response.notAvailableQty ? parseFloat(response.notAvailableQty).toFixed(2) : '0.00';
                        document.getElementById("modal-import-qty").textContent = response.importQty ? parseFloat(response.importQty).toFixed(2) : '0.00';
                        document.getElementById("modal-export-qty").textContent = response.exportQty ? parseFloat(response.exportQty).toFixed(2) : '0.00';
                        document.getElementById("modal-inventory-date").textContent = response.inventoryDate ?
                            new Date(response.inventoryDate).toLocaleDateString('en-GB', {
                                day: '2-digit',
                                month: '2-digit',
                                year: 'numeric'
                            }) : 'N/A';
                    } else {
                        console.warn("viewDetails - No valid data received");
                        modalBody.insertAdjacentHTML('afterbegin',
                            '<div class="alert alert-warning">No recent inventory data found.</div>');
                    }
                },
                error: function (xhr, status, error) {
                    console.error("viewDetails - AJAX error:", status, error, xhr.responseText);
                    const modalBody = document.querySelector(".modal-body");
                    const existingAlert = modalBody.querySelector(".alert");
                    if (existingAlert) existingAlert.remove();
                    modalBody.insertAdjacentHTML('afterbegin',
                        '<div class="alert alert-danger">Error fetching inventory details: ' + error + '</div>');
                    const imageElement = modalBody.querySelector(".detail-item img");
                    imageElement.src = 'img/default-material.png';
                    imageElement.alt = 'No Image';
                    document.getElementById("modal-material-name").textContent = 'N/A';
                    document.getElementById("modal-category-name").textContent = 'N/A';
                    document.getElementById("modal-unit-name").textContent = 'N/A';
                    document.getElementById("modal-available-qty").textContent = '0.00';
                    document.getElementById("modal-not-available-qty").textContent = '0.00';
                    document.getElementById("modal-import-qty").textContent = '0.00';
                    document.getElementById("modal-export-qty").textContent = '0.00';
                    document.getElementById("modal-inventory-date").textContent = 'N/A';
                }
            });
        }
    </script>
</body>
</html>