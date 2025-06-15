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
                        <c:if test="${perms['Role_ADD']}">

                            <button id="addRoleBtn" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addRoleModal">Add Role</button>
                        </c:if>
                    </div>
                </div>


                <table class="table table-bordered">
                    <thead class="table-header">
                        <tr>
                            <th>Role Name</th>
                            <th>Permission</th>
                            <th>Create</th>
                            <th>Read</th>
                            <th>Update</th>
                            <th>Delete</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="roleTableBody"></tbody>
                </table>
                 <c:if test="${perms['Role_UPDATE']}"> 
                <div class="row mb-3">
                    <div class="col d-flex justify-content-end">
                        <button id="submitBtn" class="btn btn-primary">Submit</button>
                    </div>
                </div>
                </c:if>
            </div>

            <!-- Modal for Adding Role -->
            <div class="modal fade" id="addRoleModal" tabindex="-1" aria-labelledby="addRoleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form id="addRoleForm" novalidate>
                            <div class="modal-header">
                                <h5 class="modal-title" id="addRoleModalLabel">Add New Role</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>

                            <div class="modal-body">
                                <!-- Role Name -->
                                <div class="mb-3">
                                    <label for="roleName" class="form-label">Role Name</label>
                                    <input
                                        type="text"
                                        class="form-control"
                                        id="roleName"
                                        name="name"
                                        placeholder="Enter role name"
                                        required
                                        >
                                    <div class="invalid-feedback">
                                        Please enter correct role
                                    </div>
                                </div>

                                <!-- Description -->
                                <div class="mb-3">
                                    <label for="roleDescription" class="form-label">Description</label>
                                    <textarea
                                        class="form-control"
                                        id="roleDescription"
                                        name="description"
                                        rows="3"
                                        placeholder="Enter description"
                                        required
                                        ></textarea>
                                    <div class="invalid-feedback">
                                        Please enter description
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary" id="saveRoleBtn">Save Role</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Modal Confirm -->
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



        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            const ctx = '${pageContext.request.contextPath}';
            // 1) transform từ mảng phẳng dao về format của renderRoleTable
            function transformDataToRoles(data) {
                const map = {};
                data.forEach(rp => {
                    if (!map[rp.roleId]) {
                        map[rp.roleId] = {
                            roleId: rp.roleId,
                            name: rp.role,
                            permissions: []
                        };
                    }
                    // chỉ thêm khi có resource
                    if (rp.resources) {
                        rp.resources.forEach(res => {
                            map[rp.roleId].permissions.push({
                                resourceId: res.resourceId,
                                name: res.resourceName || '',
                                canAdd: res.canAdd,
                                canView: res.canView,
                                canUpdate: res.canUpdate,
                                canDelete: res.canDelete
                            });
                        });
                    } else if (rp.resourceName) {

                        // nếu endpoint trả flat, fallback
                        map[rp.roleId].permissions.push({
                            resourceId: res.resourceId,
                            name: rp.resourceName,
                            canAdd: rp.canAdd,
                            canView: rp.canView,
                            canUpdate: rp.canUpdate,
                            canDelete: rp.canDelete
                        });
                    }
                });
                return Object.values(map);
            }

            // 2) fetch + render
            fetch('${pageContext.request.contextPath}/permissionrole/data')
                    .then(r => r.json())
                    .then(json => {
                        // nếu endpoint trả nested RoleData, gán trực tiếp
                        if (json.length && json[0].resources !== undefined) {
                            console.log('data here', json);
                            roles = json.map(r => ({
                                    roleId: r.roleId,
                                    name: r.role,
                                    permissions: r.resources.map(res => ({
                                            resourceId: res.resourceId,
                                            name: res.resourceName,
                                            canAdd: res.canAdd,
                                            canView: res.canView,
                                            canUpdate: res.canUpdate,
                                            canDelete: res.canDelete
                                        }))
                                }));
                        } else {
                            // ngược lại endpoint trả flat RolePermission
                            roles = transformDataToRoles(json);
                        }
                        renderRoleTable();
                    })
                    .catch(console.error);

            let roleToDeleteIndex = null;
            // Hàm render table chính

            function renderRoleTable() {
                const tbody = document.getElementById('roleTableBody');
                tbody.innerHTML = '';
                roles.forEach((role, i) => {
                    role.permissions.forEach((perm, j) => {
                        const tr = document.createElement('tr');

                        if (j === 0) {
                            const td = document.createElement('td');
                            td.textContent = role.name;
                            td.rowSpan = role.permissions.length;
                            tr.appendChild(td);
                        }

                        ['name', 'canAdd', 'canView', 'canUpdate', 'canDelete'].forEach(field => {
                            const td = document.createElement('td');
                            if (field === 'name') {
                                td.textContent = perm.name;
                            } else {
                                const cb = document.createElement('input');
                                cb.type = 'checkbox';
                                cb.className = 'form-check-input';
                                cb.checked = perm[field];
                                cb.dataset.roleIndex = i;
                                cb.dataset.permIndex = j;
                                cb.dataset.field = field;
                                td.appendChild(cb);
                            }
                            tr.appendChild(td);
                        });

                        if (j === 0) {
                            console.log('Rendering role with ID:', role.roleId, 'Full role object:', role);
                            const tdBtn = document.createElement('td');
                            tdBtn.rowSpan = role.permissions.length;
                            const deleteBtn = document.createElement('button');
                            deleteBtn.className = 'btn btn-danger btn-sm delete-role-btn';
                            deleteBtn.textContent = 'Delete';
                            deleteBtn.setAttribute('data-role-id', role.roleId);

                            // Thêm trực tiếp event listener vào button
                            deleteBtn.addEventListener('click', (e) => {
                                e.preventDefault();
                                console.log('Delete button clicked');
                                console.log('Role ID:', role.roleId);
                                roleIdToDelete = role.roleId;
                                confirmModal.show();
                            });

                            tdBtn.appendChild(deleteBtn);
                            tr.appendChild(tdBtn);
                        }
                        tbody.appendChild(tr);
                    });
                });
            }


            // Sự kiện cập nhật data khi nhấn Submit
            document.getElementById('submitBtn').addEventListener('click', () => {
                // 1) Cập nhật lại trạng thái checkbox vào mảng roles
                document
                        .querySelectorAll('#roleTableBody input[type="checkbox"]')
                        .forEach(cb => {
                            const i = +cb.dataset.roleIndex;
                            const j = +cb.dataset.permIndex;
                            const f = cb.dataset.field;
                            roles[i].permissions[j][f] = cb.checked;
                        });

                // 2) Gửi AJAX POST về server
                fetch(`${pageContext.request.contextPath}/permissionrole`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(roles)
                })
                        .then(res => {
                            if (!res.ok)
                                throw new Error(`HTTP ${res.status}`);
                            return res.json();
                        })
                        .then(payload => {
                            // giả định server trả { status: 'ok' } hoặc tương tự
                            if (payload.status === 'ok') {
                                console.log('Cập nhật thành công!', 'success');
                            } else {
                                console.log('Cập nhật thất bại: ' + (payload.message || ''), 'danger');
                            }
                        })
                        .catch(err => {

                            console.log('Có lỗi khi cập nhật.', 'danger');
                        });
            });

        </script>

        <script>
            document.addEventListener('DOMContentLoaded', () => {
                const form = document.getElementById('addRoleForm');
                const modalEl = document.getElementById('addRoleModal');
                const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);

                form.addEventListener('submit', async e => {
                    e.preventDefault();
                    // bật validation của Bootstrap
                    if (!form.checkValidity()) {
                        form.classList.add('was-validated');
                        return;
                    }

                    // chuẩn bị dữ liệu
                    const formData = new FormData(form);
                    const params = new URLSearchParams(formData);

                    try {
                        console.log('Form data:', Object.fromEntries(formData));
                        console.log('Request URL:', `${pageContext.request.contextPath}/addrole`);
                        
                        const resp = await fetch(`${pageContext.request.contextPath}/addrole`, {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            body: params
                        });
                        
                        console.log('Response status:', resp.status);
                        const result = await resp.json();
                        console.log('Response data:', result);

                        if (result.success) {
                            // đóng modal
                            bsModal.hide();
                            // reload lại trang hoặc chỉ thêm row mới vào bảng
                            window.location.reload();
                        } else {
                            alert('Error: ' + (result.message || 'Không thể thêm role'));
                        }
                    } catch (err) {
                        console.error(err);
                        alert('Lỗi kết nối, vui lòng thử lại.');
                    }
                });
            });
        </script>

        <script>
            // Khởi tạo biến và modal ở phạm vi global
            let roleIdToDelete = null;
            let confirmModal = null;

            // Đợi DOM load xong
            document.addEventListener('DOMContentLoaded', () => {
                // Khởi tạo modal
                const confirmModalEl = document.getElementById('confirmDeleteModal');
                confirmModal = new bootstrap.Modal(confirmModalEl);

                // Xử lý nút confirm trong modal
                document.getElementById('confirmDeleteBtn').addEventListener('click', async () => {
                    try {
                        console.log('Confirming delete for roleId:', roleIdToDelete);
                        if (!roleIdToDelete) {
                            console.log('No role ID to delete');
                            return;
                        }

                        // Tạo URLSearchParams (application/x-www-form-urlencoded)
                        const params = new URLSearchParams();
                        params.append('roleId', roleIdToDelete);

                        console.log('Params being sent:', params.toString()); // ví dụ "roleId=4"

                        const resp = await fetch(`${pageContext.request.contextPath}/deleterole`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                            },
                            body: params.toString()
                        });


                        // Refresh lại dữ liệu
                        const response = await fetch(`${pageContext.request.contextPath}/permissionrole/data`);

                        const result = await resp.json();
                        console.log('Response from server:', result);

                        if (result.status === 'ok') {
                            confirmModal.hide();
                            // Refresh lại dữ liệu
                            const response = await fetch(`${pageContext.request.contextPath}/permissionrole/data`);
                            const data = await response.json();
                            console.log('New data after delete:', data);
                            window.location.reload();
                            // … render lại bảng …
                        } else {
                            throw new Error(result.msg || 'Lỗi không xác định');
                        }

                    } catch (err) {
                        console.error('Error deleting role:', err);
                        const alertContainer = document.getElementById('alertContainer');
                        alertContainer.innerHTML = `
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                Có lỗi xảy ra khi xóa role: ${err.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
                    } finally {
                        roleIdToDelete = null;
                    }
                });

            });
        </script>



    </body>
</html>