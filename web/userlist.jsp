<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <script src="https://cdn.tailwindcss.com"></script>
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
            background-color: white;
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
            color: white;
        }

        .btn-primary:hover {
            background-color: #2563eb;
        }

        .btn-danger {
            background-color: #ef4444;
            color: white;
        }

        .btn-danger:hover {
            background-color: #dc2626;
        }

        .table-container {
            background-color: white;
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

        .action-buttons {
            display: flex;
            gap: 8px;
        }

        .action-buttons .btn {
            padding: 6px 12px;
            font-size: 12px;
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
            color: white;
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
            background-color: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 90%;
            max-width: 500px;
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
            font-size: 24px;
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
        .form-group select {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background-color: white;
            transition: all 0.2s;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .error-message {
            color: #dc2626;
            font-size: 12px;
            margin-bottom: 16px;
        }

        .success-message {
            color: #059669;
            font-size: 12px;
            margin-bottom: 16px;
        }

        .filter-form {
            display: flex;
            gap: 12px;
            margin-bottom: 24px;
            flex-wrap: wrap;
            justify-content: center;
        }

        .filter-form select {
            flex: 1;
            min-width: 150px;
            max-width: 200px;
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

            .filter-form select,
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

            .action-buttons {
                flex-direction: column;
                gap: 4px;
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
                updateGroupOptions();
            }
        }

        function closeModal() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = "none";
        }

        function confirmDelete(userId) {
            if (confirm("Are you sure you want to delete this user?")) {
                window.location.href = "${pageContext.request.contextPath}/userlist?action=delete&id=" + userId
                    + "&page=${currentPage}&search=${fn:escapeXml(search)}&branchId=${selectedBranchId}&roleId=${selectedRoleId}&groupId=${selectedGroupId}&sortOrder=${sortOrder}";
            }
        }

        window.onclick = function(event) {
            var modal = document.getElementById("createUserModal");
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        function updateGroupOptions() {
            var roleId = document.getElementById("roleId").value;
            var groupSelect = document.getElementById("groupId");
            groupSelect.innerHTML = '<option value="">Select Group</option>';

            if (roleId) {
                fetch('${pageContext.request.contextPath}/userlist?action=getGroups&roleId=' + roleId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) {
                            console.error(data.error);
                            return;
                        }
                        data.forEach(group => {
                            var option = document.createElement("option");
                            option.value = group.groupId;
                            option.text = group.name;
                            groupSelect.appendChild(option);
                        });
                    })
                    .catch(error => console.error('Error fetching groups:', error));
            }
        }

        function updateFilterGroupOptions() {
            var roleId = document.getElementById("filterRoleId").value;
            var groupSelect = document.getElementById("filterGroupId");
            groupSelect.innerHTML = '<option value="">All Groups</option>';

            if (roleId) {
                fetch('${pageContext.request.contextPath}/userlist?action=getGroups&roleId=' + roleId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) {
                            console.error(data.error);
                            return;
                        }
                        data.forEach(group => {
                            var option = document.createElement("option");
                            option.value = group.groupId;
                            option.text = group.name;
                            if (group.groupId == '${selectedGroupId}') {
                                option.selected = true;
                            }
                            groupSelect.appendChild(option);
                        });
                    })
                    .catch(error => console.error('Error fetching groups:', error));
            } else {
                <c:forEach var="group" items="${allGroups}">
                    var option = document.createElement("option");
                    option.value = "${group.groupId}";
                    option.text = "${group.name}";
                    if ("${group.groupId}" == "${selectedGroupId}") {
                        option.selected = true;
                    }
                    groupSelect.appendChild(option);
                </c:forEach>
            }
        }

        function toggleSortOrder() {
            var currentSort = '${sortOrder}';
            var newSort = currentSort === 'asc' ? 'desc' : 'asc';
            var url = "${pageContext.request.contextPath}/userlist?page=${currentPage}&search=${fn:escapeXml(search)}&branchId=${selectedBranchId}&roleId=${selectedRoleId}&groupId=${selectedGroupId}&sortOrder=" + newSort;
            window.location.href = url;
        }

        window.onload = function() {
            updateFilterGroupOptions();
            if (document.getElementById("roleId") && document.getElementById("roleId").value) {
                updateGroupOptions();
            }
        };
    </script>
</head>
<body>
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
            <select name="branchId" class="border border-gray-300 rounded-md">
                <option value="">All Branches</option>
                <c:forEach var="branch" items="${branches}">
                    <option value="${branch.branchId}" ${branch.branchId == selectedBranchId ? 'selected' : ''}>${branch.name}</option>
                </c:forEach>
            </select>
            <select name="roleId" id="filterRoleId" class="border border-gray-300 rounded-md" onchange="updateFilterGroupOptions()">
                <option value="">All Roles</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role.roleId}" ${role.roleId == selectedRoleId ? 'selected' : ''}>${role.name}</option>
                </c:forEach>
            </select>
            <select name="groupId" id="filterGroupId" class="border border-gray-300 rounded-md">
                <option value="">All Groups</option>
            </select>
            <button type="submit" class="btn btn-primary">Filter</button>
        </form>

        <c:if test="${not empty success}">
            <div class="success-message">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <div class="stats-info">
            Showing ${fn:length(users)} of ${totalUsers} users
        </div>

        <!-- Sort Controls -->
        <div class="sort-controls">
            <button class="btn btn-primary" onclick="toggleSortOrder()">Sort by updated(${sortOrder == 'asc' ? 'Ascending' : 'Descending'})</button>
        </div>

        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 60px;">Avatar</th>
                        <th>User ID</th>
                        <th>Email</th>
                        <th>Name</th>
                        <th>Gender</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Group(s)</th>
                        <th>Branch</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="12" class="no-data">
                                <div class="no-data-icon">📭</div>
                                No users found
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
                                <a href="${pageContext.request.contextPath}/userdetail?id=${user.userId}" class="category-name" title="${user.fullName}">${fn:escapeXml(user.fullName)}</a>
                            </td>
                            <td>${user.gender == 1 ? 'Male' : 'Female'}</td>
                            <td title="${user.phoneNumber}">${fn:escapeXml(user.phoneNumber)}</td>
                            <td title="${user.address}">${fn:escapeXml(user.address)}</td>
                            <td title="${not empty user.groupNames ? user.groupNames : 'None'}">${not empty user.groupNames ? fn:escapeXml(user.groupNames) : 'None'}</td>
                            <td title="${user.branchName}">${fn:escapeXml(user.branchName)}</td>
                            <td title="${user.roleName}">${fn:escapeXml(user.roleName)}</td>
                            <td>
                                <span class="${user.status ? 'status-active' : 'status-inactive'}">${user.status ? 'Active' : 'Inactive'}</span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button class="btn btn-danger" onclick="confirmDelete(${user.userId})">Delete</button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${fn:escapeXml(search)}&branchId=${selectedBranchId}&roleId=${selectedRoleId}&groupId=${selectedGroupId}&sortOrder=${sortOrder}">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="current">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&branchId=${selectedBranchId}&roleId=${selectedRoleId}&groupId=${selectedGroupId}&sortOrder=${sortOrder}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${fn:escapeXml(search)}&branchId=${selectedBranchId}&roleId=${selectedRoleId}&groupId=${selectedGroupId}&sortOrder=${sortOrder}">Next</a>
            </c:if>
        </div>

        <div id="createUserModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title">Create New User</h2>
                    <button class="close-btn" onclick="closeModal()">×</button>
                </div>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
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
                        <label for="branchId">Branch</label>
                        <select name="branchId" id="branchId" required>
                            <c:forEach var="branch" items="${branches}">
                                <option value="${branch.branchId}">${branch.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="roleId">Role</label>
                        <select name="roleId" id="roleId" required onchange="updateGroupOptions()">
                            <option value="">Select Role</option>
                            <c:forEach var="role" items="${roles}">
                                <option value="${role.roleId}">${role.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="groupId">Group</label>
                        <select name="groupId" id="groupId">
                            <option value="">Select Group</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="status">Status</label>
                        <select name="status" id="status" required>
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Create User</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>