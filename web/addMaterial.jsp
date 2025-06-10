<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Material</title>
    <!-- Bootstrap CSS (v5.3) -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-ENjdO4Dr2bkBIFxQpeoYDx1C0Pj7skkPXQ9v+zAP+Yd1zYV6G/p3v5y5B5CEg7vh"
      crossorigin="anonymous"
    />
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
        * {
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background-color: #f3f4f6;
            margin: 0;
            padding: 0;
            color: #374151;
        }
        .container {
            max-width: 600px;
            margin: 40px auto;
            padding: 24px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .title {
            font-size: 24px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 24px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 8px;
        }
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            color: #374151;
            background-color: #fff;
        }
        .form-group input[type="checkbox"] {
            width: auto;
        }
        .unit-section {
            margin-top: 10px;
            padding-left: 20px;
        }
        .unit-section .unit-item {
            display: flex;
            gap: 10px;
            margin-bottom: 10px;
            align-items: center;
        }
        .unit-section input[type="number"] {
            width: 120px;
        }
        .btn {
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
            font-size: 14px;
            border: none;
            transition: background-color 0.2s;
        }
        .btn-primary {
            background-color: #6366f1;
        }
        .btn-primary:hover {
            background-color: #4f46e5;
        }
        .btn-secondary {
            background-color: #6b7280;
        }
        .btn-secondary:hover {
            background-color: #4b5563;
        }
        .error {
            color: #ef4444;
            font-size: 14px;
            margin-bottom: 16px;
        }
        .warning {
            color: #b45309;
            font-size: 14px;
            margin-top: 4px;
        }
    </style>
</head>
<body>
    <!-- INCLUDE SIDEBAR & NAVBAR -->
    <jsp:include page="sidebar.jsp" flush="true" />
    <section class="main_content dashboard_part">
        <jsp:include page="navbar.jsp" flush="true" />

        <div class="container">
            <h1 class="title">Add Material</h1>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <form action="add-material" method="POST" onsubmit="return validateForm()">
                <!-- Select existing material or enter new name -->
                <div class="form-group">
                    <label for="materialId">Select Existing Material</label>
                    <select id="materialId" name="materialId" onchange="toggleNewName(this)">
                        <option value="">-- Choose or Enter New --</option>
                        <option value="0">*** Enter New Material ***</option>
                        <c:forEach var="mat" items="${materials}">
                            <option value="${mat.materialId}">${mat.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group" id="newNameGroup" style="display: none;">
                    <label for="newName">New Material Name</label>
                    <input type="text" id="newName" name="newName" placeholder="Enter new material name"
                           oninput="checkNameConflict()" autocomplete="off">
                    <div id="nameWarning" class="warning" style="display: none;">
                        This material name already exists in the database. Submitting will link to the existing material.
                    </div>
                </div>

                <!-- Category (only subcategories: parentId != null) -->
                <div class="form-group">
                    <label for="categoryId">Category (Subcategories Only)</label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="">-- Select Subcategory --</option>
                        <c:forEach var="category" items="${categories}">
                            <c:if test="${category.parentId != null}">
                                <option value="${category.categoryId}">${category.name}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <!-- Supplier -->
                <div class="form-group">
                    <label for="supplierId">Supplier</label>
                    <select id="supplierId" name="supplierId" required>
                        <option value="">-- Select Supplier --</option>
                        <c:forEach var="supplier" items="${suppliers}">
                            <option value="${supplier.id}">${supplier.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Units with price and quantity -->
                <div class="form-group">
                    <label>Units</label>
                    <div class="unit-section">
                        <c:forEach var="unit" items="${units}">
                            <div class="unit-item">
                                <input type="checkbox" name="unitIds" value="${unit.unitId}"
                                       onchange="toggleUnitInputs(this)">
                                <label>${unit.name}</label>
                                <input type="number" step="0.01" name="price_${unit.unitId}" placeholder="Price"
                                       disabled>
                                <input type="number" step="0.01" name="quantity_${unit.unitId}" placeholder="Quantity"
                                       disabled>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Buttons -->
                <button type="submit" class="btn btn-primary">Add Material</button>
                <a href="list-material" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </section>

    <script>
        // Build an array of existing names (lowercase) for quick conflict checking
        const existingNames = [
            <c:forEach var="mat" items="${materials}" varStatus="loop">
                "${mat.name.toLowerCase()}"<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        function toggleNewName(selectElem) {
            const newNameGroup = document.getElementById('newNameGroup');
            const nameWarning = document.getElementById('nameWarning');
            if (selectElem.value === '0') {
                newNameGroup.style.display = 'block';
                document.getElementById('newName').required = true;
            } else {
                newNameGroup.style.display = 'none';
                nameWarning.style.display = 'none';
                document.getElementById('newName').required = false;
                document.getElementById('newName').value = '';
            }
        }

        function toggleUnitInputs(checkbox) {
            const priceInput = checkbox.nextElementSibling.nextElementSibling;
            const quantityInput = priceInput.nextElementSibling;
            priceInput.disabled = !checkbox.checked;
            quantityInput.disabled = !checkbox.checked;
            priceInput.required = checkbox.checked;
            quantityInput.required = checkbox.checked;
            if (!checkbox.checked) {
                priceInput.value = '';
                quantityInput.value = '';
            }
        }

        function checkNameConflict() {
            const input = document.getElementById('newName').value.trim().toLowerCase();
            const nameWarning = document.getElementById('nameWarning');
            if (input.length === 0) {
                nameWarning.style.display = 'none';
                return;
            }
            if (existingNames.includes(input)) {
                nameWarning.style.display = 'block';
            } else {
                nameWarning.style.display = 'none';
            }
        }

        function validateForm() {
            const materialSelect = document.getElementById('materialId');
            const newNameInput = document.getElementById('newName');
            if (materialSelect.value === '') {
                alert("Please select an existing material or choose to enter a new one.");
                return false;
            }
            if (materialSelect.value === '0' && newNameInput.value.trim() === '') {
                alert("New material name is required.");
                return false;
            }

            const unitCheckboxes = document.querySelectorAll('input[name=\"unitIds\"]');
            let atLeastOneUnitChecked = false;
            for (let checkbox of unitCheckboxes) {
                if (checkbox.checked) {
                    atLeastOneUnitChecked = true;
                    break;
                }
            }
            if (!atLeastOneUnitChecked) {
                alert("At least one unit must be selected.");
                return false;
            }
            return true;
        }
    </script>

    <!-- Bootstrap Bundle JS (includes Popper) -->
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+YxVZXyGTc5J0QTDkfyQp5kw+Vb4D"
      crossorigin="anonymous"
    ></script>
</body>
</html>
