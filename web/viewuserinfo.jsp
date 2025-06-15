<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Users" %>
<%@ page session="true" %>

<%
    Users profileUser = (Users) request.getAttribute("userInfo");
    if (profileUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>My Profile</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/style1.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
        <style>
            body {
                margin: 0;
                background-color: #f0f2f5;
                font-family: Arial, sans-serif;
            }
            .main-section {
                display: flex;
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
                gap: 24px;
            }
            .account-settings {
                width: 280px;
                background-color: #fff;
                border-radius: 8px;
                padding: 16px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .account-settings h2 {
                font-size: 18px;
                margin-bottom: 16px;
            }
            .account-settings ul {
                list-style: none;
                padding: 0;
            }
            .account-settings li {
                padding: 10px 12px;
                border-radius: 6px;
                margin-bottom: 8px;
                cursor: pointer;
            }
            .account-settings li.active,
            .account-settings li:hover {
                background-color: #e4e6eb;
                font-weight: bold;
            }
            .profile-box {
                flex: 1;
                background-color: #fff;
                border-radius: 8px;
                padding: 24px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .profile-box h2 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 20px;
            }
            .info-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 12px;
                padding: 12px 0;
                border-bottom: 1px solid #eee;
            }
            .info-label {
                font-weight: 500;
                color: #606770;
                width: 180px;
            }
            .info-value {
                color: #050505;
                flex: 1;
            }
            .dropdown {
                position: relative;
            }
            .dropdown-toggle {
                background: none;
                border: none;
                color: #65676b;
                font-size: 18px;
                cursor: pointer;
            }
            .dropdown-menu {
                position: absolute;
                top: 100%;
                right: 0;
                background-color: #fff;
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 0;
                margin-top: 8px;
                min-width: 220px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.15);
                z-index: 1000;
                display: none;
            }
            .dropdown-menu.show {
                display: block;
            }
            .dropdown-menu a {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 10px 16px;
                text-decoration: none;
                color: #050505;
                font-size: 14px;
            }
            .dropdown-menu a:hover {
                background-color: #f0f2f5;
            }
            img.profile-img {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                object-fit: cover;
                border: 2px solid #ddd;
            }
        </style>
    </head>
    <body>
        <%@ include file="sidebar.jsp" %>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="main-section">
                <div class="account-settings">
                    <h2>Account Setting</h2>
                    <ul>
                        <li class="${page == 'profile' ? 'active' : ''}">
        My Profile
    </li>
    <li class="${page == 'changepassword' ? 'active' : ''}">
        <a href="changepassword" style="text-decoration: none; color: inherit;">Change Password</a>
    </li>
                    </ul>
                </div>
                <div class="profile-box">
                    <h2>My Profile</h2>
                     <!-- Profile Image -->
                    <div class="info-row" id="imageRow">
                        <div class="info-label">Profile Image:</div>
                        <div class="info-value" id="imageText">
                            <% if (profileUser.getImage() != null && !profileUser.getImage().isEmpty()) { %>
                            <img id="profilePreview" class="profile-img" src="uploads/<%= profileUser.getImage() %>" alt="Profile Image" style="height: 80px;" />
                            <% } else { %>
                            <span id="profilePreview">No image uploaded</span>
                            <% } %>
                            <form id="imageForm" class="d-none d-inline">
                                <input type="file" name="value" accept="image/*" onchange="previewImage(event)" class="form-control-file d-inline" />
                                <input type="hidden" name="field" value="image" />
                                <button type="button" class="btn btn-sm btn-success ms-2" onclick="submitImage()">Save</button>
                                <button type="button" onclick="cancelEdit('image')" class="btn btn-sm btn-secondary ms-1">Cancel</button>
                            </form>
                        </div>
                        <div class="dropdown">
                            <button class="dropdown-toggle" onclick="toggleDropdown(event, this)"><i class="fas fa-ellipsis-h"></i></button>
                            <div class="dropdown-menu">
                                <a href="javascript:void(0)" onclick="toggleEditMode('image')"><i class="fas fa-edit"></i> Edit Image</a>
                            </div>
                        </div>
                    </div>


                    <% String[][] fields = {
                        {"Full Name", profileUser.getFullName(), "Edit Name", "fullName"},
                        {"Email", profileUser.getEmail(), "Edit Email", "email"},
                        {"Phone", profileUser.getPhoneNumber(), "Edit Phone Number", "phoneNumber"},
                        {"Address", profileUser.getAddress(), "Edit Address", "address"},
                        {"Date of Birth", (profileUser.getDateOfBirth() != null ? profileUser.getDateOfBirth().toString().substring(0, 10) : "N/A"), "Edit Date of Birth", "dateOfBirth"},
                        {"Gender", (profileUser.isGender() ? "Male" : "Female"), "Edit Gender", "gender"},
                        {"Status", (profileUser.isStatus() ? "Active" : "Inactive"), null, null}
                    }; %>

                    <% for (String[] field : fields) {
                        String label = field[0];
                        String value = field[1];
                        String editLabel = field[2];
                        String key = field[3];
                    %>
                    <div class="info-row" id="<%= key %>Row">
                        <div class="info-label"><%= label %>:</div>
                        <div class="info-value" id="<%= key %>Text">
                            <span id="<%= key %>Display"><%= value %></span>
                            <form id="<%= key %>Form" class="d-none d-inline" onsubmit="submitEdit(event, '<%= key %>')">
                                <% if ("gender".equals(key)) { %>
                                <label><input type="radio" name="genderVal" value="true" <%= "Male".equals(value) ? "checked" : "" %>> Male</label>
                                <label class="ms-3"><input type="radio" name="genderVal" value="false" <%= "Female".equals(value) ? "checked" : "" %>> Female</label>
                                    <% } else if ("dateOfBirth".equals(key)) { %>
                                <input type="date" name="value" value="<%= value %>" class="form-control d-inline w-auto" />
                                <% } else { %>
                                <input type="text" name="value" value="<%= value %>" class="form-control d-inline w-50" />
                                <% } %>
                                <input type="hidden" name="field" value="<%= key %>" />
                                <button type="submit" class="btn btn-sm btn-success ms-2">Save</button>
                                <button type="button" onclick="cancelEdit('<%= key %>', '<%= value.replace("\"", "&quot;").replace("'", "&#39;") %>')" class="btn btn-sm btn-secondary ms-1">Cancel</button>
                            </form>
                        </div>
                        <% if (editLabel != null && key != null) { %>
                        <div class="dropdown">
                            <button class="dropdown-toggle" onclick="toggleDropdown(event, this)"><i class="fas fa-ellipsis-h"></i></button>
                            <div class="dropdown-menu">
                                <a href="javascript:void(0)" onclick="toggleEditMode('<%= key %>')"><i class="fas fa-edit"></i> <%= editLabel %></a>
                            </div>
                        </div>
                        <% } %>
                    </div>
                    <% } %>

                   
                    <script>
                        function toggleDropdown(event, button) {
                            event.stopPropagation();
                            document.querySelectorAll('.dropdown-menu').forEach(menu => {
                                if (menu !== button.nextElementSibling)
                                    menu.classList.remove('show');
                            });
                            button.nextElementSibling.classList.toggle('show');
                        }

                        function toggleEditMode(field) {
                            document.getElementById(field + 'Display')?.classList.add('d-none');
                            document.getElementById(field + 'Form')?.classList.remove('d-none');
                        }

                        function cancelEdit(field, value) {
                            document.getElementById(field + 'Display')?.classList.remove('d-none');
                            document.getElementById(field + 'Form')?.classList.add('d-none');
                        }

                        function submitEdit(e, field) {
                            e.preventDefault();
                            const form = e.target;
                            let value;
                            if (field === 'gender') {
                                const selected = form.querySelector('input[name="genderVal"]:checked');
                                value = selected ? selected.value : '';
                            } else {
                                value = form.querySelector('input[name="value"]').value.trim();
                            }

                            if (!field || !value) {
                                alert('Missing field or value');
                                return;
                            }

                            fetch('updateuserinfo', {
                                method: 'POST',
                                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                                body: 'field=' + encodeURIComponent(field) + '&value=' + encodeURIComponent(value)
                            }).then(res => {
                                if (res.ok) {
                                    const newText = (field === 'gender') ? (value === 'true' ? 'Male' : 'Female') : value;
                                    const displaySpan = document.getElementById(field + 'Display');
                                    if (displaySpan) {
                                        displaySpan.textContent = newText;
                                        displaySpan.classList.remove('d-none');
                                    }
                                    document.getElementById(field + 'Form')?.classList.add('d-none');
                                } else {
                                    alert('Update failed');
                                }
                            }).catch(err => alert('Error: ' + err));

                            cancelEdit(field, value);
                        }

                        function previewImage(event) {
                            const output = document.getElementById('profilePreview');
                            const file = event.target.files[0];
                            if (file && file.type.startsWith('image/')) {
                                const reader = new FileReader();
                                reader.onload = function (e) {
                                    output.outerHTML = `<img id="profilePreview" class="profile-img" src="${e.target.result}" style="height: 80px;" />`;
                                };
                                reader.readAsDataURL(file);
                            }
                        }

                        window.addEventListener('click', () => {
                            document.querySelectorAll('.dropdown-menu').forEach(menu => menu.classList.remove('show'));
                        });

                        function submitImage() {
                            const form = document.getElementById("imageForm");
                            const fileInput = form.querySelector('input[type="file"]');
                            const file = fileInput.files[0];

                            if (!file) {
                                alert("Please select an image.");
                                return;
                            }

                            const formData = new FormData(form);

                            fetch('updateuserinfo', {
                                method: 'POST',
                                body: formData
                            }).then(res => {
                                if (!res.ok)
                                    throw new Error("Upload failed");
                                return res.text();
                            }).then(() => {
                                // Cập nhật hình ảnh mới bằng URL blob
                                const newImageURL = URL.createObjectURL(file);
                                const preview = document.getElementById('profilePreview');

                                if (preview.tagName.toLowerCase() === 'img') {
                                    preview.src = newImageURL;
                                } else {
                                    // Nếu trước đó là span (No image uploaded)
                                    preview.outerHTML = `<img id="profilePreview" class="profile-img" src="${newImageURL}" style="height: 80px;" />`;
                                }

                                form.classList.add('d-none');
                            }).catch(err => {
                                alert('Upload failed: ' + err.message);
                            }
                            );
                        }
                    </script>


