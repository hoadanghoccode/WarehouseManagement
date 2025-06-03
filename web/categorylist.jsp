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
                                                <button type="button" class="btn btn-primary" onclick="openUpdateModal('${category.categoryId}', '${category.name}', '${category.parentId}', '${category.status}')">
                                                    <i class="fas fa-edit"></i>
                                                </button>
<!--                                               <a href="deletecategory?cid=${category.categoryId}" 
                                                   class="btn btn-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>-->
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
                                                                                    <button type="button" class="btn btn-primary" onclick="openUpdateModal('${sub.categoryId}', '${sub.name}', '${sub.parentId.categoryId}', '${sub.status}')">
                                                                                        <i class="fas fa-edit"></i>
                                                                                    </button>
<!--                                                                                    <a href="deletecategory?cid=${sub.categoryId}" class="btn btn-danger">
                                                                                        <i class="fas fa-trash"></i>
                                                                                    </a>-->
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
                                    <c:if test="${cat.status == 'active'}">
                                        <option value="${cat.categoryId}">${cat.name} </option>
                                    </c:if>
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

        <!-- Update Category Modal -->
        <div id="updateModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Update Category</h2>
                    <span class="close" onclick="closeModal('updateModal')">&times;</span>
                </div>
                <form action="categorylist" method="post">
                    <input type="hidden" name="action" value="updateModal"/>
                    <input type="hidden" id="updateCategoryId" name="categoryId"/>
                    <input type="hidden" name="page" value="${page}" />
                    <input type="hidden" name="search" value="${search}" />
                    <input type="hidden" name="statusFilter" value="${param.status}" />

                    <div class="modal-body">
                        <div class="form-group">
                            <label for="updateCategoryName">Category Name:</label>
                            <input type="text" id="updateCategoryName" name="categoryName" placeholder="Enter category name" required>
                        </div>

                        <div class="form-group">
                            <label for="updateParentId">Parent Category (optional):</label>
                            <select id="updateParentId" name="parentId">
                                <option value="0">-- None --</option>
                                <c:forEach var="cat" items="${allCategories}">
                                    <option value="${cat.categoryId}" class="parent-option" data-category-id="${cat.categoryId}">
                                        <c:choose>
                                            <c:when test="${cat.parentId != null}">
                                                ├── ${cat.name} ${cat.status == 'inactive' ? ' - inactive' : ''}
                                            </c:when>
                                            <c:otherwise>
                                                ${cat.name} ${cat.status == 'inactive' ? ' - inactive' : ''}
                                            </c:otherwise>
                                        </c:choose>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="updateStatus">Status:</label>
                            <select id="updateStatus" name="categoryStatus" required>
                                <option value="active">Active</option>
                                <option value="inactive">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-blue">Update Category</button>
                        <button type="button" class="btn btn-gray" onclick="closeModal('updateModal')">Cancel</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Confirm Update Category Modal -->
        <div id="updateConfirmModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Confirm Update Category</h2>
                    <span class="close" onclick="closeModal('updateConfirmModal')">&times;</span>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to update this category with the following changes?</p>

                    <!-- LUÔN HIỂN THỊ THÔNG TIN VỀ SUB-CATEGORIES VÀ MATERIALS NỀU CÓ -->
                    <c:if test="${(countSub > 0 || countMaterial > 0)}">
                        <div class="warning-box">
                            <div class="warning-header">
                                <i class="fas fa-info-circle warning-icon"></i>
                                <span class="warning-title">Information</span>
                            </div>
                            <div class="warning-content">
                                This category contains 
                                <c:if test="${countSub > 0}"><strong>${countSub}</strong> sub-categories</c:if>
                                <c:if test="${countSub > 0 && countMaterial > 0}"> and </c:if>
                                <c:if test="${countMaterial > 0}"><strong>${countMaterial}</strong> materials</c:if>.
                                    <br/><br/>

                                    <!-- CHỈ HIỂN THỊ CẢNH BÁO KHI THAY ĐỔI STATUS SANG INACTIVE -->
                                <c:if test="${updateConfirmStatus == 'inactive'}">
                                    <span style="color: #991B1B;">
                                        <i class="fas fa-exclamation-triangle"></i>
                                        All of these sub-categories and materials will also have their status changed to inactive.
                                    </span>
                                </c:if>

                                <!-- HIỂN THỊ THÔNG TIN CHUNG KHI KHÔNG PHẢI LÀ THAY ĐỔI STATUS SANG INACTIVE -->
                                <c:if test="${updateConfirmStatus != 'inactive'}">
                                    <span style="color: #059669;">
                                        <i class="fas fa-check-circle"></i>
                                        Please review your changes carefully as this category has related data.
                                    </span>
                                </c:if>
                            </div>
                        </div>
                    </c:if>

                    <!-- HIỂN THỊ KHI KHÔNG CÓ SUB-CATEGORIES HOẶC MATERIALS -->
                    <c:if test="${countSub == 0 && countMaterial == 0}">
                        <div class="info-box" style="background-color: #f0f9ff; border: 1px solid #0ea5e9; border-radius: 8px; padding: 16px; margin: 16px 0;">
                            <div style="display: flex; align-items: center; gap: 8px; color: #0369a1;">
                                <i class="fas fa-info-circle"></i>
                                <span style="font-weight: 500;">This category has no sub-categories or materials.</span>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <form action="categorylist" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="confirmUpdateCategory"/>
                        <input type="hidden" name="categoryId" value="${updateConfirmCategoryId}"/>
                        <input type="hidden" name="categoryName" value="${updateConfirmName}"/>
                        <input type="hidden" name="parentId" value="${updateConfirmParentId}"/>
                        <input type="hidden" name="categoryStatus" value="${updateConfirmStatus}"/>
                        <input type="hidden" name="page" value="${page}"/>
                        <input type="hidden" name="search" value="${search}"/>
                        <input type="hidden" name="statusFilter" value="${param.status}"/>

                        <button type="submit" class="btn btn-blue">Yes, Update Category</button>
                    </form>
                    <button type="button" class="btn btn-gray" onclick="closeModal('updateConfirmModal')">Cancel</button>
                </div>
            </div>
        </div>


        <!-- Error Modal -->
        <div id="errorModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Update Failed</h2>
                    <button type="button" class="modal-close" onclick="closeErrorModal()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <p>Unable to update the category status due to the following reason:</p>

                    <div class="warning-box">
                        <div class="warning-header">
                            <i class="fas fa-exclamation-triangle warning-icon"></i>
                            <span class="warning-title">Warning</span>
                        </div>
                        <p class="warning-content" id="errorMessage">
                            ${errorMessage}
                        </p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-gray" onclick="closeErrorModal()">
                        <i class="fas fa-check"></i> I Understand
                    </button>
                </div>
            </div>
        </div>


        <!-- Confirm Status Change Modal -->
        <div id="confirmModal" class="modal">
            <div class="modal-card">
                <div class="modal-header">
                    <h2>Change Status?</h2>
                    <span class="close" onclick="closeModal('confirmModal')">&times;</span>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to change the status of this category?</p>

                    <c:if test="${countSub > 0 || countMaterial > 0}">
                        <div class="warning-box">
                            <div class="warning-header">
                                <i class="fas fa-exclamation-triangle warning-icon"></i>
                                <span class="warning-title">Warning</span>
                            </div>
                            <div class="warning-content">
                                This category contains 
                                <c:if test="${countSub > 0}"><strong>${countSub}</strong> sub-categories</c:if>
                                <c:if test="${countSub > 0 && countMaterial > 0}"> and </c:if>
                                <c:if test="${countMaterial > 0}"><strong>${countMaterial}</strong> materials</c:if>.
                                    <br/><br/>
                                    <span style="color: #991B1B;">
                                        All of these sub-categories and materials will also have their status changed.
                                    </span>
                                </div>
                            </div>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <form action="categorylist" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="confirmUpdateStatus"/>
                        <input type="hidden" name="categoryId" value="${confirmCategoryId}"/>
                        <input type="hidden" name="newStatus" value="${confirmNewStatus}"/>
                        <input type="hidden" name="page" value="${page}"/>
                        <input type="hidden" name="search" value="${search}"/>
                        <input type="hidden" name="statusFilter" value="${param.status}"/>

                        <button type="submit" class="btn btn-blue">Yes, Change Status</button>
                    </form>
                    <button type="button" class="btn btn-gray" onclick="closeModal('confirmModal')">No, Keep Current</button>
                </div>
            </div>
        </div>

        <script>
            // Truyền dữ liệu từ server sang JavaScript
            <c:if test="${showAddModal}">
            window.showAddModal = true;
            </c:if>

            <c:if test="${showUpdateModal}">
            window.showUpdateModal = true;
            </c:if>

            <c:if test="${showUpdateConfirmModal}">
            window.showUpdateConfirmModal = true;
            </c:if>

            <c:if test="${not empty categoryName}">
            window.categoryName = '${categoryName}';
            </c:if>

            <c:if test="${not empty parentId}">
            window.parentId = '${parentId}';
            </c:if>

            <c:if test="${not empty categoryStatus}">
            window.categoryStatus = '${categoryStatus}';
            </c:if>

            <c:if test="${not empty updateCategoryId}">
            window.updateCategoryId = '${updateCategoryId}';
            </c:if>

            <c:if test="${not empty errorMessage}">
            window.errorMessage = '${errorMessage}';
            </c:if>

            <c:if test="${not empty successMessage}">
            window.successMessage = '${successMessage}';
            </c:if>

            <c:if test="${showConfirmModal}">
            window.showConfirmModal = true;
            </c:if>

            <c:if test="${showUpdateConfirmModal}">
            document.addEventListener('DOMContentLoaded', function () {
                document.getElementById('updateConfirmModal').style.display = 'flex';
            });
            </c:if>
        </script>

        <script src="js/categorylist.js"></script>

    </body>
</html>