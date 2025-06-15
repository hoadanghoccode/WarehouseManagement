<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    @SuppressWarnings("unchecked")
    Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (perms == null) {
        perms = new HashMap<>();
    }        
    // Set attribute để có thể truy cập trong JSP
    request.setAttribute("perms", perms);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Supplier Management</title>
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
    </head>
    <body>
        <%--<%@ include file="sidebar.jsp" %>--%>
        <jsp:include page="sidebar.jsp" flush="true"/>
        <!-- … đã include sidebar & navbar … -->
        <section class="main_content dashboard_part">
            <%@ include file="navbar.jsp" %>
            <div class="container">
                <!-- Bootstrap Alert Container -->
                <div class="bootstrap-alert-container" id="alertContainer"></div>
                <!-- Header + Search + Add -->
                <div class="header d-flex align-items-center justify-content-between" style="gap: 24px;">
                    <h1 class="title mb-0">Supplier List</h1>
                    <div class="header-actions d-flex align-items-center" style="gap: 12px;">
                        <form action="${pageContext.request.contextPath}/supplier" method="get" class="search-form d-flex align-items-center" style="gap: 8px; margin-bottom: 0;">
                            <div class="input-group" style="min-width: 180px;">
                                <span class="input-group-text bg-white">

                                    <span class="search-icon">🔍</span>

                                </span>
                                <input type="text" name="search" value="${search}"
                                       placeholder="Search supplier..." class="search-input form-control border-start-0"/>
                            </div>
                            <select name="status" class="form-select me-2" style="min-width: 100px;">
                                <option value="all" ${status == 'all' || empty status ? 'selected' : ''}>All</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                            <button type="submit" class="btn btn-primary">Filter</button>
                        </form>
                        <c:if test="${perms['Supplier_ADD']}">
                            <button id="addPermissionBtn"
                                    class="btn btn-success"
                                    data-bs-toggle="modal"
                                    data-bs-target="#addPermissionModal">
                                Add Supplier
                            </button>
                        </c:if>
                    </div>
                </div>




                <!-- Modal for Adding Supplier -->
                <div class="modal fade" id="addPermissionModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form id="addPermissionForm" action="${pageContext.request.contextPath}/supplier">
                                <div class="modal-header">
                                    <h5 class="modal-title">Add New Supplier</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="resourceName" class="form-label">Supplier Name</label>
                                        <input type="text" id="resourceName" name="name"
                                               class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="status" class="form-label">Status</label>
                                        <select id="status" name="status" class="form-select" required>
                                            <option value="active">Active</option>
                                            <option value="inactive">Inactive</option>
                                        </select>
                                    </div>
                                    <div id="addError" class="text-danger" style="display:none;"></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Edit Supplier Modal -->
                <div class="modal fade" id="editSupplierModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form id="editSupplierForm" action="${pageContext.request.contextPath}/updatesupplier">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" id="editSupplierId" name="supplierId">
                                <div class="modal-header">
                                    <h5 class="modal-title">Edit Supplier</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="editSupplierName" class="form-label">Supplier Name</label>
                                        <input type="text" id="editSupplierName" name="name"
                                               class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="editStatus" class="form-label">Status</label>
                                        <select id="editStatus" name="status" class="form-select" required>
                                            <option value="active">Active</option>
                                            <option value="inactive">Inactive</option>
                                        </select>
                                    </div>
                                    <div id="editError" class="text-danger" style="display:none;"></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Stats -->
                <c:if test="${not empty supplierList}">
                    <div class="stats-info">
                        <i class="fas fa-info-circle"></i>
                        <c:choose>
                            <c:when test="${not empty search}">
                                Found <strong>${totalSuppliers}</strong> for "<strong>${search}</strong>"
                            </c:when>
                            <c:otherwise>
                                Showing <strong>${supplierList.size()}</strong>
                                / <strong>${totalSuppliers}</strong> resources
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>



                <!-- Table -->
                <c:choose>
                    <c:when test="${not empty supplierList}">
                        <div class="table-container">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Status</th>
                                            <c:if test="${perms['Supplier_DELETE']}">

                                            <th>Update</th>
                                            </c:if>
                                            <c:if test="${perms['Supplier_UPDATE']}">
                                            <th>Action</th>
                                            </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="res" items="${supplierList}" varStatus="st">
                                        <tr>
                                            <td><strong>${(page-1)*pageSize + st.index + 1}</strong></td>
                                            <td>${res.name}</td>
                                            <td>
                                                <span class="status-label badge ${res.status == 'active' ? 'bg-success' : 'bg-secondary'}"
                                                      data-id="${res.supplierId}">
                                                    ${res.status}
                                                </span>
                                            </td>
                                            <c:if test="${perms['Supplier_DELETE']}">
                                                <td class="action-buttons">
                                                    <form class="delete-resource-form" style="display:inline;">
                                                        <div class="form-check form-switch">
                                                            <input class="form-check-input toggle-status" type="checkbox"
                                                                   data-id="${res.supplierId}"
                                                                   ${res.status == 'active' ? 'checked' : ''}>
                                                        </div>

                                                    </form>

                                                </td>
                                            </c:if>
                                            <c:if test="${perms['Supplier_UPDATE']}">
                                                <td>
                                                    <a href="#" class="btn btn-primary btn-sm edit-supplier" 
                                                       data-id="${res.supplierId}"
                                                       data-name="${res.name}"
                                                       data-status="${res.status}"
                                                       data-bs-toggle="modal" 
                                                       data-bs-target="#editSupplierModal">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                </td>
                                            </c:if>

                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-data">
                            <i class="fas fa-folder-open fa-3x"></i>
                            <h3>No supplier found</h3>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Pagination -->
                <c:if test="${numPages > 1}">
                    <div class="pagination">
                        <!-- First / Prev -->
                        <c:url var="firstUrl" value="/supplier">
                            <c:param name="page" value="1"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>
                        <c:url var="prevUrl" value="/supplier">
                            <c:param name="page" value="${page-1}"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>

                        <c:choose>
                            <c:when test="${page > 1}">
                                <a href="${firstUrl}" title="First"><i class="fas fa-angle-double-left"></i></a>
                                <a href="${prevUrl}"  title="Prev" ><i class="fas fa-angle-left"></i></a>
                                </c:when>
                                <c:otherwise>
                                <span class="disabled"><i class="fas fa-angle-double-left"></i></span>
                                <span class="disabled"><i class="fas fa-angle-left"></i></span>
                                </c:otherwise>
                            </c:choose>

                        <!-- Page numbers -->
                        <c:forEach begin="1" end="${numPages}" var="i">
                            <c:url var="pageUrl" value="/supplier">
                                <c:param name="page" value="${i}"/>
                                <c:if test="${not empty search}">
                                    <c:param name="search" value="${search}"/>
                                </c:if>
                            </c:url>
                            <c:choose>
                                <c:when test="${i == page}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageUrl}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <!-- Next / Last -->
                        <c:url var="nextUrl" value="/supplier">
                            <c:param name="page" value="${page+1}"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>
                        <c:url var="lastUrl" value="/supplier">
                            <c:param name="page" value="${numPages}"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>

                        <c:choose>
                            <c:when test="${page < numPages}">
                                <a href="${nextUrl}" title="Next"><i class="fas fa-angle-right"></i></a>
                                <a href="${lastUrl}" title="Last"><i class="fas fa-angle-double-right"></i></a>
                                </c:when>
                                <c:otherwise>
                                <span class="disabled"><i class="fas fa-angle-right"></i></span>
                                <span class="disabled"><i class="fas fa-angle-double-right"></i></span>
                                </c:otherwise>
                            </c:choose>
                    </div>

                    <div class="page-info">
                        Page ${page} of ${numPages} (${totalSuppliers} total)
                    </div>
                </c:if>

            </div>
        </section>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById('addPermissionForm').addEventListener('submit', function (e) {
                e.preventDefault();
                const form = e.target;
                const data = new URLSearchParams(new FormData(form));

                fetch(form.getAttribute('action') || '${pageContext.request.contextPath}/supplier', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: data
                })
                        .then(res => res.json())
                        .then(json => {
                            if (json.success) {
                                // Close modal
                                var modal = bootstrap.Modal.getInstance(document.getElementById('addPermissionModal'));
                                modal.hide();
                                // Show success alert
                                showAlert(true, "Supplier added successfully!");
                                // Reload trang hoặc cập nhật table
                                setTimeout(() => location.reload(), 1000);
                            } else {
                                const err = document.getElementById('addError');
                                err.textContent = json.message;
                                err.style.display = 'block';
                                showAlert(false, json.message || "Failed to add supplier");
                            }
                        })
                        .catch(error => {
                            showAlert(false, "Error adding supplier");
                        });
            });

            document.addEventListener('DOMContentLoaded', function () {
                document.querySelectorAll('.toggle-status').forEach(toggle => {
                    toggle.addEventListener('change', function () {
                        const supplierId = toggle.getAttribute('data-id');
                        const newStatus = toggle.checked ? 'active' : 'inactive';

                        fetch('${pageContext.request.contextPath}/deletesupplier', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                            body: new URLSearchParams({
                                supplierId: supplierId,
                                status: newStatus
                            })
                        })
                                .then(res => res.json())
                                .then(json => {
                                    if (json.success) {
                                        const label = document.querySelector(`.status-label[data-id="${supplierId}"]`);
                                        if (label) {
                                            const badgeClass = newStatus === 'active' ? 'bg-success' : 'bg-secondary';
                                            label.className = `status-label badge ${badgeClass}`;
                                            label.textContent = newStatus;
                                        }
                                        showAlert(true, `Supplier status updated successfully`);
                                    } else {
                                        showAlert(false, json.message || "Failed to update status");
                                        toggle.checked = !toggle.checked;
                                    }
                                })
                                .catch(err => {
                                    showAlert(false, "Error updating status");
                                    toggle.checked = !toggle.checked;
                                });
                    });
                });
            });

            // Add edit supplier functionality
            document.querySelectorAll('.edit-supplier').forEach(button => {
                button.addEventListener('click', function () {
                    const supplierId = this.getAttribute('data-id');
                    const name = this.getAttribute('data-name');
                    const status = this.getAttribute('data-status');

                    document.getElementById('editSupplierId').value = supplierId;
                    document.getElementById('editSupplierName').value = name;
                    document.getElementById('editStatus').value = status;
                });
            });

            document.getElementById('editSupplierForm').addEventListener('submit', function (e) {
                e.preventDefault();
                const form = e.target;
                const data = new URLSearchParams(new FormData(form));

                fetch(form.getAttribute('action'), {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: data
                })
                        .then(res => res.json())
                        .then(json => {
                            if (json.success) {
                                // Close modal
                                var modal = bootstrap.Modal.getInstance(document.getElementById('editSupplierModal'));
                                modal.hide();
                                // Show success alert
                                showAlert(true, "Supplier updated successfully!");
                                // Reload page after showing alert
                                setTimeout(() => location.reload(), 1000);
                            } else {
                                const err = document.getElementById('editError');
                                err.textContent = json.message;
                                err.style.display = 'block';
                                showAlert(false, json.message || "Failed to update supplier");
                            }
                        })
                        .catch(error => {
                            showAlert(false, "Error updating supplier");
                        });
            });

        </script>

        <script>
            /**
             * Hàm showAlert(status, message):
             *   - status = true  → alert màu xanh (alert-success)
             *   - status = false → alert màu đỏ  (alert-danger)
             * alert sẽ nằm cố định bên phải, tự đóng sau 4s
             */
            function showAlert(status, message) {
                // Xóa alert cũ nếu có
                const existingAlert = document.querySelector('.custom-alert');
                if (existingAlert) {
                    existingAlert.remove();
                }
                console.log('here', status);
                // Tạo alert mới
                const alertDiv = document.createElement('div');
                if (status === true) {
                    alertDiv.className = `alert alert-success alert-dismissible fade show custom-alert`;

                } else {
                    alertDiv.className = `alert alert-danger alert-dismissible fade show custom-alert`;

                }
                alertDiv.setAttribute('role', 'alert');
                alertDiv.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    min-width: 300px;
                    z-index: 9999;
                    padding: 1rem;
                    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
                    border-radius: 0.375rem;
                `;

                // Thêm icon
                const icon = document.createElement('i');

                icon.className = `fas ${status ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2`;
                alertDiv.appendChild(icon);

                // Thêm message
                const messageText = document.createTextNode(message);
                alertDiv.appendChild(messageText);



                // Thêm vào body
                document.body.appendChild(alertDiv);

                // Animation khi hiện alert
                setTimeout(() => {
                    alertDiv.style.opacity = '1';
                }, 100);

//                 Tự động đóng sau 4s
                setTimeout(() => {
                    if (alertDiv && document.body.contains(alertDiv)) {
                        alertDiv.classList.remove('show');
                        setTimeout(() => alertDiv.remove(), 150);
                    }
                }, 4000);
            }
        </script>

    </body>
</html>
