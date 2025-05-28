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
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
    <style>
        .form-container {
            max-width: 600px;
            margin: 24px auto;
            padding: 24px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .form-title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 24px;
        }
        .form-group {
            margin-bottom: 16px;
        }
        .form-label {
            font-weight: 500;
            color: #374151;
            margin-bottom: 8px;
        }
        .form-control:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }
        .btn-space {
            margin-right: 8px;
        }
    </style>
    <link rel="icon" href="img/logo.png" type="image/png">
</head>
<body>
    <div class="form-container">
        <h2 class="form-title">Add New Material</h2>
        <form action="material?action=add" method="post">
            <div class="form-group">
                <label for="categoryId" class="form-label">Category</label>
                <select class="form-select" id="categoryId" name="categoryId" required>
                    <option value="">Select Category</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.categoryId}">${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="unitId" class="form-label">Unit</label>
                <select class="form-select" id="unitId" name="unitId" required>
                    <option value="">Select Unit</option>
                    <c:forEach var="unit" items="${units}">
                        <option value="${unit.unitId}">${unit.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="name" class="form-label">Name</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="unitOfCalculation" class="form-label">Unit of Calculation</label>
                <input type="text" class="form-control" id="unitOfCalculation" name="unitOfCalculation" required>
            </div>
            <div class="form-group">
                <label for="inventoryQuantity" class="form-label">Inventory Quantity</label>
                <input type="number" class="form-control" id="inventoryQuantity" name="inventoryQuantity" min="0" required>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary btn-space">Add Material</button>
                <a href="material?action=list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>
</body>
</html>