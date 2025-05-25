<%-- 
    Document   : resetpassword
    Created on : May 23, 2025, 12:35:15 PM
    Author     : duong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
    </head>
    <body>
        <form action="resetpassword" method="post">
    <label>Enter your email:</label>
    <input type="email" name="email" required />
    <button type="submit">Request</button>
</form>
        <p class="text-danger">${message}</p>
    </body>
</html>
