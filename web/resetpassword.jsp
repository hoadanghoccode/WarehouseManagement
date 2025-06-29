<%-- Document : resetpassword Created on : May 23, 2025, 12:35:15 PM Author :
duong --%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Reset Your Password</title>
    <html>
      <head>
        <link
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
        />
        <link rel="icon" href="img/logo.png" type="image/png" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/bootstrap1.min.css" />
        <!-- themefy CSS -->
        <link rel="stylesheet" href="vendors/themefy_icon/themify-icons.css" />
        <!-- swiper slider CSS -->
        <link
          rel="stylesheet"
          href="vendors/swiper_slider/css/swiper.min.css"
        />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/select2/css/select2.min.css" />
        <!-- select2 CSS -->
        <link rel="stylesheet" href="vendors/niceselect/css/nice-select.css" />
        <!-- owl carousel CSS -->
        <link
          rel="stylesheet"
          href="vendors/owl_carousel/css/owl.carousel.css"
        />
        <!-- gijgo css -->
        <link rel="stylesheet" href="vendors/gijgo/gijgo.min.css" />
        <!-- font awesome CSS -->
        <link rel="stylesheet" href="vendors/font_awesome/css/all.min.css" />
        <link rel="stylesheet" href="vendors/tagsinput/tagsinput.css" />
        <!-- date picker -->
        <link rel="stylesheet" href="vendors/datepicker/date-picker.css" />
        <!-- datatable CSS -->
        <link
          rel="stylesheet"
          href="vendors/datatable/css/jquery.dataTables.min.css"
        />
        <link
          rel="stylesheet"
          href="vendors/datatable/css/responsive.dataTables.min.css"
        />
        <link
          rel="stylesheet"
          href="vendors/datatable/css/buttons.dataTables.min.css"
        />
        <!-- text editor css -->
        <link rel="stylesheet" href="vendors/text_editor/summernote-bs4.css" />
        <!-- morris css -->
        <link rel="stylesheet" href="vendors/morris/morris.css" />
        <!-- metarial icon css -->
        <link
          rel="stylesheet"
          six
          href="vendors/material_icon/material-icons.css"
        />
        <!-- menu css  -->
        <link rel="stylesheet" href="css/metisMenu.css" />
        <!-- style CSS -->
        <link rel="stylesheet" href="css/style1.css" />
        <link
          rel="stylesheet"
          href="css/colors/default.css"
          id="colorSkinCSS"
        />
        <link rel="stylesheet" type="text/css" href="css/resetPassword.css" />
      </head>
      <body>
        <div class="box">
          <h2>Reset Your Password</h2>
          <p class="description">
            Enter your email to receive a link to reset your password.
          </p>

          <form action="resetpassword" method="post">
            <label for="email">Email address</label>
            <input type="email" id="email" name="email" required />
            <div class="mb-3">
            <button type="submit">Send Reset Link</button>
          </form>
            <div style="margin-top: 16px;">
            <a href="login" style="font-weight: bold; text-decoration: none; color: #1d4ed8;">
                ← Back to Login
            </a>
        </div>
        </form>
          <div class="message">
            <p>${message}</p>
          </div>
        </div>
      </body>
    </html>
  </head>
</html>
