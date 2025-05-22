<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f7fa;
        }
        h1 {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .create-user-btn {
            background-color: #1a73e8;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .create-user-btn:hover {
            background-color: #1557b0;
        }
        .search-bar {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .search-bar input {
            padding: 8px;
            width: 200px;
            border: 1px solid #dadce0;
            border-radius: 5px;
            font-size: 14px;
        }
        .search-bar select {
            padding: 8px;
            border: 1px solid #dadce0;
            border-radius: 5px;
            font-size: 14px;
        }
        .search-bar button {
            padding: 8px 16px;
            background-color: #1a73e8;
            color: white;
            border: none;
            border-radius: 5px;
            margin-left: 10px;
            cursor: pointer;
        }
        .search-bar button:hover {
            background-color: #1557b0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 12px 16px;
            text-align: left;
            font-size: 14px;
        }
        th {
            background-color: #f8f9fa;
            color: #5f6368;
            font-weight: 500;
            text-transform: uppercase;
        }
        td {
            color: #202124;
            border-bottom: 1px solid #dadce0;
        }
        td a {
            color: #1a73e8;
            text-decoration: none;
        }
        td a:hover {
            text-decoration: underline;
        }
        .delete-btn {
            background-color: #d32f2f;
            color: white;
            border: none;
            cursor: pointer;
            padding: 6px 10px;
            border-radius: 4px;
            font-size: 16px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        .delete-btn:hover {
            background-color: #b71c1c;
        }
        .pagination {
            margin-top: 20px;
            text-align: center;
        }
        .pagination a {
            margin: 0 5px;
            padding: 8px 12px;
            color: #1a73e8;
            text-decoration: none;
            border: 1px solid #dadce0;
            border-radius: 5px;
        }
        .pagination a:hover {
            background-color: #f1f3f4;
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
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            width: 600px;
            max-height: 80vh;
            overflow-y: auto;
        }
        .modal-content label {
            display: block;
            margin-top: 12px;
            font-weight: bold;
            color: #5f6368;
        }
        .modal-content input, .modal-content select {
            width: 100%;
            padding: 8px;
            margin-top: 4px;
            border: 1px solid #dadce0;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 14px;
        }
        .modal-content input[type="submit"] {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #1a73e8;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .modal-content input[type="submit"]:hover {
            background-color: #1557b0;
        }
        .close-btn {
            float: right;
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
            color: #5f6368;
        }
        .close-btn:hover {
            color: #202124;
        }
        .avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            object-fit: cover;
            vertical-align: middle;
        }
    </style>
    <script>
        function toggleCreateForm() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = modal.style.display === "none" ? "flex" : "none";
        }

        function closeModal() {
            var modal = document.getElementById("createUserModal");
            modal.style.display = "none";
        }

        function confirmDelete(userId) {
            if (confirm("Are you sure you want to delete the user?")) {
                var form = document.createElement("form");
                form.method = "post";
                form.action = "${pageContext.request.contextPath}/userlist";
                var input = document.createElement("input");
                input.type = "hidden";
                input.name = "action";
                input.value = "delete";
                form.appendChild(input);
                var userIdInput = document.createElement("input");
                userIdInput.type = "hidden";
                userIdInput.name = "userId";
                userIdInput.value = userId;
                form.appendChild(userIdInput);
                document.body.appendChild(form);
                form.submit();
            }
        }

        // Close modal when clicking outside
        window.onclick = function(event) {
            var modal = document.getElementById("createUserModal");
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    </script>
</head>
<body>
    <div class="header">
        <h1>Users</h1>
        <button class="create-user-btn" onclick="toggleCreateForm()">Create User</button>
    </div>

    <div class="search-bar">
        <form method="get" action="${pageContext.request.contextPath}/userlist">
            <input type="text" name="search" placeholder="Search by name, email, phone, or address" value="${searchQuery}">
            <select name="branchId">
                <option value="">All Branches</option>
                <c:forEach var="branch" items="${branches}">
                    <option value="${branch.branchId}" ${branch.branchId == selectedBranchId ? 'selected' : ''}>${branch.name}</option>
                </c:forEach>
            </select>
            <select name="roleId">
                <option value="">All Roles</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role.roleId}" ${role.roleId == selectedRoleId ? 'selected' : ''}>${role.name}</option>
                </c:forEach>
            </select>
            <button type="submit">Search</button>
        </form>
    </div>

    <table>
        <tr>
            <th>Avatar</th>
            <th>User ID</th>
            <th>Email</th>
            <th>Name</th>
            <th>Gender</th>
            <th>Phone Number</th>
            <th>Address</th>
            <th>Date of Birth</th>
            <th>Branch</th>
            <th>Role</th>
            <th>Updated At</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>
                    <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                         alt="User Avatar" class="avatar">
                </td>
                <td>${user.userId}</td>
                <td>${user.email}</td>
                <td><a href="${pageContext.request.contextPath}/userdetail?id=${user.userId}">${user.fullName}</a></td>
                <td>${user.gender == 1 ? 'Male' : 'Female'}</td>
                <td>${user.phoneNumber}</td>
                <td>${user.address}</td>
                <td>${user.dateOfBirth}</td>
                <td>${user.branchName}</td>
                <td>${user.roleName}</td>
                <td>${user.updatedAt}</td>
                <td>${user.status ? 'Active' : 'Inactive'}</td>
                <td>
                    <button class="delete-btn" onclick="confirmDelete(${user.userId})">
                        <span role="img" aria-label="Delete">🗑️</span>
                    </button>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${searchQuery}&branchId=${selectedBranchId}&roleId=${selectedRoleId}">Previous</a>
        </c:if>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${searchQuery}&branchId=${selectedBranchId}&roleId=${selectedRoleId}">${i}</a>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${searchQuery}&branchId=${selectedBranchId}&roleId=${selectedRoleId}">Next</a>
        </c:if>
    </div>

    <div id="createUserModal" class="modal">
        <div class="modal-content">
            <button class="close-btn" onclick="closeModal()">×</button>
            <form action="${pageContext.request.contextPath}/userlist" method="post">
                <input type="hidden" name="action" value="create" />
                <h2>Create New User</h2>
                <label for="fullName">Full Name</label>
                <input type="text" name="fullName" id="fullName" required />
                <label for="email">Email</label>
                <input type="email" name="email" id="email" required />
                <label for="password">Password</label>
                <input type="password" name="password" id="password" required />
                <label for="gender">Gender</label>
                <select name="gender" id="gender" required>
                    <option value="1">Male</option>
                    <option value="0">Female</option>
                </select>
                <label for="phoneNumber">Phone Number</label>
                <input type="text" name="phoneNumber" id="phoneNumber" />
                <label for="address">Address</label>
                <input type="text" name="address" id="address" />
                <label for="dob">Date of Birth</label>
                <input type="date" name="dob" id="dob" required />
                <label for="branchId">Branch</label>
                <select name="branchId" id="branchId" required>
                    <c:forEach var="branch" items="${branches}">
                        <option value="${branch.branchId}">${branch.name}</option>
                    </c:forEach>
                </select>
                <label for="roleId">Role</label>
                <select name="roleId" id="roleId" required>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.roleId}">${role.name}</option>
                    </c:forEach>
                </select>
                <label for="status">Status</label>
                <select name="status" id="status" required>
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                </select>
                <input type="submit" value="Create User" />
            </form>
        </div>
    </div>
</body>
</html>