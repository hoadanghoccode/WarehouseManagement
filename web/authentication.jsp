<%-- 
    Document   : test
    Created on : May 20, 2025, 11:56:54 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <div class="row mb-3">
                <div class="col">
                    <nav class="nav nav-pills">
                        <a class="nav-link active" href="#">CATALOGUE</a>
                        <a class="nav-link" href="#">Read</a>
                        <a class="nav-link" href="#">Write</a>
                        <a class="nav-link" href="#">EDIT</a>
                    </nav>
                </div>
            </div>
            <table class="table table-bordered">
                <thead class="table-header">
                    <tr>
                        <th>Name</th>
                        <th>CATALOGUE</th>
                        <th>Read</th>
                        <th>Write</th>
                        <th>EDIT</th>
                    </tr>
                </thead>
                <tbody id="tableBody"></tbody>
            </table>
            <div class="row mt-3">
                <div class="col">
                    <button id="addPermissionBtn" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addPermissionModal">Add Permission</button>
                    <button id="submitBtn" class="btn btn-primary">Submit</button>
                </div>
            </div>
        </div>

        <!-- Modal for Adding Permission -->
        <div class="modal fade" id="addPermissionModal" tabindex="-1" aria-labelledby="addPermissionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addPermissionModalLabel">Add New Permission</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="permissionName" class="form-label">Permission Name</label>
                            <input type="text" class="form-control" id="permissionName" placeholder="Enter permission name">
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="catalogueCheck">
                            <label class="form-check-label" for="catalogueCheck">Catalogue</label>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="readCheck">
                            <label class="form-check-label" for="readCheck">Read</label>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="writeCheck">
                            <label class="form-check-label" for="writeCheck">Write</label>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="editCheck">
                            <label class="form-check-label" for="editCheck">Edit</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="savePermissionBtn">Save Permission</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            let features = [
                { name: "Add Product", catalogue: true, read: true, write: false, edit: true },
                { name: "Add Product via upload", catalogue: true, read: false, write: false, edit: true },
                { name: "Complete Drafts", catalogue: true, read: true, write: false, edit: true },
                { name: "View Listing Applications", catalogue: true, read: false, write: false, edit: true },
                { name: "Improve Listing Quality", catalogue: true, read: true, write: false, edit: true },
                { name: "Upload & Manage Videos", catalogue: true, read: true, write: false, edit: true },
                { name: "Manage Product Videos", catalogue: true, read: true, write: false, edit: true }
            ];

            const tableBody = document.getElementById('tableBody');
            if (!tableBody) {
                console.error('Table body element not found!');
            } else {
                renderTable();
            }

            function renderTable() {
                tableBody.innerHTML = ''; // Clear existing rows
                features.forEach((feature, index) => {
                    const row = document.createElement('tr');

                    // Name column
                    const nameCell = document.createElement('td');
                    nameCell.textContent = feature.name || 'Unnamed';
                    row.appendChild(nameCell);

                    // Catalogue checkbox
                    const catalogueCell = document.createElement('td');
                    const catalogueCheckbox = document.createElement('input');
                    catalogueCheckbox.type = 'checkbox';
                    catalogueCheckbox.className = 'form-check-input';
                    catalogueCheckbox.checked = feature.catalogue;
                    catalogueCheckbox.dataset.index = index;
                    catalogueCheckbox.dataset.field = 'catalogue';
                    catalogueCell.appendChild(catalogueCheckbox);
                    row.appendChild(catalogueCell);

                    // Read checkbox
                    const readCell = document.createElement('td');
                    const readCheckbox = document.createElement('input');
                    readCheckbox.type = 'checkbox';
                    readCheckbox.className = 'form-check-input';
                    readCheckbox.checked = feature.read;
                    readCheckbox.dataset.index = index;
                    readCheckbox.dataset.field = 'read';
                    readCell.appendChild(readCheckbox);
                    row.appendChild(readCell);

                    // Write checkbox
                    const writeCell = document.createElement('td');
                    const writeCheckbox = document.createElement('input');
                    writeCheckbox.type = 'checkbox';
                    writeCheckbox.className = 'form-check-input';
                    writeCheckbox.checked = feature.write;
                    writeCheckbox.dataset.index = index;
                    writeCheckbox.dataset.field = 'write';
                    writeCell.appendChild(writeCheckbox);
                    row.appendChild(writeCell);

                    // Edit checkbox
                    const editCell = document.createElement('td');
                    const editCheckbox = document.createElement('input');
                    editCheckbox.type = 'checkbox';
                    editCheckbox.className = 'form-check-input';
                    editCheckbox.checked = feature.edit;
                    editCheckbox.dataset.index = index;
                    editCheckbox.dataset.field = 'edit';
                    editCell.appendChild(editCheckbox);
                    row.appendChild(editCell);

                    tableBody.appendChild(row);
                });
            }

            // Submit button event listener
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.addEventListener('click', () => {
                const checkboxes = document.querySelectorAll('input[type="checkbox"][data-index]');
                checkboxes.forEach(checkbox => {
                    const index = checkbox.dataset.index;
                    const field = checkbox.dataset.field;
                    features[index][field] = checkbox.checked;
                });
                console.log('Updated features:', features);
                alert('Features updated! Check the console for the updated array.');
            });

            // Add Permission Modal - Save button event listener
            const savePermissionBtn = document.getElementById('savePermissionBtn');
            savePermissionBtn.addEventListener('click', () => {
                const permissionName = document.getElementById('permissionName').value.trim();
                const catalogueCheck = document.getElementById('catalogueCheck').checked;
                const readCheck = document.getElementById('readCheck').checked;
                const writeCheck = document.getElementById('writeCheck').checked;
                const editCheck = document.getElementById('editCheck').checked;

                if (!permissionName) {
                    alert('Please enter a permission name!');
                    return;
                }

                // Add new permission to features array
                features.push({
                    name: permissionName,
                    catalogue: catalogueCheck,
                    read: readCheck,
                    write: writeCheck,
                    edit: editCheck
                });

                // Re-render the table
                renderTable();

                // Clear modal inputs
                document.getElementById('permissionName').value = '';
                document.getElementById('catalogueCheck').checked = false;
                document.getElementById('readCheck').checked = false;
                document.getElementById('writeCheck').checked = false;
                document.getElementById('editCheck').checked = false;

                // Close the modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('addPermissionModal'));
                modal.hide();

                alert('New permission added successfully!');
            });
        </script>
    </body>
</html>