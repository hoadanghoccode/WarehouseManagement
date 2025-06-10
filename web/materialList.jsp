<%@ page import="com.google.gson.Gson" %> <%@ page contentType="text/html"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Materials List</title>

    <!-- Bootstrap CSS (v5.3) -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
      crossorigin="anonymous"
    />

    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
    <link rel="icon" href="img/logo.png" type="image/png" />
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
    <link rel="stylesheet" href="vendors/material_icon/material-icons.css" />
    <!-- menu css  -->
    <link rel="stylesheet" href="css/metisMenu.css" />
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/colors/default.css" id="colorSkinCSS" />

    <!-- Your custom CSS for material list -->
    <link rel="stylesheet" type="text/css" href="css/materiallist.css" />

    <style>
      * {
        box-sizing: border-box;
      }
      body {
        font-family: "Segoe UI", Tahoma, sans-serif;
        background-color: #f3f4f6;
        margin: 0;
        padding: 0;
        color: #374151;
      }
      .container {
        padding-top: 24px;
        padding-bottom: 24px;
        max-width: 1200px;
        margin: 0 auto;
      }
      .title {
        font-size: 28px;
        font-weight: 600;
        color: #1f2937;
        margin-bottom: 16px;
      }
      .stats-info {
        margin-bottom: 16px;
        color: #374151;
        display: flex;
        align-items: center;
        gap: 8px;
        background-color: #dbeafe;
        padding: 12px 16px;
        border-radius: 8px;
        font-size: 14px;
      }
      .stats-info i {
        color: #3b82f6;
      }
      .stats-info strong {
        color: #1f2937;
      }
      .table-container {
        overflow-x: auto;
        background-color: white;
        border-radius: 12px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
      }
      table.table {
        width: 100%;
        border-collapse: collapse;
        min-width: 600px;
      }
      table.table th,
      table.table td {
        padding: 12px 16px;
        text-align: left;
        border-bottom: 1px solid #e5e7eb;
        font-size: 14px;
        vertical-align: middle;
      }
      table.table th {
        background-color: #f3f4f6;
        font-weight: 600;
        color: #1f2937;
        position: sticky;
        top: 0;
        z-index: 2;
      }
      table.table tbody tr:nth-child(even) {
        background-color: #f9fafb;
      }
      table.table tbody tr:hover {
        background-color: #eef2ff;
      }
      .action-buttons {
        display: flex;
        gap: 8px;
      }
      .no-data {
        text-align: center;
        padding: 24px;
        background-color: #f3f4f6;
        border-radius: 12px;
        font-size: 16px;
        color: #9ca3af;
      }
      .modal {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 999;
      }
      .modal-content {
        background-color: white;
        margin: 6% auto;
        padding: 24px 32px;
        border-radius: 4px;
        width: 90%;
        max-width: 480px;
        position: relative;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        font-size: 14px;
      }
      .modal .close {
        position: absolute;
        top: 12px;
        right: 18px;
        font-size: 24px;
        cursor: pointer;
        color: #9ca3af;
        transition: color 0.2s;
      }
      .modal .close:hover {
        color: #374151;
      }
      .detail-item {
        margin-bottom: 14px;
        display: flex;
        gap: 8px;
      }
      .detail-item strong {
        color: #1f2937;
        width: 110px;
      }
      @media (max-width: 768px) {
        .title {
          font-size: 24px;
        }
        table.table {
          min-width: 600px;
        }
        .modal-content {
          padding: 20px;
        }
      }
      /* Ensure Bootstrap modal is interactive */
      .modal.fade .modal-dialog {
        z-index: 1050; /* Higher than backdrop */
      }
      .modal-backdrop {
        z-index: 1040 !important; /* Ensure backdrop is behind the modal */
      }
      .modal-open .modal {
        pointer-events: auto !important; /* Ensure modal is interactive */
      }
    </style>
  </head>
  <body>
    <jsp:include page="sidebar.jsp" />
    <section class="main_content dashboard_part">
      <jsp:include page="navbar.jsp" />

      <div class="container">
        <h1 class="title">Materials List</h1>

        <div class="row g-2 mb-3 align-items-center">
          <div class="col-12 col-md-3">
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0">
                <i class="fas fa-search text-muted"></i>
              </span>
              <input
                type="text"
                id="searchInput"
                class="form-control border-start-0"
                placeholder="Search materials..."
                oninput="filterMaterials()"
              />
            </div>
          </div>
          <div class="col-12 col-md-2">
            <select
              class="form-select"
              id="categoryFilter"
              onchange="filterMaterials()"
            >
              <option value="">All Categories</option>
              <c:forEach items="${categories}" var="category">
                <option value="${category.name}">${category.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-12 col-md-2">
            <select
              class="form-select"
              id="unitFilter"
              onchange="filterMaterials()"
            >
              <option value="">All Units</option>
              <c:forEach items="${units}" var="unit">
                <option value="${unit.name}">${unit.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-12 col-md-2">
            <select
              class="form-select"
              id="supplierFilter"
              onchange="filterMaterials()"
            >
              <option value="">All Suppliers</option>
              <c:forEach items="${suppliers}" var="supplier">
                <option value="${supplier.name}">${supplier.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-12 col-md-1">
            <select
              class="form-select"
              id="activeFilter"
              onchange="filterMaterials()"
            >
              <option value="">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
            </select>
          </div>
          <div class="col-6 col-md-1">
            <input
              type="number"
              step="0.01"
              min="0"
              class="form-control"
              id="quantityFilterMin"
              placeholder="Min"
              oninput="filterMaterials()"
            />
          </div>
          <div class="col-6 col-md-1">
            <input
              type="number"
              step="0.01"
              min="0"
              class="form-control"
              id="quantityFilterMax"
              placeholder="Max"
              oninput="filterMaterials()"
            />
          </div>
        </div>

        <div class="d-flex justify-content-end mb-3">
          <a href="add-material" class="btn btn-success">
            <i class="fas fa-plus"></i> Add Material
          </a>
        </div>

        <div class="table-container mb-4">
          <table class="table" id="materialsTable">
            <thead>
              <tr>
                <th style="width: 40px">#</th>
                <th>Name</th>
                <th>Unit</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Supplier</th>
                <th>Category</th>
                <th>Active</th>
                <th style="width: 140px">Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="material" items="${materials}" varStatus="status">
                <tr
                  data-index="${status.index + 1}"
                  data-material-id="${material.materialId}"
                >
                  <td class="row-number">
                    <strong>${status.index + 1}</strong>
                  </td>
                  <td>${material.name}</td>
                  <td>
                    ${material.unitName != null ? material.unitName : '-'}
                  </td>
                  <td>
                    <fmt:formatNumber
                      value="${material.price}"
                      type="number"
                      minFractionDigits="2"
                    />
                  </td>
                  <td>
                    <fmt:formatNumber
                      value="${material.quantity}"
                      type="number"
                      minFractionDigits="2"
                    />
                  </td>
                  <td>
                    ${material.supplierName != null ? material.supplierName :
                    '-'}
                  </td>
                  <td>${material.categoryName}</td>
                  <td>
                    <span
                      style="
                        display: inline-block;
                        padding: 4px 8px;
                        border-radius: 4px;
                        font-size: 13px;
                        font-weight: 500;
                        color: $ {
                          material.status=='active' ? '#065f46' : '#991b1b';
                        }
                        background-color: $ {
                          material.status=='active' ? '#d1fae5' : '#fee2e2';
                        }
                      "
                    >
                      ${material.status == 'active' ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    <div class="action-buttons">
                      <button
                        class="btn btn-info btn-sm view-detail"
                        data-id="${material.materialId}"
                        data-unit-id="${material.unitId}"
                        title="View"
                      >
                        <i class="fas fa-eye"></i>
                      </button>
                      <a
                        href="update-material?id=${material.materialId}&unitId=${material.unitId}"
                        class="btn btn-primary btn-sm"
                        title="Edit"
                      >
                        <i class="fas fa-edit"></i>
                      </a>
                      <button
                        class="btn btn-danger btn-sm delete-material"
                        data-material-id="${material.materialId}"
                        data-bs-toggle="modal"
                        data-bs-target="#confirmDeleteModal"
                        title="Delete"
                      >
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

        <div class="pagination" id="pagination"></div>
      </div>

      <div
        class="modal fade"
        id="confirmDeleteModal"
        tabindex="-1"
        aria-labelledby="confirmDeleteModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="confirmDeleteModalLabel">
                Xác nhận xoá
              </h5>
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              Bạn có chắc muốn xoá sản phẩm này? (Sẽ chuyển trạng thái thành
              Inactive)
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                data-bs-dismiss="modal"
              >
                Huỷ
              </button>
              <button
                type="button"
                id="confirmDeleteBtn"
                class="btn btn-danger"
              >
                Xoá
              </button>
            </div>
          </div>
        </div>
      </div>

      <div id="materialModal" class="modal">
        <div class="modal-content">
          <span class="close" onclick="closeModal()">×</span>
          <h2 style="font-size: 20px; margin-bottom: 16px; color: #1f2937">
            Material Details
          </h2>
          <div class="detail-item">
            <strong>ID:</strong> <span id="modalMaterialId"></span>
          </div>
          <div class="detail-item">
            <strong>Name:</strong> <span id="modalName"></span>
          </div>
          <div class="detail-item">
            <strong>Unit:</strong> <span id="modalUnitName"></span>
          </div>
          <div class="detail-item">
            <strong>Price:</strong> <span id="modalPrice"></span>
          </div>
          <div class="detail-item">
            <strong>Quantity:</strong> <span id="modalQuantity"></span>
          </div>
          <div class="detail-item">
            <strong>Supplier:</strong> <span id="modalSupplierName"></span>
          </div>
          <div class="detail-item">
            <strong>Status:</strong> <span id="modalStatus"></span>
          </div>
          <div class="detail-item">
            <strong>Category:</strong> <span id="modalCategoryName"></span>
          </div>
          <div style="margin-top: 16px">
            <button class="btn btn-primary" onclick="closeModal()">
              Close
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- jQuery -->
    <script
      src="https://code.jquery.com/jquery-3.6.0.min.js"
      integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
      crossorigin="anonymous"
    ></script>

    <!-- Bootstrap Bundle JS (includes Popper) -->
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
      crossorigin="anonymous"
    ></script>

    <!-- Custom Script -->
    <script>
      let materialToDeleteId = null;
      const pageSize = 5;
      let currentPage = 1;
      let allRows = [];
      let filteredRows = [];

      window.addEventListener("DOMContentLoaded", function () {
        allRows = Array.from(
          document.querySelectorAll("#materialsTable tbody tr")
        );
        filteredRows = [...allRows];
        updatePagination();
        updateTable();

        // View Detail click handlers
        document.querySelectorAll(".view-detail").forEach((btn) => {
          btn.addEventListener("click", function (e) {
            e.preventDefault();
            let mid = this.getAttribute("data-id");
            let uid = this.getAttribute("data-unit-id");
            showMaterialDetail(mid, uid);
          });
        });

        // Delete click handlers
        document.querySelectorAll(".delete-material").forEach((btn) => {
          btn.addEventListener("click", function (e) {
            materialToDeleteId = this.getAttribute("data-material-id");
          });
        });

        // Confirm Delete button handler
        document.querySelectorAll(".delete-material").forEach((btn) => {
          btn.addEventListener("click", function (e) {
            materialToDeleteId = this.getAttribute("data-material-id");
          });
        });

        // ------------------------------------------------------------
        // Phần này đã sửa: Confirm Delete button handler
        // ------------------------------------------------------------
        document
          .getElementById("confirmDeleteBtn")
          .addEventListener("click", function () {
            if (!materialToDeleteId) return;

            // Xây dựng URL delete-material (server-side sẽ redirect về list-material)
            const base =
              window.location.origin +
              window.location.pathname.replace(/\/list-material.*$/, "");
            const url = base + "/delete-material?id=" + materialToDeleteId;

            fetch(url, {
              method: "GET",
              // Không cần header 'Accept': 'application/json' vì chúng ta không parse JSON
            })
              .then((response) => {
                // Nếu HTTP status không phải 2xx thì throw lỗi
                if (!response.ok) {
                  throw new Error("Lỗi khi cập nhật trạng thái material");
                }
                // response.redirected == true nếu server đã redirect (đây chính là dấu hiệu thành công)
                // Hoặc chúng ta có thể chỉ cần response.ok → coi như xóa mềm thành công
                // Cập nhật trực tiếp trên giao diện:
                const row = document.querySelector(
                  `#materialsTable tbody tr[data-material-id="${materialToDeleteId}"]`
                );
                if (row) {
                  // Cột “Active” nằm ở vị trí td thứ 8 (index 7)
                  const statusCell = row.querySelector("td:nth-child(8) span");
                  statusCell.textContent = "Inactive";
                  statusCell.style.color = "#991b1b";
                  statusCell.style.backgroundColor = "#fee2e2";
                } else {
                  // Dòng không còn tìm thấy? Có thể reload nếu muốn
                  console.warn(
                    "Không tìm thấy sản phẩm để cập nhật trạng thái trên giao diện."
                  );
                }
                // Hiển thị alert thành công
                showAlert(
                  true,
                  "Cập nhật trạng thái thành công! Sản phẩm đã chuyển sang Inactive."
                );

                setTimeout(() => {
                  window.location.reload();
                }, 1000);
              })
              .catch((error) => {
                console.error("Lỗi:", error);
                showAlert(false, "Có lỗi xảy ra khi cập nhật trạng thái.");
              })
              .finally(() => {
                // Đóng modal bất kể thành công hay thất bại
                const confirmModalElement =
                  document.getElementById("confirmDeleteModal");
                const confirmModal =
                  bootstrap.Modal.getInstance(confirmModalElement);
                if (confirmModal) {
                  confirmModal.hide();
                }
                materialToDeleteId = null;
              });
          });

        // Ensure modal is interactive after show
        const confirmModalElement =
          document.getElementById("confirmDeleteModal");
        confirmModalElement.addEventListener("shown.bs.modal", function () {
          this.style.zIndex = "1050";
          document.querySelector(".modal-backdrop").style.zIndex = "1040";
          this.querySelectorAll("button").forEach((btn) => {
            btn.style.pointerEvents = "auto";
          });
        });
      });

      function showAlert(status, message) {
        const existingAlert = document.querySelector(".custom-alert");
        if (existingAlert) {
          existingAlert.remove();
        }
        const alertDiv = document.createElement("div");
        alertDiv.className = `alert alert-${
          status ? "success" : "danger"
        } alert-dismissible fade show custom-alert`;
        alertDiv.setAttribute("role", "alert");
        alertDiv.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                min-width: 300px;
                z-index: 1060; /* Higher than modal */
                padding: 1rem;
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
                border-radius: 0.375rem;
            `;
        const icon = document.createElement("i");
        icon.className = `fas ${
          status ? "fa-check-circle" : "fa-exclamation-circle"
        } me-2`;
        alertDiv.appendChild(icon);
        const messageText = document.createTextNode(message);
        alertDiv.appendChild(messageText);
        const closeBtn = document.createElement("button");
        closeBtn.type = "button";
        closeBtn.className = "btn-close";
        closeBtn.setAttribute("data-bs-dismiss", "alert");
        closeBtn.setAttribute("aria-label", "Close");
        alertDiv.appendChild(closeBtn);
        document.body.appendChild(alertDiv);
        setTimeout(() => {
          alertDiv.style.opacity = "1";
        }, 100);
        setTimeout(() => {
          if (alertDiv && document.body.contains(alertDiv)) {
            alertDiv.classList.remove("show");
            setTimeout(() => alertDiv.remove(), 150);
          }
        }, 4000);
      }

      function filterMaterials() {
        let qtyMinRaw = document.getElementById("quantityFilterMin").value;
        let qtyMaxRaw = document.getElementById("quantityFilterMax").value;
        let qtyMin = qtyMinRaw === "" ? 0 : parseFloat(qtyMinRaw);
        let qtyMax = qtyMaxRaw === "" ? Infinity : parseFloat(qtyMaxRaw);
        if (isNaN(qtyMin)) qtyMin = 0;
        if (isNaN(qtyMax)) qtyMax = Infinity;

        let searchText = document
          .getElementById("searchInput")
          .value.trim()
          .toLowerCase();
        let categoryValue = document
          .getElementById("categoryFilter")
          .value.trim()
          .toLowerCase();
        let unitValue = document
          .getElementById("unitFilter")
          .value.trim()
          .toLowerCase();
        let supplierValue = document
          .getElementById("supplierFilter")
          .value.trim()
          .toLowerCase();
        let statusValue = document
          .getElementById("activeFilter")
          .value.trim()
          .toLowerCase();

        filteredRows = allRows.filter((row) => {
          let nameText = row.cells[1].textContent.trim().toLowerCase();
          let unitText = row.cells[2].textContent.trim().toLowerCase();
          let qtyText = row.cells[4].textContent.trim().replace(/,/g, "");
          let supplierText = row.cells[5].textContent.trim().toLowerCase();
          let categoryText = row.cells[6].textContent.trim().toLowerCase();
          let statusText = row.cells[7].textContent.trim().toLowerCase();

          let qtyValue = parseFloat(qtyText);
          if (isNaN(qtyValue)) {
            qtyValue = 0;
          }

          let isMatchName = !searchText || nameText.includes(searchText);
          let isMatchCat = !categoryValue || categoryText === categoryValue;
          let isMatchUnit = !unitValue || unitText === unitValue;
          let isMatchSupplier =
            !supplierValue || supplierText === supplierValue;
          let isMatchStatus = !statusValue || statusText === statusValue;
          let isMatchQty = qtyValue >= qtyMin && qtyValue <= qtyMax;

          return (
            isMatchName &&
            isMatchCat &&
            isMatchUnit &&
            isMatchSupplier &&
            isMatchStatus &&
            isMatchQty
          );
        });

        currentPage = 1;
        updatePagination();
        updateTable();
      }

      function updateTable() {
        allRows.forEach((row) => {
          row.style.display = "none";
        });

        const start = (currentPage - 1) * pageSize;
        const end = Math.min(start + pageSize, filteredRows.length);
        const rowsToShow = filteredRows.slice(start, end);

        rowsToShow.forEach((row, index) => {
          row.style.display = "";
          const rowNumberCell = row.querySelector(".row-number strong");
          rowNumberCell.textContent = start + index + 1;
        });
      }

      function updatePagination() {
        const totalPages = Math.ceil(filteredRows.length / pageSize);
        const pagination = document.getElementById("pagination");
        pagination.innerHTML = "";

        if (totalPages <= 1) return;

        if (currentPage > 1) {
          const first = document.createElement("a");
          first.href = "#";
          first.innerHTML = '<i class="fas fa-angle-double-left"></i>';
          first.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage = 1;
            updatePagination();
            updateTable();
          });
          pagination.appendChild(first);

          const prev = document.createElement("a");
          prev.href = "#";
          prev.innerHTML = '<i class="fas fa-angle-left"></i>';
          prev.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage--;
            updatePagination();
            updateTable();
          });
          pagination.appendChild(prev);
        } else {
          const disabledFirst = document.createElement("span");
          disabledFirst.className = "text-muted me-2";
          disabledFirst.innerHTML = '<i class="fas fa-angle-double-left"></i>';
          pagination.appendChild(disabledFirst);

          const disabledPrev = document.createElement("span");
          disabledPrev.className = "text-muted me-2";
          disabledPrev.innerHTML = '<i class="fas fa-angle-left"></i>';
          pagination.appendChild(disabledPrev);
        }

        if (totalPages <= 7) {
          for (let i = 1; i <= totalPages; i++) {
            if (i === currentPage) {
              const current = document.createElement("span");
              current.className = "px-2 mx-1 bg-primary text-white rounded";
              current.textContent = i;
              pagination.appendChild(current);
            } else {
              const pageLink = document.createElement("a");
              pageLink.href = "#";
              pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
              pageLink.textContent = i;
              pageLink.addEventListener("click", (e) => {
                e.preventDefault();
                currentPage = i;
                updatePagination();
                updateTable();
              });
              pagination.appendChild(pageLink);
            }
          }
        } else {
          if (currentPage <= 4) {
            for (let i = 1; i <= 5; i++) {
              if (i === currentPage) {
                const current = document.createElement("span");
                current.className = "px-2 mx-1 bg-primary text-white rounded";
                current.textContent = i;
                pagination.appendChild(current);
              } else {
                const pageLink = document.createElement("a");
                pageLink.href = "#";
                pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                pageLink.textContent = i;
                pageLink.addEventListener("click", (e) => {
                  e.preventDefault();
                  currentPage = i;
                  updatePagination();
                  updateTable();
                });
                pagination.appendChild(pageLink);
              }
            }
            if (totalPages > 6) {
              const dots = document.createElement("span");
              dots.className = "px-2 mx-1 text-muted";
              dots.textContent = "...";
              pagination.appendChild(dots);

              const last = document.createElement("a");
              last.href = "#";
              last.className = "px-2 mx-1 text-decoration-none text-dark";
              last.textContent = totalPages;
              last.addEventListener("click", (e) => {
                e.preventDefault();
                currentPage = totalPages;
                updatePagination();
                updateTable();
              });
              pagination.appendChild(last);
            }
          } else if (currentPage >= totalPages - 3) {
            const first = document.createElement("a");
            first.href = "#";
            first.className = "px-2 mx-1 text-decoration-none text-dark";
            first.textContent = "1";
            first.addEventListener("click", (e) => {
              e.preventDefault();
              currentPage = 1;
              updatePagination();
              updateTable();
            });
            pagination.appendChild(first);

            if (totalPages > 6) {
              const dots = document.createElement("span");
              dots.className = "px-2 mx-1 text-muted";
              dots.textContent = "...";
              pagination.appendChild(dots);
            }

            for (let i = totalPages - 4; i <= totalPages; i++) {
              if (i === currentPage) {
                const current = document.createElement("span");
                current.className = "px-2 mx-1 bg-primary text-white rounded";
                current.textContent = i;
                pagination.appendChild(current);
              } else {
                const pageLink = document.createElement("a");
                pageLink.href = "#";
                pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                pageLink.textContent = i;
                pageLink.addEventListener("click", (e) => {
                  e.preventDefault();
                  currentPage = i;
                  updatePagination();
                  updateTable();
                });
                pagination.appendChild(pageLink);
              }
            }
          } else {
            const first = document.createElement("a");
            first.href = "#";
            first.className = "px-2 mx-1 text-decoration-none text-dark";
            first.textContent = "1";
            first.addEventListener("click", (e) => {
              e.preventDefault();
              currentPage = 1;
              updatePagination();
              updateTable();
            });
            pagination.appendChild(first);

            const dots1 = document.createElement("span");
            dots1.className = "px-2 mx-1 text-muted";
            dots1.textContent = "...";
            pagination.appendChild(dots1);

            for (let i = currentPage - 1; i <= currentPage + 1; i++) {
              if (i === currentPage) {
                const current = document.createElement("span");
                current.className = "px-2 mx-1 bg-primary text-white rounded";
                current.textContent = i;
                pagination.appendChild(current);
              } else {
                const pageLink = document.createElement("a");
                pageLink.href = "#";
                pageLink.className = "px-2 mx-1 text-decoration-none text-dark";
                pageLink.textContent = i;
                pageLink.addEventListener("click", (e) => {
                  e.preventDefault();
                  currentPage = i;
                  updatePagination();
                  updateTable();
                });
                pagination.appendChild(pageLink);
              }
            }

            const dots2 = document.createElement("span");
            dots2.className = "px-2 mx-1 text-muted";
            dots2.textContent = "...";
            pagination.appendChild(dots2);

            const last = document.createElement("a");
            last.href = "#";
            last.className = "px-2 mx-1 text-decoration-none text-dark";
            last.textContent = totalPages;
            last.addEventListener("click", (e) => {
              e.preventDefault();
              currentPage = totalPages;
              updatePagination();
              updateTable();
            });
            pagination.appendChild(last);
          }
        }

        if (currentPage < totalPages) {
          const next = document.createElement("a");
          next.href = "#";
          next.innerHTML = '<i class="fas fa-angle-right"></i>';
          next.className = "ms-2 text-decoration-none text-dark";
          next.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage++;
            updatePagination();
            updateTable();
          });
          pagination.appendChild(next);

          const lastBtn = document.createElement("a");
          lastBtn.href = "#";
          lastBtn.innerHTML = '<i class="fas fa-angle-double-right"></i>';
          lastBtn.className = "ms-2 text-decoration-none text-dark";
          lastBtn.addEventListener("click", (e) => {
            e.preventDefault();
            currentPage = totalPages;
            updatePagination();
            updateTable();
          });
          pagination.appendChild(lastBtn);
        } else {
          const disabledNext = document.createElement("span");
          disabledNext.className = "ms-2 text-muted";
          disabledNext.innerHTML = '<i class="fas fa-angle-right"></i>';
          pagination.appendChild(disabledNext);

          const disabledLast = document.createElement("span");
          disabledLast.className = "ms-2 text-muted";
          disabledLast.innerHTML = '<i class="fas fa-angle-double-right"></i>';
          pagination.appendChild(disabledLast);
        }
      }

      function closeModal() {
        document.getElementById("materialModal").style.display = "none";
      }

      function showMaterialDetail(materialId, unitId) {
        if (!materialId) {
          alert("Material ID is missing.");
          return;
        }
        const base =
          window.location.origin +
          window.location.pathname.replace(/\/list-material.*$/, "");
        const url =
          base +
          "/detail-material?id=" +
          materialId +
          (unitId ? "&unitId=" + unitId : "");

        fetch(url, {
          method: "GET",
          headers: { Accept: "application/json" },
        })
          .then((resp) => {
            if (!resp.ok) throw new Error("HTTP status " + resp.status);
            return resp.json();
          })
          .then((data) => {
            document.getElementById("modalMaterialId").textContent =
              data.materialId;
            document.getElementById("modalName").textContent = data.name;
            document.getElementById("modalUnitName").textContent =
              data.unitName || "-";
            document.getElementById("modalPrice").textContent =
              data.price != null
                ? parseFloat(data.price).toFixed(2).replace(".", ",")
                : "-";
            document.getElementById("modalQuantity").textContent =
              data.quantity != null
                ? parseFloat(data.quantity).toFixed(2).replace(".", ",")
                : "-";
            document.getElementById("modalSupplierName").textContent =
              data.supplierName || "No Supplier";
            document.getElementById("modalStatus").textContent = data.status;
            document.getElementById("modalCategoryName").textContent =
              data.categoryName;

            document.getElementById("materialModal").style.display = "block";
          })
          .catch((err) => {
            console.error("Error fetching material detail:", err);
            alert("Cannot load material details.");
          });
      }
    </script>
  </body>
</html>
