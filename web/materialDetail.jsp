<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Material Detail</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .modal {
            display: block;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
        }
        .modal-content {
            background-color: white;
            margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            width: 70%;
            max-width: 500px;
            position: relative;
        }
        .close {
            position: absolute;
            top: 10px;
            right: 15px;
            font-size: 24px;
            cursor: pointer;
            color: #9ca3af;
        }
        .close:hover {
            color: #374151;
        }
        .detail-item {
            margin-bottom: 12px;
        }
        .detail-item strong {
            color: #1e293b;
            margin-right: 8px;
        }
        .btn {
            padding: 8px 16px;
            border-radius: 8px;
            text-decoration: none;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }
        .btn-primary {
            background-color: #6366f1;
        }
        .btn-primary:hover {
            background-color: #4f46e5;
        }
    </style>
</head>
<body>
    <div class="modal">
        <div class="modal-content">
            <span class="close" onclick="window.location.href='list-material'">×</span>
            <h2>Material Details</h2>
            <div class="detail-item"><strong>ID:</strong> ${material.materialId}</div>
            <div class="detail-item"><strong>Name:</strong> ${material.name}</div>
            <div class="detail-item"><strong>Unit:</strong> ${material.unitName != null ? material.unitName : '-'}</div>
            <div class="detail-item"><strong>Price:</strong> ${material.price != 0 ? material.price : '-'}</div>
            <div class="detail-item"><strong>Quantity:</strong> ${material.quantity != 0 ? material.quantity : '-'}</div>
            <div class="detail-item"><strong>Supplier:</strong> ${material.supplierName != null ? material.supplierName : 'No Supplier'}</div>
            <div class="detail-item"><strong>Status:</strong> ${material.status}</div>
            <div class="detail-item"><strong>Category:</strong> ${material.categoryName}</div>
            <div class="detail-item"><strong>Parent Category:</strong> ${material.parentCategoryName != null ? material.parentCategoryName : '-'}</div>
            <a href="list-material" class="btn btn-primary">Close</a>
        </div>
    </div>
</body>
</html>