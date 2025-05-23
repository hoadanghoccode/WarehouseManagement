<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User List</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f5f7fa;
            font-family: Arial, sans-serif;
        }
        .container {
            width: 100%;
            max-width: 1200px;
            height: 100%;
            padding: 0.5rem;
            background-color: white;
            border-radius: 0.5rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 15px;
            border-bottom: 1px solid #e0e0e0;
        }
        .header h1 {
            font-size: 18px;
            font-weight: bold;
            color: #1a1a1a;
            margin: 0;
        }
        .create-btn {
            background-color: #1da1f2;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
        }
        .create-btn:hover {
            background-color: #1a91da;
        }
        .filter-form {
            display: flex;
            gap: 10px;
            padding: 10px 15px;
            border-bottom: 1px solid #e0e0e0;
        }
        .filter-form input[type="text"],
        .filter-form select,
        .filter-form button {
            padding: 6px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 12px;
        }
        .filter-form input[type="text"] {
            width: 200px;
        }
        .filter-form select {
            width: 150px;
        }
        .filter-form button {
            background-color: #1da1f2;
            color: white;
            cursor: pointer;
        }
        .filter-form button:hover {
            background-color: #1a91da;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
            font-size: 12px;
        }
        th {
            background-color: #f5f7fa;
            color: #5f6368;
            font-weight: 600;
            text-transform: uppercase;
            padding: 8px;
            border-bottom: 1px solid #e0e0e0;
            text-align: left;
        }
        td {
            padding: 8px;
            border-bottom: 1px solid #e0e0e0;
            text-align: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .avatar {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            object-fit: cover;
            vertical-align: middle;
        }
        .delete-btn {
            background-color: #e53935;
            color: white;
            border: none;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        .delete-btn:hover {
            background-color: #d32f2f;
        }
        .status-inactive {
            color: white;
            background-color: #e53935;
            padding: 2px 6px;
            border-radius: 4px;
        }
        .status-active {
            color: white;
            background-color: #43a047;
            padding: 2px 6px;
            border-radius: 4px;
        }
        .pagination {
            display: flex;
            justify-content: center;
            padding: 10px;
            gap: 5px;
        }
        .pagination a {
            padding: 6px 10px;
            background-color: #e0e0e0;
            color: #1a1a1a;
            text-decoration: none;
            border-radius: 4px;
        }
        .pagination a:hover {
            background-color: #ccc;
        }
        .pagination a.active {
            background-color: #1da1f2;
            color: white;
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
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 90%;
            max-width: 400px;
            max-height: 80vh;
            overflow-y: auto;
        }
        .form-group {
            margin-bottom: 10px;
        }
        .form-group label {
            display: block;
            margin-bottom: 3px;
            font-weight: 500;
            color: #333;
            font-size: 12px;
        }
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 12px;
        }
        .close-btn {
            float: right;
            font-size: 20px;
            border: none;
            background: none;
            cursor: pointer;
        }
        .error-message {
            color: #e53935;
            font-size: 12px;
            margin-bottom: 10px;
        }
    </style>
    <script>
        function toggleCreateForm() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = modal.style.display === "none" ? "flex" : "none";
            if (modal.style.display === "flex") {
                document.getElementById("fullName").focus();
                updateGroupOptions(); // Initialize group options
            }
        }
        function closeModal() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = "none";
        }
        function confirmDelete(userId) {
            if (confirm("Are you sure you want to delete this user?")) {
                window.location.href = "${pageContext.request.contextPath}/userlist?action=delete&id=" + userId;
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
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Users</h1>
            <button class="create-btn" onclick="toggleCreateForm()">Create User</button>
        </div>
        <form method="get" action="${pageContext.request.contextPath}/userlist" class="filter-form">
            <input type="text" name="search" value="${search}" placeholder="Search by name, email, phone, ..."
                   class="border border-gray-300 rounded-md p-1 w-full sm:w-1/3 focus:outline-none focus:ring-1 focus:ring-blue-500 text-xs">
            <select name="branchId" class="border border-gray-300 rounded-md p-1 w-full sm:w-1/4 focus:outline-none focus:ring-1 focus:ring-blue-500 text-xs">
                <option value="">All Branches</option>
                <c:forEach var="branch" items="${branches}">
                    <option value="${branch.branchId}" ${branch.branchId == selectedBranchId ? 'selected' : ''}>${branch.name}</option>
                </c:forEach>
            </select>
            <select name="roleId" class="border border-gray-300 rounded-md p-1 w-full sm:w-1/4 focus:outline-none focus:ring-1 focus:ring-blue-500 text-xs">
                <option value="">All Roles</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role.roleId}" ${role.roleId == selectedRoleId ? 'selected' : ''}>${role.name}</option>
                </c:forEach>
            </select>
            <button type="submit" class="bg-blue-600 text-white px-2 py-1 rounded-md hover:bg-blue-700 w-full sm:w-auto text-xs">Search</button>
        </form>
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        <div class="flex-1">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="w-6"></th>
                        <th class="w-8">User ID</th>
                        <th class="w-12">Email</th>
                        <th class="w-12">Name</th>
                        <th class="w-8">Gender</th>
                        <th class="w-12">Phone</th>
                        <th class="w-12">Address</th>
                        <th class="w-12">Group(s)</th>
                        <th class="w-12">Branch</th>
                        <th class="w-12">Role</th>
                        <th class="w-8">Status</th>
                        <th class="w-12"></th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td class="px-2 py-1 whitespace-nowrap">
                                <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                                     alt="User Avatar" class="avatar">
                            </td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs">${user.userId}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${user.email}">${user.email}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs">
                                <a href="${pageContext.request.contextPath}/userdetail?id=${user.userId}" class="text-blue-600 hover:underline" title="${user.fullName}">${user.fullName}</a>
                            </td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs">${user.gender == 1 ? 'Male' : 'Female'}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${user.phoneNumber}">${user.phoneNumber}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${user.address}">${user.address}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${not empty user.groupNames ? user.groupNames : 'None'}">${not empty user.groupNames ? user.groupNames : 'None'}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${user.branchName}">${user.branchName}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs" title="${user.roleName}">${user.roleName}</td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs"><span class="${user.status ? 'status-active' : 'status-inactive'}">${user.status ? 'Active' : 'Inactive'}</span></td>
                            <td class="px-2 py-1 whitespace-nowrap text-xs">
                                <button class="delete-btn" onclick="confirmDelete(${user.userId})">
                                    <span role="img" aria-label="Delete">🗑️</span>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination mt-2 flex justify-center">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${search}&branchId=${selectedBranchId}&roleId=${selectedRoleId}"
                   class="px-1 py-0.5 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 text-xs">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${search}&branchId=${selectedBranchId}&roleId=${selectedRoleId}"
                   class="px-1 py-0.5 ${i == currentPage ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'} rounded-md hover:bg-gray-300 text-xs mx-0.5">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${search}&branchId=${selectedBranchId}&roleId=${selectedRoleId}"
                   class="px-1 py-0.5 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 text-xs">Next</a>
            </c:if>
        </div>
        <div id="createUserModal" class="modal">
            <div class="modal-content">
                <button class="close-btn" onclick="closeModal()">×</button>
                <h2 class="text-md font-bold mb-3">Create New User</h2>
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
                    <button type="submit" class="bg-blue-600 text-white px-3 py-1.5 rounded-md hover:bg-blue-700 text-sm">Create User</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>