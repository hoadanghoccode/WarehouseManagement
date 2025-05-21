<%-- 
    Document   : addcategory
    Created on : May 20, 2025, 1:15:00 PM
    Author     : ADMIN
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Add New Category</title>
        <style>
            .container {
                width: 50%;
                margin: 0 auto;
                padding: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            label {
                display: block;
                font-weight: bold;
                margin-bottom: 5px;
            }
            input[type="text"], select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }
            .btn {
                padding: 10px 15px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                text-decoration: none;
                display: inline-block;
            }
            .btn-cancel {
                background-color: #f44336;
                margin-left: 10px;
            }
            .error-message {
                color: red;
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Add New Category</h2>

            <!-- Hiển thị lỗi nếu có -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    ${errorMessage}
                </div>
            </c:if>

            <!-- Hiển thị thông báo thành công rồi redirect -->
            <c:if test="${not empty successMessage}">
                <script>
            alert("'${categoryName}' has been added successfully!");
            window.location.href = "categorylist";
                </script>
            </c:if>

            <!-- Form nhập dữ liệu -->
            <c:if test="${empty successMessage}">
                <form action="addcategory" method="post">
                    <div class="form-group">
                        <label for="categoryName">Category Name:</label>
                        <input type="text" id="categoryName" name="categoryName" value="${categoryName}" required>
                    </div>

                    <div class="form-group">
                        <label for="parentId">Parent Category (optional):</label>
                        <select name="parentId" id="parentId">
                            <option value="">-- None --</option>
                            <c:forEach var="cat" items="${allCategories}">
                                <option value="${cat.categoryId}">${cat.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn">Save Category</button>
                        <a href="categorylist" class="btn btn-cancel">Cancel</a>
                    </div>
                </form>
            </c:if>
        </div>
    </body>
</html>
