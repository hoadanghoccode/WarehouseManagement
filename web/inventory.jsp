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
        table.table { width: 100%; border-collapse: collapse; min-width: 800px; }
        table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; }
        table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; }
        table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
        table.table tbody tr:hover { background-color: #eef2ff; }
        .action-buttons { display: flex; gap: 8px; }
        .no-data { text-align: center; padding: 16px; background-color: #f3f4f6; border-radius: 8px; font-size: 16px; color: #9ca3af; }
        .error { color: red; font-size: 14px; }
        .pagination { font-size: 14px; }
        .pagination a, .pagination {span: 2px; padding: 8px 12px; margin: 0 4px; text-decoration: none; color: #374151; }
        .pagination span { background-color: #9e9e9e1c; color: white; border-radius: 4px; }
        .pagination a:hover { background-color: #e5e7eb; border-radius: 4px; }
        @media (max-width: 768px) { .title { font-size: 24px; } table.table { min-width: 800px; }}
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true" />
    <section class="main_content dashboard_part">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <h1 class="title">Current Inventory</h1>
            <div class="row g-2 mb-3 align-items-center">
                <div class="col-md-4">
                    <select class="form-select" id="categoryFilter">
                        <option value="" ${empty categoryId ? 'selected' : ''}>All Categories</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.categoryId}" ${category.categoryId == categoryId ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <input type="text" id="searchInput" class="form-control" placeholder="Search by ID/Name..." value="${searchTerm}">
                </div>
                <div class="col-md-4">
                    <select class="form-select" id="sortFilter">
                        <option value="closing_qty ASC" ${sortBy == 'closing_qty ASC' ? 'selected' : ''}>Quantity Asc</option>
                        <option value="closing_qty DESC" ${sortBy == 'closing_qty DESC' ? 'selected' : ''}>Quantity Desc</option>
                        <option value="material_id ASC" ${sortBy == 'material_id ASC' ? 'selected' : ''}>Material ID Asc</option>
                        <option value="material_id DESC" ${sortBy == 'material_id DESC' ? 'selected' : ''}>Material ID Desc</option>
                        <option value="material_name ASC" ${sortBy == 'material_name ASC' ? 'selected' : ''}>Name Asc</option>
                        <option value="material_name DESC" ${sortBy == 'material_name DESC' ? 'selected' : ''}>Name Desc</option>
                    </select>
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
                            <th>Material ID</th>
                            <th>Material Name</th>
                            <th>Category</th>
                            <th>SubUnit Name</th>
                            <th>Quantity</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty inventoryList}">
                                <c:forEach var="item" items="${inventoryList}" varStatus="status">
                                    <tr data-index="${status.index + 1}" data-material-id="${item.materialId}" data-category-id="${item.categoryId}">
                                        <td class="row-number"><strong>${status.index + 1}</strong></td>
                                        <td class="material-id">${item.materialId}</td>
                                        <td class="material-name">${item.materialName}</td>
                                        <td class="category-name">${item.categoryName}</td>
                                        <td class="subunit-name">${item.subUnitName}</td>
                                        <td class="quantity"><fmt:formatNumber value="${item.closingQty}" pattern="#,##0.00"/></td>
                                        <td class="inventory-date">${item.inventoryDate}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="no-data">No inventory items found</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="pagination" id="pagination"></div>
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

            // Bind filter events
            document.querySelectorAll("#categoryFilter, #searchInput, #sortFilter").forEach((element) => {
                element.addEventListener("input", filterInventory);
                element.addEventListener("change", filterInventory);
            });
        });

        function filterInventory() {
            let searchText = document.getElementById("searchInput").value.trim().toLowerCase();
            let categoryValue = document.getElementById("categoryFilter").value;
            let sortBy = document.getElementById("sortFilter").value;

            console.log("Filter - Category: " + (categoryValue || "empty") + ", Search: " + searchText + ", Sort: " + sortBy);

            filteredRows = allRows.filter((row) => {
                let materialId = row.querySelector(".material-id").textContent.trim().toLowerCase();
                let materialName = row.querySelector(".material-name").textContent.trim().toLowerCase();
                let categoryName = row.querySelector(".category-name").textContent.trim().toLowerCase();
                let subUnitName = row.querySelector(".subunit-name").textContent.trim().toLowerCase();
                let quantityText = row.querySelector(".quantity").textContent.trim().replace(/,/g, "");
                let inventoryDate = row.querySelector(".inventory-date").textContent.trim().toLowerCase();
                let rowCategoryId = row.getAttribute("data-category-id");

                let isMatchSearch = !searchText || materialId.includes(searchText) || materialName.includes(searchText) ||
                                    categoryName.includes(searchText) || subUnitName.includes(searchText) ||
                                    quantityText.includes(searchText) || inventoryDate.includes(searchText);
                let isMatchCategory = !categoryValue || (rowCategoryId && parseInt(rowCategoryId) === parseInt(categoryValue));

                console.log("Row - materialId: " + materialId + ", rowCategoryId: " + rowCategoryId + 
                            ", categoryValue: " + categoryValue + ", isMatchCategory: " + isMatchCategory + ", isMatchSearch: " + isMatchSearch);

                return isMatchSearch && isMatchCategory;
            });

            // Sort filtered rows
            if (sortBy) {
                filteredRows.sort((a, b) => {
                    let aValue, bValue;
                    if (sortBy.includes("closing_qty")) {
                        aValue = parseFloat(a.querySelector(".quantity").textContent.replace(/,/g, "")) || 0;
                        bValue = parseFloat(b.querySelector(".quantity").textContent.replace(/,/g, "")) || 0;
                    } else if (sortBy.includes("material_id")) {
                        aValue = parseInt(a.querySelector(".material-id").textContent) || 0;
                        bValue = parseInt(b.querySelector(".material-id").textContent) || 0;
                    } else {
                        aValue = a.querySelector(".material-name").textContent.toLowerCase();
                        bValue = b.querySelector(".material-name").textContent.toLowerCase();
                    }
                    return sortBy.endsWith("ASC") ? (aValue > bValue ? 1 : -1) : (aValue < bValue ? 1 : -1);
                });
            }

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
                    if (totalPages > 6) {
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
                    }
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

                    if (totalPages > 6) {
                        const dots = document.createElement("span");
                        dots.className = "px-2 mx-1 text-muted";
                        dots.textContent = "...";
                        pagination.appendChild(dots);
                    }

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
    </script>
</body>
</html>