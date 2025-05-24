<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Material Details</title>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
</head>
<body>
    <div class="container">
        <h1 class="title">Material Details</h1>
        <div class="table-container">
            <table class="table">
                <tbody>
                    <tr>
                        <td><strong>ID:</strong></td>
                        <td>${material.materialId}</td>
                    </tr>
                    <tr>
                        <td><strong>Name:</strong></td>
                        <td>${material.name}</td>
                    </tr>
                    <tr>
                        <td><strong>Unit of Calculation:</strong></td>
                        <td>${material.unitOfCalculation}</td>
                    </tr>
                    <tr>
                        <td><strong>Inventory Quantity:</strong></td>
                        <td>${material.inventoryQuantity}</td>
                    </tr>
                    <tr>
                        <td><strong>Unit:</strong></td>
                        <td>${material.unitName}</td>
                    </tr>
                    <tr>
                        <td><strong>Category:</strong></td>
                        <td>${material.categoryName}</td>
                    </tr>
                    <tr>
                        <td><strong>Parent Category:</strong></td>
                        <td>${material.parentCategoryName != null ? material.parentCategoryName : '-'}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <a href="material?action=list" class="btn btn-outline">Back to Materials List</a>
    </div>
</body>
</html>