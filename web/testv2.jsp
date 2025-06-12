<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create User</title>
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
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#userModal">
            Create New User
        </button>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="userModal" tabindex="-1" aria-labelledby="userModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="userModalLabel">Create New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullName" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="gender" class="form-label">Gender</label>
                            <select class="form-select" id="gender" required>
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="phoneNumber" class="form-label">Phone Number</label>
                            <input type="tel" class="form-control" id="phoneNumber" required>
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <textarea class="form-control" id="address" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="dateOfBirth" class="form-label">Date of Birth</label>
                            <input type="date" class="form-control" id="dateOfBirth" required>
                        </div>
                        <div class="mb-3">
                            <label for="branch" class="form-label">Branch</label>
                            <select class="form-select" id="branch" required>
                                <option value="">Select Branch</option>
                                <option value="Hanoi">Hanoi</option>
                                <option value="Ho Chi Minh">Ho Chi Minh</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="department" class="form-label">Department</label>
                            <select class="form-select" id="department" onchange="updatePermissionMatrix()" required>
                                <option value="">Select Department</option>
                                <option value="Sales">Sales</option>
                                <option value="Marketing">Marketing</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role</label>
                            <select class="form-select" id="role" required>
                                <option value="">Select Role</option>
                                <option value="Admin">Admin</option>
                                <option value="Editor">Editor</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="group" class="form-label">Group</label>
                            <select class="form-select" id="group" required>
                                <option value="">Select Group</option>
                                <option value="Group A">Group A</option>
                                <option value="Group B">Group B</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" required>
                                <option value="">Select Status</option>
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
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
            const department = document.getElementById('department').value;
            const matrixBody = document.getElementById('permissionMatrixBody');
            const matrixContainer = document.getElementById('permissionMatrix');
            matrixBody.innerHTML = '';

            if (department) {
                matrixContainer.style.display = 'block';
                // Mapping departments to roles
                const departmentRoles = {
                    'Sales': 'Admin',
                    'Marketing': 'Editor'
                };
                const role = departmentRoles[department] || '';

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

                if (role && permissions[role]) {
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
                }
            } else {
                matrixContainer.style.display = 'none';
            }
        }
    </script>
</body>
</html>