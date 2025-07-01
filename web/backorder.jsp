<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BackOrder</title>
    <link rel="icon" href="img/logo.png" type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap1.min.css" />
    <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" href="css/metisMenu.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />
    <style>
        body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .main-layout { display: flex; min-height: 100vh; }
        .main-content { flex: 1; margin-left: 260px; padding: 0; background-color: #f8f9fa; min-height: 100vh; transition: margin-left 0.3s ease; }
        .container { max-width: none; padding: 30px; margin: 0; }
        .title { font-size: 28px; font-weight: 600; color: #1f2937; margin-bottom: 16px; }
        .stats-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; padding: 30px; background: #f3f4f6; border-radius: 12px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08); margin-bottom: 16px; }
        .stat-card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); border-left: 4px solid; }
        .stat-card.total { border-left-color: #3b82f6; }
        .stat-card.pending { border-left-color: #f59e0b; }
        .stat-card.exported { border-left-color: #a3bffa; }
        .stat-number { font-size: 1.8rem; font-weight: 700; margin-bottom: 5px; }
        .stat-label { color: #6b7280; font-size: 0.9rem; font-weight: 500; }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
        .search-container form { display: flex; gap: 12px; align-items: center; }
        .search-container .search-icon { position: absolute; top: 50%; left: 12px; transform: translateY(-50%); color: #6b7280; }
        .search-container .search-input { padding-left: 36px; height: 38px; border-radius: 6px; border: 1px solid #d1d5db; }
        .table-container { overflow-x: auto; background-color: white; border-radius: 12px; box-shadow: 0 2px4px rgba(0, 0, 0, 0.08); }
        table.table { width: 100%; border-collapse: collapse; min-width: 600px; }
        table.table th, table.table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e5e7eb; font-size: 14px; vertical-align: middle; }
        table.table th { background-color: #f3f4f6; font-weight: 600; color: #1f2937; position: sticky; top: 0; z-index: 2; }
        table.table tbody tr:nth-child(even) { background-color: #f9fafb; }
        table.table tbody tr:hover { background-color: #eef2ff; }
        .action-buttons { display: flex; gap: 8px; }
        .no-data { text-align: center; padding: 24px; background-color: #f3f4f6; border-radius: 12px; font-size: 16px; color: #6b7280; }
        .pagination { display: flex; gap: 8px; justify-content: center; margin-top: 16px; }
        .pagination a, .pagination span { padding: 6px 12px; border: 1px solid #ddd; border-radius: 4px; color: #333; text-decoration: none; }
        .pagination .current { background: #3b82f6; color: #fff; border-color: #3b82f6; }
        .pagination span:not(.current) { opacity: 0.3; cursor: not-allowed; }
        .modal-dialog { max-width: 800px; }
        .modal-body { max-height: 70vh; overflow-y: auto; }
        .status-badge { padding: 6px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; text-transform: uppercase; }
        .status-pending { background: #fef3c7; color: #92400e; }
        .status-completed { background: #d1fae5; color: #065f46; }
        .status-cancelled { background: #fee2e2; color: #991b1b; }
        .status-exported { background: #a3bffa; color: #1e40af; }
        .priority-low { color: #065f46; background: #d1fae5; padding: 2px 6px; border-radius: 4px; }
        .priority-medium { color: #92400e; background: #fef3c7; padding: 2px 6px; border-radius: 4px; }
        .priority-high { color: #991b1b; background: #fee2e2; padding: 2px 6px; border-radius: 4px; }
        .form-select { width: 170px; }
        @media (max-width: 768px) {
            .main-content { margin-left: 0; }
            .title { font-size: 24px; }
            table.table { min-width: 600px; }
            .stats-row { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <div class="main-layout">
        <%@ include file="sidebar.jsp" %>
        <div class="main-content">
            <div class="container">
                <h1 class="title">BackOrder Management</h1>

                <!-- Statistics -->
                <div class="stats-row">
                    <div class="stat-card total">
                        <div class="stat-number">${stats[0]}</div>
                        <div class="stat-label">Total BackOrders</div>
                    </div>
                    <div class="stat-card pending">
                        <div class="stat-number">${stats[1]}</div>
                        <div class="stat-label">Pending</div>
                    </div>
                    <div class="stat-card exported">
                        <div class="stat-number">${stats[4]}</div>
                        <div class="stat-label">Exported</div>
                    </div>
                </div>

                <!-- Controls -->
                <div class="header-actions">
                    <div class="search-container">
                        <form method="GET" action="backorder" id="filterForm">
                            <div style="display: flex; gap: 12px; align-items: center;">
                                <div style="position: relative;">
                                    <i class="fas fa-search search-icon"></i>
                                    <input type="text" name="search" class="search-input" 
                                           placeholder="Search material or priority..." value="${search}">
                                </div>
                                <select name="status" class="form-select">
                                    <option value="" ${empty status ? 'selected' : ''}>All</option>
                                    <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                    <option value="EXPORTED" ${status == 'EXPORTED' ? 'selected' : ''}>Exported</option>
                                </select>
                                <select name="sortBy" class="form-select">
                                    <option value="Created_at" ${sortBy == 'Created_at' ? 'selected' : ''}>Created Date</option>
                                    <option value="Remaining_quantity" ${sortBy == 'Remaining_quantity' ? 'selected' : ''}>Remaining Quantity</option>
                                    <option value="Material_name" ${sortBy == 'Material_name' ? 'selected' : ''}>Material Name</option>
                                    <option value="Available_quantity" ${sortBy == 'Available_quantity' ? 'selected' : ''}>Available Quantity</option>
                                </select>
                                <button type="submit" class="btn btn-primary" style="padding: 6px 12px;">Filter</button>
                                <a href="backorder" class="btn btn-secondary" style="padding: 6px 12px;">Reset</a>
                                <a href="orderlist" class="btn btn-primary" style="padding: 6px 12px;">Order</a>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Table -->
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th class="col-md-1">#</th>
                                <th class="col-md-2">Material</th>
                                <th class="col-md-2">Unit</th>
                                <th class="col-md-2">Requested Quantity</th>
                                <th class="col-md-2">Remaining Quantity</th>
                                <th class="col-md-2">Available Quantity</th>
                                <th class="col-md-2">Status</th>
                                <th class="col-md-2">Created At</th>
                                <th class="col-md-1">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty backOrders}">
                                    <c:forEach var="bo" items="${backOrders}" varStatus="loop">
                                        <tr>
                                            <td><strong>${(page-1)*5 + loop.index + 1}</strong></td>
                                            <td>${bo.materialName}</td>
                                            <td>${bo.subUnitName}</td>
                                            <td><fmt:formatNumber value="${bo.requestedQuantity}" pattern="#,##0.00" /></td>
                                            <td><span class="priority-${bo.note != null ? bo.note.toLowerCase() : 'low'}"><fmt:formatNumber value="${bo.remainingQuantity}" pattern="#,##0.00" /></span></td>
                                            <td><fmt:formatNumber value="${bo.availableQuantity}" pattern="#,##0.00" /></td>
                                            <td><span class="status-badge status-${bo.status.toLowerCase()}">${bo.status}</span></td>
                                            <td><fmt:formatDate value="${bo.createdAt}" pattern="dd/MM/yyyy" /></td>
                                            <td>
                                                <div class="action-buttons">
                                                    <button class="btn btn-info btn-sm view-detail" data-id="${bo.backOrderId}" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <c:if test="${bo.status == 'PENDING' && bo.remainingQuantity > 0}">
                                                        <button class="btn btn-success btn-sm export-backorder" data-id="${bo.backOrderId}" title="Export">
                                                            <i class="fas fa-file-export"></i>
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" class="no-data">No BackOrders found.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="backorder?page=1${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}" title="First page">
                                    <i class="fas fa-angle-double-left"></i>
                                </a>
                                <a href="backorder?page=${page-1}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}" title="Previous page">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span title="First page"><i class="fas fa-angle-double-left"></i></span>
                                <span title="Previous page"><i class="fas fa-angle-left"></i></span>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${totalPages <= 7}">
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                        <c:otherwise><a href="backorder?page=${i}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${i}</a></c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${page <= 4}">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                                <c:otherwise><a href="backorder?page=${i}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${i}</a></c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <c:if test="${totalPages > 6}"><span style="padding: 8px 4px;">...</span><a href="backorder?page=${totalPages}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${totalPages}</a></c:if>
                                    </c:when>
                                    <c:when test="${page >= totalPages - 3}">
                                        <a href="backorder?page=1${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">1</a>
                                        <c:if test="${totalPages > 6}"><span style="padding: 8px 4px;">...</span></c:if>
                                        <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                                <c:otherwise><a href="backorder?page=${i}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${i}</a></c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="backorder?page=1${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">1</a>
                                        <span style="padding: 8px 4px;">...</span>
                                        <c:forEach begin="${page - 1}" end="${page + 1}" var="i">
                                            <c:choose>
                                                <c:when test="${i == page}"><span class="current">${i}</span></c:when>
                                                <c:otherwise><a href="backorder?page=${i}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${i}</a></c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span style="padding: 8px 4px;">...</span>
                                        <a href="backorder?page=${totalPages}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}">${totalPages}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${page < totalPages}">
                                <a href="backorder?page=${page+1}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}" title="Next page">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                                <a href="backorder?page=${totalPages}${not empty search ? '&search=' : ''}${search}${not empty status ? '&status=' : ''}${status}${not empty sortBy ? '&sortBy=' : ''}${sortBy}" title="Last page">
                                    <i class="fas fa-angle-double-right"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <span title="Next page"><i class="fas fa-angle-right"></i></span>
                                <span title="Last page"><i class="fas fa-angle-double-right"></i></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                        Page ${page} of ${totalPages} (${stats[0]} total BackOrders)
                    </div>
                </c:if>

                <!-- Modal for Updating BackOrder -->
                <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editModalLabel">Update BackOrder</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form method="POST" action="backorder" id="updateForm">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="backOrderId" id="modalBackOrderId">
                                    <div class="mb-3">
                                        <label for="modalMaterial" class="form-label">Material</label>
                                        <input type="text" id="modalMaterial" class="form-control" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalUnit" class="form-label">Unit</label>
                                        <input type="text" id="modalUnit" class="form-control" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalQuantity" class="form-label">Remaining Quantity</label>
                                        <input type="text" id="modalQuantity" class="form-control" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalAvailableQuantity" class="form-label">Available Quantity</label>
                                        <input type="text" id="modalAvailableQuantity" class="form-control" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalStatus" class="form-label">Status</label>
                                        <input type="text" id="modalStatus" class="form-control" readonly>
                                    </div>
                                    <div class="mb-3">
                                        <label for="modalPriority" class="form-label">Priority</label>
                                        <select id="modalPriority" name="priority" class="form-select">
                                            <option value="Low">Low</option>
                                            <option value="Medium">Medium</option>
                                            <option value="High">High</option>
                                        </select>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary" form="updateForm">Update</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Toast for Notifications -->
                <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                    <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="toast-header">
                            <strong class="me-auto">Notification</strong>
                            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                        <div class="toast-body" id="toastMessage"></div>
                        <div class="toast-footer" id="toastFooter" style="display: none; padding: 8px; text-align: right;">
                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="toast">Cancel</button>
                            <button type="button" class="btn btn-primary btn-sm" id="confirmExportBtn">Confirm</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function () {
            const toast = new bootstrap.Toast(document.getElementById('liveToast'));

            $('.view-detail').on('click', function () {
                var id = $(this).data('id');
                $.ajax({
                    url: 'backorder',
                    type: 'GET',
                    data: { backOrderId: id },
                    dataType: 'json',
                    success: function(response) {
                        if (response.success) {
                            $('#modalBackOrderId').val(id);
                            $('#modalMaterial').val(response.material);
                            $('#modalUnit').val(response.unit);
                            $('#modalQuantity').val(response.quantity);
                            $('#modalAvailableQuantity').val(response.availableQuantity);
                            $('#modalStatus').val(response.status);
                            $('#modalPriority').val(response.priority || 'Low');
                            new bootstrap.Modal(document.getElementById('editModal')).show();
                        } else {
                            $('#toastMessage').text('Error: ' + response.message);
                            $('#toastFooter').hide();
                            $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass('text-bg-danger');
                            toast.show();
                        }
                    },
                    error: function() {
                        $('#toastMessage').text('Error loading BackOrder data.');
                        $('#toastFooter').hide();
                        $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass('text-bg-danger');
                        toast.show();
                    }
                });
            });

            $('#updateForm').on('submit', function(e) {
                e.preventDefault();
                var formData = $(this).serialize();
                $.ajax({
                    url: $(this).attr('action'),
                    type: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function(response) {
                        $('#editModal').modal('hide');
                        $('#toastMessage').text(response.message);
                        $('#toastFooter').hide();
                        $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass(response.success ? 'text-bg-success' : 'text-bg-danger');
                        toast.show();
                        if (response.success) {
                            setTimeout(() => location.reload(), 1000);
                        }
                    },
                    error: function() {
                        $('#editModal').modal('hide');
                        $('#toastMessage').text('An error occurred during update.');
                        $('#toastFooter').hide();
                        $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass('text-bg-danger');
                        toast.show();
                    }
                });
            });

            $('.export-backorder').on('click', function () {
                var backOrderId = $(this).data('id');
                $('#toastMessage').text('Are you sure you want to export this BackOrder and create a new Order?');
                $('#toastFooter').show();
                $('#liveToast').removeClass('text-bg-success text-bg-danger');
                $('#confirmExportBtn').data('backorder-id', backOrderId);
                toast.show();
            });

            $('#confirmExportBtn').on('click', function () {
                var backOrderId = $(this).data('backorder-id');
                $.ajax({
                    url: 'backorder',
                    type: 'POST',
                    data: { action: 'export', backOrderId: backOrderId },
                    dataType: 'json',
                    success: function(response) {
                        $('#toastMessage').text(response.message);
                        $('#toastFooter').hide();
                        $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass(response.success ? 'text-bg-success' : 'text-bg-danger');
                        toast.show();
                        if (response.success) {
                            setTimeout(() => location.reload(), 1000);
                        }
                    },
                    error: function() {
                        $('#toastMessage').text('Error exporting BackOrder.');
                        $('#toastFooter').hide();
                        $('#liveToast').removeClass('text-bg-success text-bg-danger').addClass('text-bg-danger');
                        toast.show();
                    }
                });
            });
        });
    </script>
</body>
</html>