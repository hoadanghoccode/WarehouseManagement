<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Role Assignment</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
             <link rel="stylesheet" type="text/css" href="css/permissionlist.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
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
        <link rel="stylesheet"six href="vendors/material_icon/material-icons.css" />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <!-- Custom CSS -->
        <style>
            .user-list {
                height: 100vh;
                overflow-y: auto;
                border-right: 1px solid #dee2e6;
            }
            .user-item {
                cursor: pointer;
                padding: 10px;
                border-bottom: 1px solid #f1f1f1;
            }
            .user-item:hover {
                background-color: #f8f9fa;
            }
            .user-item.active {
                background-color: #e9ecef;
                font-weight: bold;
            }
            .role-list {
                max-height: 400px;
                overflow-y: auto;
            }
            .role-item {
                padding: 10px;
                border-bottom: 1px solid #f1f1f1;
                cursor: pointer;
            }
            .permission-table th, .permission-table td {
                text-align: center;
                vertical-align: middle;
            }
            .permission-table {
                display: none;
                margin-top: 10px;
            }
            .role-item.active + .permission-table {
                display: table;
            }
        </style>
    </head>
    <body>
        <%--<%@ include file="sidebar.jsp" %>--%>
        <jsp:include page="sidebar.jsp" flush="true"/>
        <!-- … đã include sidebar & navbar … -->
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container-fluid">
                <div class="row">
                    <!-- Left Side: User List -->
                    <div class="col-md-3 user-list p-3">
                        <h5>Users</h5>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="searchInput" placeholder="Search by Employee ID">
                        </div>
                        <div id="userList">
                            <!-- Sample User Data -->
                            <div class="user-item" data-id="1" data-name="John Doe" data-employee-id="EMP001">John Doe (EMP001)</div>
                            <div class="user-item" data-id="2" data-name="Jane Smith" data-employee-id="EMP002">Jane Smith (EMP002)</div>
                            <div class="user-item" data-id="3" data-name="Emily Johnson" data-employee-id="EMP003">Emily Johnson (EMP003)</div>
                            <div class="user-item" data-id="4" data-name="Michael Brown" data-employee-id="EMP004">Michael Brown (EMP004)</div>
                            <div class="user-item" data-id="5" data-name="Sarah Wilson" data-employee-id="EMP005">Sarah Wilson (EMP005)</div>
                            <div class="user-item" data-id="6" data-name="David Lee" data-employee-id="EMP006">David Lee (EMP006)</div>
                            <div class="user-item" data-id="7" data-name="Laura Clark" data-employee-id="EMP007">Laura Clark (EMP007)</div>
                            <div class="user-item" data-id="8" data-name="James Hall" data-employee-id="EMP008">James Hall (EMP008)</div>
                        </div>
                    </div>

                    <!-- Right Side: User Details and Role Assignment -->
                    <div class="col-md-9 p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 id="userDetailsTitle">User Role Assignment</h5>
                            <div>
                                <button class="btn btn-outline-secondary me-2" id="cancelBtn">Cancel</button>
                                <button class="btn btn-primary" id="saveBtn">Save</button>
                            </div>
                        </div>
                        <div id="userDetails" class="mb-4">
                            <!-- User details will be populated here -->
                        </div>
                        <h6>Assign Roles</h6>
                        <div class="role-list border rounded p-2">
                            <!-- Admin Role -->
                            <div class="role-item">
                                <input type="checkbox" id="roleAdmin" data-role="Admin">
                                <label for="roleAdmin" class="ms-2">Admin</label>
                            </div>
                            <table class="table table-bordered permission-table">
                                <thead>
                                    <tr>
                                        <th>CATALOGUE</th>
                                        <th>Read</th>
                                        <th>Write</th>
                                        <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Add Product</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Add Product via upload</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Complete Drafts</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>View Listing Applications</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Improve Listing Quality</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Upload & Manage Videos</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Manage Product Videos</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- Editor Role -->
                            <div class="role-item">
                                <input type="checkbox" id="roleEditor" data-role="Editor">
                                <label for="roleEditor" class="ms-2">Editor</label>
                            </div>
                            <table class="table table-bordered permission-table">
                                <thead>
                                    <tr>
                                        <th>CATALOGUE</th>
                                        <th>Read</th>
                                        <th>Write</th>
                                        <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Add Product</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Add Product via upload</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Complete Drafts</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>View Listing Applications</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Improve Listing Quality</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Upload & Manage Videos</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Manage Product Videos</td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" checked disabled></td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- Viewer Role -->
                            <div class="role-item">
                                <input type="checkbox" id="roleViewer" data-role="Viewer">
                                <label for="roleViewer" class="ms-2">Viewer</label>
                            </div>
                            <table class="table table-bordered permission-table">
                                <thead>
                                    <tr>
                                        <th>CATALOGUE</th>
                                        <th>Read</th>
                                        <th>Write</th>
                                        <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Add Product</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Add Product via upload</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Complete Drafts</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>
                                    <tr>
                                        <td>View Listing Applications</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Improve Listing Quality</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>
                                    <tr>
                                        <td>Upload & Manage Videos</td>
                                        <td><input type="checkbox" checked disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                        <td><input type="checkbox" disabled></td>
                                    </tr>

                                <div class="role-item">
                                    <input type="checkbox" id="roleViewer" data-role="Viewer">
                                    <label for="roleViewer" class="ms-2">Viewer</label>
                                </div>
                                <table class="table table-bordered permission-table">
                                    <thead>
                                        <tr>
                                            <th>CATALOGUE</th>
                                            <th>Read</th>
                                            <th>Write</th>
                                            <th>Edit</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Add Product</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>Add Product via upload</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>Complete Drafts</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>View Listing Applications</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>Improve Listing Quality</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>Upload & Manage Videos</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                        <tr>
                                            <td>Manage Product Videos</td>
                                            <td><input type="checkbox" checked disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                            <td><input type="checkbox" disabled></td>
                                        </tr>
                                    </tbody>
                                </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Bootstrap 5 JS and Popper.js -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Custom JS -->
        <script>
            // Sample user role data (for demo purposes)
            const userRolesData = {
                1: {name: "John Doe", employeeId: "EMP001", roles: ["Admin", "Editor"]},
                2: {name: "Jane Smith", employeeId: "EMP002", roles: ["Editor"]},
                3: {name: "Emily Johnson", employeeId: "EMP003", roles: ["Viewer"]},
                4: {name: "Michael Brown", employeeId: "EMP004", roles: []},
                5: {name: "Sarah Wilson", employeeId: "EMP005", roles: ["Admin"]},
                6: {name: "David Lee", employeeId: "EMP006", roles: ["Editor", "Viewer"]},
                7: {name: "Laura Clark", employeeId: "EMP007", roles: []},
                8: {name: "James Hall", employeeId: "EMP008", roles: ["Viewer"]}
            };

            // Search functionality
            document.getElementById('searchInput').addEventListener('input', function (e) {
                const searchTerm = e.target.value.toLowerCase();
                const userItems = document.querySelectorAll('.user-item');
                userItems.forEach(item => {
                    const employeeId = item.getAttribute('data-employee-id').toLowerCase();
                    if (employeeId.includes(searchTerm)) {
                        item.style.display = 'block';
                    } else {
                        item.style.display = 'none';
                    }
                });
            });

            // Handle user click
            document.querySelectorAll('.user-item').forEach(item => {
                item.addEventListener('click', function () {
                    // Highlight selected user
                    document.querySelectorAll('.user-item').forEach(i => i.classList.remove('active'));
                    this.classList.add('active');

                    const userId = this.getAttribute('data-id');
                    const userData = userRolesData[userId] || {};

                    // Update user details
                    document.getElementById('userDetailsTitle').innerText = userData.name || "User Role Assignment";
                    document.getElementById('userDetails').innerHTML = `
                        <p><strong>Name:</strong> ${userData.name || ''}</p>
                        <p><strong>Employee ID:</strong> ${userData.employeeId || ''}</p>
                    `;

                    // Update role checkboxes
                    document.querySelectorAll('.role-item input[type="checkbox"]').forEach(checkbox => {
                        const role = checkbox.getAttribute('data-role');
                        checkbox.checked = (userData.roles || []).includes(role);
                    });
                });
            });

            // Handle role click to show/hide permissions table
            document.querySelectorAll('.role-item').forEach(item => {
                item.addEventListener('click', function (e) {
                    // Prevent checkbox click from triggering this
                    if (e.target.type === 'checkbox')
                        return;

                    // Toggle active class for role item
                    document.querySelectorAll('.role-item').forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });

            // Save button (placeholder functionality)
            document.getElementById('saveBtn').addEventListener('click', function () {
                const selectedUser = document.querySelector('.user-item.active');
                if (!selectedUser) {
                    alert('Please select a user first!');
                    return;
                }
                const userId = selectedUser.getAttribute('data-id');
                const selectedRoles = [];
                document.querySelectorAll('.role-item input[type="checkbox"]:checked').forEach(checkbox => {
                    selectedRoles.push(checkbox.getAttribute('data-role'));
                });
                alert(`Roles assigned to user ${userId}: ${selectedRoles.join(', ')} (This is a demo)`);
            });

            // Cancel button (placeholder functionality)
            document.getElementById('cancelBtn').addEventListener('click', function () {
                alert('Changes discarded! (This is a demo)');
            });

            // Trigger click on first user by default
            document.querySelector('.user-item').click();
        </script>
    </body>
</html>