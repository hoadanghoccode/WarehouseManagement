<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    @SuppressWarnings("unchecked")
    Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (perms == null) {
        perms = new HashMap<>();
    }        
    request.setAttribute("perms", perms);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User List</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <link rel="icon" href="img/logo.png" type="image/png">
        <!-- themefy CSS -->
        <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
        <!-- swiper slider CSS -->
        <link rel="stylesheet" href="vendors/swiper_slider/css/swiper.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
        <!-- nice-select CSS -->
        <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
        <!-- owl carousel CSS -->
        <link rel="stylesheet" href="vendors/owl_carousel/css/owl.carousel.css" />
        <!-- gijgo CSS -->
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
        <!-- text editor CSS -->
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <!-- morris CSS -->
        <link rel="stylesheet" href="vendors/morris/morris.css">
        <!-- material icon CSS -->
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
        <!-- menu CSS -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userList.css?v=1" />
    </head>
    <body>
        <div class="main-layout">
            <%@ include file="sidebar.jsp" %>
            <section class="main_content">
                <%@ include file="navbar.jsp" %>
                <div class="container">
                    <div class="header">
                        <h1 class="title">Users</h1>
                        <div class="header-actions">
                            <c:if test="${perms['Customer_ADD']}"> 
                                <button class="btn btn-primary" onclick="toggleCreateForm()">
                                    <i class="fas fa-plus"></i>
                                    Create User
                                </button>
                            </c:if>
                        </div>
                    </div>
                    <div class="search-container">
                        <form id="filterForm" method="get" action="${pageContext.request.contextPath}/userlist" style="display: flex; gap: 12px; align-items: center;">
                            <div style="position: relative;">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" name="search" value="${fn:escapeXml(search)}" placeholder="Search by name, email, phone..." class="search-input form-control">
                            </div>
                            <select name="departmentId" class="form-select">
                                <option value="">All Departments</option>
                                <c:forEach var="department" items="${departments}">
                                    <option value="${department.departmentId}" ${department.departmentId == selectedDepartmentId ? 'selected' : ''}>${department.name}</option>
                                </c:forEach>
                            </select>
                            <select name="roleId" class="form-select">
                                <option value="">All Roles</option>
                                <c:forEach var="role" items="${roles}">
                                    <option value="${role.roleId}" ${role.roleId == selectedRoleId ? 'selected' : ''}>${role.name}</option>
                                </c:forEach>
                            </select>
                            <select name="status" class="form-select">
                                <option value="">All Status</option>
                                <option value="true" ${selectedStatus == 'true' ? 'selected' : ''}>Active</option>
                                <option value="false" ${selectedStatus == 'false' ? 'selected' : ''}>Inactive</option>
                            </select>
                            <button type="submit" class="btn btn-primary">Filter</button>
                        </form>
                    </div>
                    <div class="stats-info">
                        <i class="fas fa-users"></i> Showing <strong>${fn:length(users)}</strong> of <strong>${totalUsers}</strong> users
                    </div>
                    <c:if test="${not empty sessionScope.success}">
                        <div class="success-message">
                            ${sessionScope.success}
                            <button class="dismiss-btn" onclick="dismissNotification()">×</button>
                        </div>
                        <c:remove var="success" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.error}">
                        <div class="error-message">
                            ${sessionScope.error}
                            <button class="dismiss-btn" onclick="dismissNotification()">×</button>
                        </div>
                        <c:remove var="error" scope="session" />
                    </c:if>
                    <div class="table-container">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th style="width:60px;">Avatar</th>
                                    <th>Email</th>
                                    <th>Name</th>
                                    <th>Gender</th>
                                    <th>Phone</th>
                                    <th>Address</th>
                                    <th>Department</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty users}">
                                    <tr>
                                        <td colspan="9" class="no-data">
                                            <i class="fas fa-inbox no-data-icon"></i>
                                            <div>No users found</div>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:forEach var="user" items="${users}">
                                    <tr>
                                        <td>
                                            <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                                                 alt="User Avatar" class="avatar">
                                        </td>
                                        <td title="${user.email}">${fn:escapeXml(user.email)}</td>
                                        <td>
                                            <a href="javascript:void(0)" onclick="openUserDetailModal(${user.userId})" class="category-name" title="${user.fullName}">${fn:escapeXml(user.fullName)}</a>
                                        </td>
                                        <td>${user.gender ? 'Male' : 'Female'}</td>
                                        <td title="${user.phoneNumber}">${fn:escapeXml(user.phoneNumber)}</td>
                                        <td title="${user.address}">${fn:escapeXml(user.address)}</td>
                                        <td title="${not empty user.departmentName ? user.departmentName : ''}">${not empty user.departmentName ? fn:escapeXml(user.departmentName) : 'None'}</td>
                                        <td title="${user.roleName}">${fn:escapeXml(user.roleName)}</td>
                                        <td>
                                            <span class="badge ${user.status ? 'bg-success' : 'bg-danger'}">${user.status ? 'Active' : 'Inactive'}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:choose>
                                <c:when test="${currentPage > 1}">
                                    <a href="${pageContext.request.contextPath}/userlist?page=1&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}" title="First page">
                                        <i class="fas fa-angle-double-left"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/userlist?page=${currentPage - 1}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}" title="Previous page">
                                        <i class="fas fa-angle-left"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="disabled" title="First page">
                                        <i class="fas fa-angle-double-left"></i>
                                    </span>
                                    <span class="disabled" title="Previous page">
                                        <i class="fas fa-angle-left"></i>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${totalPages <= 7}">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <c:choose>
                                            <c:when test="${i == currentPage}">
                                                <span class="current">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${currentPage <= 4}">
                                            <c:forEach begin="1" end="5" var="i">
                                                <c:choose>
                                                    <c:when test="${i == currentPage}">
                                                        <span class="current">${i}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${i}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                            <c:if test="${totalPages > 6}">
                                                <span style="padding: 8px 4px;">...</span>
                                                <a href="${pageContext.request.contextPath}/userlist?page=${totalPages}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${totalPages}</a>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${currentPage >= totalPages - 3}">
                                            <a href="${pageContext.request.contextPath}/userlist?page=1&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">1</a>
                                            <c:if test="${totalPages > 6}">
                                                <span style="padding: 8px 4px;">...</span>
                                            </c:if>
                                            <c:forEach begin="${totalPages - 4}" end="${totalPages}" var="i">
                                                <c:choose>
                                                    <c:when test="${i == currentPage}">
                                                        <span class="current">${i}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${i}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/userlist?page=1&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">1</a>
                                            <span style="padding: 8px 4px;">...</span>
                                            <c:forEach begin="${currentPage - 2}" end="${currentPage + 2}" var="i">
                                                <c:choose>
                                                    <c:when test="${i == currentPage}">
                                                        <span class="current">${i}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/userlist?page=${i}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${i}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                            <span style="padding: 8px 4px;">...</span>
                                            <a href="${pageContext.request.contextPath}/userlist?page=${totalPages}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}">${totalPages}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${currentPage < totalPages}">
                                    <a href="${pageContext.request.contextPath}/userlist?page=${currentPage + 1}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}" title="Next page">
                                        <i class="fas fa-angle-right"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/userlist?page=${totalPages}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&roleId=${selectedRoleId}&status=${selectedStatus}" title="Last page">
                                        <i class="fas fa-angle-double-right"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="disabled" title="Next page">
                                        <i class="fas fa-angle-right"></i>
                                    </span>
                                    <span class="disabled" title="Last page">
                                        <i class="fas fa-angle-double-right"></i>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div style="text-align: center; margin-top: 16px; color: #6b7280; font-size: 14px;">
                            Page ${currentPage} of ${totalPages} (${totalUsers} total users)
                        </div>
                    </c:if>
                    <!-- Create User Modal -->
                    <div id="createUserModal" class="modal">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h2 class="modal-title">Create New User</h2>
                                <button class="close-btn" onclick="closeModal('createUserModal')">×</button>
                            </div>
                            <div id="createUserAlert" style="display:none;" class="field-error"></div>
                            <form id="createUserForm">
                                <div class="form-group">
                                    <label for="fullName">Full Name</label>
                                    <input type="text" name="fullName" id="fullName" required />
                                    <span class="field-error" id="error-fullName"></span>
                                </div>
                                <div class="form-group">
                                    <label for="email">Email</label>
                                    <input type="email" name="email" id="email" required />
                                    <span class="field-error" id="error-email"></span>
                                </div>
                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password" name="password" id="password" required />
                                    <span class="field-error" id="error-password"></span>
                                </div>
                                <div class="form-group">
                                    <label for="gender">Gender</label>
                                    <select name="gender" id="gender" required>
                                        <option value="1">Male</option>
                                        <option value="0">Female</option>
                                    </select>
                                    <span class="field-error" id="error-gender"></span>
                                </div>
                                <div class="form-group">
                                    <label for="phoneNumber">Phone Number</label>
                                    <input type="text" name="phoneNumber" id="phoneNumber" />
                                    <span class="field-error" id="error-phoneNumber"></span>
                                </div>
                                <div class="form-group">
                                    <label for="address">Address</label>
                                    <input type="text" name="address" id="address" />
                                    <span class="field-error" id="error-address"></span>
                                </div>
                                <div class="form-group">
                                    <label for="dateOfBirth">Date of Birth</label>
                                    <input type="date" name="dateOfBirth" id="dateOfBirth" required />
                                    <span class="field-error" id="error-dateOfBirth"></span>
                                </div>
                                <div class="form-group">
                                    <label for="departmentId">Department</label>
                                    <select name="departmentId" id="departmentId" required>
                                        <c:forEach var="department" items="${departments}">
                                            <option value="${department.departmentId}">${department.name}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="field-error" id="error-departmentId"></span>
                                </div>
                                <div class="form-group">
                                    <label for="status">Status</label>
                                    <select name="status" id="status" required>
                                        <option value="true">Active</option>
                                        <option value="false">Inactive</option>
                                    </select>
                                    <span class="field-error" id="error-status"></span>
                                </div>
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">Create User</button>
                                    <button type="button" onclick="closeModal('createUserModal')">Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!-- User Detail Modal -->
                    <div id="userDetailModal" class="modal">
                        <div class="modal-content">
                            <!-- Content will be loaded dynamically via JavaScript -->
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            function toggleCreateForm() {
                var modal = document.getElementById("createUserModal");
                modal.style.display = "flex";
                var form = modal.querySelector("form");
                if (form) form.reset();
                setTimeout(function () {
                    var firstInput = modal.querySelector("input, select, textarea");
                    if (firstInput) firstInput.focus();
                }, 100);
            }
            function closeModal(modalId) {
                var modal = document.getElementById(modalId);
                modal.style.display = "none";
            }
            function openUserDetailModal(userId) {
                const modal = document.getElementById("userDetailModal");
                const modalContent = modal.querySelector(".modal-content");
                modalContent.innerHTML = '<div>Loading...</div>';
                fetch('${pageContext.request.contextPath}/userdetail?id=' + userId)
                    .then(response => {
                        if (!response.ok) throw new Error('Network response was not ok');
                        return response.text();
                    })
                    .then(html => {
                        modalContent.innerHTML = html;
                        modal.style.display = "flex";
                        initializeDepartmentOptions();
                        const avatar = modalContent.querySelector('#avatar');
                        const fileInput = modalContent.querySelector('#fileInput');
                        if (avatar && fileInput) {
                            avatar.onclick = () => fileInput.click();
                            fileInput.onchange = (e) => {
                                const file = e.target.files[0];
                                if (file && file.type.startsWith('image/')) {
                                    const reader = new FileReader();
                                    reader.onload = function (evt) {
                                        avatar.src = evt.target.result;
                                    };
                                    reader.readAsDataURL(file);
                                } else {
                                    alert('Please select a valid image file.');
                                    e.target.value = '';
                                }
                            };
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching user details:', error);
                        modalContent.innerHTML = '<div class="error-message">Error loading user details: ' + error.message + '</div>';
                        modal.style.display = "flex";
                    });
            }
            function initializeDepartmentOptions() {
                const modalContent = document.querySelector('#userDetailModal .modal-content');
                const roleSelect = modalContent.querySelector("#roleId");
                const departmentSelect = modalContent.querySelector("#departmentId");
                if (!roleSelect || !departmentSelect) {
                    console.error("Role or Department select not found");
                    return;
                }
                function updateDepartmentOptions() {
                    const roleId = roleSelect.value;
                    departmentSelect.innerHTML = '<option value="">Select a department</option>';
                    if (roleId) {
                        fetch('${pageContext.request.contextPath}/userdetail?action=getDepartmentsByRole&roleId=' + roleId)
                            .then(response => {
                                if (!response.ok) throw new Error('Network response was not ok');
                                return response.json();
                            })
                            .then(data => {
                                if (data.error) {
                                    departmentSelect.innerHTML = '<option value="">Error: ' + data.error + '</option>';
                                    return;
                                }
                                if (data.length === 0) {
                                    departmentSelect.innerHTML = '<option value="">No departments available</option>';
                                    return;
                                }
                                const userDepartmentId = modalContent.querySelector('input[name="userDepartmentId"]').value;
                                data.forEach(department => {
                                    const option = document.createElement("option");
                                    option.value = department.departmentId;
                                    option.textContent = department.name;
                                    if (userDepartmentId && department.departmentId == userDepartmentId) {
                                        option.selected = true;
                                    }
                                    departmentSelect.appendChild(option);
                                });
                            })
                            .catch(error => {
                                console.error('Error fetching departments:', error);
                                departmentSelect.innerHTML = '<option value="">Error loading departments</option>';
                            });
                    }
                }
                updateDepartmentOptions();
                roleSelect.addEventListener("change", updateDepartmentOptions);
            }
            window.onclick = function (event) {
                var createModal = document.getElementById("createUserModal");
                var detailModal = document.getElementById("userDetailModal");
                if (event.target == createModal) createModal.style.display = "none";
                if (event.target == detailModal) detailModal.style.display = "none";
            }
            function dismissNotification() {
                window.location.href = "${pageContext.request.contextPath}/userlist?page=${currentPage}&search=${fn:escapeXml(search)}&departmentId=${selectedDepartmentId}&status=${selectedStatus}";
            }
        </script>
        <script>
            $(document).ready(function () {
                $('#createUserForm').on('submit', function (e) {
                    e.preventDefault();
                    const $form = $(this);
                    const $alert = $('#createUserAlert');
                    $alert.hide().removeClass('error-message success-message').text('');
                    $('.field-error').text('');
                    $.ajax({
                        url: '${pageContext.request.contextPath}/createuser',
                        method: 'POST',
                        data: $form.serialize(),
                        success: function (res) {
                            $alert.addClass('success-message').text(res.message || "Tạo user thành công!").fadeIn();
                            setTimeout(() => {
                                closeModal('createUserModal');
                                location.reload();
                            }, 1000);
                        },
                        error: function (xhr) {
                            try {
                                const res = JSON.parse(xhr.responseText);
                                if (res.field && res.error) {
                                    $('#error-' + res.field).text(res.error);
                                } else if (res.error) {
                                    $alert.addClass('error-message').text(res.error).fadeIn();
                                }
                            } catch (e) {
                                $alert.addClass('error-message').text("Có lỗi xảy ra.").fadeIn();
                            }
                        }
                    });
                });
            });
        </script>
    </body>
</html>