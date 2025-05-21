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
            .filter-btn {
                background-color: #f1f3f4;
                border: 1px solid #dadce0;
                padding: 8px 16px;
                border-radius: 5px;
                cursor: pointer;
                display: flex;
                align-items: center;
                font-size: 14px;
            }
            .search-bar {
                margin-bottom: 20px;
            }
            .search-bar input {
                padding: 8px;
                width: 300px;
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
            .create-form {
                max-width: 600px;
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
                margin-top: 20px;
                display: none;
            }
            .create-form label {
                display: block;
                margin-top: 12px;
                font-weight: bold;
                color: #5f6368;
            }
            .create-form input, .create-form select {
                width: 100%;
                padding: 8px;
                margin-top: 4px;
                border: 1px solid #dadce0;
                border-radius: 5px;
                box-sizing: border-box;
                font-size: 14px;
            }
            .create-form input[type="submit"] {
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #1a73e8;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .create-form input[type="submit"]:hover {
                background-color: #1557b0;
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
                var form = document.getElementById("createUserForm");
                form.style.display = form.style.display === "none" ? "block" : "none";
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
                <button type="submit">Search</button>
            </form>
            <button class="filter-btn">Filters</button>
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
                <th>Branch ID</th>
                <th>Role</th>
                <th>Updated At</th>
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
                    <td>${user.branchId}</td>
                    <td>${user.roleName}</td>
                    <td>${user.updatedAt}</td>
                </tr>
            </c:forEach>
        </table>

        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${searchQuery}">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${searchQuery}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${searchQuery}">Next</a>
            </c:if>
        </div>

        <div class="create-form" id="createUserForm">
            <form action="${pageContext.request.contextPath}/userlist" method="post">
                <input type="hidden" name="action" value="create" />
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
                        <option value="${branch}">Branch ${branch}</option>
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
    </body>
</html>