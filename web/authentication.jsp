<%--
    Document   : test
    Created on : May 20, 2025, 11:56:54 PM
    Author     : PC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Permission</title>
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
            .invalid-feedback {
                display: none;
            }
            .is-invalid ~ .invalid-feedback {
                display: block;
            }
            /* Kéo rộng nút Previous */
            #permissionsTable_paginate .paginate_button.previous {
                /* Cho nút hiển thị dưới dạng inline-block để width có tác dụng */
                display: inline-block;
                /* Đặt chiều rộng tối thiểu */
                min-width: 100px;
                /* Căn giữa text */
                text-align: center;
                /* Tăng padding nếu muốn */
                padding: 0.4em 1em;
            }

            /* (Tuỳ chọn) Đồng bộ style cho tất cả paginate_button */
            #permissionsTable_paginate .paginate_button {
                padding: 0.4em 1em;
            }
        </style>
    </head>
    <body>
        <%@ include file="sidebar.jsp" %>
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container mt-4">

                <!-- Nút mở modal -->
                <button id="addPermissionBtn"
                        class="btn btn-success"
                        data-bs-toggle="modal"
                        data-bs-target="#addPermissionModal">
                    Add Permission
                </button>

                <!-- Modal for Adding Permission -->
                <div class="modal fade" id="addPermissionModal" tabindex="-1"
                     aria-labelledby="addPermissionModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form id="addPermissionForm"
                                  action="${pageContext.request.contextPath}/permission"
                                  method="post">
                                <!-- Nếu cần userId để lấy roleId -->
                                <input type="hidden" name="userId" value="${sessionScope.userId}" />

                                <div class="modal-header">
                                    <h5 class="modal-title" id="addPermissionModalLabel">
                                        Add New Resource & Permission
                                    </h5>
                                    <button type="button" class="btn-close"
                                            data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <div class="modal-body">
                                    <!-- Người dùng nhập tên resource mới -->
                                    <div class="mb-3">
                                        <label for="resourceName" class="form-label">
                                            Resource Name
                                        </label>
                                        <input type="text"
                                               class="form-control"
                                               id="resourceName"
                                               name="resourceName"
                                               placeholder="Enter new resource name"
                                               required>
                                    </div>

                                    <!-- Các quyền cho resource -->
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox"
                                               id="canCreate" name="canCreate" />
                                        <label class="form-check-label" for="canCreate">
                                            Can Create
                                        </label>
                                    </div>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox"
                                               id="canRead" name="canRead" />
                                        <label class="form-check-label" for="canRead">
                                            Can Read
                                        </label>
                                    </div>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox"
                                               id="canUpdate" name="canUpdate" />
                                        <label class="form-check-label" for="canUpdate">
                                            Can Update
                                        </label>
                                    </div>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox"
                                               id="canDelete" name="canDelete" />
                                        <label class="form-check-label" for="canDelete">
                                            Can Delete
                                        </label>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">
                                        Save Permission
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="container mt-4">
                    <form action="permission" method="post">
                        <table id="permissionsTable" class="table table-bordered display responsive nowrap" style="width:100%">
                            <thead class="table-header">
                                <tr>
                                    <th>Name</th>
                                    <th>Create</th>
                                    <th>Read</th>
                                    <th>Update</th>
                                    <th>Delete</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${permissionsList}">
                                    <tr>
                                        <td>
                                            ${p.resourceName}
                                            <!-- giữ lại id để servlet biết đang cập nhật bản ghi nào -->
                                            <input type="hidden" name="permId" value="${p.id}" />
                                            <input type="hidden" name="resourceId" value="${p.resourceId}" />
                                        </td>
                                        <td>
                                            <input type="checkbox" name="canCreate" value="${p.id}"
                                                   <c:if test="${p.canCreate}">checked</c:if> />
                                            </td>
                                            <td>
                                                <input type="checkbox" name="canRead" value="${p.id}"
                                                   <c:if test="${p.canRead}">checked</c:if> />
                                            </td>
                                            <td>
                                                <input type="checkbox" name="canUpdate" value="${p.id}"
                                                   <c:if test="${p.canUpdate}">checked</c:if> />
                                            </td>
                                            <td>
                                                <input type="checkbox" name="canDelete" value="${p.id}"
                                                   <c:if test="${p.canDelete}">checked</c:if> />
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/deletepermission" onsubmit="return confirm('Are you sure you want to delete this permission?');">
                                                <input type="hidden" name="permId" value="${p.id}" />
                                                <button type="submit" class="btn btn-link text-danger" style="padding:0; border:none;">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="d-flex justify-content-end mt-3">
                            <button type="submit" class="btn btn-primary">Save Change</button>
                        </div>
                    </form>
                </div>
            </div>

        </section>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="vendors/datatable/js/jquery.dataTables.min.js"></script>
        <script src="vendors/datatable/js/dataTables.responsive.min.js"></script>

        <script>
                                                    $(function () {
                                                        // 1) Initialize DataTable
                                                        $('#permissionsTable').DataTable({
                                                            paging: true,
                                                            pageLength: 5,
                                                            lengthChange: false,
                                                            searching: false,
                                                            info: false,
                                                            responsive: true
                                                        });

                                                        // 2) AJAX Add Permission (giữ nguyên)
                                                        const url = '${pageContext.request.contextPath}/addpermission';
                                                        const modalEl = document.getElementById('addPermissionModal');
                                                        const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);

                                                        $('#addPermissionForm').on('submit', function (e) {
                                                            e.preventDefault();
                                                            const data = {
                                                                resourceName: $('#resourceName').val().trim(),
                                                                description: $('#description').val().trim(),
                                                                roleId: $('#roleId').val(),
                                                                canCreate: $('#canCreate').is(':checked'),
                                                                canRead: $('#canRead').is(':checked'),
                                                                canUpdate: $('#canUpdate').is(':checked'),
                                                                canDelete: $('#canDelete').is(':checked')
                                                            };
                                                            $.post(url, data, function (resp) {
                                                                if (!resp.success) {
                                                                    return alert('Error: ' + resp.error);
                                                                }
                                                                // reload page
                                                                window.location.reload();
                                                            }, 'json')
                                                                    .fail(function (xhr) {
                                                                        console.error('AJAX error:', xhr.responseText);
                                                                        alert('Có lỗi khi thêm permission mới');
                                                                    });
                                                        });
                                                    });
        </script>


    </body>
</html>