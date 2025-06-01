<%-- 
    Document   : updateMaterial
    Created on : May 21, 2025, 10:02:12 AM
    Author     : legia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update Material</title>
    <!-- Font Awesome cho icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- CSS chính của bạn -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
        /* ========================= Container & Form Styles ========================= */
        .form-container {
            max-width: 600px;
            margin: 24px auto;
            background-color: #ffffff;
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
        .form-container .form-control,
        .form-container .form-select {
            width: 100%;
            padding: 8px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.2s;
            color: #374151;
            background-color: #ffffff;
        }
        .form-container .form-control:focus,
        .form-container .form-select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        /* ============================ Button Styles ============================= */
        .form-container .btn {
            margin-right: 8px;
            padding: 8px 16px;
            border-radius: 8px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            font-size: 14px;
            cursor: pointer;
            border: none;
        }
        /* Nút “Update Material” giữ nguyên màu Primary */
        .btn-primary {
            background-color: #6366f1;
            color: #ffffff;
        }
        .btn-primary:hover {
            background-color: #4f46e5;
        }
        /* Nút “Cancel” đổi sang đen (btn-outline) */
        .btn-outline {
            background-color: transparent;
            border: 1px solid #000000;  /* viền đen */
            color: #000000;              /* chữ đen */
            transition: background-color 0.2s, color 0.2s;
        }
        .btn-outline:hover {
            background-color: #e5e7eb;   /* nền xám nhạt khi hover */
            color: #000000;
        }
        /* ========================================================================= */
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Update Material</h2>
            <form action="update-material" method="post">
                <!-- Hidden để controller biết đây là request update (không phải update-status) -->
                <input type="hidden" name="action" value="update" />
                <!-- Hidden để truyền Material ID -->
                <input type="hidden" name="materialId" value="${material.materialId}" />

                <!-- Category -->
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select class="form-select" id="categoryId" name="categoryId" required>
                        <option value="">Select Category</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}"
                                ${category.categoryId == material.categoryId ? 'selected' : ''}>
                                ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Name -->
                <div class="form-group">
                    <label for="name">Name</label>
                    <input
                        type="text"
                        class="form-control"
                        id="name"
                        name="name"
                        value="${material.name}"
                        required
                    />
                </div>

                <!-- Unit -->
                <div class="form-group">
                    <label for="unitId">Unit</label>
                    <select class="form-select" id="unitId" name="unitId" required>
                        <option value="">Select Unit</option>
                        <c:forEach var="unit" items="${units}">
                            <option value="${unit.unitId}"
                                    ${unit.unitId == material.unitId ? 'selected' : ''}
                                    ${unit.status == 'active' ? '' : 'disabled'}>
                                ${unit.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Price -->
                <div class="form-group">
                    <label for="price">Price</label>
                    <input
                        type="number"
                        step="0.01"
                        class="form-control"
                        id="price"
                        name="price"
                        value="${material.price}"
                        required
                    />
                </div>

                <!-- Quantity -->
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input
                        type="number"
                        step="0.01"
                        class="form-control"
                        id="quantity"
                        name="quantity"
                        value="${material.quantity}"
                        required
                    />
                </div>

                <!-- Supplier -->
                <div class="form-group">
                    <label for="supplierId">Supplier</label>
                    <select class="form-select" id="supplierId" name="supplierId">
                        <option value="0" ${material.supplierName == null ? 'selected' : ''}>No Supplier</option>
                        <c:forEach var="supplier" items="${suppliers}">
                            <option value="${supplier.id}"
                                    ${supplier.name == material.supplierName ? 'selected' : ''}
                                    ${supplier.status == 'active' ? '' : 'disabled'}>
                                ${supplier.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Status -->
                <div class="form-group">
                    <label for="status">Status</label>
                    <select class="form-select" id="status" name="status" required>
                        <option value="active" ${material.status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${material.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>

                <!-- Nút Update và Cancel -->
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Update Material
                </button>
                <a href="list-material" class="btn btn-outline">
                    <i class="fas fa-times"></i> Cancel
                </a>
            </form>
        </div>
    </div>
</body>
</html>
