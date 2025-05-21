<%--
    Document   : assign
    Created on : May 21, 2025, 1:00 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Assign Roles to Employee</title>
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
            .employee-info, .role-details {
                min-height: 200px;
            }
            .split-container {
                display: flex;
                gap: 20px;
            }
            .left-panel, .right-panel {
                flex: 1;
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
                    <div class="col d-flex justify-content-end">
                        <button id="selectEmployeeBtn" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#selectEmployeeModal">Chọn nhân viên</button>
                    </div>
                </div>

                <div class="split-container">
                    <div class="left-panel">
                        <div class="employee-info card p-3 mb-3" id="employeeInfo">
                            <h5>Thông tin nhân viên</h5>
                            <p><strong>Tên:</strong> <span id="employeeName"></span></p>
                            <p><strong>Email:</strong> <span id="employeeEmail"></span></p>
                            <div class="mb-3">
                                <label for="roleSelect" class="form-label">Chọn Role</label>
                                <select class="form-control" id="roleSelect" multiple>
                                    <!-- Options will be populated by JavaScript -->     
                                </select>
                            </div>
                            <button id="assignRolesBtn" class="btn btn-success" style="display: none;">Gán Role</button>
                        </div>
                    </div>
                    <div class="right-panel">
                        <div class="role-details card p-3" id="roleDetails">
                            <h5>Chi tiết Role đã chọn</h5>
                            <table class="table table-bordered" id="roleDetailTable">
                                <thead>
                                    <tr>
                                        <th>Role Name</th>
                                        <th>CATALOGUE</th>
                                        <th>Read</th>
                                        <th>Write</th>
                                        <th>EDIT</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal for Selecting Employee -->
            <div class="modal fade" id="selectEmployeeModal" tabindex="-1" aria-labelledby="selectEmployeeModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="selectEmployeeModalLabel">Chọn nhân viên</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Tên</th>
                                        <th>Email</th>
                                        <th>Chọn</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Nguyen Van A</td>
                                        <td>nguyenvana@example.com</td>
                                        <td><button class="btn btn-primary select-employee-btn" data-name="Nguyen Van A" data-email="nguyenvana@example.com">Chọn</button></td>
                                    </tr>
                                    <tr>
                                        <td>Tran Thi B</td>
                                        <td>tranthib@example.com</td>
                                        <td><button class="btn btn-primary select-employee-btn" data-name="Tran Thi B" data-email="tranthib@example.com">Chọn</button></td>
                                    </tr>
                                    <tr>
                                        <td>Le Van C</td>
                                        <td>levanc@example.com</td>
                                        <td><button class="btn btn-primary select-employee-btn" data-name="Le Van C" data-email="levanc@example.com">Chọn</button></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script>
            // Danh sách permissions và roles từ màn hình trước
            const permissions = [
                {name: "Add Product", catalogue: true, read: true, write: false, edit: true},
                {name: "Add Product via upload", catalogue: true, read: false, write: false, edit: true},
                {name: "Complete Drafts", catalogue: true, read: true, write: false, edit: true},
                {name: "View Listing Applications", catalogue: true, read: false, write: false, edit: true},
                {name: "Improve Listing Quality", catalogue: true, read: true, write: false, edit: true},
                {name: "Upload & Manage Videos", catalogue: true, read: true, write: false, edit: true},
                {name: "Manage Product Videos", catalogue: true, read: true, write: false, edit: true}
            ];

            let roles = [
                { name: "Admin", permissions: permissions.map(p => ({ ...p })) },
                { name: "Editor", permissions: permissions.map(p => ({ ...p, write: false, edit: true, catalogue: false, read: true })) }
            ];

            // Hàm hiển thị thông báo Bootstrap
            function showBootstrapAlert(message, type = 'success') {
                const alertContainer = document.getElementById('alertContainer');
                const alertDiv = document.createElement('div');
                alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
                alertDiv.role = 'alert';
                alertDiv.innerHTML = `
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                `;
                alertContainer.appendChild(alertDiv);

                setTimeout(() => {
                    alertDiv.classList.remove('show');
                    alertDiv.classList.add('fade');
                    setTimeout(() => alertDiv.remove(), 150);
                }, 3000);
            }

            // Khởi tạo Select2 cho multi-select role
            $(document).ready(function() {
                $('#roleSelect').select2({
                    placeholder: "Chọn role",
                    allowClear: true,
                    multiple: true
                });
            });

            // Populate role options
            function populateRoleOptions() {
                const roleSelect = document.getElementById('roleSelect');
                roleSelect.innerHTML = '';
                roles.forEach((role, index) => {
                    const option = document.createElement('option');
                    option.value = index;
                    option.text = role.name;
                    roleSelect.appendChild(option);
                });
            }

            // Hiển thị thông tin role đã chọn
            function renderRoleDetails(selectedRoles) {
                const tbody = document.getElementById('roleDetailTable').querySelector('tbody');
                tbody.innerHTML = '';
                selectedRoles.forEach(roleIndex => {
                    const role = roles[roleIndex];
                    role.permissions.forEach((perm, permIndex) => {
                        const row = document.createElement('tr');
                        if (permIndex === 0) {
                            const nameCell = document.createElement('td');
                            nameCell.textContent = role.name;
                            nameCell.rowSpan = role.permissions.length;
                            row.appendChild(nameCell);
                        }

                        const permCell = document.createElement('td');
                        permCell.textContent = perm.name;
                        row.appendChild(permCell);

                        const catalogueCell = document.createElement('td');
                        const catalogueCheckbox = document.createElement('input');
                        catalogueCheckbox.type = 'checkbox';
                        catalogueCheckbox.className = 'form-check-input';
                        catalogueCheckbox.checked = perm.catalogue;
                        catalogueCheckbox.disabled = true;
                        catalogueCell.appendChild(catalogueCheckbox);
                        row.appendChild(catalogueCell);

                        const readCell = document.createElement('td');
                        const readCheckbox = document.createElement('input');
                        readCheckbox.type = 'checkbox';
                        readCheckbox.className = 'form-check-input';
                        readCheckbox.checked = perm.read;
                        readCheckbox.disabled = true;
                        readCell.appendChild(readCheckbox);
                        row.appendChild(readCell);

                        const writeCell = document.createElement('td');
                        const writeCheckbox = document.createElement('input');
                        writeCheckbox.type = 'checkbox';
                        writeCheckbox.className = 'form-check-input';
                        writeCheckbox.checked = perm.write;
                        writeCheckbox.disabled = true;
                        writeCell.appendChild(writeCheckbox);
                        row.appendChild(writeCell);

                        const editCell = document.createElement('td');
                        const editCheckbox = document.createElement('input');
                        editCheckbox.type = 'checkbox';
                        editCheckbox.className = 'form-check-input';
                        editCheckbox.checked = perm.edit;
                        editCheckbox.disabled = true;
                        editCell.appendChild(editCheckbox);
                        row.appendChild(editCell);

                        tbody.appendChild(row);
                    });
                });
            }

            // Chọn nhân viên
            document.addEventListener('click', (e) => {
                if (e.target.classList.contains('select-employee-btn')) {
                    const name = e.target.dataset.name;
                    const email = e.target.dataset.email;
                    document.getElementById('employeeName').textContent = name;
                    document.getElementById('employeeEmail').textContent = email;
                    document.getElementById('assignRolesBtn').style.display = 'block';
                    populateRoleOptions();
                    const modal = bootstrap.Modal.getInstance(document.getElementById('selectEmployeeModal'));
                    modal.hide();
                }
            });

            // Gán role cho nhân viên
            document.getElementById('assignRolesBtn').addEventListener('click', () => {
                const roleSelect = document.getElementById('roleSelect');
                const selectedRoles = Array.from(roleSelect.selectedOptions).map(option => option.value);
                if (selectedRoles.length === 0) {
                    showBootstrapAlert('Vui lòng chọn ít nhất một role!', 'warning');
                    return;
                }
                renderRoleDetails(selectedRoles);
                showBootstrapAlert('Roles assigned successfully!', 'success');
            });

            // Xử lý khi thay đổi lựa chọn role
            $('#roleSelect').on('change', function() {
                const selectedRoles = Array.from(this.selectedOptions).map(option => option.value);
                renderRoleDetails(selectedRoles);
            });
        </script>
    </body>
</html>