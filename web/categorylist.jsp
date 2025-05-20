<%-- 
    Document   : categorylist
    Created on : May 20, 2025, 12:43:14 PM
    Author     : ADMIN
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Category List</title>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 10px;
            }
        </style>
    </head>
    <body>
        <h2>Category List</h2>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <form action="categorylist" method="get" style="display: inline-block;">
                <input type="text" name="search" value="${search}" placeholder="Search by name..." />
                <input type="submit" value="Search" />
            </form>
            <a href="addcategory" style="display: inline-block; padding: 8px 15px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px;">Add New Category</a>
        </div>

        <br/>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="category" items="${categoryList}">
                    <tr>
                        <td>${category.categoryId}</td>
                        <td>${category.name}</td>
                        <td>
                            <a href="categorydetail?cid=${category.categoryId}">View Detail</a> |
                            <a href="deletecategory?cid=${category.categoryId}" onclick="return confirm('Are you sure to delete?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
