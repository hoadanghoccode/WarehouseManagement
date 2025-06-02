<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Department Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .permission-matrix {
            margin-top: 20px;
        }
        .permission-matrix table {
            width: 100%;
            border-collapse: collapse;
        }
        .permission-matrix th, .permission-matrix td {
            border: 1px solid #dee2e6;
            padding: 8px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#departmentModal">
            Add Department
        </button>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="departmentModal" tabindex="-1" aria-labelledby="departmentModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="departmentModalLabel">Add New Department</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <label for="departmentName" class="form-label">Department Name</label>
                            <input type="text" class="form-control" id="departmentName" required>
                        </div>
                        <div class="mb-3">
                            <label for="departmentDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="departmentDescription" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="departmentRole" class="form-label">Select Role</label>
                            <select class="form-select" id="departmentRole" onchange="updatePermissionMatrix()">
                                <option value="">Select a role</option>
                                <option value="Admin">Admin</option>
                                <option value="Editor">Editor</option>
                            </select>
                        </div>
                        <div id="permissionMatrix" class="permission-matrix" style="display: none;">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Role Name</th>
                                        <th>Permission</th>
                                        <th>CATALOGUE</th>
                                        <th>Read</th>
                                        <th>Write</th>
                                        <th>EDIT</th>
                                    </tr>
                                </thead>
                                <tbody id="permissionMatrixBody">
                                </tbody>
                            </table>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        function updatePermissionMatrix() {
            const role = document.getElementById('departmentRole').value;
            const matrixBody = document.getElementById('permissionMatrixBody');
            const matrixContainer = document.getElementById('permissionMatrix');
            matrixBody.innerHTML = '';

            if (role) {
                matrixContainer.style.display = 'block';
                const permissions = {
                    'Admin': [
                        { perm: 'Add Product', read: true, write: false, edit: true },
                        { perm: 'Add Product via upload', read: false, write: false, edit: true },
                        { perm: 'Complete Drafts', read: true, write: false, edit: true },
                        { perm: 'View Listing Applications', read: false, write: false, edit: true },
                        { perm: 'Improve Listing Quality', read: false, write: false, edit: true },
                        { perm: 'Upload & Manage Videos', read: true, write: false, edit: true },
                        { perm: 'Manage Product Videos', read: true, write: false, edit: true }
                    ],
                    'Editor': [
                        { perm: 'Add Product', read: true, write: false, edit: true },
                        { perm: 'Add Product via upload', read: true, write: false, edit: true },
                        { perm: 'Complete Drafts', read: true, write: false, edit: true },
                        { perm: 'View Listing Applications', read: true, write: false, edit: true },
                        { perm: 'Improve Listing Quality', read: true, write: false, edit: true }
                    ]
                };

                permissions[role].forEach(perm => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${role}</td>
                        <td>${perm.perm}</td>
                        <td>✔</td>
                        <td>${perm.read ? '✔' : '✘'}</td>
                        <td>${perm.write ? '✔' : '✘'}</td>
                        <td>${perm.edit ? '✔' : '✘'}</td>
                    `;
                    matrixBody.appendChild(row);
                });
            } else {
                matrixContainer.style.display = 'none';
            }
        }
    </script>
</body>
</html>