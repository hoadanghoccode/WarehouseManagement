<%-- 
    Document   : addMaterial
    Created on : May 21, 2025, 10:01:57 AM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Material</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
    <style>
        .form-container {
            max-width: 600px;
            margin: 24px auto;
            background-color: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .form-container h2 {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 24px;
        }
        .form-container .form-group {
            margin-bottom: 16px;
        }
        .form-container label {
            font-weight: 500;
            color: #374151;
            margin-bottom: 8px;
            display: block;
        }
        .form-container .form-control, .form-container .form-select {
            width: 100%;
            padding: 8px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.2s;
        }
        .form-container .form-control:focus, .form-container .form-select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }
        .form-container .btn {
            margin-right: 8px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Add New Material</h2>
            <form action="material?action=add" method="post">
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select class="form-select" id="categoryId" name="categoryId" required>
                        <option value="">Select Category</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="unitId">Unit</label>
                    <select class="form-select" id="unitId" name="unitId" required>
                        <option value="">Select Unit</option>
                        <c:forEach var="unit" items="${units}">
                            <option value="${unit.unitId}" ${unit.status == 'active' ? '' : 'disabled'}>${unit.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" step="0.01" class="form-control" id="price" name="price" required>
                </div>
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input type="number" step="0.01" class="form-control" id="quantity" name="quantity" required>
                </div>
                <div class="form-group">
                    <label for="supplierId">Supplier</label>
                    <select class="form-select" id="supplierId" name="supplierId">
                        <option value="0">No Supplier</option>
                        <c:forEach var="supplier" items="${suppliers}">
                            <option value="${supplier.id}" ${supplier.status == 'active' ? '' : 'disabled'}>${supplier.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Add Material</button>
                <a href="material?action=list" class="btn btn-outline">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>