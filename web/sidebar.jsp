<%-- 
    Document   : sidebar
    Created on : May 16, 2025, 3:29:58 PM
    Author     : legia
--%>
<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>
<%@ page import="java.util.Map" %>

<%
    Users user = (Users) session.getAttribute("user");
    request.setAttribute("user", user);

    @SuppressWarnings(
            
    
    "unchecked")
    Map<String, Boolean> permissions = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
    if (permissions == null) {
        permissions = new java.util.HashMap<>();
    }
    request.setAttribute("permissions", permissions);
%>
<nav class="sidebar">
    <div class="logo d-flex justify-content-between">
        <a href="index.jsp"><img src="img/logo.png" alt=""></a>
        <div class="sidebar_close_icon d-lg-none">
            <i class="ti-close"></i>
        </div>
    </div>

    <ul id="sidebar_menu">
        <li>
            <a href="index.jsp" aria-expanded="false">
                <img src="img/menu-icon/dashboard.svg" alt="">
                <span>Dashboard</span>
            </a>
        </li>

        <li>
            <a class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/4.svg" alt="">
                <span>Authentication</span>
            </a>
            <ul>
                <c:if test="${permissions['Permission_VIEW']}">
                    <li><a href="/WarehouseManagement/resource">Permission</a></li>
                </c:if>
                <c:if test="${permissions['Role_VIEW']}">
                    <li><a href="/WarehouseManagement/permissionrole">Role</a></li>
                </c:if>
                <c:if test="${permissions['Department_VIEW']}">
                    <li><a href="/WarehouseManagement/department">Department</a></li>
                </c:if>
            </ul>
        </li>
        <c:if test="${permissions['Category_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/categorylist">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Categories</span>
                </a>
            </li>
        </c:if>
        <c:if test="${permissions['Material_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/list-material">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Materials</span>
                </a>
            </li>
        </c:if>
        <c:if test="${permissions['Material_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/import-note-list">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Import_note</span>
                </a>
            </li>
        </c:if>
            <c:if test="${permissions['Material_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/unit">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Unit</span>
                </a>
            </li>
        </c:if>
        <c:if test="${permissions['Material_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/inventory">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Current Inventory</span>
                </a>
            </li>
        </c:if>
            <c:if test="${permissions['Customer_VIEW']}">
            <li>
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/userlist">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Users</span>
                </a>
            </li>
        </c:if>
        <c:if test="${permissions['Password_VIEW']}">
            <li>
                <a href="/WarehouseManagement/adminresetlist">
                    <img src="img/menu-icon/your-icon.svg" alt="">
                    <span>Reset List</span>
                </a>
            </li>
        </c:if>

        <c:if test="${permissions['Material_VIEW']}">
            <li class="">
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/unit">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Unit</span>
                </a>
            </li>
        </c:if>

        <c:if test="${permissions['Material_VIEW']}">
            <li class="">
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/inventory">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Current Inventory</span>
                </a>
            </li>
        </c:if>


        <c:if test="${permissions['Material_VIEW']}">
            <li class="">
                <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/orderlist">
                    <img src="img/menu-icon/4.svg" alt="">
                    <span>Order List</span>
                </a>
            </li>
        </c:if>

    </ul>
</nav>

<script>
document.addEventListener('DOMContentLoaded', function () {
    const currentPath = window.location.pathname;
    const menuItems = document.querySelectorAll('#sidebar_menu li');

    menuItems.forEach(item => {
        const link = item.querySelector('a[href]');
        if (link) {
            const href = link.getAttribute('href');
            if (href === "index.jsp" && (currentPath === "/" || currentPath === "/WarehouseManagement/" || currentPath === "/WarehouseManagement/index.jsp")) {
                item.classList.add('mm-active');
            }
            else if (currentPath === href || (href !== '#' && currentPath.startsWith(href))) {
                item.classList.add('mm-active');
                
                const parentLi = item.closest('li');
                if (parentLi) {
                    const parentLink = parentLi.querySelector('a.has-arrow');
                    if (parentLink) {
                        parentLink.setAttribute('aria-expanded', 'true');
                        const submenu = parentLi.querySelector('ul');
                        if (submenu) {
                            submenu.style.display = 'block';
                        }
                    }
                }
            } else {
                item.classList.remove('mm-active');
            }
        }
        const authSubItems = item.querySelectorAll('ul li a');
        if (authSubItems.length > 0) {
            authSubItems.forEach(subItem => {
                if (currentPath === subItem.getAttribute('href')) {
                    item.classList.add('mm-active');
                    item.querySelector('a.has-arrow').setAttribute('aria-expanded', 'true');
                    const submenu = item.querySelector('ul');
                    if (submenu) {
                        submenu.style.display = 'block';
                    }
                }
            });
        }
    });
});
</script>