<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List</title>
    </head>
    <body>
        <h1>User List</h1>
        <form method="get" action="UserListServlet">
            <input type="text" name="search" placeholder="Search by name or email" value="${searchQuery}">
            <button type="submit">Search</button>
        </form>
        <table border="1">
            <tr>
                <th>User ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Gender</th>
                <th>Phone Number</th>
                <th>Address</th>
                <th>Date of Birth</th>
                <th>Branch ID</th>
            </tr>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.userId}</td>
                    <td><a href="userdetail?id=${user.userId}">${user.fullName}</a></td>
                    <td>${user.email}</td>
                    <td>${user.gender}</td>
                    <td>${user.phoneNumber}</td>
                    <td>${user.address}</td>
                    <td>${user.dateOfBirth}</td>
                    <td>${user.branchId}</td>
                </tr>
            </c:forEach>
        </table>
        <div>
            <c:if test="${currentPage > 1}">
                <a href="UserListServlet?page=${currentPage - 1}&search=${searchQuery}">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="UserListServlet?page=${i}&search=${searchQuery}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="UserListServlet?page=${currentPage + 1}&search=${searchQuery}">Next</a>
            </c:if>
        </div>
    </body>
</html>