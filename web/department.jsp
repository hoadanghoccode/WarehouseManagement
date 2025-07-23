<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    @SuppressWarnings("unchecked")
    Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (perms == null) {
        perms = new HashMap<>();
    }        
    // Set attribute để có thể truy cập trong JSP
    request.setAttribute("perms", perms);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Department Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="icon" href="img/logo.png" type="image/png">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <!-- themefy CSS -->
        <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
        <!-- swiper slider CSS -->
        <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
        <!-- owl carousel CSS -->
        <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
        <!-- gijgo css -->
        <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
        <!-- font awesome CSS -->
        <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
        <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
        <!-- date picker -->
        <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
        <!-- datatable CSS -->
        <link rel="stylesheet" href="vendors/datatable/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/responsive.dataTables.min.css" />
        <link rel="stylesheet" href="vendors/datatable/css/buttons.dataTables.min.css" />
        <!-- text editor css -->
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <!-- morris css -->
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <!-- metarial icon css -->
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <style>
            .custom-switch .form-check-input {
                width: 2.5em;
                height: 1.5em;
            }
            .custom-switch .form-check-input:checked {
                background-color: #198754;
            }
            .table-bordered th, .table-bordered td {
                border: 1px solid #dee2e6;
            }
            .table-header {
                background-color: #f8f9fa;
                border-bottom: 2px solid #dee2e6;
            }
            .invalid-feedback {
                display: none;
            }
            .is-invalid ~ .invalid-feedback {
                display: block;
            }
            .bootstrap-alert-container {
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                z-index: 1050;
                width: 100%;
                max-width: 500px;
            }
        </style>
    </head>
    <body>
        <%@ include file="sidebar.jsp" %>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container mt-4">
                <!-- Bootstrap Alert Container -->
                <div class="bootstrap-alert-container" id="alertContainer"></div>
                <div class="row mb-3">
                    <div class="col">
                        <form class="d-flex" method="get" action="department">
                            <input type="text" class="form-control me-2" name="search" placeholder="Search department..." value="${search}">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </form>
                    </div>
                    <div class="col d-flex justify-content-end">
                        <c:if test="${perms['Department_ADD']}">
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#departmentModal">
                                Add Department
                            </button>
                        </c:if>
                    </div>
                </div>
                <table class="table table-bordered">
                    <thead class="table-header">
                        <tr>
                            <th>#</th>
                            <th>Department Name</th>
                            <th>Role Name</th>
                            <th>User Count</th>
                            <th>Description</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dept" items="${departmentList}" varStatus="st">
                            <tr>
                                <td><strong>${(page-1)*pageSize + st.index + 1}</strong></td>
                                <td>${dept.departmentName}</td>
                                <td>${empty dept.roleName ? 'No Role Assigned' : dept.roleName}</td>
                                <td>${empty dept.userCount ? 0 : dept.userCount}</td>
                                <td>${dept.description}</td>
                                <td>
                                    <button class="btn btn-info btn-sm me-2 view-btn" data-dept-id="${dept.departmentId}" data-bs-toggle="modal" data-bs-target="#viewDepartmentModal"><i class="fas fa-eye text-white"></i></button>
                                    <button class="btn btn-danger btn-sm delete-btn" data-dept-id="${dept.departmentId}" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal"><i class="fas fa-trash"></i></button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <!-- Stats info like supplier -->
                <div class="stats-info mb-2">
                    <i class="fas fa-info-circle"></i>
                    <c:choose>
                        <c:when test="${not empty search}">
                            Found <strong>${totalDepartments}</strong> for "<strong>${search}</strong>"
                        </c:when>
                        <c:otherwise>
                            Showing <strong>${departmentList.size()}</strong> / <strong>${totalDepartments}</strong> departments
                        </c:otherwise>
                    </c:choose>
                </div>
                <!-- Pagination (Bootstrap style with arrow icons like supplier) -->
                <c:if test="${numPages > 1}">
                    <style>
                        .pagination .page-item.active .page-link {
                            background-color: #3f2179 !important;
                            border-color: #3f2179 !important;
                            color: #fff !important;
                        }
                        .pagination .page-link {
                            color: #3f2179;
                            border-radius: 6px !important;
                            border: 1px solid #e5e5e5;
                            margin: 0 2px;
                        }
                        .pagination .page-item .page-link:hover {
                            background-color: #ede7f6;
                            color: #3f2179;
                        }
                        .pagination .page-item.disabled .page-link {
                            color: #ccc;
                            background: #f8f9fa;
                            border-color: #e5e5e5;
                        }
                    </style>
                    <div class="d-flex justify-content-center">
                        <ul class="pagination">
                            <c:url var="firstUrl" value="/department">
                                <c:param name="page" value="1"/>
                                <c:if test="${not empty search}">
                                    <c:param name="search" value="${search}"/>
                                </c:if>
                            </c:url>
                            <c:url var="prevUrl" value="/department">
                                <c:param name="page" value="${page-1}"/>
                                <c:if test="${not empty search}">
                                    <c:param name="search" value="${search}"/>
                                </c:if>
                            </c:url>
                            <li class="page-item${page == 1 ? ' disabled' : ''}">
                                <a class="page-link" href="${firstUrl}" tabindex="-1">&laquo;</a>
                            </li>
                            <li class="page-item${page == 1 ? ' disabled' : ''}">
                                <a class="page-link" href="${prevUrl}" tabindex="-1">&lsaquo;</a>
                            </li>
                            <c:forEach begin="1" end="${numPages}" var="i">
                                <c:url var="pageUrl" value="/department">
                                    <c:param name="page" value="${i}"/>
                                    <c:if test="${not empty search}">
                                        <c:param name="search" value="${search}"/>
                                    </c:if>
                                </c:url>
                                <li class="page-item${i == page ? ' active' : ''}">
                                    <a class="page-link" href="${pageUrl}">${i}</a>
                                </li>
                            </c:forEach>
                            <c:url var="nextUrl" value="/department">
                                <c:param name="page" value="${page+1}"/>
                                <c:if test="${not empty search}">
                                    <c:param name="search" value="${search}"/>
                                </c:if>
                            </c:url>
                            <c:url var="lastUrl" value="/department">
                                <c:param name="page" value="${numPages}"/>
                                <c:if test="${not empty search}">
                                    <c:param name="search" value="${search}"/>
                                </c:if>
                            </c:url>
                            <li class="page-item${page == numPages ? ' disabled' : ''}">
                                <a class="page-link" href="${nextUrl}">&rsaquo;</a>
                            </li>
                            <li class="page-item${page == numPages ? ' disabled' : ''}">
                                <a class="page-link" href="${lastUrl}">&raquo;</a>
                            </li>
                        </ul>
                    </div>
                    <div class="page-info text-center mt-2">
                        Page ${page} of ${numPages} (${totalDepartments} total)
                    </div>
                </c:if>
            </div>

            <!-- Modal Xác nhận Xoá -->
            <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
                <div class="modal-dialog">
                    <c:if test="${perms['Role_DELETE']}"> 
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirm deletion</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                Are you sure you want to delete this role?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="button" id="confirmDeleteBtn" class="btn btn-danger">Delete</button>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${!perms['Role_DELETE']}"> 
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirm deletion</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                You do not have permission to delete !
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>

                            </div>
                        </div>

                    </c:if>
                </div>
            </div>

            <!-- Modal Add -->
            <div class="modal fade" id="departmentModal" tabindex="-1" aria-labelledby="departmentModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="departmentModalLabel">Add New Department</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="departmentForm">
                                <div class="mb-3">
                                    <label for="departmentName" class="form-label">Department Name</label>
                                    <input type="text" class="form-control" id="departmentName" name="departmentName" required>

                                    <small class="text-danger" id="departmentNameError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="departmentDescription" class="form-label">Description</label>
                                    <textarea class="form-control" id="departmentDescription" name="departmentDescription" rows="3" required></textarea>

                                    <small class="text-danger" id="departmentDescError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="departmentRole" class="form-label">Select Role</label>
                                    <select class="form-select" id="departmentRole" name="departmentRole" onchange="updatePermissionMatrix()" required>
                                        <option value="">Select a role...</option>
                                        <!-- Các <option> sẽ được JS đổ vào từ JSON -->
                                    </select>

                                    <small class="text-danger" id="departmentRoleError"></small>
                                </div>
                                <div id="permissionMatrix" class="permission-matrix" style="display: none;">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Role Name</th>
                                                <th>Resource</th>
                                                <th>Can Add</th>
                                                <th>Can View</th>
                                                <th>Can Update</th>
                                                <th>Can Delete</th>
                                            </tr>
                                        </thead>
                                        <tbody id="permissionMatrixBody">
                                            <!-- JS sẽ tạo <tr> bên trong -->
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button id="btnSaveDept" type="button" class="btn btn-primary">Save</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal View/Edit Department -->
            <div class="modal fade" id="viewDepartmentModal" tabindex="-1" aria-labelledby="viewDepartmentModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="viewDepartmentModalLabel">Department Details</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="viewDepartmentForm">
                                <input type="hidden" id="viewDepartmentId" name="departmentId">
                                <div class="mb-3">
                                    <label for="viewDepartmentName" class="form-label">Department Name</label>
                                    <input type="text" class="form-control" id="viewDepartmentName" name="departmentName" required>
                                    <div class="invalid-feedback">
                                        Department name cannot be blank
                                    </div>
                                    <small class="text-danger" id="viewDepartmentNameError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="viewDepartmentDescription" class="form-label">Description</label>
                                    <textarea class="form-control" id="viewDepartmentDescription" name="departmentDescription" rows="3" required></textarea>
                                    <div class="invalid-feedback">
                                        Department Description cannot be blank
                                    </div>
                                    <small class="text-danger" id="viewDepartmentDescError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="viewDepartmentRole" class="form-label">Select Role</label>
                                    <select class="form-select" id="viewDepartmentRole" name="departmentRole" onchange="updateViewPermissionMatrix()" required>
                                        <option value="">Select a role...</option>
                                    </select>
                                    <div class="invalid-feedback">
                                        You must select 1 Role for the Department
                                    </div>
                                    <small class="text-danger" id="viewDepartmentRoleError"></small>
                                </div>
                                <div id="viewPermissionMatrix" class="permission-matrix" style="display: none;">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Role Name</th>
                                                <th>Resource</th>
                                                <th>Can Add</th>
                                                <th>Can View</th>
                                                <th>Can Update</th>
                                                <th>Can Delete</th>
                                            </tr>
                                        </thead>
                                        <tbody id="viewPermissionMatrixBody">
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <c:if test="${perms['Department_UPDATE']}"> 
                                <button id="btnSaveViewDept" type="button" class="btn btn-primary">Save Changes</button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                                        // Mảng chứa các "entry" để render vào bảng
                                        // Mỗi entry tương ứng 1 department + 1 role + danh sách resources
                                        let entries = [];

                                        // 1) fetch dữ liệu từ server (endpoint /department/data)
                                        fetch(`/WarehouseManagement/department/data`)
                                                .then(response => {
                                                    if (!response.ok)
                                                        throw new Error("HTTP error " + response.status);
                                                    return response.json();
                                                })
                                                .then(data => {
                                                   

                                                    // Chuyển sang entries với cùng cấu trúc cho render
                                                    entries = data.map(dept => {
                                                        // Nếu role = null (department chưa gán role), ta tạo 1 object role tạm
                                                        if (!dept.role) {
                                                            return {
                                                                departmentId: dept.departmentId,
                                                                departmentName: dept.departmentName,
                                                                description: dept.description,
                                                                roleId: null,
                                                                roleName: null,
                                                                permissions: [], // rỗng
                                                                userCount: dept.userCount // Thêm userCount
                                                            };
                                                        }
                                                        // Ngược lại role không null
                                                        return {
                                                            departmentId: dept.departmentId,
                                                            departmentName: dept.departmentName,
                                                            description: dept.description,
                                                            roleId: dept.role.roleId,
                                                            roleName: dept.role.role,
                                                            permissions: Array.isArray(dept.role.resources)
                                                                    ? dept.role.resources.map(res => ({
                                                                            resourceId: res.resourceId,
                                                                            resourceName: res.resourceName,
                                                                            canAdd: res.canAdd,
                                                                            canView: res.canView,
                                                                            canUpdate: res.canUpdate,
                                                                            canDelete: res.canDelete
                                                                        }))
                                                                    : [],
                                                            userCount: dept.userCount // Thêm userCount
                                                        };
                                                    });

                                                    // Sau khi có entries, gọi hàm render để vẽ bảng
                                                    renderRoleTable();
                                                })
                                                .catch(err => {
                                                    console.error("Lỗi khi fetch dữ liệu:", err);
                                                });

                                        // 2) Hàm render bảng
                                        function renderRoleTable() {
                                            const tbody = document.getElementById('roleTableBody');
                                            tbody.innerHTML = ''; // Làm mới nội dung

                                            entries.forEach((entry, i) => {
                                                // Nếu roleId là null ⇒ department chưa gán role,
                                                // hiển thị 1 dòng duy nhất với thông báo "No Role"
                                                if (entry.roleId === null) {
                                                    const tr = document.createElement('tr');

                                                    // Department Name
                                                    const tdDept = document.createElement('td');
                                                    tdDept.textContent = entry.departmentName || '';
                                                    tdDept.colSpan = 2; // gộp hai cột Department Name + Role Name
                                                    tr.appendChild(tdDept);

                                                    // Hiển thị thông báo "No Role"
                                                    const tdNoRole = document.createElement('td');
                                                    tdNoRole.textContent = 'No Role Assigned';
                                                    tdNoRole.colSpan = 5; // Giảm colspan xuống 4 để dành chỗ cho cột Actions
                                                    tdNoRole.className = 'text-center fst-italic text-muted';
                                                    tr.appendChild(tdNoRole);

                                                    // Thêm cột User Count
                                                    const tdUserCount = document.createElement('td');
                                                    tdUserCount.textContent = entry.userCount ?? 0;
                                                    tr.appendChild(tdUserCount);

                                                    // Thêm cột Actions với các nút chức năng
                                                    const tdAction = document.createElement('td');

                                                    // Nút View/Edit
                                                    const viewBtn = document.createElement('button');
                                                    viewBtn.className = 'btn btn-info btn-sm me-2';
                                                    viewBtn.innerHTML = '<i class="fas fa-eye text-white"></i>';
                                                    viewBtn.setAttribute('data-dept-id', entry.departmentId);

                                                    // Event khi click vào nút View
                                                    viewBtn.addEventListener('click', (e) => {
                                                        e.preventDefault();
                                                        const deptId = viewBtn.getAttribute('data-dept-id');
                                                        loadDepartmentDetails(deptId);
                                                    });

                                                    // Nút Delete
                                                    const deleteBtn = document.createElement('button');
                                                    deleteBtn.className = 'btn btn-danger btn-sm';
                                                    deleteBtn.innerHTML = '<i class="fas fa-trash"></i>';
                                                    deleteBtn.setAttribute('data-dept-id', entry.departmentId);

                                                    // Event khi click vào nút Xoá
                                                    deleteBtn.addEventListener('click', (e) => {
                                                        e.preventDefault();
                                                        const did = deleteBtn.getAttribute('data-dept-id');
                                                        console.log('Delete department clicked: deptId=', did);

                                                        // Lưu ID department cần xóa vào biến global
                                                        departmentToDeleteId = did;

                                                        // Hiển thị modal xác nhận
                                                        const confirmModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
                                                        confirmModal.show();
                                                    });

                                                    tdAction.appendChild(viewBtn);
                                                    tdAction.appendChild(deleteBtn);
                                                    tr.appendChild(tdAction);

                                                    tbody.appendChild(tr);
                                                } else {
                                                    // Nếu department có role, duyệt từng permission của role đó
                                                    entry.permissions.forEach((perm, j) => {
                                                        const tr = document.createElement('tr');

                                                        // Chỉ hiển thị Department Name + Role Name ở dòng đầu tiên (j === 0)
                                                        if (j === 0) {
                                                            // Cột Department Name
                                                            const tdDept = document.createElement('td');
                                                            tdDept.textContent = entry.departmentName || '';
                                                            tdDept.rowSpan = entry.permissions.length;
                                                            tr.appendChild(tdDept);

                                                            // Cột Role Name
                                                            const tdRole = document.createElement('td');
                                                            tdRole.textContent = entry.roleName || '';
                                                            tdRole.rowSpan = entry.permissions.length;
                                                            tr.appendChild(tdRole);
                                                        }

                                                        // Chỉ dòng đầu tiên (j === 0) mới hiển thị cột User Count và Actions
                                                        if (j === 0) {
                                                            // Thêm cột User Count
                                                            const tdUserCount = document.createElement('td');
                                                            tdUserCount.textContent = entry.userCount ?? 0;
                                                            tdUserCount.rowSpan = entry.permissions.length;
                                                            tr.appendChild(tdUserCount);

                                                            const tdBtn = document.createElement('td');
                                                            tdBtn.rowSpan = entry.permissions.length;

                                                            // Nút View/Edit
                                                            const viewBtn = document.createElement('button');
                                                            viewBtn.className = 'btn btn-info btn-sm me-2';
                                                            viewBtn.innerHTML = '<i class="fas fa-eye text-white"></i>';
                                                            viewBtn.setAttribute('data-dept-id', entry.departmentId);

                                                            // Event khi click vào nút View
                                                            viewBtn.addEventListener('click', (e) => {
                                                                e.preventDefault();
                                                                const deptId = viewBtn.getAttribute('data-dept-id');
                                                                loadDepartmentDetails(deptId);
                                                            });

                                                            // Nút Delete
                                                            const deleteBtn = document.createElement('button');
                                                            deleteBtn.className = 'btn btn-danger btn-sm';
                                                            deleteBtn.innerHTML = '<i class="fas fa-trash"></i>';
                                                            deleteBtn.setAttribute('data-role-id', entry.roleId);
                                                            deleteBtn.setAttribute('data-dept-id', entry.departmentId);

                                                            // Event khi click vào nút Xoá
                                                            deleteBtn.addEventListener('click', (e) => {
                                                                e.preventDefault();
                                                                const rid = deleteBtn.getAttribute('data-role-id');
                                                                const did = deleteBtn.getAttribute('data-dept-id');
                                                                console.log('Delete role clicked: deptId=', did, 'roleId=', rid);

                                                                // Lưu ID department cần xóa vào biến global
                                                                departmentToDeleteId = did;

                                                                // Hiển thị modal xác nhận
                                                                const confirmModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
                                                                confirmModal.show();
                                                            });

                                                            tdBtn.appendChild(viewBtn);
                                                            tdBtn.appendChild(deleteBtn);
                                                            tr.appendChild(tdBtn);
                                                        }

                                                        tbody.appendChild(tr);
                                                    });
                                                }
                                            });
                                        }
        </script>

        <script>
            // Biến toàn cục để chứa dữ liệu roles + permissions sau khi fetch
            let rolesData = [];

            // 1. Khi modal được show (Bootstrap event), ta gọi API để load JSON lần đầu.
            const departmentModalEl = document.getElementById('departmentModal');
            departmentModalEl.addEventListener('shown.bs.modal', function () {
                // Luôn fetch lại danh sách role mỗi lần mở modal
                fetch('/WarehouseManagement/permissionrole/data')
                    .then(response => {
                        if (!response.ok)
                            throw new Error('Lỗi khi lấy dữ liệu permissions.');
                        return response.json();
                    })
                    .then(data => {
                        rolesData = data; // lưu vào biến toàn cục
                        populateRoleDropdown();
                    })
                    .catch(err => {
                        console.error(err);
                        alert('Không thể tải danh sách role từ server.');
                    });
                // Mỗi lần mở modal, reset form + ẩn ma trận
                resetDepartmentForm();
            });

            // 2. Hàm đổ <option> dựa trên rolesData
            function populateRoleDropdown() {
                const selectEl = document.getElementById('departmentRole');
                // Xoá hết option cũ (ngoại trừ option "Select a role...")
                selectEl.innerHTML = '<option value="">Select a role...</option>';

                rolesData.forEach(roleObj => {
                    console.log('test', roleObj);
                    // roleObj có dạng { roleId: 1, roleName: "Admin", resources: [ ... ] }
                    const opt = document.createElement('option');
                    opt.value = roleObj.roleId;      // lấy roleId làm value
                    opt.text = roleObj.role;     // hiện roleName
                    selectEl.appendChild(opt);
                });
            }

            console.log('data here', rolesData);


            // 3. Khi user chọn 1 role (onchange), hiển thị ma trận permissions tương ứng
            function updatePermissionMatrix() {
                const roleId = parseInt(document.getElementById('departmentRole').value);
                const tblContainer = document.getElementById('permissionMatrix');
                const tbody = document.getElementById('permissionMatrixBody');

                // Nếu chưa chọn hoặc chọn giá trị rỗng → ẩn bảng luôn
                if (isNaN(roleId)) {
                    tblContainer.style.display = 'none';
                    tbody.innerHTML = '';
                    return;
                }

                // Tìm object RoleData tương ứng trong rolesData
                const selectedRole = rolesData.find(r => r.roleId === roleId);
                if (!selectedRole) {
                    tblContainer.style.display = 'none';
                    tbody.innerHTML = '';
                    return;
                }

                // Nếu tìm được → hiển thị container
                tblContainer.style.display = 'block';
                // Xoá hết nội dung cũ
                tbody.innerHTML = '';

                // Tạo từng <tr> cho mỗi resourcePerm trong selectedRole.resources
                selectedRole.resources.forEach(rp => {
                    const tr = document.createElement('tr');

                    // 1st column: Role Name
                    const tdRoleName = document.createElement('td');
                    tdRoleName.textContent = selectedRole.role;
                    tr.appendChild(tdRoleName);

                    // 2nd column: Resource Name
                    const tdResource = document.createElement('td');
                    tdResource.textContent = rp.resourceName;
                    tr.appendChild(tdResource);

                    // 3rd: Can Add
                    const tdAdd = document.createElement('td');
                    tdAdd.innerHTML = rp.canAdd ? '✓' : '✗';
                    tdAdd.style.textAlign = 'center';
                    tr.appendChild(tdAdd);

                    // 4th: Can View
                    const tdView = document.createElement('td');
                    tdView.innerHTML = rp.canView ? '✓' : '✗';
                    tdView.style.textAlign = 'center';
                    tr.appendChild(tdView);

                    // 5th: Can Update
                    const tdUpdate = document.createElement('td');
                    tdUpdate.innerHTML = rp.canUpdate ? '✓' : '✗';
                    tdUpdate.style.textAlign = 'center';
                    tr.appendChild(tdUpdate);

                    // 6th: Can Delete
                    const tdDelete = document.createElement('td');
                    tdDelete.innerHTML = rp.canDelete ? '✓' : '✗';
                    tdDelete.style.textAlign = 'center';
                    tr.appendChild(tdDelete);

                    tbody.appendChild(tr);
                });
            }

            // 4. Khi modal show, clear form để tránh dữ liệu cũ
            function resetDepartmentForm() {
                document.getElementById('departmentForm').reset();
                document.getElementById('permissionMatrix').style.display = 'none';
                document.getElementById('permissionMatrixBody').innerHTML = '';

                // Reset validation states
                const inputs = ['departmentName', 'departmentDescription', 'departmentRole'];
                inputs.forEach(id => {
                    const element = document.getElementById(id);
                    if (element) {
                        element.classList.remove('is-invalid');
                    }
                });

                // Reset error messages
                document.getElementById('departmentNameError').textContent = '';
                document.getElementById('departmentDescError').textContent = '';
                document.getElementById('departmentRoleError').textContent = '';
            }

            // 5. Bắt sự kiện Save → gửi AJAX POST để lưu vào DB
            document.getElementById('btnSaveDept').addEventListener('click', function () {
                const deptName = document.getElementById('departmentName');
                const deptDesc = document.getElementById('departmentDescription');
                const roleSelect = document.getElementById('departmentRole');
                let isValid = true;

                // Reset validation state
                [deptName, deptDesc, roleSelect].forEach(el => {
                    el.classList.remove('is-invalid');
                });

                // Reset error messages
                document.getElementById('departmentNameError').textContent = '';
                document.getElementById('departmentDescError').textContent = '';
                document.getElementById('departmentRoleError').textContent = '';

                // Validate Department Name
                if (!deptName.value.trim()) {
                    deptName.classList.add('is-invalid');
                    document.getElementById('departmentNameError').textContent = 'Please enter department name';
                    isValid = false;
                }

                // Validate Description
                if (!deptDesc.value.trim()) {
                    deptDesc.classList.add('is-invalid');
                    document.getElementById('departmentDescError').textContent = 'Please enter department description';
                    isValid = false;
                }

                // Validate Role
                if (!roleSelect.value) {
                    roleSelect.classList.add('is-invalid');
                    document.getElementById('departmentRoleError').textContent = 'Please select a Role for the department';
                    isValid = false;
                }

                if (!isValid) {
                    return;
                }

                // Chuẩn bị payload (có thể gửi theo FormData hoặc JSON; ví dụ gửi form-urlencoded)
                const formData = new URLSearchParams();
                formData.append('departmentName', deptName.value.trim());
                formData.append('departmentDescription', deptDesc.value.trim());
                formData.append('roleId', roleSelect.value);

                fetch('/WarehouseManagement/department', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    },
                    body: formData.toString()
                })
                        .then(response => {
                            if (!response.ok)
                                throw new Error('Error saving Department.');
                            return response.json();
                        })
                        .then(json => {
                            if (json.success) {
                                // Nếu backend trả về success: true                             
                                showAlert(true, 'Department added successfully!');

                                // Đóng modal
                                const modal = bootstrap.Modal.getInstance(departmentModalEl);
                                modal.hide();

                                // Gọi lại API để lấy dữ liệu mới nhất
                                fetch(`/WarehouseManagement/department/data`)
                                        .then(response => {
                                            if (!response.ok)
                                                throw new Error("HTTP error " + response.status);
                                            return response.json();
                                        })
                                        .then(data => {
                                            // Cập nhật lại mảng entries với dữ liệu mới
                                            entries = data.map(dept => {
                                                if (!dept.role) {
                                                    return {
                                                        departmentId: dept.departmentId,
                                                        departmentName: dept.departmentName,
                                                        description: dept.description,
                                                        roleId: null,
                                                        roleName: null,
                                                        permissions: [],
                                                        userCount: dept.userCount // Thêm userCount
                                                    };
                                                }
                                                return {
                                                    departmentId: dept.departmentId,
                                                    departmentName: dept.departmentName,
                                                    description: dept.description,
                                                    roleId: dept.role.roleId,
                                                    roleName: dept.role.role,
                                                    permissions: Array.isArray(dept.role.resources)
                                                            ? dept.role.resources.map(res => ({
                                                                    resourceId: res.resourceId,
                                                                    resourceName: res.resourceName,
                                                                    canAdd: res.canAdd,
                                                                    canView: res.canView,
                                                                    canUpdate: res.canUpdate,
                                                                    canDelete: res.canDelete
                                                                }))
                                                            : [],
                                                    userCount: dept.userCount // Thêm userCount
                                                };
                                            });

                                            // Render lại bảng với dữ liệu mới
                                            renderRoleTable();
                                        })
                                        .catch(err => {
                                            console.error("Lỗi khi fetch dữ liệu mới:", err);
                                            showAlert(false, 'An error occurred while updating data!');
                                        });
                            } else {
                                const modal = bootstrap.Modal.getInstance(departmentModalEl);
                                modal.hide();
                                showAlert(false, json.message);
                            }
                        })
                        .catch(err => {
                            showAlert(false, err.message || 'An error occurred while updating data!s');
                        });
            });
        </script>
        <!--        Xóa phòng ban-->
        <script>
            let departmentToDeleteId = null;

            document.addEventListener('DOMContentLoaded', function () {
                // Gán sự kiện cho nút delete trong bảng render bằng JSP
                document.querySelectorAll('.delete-btn').forEach(function (btn) {
                    btn.addEventListener('click', function (e) {
                        e.preventDefault();
                        departmentToDeleteId = btn.getAttribute('data-dept-id');
                        const confirmModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
                        confirmModal.show();
                    });
                });

                // Gán sự kiện cho nút xem chi tiết trong bảng render bằng JSP
                document.querySelectorAll('.view-btn').forEach(function (btn) {
                    btn.addEventListener('click', function (e) {
                        e.preventDefault();
                        const deptId = btn.getAttribute('data-dept-id');
                        loadDepartmentDetails(deptId);
                    });
                });

                // Gán sự kiện cho nút xác nhận xóa trong modal
                const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
                if (confirmDeleteBtn) {
                    confirmDeleteBtn.addEventListener('click', function () {
                        if (departmentToDeleteId) {
                            const formData = new URLSearchParams();
                            formData.append('departmentId', departmentToDeleteId);

                            fetch(`/WarehouseManagement/deletedepartment`, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                                },
                                body: formData.toString()
                            })
                            .then(response => {
                                if (!response.ok)
                                    throw new Error('Error while deleting department');
                                return response.json();
                            })
                            .then(data => {
                                if (data.success) {
                                    // Xóa department khỏi mảng entries nếu có
                                    if (typeof entries !== 'undefined' && Array.isArray(entries)) {
                                        entries = entries.filter(entry => entry.departmentId !== parseInt(departmentToDeleteId));
                                        if (typeof renderRoleTable === 'function') renderRoleTable();
                                    }
                                    showAlert(true, 'Department deleted successfully!');
                                } else {
                                    showAlert(false, data.message);
                                }
                            })
                            .catch(err => {
                                showAlert(false, err.message || 'An error occurred while deleting department');
                            })
                            .finally(() => {
                                const confirmModal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
                                if (confirmModal) {
                                    confirmModal.hide();
                                }
                                departmentToDeleteId = null;
                            });
                        }
                    });
                }
            });
        </script>

        <script>
            // Hàm load chi tiết department
            function loadDepartmentDetails(departmentId) {
                // Kiểm tra và load roles data nếu chưa có
                if (rolesData.length === 0) {
                    // Fetch roles data trước
                    fetch('/WarehouseManagement/permissionrole/data')
                            .then(response => {
                                if (!response.ok)
                                    throw new Error('Lỗi khi lấy dữ liệu permissions.');
                                return response.json();
                            })
                            .then(data => {
                                rolesData = data;
                                // Sau khi có roles data, tiếp tục hiển thị department
                                showDepartmentDetails(departmentId);
                            })
                            .catch(err => {
                                console.error(err);
                                showAlert(true, 'Unable to load role list from server.');

                            });
                } else {
                    // Nếu đã có roles data, hiển thị department luôn
                    showDepartmentDetails(departmentId);
                }
            }

            // Tách logic hiển thị department ra hàm riêng
            function showDepartmentDetails(departmentId) {
                // Reset form và trạng thái validate
                const form = document.getElementById('viewDepartmentForm');
                form.reset();
                resetValidation(form);

                // Tìm department trong mảng entries dựa vào ID
                const department = entries.find(entry => entry.departmentId === parseInt(departmentId));
                console.log('department', department);

                if (!department) {
//                    alert('Không tìm thấy thông tin phòng ban');
                                                    showAlert(false, 'Department information not found');

                    return;
                }

                // Điền thông tin vào form
                document.getElementById('viewDepartmentId').value = department.departmentId;
                document.getElementById('viewDepartmentName').value = department.departmentName;
                document.getElementById('viewDepartmentDescription').value = department.description || '';

                // Populate role dropdown
                populateViewRoleDropdown();

                // Set selected role nếu có
                if (department.roleId) {
                    document.getElementById('viewDepartmentRole').value = department.roleId;
                } else if (department.role && department.role.roleId) {
                    document.getElementById('viewDepartmentRole').value = department.role.roleId;
                }

                // Update permission matrix sau khi set role
                updateViewPermissionMatrix();

                // Hiển thị modal
                const viewModal = new bootstrap.Modal(document.getElementById('viewDepartmentModal'));
                viewModal.show();
            }

            // Populate role dropdown trong modal view
            function populateViewRoleDropdown() {
                const selectEl = document.getElementById('viewDepartmentRole');
                selectEl.innerHTML = '<option value="">Select a role...</option>';

                if (rolesData && rolesData.length > 0) {
                    rolesData.forEach(roleObj => {
                        const opt = document.createElement('option');
                        opt.value = roleObj.roleId;
                        opt.text = roleObj.role;
                        selectEl.appendChild(opt);
                    });
                }
            }

            // Update permission matrix trong modal view
            function updateViewPermissionMatrix() {
                const roleId = parseInt(document.getElementById('viewDepartmentRole').value);
                const tblContainer = document.getElementById('viewPermissionMatrix');
                const tbody = document.getElementById('viewPermissionMatrixBody');

                if (isNaN(roleId)) {
                    tblContainer.style.display = 'none';
                    tbody.innerHTML = '';
                    return;
                }

                const selectedRole = rolesData.find(r => r.roleId === roleId);
                if (!selectedRole) {
                    tblContainer.style.display = 'none';
                    tbody.innerHTML = '';
                    return;
                }

                tblContainer.style.display = 'block';
                tbody.innerHTML = '';

                selectedRole.resources.forEach(rp => {
                    const tr = document.createElement('tr');

                    // Role Name
                    const tdRoleName = document.createElement('td');
                    tdRoleName.textContent = selectedRole.role;
                    tr.appendChild(tdRoleName);

                    // Resource Name
                    const tdResource = document.createElement('td');
                    tdResource.textContent = rp.resourceName;
                    tr.appendChild(tdResource);

                    // Permissions
                    ['canAdd', 'canView', 'canUpdate', 'canDelete'].forEach(perm => {
                        const td = document.createElement('td');
                        td.innerHTML = rp[perm] ? '✓' : '✗';
                        td.style.textAlign = 'center';
                        tr.appendChild(td);
                    });

                    tbody.appendChild(tr);
                });
            }

            // Hàm reset validation
            function resetValidation(form) {
                form.querySelectorAll('.is-invalid').forEach(el => {
                    el.classList.remove('is-invalid');
                });
            }

            // Thêm event listener cho modal khi ẩn
            document.getElementById('viewDepartmentModal').addEventListener('hidden.bs.modal', function () {
                const form = document.getElementById('viewDepartmentForm');
                resetValidation(form);
            });

            // Thêm event listeners cho các input để tự động xóa trạng thái lỗi khi người dùng nhập liệu
            document.getElementById('viewDepartmentName').addEventListener('input', function () {
                if (this.value.trim()) {
                    this.classList.remove('is-invalid');
                }
            });

            document.getElementById('viewDepartmentDescription').addEventListener('input', function () {
                if (this.value.trim()) {
                    this.classList.remove('is-invalid');
                }
            });

            document.getElementById('viewDepartmentRole').addEventListener('change', function () {
                if (this.value) {
                    this.classList.remove('is-invalid');
                }
            });

            // Xử lý sự kiện Save Changes
            document.getElementById('btnSaveViewDept').addEventListener('click', function () {
                const form = document.getElementById('viewDepartmentForm');
                resetValidation(form);

                let isValid = true;

                // Validate Department Name
                const nameInput = document.getElementById('viewDepartmentName');
                if (!nameInput.value.trim()) {
                    nameInput.classList.add('is-invalid');
                    isValid = false;
                }

                // Validate Description
                const descInput = document.getElementById('viewDepartmentDescription');
                if (!descInput.value.trim()) {
                    descInput.classList.add('is-invalid');
                    isValid = false;
                }

                // Validate Role
                const roleSelect = document.getElementById('viewDepartmentRole');
                if (!roleSelect.value) {
                    roleSelect.classList.add('is-invalid');
                    isValid = false;
                }

                if (!isValid) {
                    return;
                }

                // Nếu validate pass, gửi request update
                const formData = new URLSearchParams();
                formData.append('departmentId', document.getElementById('viewDepartmentId').value);
                formData.append('departmentName', nameInput.value.trim());
                formData.append('departmentDescription', descInput.value.trim());
                formData.append('roleId', roleSelect.value);

                // Gửi request update đến servlet
                fetch('/WarehouseManagement/detaildepartment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    },
                    body: formData.toString()
                })
                        .then(async response => {
                            const data = await response.json();

                            // Xử lý các loại lỗi dựa vào status code
                            if (!response.ok) {
                                switch (response.status) {
                                    case 400: // Bad Request
                                        // Kiểm tra loại lỗi validate
                                        if (data.message.includes("Department ID không hợp lệ")) {
                                            throw new Error("ID phòng ban không hợp lệ");
                                        } else if (data.message.includes("Role ID không hợp lệ")) {
                                            roleSelect.classList.add('is-invalid');
                                            throw new Error("Role không hợp lệ");
                                        } else {
                                            // Lỗi validate chung
                                            if (!nameInput.value.trim())
                                                nameInput.classList.add('is-invalid');
                                            if (!descInput.value.trim())
                                                descInput.classList.add('is-invalid');
                                            if (!roleSelect.value)
                                                roleSelect.classList.add('is-invalid');
                                            throw new Error(data.message || "Vui lòng điền đầy đủ thông tin bắt buộc");
                                        }
                                    case 409: // Conflict
                                        roleSelect.classList.add('is-invalid');
                                        throw new Error(data.message || "Role này đã được sử dụng bởi department khác");
                                    case 500: // Internal Server Error
                                        throw new Error(data.message || "Lỗi hệ thống, vui lòng thử lại sau");
                                    default:
                                        throw new Error("Có lỗi xảy ra, vui lòng thử lại");
                                }
                            }
                            return data;
                        })
                        .then(data => {
                            if (data.success) {
                                // Cập nhật lại dữ liệu trong entries
                                const departmentIndex = entries.findIndex(entry =>
                                    entry.departmentId === parseInt(document.getElementById('viewDepartmentId').value)
                                );
                                if (departmentIndex !== -1) {
                                    entries[departmentIndex] = {
                                        ...entries[departmentIndex],
                                        departmentName: nameInput.value.trim(),
                                        description: descInput.value.trim(),
                                        roleId: parseInt(roleSelect.value),
                                        role: rolesData.find(r => r.roleId === parseInt(roleSelect.value))
                                    };
                                }

                                // Render lại bảng
                                renderRoleTable();

                                // Đóng modal
                                const viewModal = bootstrap.Modal.getInstance(document.getElementById('viewDepartmentModal'));
                                viewModal.hide();

                                // Hiển thị thông báo thành công
                                showAlert(true, data.message || 'Department update successful');
                            } else {
                                // Nếu có lỗi nhưng không phải lỗi HTTP
                                showAlert(false, data.message || 'Update failed');
                            }
                        })
                        .catch(err => {
                            console.error('Lỗi:', err);
                            showAlert('danger', err.message || 'An error occurred while updating Department');
                        });
            });


        </script>

        <script>
            /**
             * Hàm showAlert(status, message):
             *   - status = true  → alert màu xanh (alert-success)
             *   - status = false → alert màu đỏ  (alert-danger)
             * alert sẽ nằm cố định bên phải, tự đóng sau 4s
             */
            function showAlert(status, message) {
                // Xóa alert cũ nếu có
                const existingAlert = document.querySelector('.custom-alert');
                if (existingAlert) {
                    existingAlert.remove();
                }
                console.log('here', status);
                // Tạo alert mới
                const alertDiv = document.createElement('div');
                if (status === true) {
                    alertDiv.className = `alert alert-success alert-dismissible fade show custom-alert`;

                } else {
                    alertDiv.className = `alert alert-danger alert-dismissible fade show custom-alert`;

                }
                alertDiv.setAttribute('role', 'alert');
                alertDiv.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    min-width: 300px;
                    z-index: 9999;
                    padding: 1rem;
                    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
                    border-radius: 0.375rem;
                `;

                // Thêm icon
                const icon = document.createElement('i');

                icon.className = `fas ${status ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2`;
                alertDiv.appendChild(icon);

                // Thêm message
                const messageText = document.createTextNode(message);
                alertDiv.appendChild(messageText);



                // Thêm vào body
                document.body.appendChild(alertDiv);

                // Animation khi hiện alert
                setTimeout(() => {
                    alertDiv.style.opacity = '1';
                }, 100);

//                 Tự động đóng sau 4s
                setTimeout(() => {
                    if (alertDiv && document.body.contains(alertDiv)) {
                        alertDiv.classList.remove('show');
                        setTimeout(() => alertDiv.remove(), 150);
                    }
                }, 4000);
            }
        </script>

    </body>
</html>