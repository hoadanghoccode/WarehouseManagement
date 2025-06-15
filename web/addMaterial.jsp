<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>
<%@ page import="model.Supplier" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Material</title>
    <!-- Bootstrap CSS (v5.3) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <!-- menu css -->
    <link rel="stylesheet" href="css/metisMenu.css" />
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .main-layout {
            display: flex;
            min-height: 100vh;
        }

        .main-content {
            flex: 1;
            margin-left: 260px; /* Width of sidebar */
            padding: 0;
            background-color: #f8f9fa;
            min-height: 100vh;
            transition: margin-left 0.3s ease;
        }

        .container {
            max-width: none;
            padding: 30px;
            margin: 0;
        }

        .title {
            font-size: 28px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 8px;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            color: #374151;
            background-color: #fff;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
            font-size: 14px;
            border: none;
            transition: background-color 0.2s;
        }

        .btn-primary {
            background-color: #6366f1;
        }

        .btn-primary:hover {
            background-color: #4f46e5;
        }

        .btn-secondary {
            background-color: #6b7280;
        }

        .btn-secondary:hover {
            background-color: #4b5563;
        }

        .error {
            color: #ef4444;
            font-size: 14px;
            margin-bottom: 16px;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
            }
            .title {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <div class="main-layout">
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
            <div class="container">
                <h1 class="title">Add Material</h1>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <form action="add-material" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="categoryId">Category</label>
                        <select id="categoryId" name="categoryId" class="form-control" required>
                            <option value="">Select Category</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}">${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="supplierId">Supplier</label>
                        <select id="supplierId" name="supplierId" class="form-control" required>
                            <option value="">Select Supplier</option>
                            <c:forEach items="${suppliers}" var="supplier">
                                <option value="${supplier.supplierId}">${supplier.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="imageFile">Upload Image</label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/*" class="form-control" required>
                    </div>
                    <div class="header-actions">
                        <button type="submit" class="btn btn-primary">Add Material</button>
                        <a href="list-material" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>