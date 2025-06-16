<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory Management - WarehouseHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 12px 20px;
            border-radius: 8px;
            margin: 2px 0;
            transition: all 0.3s ease;
        }
        .sidebar .nav-link:hover,
        .sidebar .nav-link.active {
            background: rgba(255, 255, 255, 0.1);
            color: white;
            transform: translateX(5px);
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .table-container {
            background: white;
            border-radius: 15px;
            overflow: hidden;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 25px;
        }
        .search-box {
            border-radius: 25px;
            border: 2px solid #e9ecef;
            padding: 10px 20px;
        }
        .search-box:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">
                <i class="fas fa-warehouse me-2"></i>
                WarehouseHub
            </a>
            <div class="navbar-nav ms-auto">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-circle me-1"></i>
                        John Manager
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Settings</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0">
                <div class="sidebar">
                    <div class="p-3">
                        <h6 class="text-white-50 text-uppercase mb-3">Navigation</h6>
                        <nav class="nav flex-column">
                            <a class="nav-link" href="index.jsp">
                                <i class="fas fa-tachometer-alt me-2"></i>
                                Dashboard
                            </a>
                            <a class="nav-link active" href="inventory.jsp">
                                <i class="fas fa-boxes me-2"></i>
                                Inventory
                            </a>
                            <a class="nav-link" href="orders.jsp">
                                <i class="fas fa-shopping-cart me-2"></i>
                                Orders
                            </a>
                            <a class="nav-link" href="#receiving">
                                <i class="fas fa-truck-loading me-2"></i>
                                Receiving
                            </a>
                            <a class="nav-link" href="#shipping">
                                <i class="fas fa-shipping-fast me-2"></i>
                                Shipping
                            </a>
                            <a class="nav-link" href="#reports">
                                <i class="fas fa-chart-bar me-2"></i>
                                Reports
                            </a>
                            <a class="nav-link" href="#settings">
                                <i class="fas fa-cog me-2"></i>
                                Settings
                            </a>
                        </nav>
                    </div>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10">
                <div class="p-4">
                    <!-- Header -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h2 class="mb-1">Inventory Management</h2>
                            <p class="text-muted mb-0">Manage your warehouse inventory and stock levels</p>
                        </div>
                        <div>
                            <button class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#addItemModal">
                                <i class="fas fa-plus me-1"></i>
                                Add Item
                            </button>
                            <button class="btn btn-outline-primary">
                                <i class="fas fa-download me-1"></i>
                                Export
                            </button>
                        </div>
                    </div>

                    <!-- Search and Filters -->
                    <div class="row mb-4">
                        <div class="col-md-6">
                            <div class="input-group">
                                <input type="text" class="form-control search-box" placeholder="Search inventory...">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="d-flex gap-2">
                                <select class="form-select">
                                    <option>All Categories</option>
                                    <option>Electronics</option>
                                    <option>Accessories</option>
                                    <option>Components</option>
                                </select>
                                <select class="form-select">
                                    <option>All Locations</option>
                                    <option>Zone A</option>
                                    <option>Zone B</option>
                                    <option>Zone C</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Inventory Table -->
                    <div class="card table-container">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>
                                                <input type="checkbox" class="form-check-input">
                                            </th>
                                            <th>SKU</th>
                                            <th>Product Name</th>
                                            <th>Category</th>
                                            <th>Stock Level</th>
                                            <th>Min. Stock</th>
                                            <th>Location</th>
                                            <th>Unit Price</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><input type="checkbox" class="form-check-input"></td>
                                            <td><strong>SKU-001</strong></td>
                                            <td>Wireless Headphones</td>
                                            <td>Electronics</td>
                                            <td><span class="badge bg-danger">5</span></td>
                                            <td>20</td>
                                            <td>A-1-15</td>
                                            <td>$89.99</td>
                                            <td><span class="badge bg-danger">Low Stock</span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success" title="Restock">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-info" title="Move">
                                                        <i class="fas fa-arrows-alt"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" class="form-check-input"></td>
                                            <td><strong>SKU-002</strong></td>
                                            <td>Bluetooth Speaker</td>
                                            <td>Electronics</td>
                                            <td><span class="badge bg-success">45</span></td>
                                            <td>15</td>
                                            <td>A-2-08</td>
                                            <td>$129.99</td>
                                            <td><span class="badge bg-success">In Stock</span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success" title="Restock">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-info" title="Move">
                                                        <i class="fas fa-arrows-alt"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" class="form-check-input"></td>
                                            <td><strong>SKU-003</strong></td>
                                            <td>USB-C Cable</td>
                                            <td>Accessories</td>
                                            <td><span class="badge bg-warning">12</span></td>
                                            <td>50</td>
                                            <td>B-1-22</td>
                                            <td>$19.99</td>
                                            <td><span class="badge bg-warning">Low Stock</span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success" title="Restock">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-info" title="Move">
                                                        <i class="fas fa-arrows-alt"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" class="form-check-input"></td>
                                            <td><strong>SKU-004</strong></td>
                                            <td>Laptop Stand</td>
                                            <td>Accessories</td>
                                            <td><span class="badge bg-success">78</span></td>
                                            <td>25</td>
                                            <td>C-1-05</td>
                                            <td>$49.99</td>
                                            <td><span class="badge bg-success">In Stock</span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success" title="Restock">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-info" title="Move">
                                                        <i class="fas fa-arrows-alt"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" class="form-check-input"></td>
                                            <td><strong>SKU-005</strong></td>
                                            <td>Wireless Mouse</td>
                                            <td>Electronics</td>
                                            <td><span class="badge bg-danger">0</span></td>
                                            <td>30</td>
                                            <td>A-3-12</td>
                                            <td>$39.99</td>
                                            <td><span class="badge bg-danger">Out of Stock</span></td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <button class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-success" title="Restock">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-info" title="Move">
                                                        <i class="fas fa-arrows-alt"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="card-footer bg-white">
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="text-muted">Showing 1-5 of 247 items</span>
                                <nav>
                                    <ul class="pagination pagination-sm mb-0">
                                        <li class="page-item disabled">
                                            <a class="page-link" href="#">Previous</a>
                                        </li>
                                        <li class="page-item active">
                                            <a class="page-link" href="#">1</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">2</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">3</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#">Next</a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Item Modal -->
    <div class="modal fade" id="addItemModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">SKU</label>
                                <input type="text" class="form-control" placeholder="Enter SKU">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Product Name</label>
                                <input type="text" class="form-control" placeholder="Enter product name">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Category</label>
                                <select class="form-select">
                                    <option>Select category</option>
                                    <option>Electronics</option>
                                    <option>Accessories</option>
                                    <option>Components</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Location</label>
                                <input type="text" class="form-control" placeholder="e.g., A-1-15">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label class="form-label">Initial Stock</label>
                                <input type="number" class="form-control" placeholder="0">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label class="form-label">Minimum Stock</label>
                                <input type="number" class="form-control" placeholder="0">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label class="form-label">Unit Price</label>
                                <input type="number" class="form-control" placeholder="0.00" step="0.01">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" rows="3" placeholder="Enter product description"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary">Add Item</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
