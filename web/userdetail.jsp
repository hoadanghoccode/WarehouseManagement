<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    // Get logged-in user from session
    model.Users loggedInUser = (model.Users) session.getAttribute("USER");
    boolean isEditingSelf = loggedInUser != null && loggedInUser.getUserId() == ((model.Users) request.getAttribute("user")).getUserId();
    request.setAttribute("isEditingSelf", isEditingSelf);
%>

<div class="form-container">
    <div class="header">
        <div>
            <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                 alt="User Avatar" class="avatar" id="avatar" onclick="document.getElementById('fileInput').click();">
            <h2>${user.fullName}</h2>
        </div>
        <button class="close-btn" onclick="closeModal('userDetailModal')">×</button>
    </div>

    <c:choose>
        <c:when test="${isEditingSelf}">
            <form id="userForm" action="${pageContext.request.contextPath}/userdetail" method="post" enctype="multipart/form-data">
                <input type="hidden" name="userId" value="${user.userId}" />
                <input type="hidden" name="email" value="${user.email}" />
                <input type="hidden" name="roleId" value="${user.roleId}" />
                <input type="hidden" name="status" value="${user.status}" />
                <input type="hidden" name="phoneNumber" value="${user.phoneNumber}" />
                <input type="hidden" name="address" value="${user.address}" />
                <input type="hidden" name="dateOfBirth" value="${empty user.dateOfBirth ? '' : user.dateOfBirth}" />
                <input type="file" id="fileInput" name="imageFile" style="display:none;" accept="image/*" onchange="previewImage(event)" />
                <div class="grid-container">
                    <div class="form-group">
                        <label>Email</label>
                        <p class="text-gray-800">${user.email}</p>
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <p class="text-gray-800">${user.status ? 'Active' : 'Inactive'}</p>
                    </div>
                </div>
                <div class="grid-container">
                    <div class="form-group">
                        <label>Date of Birth</label>
                        <p class="text-gray-800">${empty user.dateOfBirth ? 'Not specified' : user.dateOfBirth}</p>
                    </div>
                    <div class="form-group">
                        <label>Role</label>
                        <p class="text-gray-800">
                            <c:forEach var="role" items="${roles}">
                                <c:if test="${role.roleId == user.roleId}">${role.name}</c:if>
                            </c:forEach>
                        </p>
                    </div>
                </div>
                <div class="grid-container">
                    <div class="form-group">
                        <label>Gender</label>
                        <p class="text-gray-800">${user.gender ? 'Male' : 'Female'}</p>
                    </div>
                    <div class="form-group">
                        <label for="departmentId">Department</label>
                        <select name="departmentId" id="departmentId">
                            <option value="">Select a department</option>
                            <c:forEach var="department" items="${departments}">
                                <option value="${department.departmentId}" ${department.departmentId == userDepartmentId ? 'selected' : ''}>${department.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>Phone Number</label>
                    <p class="text-gray-800">${empty user.phoneNumber ? 'Not specified' : user.phoneNumber}</p>
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <p class="text-gray-800">${empty user.address ? 'Not specified' : user.address}</p>
                </div>
                <div class="form-actions">
                    <input type="submit" value="Update Profile" />
                    <button type="button" onclick="closeModal('userDetailModal')">Cancel</button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <c:if test="${not perms['Customer_UPDATE']}">
                <div class="error-message">
                    <p style="color: red;">You do not have permission to edit other users' profiles.</p>
                    <button type="button" onclick="closeModal('userDetailModal')">Close</button>
                </div>
            </c:if>
            <c:if test="${perms['Customer_UPDATE']}">
                <form id="userForm" action="${pageContext.request.contextPath}/userdetail" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="userId" value="${user.userId}" />
                    <input type="hidden" name="email" value="${user.email}" />
                    <input type="hidden" name="userDepartmentId" value="${userDepartmentId}" />
                    <input type="file" id="fileInput" name="imageFile" style="display:none;" accept="image/*" onchange="previewImage(event)" />
                    <div class="grid-container">
                        <div class="form-group">
                            <label>Email</label>
                            <p class="text-gray-800">${user.email}</p>
                        </div>
                        <div class="form-group">
                            <label for="status">Status</label>
                            <select name="status" id="status">
                                <option value="true" ${user.status ? 'selected' : ''}>Active</option>
                                <option value="false" ${!user.status ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="grid-container">
                        <div class="form-group">
                            <label>Date of Birth</label>
                            <p class="text-gray-800">${empty user.dateOfBirth ? 'Not specified' : user.dateOfBirth}</p>
                            <input type="hidden" name="dateOfBirth" value="${empty user.dateOfBirth ? '' : user.dateOfBirth}">
                        </div>
                        <div class="form-group">
                            <label for="roleId">Role</label>
                            <select name="roleId" id="roleId">
                                <c:forEach var="role" items="${roles}">
                                    <option value="${role.roleId}" ${role.roleId == user.roleId ? 'selected' : ''}>${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="grid-container">
                        <div class="form-group">
                            <label>Gender</label>
                            <p class="text-gray-800">${user.gender ? 'Male' : 'Female'}</p>
                        </div>
                        <div class="form-group">
                            <label for="departmentId">Department</label>
                            <select name="departmentId" id="departmentId">
                                <option value="">Select a department</option>
                                <c:forEach var="department" items="${departments}">
                                    <option value="${department.departmentId}" ${department.departmentId == userDepartmentId ? 'selected' : ''}>${department.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phoneNumber">Phone Number</label>
                        <input type="text" name="phoneNumber" id="phoneNumber" value="${user.phoneNumber}" />
                    </div>
                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" name="address" id="address" value="${user.address}" />
                    </div>
                    <div class="form-actions">
                        <input type="submit" value="Update User" />
                        <button type="button" onclick="closeModal('userDetailModal')">Cancel</button>
                    </div>
                </form>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function previewImage(event) {
        const file = event.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('avatar').src = e.target.result;
            };
            reader.readAsDataURL(file);
        } else {
            alert('Please select a valid image file.');
            event.target.value = '';
        }
    }

    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        modal.style.display = 'none';
    }
</script>