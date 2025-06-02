<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="form-container">
    <div class="header">
        <div>
            <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                 alt="User Avatar" class="avatar" onclick="document.getElementById('fileInput').click()">
            <h2>${user.fullName}</h2>
        </div>
        <button class="close-btn" onclick="closeModal('userDetailModal')">×</button>
    </div>
    <form action="${pageContext.request.contextPath}/userdetail" method="post" enctype="multipart/form-data">
        <input type="hidden" name="userId" value="${user.userId}" />
        <input type="hidden" name="email" value="${user.email}" />
        <input type="hidden" name="userDepartmentId" value="${userDepartmentId}" />
        <input type="file" id="fileInput" name="imageFile" style="display:none;" onchange="this.form.submit()"/>
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
        <div class="grid-container">
            <div class="form-group">
                <label for="image">Image URL</label>
                <input type="text" name="image" id="image" value="${user.image}" readonly />
            </div>
            <div class="form-group">
                <label>Updated At</label>
                <p class="text-gray-800">
                    <fmt:formatDate value="${user.updatedAt}" pattern="yyyy-MM-dd" />
                </p>
            </div>
        </div>
        <div class="form-actions">
            <input type="submit" value="Update User" />
            <button type="button" onclick="closeModal('userDetailModal')">Cancel</button>
        </div>
    </form>
</div>