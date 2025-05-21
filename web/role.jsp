<%--
    Document   : role
    Created on : May 21, 2025, 11:04:00 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Role Management</title>
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
                    <div class="col d-flex justify-content-end">
                        <button id="addRoleBtn" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addRoleModal">Add Role</button>
                    </div>
                </div>
                <table class="table table-bordered">
                    <thead class="table-header">
                        <tr>
                            <th>Role Name</th>
                            <th>Permission</th>
                            <th>CATALOGUE</th>
                            <th>Read</th>
                            <th>Write</th>
                            <th>EDIT</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="roleTableBody"></tbody>
                </table>
                <div class="row mb-3">
                    <div class="col d-flex justify-content-end">
                        <button id="submitBtn" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </div>

            <!-- Modal for Adding Role -->
            <div class="modal fade" id="addRoleModal" tabindex="-1" aria-labelledby="addRoleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addRoleModalLabel">Add New Role</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form id="addRoleForm">
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="roleName" class="form-label">Role Name</label>
                                    <input type="text" class="form-control" id="roleName" placeholder="Enter role name" required>
                                    <div class="invalid-feedback">Vui lòng nhập tên vai trò.</div>
                                </div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Permission</th>
                                            <th>CATALOGUE</th>
                                            <th>Read</th>
                                            <th>Write</th>
                                            <th>EDIT</th>
                                        </tr>
                                    </thead>
                                    <tbody id="permissionTableBody"></tbody>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary" id="saveRoleBtn">Save Role</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Modal for Editing Role -->
            <div class="modal fade" id="editRoleModal" tabindex="-1" aria-labelledby="editRoleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editRoleModalLabel">Edit Role</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form id="editRoleForm">
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="editRoleName" class="form-label">Role Name</label>
                                    <input type="text" class="form-control" id="editRoleName" placeholder="Enter role name" required>
                                    <div class="invalid-feedback">Vui lòng nhập tên vai trò.</div>
                                </div>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Permission</th>
                                            <th>CATALOGUE</th>
                                            <th>Read</th>
                                            <th>Write</th>
                                            <th>EDIT</th>
                                        </tr>
                                    </thead>
                                    <tbody id="editPermissionTableBody"></tbody>
                                </table>
                                <input type="hidden" id="editRoleIndex">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary" id="updateRoleBtn">Update Role</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Danh sách permissions từ màn hình trước
            const permissions = [
                {name: "Add Product", catalogue: true, read: true, write: false, edit: true},
                {name: "Add Product via upload", catalogue: true, read: false, write: false, edit: true},
                {name: "Complete Drafts", catalogue: true, read: true, write: false, edit: true},
                {name: "View Listing Applications", catalogue: true, read: false, write: false, edit: true},
                {name: "Improve Listing Quality", catalogue: true, read: true, write: false, edit: true},
                {name: "Upload & Manage Videos", catalogue: true, read: true, write: false, edit: true},
                {name: "Manage Product Videos", catalogue: true, read: true, write: false, edit: true}
            ];

            // Danh sách roles (mẫu ban đầu)
            let roles = [
                { name: "Admin", permissions: permissions.map(p => ({ ...p })) },
                { name: "Editor", permissions: permissions.map(p => ({ ...p, write: false, edit: true, catalogue: false, read: true })) }
            ];

            const roleTableBody = document.getElementById('roleTableBody');
            const permissionTableBody = document.getElementById('permissionTableBody');
            const editPermissionTableBody = document.getElementById('editPermissionTableBody');

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

                // Tự động ẩn sau 3 giây
                setTimeout(() => {
                    alertDiv.classList.remove('show');
                    alertDiv.classList.add('fade');
                    setTimeout(() => alertDiv.remove(), 150);
                }, 3000);
            }

            // Hàm hiển thị bảng roles
            function renderRoleTable() {
                roleTableBody.innerHTML = '';
                roles.forEach((role, roleIndex) => {
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
                        catalogueCheckbox.dataset.roleIndex = roleIndex;
                        catalogueCheckbox.dataset.permIndex = permIndex;
                        catalogueCheckbox.dataset.field = 'catalogue';
                        catalogueCell.appendChild(catalogueCheckbox);
                        row.appendChild(catalogueCell);

                        const readCell = document.createElement('td');
                        const readCheckbox = document.createElement('input');
                        readCheckbox.type = 'checkbox';
                        readCheckbox.className = 'form-check-input';
                        readCheckbox.checked = perm.read;
                        readCheckbox.dataset.roleIndex = roleIndex;
                        readCheckbox.dataset.permIndex = permIndex;
                        readCheckbox.dataset.field = 'read';
                        readCell.appendChild(readCheckbox);
                        row.appendChild(readCell);

                        const writeCell = document.createElement('td');
                        const writeCheckbox = document.createElement('input');
                        writeCheckbox.type = 'checkbox';
                        writeCheckbox.className = 'form-check-input';
                        writeCheckbox.checked = perm.write;
                        writeCheckbox.dataset.roleIndex = roleIndex;
                        writeCheckbox.dataset.permIndex = permIndex;
                        writeCheckbox.dataset.field = 'write';
                        writeCell.appendChild(writeCheckbox);
                        row.appendChild(writeCell);

                        const editCell = document.createElement('td');
                        const editCheckbox = document.createElement('input');
                        editCheckbox.type = 'checkbox';
                        editCheckbox.className = 'form-check-input';
                        editCheckbox.checked = perm.edit;
                        editCheckbox.dataset.roleIndex = roleIndex;
                        editCheckbox.dataset.permIndex = permIndex;
                        editCheckbox.dataset.field = 'edit';
                        editCell.appendChild(editCheckbox);
                        row.appendChild(editCell);

                        if (permIndex === 0) {
                            const actionCell = document.createElement('td');
                            actionCell.rowSpan = role.permissions.length;
                            actionCell.innerHTML = `
                                <button class="btn btn-warning btn-sm edit-role-btn" data-role-index="${roleIndex}" data-bs-toggle="modal" data-bs-target="#editRoleModal">Edit</button>
                                <button class="btn btn-danger btn-sm delete-role-btn" data-role-index="${roleIndex}">Delete</button>
                            `;
                            row.appendChild(actionCell);
                        }

                        roleTableBody.appendChild(row);
                    });
                });
            }

            // Hàm hiển thị danh sách permissions trong modal Add Role
            function renderPermissionTable() {
                permissionTableBody.innerHTML = '';
                permissions.forEach((perm, index) => {
                    const row = document.createElement('tr');
                    const permCell = document.createElement('td');
                    permCell.textContent = perm.name;
                    row.appendChild(permCell);

                    const catalogueCell = document.createElement('td');
                    const catalogueCheckbox = document.createElement('input');
                    catalogueCheckbox.type = 'checkbox';
                    catalogueCheckbox.className = 'form-check-input';
                    catalogueCheckbox.checked = perm.catalogue;
                    catalogueCheckbox.dataset.index = index;
                    catalogueCheckbox.dataset.field = 'catalogue';
                    catalogueCell.appendChild(catalogueCheckbox);
                    row.appendChild(catalogueCell);

                    const readCell = document.createElement('td');
                    const readCheckbox = document.createElement('input');
                    readCheckbox.type = 'checkbox';
                    readCheckbox.className = 'form-check-input';
                    readCheckbox.checked = perm.read;
                    readCheckbox.dataset.index = index;
                    readCheckbox.dataset.field = 'read';
                    readCell.appendChild(readCheckbox);
                    row.appendChild(readCell);

                    const writeCell = document.createElement('td');
                    const writeCheckbox = document.createElement('input');
                    writeCheckbox.type = 'checkbox';
                    writeCheckbox.className = 'form-check-input';
                    writeCheckbox.checked = perm.write;
                    writeCheckbox.dataset.index = index;
                    writeCheckbox.dataset.field = 'write';
                    writeCell.appendChild(writeCheckbox);
                    row.appendChild(writeCell);

                    const editCell = document.createElement('td');
                    const editCheckbox = document.createElement('input');
                    editCheckbox.type = 'checkbox';
                    editCheckbox.className = 'form-check-input';
                    editCheckbox.checked = perm.edit;
                    editCheckbox.dataset.index = index;
                    editCheckbox.dataset.field = 'edit';
                    editCell.appendChild(editCheckbox);
                    row.appendChild(editCell);

                    permissionTableBody.appendChild(row);
                });
            }

            // Hàm hiển thị danh sách permissions trong modal Edit Role
            function renderEditPermissionTable(roleIndex) {
                editPermissionTableBody.innerHTML = '';
                roles[roleIndex].permissions.forEach((perm, index) => {
                    const row = document.createElement('tr');
                    const permCell = document.createElement('td');
                    permCell.textContent = perm.name;
                    row.appendChild(permCell);

                    const catalogueCell = document.createElement('td');
                    const catalogueCheckbox = document.createElement('input');
                    catalogueCheckbox.type = 'checkbox';
                    catalogueCheckbox.className = 'form-check-input';
                    catalogueCheckbox.checked = perm.catalogue;
                    catalogueCheckbox.dataset.index = index;
                    catalogueCheckbox.dataset.field = 'catalogue';
                    catalogueCell.appendChild(catalogueCheckbox);
                    row.appendChild(catalogueCell);

                    const readCell = document.createElement('td');
                    const readCheckbox = document.createElement('input');
                    readCheckbox.type = 'checkbox';
                    readCheckbox.className = 'form-check-input';
                    readCheckbox.checked = perm.read;
                    readCheckbox.dataset.index = index;
                    readCheckbox.dataset.field = 'read';
                    readCell.appendChild(readCheckbox);
                    row.appendChild(readCell);

                    const writeCell = document.createElement('td');
                    const writeCheckbox = document.createElement('input');
                    writeCheckbox.type = 'checkbox';
                    writeCheckbox.className = 'form-check-input';
                    writeCheckbox.checked = perm.write;
                    writeCheckbox.dataset.index = index;
                    writeCheckbox.dataset.field = 'write';
                    writeCell.appendChild(writeCheckbox);
                    row.appendChild(writeCell);

                    const editCell = document.createElement('td');
                    const editCheckbox = document.createElement('input');
                    editCheckbox.type = 'checkbox';
                    editCheckbox.className = 'form-check-input';
                    editCheckbox.checked = perm.edit;
                    editCheckbox.dataset.index = index;
                    editCheckbox.dataset.field = 'edit';
                    editCell.appendChild(editCheckbox);
                    row.appendChild(editCell);

                    editPermissionTableBody.appendChild(row);
                });
            }

            // Hiển thị bảng ban đầu
            renderRoleTable();
            renderPermissionTable();

            // Submit button event listener
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.addEventListener('click', () => {
                const checkboxes = document.querySelectorAll('#roleTableBody input[type="checkbox"][data-role-index]');
                checkboxes.forEach(checkbox => {
                    const roleIndex = checkbox.dataset.roleIndex;
                    const permIndex = checkbox.dataset.permIndex;
                    const field = checkbox.dataset.field;
                    roles[roleIndex].permissions[permIndex][field] = checkbox.checked;
                });
                console.log('Updated roles:', roles);
                showBootstrapAlert('Roles updated successfully!', 'success');
            });

            // Add Role Modal - Form submission handler
            const addRoleForm = document.getElementById('addRoleForm');
            addRoleForm.addEventListener('submit', (e) => {
                e.preventDefault();

                const roleNameInput = document.getElementById('roleName');
                const roleName = roleNameInput.value.trim();

                // Validate role name
                if (roleName === '') {
                    roleNameInput.classList.add('is-invalid');
                    return;
                } else {
                    roleNameInput.classList.remove('is-invalid');
                    roleNameInput.classList.add('is-valid');
                }

                // Lấy giá trị permissions từ modal
                const newPermissions = [];
                const rows = permissionTableBody.querySelectorAll('tr');
                rows.forEach((row, index) => {
                    const catalogueCheck = row.querySelector('input[data-field="catalogue"]').checked;
                    const readCheck = row.querySelector('input[data-field="read"]').checked;
                    const writeCheck = row.querySelector('input[data-field="write"]').checked;
                    const editCheck = row.querySelector('input[data-field="edit"]').checked;

                    newPermissions.push({
                        name: permissions[index].name,
                        catalogue: catalogueCheck,
                        read: readCheck,
                        write: writeCheck,
                        edit: editCheck
                    });
                });

                // Thêm role mới
                roles.push({
                    name: roleName,
                    permissions: newPermissions
                });

                // Re-render bảng
                renderRoleTable();

                // Clear modal inputs
                roleNameInput.value = '';
                roleNameInput.classList.remove('is-valid');
                renderPermissionTable();

                // Đóng modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('addRoleModal'));
                modal.hide();

                showBootstrapAlert('New role added successfully!', 'success');
            });

            // Edit Role Modal - Populate data and handle submission
            document.addEventListener('click', (e) => {
                if (e.target.classList.contains('edit-role-btn')) {
                    const roleIndex = e.target.dataset.roleIndex;
                    document.getElementById('editRoleIndex').value = roleIndex;
                    document.getElementById('editRoleName').value = roles[roleIndex].name;
                    renderEditPermissionTable(roleIndex);
                }
            });

            const editRoleForm = document.getElementById('editRoleForm');
            editRoleForm.addEventListener('submit', (e) => {
                e.preventDefault();

                const roleIndex = document.getElementById('editRoleIndex').value;
                const roleNameInput = document.getElementById('editRoleName');
                const roleName = roleNameInput.value.trim();

                // Validate role name
                if (roleName === '') {
                    roleNameInput.classList.add('is-invalid');
                    return;
                } else {
                    roleNameInput.classList.remove('is-invalid');
                    roleNameInput.classList.add('is-valid');
                }

                // Cập nhật permissions từ modal
                const updatedPermissions = [];
                const rows = editPermissionTableBody.querySelectorAll('tr');
                rows.forEach((row, index) => {
                    const catalogueCheck = row.querySelector('input[data-field="catalogue"]').checked;
                    const readCheck = row.querySelector('input[data-field="read"]').checked;
                    const writeCheck = row.querySelector('input[data-field="write"]').checked;
                    const editCheck = row.querySelector('input[data-field="edit"]').checked;

                    updatedPermissions.push({
                        name: roles[roleIndex].permissions[index].name,
                        catalogue: catalogueCheck,
                        read: readCheck,
                        write: writeCheck,
                        edit: editCheck
                    });
                });

                // Cập nhật role
                roles[roleIndex].name = roleName;
                roles[roleIndex].permissions = updatedPermissions;

                // Re-render bảng
                renderRoleTable();

                // Đóng modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('editRoleModal'));
                modal.hide();

                showBootstrapAlert('Role updated successfully!', 'success');
            });

            // Delete Role
            document.addEventListener('click', (e) => {
                if (e.target.classList.contains('delete-role-btn')) {
                    const roleIndex = e.target.dataset.roleIndex;
                    if (confirm('Are you sure you want to delete this role?')) {
                        roles.splice(roleIndex, 1);
                        renderRoleTable();
                        showBootstrapAlert('Role deleted successfully!', 'danger');
                    }
                }
            });

            // Clear validation feedback on input
            document.getElementById('roleName').addEventListener('input', function() {
                if (this.value.trim() !== '') {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                }
            });

            document.getElementById('editRoleName').addEventListener('input', function() {
                if (this.value.trim() !== '') {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                }
            });
        </script>
    </body>
</html>