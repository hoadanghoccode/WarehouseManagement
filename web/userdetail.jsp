<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Users" %>
<%@ page import="dal.UserDAO" %>

<%
    Users user = (Users) request.getAttribute("user");
    List<Integer> branches = new UserDAO().getAllBranchIds();
    request.setAttribute("branches", branches);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Detail</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        form {
            max-width: 600px;
        }
        label {
            display: block;
            margin-top: 12px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="password"],
        input[type="date"],
        select {
            width: 100%;
            padding: 8px;
            margin-top: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            margin-top: 20px;
            padding: 10px 20px;
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

        <input type="submit" value="Update User" />
    </form>

</body>
</html>
