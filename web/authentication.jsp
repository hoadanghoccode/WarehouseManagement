<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Permission List</title>
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
                <!-- Header + Search + Add -->
                <div class="header">
                    <h1 class="title">Resource List</h1>
                    <div class="header-actions">
                        <form action="resource" method="get" class="search-form">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text" name="search" value="${search}"
                                   placeholder="Search resources..." class="search-input"/>
                        </form>
                        <button id="addPermissionBtn"
                                class="btn btn-success"
                                data-bs-toggle="modal"
                                data-bs-target="#addPermissionModal">
                            Add Permission
                        </button>
                    </div>
                </div>

                <!-- Modal for Adding Permission -->
                <!-- Modal -->
                <div class="modal fade" id="addPermissionModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form id="addPermissionForm">
                                <div class="modal-header">
                                    <h5 class="modal-title">Add New Permission</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="resourceName" class="form-label">Resource Name</label>
                                        <input type="text" id="resourceName" name="resourceName"
                                               class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="description" class="form-label">Description</label>
                                        <input type="text" id="description" name="description"
                                               class="form-control" required>
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


                <!-- Stats -->
                <c:if test="${not empty resourceList}">
                    <div class="stats-info">
                        <i class="fas fa-info-circle"></i>
                        <c:choose>
                            <c:when test="${not empty search}">
                                Found <strong>${totalResources}</strong> for "<strong>${search}</strong>"
                            </c:when>
                            <c:otherwise>
                                Showing <strong>${resourceList.size()}</strong>
                                / <strong>${totalResources}</strong> resources
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <!-- Table -->
                <c:choose>
                    <c:when test="${not empty resourceList}">
                        <div class="table-container">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="res" items="${resourceList}" varStatus="st">
                                        <tr>
                                            <td><strong>${(page-1)*pageSize + st.index + 1}</strong></td>
                                            <td>${res.name}</td>
                                            <td>${res.description}</td>

                                            <td class="action-buttons">                                               
                                                <!-- Delete -->
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/deleteresource"
                                                      style="display:inline;"
                                                      onsubmit="return confirm('Bạn có chắc muốn xóa resource này không?');">
                                                    <input type="hidden" name="resourceId" value="${res.resourceId}"/>
                                                    <button type="submit" class="btn btn-danger">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </form>

                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-data">
                            <i class="fas fa-folder-open fa-3x"></i>
                            <h3>No resources found</h3>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Pagination -->
                <c:if test="${numPages > 1}">
                    <div class="pagination">
                        <!-- First / Prev -->
                        <c:url var="firstUrl" value="/resource">
                            <c:param name="page" value="1"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>
                        <c:url var="prevUrl" value="/resource">
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
                            <c:url var="pageUrl" value="/resource">
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
                        <c:url var="nextUrl" value="/resource">
                            <c:param name="page" value="${page+1}"/>
                            <c:if test="${not empty search}">
                                <c:param name="search" value="${search}"/>
                            </c:if>
                        </c:url>
                        <c:url var="lastUrl" value="/resource">
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
                        Page ${page} of ${numPages} (${totalResources} total)
                    </div>
                </c:if>

            </div>
        </section>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                          document.getElementById('addPermissionForm').addEventListener('submit', function (e) {
                                                              e.preventDefault();
                                                              const form = e.target;
                                                              const data = new URLSearchParams(new FormData(form));

                                                              fetch(form.getAttribute('action') || '${pageContext.request.contextPath}/resource', {
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
                                                                              // Reload trang hoặc cập nhật table
                                                                              location.reload();
                                                                          } else {
                                                                              const err = document.getElementById('addError');
                                                                              err.textContent = json.message;
                                                                              err.style.display = 'block';
                                                                          }
                                                                      });
                                                          });
        </script>
    </body>
</html>
