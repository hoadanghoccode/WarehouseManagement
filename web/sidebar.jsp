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
    Map<String, Boolean> permissions = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
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
        <li class="mm-active">
            <a href="index.jsp" aria-expanded="false">
                <img src="img/menu-icon/dashboard.svg" alt="">
                <span>Dashboard</span>
            </a>
        </li>

        <li class="">
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
                <c:if test="${permissions['Customer_VIEW']}">
                    <li><a href="/WarehouseManagement/userlist">User List</a></li>
                </c:if>
            </ul>
        </li>
        <li class="">
            <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/categorylist">
                <img src="img/menu-icon/4.svg" alt="">
                <span>Categories</span>
            </a>
        </li>
        <li class="">
            <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/list-material">
                <img src="img/menu-icon/4.svg" alt="">
                <span>Materials</span>
            </a>
        </li>
        <li class="">
            <a class="has-arrow" aria-expanded="false" href="/WarehouseManagement/userlist">
                <img src="img/menu-icon/4.svg" alt="">
                <span>Users</span>
            </a>
        </li>
        <c:if test="${user.roleId == 1}">
            <li class="mm-active">
                <a href="adminresetlist">
                    <img src="img/menu-icon/your-icon.svg" alt="">
                    <span>Reset List</span>
                </a>
            </li>
        </c:if>

    </ul>
</nav>

<!--        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/3.svg" alt="">
                <span>Applications</span>
            </a>
            <ul>
                <li><a href="mail_box.html">Mail Box</a></li>
                <li><a href="chat.html">Chat</a></li>
                <li><a href="faq.html">FAQ</a></li>
            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/4.svg" alt="">
                <span>Components</span>
            </a>
            <ul>
                <li><a href="accordion.html">Accordions</a></li>
                <li><a href="Scrollable.html">Scrollable</a></li>
                <li><a href="notification.html">Notifications</a></li>
                <li><a href="carousel.html">Carousel</a></li>
                <li><a href="Pagination.html">Pagination</a></li>
            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/5.svg" alt="">
                <span>UI Elements</span>
            </a>
            <ul>
                <li><a href="buttons.html">Buttons</a></li>
                <li><a href="dropdown.html">Droopdowns</a></li>
                <li><a href="Badges.html">Badges</a></li>
                <li><a href="Loading_Indicators.html">Loading Indicators</a></li>
                <li><a href="State_color.html">State color</a></li>
                <li><a href="typography.html">Typography</a></li>
                <li><a href="datepicker.html">Date Picker</a></li>
            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/6.svg" alt="">
                <span>Animations</span>
            </a>
            <ul>
                <li><a href="wow_animation.html">Animate</a></li>
                <li><a href="Scroll_Reveal.html">Scroll Reveal</a></li>
                <li><a href="tilt.html">Tilt Animation</a></li>

            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/7.svg" alt="">
                <span>Cards</span>
            </a>
            <ul>
                <li><a href="basic_card.html">Basic Card</a></li>
                <li><a href="theme_card.html">Theme Card</a></li>
                <li><a href="dargable_card.html">Draggable Card</a></li>
            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/8.svg" alt="">
                <span>Table</span>
            </a>
            <ul>
                <li><a href="data_table.html">Data Tables</a></li>
                <li><a href="bootstrap_table.html">Grid Tables</a></li>
            </ul>
        </li>

        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/9.svg" alt="">
                <span>Charts</span>
            </a>
            <ul>
                <li><a href="chartjs.html">ChartJS</a></li>
                <li><a href="apex_chart.html">Apex Charts</a></li>
                <li><a href="chart_sparkline.html">Chart sparkline</a></li>
                <li><a href="am_chart.html">am-charts</a></li>
                <li><a href="nvd3_charts.html">nvd3 charts.</a></li>
            </ul>
        </li>


        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/10.svg" alt="">
                <span>Widgets</span>
            </a>
            <ul>
                <li><a href="chart_box_1.html">Chart Boxes 1</a></li>
                <li><a href="profilebox.html">Profile Box</a></li>
            </ul>
        </li>


        <li class="">
            <a   class="has-arrow" href="#" aria-expanded="false">
                <img src="img/menu-icon/map.svg" alt="">
                <span>Maps</span>
            </a>
            <ul>
                <li><a href="mapjs.html">Maps JS</a></li>
                <li><a href="vector_map.html">Vector Maps</a></li>
            </ul>
        </li>-->

</ul>
</nav>