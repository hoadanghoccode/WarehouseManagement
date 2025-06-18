<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management - WarehouseHub</title>
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
        .order-card {
            transition: transform 0.3s ease;
        }
        .order-card:hover {
            transform: translateY(-2px);
        }
        .status-badge {
            font-size: 0.75rem;
            padding: 0.5rem 1rem;
            border-radius: 20px;
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
                            <a class="nav-link" href="inventory.jsp">
                                <i class="fas fa-boxes me-2"></i>
                                Inventory
                            </a>
                            <a class="nav-link active" href="orders.jsp">
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
                            <h2 class="mb-1">Order Management</h2>
                            <p class="text-muted mb-0">Track and manage all warehouse orders</p>
                        </div>
                        <div>
                            <button class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#newOrderModal">
                                <i class="fas fa-plus me-1"></i>
                                New Order
                            </button>
                            <button class="btn btn-outline-primary">
                                <i class="fas fa-download me-1"></i>
                                Export
                            </button>
                        </div>
                    </div>

                    <!-- Order Stats -->
                    <div class="row mb-4">
                        <div class="col-md-3 mb-3">
                            <div class="card text-center">
                                <div class="card-body">
                                    <i class="fas fa-clock fa-2x text-warning mb-2"></i>
                                    <h4 class="mb-1">24</h4>
                                    <small class="text-muted">Pending Orders</small>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card text-center">
                                <div class="card-body">
                                    <i class="fas fa-cog fa-2x text-info mb-2"></i>
                                    <h4 class="mb-1">18</h4>
                                    <small class="text-muted">Processing</small>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card text-center">
                                <div class="card-body">
                                    <i class="fas fa-truck fa-2x text-primary mb-2"></i>
                                    <h4 class="mb-1">42</h4>
                                    <small class="text-muted">Shipped</small>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <div class="card text-center">
                                <div class="card-body">
                                    <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
                                    <h4 class="mb-1">156</h4>
                                    <small class="text-muted">Completed</small>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Filters -->
                    <div class="row mb-4">
                        <div class="col-md-8">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Search orders by ID, customer, or product...">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <select class="form-select">
                                <option>All Status</option>
                                <option>Pending</option>
                                <option>Processing</option>
                                <option>Shipped</option>
                                <option>Delivered</option>
                                <option>Cancelled</option>
                            </select>
                        </div>
                    </div>

                    <!-- Orders List -->
                    <div class="row">
                        <!-- Order Card 1 -->
                        <div class="col-lg-6 mb-4">
                            <div class="card order-card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0">#ORD-2024-001</h6>
                                        <small class="text-muted">ABC Electronics</small>
                                    </div>
                                    <span class="badge bg-success status-badge">Shipped</span>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-6">
                                            <small class="text-muted">Items</small>
                                            <div class="fw-bold">15 items</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Total</small>
                                            <div class="fw-bold">$2,450.00</div>
                                        </div>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="col-6">
                                            <small class="text-muted">Order Date</small>
                                            <div>Jan 15, 2024</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Ship Date</small>
                                            <div>Jan 16, 2024</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <div class="btn-group w-100" role="group">
                                        <button class="btn btn-outline-primary btn-sm">
                                            <i class="fas fa-eye me-1"></i>View
                                        </button>
                                        <button class="btn btn-outline-info btn-sm">
                                            <i class="fas fa-edit me-1"></i>Edit
                                        </button>
                                        <button class="btn btn-outline-success btn-sm">
                                            <i class="fas fa-print me-1"></i>Print
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Order Card 2 -->
                        <div class="col-lg-6 mb-4">
                            <div class="card order-card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0">#ORD-2024-002</h6>
                                        <small class="text-muted">Tech Solutions Inc</small>
                                    </div>
                                    <span class="badge bg-warning status-badge">Processing</span>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-6">
                                            <small class="text-muted">Items</small>
                                            <div class="fw-bold">8 items</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Total</small>
                                            <div class="fw-bold">$1,890.00</div>
                                        </div>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="col-6">
                                            <small class="text-muted">Order Date</small>
                                            <div>Jan 15, 2024</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Priority</small>
                                            <div><span class="badge bg-danger">High</span></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <div class="btn-group w-100" role="group">
                                        <button class="btn btn-outline-primary btn-sm">
                                            <i class="fas fa-eye me-1"></i>View
                                        </button>
                                        <button class="btn btn-outline-info btn-sm">
                                            <i class="fas fa-edit me-1"></i>Edit
                                        </button>
                                        <button class="btn btn-outline-success btn-sm">
                                            <i class="fas fa-print me-1"></i>Print
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Order Card 3 -->
                        <div class="col-lg-6 mb-4">
                            <div class="card order-card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0">#ORD-2024-003</h6>
                                        <small class="text-muted">Global Retail Co</small>
                                    </div>
                                    <span class="badge bg-info status-badge">Preparing</span>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-6">
                                            <small class="text-muted">Items</small>
                                            <div class="fw-bold">25 items</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Total</small>
                                            <div class="fw-bold">$3,750.00</div>
                                        </div>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="col-6">
                                            <small class="text-muted">Order Date</small>
                                            <div>Jan 14, 2024</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Progress</small>
                                            <div class="progress" style="height: 6px;">
                                                <div class="progress-bar" style="width: 60%"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <div class="btn-group w-100" role="group">
                                        <button class="btn btn-outline-primary btn-sm">
                                            <i class="fas fa-eye me-1"></i>View
                                        </button>
                                        <button class="btn btn-outline-info btn-sm">
                                            <i class="fas fa-edit me-1"></i>Edit
                                        </button>
                                        <button class="btn btn-outline-success btn-sm">
                                            <i class="fas fa-print me-1"></i>Print
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Order Card 4 -->
                        <div class="col-lg-6 mb-4">
                            <div class="card order-card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-0">#ORD-2024-004</h6>
                                        <small class="text-muted">Metro Supplies</small>
                                    </div>
                                    <span class="badge bg-danger status-badge">Delayed</span>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-6">
                                            <small class="text-muted">Items</small>
                                            <div class="fw-bold">12 items</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Total</small>
                                            <div class="fw-bold">$1,200.00</div>
                                        </div>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="col-6">
                                            <small class="text-muted">Order Date</small>
                                            <div>Jan 14, 2024</div>
                                        </div>
                                        <div class="col-6">
                                            <small class="text-muted">Issue</small>
                                            <div><i class="fas fa-exclamation-triangle text-warning me-1"></i>Stock shortage</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <div class="btn-group w-100" role="group">
                                        <button class="btn btn-outline-primary btn-sm">
                                            <i class="fas fa-eye me-1"></i>View
                                        </button>
                                        <button class="btn btn-outline-info btn-sm">
                                            <i class="fas fa-edit me-1"></i>Edit
                                        </button>
                                        <button class="btn btn-outline-success btn-sm">
                                            <i class="fas fa-print me-1"></i>Print
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Pagination -->
                    <div class="d-flex justify-content-center">
                        <nav>
                            <ul class="pagination">
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

    <!-- New Order Modal -->
    <div class="modal fade" id="newOrderModal" tabindex="-1">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Create New Order</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Customer</label>
                                <select class="form-select">
                                    <option>Select customer</option>
                                    <option>ABC Electronics</option>
                                    <option>Tech Solutions Inc</option>
                                    <option>Global Retail Co</option>
                                    <option>Metro Supplies</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Priority</label>
                                <select class="form-select">
                                    <option>Normal</option>
                                    <option>High</option>
                                    <option>Urgent</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Expected Ship Date</label>
                                <input type="date" class="form-control">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Shipping Method</label>
                                <select class="form-select">
                                    <option>Standard Shipping</option>
                                    <option>Express Shipping</option>
                                    <option>Overnight</option>
                                </select>
                            </div>
                        </div>
                        <hr>
                        <h6>Order Items</h6>
                        <div class="table-responsive">
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>SKU</th>
                                        <th>Quantity</th>
                                        <th>Unit Price</th>
                                        <th>Total</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <select class="form-select form-select-sm">
                                                <option>Select product</option>
                                                <option>Wireless Headphones</option>
                                                <option>Bluetooth Speaker</option>
                                                <option>USB-C Cable</option>
                                            </select>
                                        </td>
                                        <td><input type="text" class="form-control form-control-sm" readonly></td>
                                        <td><input type="number" class="form-control form-control-sm" value="1"></td>
                                        <td><input type="number" class="form-control form-control-sm" step="0.01"></td>
                                        <td><span class="fw-bold">$0.00</span></td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-outline-danger">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <button type="button" class="btn btn-outline-primary btn-sm">
                            <i class="fas fa-plus me-1"></i>Add Item
                        </button>
                        <hr>
                        <div class="row">
                            <div class="col-md-8">
                                <label class="form-label">Notes</label>
                                <textarea class="form-control" rows="3" placeholder="Order notes or special instructions"></textarea>
                            </div>
                            <div class="col-md-4">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <span>Subtotal:</span>
                                            <span>$0.00</span>
                                        </div>
                                        <div class="d-flex justify-content-between">
                                            <span>Tax:</span>
                                            <span>$0.00</span>
                                        </div>
                                        <div class="d-flex justify-content-between">
                                            <span>Shipping:</span>
                                            <span>$0.00</span>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between fw-bold">
                                            <span>Total:</span>
                                            <span>$0.00</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary">Create Order</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Order management functionality
        document.addEventListener('DOMContentLoaded', function() {
            // Status filter functionality
            const statusFilter = document.querySelector('select');
            statusFilter.addEventListener('change', function() {
                // Filter orders by status (implementation would go here)
                console.log('Filtering by status:', this.value);
            });

            // Search functionality
            const searchInput = document.querySelector('input[placeholder*="Search orders"]');
            searchInput.addEventListener('input', function() {
                // Search orders (implementation would go here)
                console.log('Searching for:', this.value);
            });
        });
    </script>
</body>
</html>
