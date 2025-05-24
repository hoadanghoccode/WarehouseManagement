<%-- 
    Document   : materialList
    Created on : May 21, 2025, 12:55:17 AM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Materials List</title>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <h1 class="title">Materials List</h1>
            <div class="header-actions">
                <div class="search-container">
                    <div style="display: flex; gap: 12px; align-items: center;">
                        <div style="position: relative;">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text" id="searchInput" placeholder="Search materials..." class="search-input" oninput="filterTable()" />
                        </div>
                        <!-- Category Filter -->
                        <select class="form-select" id="categoryFilter" onchange="filterTable()">
                            <option value="">All Categories</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.name}">${category.name}</option>
                            </c:forEach>
                        </select>
                        <!-- Quantity Filter -->
                        <div class="quantity-filter">
                            <input type="number" class="form-control" id="quantityFilterMin" placeholder="Min" oninput="filterTable()">
                            <input type="number" class="form-control" id="quantityFilterMax" placeholder="Max" oninput="filterTable()">
                        </div>
                    </div>
                </div>
                <a href="material?action=add" class="btn btn-success">
                    <i class="fas fa-plus"></i> Add Material
                </a>
            </div>
        </div>

        <!-- Stats information -->
        <c:if test="${not empty materials}">
            <div class="stats-info">
                <i class="fas fa-info-circle"></i>
                Showing <strong id="showingCount">${materials.size()}</strong> / <strong id="totalCount">${totalMaterials}</strong> materials
            </div>
        </c:if>

        <!-- Main material table -->
        <c:choose>
            <c:when test="${not empty materials}">
                <div class="table-container">
                    <table class="table" id="materialsTable">
                        <thead>
                            <tr>
                                <th style="width: 60px;">#</th>
                                <th>Name</th>
                                <th>Unit of Calculation</th>
                                <th>Inventory Quantity</th>
                                <th>Unit</th>
                                <th>Category</th>
                                <th>Parent Category</th>
                                <th style="width: 200px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${materials}" varStatus="status">
                                <tr data-id="${material.materialId}">
                                    <td><strong>${(currentPage - 1) * pageSize + status.index + 1}</strong></td>
                                    <td>
                                        <div class="material-name">${material.name}</div>
                                    </td>
                                    <td>${material.unitOfCalculation}</td>
                                    <td>${material.inventoryQuantity}</td>
                                    <td>${material.unitName}</td>
                                    <td>${material.categoryName}</td>
                                    <td>${material.parentCategoryName != null ? material.parentCategoryName : '-'}</td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="material?action=detail&id=${material.materialId}" class="btn btn-info" title="View">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="material?action=update&id=${material.materialId}" class="btn btn-primary" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="material?action=delete&id=${material.materialId}" class="btn btn-danger" title="Delete"
                                               onclick="return confirm('Are you sure you want to delete this material?')">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    <div class="no-data-icon">
                        <i class="fas fa-folder-open"></i>
                    </div>
                    <h3 style="margin-bottom: 8px;">No materials found</h3>
                    <p>No materials in the system yet</p>
                    <a href="material?action=add" class="btn btn-success" style="margin-top: 16px;">Add first material</a>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <!-- First and Previous buttons -->
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="material?action=list&page=1" title="First page">
                            <i class="fas fa-angle-double-left"></i>
                        </a>
                        <a href="material?action=list&page=${currentPage-1}" title="Previous page">
                            <i class="fas fa-angle-left"></i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span style="opacity: 0.3; cursor: not-allowed;" title="First page">
                            <i class="fas fa-angle-double-left"></i>
                        </span>
                        <span style="opacity: 0.3; cursor: not-allowed;" title="Previous page">
                            <i class="fas fa-angle-left"></i>
                        </span>
                    </c:otherwise>
                </c:choose>

                <!-- Page numbers with smart pagination -->
                <c:choose>
                    <c:when test="${totalPages <= 7}">
                        <!-- Show all pages if total pages <= 7 -->
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="material?action=list&page=${i}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Smart pagination for many pages -->
                        <c:choose>
                            <c:when test="${currentPage <= 4}">
                                <!-- Show first 5 pages, then ... and last page -->
                                <c:forEach begin="1" end="5" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${totalPages > 6}">
                                    <span style="padding: 8px 4px;">...</span>
                                    <a href="material?action=list&page=${totalPages}">${totalPages}</a>
                                </c:if>
                            </c:when>
                            <c:when test="${currentPage >= totalPages - 3}">
                                <!-- Show first page, then ... and last 5 pages -->
                                <a href="material?action=list&page=1">1</a>
                                <c:if test="${totalPages > 6}">
                                    <span style="padding: 8px 4px;">...</span>
                                </c:if>
                                <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <!-- Show first page, current page with neighbors, and last page -->
                                <a href="material?action=list&page=1">1</a>
                                <span style="padding: 8px 4px;">...</span>
                                <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="current">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="material?action=list&page=${i}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <span style="padding: 8px 4px;">...</span>
                                <a href="material?action=list&page=${totalPages}">${totalPages}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

                <!-- Next and Last buttons -->
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="material?action=list&page=${currentPage+1}" title="Next page">
                            <i class="fas fa-angle-right"></i>
                        </a>
                        <a href="material?action=list&page=${totalPages}" title="Last page">
                            <i class="fas fa-angle-double-right"></i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span style="opacity: 0.3; cursor: not-allowed;" title="Next page">
                            <i class="fas fa-angle-right"></i>
                        </span>
                        <span style="opacity: 0.3; cursor: not-allowed;" title="Last page">
                            <i class="fas fa-angle-double-right"></i>
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination info -->
            <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                Page ${currentPage} of ${totalPages}
                <c:if test="${not empty totalMaterials}">
                    (<span id="totalMaterialsCount">${totalMaterials}</span> total materials)
                </c:if>
            </div>
        </c:if>
    </div>

    <!-- JavaScript for client-side filtering -->
    <script>
        function filterTable() {
            const searchInput = document.getElementById("searchInput").value.toLowerCase();
            const categoryFilter = document.getElementById("categoryFilter").value;
            const quantityMin = parseInt(document.getElementById("quantityFilterMin").value) || 0;
            const quantityMax = parseInt(document.getElementById("quantityFilterMax").value) || Number.MAX_SAFE_INTEGER;

            const table = document.getElementById("materialsTable");
            const rows = table.getElementsByTagName("tr");
            let visibleRows = 0;

            for (let i = 1; i < rows.length; i++) { // Start from 1 to skip header row
                const cells = rows[i].getElementsByTagName("td");
                const name = cells[1].textContent.toLowerCase();
                const quantity = parseInt(cells[3].textContent);
                const unit = cells[4].textContent;
                const category = cells[5].textContent;
                const parentCategory = cells[6].textContent;

                // Apply filters
                const matchesSearch = name.includes(searchInput);
                const matchesCategory = categoryFilter === "" || category === categoryFilter || parentCategory === categoryFilter;
                const matchesQuantity = quantity >= quantityMin && quantity <= quantityMax;

                // Show or hide row based on all filter conditions
                if (matchesSearch && matchesCategory && matchesQuantity) {
                    rows[i].style.display = "";
                    visibleRows++;
                } else {
                    rows[i].style.display = "none";
                }
            }

            // Update stats info
            document.getElementById("showingCount").textContent = visibleRows;
            const totalCount = parseInt(document.getElementById("totalMaterialsCount").textContent);
            document.getElementById("totalCount").textContent = totalCount;
        }
    </script>
</body>
</html>