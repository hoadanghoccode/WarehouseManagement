<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Update Category</title>
        <style>
            body {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                background-color: #f8fafc;
                color: #1e293b;
                padding: 40px 0;
            }

            .container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                padding: 32px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            }

            h2 {
                font-size: 24px;
                font-weight: 600;
                margin-bottom: 24px;
                color: #1e40af;
                text-align: center;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                font-size: 14px;
                font-weight: 500;
                color: #374151;
                margin-bottom: 8px;
            }

            input[type="text"],
            select {
                width: 100%;
                padding: 10px 14px;
                border: 1px solid #d1d5db;
                border-radius: 8px;
                font-size: 14px;
                background-color: white;
                transition: border-color 0.2s ease, box-shadow 0.2s ease;
            }

            input[type="text"]:focus,
            select:focus {
                outline: none;
                border-color: #3b82f6;
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
            }

            .btn {
                padding: 10px 20px;
                font-size: 14px;
                font-weight: 500;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                text-decoration: none;
                transition: all 0.2s;
                margin-right: 10px;
            }

            .btn:hover {
                transform: translateY(-1px);
            }

            .btn-primary,
            .btn[type="submit"] {
                background-color: #3b82f6;
                color: white;
            }

            .btn-primary:hover,
            .btn[type="submit"]:hover {
                background-color: #2563eb;
            }

            .btn-cancel {
                background-color: #f3f4f6;
                color: #374151;
                border: 1px solid #d1d5db;
            }

            .btn-cancel:hover {
                background-color: #e5e7eb;
            }

            .error-message {
                background-color: #fee2e2;
                color: #b91c1c;
                padding: 12px 16px;
                border-radius: 6px;
                margin-bottom: 20px;
                font-size: 14px;
                text-align: center;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2>Update Category</h2>

            <c:if test="${not empty sessionScope.error}">
                <script>alert("${sessionScope.error}");</script>
                <c:remove var="error" scope="session"/>
            </c:if>

            <c:if test="${not empty successMessage}">
                <script>
                    alert("'${categoryName}' đã được cập nhật thành công!");
                    window.location.href = "categorylist";
                </script>
            </c:if>

            <form action="updatecategory" method="post">
                <input type="hidden" name="id" value="${currentCategory.categoryId}" />

                <div class="form-group">
                    <label for="name">Category Name:</label>
                    <input type="text" id="name" name="name" value="${currentCategory.name}" required />
                </div>

                <div class="form-group">
                    <label for="parent">Parent Category (optional):</label>
                    <select name="parentId" id="parent">
                        <option value="0">-- None --</option>

                        <c:forEach var="cat" items="${allCategories}">
                            <c:if test="${cat.categoryId != currentCategory.categoryId}">
                                <option value="${cat.categoryId}"
                                        <c:if test="${currentCategory.parentId != null && cat.categoryId == currentCategory.parentId.categoryId}">selected</c:if>>
                                    <c:choose>
                                        <c:when test="${cat.parentId != null}">
                                            ├── ${cat.name}
                                        </c:when>
                                        <c:otherwise>
                                            ${cat.name}
                                        </c:otherwise>
                                    </c:choose>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>

                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Save Category</button>
                    <a href="categorylist" class="btn btn-cancel">Cancel</a>
                </div>
            </form>
        </div>

    </body>
</html>
