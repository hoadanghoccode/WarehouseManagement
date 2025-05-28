<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Category List</title>
        <!--        file css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/categorylist.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">


    </head>

    <body>
        <div class="container">
            <!-- Header -->
            <div class="header">
                <h1 class="title">Category List</h1>
                <div class="header-actions">
                    <div class="search-container">
                        <form action="categorylist" method="get" style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" name="search" value="${search}" placeholder="Search categories..." class="search-input" />
                            </div>

                            <select class="form-select" id="statusFilter" name="status" onchange="this.form.submit()">
                                <option value="">All Status</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>

                        </form>
                    </div>
                    <button type="button" class="btn btn-success" onclick="openAddModal()">
                        <i class="fas fa-plus"></i> Add Category
                    </button>

                </div>
            </div>

            <!-- Stats information -->
            <c:if test="${not empty categoryList}">
                <div class="stats-info">
                    <i class="fas fa-info-circle"></i>
                    <c:choose>
                        <c:when test="${not empty search}">
                            Found <strong>${totalCategories}</strong> categories for keyword: "<strong>${search}</strong>"
                        </c:when>
                        <c:otherwise>
                            Showing <strong>${categoryList.size()}</strong> / <strong>${totalCategories}</strong> parent categories
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <!-- Main category table -->
            <c:choose>
                <c:when test="${not empty categoryList}">
                    <div class="table-container">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th class="col-md-1">#</th>
                                    <th class="col-md-5">Name</th>
                                    <th class="col-md-2">Status</th>
                                    <th class="col-md-2">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="category" items="${categoryList}" varStatus="status">
                                    <!-- Main category row -->
                                    <tr>
                                        <td><strong>${(page - 1) * pageSize + status.index + 1}</strong></td>
                                        <td>
                                            <div class="category-name">${category.name}</div>
                                            <c:choose>
                                                <c:when test="${category.subCategoryCount > 0}">
                                                    <span class="sub-count-btn" 
                                                          data-id="${status.index}"
                                                          title="Click to show/hide sub-categories">
                                                        <i class="fas fa-sitemap"></i> ${category.subCategoryCount} subcategories
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="sub-count-btn zero-count" 
                                                          title="No sub-categories">
                                                        <i class="fas fa-ban"></i> 0 subcategories
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <!-- Switch button -->
                                            <form action="categorylist" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="updateStatus" />
                                                <input type="hidden" name="categoryId" value="${category.categoryId}" />
                                                <input type="hidden" name="page" value="${page}" />
                                                <input type="hidden" name="search" value="${search}" />
                                                <input type="hidden" name="statusFilter" value="${param.status}" />

                                                <label class="switch">
                                                    <input type="checkbox" name="statusParam"
                                                           onchange="this.form.submit()"
                                                           ${category.status == 'active' ? 'checked' : ''}>
                                                    <span class="slider"></span>
                                                </label>
                                            </form>
                                        </td>

                                        <td>
                                            <div class="action-buttons">
                                                <a href="updatecategory?cid=${category.categoryId}" class="btn btn-primary">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="deletecategory?cid=${category.categoryId}" 
                                                   class="btn btn-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>

                                    <!-- Sub-category row with table format -->
                                    <c:if test="${category.subCategoryCount > 0}">
                                        <tr class="subcat-row" id="tooltip-${status.index}" style="display: none;">
                                            <td colspan="4">
                                                <div class="subcat-content">
                                                    <div class="subcat-title">
                                                        <i class="fas fa-list-ul"></i> 
                                                        Sub-Categories of "${category.name}":
                                                    </div>

                                                    <c:choose>
                                                        <c:when test="${not empty category.subCategories}">
                                                            <table class="subcat-table">

                                                                <tbody>
                                                                    <c:forEach var="sub" items="${category.subCategories}" varStatus="subStatus">
                                                                        <tr>
                                                                            <td>
                                                                                <span class="subcat-number">${subStatus.index + 1}.</span>
                                                                            </td>
                                                                            <td style="width: 583px">
                                                                                <div class="subcat-name">

                                                                                    <a href="material?cid=${sub.categoryId}">${sub.name}</a>
                                                                                    <span class="material-count" style="font-size: 12px">(${sub.materialCount})</span>
                                                                                </div>
                                                                            </td>
                                                                            <td style="width: 235px">
                                                                                <form action="categorylist" method="post" style="display:inline;">
                                                                                    <input type="hidden" name="action" value="updateStatus" />
                                                                                    <input type="hidden" name="categoryId" value="${sub.categoryId}" />
                                                                                    <input type="hidden" name="page" value="${page}" />
                                                                                    <input type="hidden" name="search" value="${search}" />
                                                                                    <input type="hidden" name="statusFilter" value="${param.status}" />

                                                                                    <label class="switch">
                                                                                        <input type="checkbox" name="statusParam"
                                                                                               onchange="this.form.submit()"
                                                                                               ${sub.status == 'active' ? 'checked' : ''}>
                                                                                        <span class="slider"></span>
                                                                                    </label>
                                                                                </form>
                                                                            </td>
                                                                            <td>
                                                                                <div class="action-buttons">
                                                                                    <a href="updatecategory?cid=${sub.categoryId}" class="btn btn-primary">
                                                                                        <i class="fas fa-edit"></i>
                                                                                    </a>
                                                                                    <a href="deletecategory?cid=${sub.categoryId}" class="btn btn-danger">
                                                                                        <i class="fas fa-trash"></i>
                                                                                    </a>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <p style="color: #6b7280; font-style: italic;">
                                                                <i class="fas fa-ban"></i> No sub-categories found.
                                                            </p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
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
                        <h3 style="margin-bottom: 8px;">No categories found</h3>
                        <c:choose>
                            <c:when test="${not empty search}">
                                <p>No categories match the keyword: "<strong>${search}</strong>"</p>
                                <a href="categorylist" class="btn btn-primary" style="margin-top: 16px;">View all categories</a>
                            </c:when>
                            <c:otherwise>
                                <p>No categories in the system yet</p>
                                <a href="addcategory" class="btn btn-success" style="margin-top: 16px;">Add first category</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:otherwise>
            </c:choose>

            <!-- Pagination -->
            <c:if test="${num > 1}">
                <div class="pagination">
                    <!-- First and Previous buttons -->
                    <c:choose>
                        <c:when test="${page > 1}">
                            <a href="categorylist?page=1<c:if test='${not empty search}'>&search=${search}</c:if>" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="categorylist?page=${page-1}<c:if test='${not empty search}'>&search=${search}</c:if>" title="Previous page">
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
                        <c:when test="${num <= 7}">
                            <!-- Show all pages if total pages <= 7 -->
                            <c:forEach begin="1" end="${num}" var="i">
                                <c:choose>
                                    <c:when test="${i == page}">
                                        <span class="current">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="categorylist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if>">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <!-- Smart pagination for many pages -->
                            <c:choose>
                                <c:when test="${page <= 4}">
                                    <!-- Show first 5 pages, then ... and last page -->
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}">
                                                <span class="current">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="categorylist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if>">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <c:if test="${num > 6}">
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="categorylist?page=${num}<c:if test='${not empty search}'>&search=${search}</c:if>">${num}</a>
                                    </c:if>
                                </c:when>
                                <c:when test="${page >= num - 3}">
                                    <!-- Show first page, then ... and last 5 pages -->
                                    <a href="categorylist?page=1<c:if test='${not empty search}'>&search=${search}</c:if>">1</a>
                                    <c:if test="${num > 6}">
                                        <span style="padding: 8px 4px;">...</span>
                                    </c:if>
                                    <c:forEach begin="${num - 4}" end="${num}" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}">
                                                <span class="current">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="categorylist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if>">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <!-- Show first page, current page with neighbors, and last page -->
                                    <a href="categorylist?page=1<c:if test='${not empty search}'>&search=${search}</c:if>">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                    <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                        <c:choose>
                                            <c:when test="${i == page}">
                                                <span class="current">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="categorylist?page=${i}<c:if test='${not empty search}'>&search=${search}</c:if>">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <span style="padding: 8px 4px;">...</span>
                                    <a href="categorylist?page=${num}<c:if test='${not empty search}'>&search=${search}</c:if>">${num}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                    <!-- Next and Last buttons -->
                    <c:choose>
                        <c:when test="${page < num}">
                            <a href="categorylist?page=${page+1}<c:if test='${not empty search}'>&search=${search}</c:if>" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="categorylist?page=${num}<c:if test='${not empty search}'>&search=${search}</c:if>" title="Last page">
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
                    Page ${page} of ${num}
                    <c:if test="${not empty totalCategories}">
                        (${totalCategories} total categories)
                    </c:if>
                </div>
            </c:if>
        </div>

        <!-- Add Category Modal -->

        <div id="addModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Add New Category</h2>
                    <span class="close" onclick="closeModal('addModal')">&times;</span>
                </div>
                <form action="categorylist" method="post">
                    <input type="hidden" name="action" value="add"/>
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="addCategoryName">Category Name:</label>
                            <input type="text" id="addCategoryName" name="categoryName" placeholder="Enter category name" required>
                        </div>

                        <div class="form-group">
                            <label for="addParentId">Parent Category (optional):</label>
                            <select id="addParentId" name="parentId">
                                <option value="0">-- None --</option>
                                <c:forEach var="cat" items="${parentCategories}">
                                    <option value="${cat.categoryId}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-blue">Save Category</button>
                        <button type="button" class="btn btn-gray" onclick="closeModal('addModal')">Cancel</button>
                    </div>
                </form>
            </div>
        </div>



        <script src="js/categorylist.js"></script>

        <script>
//                            function openAddModal() {
//                                document.getElementById('addModal').style.display = 'flex';
//                                document.getElementById('addCategoryName').focus();
//                            }
//
//                            function closeModal(id) {
//                                document.getElementById(id).style.display = 'none';
//                            }

                            const status = new URLSearchParams(window.location.search).get("status");
                            if (status === "success") {
                                alert("Thêm danh mục thành công!");
                            } else if (status === "fail") {
                                alert("Thêm danh mục thất bại!");
                            }

                            function openAddModal() {
                                console.log("Opening modal..."); // Debug
                                document.getElementById('addModal').style.display = 'flex';
                                document.getElementById('addCategoryName').focus();
                            }

                            function closeModal(id) {
                                document.getElementById(id).style.display = 'none';
                                // Clear form khi đóng modal
                                document.getElementById('addCategoryName').value = '';
                                document.getElementById('addParentId').value = '0';
                            }

                            // Tự động hiển thị modal khi có lỗi validation
                            document.addEventListener('DOMContentLoaded', function () {
            <c:if test="${showAddModal}">
                                console.log("Auto showing modal due to validation error");
                                openAddModal();

                                // Restore form values
                <c:if test="${not empty categoryName}">
                                document.getElementById('addCategoryName').value = '${categoryName}';
                </c:if>
                <c:if test="${not empty parentId}">
                                document.getElementById('addParentId').value = '${parentId}';
                </c:if>
            </c:if>

                                // Hiển thị success message nếu có
            <c:if test="${not empty successMessage}">
                                document.addEventListener('DOMContentLoaded', function () {
                                    alert('${successMessage}');
                                });
            </c:if>
                            });

                            // Hiển thị error message nếu có
            <c:if test="${not empty errorMessage}">
                            document.addEventListener('DOMContentLoaded', function () {
                                alert('${errorMessage}');
                            });
            </c:if>
        </script>

    </body>
</html>