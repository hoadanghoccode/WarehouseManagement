<%-- 
    Document   : oderlist
    Created on : Jun 13, 2025, 2:08:31 PM
    Author     : duong
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Order List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="css/style1.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
        <style>
            body {
                background-color: #f5f7fa;
                padding: 30px;
                font-family: 'Segoe UI', sans-serif;
            }

            .order-header {
                margin-bottom: 20px;
            }

            .filter-section {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                gap: 12px;
                margin-bottom: 15px;
            }

            .sort-section {
                display: flex;
                align-items: center;
                gap: 12px;
                margin-bottom: 30px;
            }

            .pagination-container {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 6px;
                margin-top: 20px;
            }

            .pagination-container a,
            .pagination-container span {
                padding: 6px 12px;
                border-radius: 6px;
                text-decoration: none;
                border: 1px solid #dee2e6;
                background-color: #fff;
                color: #333;
            }

            .pagination-container .active {
                background-color: #4f46e5;
                color: white;
                font-weight: 500;
                border-color: #4f46e5;
            }

            table th, table td {
                vertical-align: middle;
                text-align: center;
                font-size: 15px;
            }

            table th {
                font-weight: 600;
                border-bottom: 2px solid #e2e8f0;
            }

            table td {
                border-top: none;
            }
            .sort-section {
                display: flex;
                align-items: center;
                gap: 12px;
                margin-bottom: 30px;
                flex-wrap: wrap;
            }

            .sort-section label {
                white-space: nowrap;
                margin-bottom: 0;
                font-weight: 500;
            }

            .sort-section select {
                min-width: 200px;
                max-width: 220px;
            }

            .sort-section button {
                white-space: nowrap;
            }
            .btn-sm {
                padding: 4px 8px;
                font-size: 13px;
            }

            td a.btn {
                margin-right: 4px;
            }
        </style>
    </head>
    <body>
        <%@ include file="sidebar.jsp" %>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="order-header">
                <h3>Order List</h3>
            </div>

            <form method="get" action="orderlist">
                <div class="filter-section">
                    <input type="text" name="search" placeholder="Search orders..." value="${search}" class="form-control" />
                    <select name="type" class="form-select">
                        <option value="">All Types</option>
                        <option value="import" ${type == 'import' ? 'selected' : ''}>Import</option>
                        <option value="export" ${type == 'export' ? 'selected' : ''}>Export</option>
                        <option value="exportToRepair" ${type == 'exportToRepair' ? 'selected' : ''}>Export to Repair</option>
                    </select>
                    <select name="status" class="form-select">
                        <option value="">All Status</option>
                        <option value="pending" ${status == 'pending' ? 'selected' : ''}>Pending</option>
                        <option value="approved" ${status == 'approved' ? 'selected' : ''}>Approved</option>
                    </select>
                    <input type="date" name="fromDate" value="${fromDate}" class="form-control" />
                    <input type="date" name="toDate" value="${toDate}" class="form-control" />
                </div>

                <div class="sort-section">
                    <label for="sort" class="form-label mb-0">Sort by:</label>
                    <select name="sort" id="sort" class="form-select">
                        <option value="Order_id asc" ${sort == 'Order_id asc' ? 'selected' : ''}>ID Asc</option>
                        <option value="Order_id desc" ${sort == 'Order_id desc' ? 'selected' : ''}>ID Desc</option>
                        <option value="UserName asc" ${sort == 'UserName asc' ? 'selected' : ''}>User Asc</option>
                        <option value="UserName desc" ${sort == 'UserName desc' ? 'selected' : ''}>User Desc</option>
                        <option value="SupplierName asc" ${sort == 'SupplierName asc' ? 'selected' : ''}>Supplier Asc</option>
                        <option value="SupplierName desc" ${sort == 'SupplierName desc' ? 'selected' : ''}>Supplier Desc</option>
                        <option value="Type asc" ${sort == 'Type asc' ? 'selected' : ''}>Type Asc</option>
                        <option value="Type desc" ${sort == 'Type desc' ? 'selected' : ''}>Type Desc</option>
                        <option value="Status asc" ${sort == 'Status asc' ? 'selected' : ''}>Status Asc</option>
                        <option value="Status desc" ${sort == 'Status desc' ? 'selected' : ''}>Status Desc</option>
                        <option value="Created_at asc" ${sort == 'Created_at asc' ? 'selected' : ''}>Created At Asc</option>
                        <option value="Created_at desc" ${sort == 'Created_at desc' ? 'selected' : ''}>Created At Desc</option>
                    </select>
                    <button type="submit" class="btn btn-primary">Apply</button>
                    <a href="createorder" class="btn btn-primary">Create</a>
                </div>
            </form>

            <table class="table table-borderless bg-white rounded shadow-sm">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Supplier</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Note</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}" varStatus="loop">
                        <tr>
                            <td>${order.orderId}</td>
                            <td>${order.userName}</td>
                            <td><c:out value="${empty order.supplierName ? 'N/A' : order.supplierName}" /></td>
                            <td>${order.type}</td>
                            <td>
                                <span class="badge bg-${order.status == 'approved' ? 'success' : order.status == 'pending' ? 'warning' : 'secondary'} text-capitalize">${order.status}</span>
                            </td>
                            <td>${order.createdAt}</td>
                            <td>${order.note}</td>
                            <td>
                                <a href="orderdetail?oid=${order.orderId}" class="btn btn-info btn-sm me-1" title="View">
                                    <i class="fas fa-eye"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Pagination -->
            <div class="pagination-container">
                <c:if test="${currentPage > 1}">
                    <a href="?page=1&search=${search}&type=${type}&status=${status}&created=${created}&sort=${sort}"><i class="fas fa-angle-double-left"></i></a>
                    <a href="?page=${currentPage - 1}&search=${search}&type=${type}&status=${status}&created=${created}&sort=${sort}"><i class="fas fa-angle-left"></i></a>
                    </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?page=${i}&search=${search}&type=${type}&status=${status}&created=${created}&sort=${sort}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}&search=${search}&type=${type}&status=${status}&created=${created}&sort=${sort}"><i class="fas fa-angle-right"></i></a>
                    <a href="?page=${totalPages}&search=${search}&type=${type}&status=${status}&created=${created}&sort=${sort}"><i class="fas fa-angle-double-right"></i></a>
                    </c:if>
            </div>

    </body>
</html>
