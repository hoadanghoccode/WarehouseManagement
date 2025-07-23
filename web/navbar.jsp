<%@ page import="model.Users" %>

<%
    Users navbarUser = (Users) session.getAttribute("USER");
    String imagePath = (navbarUser != null && navbarUser.getImage() != null && !navbarUser.getImage().isEmpty())
                       ? navbarUser.getImage()
                       : "img/client_img.png";
%>
<style>
    .avatar-img { width: 60px; height: 60px; object-fit: cover; border-radius: 50%; display: block;}
    .notification-bell { position:relative; cursor:pointer; }
    .notification-badge {position:absolute;top:2px;right:8px;background:#f00;color:#fff;font-size:12px;border-radius:8px;padding:0 5px;}
    .notification-dropdown { display:none; position:absolute; top:40px; right:0; width:370px; max-height:400px; overflow-y:auto; background:#fff; box-shadow:0 2px 8px #bbb; border-radius:10px; z-index:9999;}
    .notification-item { display:flex; gap:10px; padding:12px 12px; border-bottom:1px solid #eee; cursor:pointer; }
    .notification-item.unread { background:#f7f7ff; font-weight:600;}
    .notification-thumb img { width:38px;height:38px;object-fit:cover;border-radius:50%; }
    .notification-content h5 {
        display: block !important;
        color: #222;
        font-size: 15px;
        margin-bottom: 2px;
        font-weight: bold;
    }
    .notification-content p {
        display: block !important;
        color: #555;
        font-size: 14px;
        margin-bottom: 2px;
    }
    .notification-content small {
        display: block !important;
        color: #777;
        font-size: 12px;
    }
    .notification-footer { text-align:center;padding:14px;}
    .notification-footer a { color:#5055eb;text-decoration:underline;font-weight:500;}
</style>

<div class="container-fluid g-0">
    <div class="row">
        <div class="col-lg-12 p-0">
            <div class="header_iner d-flex justify-content-between align-items-center">
                <div class="sidebar_icon d-lg-none">
                    <i class="ti-menu"></i>
                </div>                                
                <div class="header_right d-flex justify-content-end align-items-center w-100" style="gap: 1rem;">
                    <div class="header_notification_warp d-flex align-items-center" style="position:relative;">
                        <!-- <div class="notification-bell" id="notificationBell">
                            <img src="img/icon/bell.svg" alt="">
                            <span class="notification-badge" id="notiCount" style="display:none;">0</span>
                        </div> -->
                        <!-- <div class="notification-dropdown" id="notificationDropdown">
                            <div class="notification_Header">
                                <h4>Notifications</h4>
                            </div>
                            <div class="Notification_body" id="notiList">
                                
                            </div>
                            <div class="notification-footer">
                                <a href="allnotifications.jsp">See More</a>
                            </div>
                        </div> -->
                    </div>
                    <div class="profile_info">
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

<script>
document.addEventListener('DOMContentLoaded', function () {
    const bell = document.getElementById('notificationBell');
    const dropdown = document.getElementById('notificationDropdown');
    const notiCount = document.getElementById('notiCount');
    const notiList = document.getElementById('notiList');
    let loaded = false;

    bell.onclick = function (e) {
        e.stopPropagation();
        if (dropdown.style.display === 'block') {
            dropdown.style.display = 'none';
            return;
        }
        dropdown.style.display = 'block';
        if (!loaded) loadNotifications(1);
    };

    document.addEventListener('click', function () {
        dropdown.style.display = 'none';
    });

    function loadNotifications(page) {
        fetch('notification?page=' + page + '&size=7')
            .then(res => res.json())
            .then(data => {
                if (data.unreadCount > 0) {
                    notiCount.innerText = data.unreadCount;
                    notiCount.style.display = 'inline-block';
                } else {
                    notiCount.style.display = 'none';
                }
                let html = '';
                data.notifications.forEach(noti => {
                    console.log('noti:', noti);
                    html += `<div class="notification-item${noti.isRead ? '' : ' unread'}" onclick="markReadAndRedirect(${noti.id},'${noti.link ? noti.link : '#'}')">
                        <div class="notification-thumb">
                            <img src="img/client_img.png" />
                        </div>
                        <div class="notification-content">
                            <h5>${noti.title}</h5>
                            <p>${noti.content}</p>
                            <small>${noti.createdAt ? noti.createdAt.substring(0,16) : ''}</small>
                        </div>
                    </div>`;
                });
                console.log('data nef', html);
                notiList.innerHTML = html || '<div style="padding:20px;text-align:center;color:#aaa;">No notifications</div>';
                loaded = true;
            });
    }

    window.markReadAndRedirect = function (id, link) {
        fetch('notification', {
            method: 'POST',
            headers: {'Content-Type':'application/x-www-form-urlencoded'},
            body: 'notificationId=' + id
        }).then(() => {
            window.location = link;
        });
    };
});
</script>
