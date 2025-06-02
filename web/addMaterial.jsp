<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Material</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        * {
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background-color: #f3f4f6;
            margin: 0;
            padding: 0;
            color: #374151;
        }
        .container {
            max-width: 600px;
            margin: 40px auto;
            padding: 24px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 24px;
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
        .form-group input[type="checkbox"] {
            width: auto;
        }
        .unit-section {
            margin-top: 10px;
            padding-left: 20px;
        }
        .unit-section .unit-item {
            display: flex;
            gap: 10px;
            margin-bottom: 10px;
            align-items: center;
        }
        .unit-section input[type="number"] {
            width: 120px;
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
        #newNameSection {
            display: none;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="title">Add Material</h1>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="add-material" method="POST" onsubmit="return validateForm()">
            <!-- Material Selection -->
            <div class="form-group">
                <label for="materialId">Material</label>
                <select id="materialId" name="materialId" onchange="toggleNewNameSection()" required>
                    <option value="">Select Material</option>
                    <option value="0">Add new material</option>
                    <c:forEach var="material" items="${materials}">
                        <option value="${material.materialId}">${material.name}</option>
                    </c:forEach>
                </select>
                <div id="newNameSection">
                    <label for="newName">New Material Name</label>
                    <input type="text" id="newName" name="newName">
                </div>
            </div>

            <!-- Category -->
            <div class="form-group">
                <label for="categoryId">Category</label>
                <select id="categoryId" name="categoryId" required>
                    <option value="">Select Category</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.categoryId}">${category.name}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Units (Multiple selection with price and quantity) -->
            <div class="form-group">
                <label>Units</label>
                <div class="unit-section">
                    <c:forEach var="unit" items="${units}">
                        <div class="unit-item">
                            <input type="checkbox" name="unitIds" value="${unit.unitId}" onchange="toggleUnitInputs(this)">
                            <label>${unit.name}</label>
                            <input type="number" step="0.01" name="price_${unit.unitId}" placeholder="Price" disabled>
                            <input type="number" step="0.01" name="quantity_${unit.unitId}" placeholder="Quantity" disabled>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Supplier -->
            <div class="form-group">
                <label for="supplierId">Supplier</label>
                <select id="supplierId" name="supplierId" required>
                    <option value="">Select Supplier</option>
                    <c:forEach var="supplier" items="${suppliers}">
                        <option value="${supplier.id}">${supplier.name}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Status -->
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status" required>
                    <option value="">Select Status</option>
                    <option value="active">Active</option>
                    <option value="inactive">Inactive</option>
                </select>
            </div>

            <!-- Buttons -->
            <button type="submit" class="btn btn-primary">Add Material</button>
            <a href="list-material" class="btn btn-secondary">Cancel</a>
        </form>
    </div>

    <script>
        function toggleNewNameSection() {
            const materialId = document.getElementById('materialId').value;
            const newNameSection = document.getElementById('newNameSection');
            if (materialId === '0') {
                newNameSection.style.display = 'block';
                document.getElementById('newName').required = true;
            } else {
                newNameSection.style.display = 'none';
                document.getElementById('newName').required = false;
                document.getElementById('newName').value = '';
            }
        }

        function toggleUnitInputs(checkbox) {
            const priceInput = checkbox.nextElementSibling.nextElementSibling;
            const quantityInput = priceInput.nextElementSibling;
            priceInput.disabled = !checkbox.checked;
            quantityInput.disabled = !checkbox.checked;
            priceInput.required = checkbox.checked;
            quantityInput.required = checkbox.checked;
            if (!checkbox.checked) {
                priceInput.value = '';
                quantityInput.value = '';
            }
        }

        function validateForm() {
            const unitCheckboxes = document.querySelectorAll('input[name="unitIds"]');
            let atLeastOneUnitChecked = false;
            for (let checkbox of unitCheckboxes) {
                if (checkbox.checked) {
                    atLeastOneUnitChecked = true;
                    break;
                }
            }
            if (!atLeastOneUnitChecked) {
                alert("At least one unit must be selected.");
                return false;
            }
            return true;
        }
    </script>
</body>
</html>