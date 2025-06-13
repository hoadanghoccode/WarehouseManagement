<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Current Inventory</title>
    <link rel="stylesheet" type="text/css" href="css/permissionlist.css" />
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
        * { box-sizing: border-box; }
        body { font-family: "Segoe UI", Tahoma, sans-serif; background-color: #f3f4f6; margin: 0; padding: 0; color: #374151; }
        .container { padding-top: 24px; padding-bottom: 24px; max-width: 1200px; margin: 0 auto; }
        .title { font-size: 28px; font-weight: 600; color: #1f2937; margin-bottom: 16px; }
        .stats-info { margin-bottom: 16px; color: #374151; display: flex; align-items: center; gap: 8px; background-color: #dbeafe; padding: 12px 16px; border-radius: 8px; font-size: 14px; }
        .stats-info i { color: #3b82f6; }
        .stats-info strong { color: #1f2937; }
        .table-container { overflow-x: auto; background-color: white; border-radius: 12px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08); }
        table.table { width: 100%; border-collapse: collapse; min-width: 1000px; }
        table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; vertical-align: middle; }
        table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; }
        table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
        table.table tbody tr:hover { background-color: #eef2ff; }
        .action-buttons { display: flex; gap: 8px; }
        .no-data { text-align: center; padding: 16px; background-color: #f3f4f6; border-radius: 8px; font-size: 16px; color: #6b7280; }
        .modal-content { background-color: white; margin: 6% auto; padding: 24px 32px; border-radius: 4px; width: 90%; max-width: 480px; position: relative; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); font-size: 14px; }
        .modal .close { position: absolute; top: 12px; right: 18px; font-size: 24px; cursor: pointer; color: #6b7280; }
        .modal .close:hover { color: #374151; }
        .detail-item { margin-bottom: 14px; display: flex; gap: 8px; }
        .detail-item strong { color: #333; width: 120px; }
        .error { color: #dc3545; font-size: 14px; }
        .pagination { font-size: 14px; }
        .pagination a, .pagination span { padding: 8px 12px; margin: 0 4px; text-decoration: none; color: #374151; }
        .pagination span { background-color: #9e9e9e1c; color: white; border-radius: 4px; }
        .pagination a:hover { background-color: #e5e7eb; border-radius: 4px; }
        .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 500; text-transform: capitalize; }
        .quality-available { background-color: #22c55e; color: white; }
        .quality-notavailable { background-color: #ef4444; color: white; }
        @media (max-width: 768px) { .title { font-size: 24px; } table.table { min-width: 1000px; } .modal-content { padding: 20px; } }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true" />
    <section class="main_content dashboard_part">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <h1 class="title">Current Inventory</h1>
            <div class="row g-2 mb-3 align-items-center">
                <div class="col-md-3">
                    <select class="form-select" id="categoryFilter">
                        <option value="0" ${categoryId == 0 ? 'selected' : ''}>All Categories</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.categoryId}" ${category.categoryId == categoryId ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="supplierFilter">
                        <option value="0" ${supplierId == 0 ? 'selected' : ''}>All Suppliers</option>
                        <c:forEach var="supplier" items="${supplierList}">
                            <option value="${supplier.supplierId}" ${supplier.supplierId == supplierId ? 'selected' : ''}>${supplier.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="qualityFilter">
                        <option value="0" ${qualityId == 0 ? 'selected' : ''}>All Qualities</option>
                        <c:forEach var="quality" items="${qualityList}">
                            <option value="${quality.qualityId}" ${quality.qualityId == qualityId ? 'selected' : ''}>${quality.qualityName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="text" id="searchInput" class="form-control" placeholder="Search by Name/ID..." value="${searchTerm}">
                </div>
            </div>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger">${errorMsg}</div>
            </c:if>
            <div class="table-container mb-4">
                <table class="table" id="inventoryTable">
                    <thead>
                        <tr>
                            <th style="width: 40px">#</th>
                            <th>Material Name</th>
                            <th>Category</th>
                            <th>Supplier</th>
                            <th>Subunit</th>
                            <th>Quality</th>
                            <th>Quantity</th>
                            <th>Date</th>
                            <th style="width: 100px">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty inventoryList}">
                                <c:forEach var="item" items="${inventoryList}" varStatus="status">
                                    <tr data-material-id="${item.materialId}" data-category-id="${item.categoryId}" 
                                        data-supplier-id="${item.supplierId}" data-subunit-id="${item.subUnitId}" 
                                        data-quality-id="${item.qualityId}">
                                        <td class="row-number"><strong>${status.index + 1}</strong></td>
                                        <td class="material-name">${item.materialName}</td>
                                        <td class="category-name">${item.categoryName}</td>
                                        <td class="supplier-name">${item.supplierName}</td>
                                        <td class="subunit-name">${item.subUnitName}</td>
                                        <td class="quality-name">
                                            <c:choose>
                                                <c:when test="${item.qualityName == 'available'}">
                                                    <span class="badge quality-available">Available</span>
                                                </c:when>
                                                <c:when test="${item.qualityName == 'notAvailable'}">
                                                    <span class="badge quality-notavailable">Not Available</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>${item.qualityName}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="quantity">
                                            <c:choose>
                                                <c:when test="${item.qualityName == 'available' and item.closingQty != null}">
                                                    <fmt:formatNumber value="${item.closingQty}" pattern="#,##0"/>
                                                </c:when>
                                                <c:when test="${item.qualityName == 'notAvailable' and item.damagedQuantity != null}">
                                                    <fmt:formatNumber value="${item.damagedQuantity}" pattern="#,##0.00"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>0</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="inventory-date"><fmt:formatDate value="${item.inventoryDate}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn btn-info btn-sm" data-bs-toggle="modal" data-bs-target="#detailModal" 
                                                        onclick="viewDetails(${item.materialId}, ${item.subUnitId}, '${item.materialName}', '${item.categoryName}', '${item.supplierName}', '${item.subUnitName}', '${item.qualityName}', ${item.closingQty}, '${item.inventoryDate}', ${item.openingQty}, ${item.importQty}, ${item.exportQty}, ${item.damagedQuantity != null ? item.damagedQuantity : 0}, '${item.note}')">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="no-data">No inventory items found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="pagination" id="pagination"></div>

            <!-- Detail Modal -->
            <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="detailModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="detailModalLabel">Inventory Details</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="detail-item"><strong>Material Name:</strong> <span id="modal-material-name"></span></div>
                            <div class="detail-item"><strong>Category:</strong> <span id="modal-category-name"></span></div>
                            <div class="detail-item"><strong>Supplier:</strong> <span id="modal-supplier-name"></span></div>
                            <div class="detail-item"><strong>Subunit:</strong> <span id="modal-subunit-name"></span></div>
                            <div class="detail-item"><strong>Quality:</strong> <span id="modal-quality"></span></div>
                            <div class="detail-item"><strong>Total Quantity (Available):</strong> <span id="modal-closing-qty"></span></div>
                            <div class="detail-item"><strong>Damaged Quantity:</strong> <span id="modal-damaged-qty"></span></div>
                            <div class="detail-item"><strong>Opening Quantity:</strong> <span id="modal-opening-qty"></span></div>
                            <div class="detail-item"><strong>Import Quantity:</strong> <span id="modal-import-qty"></span></div>
                            <div class="detail-item"><strong>Export Quantity:</strong> <span id="modal-export-qty"></span></div>
                            <div class="detail-item"><strong>Date:</strong> <span id="modal-inventory-date"></span></div>
                            <div class="detail-item"><strong>Note:</strong> <span id="modal-note"></span></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
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
        let filteredRows = [];

        window.addEventListener("DOMContentLoaded", function () {
            allRows = Array.from(document.querySelectorAll("#inventoryTable tbody tr:not(.no-data)"));
            filteredRows = [...allRows];
            updatePagination();
            updateTable();
            console.log("Inventory table loaded with " + allRows.length + " rows");

            document.querySelectorAll("#categoryFilter, #supplierFilter, #qualityFilter, #searchInput").forEach((element) => {
                element.addEventListener("input", filterInventory);
                element.addEventListener("change", filterInventory);
            });
        });

        function filterInventory() {
            let searchText = document.getElementById("searchInput").value.trim().toLowerCase();
            let categoryValue = document.getElementById("categoryFilter").value;
            let supplierValue = document.getElementById("supplierFilter").value;
            let qualityValue = document.getElementById("qualityFilter").value;

            console.log("Filter - Category: " + (categoryValue || "empty") + ", Supplier: " + (supplierValue || "empty") + ", Quality: " + (qualityValue || "empty") + ", Search: " + searchText);

            filteredRows = allRows.filter((row) => {
                let materialId = row.getAttribute("data-material-id");
                let materialName = row.querySelector(".material-name").textContent.trim().toLowerCase();
                let categoryName = row.querySelector(".category-name").textContent.trim().toLowerCase();
                let supplierName = row.querySelector(".supplier-name").textContent.trim().toLowerCase();
                let subUnitName = row.querySelector(".subunit-name").textContent.trim().toLowerCase();
                let qualityName = row.querySelector(".quality-name").textContent.trim().toLowerCase();
                let quantityText = row.querySelector(".quantity").textContent.trim().replace(/,/g, "");
                let inventoryDate = row.querySelector(".inventory-date").textContent.trim().toLowerCase();
                let rowCategoryId = row.getAttribute("data-category-id");
                let rowSupplierId = row.getAttribute("data-supplier-id");
                let rowQualityId = row.getAttribute("data-quality-id");

                let isMatchSearch = !searchText || materialId.includes(searchText) || materialName.includes(searchText) ||
                                    categoryName.includes(searchText) || supplierName.includes(searchText) ||
                                    subUnitName.includes(searchText) || qualityName.includes(searchText) ||
                                    quantityText.includes(searchText) || inventoryDate.includes(searchText);
                let isMatchCategory = !categoryValue || parseInt(rowCategoryId) === parseInt(categoryValue);
                let isMatchSupplier = !supplierValue || parseInt(rowSupplierId) === parseInt(supplierValue);
                let isMatchQuality = !qualityValue || (rowQualityId && parseInt(rowQualityId) === parseInt(qualityValue)) || (!rowQualityId && qualityValue === "0");

                return isMatchSearch && isMatchCategory && isMatchSupplier && isMatchQuality;
            });

            currentPage = 1;
            updatePagination();
            updateTable();
            console.log("Visible Rows: " + filteredRows.length);
        }

        function updateTable() {
            allRows.forEach((row) => {
                row.style.display = "none";
            });

            const start = (currentPage - 1) * pageSize;
            const end = Math.min(start + pageSize, filteredRows.length);
            const rowsToShow = filteredRows.slice(start, end);

            rowsToShow.forEach((row, index) => {
                row.style.display = "";
                const rowNumberCell = row.querySelector(".row-number strong");
                rowNumberCell.textContent = start + index + 1;
            });
        }

        function updatePagination() {
            const totalPages = Math.ceil(filteredRows.length / pageSize);
            const pagination = document.getElementById("pagination");
            pagination.innerHTML = "";

            if (totalPages <= 1) return;

            if (currentPage > 1) {
                const first = document.createElement("a");
                first.href = "#";
                first.innerHTML = '<i class="fas fa-angle-double-left"></i>';
                first.addEventListener("click", (e) => {
                    e.preventDefault();
                    currentPage = 1;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(first);

                const prev = document.createElement("a");
                prev.href = "#";
                prev.innerHTML = '<i class="fas fa-angle-left"></i>';
                prev.addEventListener("click", (e) => {
                    e.preventDefault();
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
                        current.className = "px-2 mx-1 bg-primary text-white rounded";
                        current.textContent = i;
                        pagination.appendChild(current);
                    } else {
                        const pageLink = document.createElement("a");
                        pageLink.href = "#";
                        pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                        pageLink.textContent = i;
                        pageLink.addEventListener("click", (e) => {
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
                            const current = document.createElement("span");
                            current.className = "px-2 mx-1 bg-primary text-white rounded";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (e) => {
                                e.preventDefault();
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
                    last.addEventListener("click", (e) => {
                        e.preventDefault();
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
                    first.addEventListener("click", (e) => {
                        e.preventDefault();
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
                            current.className = "px-2 mx-1 bg-primary text-white rounded";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (e) => {
                                e.preventDefault();
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
                    first.addEventListener("click", (e) => {
                        e.preventDefault();
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
                            current.className = "px-2 mx-1 bg-primary text-white rounded";
                            current.textContent = i;
                            pagination.appendChild(current);
                        } else {
                            const pageLink = document.createElement("a");
                            pageLink.href = "#";
                            pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                            pageLink.textContent = i;
                            pageLink.addEventListener("click", (e) => {
                                e.preventDefault();
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
                    last.addEventListener("click", (e) => {
                        e.preventDefault();
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
                next.addEventListener("click", (e) => {
                    e.preventDefault();
                    currentPage++;
                    updatePagination();
                    updateTable();
                });
                pagination.appendChild(next);

                const lastBtn = document.createElement("a");
                lastBtn.href = "#";
                lastBtn.innerHTML = '<i class="fas fa-angle-double-right"></i>';
                lastBtn.className = "ms-2 text-decoration-none text-dark";
                lastBtn.addEventListener("click", (e) => {
                    e.preventDefault();
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

        function viewDetails(materialId, subUnitId, materialName, categoryName, supplierName, subUnitName, qualityName, closingQty, inventoryDate, openingQty, importQty, exportQty, damagedQuantity, note) {
            document.getElementById("modal-material-name").textContent = materialName;
            document.getElementById("modal-category-name").textContent = categoryName;
            document.getElementById("modal-supplier-name").textContent = supplierName;
            document.getElementById("modal-subunit-name").textContent = subUnitName;
            document.getElementById("modal-quality").textContent = qualityName || 'N/A';
            document.getElementById("modal-closing-qty").textContent = closingQty.toLocaleString();
            document.getElementById("modal-damaged-qty").textContent = damagedQuantity ? damagedQuantity.toFixed(2) : '0.00';
            document.getElementById("modal-opening-qty").textContent = openingQty.toLocaleString();
            document.getElementById("modal-import-qty").textContent = importQty.toLocaleString();
            document.getElementById("modal-export-qty").textContent = exportQty.toLocaleString();
            document.getElementById("modal-inventory-date").textContent = inventoryDate;
            document.getElementById("modal-note").textContent = note || 'N/A';
        }
    </script>
</body>
</html>