<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>User Detail</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 40px;
                background-color: #f5f7fa;
            }
            h2 {
                font-size: 24px;
                color: #333;
                margin-bottom: 20px;
            }
            form {
                max-width: 600px;
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }
            label {
                display: block;
                margin-top: 12px;
                font-weight: bold;
                color: #5f6368;
            }
            p {
                margin: 4px 0 12px;
                color: #202124;
            }
            input[type="text"], input[type="password"], input[type="date"], select {
                width: 100%;
                padding: 8px;
                margin-top: 4px;
                border: 1px solid #dadce0;
                border-radius: 5px;
                box-sizing: border-box;
                font-size: 14px;
            }
            input[type="submit"] {
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #1a73e8;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
            }
            input[type="submit"]:hover {
                background-color: #1557b0;
            }
        </style>
    </head>
    <body>
        <h2>User Details</h2>

        <form action="userdetail" method="post">
            <input type="hidden" name="userId" value="${user.userId}" />

            <label>Email</label>
            <p><strong>${user.email}</strong> (Cannot edit)</p>

            <label for="fullName">Full Name</label>
            <input type="text" name="fullName" id="fullName" value="${user.fullName}" required />

            <label for="password">Password</label>
            <input type="password" name="password" id="password" value="${user.password}" required />

            <label for="gender">Gender</label>
            <select name="gender" id="gender">
                <option value="1" ${user.gender == 1 ? 'selected' : ''}>Male</option>
                <option value="0" ${user.gender == 0 ? 'selected' : ''}>Female</option>
            </select>

            <label for="phoneNumber">Phone Number</label>
            <input type="text" name="phoneNumber" id="phoneNumber" value="${user.phoneNumber}" />

            <label for="address">Address</label>
            <input type="text" name="address" id="address" value="${user.address}" />

            <label for="dob">Date of Birth</label>
            <input type="date" name="dob" id="dob" value="${user.dateOfBirth}" />

            <label for="branchId">Branch</label>
            <select name="branchId" id="branchId">
                <c:forEach var="branch" items="${branches}">
                    <option value="${branch}" ${branch == user.branchId ? 'selected' : ''}>Branch ${branch}</option>
                </c:forEach>
            </select>

            <label for="roleId">Role</label>
            <select name="roleId" id="roleId">
                <c:forEach var="role" items="${roles}">
                    <option value="${role.roleId}" ${role.roleId == user.roleId ? 'selected' : ''}>${role.name}</option>
                </c:forEach>
            </select>

            <label for="image">Image URL</label>
            <input type="text" name="image" id="image" value="${user.image}" />

            <label for="status">Status</label>
            <select name="status" id="status">
                <option value="true" ${user.status ? 'selected' : ''}>Active</option>
                <option value="false" ${!user.status ? 'selected' : ''}>Inactive</option>
            </select>

            <label>Updated At</label>
            <p><strong>${user.updatedAt}</strong> (Read-only)</p>

            <input type="submit" value="Update User" />
        </form>
    </body>
</html>