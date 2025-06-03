<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <!-- themefy CSS -->
    <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
    <!-- swiper slider CSS -->
    <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
    <!-- select2 CSS -->
    <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
    <!-- select2 CSS -->
    <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
    <!-- owl carousel CSS -->
    <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
    <!-- gijgo css -->
    <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
    <!-- date picker -->
    <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
    <!-- datatable CSS -->
    <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
    <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
    <!-- text editor css -->
    <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
    <!-- morris css -->
    <link rel="stylesheet" href="vendors/morris/morris.css">
    <!-- metarial icon css -->
    <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
    <!-- menu css  -->
    <link rel="stylesheet" href="css/metisMenu.css">
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f8fafc;
            color: #334155;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 24px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }
        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
        }
        .header-actions {
            display: flex;
            gap: 12px;
            align-items: center;
        }
        .search-container {
            position: relative;
            max-width: 500px;
            width: 100%;
            margin: 0 auto 12px;
        }
        .search-input {
            width: 100%;
            padding: 10px 16px 10px 40px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: #ffffff;
            transition: all 0.2s;
        }
        .search-input:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }
        .search-icon {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: #6b7280;
            font-size: 14px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s;
        }
        .btn-primary {
            background-color: #3b82f6;
            color: #ffffff;
        }
        .btn-primary:hover {
            background-color: #2563eb;
        }
        .table-container {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin-top: 24px;
        }
        .table {
            width: 100%;
            border-collapse: collapse;
        }
        .table th {
            background-color: #f8fafc;
            padding: 16px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            color: #374151;
            border-bottom: 1px solid #e5e7eb;
        }
        .table td {
            padding: 16px;
            border-bottom: 1px solid #f3f4f6;
            vertical-align: middle;
        }
        .table tr:hover {
            background-color: #f9fafb;
        }
        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #e5e7eb;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .avatar:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        .category-name {
            font-weight: 500;
            color: #1e293b;
            text-decoration: none;
            transition: color 0.2s;
        }
        .category-name:hover {
            color: #3b82f6;
            text-decoration: underline;
        }
        .status-inactive {
            display: inline-block;
            padding: 4px 8px;
            background-color: #fef2f2;
            color: #dc2626;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
        }
        .status-active {
            display: inline-block;
            padding: 4px 8px;
            background-color: #ecfdf5;
            color: #059669;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
        }
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 32px;
        }
        .pagination a,
        .pagination span {
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
            color: #ffffff;
            border-color: #3b82f6;
        }
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }
        .modal-content {
            background-color: #ffffff;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            overflow-y: auto;
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }
        .modal-title {
            font-size: 18px;
            font-weight: 600;
            color: #1e293b;
        }
        .close-btn {
            font-size: 18px;
            color: #6b7280;
            background: none;
            border: none;
            cursor: pointer;
        }
        .form-group {
            margin-bottom: 16px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #374151;
            font-size: 14px;
        }
        .form-group input,
        .form-group select,
        .form-group p {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: #ffffff;
            transition: all 0.2s;
        }
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }
        .form-group p {
            color: #1f2937;
            background-color: #ffffff;
        }
        .error-message {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #fef2f2;
            color: #dc2626;
            padding: 8px 12px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 16px;
        }
        .success-message {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #ecfdf5;
            color: #059669;
            padding: 8px 12px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 12px;
        }
        .dismiss-btn {
            background: none;
            border: none;
            color: inherit;
            font-size: 14px;
            cursor: pointer;
            font-weight: bold;
        }
        .filter-form {
            display: flex;
            gap: 12px;
            margin-bottom: 24px;
            flex-wrap: wrap;
            justify-content: center;
        }
        .filter-select {
            flex: 1;
            min-width: 150px;
            max-width: 200px;
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
        }
        .filter-form .btn {
            flex: 0;
            min-width: 100px;
        }
        .stats-info {
            background-color: #eff6ff;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            color: #1e40af;
            font-size: 14px;
            text-align: center;
        }
        .sort-controls {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 16px;
        }
        .grid-container {
            display: grid;
            grid-template-columns: 1fr;
            gap: 16px;
        }
        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 16px;
        }
        .form-actions input[type="submit"] {
            background-color: #2563eb;
            color: #ffffff;
            padding: 6px 16px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .form-actions input[type="submit"]:hover {
            background-color: #1d4ed8;
        }
        .form-actions button {
            background-color: #e5e7eb;
            color: #374151;
            padding: 6px 16px;
            border-radius: 6px;
            font-size: 14px;
            text-decoration: none;
            transition: background-color 0.2s;
            border: none;
            cursor: pointer;
        }
        .form-actions button:hover {
            background-color: #d1d5db;
        }
        .text-gray-800 {
            color: #1f2937;
            font-weight: 500;
        }
        @media (min-width: 768px) {
            .grid-container {
                grid-template-columns: 1fr 1fr;
            }
        }
        @media (max-width: 768px) {
            .header {
                flex-direction: column;
                gap: 16px;
                align-items: stretch;
            }
            .search-container {
                max-width: 100%;
            }
            .filter-form {
                flex-direction: column;
                align-items: stretch;
            }
            .filter-form .filter-select,
            .filter-form .btn {
                width: 100%;
                max-width: none;
            }
            .header-actions {
                flex-direction: column;
                align-items: stretch;
            }
            .table-container {
                overflow-x: auto;
            }
            .sort-controls {
                justify-content: center;
            }
        }
    </style>
    <script>
        function toggleCreateForm() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = modal.style.display === "none" ? "flex" : "none";
            if (modal.style.display === "flex") {
                document.getElementById("fullName").focus();
            }
        }
        function closeModal(modalId) {
            var modal = document.getElementById(modalId);
            modal.style.display = "none";
        }
        function openUserDetailModal(userId) {
            const modal = document.getElementById("userDetailModal");
            const modalContent = modal.querySelector(".modal-content");
            modalContent.innerHTML = '<div>Loading...</div>';
            fetch('${pageContext.request.contextPath}/userdetail?id=' + userId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(html => {
                    modalContent.innerHTML = html;
                    modal.style.display = "flex";
                    initializeDepartmentOptions();
                })
                .catch(error => {
                    console.error('Error fetching user details:', error);
                    modalContent.innerHTML = '<div class="error-message">Error loading user details: ' + error.message + '</div>';
                    modal.style.display = "flex";
                });
        }
        function initializeDepartmentOptions() {
            const modalContent = document.querySelector('#userDetailModal .modal-content');
            const roleSelect = modalContent.querySelector("#roleId");
            const departmentSelect = modalContent.querySelector("#departmentId");
            if (!roleSelect || !departmentSelect) {
                console.error("Role or Department select not found");
                return;
            }
            function updateDepartmentOptions() {
                const roleId = roleSelect.value;
                departmentSelect.innerHTML = '<option value="">Select a department</option>';
                if (roleId) {
                    fetch('${pageContext.request.contextPath}/userdetail?action=getDepartmentsByRole&roleId=' + roleId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.error) {
                                departmentSelect.innerHTML = '<option value="">Error: ' + data.error + '</option>';
                                return;
                            }
                            if (data.length === 0) {
                                departmentSelect.innerHTML = '<option value="">No departments available</option>';
                                return;
                            }
                            const userDepartmentId = modalContent.querySelector('input[name="userDepartmentId"]').value;
                            data.forEach(department => {
                                const option = document.createElement("option");
                                option.value = department.departmentId;
                                option.textContent = department.name;
                                if (userDepartmentId && department.departmentId == userDepartmentId) {
                                    option.selected = true;
                                }
                                departmentSelect.appendChild(option);
                            });
                        })
                        .catch(error => {
                            console.error('Error fetching departments:', error);
                            departmentSelect.innerHTML = '<option value="">Error loading departments</option>';
                        });
                }
            }
            updateDepartmentOptions();
            roleSelect.addEventListener("change", updateDepartmentOptions);
        }
        window.onclick = function(event) {
            var createModal = document.getElementById("createUserModal");
            var detailModal = document.getElementById("userDetailModal");
            if (event.target == createModal) {
                createModal.style.display = "none";
            }
            if (event.target == detailModal) {
                detailModal.style.display = "none";
            }
        }
        function toggleSortOrder() {
            var currentSort = '${sortOrder}';
            var newSort = currentSort === 'asc' ? 'desc' : 'asc';
            var url = "${pageContext.request.contextPath}/userlist?page=${currentPage}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&sortOrder=" + newSort;
            window.location.href = url;
        }
        function dismissNotification() {
            window.location.href = "${pageContext.request.contextPath}/userlist?page=${currentPage}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&sortOrder=${sortOrder}";
        }
    </script>
</head>
<body>
    <%@ include file="sidebar.jsp" %>
    <section class="main_content dashboard_part">
        <%@ include file="navbar.jsp" %>
        <div class="container">
            <div class="header">
                <h1 class="title">Users</h1>
                <div class="header-actions">
                    <button class="btn btn-primary" onclick="toggleCreateForm()">Create User</button>
                </div>
            </div>
            <div class="search-container">
                <span class="search-icon">🔍</span>
                <input type="text" name="search" value="${fn:escapeXml(search)}" placeholder="Search by name, email, phone..."
                       class="search-input" form="filterForm">
            </div>
            <form id="filterForm" method="get" action="${pageContext.request.contextPath}/userlist" class="filter-form">
                <input type="hidden" name="sortOrder" value="${sortOrder}">
                <select name="departmentId" class="filter-select">
                    <option value="">All Departments</option>
                    <c:forEach var="department" items="${departments}">
                        <option value="${department.departmentId}" ${department.departmentId == selectedDepartmentId ? 'selected' : ''}>${department.name}</option>
                    </c:forEach>
                </select>
                <select name="roleId" class="filter-select">
                    <option value="">All Roles</option>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.roleId}" ${role.roleId == selectedRoleId ? 'selected' : ''}>${role.name}</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-primary">Filter</button>
            </form>
            <c:if test="${not empty sessionScope.success}">
                <div class="success-message">
                    ${sessionScope.success}
                    <button class="dismiss-btn" onclick="dismissNotification()">×</button>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="error-message">
                    ${sessionScope.error}
                    <button class="dismiss-btn" onclick="dismissNotification()">×</button>
                </div>
                <c:remove var="error" scope="session" />
            </c:if>
            <div class="stats-info">
                Showing ${fn:length(users)} of ${totalUsers}
            </div>
            <div class="sort-controls">
                <button class="btn btn-primary" onclick="toggleSortOrder()">Sort by updated (${sortOrder == 'asc' ? 'Ascending' : 'Descending'})</button>
            </div>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th style="width:60px;">Avatar</th>
                            <th>User ID</th>
                            <th>Email</th>
                            <th>Name</th>
                            <th>Gender</th>
                            <th>Phone</th>
                            <th>Address</th>
                            <th>Department</th>
                            <th>Role</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty users}">
                            <tr>
                                <td colspan="10" class="no-data">
                                    <div class="no-data-message">📭 No users found</div>
                                </td>
                            </tr>
                        </c:if>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td>
                                    <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                                         alt="User Avatar" class="avatar">
                                </td>
                                <td>${user.userId}</td>
                                <td title="${user.email}">${fn:escapeXml(user.email)}</td>
                                <td>
                                    <a href="javascript:void(0)" onclick="openUserDetailModal(${user.userId})" class="category-name" title="${user.fullName}">${fn:escapeXml(user.fullName)}</a>
                                </td>
                                <td>${user.gender ? 'Male' : 'Female'}</td>
                                <td title="${user.phoneNumber}">${fn:escapeXml(user.phoneNumber)}</td>
                                <td title="${user.address}">${fn:escapeXml(user.address)}</td>
                                <td title="${not empty user.departmentName ? user.departmentName : ''}">${not empty user.departmentName ? fn:escapeXml(user.departmentName) : 'None'}</td>
                                <td title="${user.roleName}">${fn:escapeXml(user.roleName)}</td>
                                <td>
                                    <span class="${user.status ? 'status-active' : 'status-inactive'}">${user.status ? 'Active' : 'Inactive'}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&sortOrder=${sortOrder}">Previous</a>
                </c:if>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&sortOrder=${sortOrder}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&sortOrder=${sortOrder}">Next</a>
                </c:if>
            </div>
            <!-- Create User Modal -->
            <div id="createUserModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2 class="modal-title">Create New User</h2>
                        <button class="close-btn" onclick="closeModal('createUserModal')">×</button>
                    </div>
                    <form action="${pageContext.request.contextPath}/userlist" method="post">
                        <input type="hidden" name="action" value="create" />
                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" name="fullName" id="fullName" required />
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" name="email" id="email" required />
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" name="password" id="password" required />
                        </div>
                        <div class="form-group">
                            <label for="gender">Gender</label>
                            <select name="gender" id="gender" required>
                                <option value="1">Male</option>
                                <option value="0">Female</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number</label>
                            <input type="text" name="phoneNumber" id="phoneNumber" />
                        </div>
                        <div class="form-group">
                            <label for="address">Address</label>
                            <input type="text" name="address" id="address" />
                        </div>
                        <div class="form-group">
                            <label for="dateOfBirth">Date of Birth</label>
                            <input type="date" name="dateOfBirth" id="dateOfBirth" />
                        </div>
                        <div class="form-group">
                            <label for="departmentId">Department</label>
                            <select name="departmentId" id="departmentId" required>
                                <c:forEach var="department" items="${departments}">
                                    <option value="${department.departmentId}">${department.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="roleId">Role</label>
                            <select name="roleId" id="roleId" required>
                                <option value="">Select Role</option>
                                <c:forEach var="role" items="${roles}">
                                    <option value="${role.roleId}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <select name="status" id="status" required>
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Create User</button>
                        <button type="button" onclick="closeModal('createUserModal')">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
        <!-- User Detail Modal -->
        <div id="userDetailModal" class="modal">
            <div class="modal-content">
                <!-- Content will be loaded dynamically via JavaScript -->
            </div>
        </div>
    </div>
</body>
</html>