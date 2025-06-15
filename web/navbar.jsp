<%@ page import="model.Users" %>
<style>
    .avatar-img {
        width: 60px;            
        height: 60px;          
        object-fit: cover;      
        border-radius: 50%;     
        display: block;
    }

</style>
<div class="container-fluid g-0">
    <div class="row">
        <div class="col-lg-12 p-0">
            <div class="header_iner d-flex justify-content-between align-items-center">
                <div class="sidebar_icon d-lg-none">
                    <i class="ti-menu"></i>
                </div>                                
                <div class="header_right d-flex justify-content-end align-items-center w-100" style="gap: 1rem;">
                    <%
                        Users navbarUser = (Users) session.getAttribute("USER");
                        String imagePath = (navbarUser != null && navbarUser.getImage() != null && !navbarUser.getImage().isEmpty())
                                           ? navbarUser.getImage()
                                           : "img/client_img.png";
                    %>
                    <div class="profile_info" style="margin-left:auto;">
                        <img src="<%= imagePath %>" alt="Profile" class="avatar-img" />
                        <div class="profile_info_iner">
                            <div class="profile_info_details">
                                <a href="viewuserinfo">My Profile </a>
                                <a href="logout">Log Out </a>
                            </div>
                        </div>
                    </div>
                </div>                
            </div>
        </div>
    </div>
</div>
