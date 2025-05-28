<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Phân quyền User</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar: Danh sách người dùng và ô tìm kiếm -->
                <div class="col-md-3 border-end vh-100 overflow-auto">
                    <form method="get" action="" class="p-2">
                        <div class="input-group mb-3">
                            <input type="text" name="searchCode" class="form-control" placeholder="Tìm theo mã NV"
                                   value="${param.searchCode}" />
                            <button class="btn btn-outline-secondary" type="submit">Tìm</button>
                        </div>
                    </form>
                    <ul class="list-group list-group-flush">
                        <c:forEach var="user" items="${users}">
                            <li class="list-group-item p-2 <c:if test='${user.id == param.userId}'>active</c:if>">
                                <a href="?userId=${user.id}&amp;searchCode=${param.searchCode}"
                                   class="text-decoration-none <c:if test='${user.id == param.userId}'>text-white</c:if>">
                                    ${user.employeeCode} - ${user.fullName}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <!-- Main: Chi tiết user & ma trận phân quyền -->
                <div class="col-md-9 vh-100 overflow-auto p-4">
                    <c:choose>
                        <!-- Khi đã chọn user -->
                        <c:when test="${not empty selectedUser}">
                            <h4>Thông tin User: <strong>${selectedUser.employeeCode}</strong> - ${selectedUser.fullName}</h4>
                            <!-- Thêm các trường chi tiết khác nếu cần -->

                            <form method="post" action="savePermissions">
                                <input type="hidden" name="userId" value="${selectedUser.id}" />
                                <table class="table table-bordered table-hover mt-4">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Resource</th>
                                            <th>Create</th>
                                            <th>Read</th>
                                            <th>Update</th>
                                            <th>Delete</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="perm" items="${permissions}">
                                            <tr>
                                                <td>${perm.resource.name}</td>
                                                <td class="text-center">
                                                    <input type="checkbox"
                                                           name="perms[${perm.resource.id}].create"
                                                           <c:if test="${perm.canCreate}">checked</c:if> />
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="checkbox"
                                                               name="perms[${perm.resource.id}].view"
                                                        <c:if test="${perm.canView}">checked</c:if> />
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="checkbox"
                                                               name="perms[${perm.resource.id}].update"
                                                        <c:if test="${perm.canUpdate}">checked</c:if> />
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="checkbox"
                                                               name="perms[${perm.resource.id}].delete"
                                                        <c:if test="${perm.canDelete}">checked</c:if> />
                                                    </td>
                                                </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <div class="mt-3">
                                    <button type="submit" class="btn btn-primary me-2">Lưu</button>
                                    <a href="?searchCode=${param.searchCode}" class="btn btn-secondary">Huỷ</a>
                                </div>
                            </form>
                        </c:when>
                        <!-- Khi chưa chọn user -->
                        <c:otherwise>
                            <div class="alert alert-info">Vui lòng chọn một người dùng để xem phân quyền.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

   

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
