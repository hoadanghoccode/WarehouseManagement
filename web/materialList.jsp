<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Materials List</title>

    <!-- Font Awesome cho icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <!-- CSS chính (nếu cần thêm) -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
        * {
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background-color: #f3f4f6;
            margin: 0; 
            padding: 0;
            color: #374151;
        }
        .container {
            padding: 24px 16px;
            max-width: 1200px;
            margin: 0 auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            margin-bottom: 24px;
        }
        .title {
            font-size: 28px;
            font-weight: 600;
            color: #1f2937;
            margin: 0 0 12px 0;
        }
        .header-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
        }

        .search-container {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
        }
        .search-input {
            width: 220px;
            padding: 8px 32px 8px 32px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            color: #374151;
            background-image: url('data:image/svg+xml;base64,PHN2ZyBmaWxsPSIjOWNhM2FmIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiI+PHBhdGggZD0iTTYuNSAxMGM0LjA4IDAgNy41LTMuNDIgNy41LTcuNUMxNCAxLjg2IDEwLjA4IDAgNi41IDAgMi42MiAwIDAgMi42MiAwIDYuNUMwIDEwLjA4IDMuNDIgMTMuNSA3LjUgMTMuNXM3LjUtMy40MiA3LjUtNy41YzAtMy42My0zLjEyLTYuNS02LjUtNi41LTQuMDggMC03LjUgMy40Mi03LjUgNy41IDAgMy42MyAzLjEyIDYuNSA2LjUgNi41em0xNC43MSAxMC42MS0yLjgxIDIuODEtNS4wMDUtNS4wMDVhOC41MjIgOC41MjIgMCAwIDAgMS4wNDYtMS4wNDVsNS4wMDUgNS4wMDV6Ii8+PC9zdmc+');
            background-repeat: no-repeat;
            background-position: 10px center;
            background-size: 16px 16px;
        }
        .search-input::placeholder {
            color: #9ca3af;
        }
        .form-select {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
            color: #374151;
            cursor: pointer;
            min-width: 140px;
        }
        .form-select option {
            color: #1f2937;
        }
        .form-control {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            color: #374151;
            min-width: 80px;
        }

        .btn {
            padding: 8px 16px;
            border-radius: 8px;
            text-decoration: none;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
            font-size: 14px;
            border: none;
            transition: background-color 0.2s;
        }
        .btn-success {
            background-color: #10b981;
        }
        .btn-success:hover {
            background-color: #059669;
        }
        .btn-outline {
            background-color: transparent;
            border: 1px solid #9ca3af;
            color: #374151;
        }
        .btn-outline:hover {
            background-color: #f3f4f6;
        }
        .btn-info {
            background-color: #3b82f6;
        }
        .btn-info:hover {
            background-color: #2563eb;
        }
        .btn-primary {
            background-color: #6366f1;
        }
        .btn-primary:hover {
            background-color: #4f46e5;
        }
        .btn-danger {
            background-color: #ef4444;
        }
        .btn-danger:hover {
            background-color: #dc2626;
        }

        .stats-info {
            margin-bottom: 16px;
            color: #374151;
            display: flex;
            align-items: center;
            gap: 8px;
            background-color: #dbeafe;
            padding: 12px 16px;
            border-radius: 8px;
            font-size: 14px;
        }
        .stats-info i {
            color: #3b82f6;
        }
        .stats-info strong {
            color: #1f2937;
        }

        .table-container {
            overflow-x: auto;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.08);
        }
        table.table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
        }
        table.table th,
        table.table td {
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
            position: sticky;
            top: 0;
            z-index: 2;
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
        .no-data {
            text-align: center;
            padding: 24px;
            background-color: #f3f4f6;
            border-radius: 12px;
            font-size: 16px;
            color: #9ca3af;
        }

        .pagination {
            display: flex;
            justify-content: center;
            gap: 8px;
            margin: 24px 0;
        }
        .pagination a,
        .pagination span {
            padding: 8px 14px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            color: #374151;
            text-decoration: none;
            font-size: 14px;
            transition: background-color 0.2s, color 0.2s;
        }
        .pagination a:hover {
            background-color: #e5e7eb;
        }
        .pagination .current {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
        }

        .modal {
            display: none; 
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
        .modal-content {
            background-color: white;
            margin: 6% auto;
            padding: 24px 32px;
            border-radius: 12px;
            width: 90%;
            max-width: 480px;
            position: relative;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            font-size: 14px;
        }
        .modal .close {
            position: absolute;
            top: 12px;
            right: 18px;
            font-size: 24px;
            cursor: pointer;
            color: #9ca3af;
            transition: color 0.2s;
        }
        .modal .close:hover {
            color: #374151;
        }
        .detail-item {
            margin-bottom: 14px;
            display: flex;
            gap: 8px;
        }
        .detail-item strong {
            color: #1f2937;
            width: 110px;
        }

        @media (max-width: 768px) {
            .search-input {
                width: 160px;
            }
            .form-select, .form-control {
                min-width: 100px;
                font-size: 13px;
            }
            table.table {
                min-width: 600px;
            }
            .title {
                font-size: 24px;
            }
            .modal-content {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1 class="title">Materials List</h1>
            <div class="header-actions">
                <div class="search-container">
                    <!-- SEARCH TEXT -->
                    <input
                        type="text"
                        id="searchInput"
                        placeholder="Search materials..."
                        class="search-input"
                        oninput="filterMaterials()"
                    />

                    <!-- CATEGORY FILTER -->
                    <select class="form-select" id="categoryFilter" onchange="filterMaterials()">
                        <option value="">All Categories</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.name}">${category.name}</option>
                        </c:forEach>
                    </select>

                    <!-- UNIT FILTER -->
                    <select class="form-select" id="unitFilter" onchange="filterMaterials()">
                        <option value="">All Units</option>
                        <c:forEach var="unit" items="${units}">
                            <option value="${unit.name}">${unit.name}</option>
                        </c:forEach>
                    </select>

                    <!-- SUPPLIER FILTER -->
                    <select class="form-select" id="supplierFilter" onchange="filterMaterials()">
                        <option value="">All Suppliers</option>
                        <c:forEach var="supplier" items="${suppliers}">
                            <option value="${supplier.name}">${supplier.name}</option>
                        </c:forEach>
                    </select>

                    <!-- STATUS FILTER -->
                    <select class="form-select" id="activeFilter" onchange="filterMaterials()">
                        <option value="">All Status</option>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>

                    <!-- QUANTITY MIN/MAX -->
                    <input
                        type="number"
                        step="0.01"
                        class="form-control"
                        id="quantityFilterMin"
                        placeholder="Min"
                        oninput="filterMaterials()"
                    />
                    <input
                        type="number"
                        step="0.01"
                        class="form-control"
                        id="quantityFilterMax"
                        placeholder="Max"
                        oninput="filterMaterials()"
                    />
                </div>

                <!-- NÚT ADD MATERIAL -->
                <a href="add-material" class="btn btn-success">
                    <i class="fas fa-plus"></i> Add Material
                </a>
            </div>
        </div>

        <div class="table-container">
            <table class="table" id="materialsTable">
                <thead>
                    <tr>
                        <th style="width: 40px;">#</th>
                        <th>Name</th>
                        <th>Unit</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Supplier</th>
                        <th>Category</th>
                        <th>Active</th>
                        <th style="width: 140px;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- JSP render toàn bộ dữ liệu -->
                    <c:forEach var="material" items="${materials}" varStatus="status">
                        <tr data-index="${status.index + 1}">
                            <td class="row-number"><strong>${status.index + 1}</strong></td>
                            <td>${material.name}</td>
                            <td>${material.unitName != null ? material.unitName : '-'}</td>
                            <td><fmt:formatNumber value="${material.price}" type="number" minFractionDigits="2" /></td>
                            <td><fmt:formatNumber value="${material.quantity}" type="number" minFractionDigits="2" /></td>
                            <td>${material.supplierName != null ? material.supplierName : '-'}</td>
                            <td>${material.categoryName}</td>
                            <td>
                                <span style="
                                    display:inline-block;
                                    padding:4px 8px;
                                    border-radius:4px;
                                    font-size:13px;
                                    font-weight:500;
                                    color: ${material.status == 'active' ? '#065f46' : '#991b1b'};
                                    background-color: ${material.status == 'active' ? '#d1fae5' : '#fee2e2'};
                                ">
                                    ${material.status == 'active' ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <!-- VIEW BUTTON -->
                                    <button class="btn btn-info view-detail"
                                            data-id="${material.materialId}"
                                            data-unit-id="${material.unitId}"
                                            title="View">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <!-- EDIT BUTTON -->
                                    <a href="update-material?id=${material.materialId}&unitId=${material.unitId}"
                                       class="btn btn-primary"
                                       title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <!-- DELETE BUTTON -->
                                    <a href="delete-material?id=${material.materialId}"
                                       class="btn btn-danger"
                                       title="Delete"
                                       onclick="return confirm('Are you sure you want to deactivate this material?')">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="pagination" id="pagination"></div>
    </div>

    <div id="materialModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">×</span>
            <h2 style="font-size: 20px; margin-bottom: 16px; color: #1f2937;">Material Details</h2>
            <div class="detail-item"><strong>ID:</strong> <span id="modalMaterialId"></span></div>
            <div class="detail-item"><strong>Name:</strong> <span id="modalName"></span></div>
            <div class="detail-item"><strong>Unit:</strong> <span id="modalUnitName"></span></div>
            <div class="detail-item"><strong>Price:</strong> <span id="modalPrice"></span></div>
            <div class="detail-item"><strong>Quantity:</strong> <span id="modalQuantity"></span></div>
            <div class="detail-item"><strong>Supplier:</strong> <span id="modalSupplierName"></span></div>
            <div class="detail-item"><strong>Status:</strong> <span id="modalStatus"></span></div>
            <div class="detail-item"><strong>Category:</strong> <span id="modalCategoryName"></span></div>
            <button class="btn btn-primary" onclick="closeModal()" style="margin-top: 16px;">Close</button>
        </div>
    </div>

    <script>
        const pageSize = 5; 
        let currentPage = 1;
        let allRows = [];
        let filteredRows = [];

        window.addEventListener('DOMContentLoaded', function() {
            allRows = Array.from(document.querySelectorAll('#materialsTable tbody tr'));
            filteredRows = [...allRows];
            updatePagination();
            updateTable();
        });

        function filterMaterials() {
            let searchText      = document.getElementById('searchInput').value.trim().toLowerCase();
            let categoryValue   = document.getElementById('categoryFilter').value.trim().toLowerCase();
            let unitValue       = document.getElementById('unitFilter').value.trim().toLowerCase();
            let supplierValue   = document.getElementById('supplierFilter').value.trim().toLowerCase();
            let statusValue     = document.getElementById('activeFilter').value.trim().toLowerCase();
            let qtyMinRaw       = document.getElementById('quantityFilterMin').value;
            let qtyMaxRaw       = document.getElementById('quantityFilterMax').value;
            let qtyMin          = qtyMinRaw === '' ? -Infinity : parseFloat(qtyMinRaw);
            let qtyMax          = qtyMaxRaw === '' ? Infinity  : parseFloat(qtyMaxRaw);

            filteredRows = allRows.filter(row => {
                let nameText       = row.cells[1].textContent.trim().toLowerCase();
                let unitText       = row.cells[2].textContent.trim().toLowerCase();
                let qtyText        = row.cells[4].textContent.trim().replace(/,/g, '');
                let supplierText   = row.cells[5].textContent.trim().toLowerCase();
                let categoryText   = row.cells[6].textContent.trim().toLowerCase();
                let statusText     = row.cells[7].textContent.trim().toLowerCase();

                let qtyValue       = parseFloat(qtyText);
                if (isNaN(qtyValue)) {
                    qtyValue = 0;
                }

                let isMatchName     = !searchText || nameText.includes(searchText);
                let isMatchCat      = !categoryValue || categoryText === categoryValue;
                let isMatchUnit     = !unitValue     || unitText === unitValue;
                let isMatchSupplier = !supplierValue || supplierText === supplierValue;
                let isMatchStatus   = !statusValue   || statusText === statusValue;
                let isMatchQty      = (qtyValue >= qtyMin) && (qtyValue <= qtyMax);

                return isMatchName && isMatchCat && isMatchUnit && isMatchSupplier && isMatchStatus && isMatchQty;
            });

            currentPage = 1;
            updatePagination();
            updateTable();
            updateShowingCount();
        }

        function updateTable() {
            allRows.forEach(row => {
                row.style.display = 'none';
            });

            const start = (currentPage - 1) * pageSize;
            const end = Math.min(start + pageSize, filteredRows.length);
            const rowsToShow = filteredRows.slice(start, end);

            rowsToShow.forEach((row, index) => {
                row.style.display = '';
                const rowNumberCell = row.querySelector('.row-number strong');
                rowNumberCell.textContent = start + index + 1;
            });
        }

        function updatePagination() {
            const totalPages = Math.ceil(filteredRows.length / pageSize);
            const pagination = document.getElementById('pagination');
            pagination.innerHTML = '';

            if (totalPages <= 1) return;

            if (currentPage > 1) {
                const first = document.createElement('a');
                first.href = '#';
                first.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                first.addEventListener('click', (e) => {
                    e.preventDefault();
                    currentPage = 1;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(first);
            } else {
                const disabled = document.createElement('span');
                disabled.style.opacity = '0.3';
                disabled.style.cursor = 'not-allowed';
                disabled.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                pagination.appendChild(disabled);
            }

            if (currentPage > 1) {
                const prev = document.createElement('a');
                prev.href = '#';
                prev.innerHTML = '<i class="fas fa-angle-left"></i>';
                prev.addEventListener('click', (e) => {
                    e.preventDefault();
                    currentPage--;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(prev);
            } else {
                const disabled = document.createElement('span');
                disabled.style.opacity = '0.3';
                disabled.style.cursor = 'not-allowed';
                disabled.innerHTML = '<i class="fas fa-angle-left"></i>';
                pagination.appendChild(disabled);
            }

            if (totalPages <= 7) {
                for (let i = 1; i <= totalPages; i++) {
                    if (i === currentPage) {
                        const current = document.createElement('span');
                        current.className = 'current';
                        current.textContent = i;
                        pagination.appendChild(current);
                    } else {
                        const pageLink = document.createElement('a');
                        pageLink.href = '#';
                        pageLink.textContent = i;
                        pageLink.addEventListener('click', (e) => {
                            e.preventDefault();
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
                            const current = document.createElement('span');
                            current.className = 'current';
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement('a');
                            pageLink.href = '#';
                            pageLink.textContent = i;
                            pageLink.addEventListener('click', (e) => {
                                e.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }
                    if (totalPages > 6) {
                        const dots = document.createElement('span');
                        dots.style.padding = '8px 4px';
                        dots.textContent = '...';
                        pagination.appendChild(dots);

                        const last = document.createElement('a');
                        last.href = '#';
                        last.textContent = totalPages;
                        last.addEventListener('click', (e) => {
                            e.preventDefault();
                            currentPage = totalPages;
                            updatePagination();
                            updateTable();
                        });
                        pagination.appendChild(last);
                    }
                } else if (currentPage >= totalPages - 3) {
                    const first = document.createElement('a');
                    first.href = '#';
                    first.textContent = '1';
                    first.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = 1;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(first);

                    if (totalPages > 6) {
                        const dots = document.createElement('span');
                        dots.style.padding = '8px 4px';
                        dots.textContent = '...';
                        pagination.appendChild(dots);
                    }

                    for (let i = totalPages - 4; i <= totalPages; i++) {
                        if (i === currentPage) {
                            const current = document.createElement('span');
                            current.className = 'current';
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement('a');
                            pageLink.href = '#';
                            pageLink.textContent = i;
                            pageLink.addEventListener('click', (e) => {
                                e.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }
                } else {
                    const first = document.createElement('a');
                    first.href = '#';
                    first.textContent = '1';
                    first.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = 1;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(first);

                    const dots1 = document.createElement('span');
                    dots1.style.padding = '8px 4px';
                    dots1.textContent = '...';
                    pagination.appendChild(dots1);

                    for (let i = currentPage - 1; i <= currentPage + 1; i++) {
                        if (i === currentPage) {
                            const current = document.createElement('span');
                            current.className = 'current';
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement('a');
                            pageLink.href = '#';
                            pageLink.textContent = i;
                            pageLink.addEventListener('click', (e) => {
                                e.preventDefault();
                                currentPage = i;
                                updatePagination();
                                updateTable();
                            });
                            pagination.appendChild(pageLink);
                        }
                    }

                    const dots2 = document.createElement('span');
                    dots2.style.padding = '8px 4px';
                    dots2.textContent = '...';
                    pagination.appendChild(dots2);

                    const last = document.createElement('a');
                    last.href = '#';
                    last.textContent = totalPages;
                    last.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = totalPages;
                        updatePagination();
                        updateTable();
                    });
                    pagination.appendChild(last);
                }
            }

            if (currentPage < totalPages) {
                const next = document.createElement('a');
                next.href = '#';
                next.innerHTML = '<i class="fas fa-angle-right"></i>';
                next.addEventListener('click', (e) => {
                    e.preventDefault();
                    currentPage++;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(next);
            } else {
                const disabled = document.createElement('span');
                disabled.style.opacity = '0.3';
                disabled.style.cursor = 'not-allowed';
                disabled.innerHTML = '<i class="fas fa-angle-right"></i>';
                pagination.appendChild(disabled);
            }

            if (currentPage < totalPages) {
                const last = document.createElement('a');
                last.href = '#';
                last.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                last.addEventListener('click', (e) => {
                    e.preventDefault();
                    currentPage = totalPages;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(last);
            } else {
                const disabled = document.createElement('span');
                disabled.style.opacity = '0.3';
                disabled.style.cursor = 'not-allowed';
                disabled.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                pagination.appendChild(disabled);
            }
        }

        function updateShowingCount() {
            const showingCount = document.getElementById('showingCount');
            const start = (currentPage - 1) * pageSize;
            const end = Math.min(start + pageSize, filteredRows.length);
            showingCount.textContent = filteredRows.length === 0 ? 0 : (end - start);
        }

        function showMaterialDetail(materialId, unitId) {
            if (!materialId) {
                alert('Material ID is missing.');
                return;
            }
            const base = window.location.origin
                       + window.location.pathname.replace(/\/list-material.*$/, '');
            const url = base + '/detail-material?id=' + materialId + (unitId ? '&unitId=' + unitId : '');

            fetch(url, {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            })
            .then(resp => {
                if (!resp.ok) throw new Error('HTTP status ' + resp.status);
                return resp.json();
            })
            .then(data => {
                document.getElementById('modalMaterialId').textContent           = data.materialId;
                document.getElementById('modalName').textContent                 = data.name;
                document.getElementById('modalUnitName').textContent             = data.unitName || '-';
                document.getElementById('modalPrice').textContent                = data.price != null 
                    ? parseFloat(data.price).toFixed(2).replace('.', ',') : '-';
                document.getElementById('modalQuantity').textContent             = data.quantity != null 
                    ? parseFloat(data.quantity).toFixed(2).replace('.', ',') : '-';
                document.getElementById('modalSupplierName').textContent         = data.supplierName || 'No Supplier';
                document.getElementById('modalStatus').textContent               = data.status;
                document.getElementById('modalCategoryName').textContent         = data.categoryName;

                document.getElementById('materialModal').style.display = 'block';
            })
            .catch(err => {
                console.error('Error fetching material detail:', err);
                alert('Cannot load material details.');
            });
        }

        function closeModal() {
            document.getElementById('materialModal').style.display = 'none';
        }

        window.addEventListener('click', function(e) {
            const modal = document.getElementById('materialModal');
            if (e.target === modal) {
                closeModal();
            }
        });

        window.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.view-detail').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    let mid = this.getAttribute('data-id');
                    let uid = this.getAttribute('data-unit-id');
                    showMaterialDetail(mid, uid);
                });
            });
        });
    </script>
</body>
</html>