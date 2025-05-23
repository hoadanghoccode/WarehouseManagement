<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Detail</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .form-container {
            max-width: 48rem;
        }
        .field-group {
            margin-bottom: 0.75rem;
        }
        .field-group label {
            font-weight: 600;
            color: #4b5563;
            font-size: 0.875rem;
        }
        .field-group input, .field-group select {
            margin-top: 0.125rem;
            border: 1px solid #d1d5db;
            border-radius: 0.375rem;
            padding: 0.375rem 0.75rem;
            width: 100%;
            font-size: 0.875rem;
            color: #1f2937;
        }
        .field-group .checkbox-group {
            margin-top: 0.125rem;
        }
        .field-group .checkbox-group .form-check {
            display: flex;
            align-items: center;
            margin-bottom: 0.25rem;
        }
        .field-group .checkbox-group input[type="checkbox"] {
            margin-right: 0.5rem;
        }
        .field-group input:focus, .field-group select:focus {
            outline: none;
            border-color: #3b82f6;
            box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
        }
        .field-group p {
            color: #1f2937;
            margin-top: 0.125rem;
            font-size: 0.875rem;
        }
        .form-actions {
            display: flex;
            gap: 0.75rem;
            margin-top: 1rem;
        }
        .avatar {
            width: 2.5rem;
            height: 2.5rem;
            border-radius: 50%;
            object-fit: cover;
        }
        .grid-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        .error-message {
            color: #e53935;
            font-size: 0.875rem;
            margin-bottom: 0.75rem;
        }
    </style>
    <script>
        function updateGroupCheckboxes() {
            var roleId = document.getElementById("roleId").value;
            var groupCheckboxesDiv = document.getElementById("groupCheckboxes");
            groupCheckboxesDiv.innerHTML = ''; // Clear existing checkboxes

            if (roleId) {
                fetch('${pageContext.request.contextPath}/userlist?action=getGroups&roleId=' + roleId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.error) {
                            console.error(data.error);
                            return;
                        }
                        const userGroupIds = [<c:forEach var="userGroupId" items="${userGroupIds}" varStatus="loop">${userGroupId}${loop.last ? '' : ','}</c:forEach>];
                        data.forEach(group => {
                            var checkboxDiv = document.createElement("div");
                            checkboxDiv.className = "form-check";
                            var checkbox = document.createElement("input");
                            checkbox.type = "checkbox";
                            checkbox.className = "form-check-input";
                            checkbox.name = "groupIds";
                            checkbox.value = group.groupId;
                            checkbox.id = "group_" + group.groupId;
                            if (userGroupIds.includes(group.groupId)) {
                                checkbox.checked = true;
                            }
                            var label = document.createElement("label");
                            label.className = "form-check-label";
                            label.htmlFor = "group_" + group.groupId;
                            label.textContent = group.name;
                            checkboxDiv.appendChild(checkbox);
                            checkboxDiv.appendChild(label);
                            groupCheckboxesDiv.appendChild(checkboxDiv);
                        });
                    })
                    .catch(error => console.error('Error fetching groups:', error));
            }
        }

        // Call on page load to populate checkboxes based on the current role
        document.addEventListener("DOMContentLoaded", updateGroupCheckboxes);
    </script>
</head>
<body class="bg-gray-100 min-h-screen flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-lg p-6 form-container">
        <div class="flex items-center justify-between mb-4">
            <div class="flex items-center gap-3">
                <img src="${empty user.image || user.image == '' ? 'https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y' : user.image}"
                     alt="User Avatar" class="avatar">
                <h2 class="text-xl font-semibold text-gray-800">User Details</h2>
            </div>
            <a href="${pageContext.request.contextPath}/userlist" class="text-blue-600 hover:underline text-sm">Back to User List</a>
        </div>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="userdetail" method="post">
            <input type="hidden" name="userId" value="${user.userId}" />

            <div class="grid-container">
                <div class="field-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" name="fullName" id="fullName" value="${user.fullName}" required />
                </div>

                <div class="field-group">
                    <label for="email">Email</label>
                    <p class="text-gray-800 font-medium">${user.email} <span class="text-gray-500 text-sm">(Cannot edit)</span></p>
                </div>
            </div>

            <div class="grid-container">
                <div class="field-group">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" value="${user.password}" required />
                </div>

                <div class="field-group">
                    <label for="dateOfBirth">Date of Birth</label>
                    <input type="date" name="dateOfBirth" id="dateOfBirth"
                           value="${not empty user.dateOfBirth ? user.dateOfBirth : ''}" />
                </div>
            </div>

            <div class="grid-container">
                <div class="field-group">
                    <label for="gender">Gender</label>
                    <select name="gender" id="gender">
                        <option value="1" ${user.gender == 1 ? 'selected' : ''}>Male</option>
                        <option value="0" ${user.gender == 0 ? 'selected' : ''}>Female</option>
                    </select>
                </div>

                <div class="field-group">
                    <label for="status">Status</label>
                    <select name="status" id="status">
                        <option value="true" ${user.status ? 'selected' : ''}>Active</option>
                        <option value="false" ${!user.status ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </div>

            <div class="grid-container">
                <div class="field-group">
                    <label for="branchId">Branch</label>
                    <select name="branchId" id="branchId">
                        <c:forEach var="branch" items="${branches}">
                            <option value="${branch.branchId}" ${branch.branchId == user.branchId ? 'selected' : ''}>${branch.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field-group">
                    <label for="roleId">Role</label>
                    <select name="roleId" id="roleId" onchange="updateGroupCheckboxes()">
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.roleId}" ${role.roleId == user.roleId ? 'selected' : ''}>${role.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="field-group">
                <label for="groupIds">Groups</label>
                <div class="checkbox-group" id="groupCheckboxes">
                    <!-- Checkboxes will be populated dynamically via JavaScript -->
                </div>
            </div>

            <div class="field-group">
                <label for="phoneNumber">Phone Number</label>
                <input type="text" name="phoneNumber" id="phoneNumber" value="${user.phoneNumber}" />
            </div>

            <div class="field-group">
                <label for="address">Address</label>
                <input type="text" name="address" id="address" value="${user.address}" />
            </div>

            <div class="grid-container">
                <div class="field-group">
                    <label for="image">Image URL</label>
                    <input type="text" name="image" id="image" value="${user.image}" />
                </div>

                <div class="field-group">
                    <label>Updated At</label>
                    <p class="text-gray-800 font-medium">
                        <fmt:formatDate value="${user.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                        <span class="text-gray-500 text-sm">(Read-only)</span>
                    </p>
                </div>
            </div>

            <div class="form-actions">
                <input type="submit" value="Update User" class="bg-blue-600 text-white px-4 py-1.5 rounded-md hover:bg-blue-700 transition duration-200 text-sm" />
                <a href="${pageContext.request.contextPath}/userlist" class="bg-gray-200 text-gray-800 px-4 py-1.5 rounded-md hover:bg-gray-300 transition duration-200 text-sm">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>