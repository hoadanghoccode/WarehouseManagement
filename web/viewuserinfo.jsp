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


                    <% String[][] fields = {
                            {"Full Name", profileUser.getFullName(), "Edit Name", "fullName"},
                            {"Email", profileUser.getEmail(), "Edit Email", "email"},
                            {"Phone", profileUser.getPhoneNumber(), "Edit Phone Number", "phoneNumber"},
                            {"Address", profileUser.getAddress(), "Edit Address", "address"},
                            {"Date of Birth", (profileUser.getDateOfBirth() != null ? profileUser.getDateOfBirth().toString().substring(0, 10) : ""), "Edit Date of Birth", "dateOfBirth"},
                            {"Gender", (profileUser.isGender() ? "Male" : "Female"), "Edit Gender", "gender"}
                        }; %>

                    <form id="updateForm" method="post" action="updateuserinfo" enctype="multipart/form-data">
                        <div class="info-row">
                            <div class="info-label">Profile Image:</div>
                            <div class="info-value">
                                <% if (profileUser.getImage() != null && !profileUser.getImage().isEmpty()) {%>
                                <img class="profile-img" src="<%= profileUser.getImage()%>" style="height:80px;" id="profilePreview">
                                <% } else { %>
                                <span id="profilePreview">No image uploaded</span>
                                <% } %>
                                <input type="file" name="imageFile" accept="image/*" class="form-control mt-2 d-none" onchange="previewProfileImage(event)" id="imageInput">
                            </div>
                            <div class="dropdown">
                                <button type="button" class="dropdown-toggle" onclick="toggleImageEdit(this)"><i class="fas fa-ellipsis-h"></i></button>
                            </div>
                        </div>

                        <% for (String[] field : fields) {
                                String label = field[0];
                                String value = field[1];
                                String key = field[3];
                        %>
                        <div class="info-row">
                            <div class="info-label"><%= label%>:</div>
                            <div class="info-value">
                                <span id="<%= key%>Display"><%= value%></span>
                                <% if (!"email".equals(key)) { %>
                                <% if ("gender".equals(key)) {%>
                                <div id="<%= key%>Edit" class="d-none">
                                    <label><input type="radio" name="gender" value="true" <%= "Male".equals(value) ? "checked" : ""%>> Male</label>
                                    <label class="ms-3"><input type="radio" name="gender" value="false" <%= "Female".equals(value) ? "checked" : ""%>> Female</label>
                                </div>
                                <% } else if ("dateOfBirth".equals(key)) {%>
                                <input type="date" name="<%= key%>" value="<%= value%>" id="<%= key%>Edit" class="form-control d-inline w-auto d-none" />
                                <% } else {%>
                                <input type="text" name="<%= key%>" value="<%= value%>" id="<%= key%>Edit" class="form-control d-inline w-50 d-none" />
                                <% } %>
                                <% } %>
                            </div>

                            <% if (!"email".equals(key)) {%>
                            <div class="dropdown">
                                <button type="button" class="dropdown-toggle" onclick="toggleEdit('<%= key%>', this)">
                                    <i class="fas fa-ellipsis-h"></i>
                                </button>
                            </div>
                            <% } %>
                        </div>
                        <% }%>

                        <div class="info-row">
                            <button type="submit" class="btn btn-primary">Save All Changes</button>
                        </div>
                    </form>

                    <script>
                        function toggleEdit(field, button) {
                            const display = document.getElementById(field + "Display");
                            const edit = document.getElementById(field + "Edit");
                            if (display && edit) {
                                display.classList.add("d-none");
                                edit.classList.remove("d-none");
                            }
                            const menu = button?.nextElementSibling;
                            if (menu?.classList.contains("show"))
                                menu.classList.remove("show");
                        }

                        function toggleImageEdit(button) {
                            const input = document.getElementById("imageInput");
                            if (input) {
                                input.classList.toggle("d-none");
                            }
                            const menu = button?.nextElementSibling;
                            if (menu?.classList.contains("show"))
                                menu.classList.remove("show");
                        }

                        function previewProfileImage(event) {
                            const file = event.target.files[0];
                            const preview = document.getElementById("profilePreview");
                            if (file && file.type.startsWith("image/")) {
                                const reader = new FileReader();
                                reader.onload = function (e) {
                                    if (preview.tagName.toLowerCase() === 'img') {
                                        preview.src = e.target.result;
                                    } else {
                                        preview.outerHTML = `<img class="profile-img" id="profilePreview" src="${e.target.result}" style="height:80px;" />`;
                                    }
                                };
                                reader.readAsDataURL(file);
                            }
                        }
                    </script>


