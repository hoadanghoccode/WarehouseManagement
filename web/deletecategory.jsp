<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Confirm Deletion</title>
        <link rel="stylesheet" href="css/deletecategory.css" />
    </head>
    <body>
        <div class="modal-overlay">
            <div class="modal">
                <button class="close-btn" onclick="window.location.href = 'categorylist'">&times;</button>
                <h2>Delete category?</h2>
                <p>Are you sure you want to delete this category? You can't undo this action.</p>

                <c:if test="${countSub > 0 || countMaterial > 0}">
                    <div class="alert">
                        <div class="alert-icon">⚠️</div>
                        <div>
                            <strong>Warning</strong><br />
                            This category contains 
                            <c:if test="${countSub > 0}"><strong>${countSub}</strong> sub-categories</c:if>
                            <c:if test="${countSub > 0 && countMaterial > 0}"> and </c:if>
                            <c:if test="${countMaterial > 0}"><strong>${countMaterial}</strong> materials</c:if>.
                                <br /><br />
                                <span style="color: #991B1B;">
                                    All of these sub-categories and materials will also be <strong>permanently deleted</strong> due to cascade deletion.
                                </span>
                            </div>


                        </div>



                </c:if>

                <form action="deletecategory" method="post" class="modal-actions">
                    <input type="hidden" name="cid" value="${cid}" />
                    <button type="button" class="btn cancel" onclick="window.location.href = 'categorylist'">No, Keep it</button>
                    <button type="submit" class="btn delete">Yes, Delete</button>
                </form>
            </div>
        </div>
    </body>
</html>
