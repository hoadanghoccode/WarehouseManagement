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
        table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; vertical-align: middle; }
        table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; position: sticky; top: 0; z-index: 2; }
        table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
        table.table tbody tr:hover { background-color: #eef2ff; }
        .action-buttons { display: flex; gap: 8px; }
        .no-data { text-align: center; padding: 24px; background-color: #f3f4f6; border-radius: 12px; font-size: 16px; color: #9ca3af; }
        .error { color: red; font-size: 14px; }
        @media (max-width: 768px) { .title { font-size: 24px; } table.table { min-width: 800px; } }
    </style>
</head>
<body>
    <jsp:include page="sidebar.jsp" flush="true"/>
    <section class="main_content dashboard_part">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <h1 class="title">Current Inventory</h1>
            <div class="row g-2 mb-3 align-items-center">
                <div class="col-md-4">
                    <select class="form-select" id="categoryFilter" onchange="filterInventory()">
                        <option value="">All Categories</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.categoryId}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <input type="text" id="searchInput" class="form-control" placeholder="Search by ID/Name..." oninput="filterInventory()">
                </div>
                <div class="col-md-4">
                    <select class="form-select" id="sortFilter" onchange="filterInventory()">
                        <option value="quantity ASC">Quantity Asc</option>
                        <option value="quantity DESC">Quantity Desc</option>
                        <option value="material_id ASC">Material ID Asc</option>
                        <option value="material_id DESC">Material ID Desc</option>
                        <option value="material_name ASC">Name Asc</option>
                        <option value="material_name DESC">Name Desc</option>
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
                            <th>Category Name</th>
                            <th>Unit</th>
                            <th>Quantity</th>
                            <th>Last Updated</th>
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
                                        <td class="unit-name">${item.unitName}</td>
                                        <td class="quantity"><fmt:formatNumber value="${item.quantity}" pattern="#,##0.00"/></td>
                                        <td class="last-updated"><fmt:formatDate value="${item.lastUpdated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
        function filterInventory() {
            const categoryId = $('#categoryFilter').val();
            const search = $('#searchInput').val().toLowerCase();
            const sortBy = $('#sortFilter').val();

            $('#inventoryTable tbody tr').each(function () {
                const materialId = $(this).find('.material-id').text().toLowerCase();
                const materialName = $(this).find('.material-name').text().toLowerCase();
                const categoryName = $(this).find('.category-name').text().toLowerCase();
                const unitName = $(this).find('.unit-name').text().toLowerCase();
                const quantity = $(this).find('.quantity').text().toLowerCase();
                const lastUpdated = $(this).find('.last-updated').text().toLowerCase();
                const rowCategoryId = $(this).data('category-id');

                const matchCategory = !categoryId || rowCategoryId == categoryId;
                const matchSearch = !search || materialId.includes(search) || materialName.includes(search) || categoryName.includes(search) || unitName.includes(search) || quantity.includes(search) || lastUpdated.includes(search);

                $(this).toggle(matchCategory && matchSearch);
                if (sortBy) {
                    const $rows = $('#inventoryTable tbody tr:visible');
                    $rows.sort((a, b) => {
                        let aValue, bValue;
                        if (sortBy.includes('quantity')) {
                            aValue = $(a).find('.quantity').text();
                            bValue = $(b).find('.quantity').text();
                        } else if (sortBy.includes('material_id')) {
                            aValue = $(a).find('.material-id').text();
                            bValue = $(b).find('.material-id').text();
                        } else {
                            aValue = $(a).find('.material-name').text();
                            bValue = $(b).find('.material-name').text();
                        }
                        return sortBy.endsWith('ASC') ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
                    }).appendTo('#inventoryTable tbody');
                }
            });
        }

        $(document).ready(function () {
            $('#categoryFilter, #searchInput, #sortFilter').on('input change', filterInventory);
        });
    </script>
</body>
</html>