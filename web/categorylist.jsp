<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Category List</title>
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
        <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />

        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css">
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS">
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 10px;
            }
            .pagination a, .pagination strong {
                margin: 0 5px;
                text-decoration: none;
            }
        </style>
        <!--        <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        const spans = document.querySelectorAll("td span");
        
                        spans.forEach(span => {
                            const tooltip = span.querySelector(".subcat-tooltip");
                            if (tooltip) {
                                span.addEventListener("mouseenter", () => {
                                    tooltip.style.display = "block";
                                });
                                span.addEventListener("mouseleave", () => {
                                    tooltip.style.display = "none";
                                });
                            }
                        });
                    });
                </script>-->
        <!--        <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        const buttons = document.querySelectorAll(".toggle-btn");
                        const tooltips = document.querySelectorAll(".subcat-tooltip");
                        const lockedTooltips = new Set();
        
                        buttons.forEach(btn => {
                            const id = btn.dataset.id;
                            const tooltip = document.getElementById("tooltip-" + id);
        
                            // Hover
                            btn.addEventListener("mouseenter", () => {
                                if (!lockedTooltips.has(id)) {
                                    tooltip.style.display = "block";
                                }
                            });
        
                            btn.addEventListener("mouseleave", () => {
                                if (!lockedTooltips.has(id)) {
                                    tooltip.style.display = "none";
                                }
                            });
        
                            // Tooltip hover giữ không bị mất
                            tooltip.addEventListener("mouseenter", () => {
                                if (!lockedTooltips.has(id)) {
                                    tooltip.style.display = "block";
                                }
                            });
        
                            tooltip.addEventListener("mouseleave", () => {
                                if (!lockedTooltips.has(id)) {
                                    tooltip.style.display = "none";
                                }
                            });
        
                            // Click toggle lock
                            btn.addEventListener("click", () => {
                                if (lockedTooltips.has(id)) {
                                    lockedTooltips.delete(id);
                                    tooltip.style.display = "none";
                                } else {
                                    lockedTooltips.add(id);
                                    tooltip.style.display = "block";
                                }
                            });
                        });
                    });
                </script>-->

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const buttons = document.querySelectorAll(".toggle-btn");
                const visibleTooltips = new Set();

                buttons.forEach(btn => {
                    const id = btn.dataset.id;
                    const tooltipRow = document.getElementById("tooltip-" + id);

                    // Hover (hiện tạm)
                    btn.addEventListener("mouseenter", () => {
                        if (!visibleTooltips.has(id)) {
                            tooltipRow.style.display = "table-row";
                        }
                    });

                    btn.addEventListener("mouseleave", () => {
                        if (!visibleTooltips.has(id)) {
                            tooltipRow.style.display = "none";
                        }
                    });

                    // Tooltip giữ hiện khi hover
                    tooltipRow.addEventListener("mouseenter", () => {
                        if (!visibleTooltips.has(id)) {
                            tooltipRow.style.display = "table-row";
                        }
                    });

                    tooltipRow.addEventListener("mouseleave", () => {
                        if (!visibleTooltips.has(id)) {
                            tooltipRow.style.display = "none";
                        }
                    });

                    // Click để giữ hoặc tắt tooltip
                    btn.addEventListener("click", () => {
                        if (visibleTooltips.has(id)) {
                            visibleTooltips.delete(id);
                            tooltipRow.style.display = "none";
                        } else {
                            visibleTooltips.add(id);
                            tooltipRow.style.display = "table-row";
                        }
                    });
                });
            });
        </script>

    </head>
    <body>
        <h2>Category List</h2>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <form action="categorylist" method="get" style="display: inline-block;">
                <input type="text" name="search" value="${search}" placeholder="Search by name..." />
                <input type="submit" value="Search" />
            </form>
            <a href="addcategory" style="display: inline-block; padding: 8px 15px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px;">Add New Category</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="category" items="${categoryList}">
                    <!-- Dòng chính -->
                    <tr>
                        <td>${category.categoryId}</td>
                        <td>
                            ${category.name}
                            <span class="toggle-btn" style="cursor: pointer; color: blue; text-decoration: underline;" data-id="${category.categoryId}">
                                (${category.subCategoryCount})
                            </span>
                        </td>
                        <td>
                            <a href="categorydetail?cid=${category.categoryId}">View Detail</a> |
                            <a href="deletecategory?cid=${category.categoryId}" onclick="return confirm('Are you sure to delete?')">Delete</a>
                        </td>
                    </tr>

                    <!-- Dòng chứa danh sách sub-category (là category con) -->
                    <tr class="subcat-row" id="tooltip-${category.categoryId}" style="display: none; background-color: #f9f9f9;">
                        <td colspan="3">
                            <ul style="margin: 0; padding-left: 20px;">
                                <c:forEach var="sub" items="${category.subCategories}">
                                    <li>
                                        <a href="categorydetail?cid=${sub.categoryId}">${sub.name}</a>
                                    </li>
                                </c:forEach>
                                <c:if test="${empty category.subCategories}">
                                    <li><em>No sub-categories</em></li>
                                    </c:if>
                            </ul>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination" style="margin-top: 20px;">
            <c:if test="${num > 1}">
                <c:forEach begin="1" end="${num}" var="i">
                    <c:choose>
                        <c:when test="${i == page}">
                            <strong>[${i}]</strong>
                        </c:when>
                        <c:otherwise>
                            <a href="categorylist?page=${i}&search=${search}">[${i}]</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </div>
    </body>
</html>
