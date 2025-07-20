/*
 * Updated createorder.js - With Material Autocomplete and Image Support
 */

let itemCounter = 0;

function addOrderItem() {
  itemCounter++;
  const container = document.getElementById("orderItemsContainer");
  const noMsg = document.getElementById("noItemsMessage");
  if (noMsg) noMsg.style.display = "none";

  const itemHtml = `
    <div class="order-item" data-item-id="${itemCounter}">
        <button type="button" class="remove-item-btn" onclick="removeOrderItem(this)">
            <i class="fas fa-times"></i>
        </button>
        <div class="item-header">
            <span class="item-number">Item</span>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label for="material">Material <span class="required">*</span></label>
                <div class="autocomplete-container">
                    <div class="material-input-wrapper">
                        <input type="text" 
                               class="form-control material-input" 
                               placeholder="Search for materials..."
                               autocomplete="off"
                               oninput="handleMaterialSearch(this)"
                               onfocus="showMaterialDropdown(this)"
                               onblur="hideMaterialDropdown(this)"
                               required>
                        <button type="button" class="clear-material-btn" onclick="clearMaterialInput(this)" style="display: none;">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <input type="hidden" name="material[]" class="material-id-input" value="">
                    <div class="material-dropdown" style="display: none;">
                        <div class="dropdown-content">
                            <!-- Dropdown items will be populated here -->
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="unit">Unit <span class="required">*</span></label>
                <input type="text" class="form-control unit-display" value="Select Material First" readonly>
                <input type="hidden" name="unit[]" class="unit-id-input" value="">
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label for="quantity">Quantity <span class="required">*</span></label>
                <div class="quantity-container">
                    <div class="quantity-controls">
                        <button type="button" class="quantity-btn" onclick="adjustQuantity(this, -1)">-</button>
                        <input type="number" name="quantity[]" class="form-control quantity-input" value="1" min="1" max="9999" required onchange="checkDuplicateItems();" />
                        <button type="button" class="quantity-btn" onclick="adjustQuantity(this, 1)">+</button>
                    </div>
                </div>
            </div>
        </div>
    </div>`;

  container.insertAdjacentHTML("beforeend", itemHtml);
  renumberItems();

  // Initialize autocomplete for the new item
  initializeAutocomplete();
}

function handleMaterialSearch(input) {
  const searchTerm = input.value.toLowerCase().trim();
  const dropdown = input
    .closest(".autocomplete-container")
    .querySelector(".material-dropdown");
  const dropdownContent = dropdown.querySelector(".dropdown-content");

  // Clear previous results
  dropdownContent.innerHTML = "";

  if (searchTerm === "") {
    // Show all materials when input is empty
    displayMaterialOptions(dropdownContent, allMaterials);
  } else {
    // Filter materials based on search term
    const filteredMaterials = allMaterials.filter((material) =>
      material.name.toLowerCase().includes(searchTerm)
    );
    displayMaterialOptions(dropdownContent, filteredMaterials);
  }

  // Show dropdown
  dropdown.style.display = "block";
}

function displayMaterialOptions(container, materials) {
  if (materials.length === 0) {
    container.innerHTML =
      '<div class="dropdown-item no-results">No materials found</div>';
    return;
  }

  materials.forEach((material) => {
    const itemDiv = document.createElement("div");
    itemDiv.className = "dropdown-item";
    itemDiv.innerHTML = `
            <div class="material-option" data-material-id="${
              material.materialId
            }" 
                 data-unit-id="${material.unitId}" 
                 data-unit-name="${material.unitName}">
                <div class="material-image">
                    <img src="${
                      material.image || "images/default-material.jpg"
                    }" 
                         alt="${material.name}" 
                         onerror="this.src='images/default-material.jpg'">
                </div>
                <div class="material-info">
                    <div class="material-name">${material.name}</div>
                    <div class="material-details">
                        <span class="material-unit">${material.unitName}</span>
                    </div>
                </div>
            </div>
        `;

    // Add click event
    itemDiv.addEventListener("click", function (e) {
      e.preventDefault();
      selectMaterial(this.querySelector(".material-option"));
    });

    container.appendChild(itemDiv);
  });
}

function selectMaterial(materialOption) {
  const autocompleteContainer = materialOption.closest(
    ".autocomplete-container"
  );
  const materialInput = autocompleteContainer.querySelector(".material-input");
  const materialIdInput =
    autocompleteContainer.querySelector(".material-id-input");
  const unitDisplay = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-display");
  const unitIdInput = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-id-input");
  const dropdown = autocompleteContainer.querySelector(".material-dropdown");
  const clearBtn = autocompleteContainer.querySelector(".clear-material-btn");

  // Get material data
  const materialId = materialOption.dataset.materialId;
  const materialName =
    materialOption.querySelector(".material-name").textContent;
  const unitId = materialOption.dataset.unitId;
  const unitName = materialOption.dataset.unitName;

  // Set values
  materialInput.value = materialName;
  materialIdInput.value = materialId;
  unitDisplay.value = unitName;
  unitIdInput.value = unitId;

  // Disable input và show clear button
  materialInput.disabled = true;
  materialInput.classList.add("material-selected");
  clearBtn.style.display = "block";

  // Hide dropdown
  dropdown.style.display = "none";

  // Trigger validation
  checkDuplicateItems();
}

function clearMaterialInput(clearBtn) {
  const autocompleteContainer = clearBtn.closest(".autocomplete-container");
  const materialInput = autocompleteContainer.querySelector(".material-input");
  const materialIdInput =
    autocompleteContainer.querySelector(".material-id-input");
  const unitDisplay = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-display");
  const unitIdInput = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-id-input");

  // Clear all values
  materialInput.value = "";
  materialIdInput.value = "";
  unitDisplay.value = "Select Material First";
  unitIdInput.value = "";

  // Enable input và hide clear button
  materialInput.disabled = false;
  materialInput.classList.remove("material-selected");
  clearBtn.style.display = "none";

  // Focus vào input
  setTimeout(() => {
    materialInput.focus();
  }, 100);

  // Trigger validation
  checkDuplicateItems();
}

function showMaterialDropdown(input) {
  const dropdown = input
    .closest(".autocomplete-container")
    .querySelector(".material-dropdown");
  const dropdownContent = dropdown.querySelector(".dropdown-content");

  // If dropdown is empty, populate with all materials
  if (dropdownContent.children.length === 0) {
    displayMaterialOptions(dropdownContent, allMaterials);
  }

  dropdown.style.display = "block";
}

function hideMaterialDropdown(input) {
  // Use setTimeout to allow click events to fire first
  setTimeout(() => {
    const dropdown = input
      .closest(".autocomplete-container")
      .querySelector(".material-dropdown");
    dropdown.style.display = "none";

    // Validate selection
    validateMaterialSelection(input);
  }, 200);
}

function validateMaterialSelection(input) {
  const autocompleteContainer = input.closest(".autocomplete-container");
  const materialIdInput =
    autocompleteContainer.querySelector(".material-id-input");
  const unitDisplay = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-display");
  const unitIdInput = autocompleteContainer
    .closest(".order-item")
    .querySelector(".unit-id-input");

  // If no material is selected (hidden input is empty), clear everything
  if (!materialIdInput.value) {
    input.value = "";
    unitDisplay.value = "Select Material First";
    unitIdInput.value = "";
  }
}

function clearMaterialSelection(orderItem) {
  const materialInput = orderItem.querySelector(".material-input");
  const materialIdInput = orderItem.querySelector(".material-id-input");
  const unitDisplay = orderItem.querySelector(".unit-display");
  const unitIdInput = orderItem.querySelector(".unit-id-input");
  const clearBtn = orderItem.querySelector(".clear-material-btn");

  materialInput.value = "";
  materialIdInput.value = "";
  unitDisplay.value = "Select Material First";
  unitIdInput.value = "";

  // Enable input và hide clear button
  materialInput.disabled = false;
  materialInput.classList.remove("material-selected");
  if (clearBtn) clearBtn.style.display = "none";
}

function removeOrderItem(btn) {
  const item = btn.closest(".order-item");
  item.remove();

  const container = document.getElementById("orderItemsContainer");
  const remaining = container.querySelectorAll(".order-item");
  if (remaining.length === 0) {
    document.getElementById("noItemsMessage").style.display = "block";
  }

  renumberItems();
  checkDuplicateItems();
}

function renumberItems() {
  document.querySelectorAll(".order-item").forEach((item, index) => {
    const label = item.querySelector(".item-number");
    if (label) label.textContent = `Item #${index + 1}`;
  });
}

function adjustQuantity(button, change) {
  const quantityInput = button.parentElement.querySelector(".quantity-input");
  let currentValue = parseInt(quantityInput.value) || 1;
  let newValue = currentValue + change;

  if (newValue < 1) newValue = 1;
  if (newValue > 9999) newValue = 9999;

  quantityInput.value = newValue;

  // Trigger validation
  checkDuplicateItems();
}

function cancelOrder() {
  showCancelModal();
}

// Hàm kiểm tra và cảnh báo về duplicate items
function checkDuplicateItems() {
  const items = document.querySelectorAll(".order-item");
  const duplicateGroups = new Map();

  // Reset tất cả warning trước đó
  items.forEach((item) => {
    item.classList.remove("duplicate-warning");
    const existingWarning = item.querySelector(".duplicate-warning-text");
    if (existingWarning) {
      existingWarning.remove();
    }
  });

  // Tìm các item trùng lặp (chỉ dựa trên materialId)
  items.forEach((item, index) => {
    const materialIdInput = item.querySelector(".material-id-input");
    const materialId = materialIdInput.value;

    if (materialId) {
      if (!duplicateGroups.has(materialId)) {
        duplicateGroups.set(materialId, []);
      }
      duplicateGroups.get(materialId).push({ item, index });
    }
  });

  // Hiển thị warning cho các nhóm trùng lặp
  duplicateGroups.forEach((group, materialId) => {
    if (group.length > 1) {
      // Tính tổng số lượng của nhóm trùng lặp
      let totalQuantity = 0;
      const materialName = group[0].item.querySelector(".material-input").value;
      const unitName = group[0].item.querySelector(".unit-display").value;

      group.forEach(({ item }) => {
        const quantityInput = item.querySelector("input[name='quantity[]']");
        const quantity = parseInt(quantityInput.value) || 0;
        totalQuantity += quantity;

        // Thêm class warning
        item.classList.add("duplicate-warning");
      });

      // Thêm thông báo warning vào item đầu tiên của nhóm
      const firstItem = group[0].item;
      const warningText = document.createElement("div");
      warningText.className = "duplicate-warning-text";
      warningText.innerHTML = `
                <i class="fas fa-exclamation-triangle"></i>
                Duplicate material detected! "${materialName}" appears ${group.length} times 
                (Total: ${totalQuantity} ${unitName}). These will be merged when saved.
            `;

      const itemHeader = firstItem.querySelector(".item-header");
      itemHeader.appendChild(warningText);
    }
  });
}

function showCancelModal() {
  const modal = document.getElementById("cancelModal");
  modal.style.display = "flex";
  setTimeout(() => {
    modal.classList.add("show");
  }, 10);
}

function hideCancelModal() {
  const modal = document.getElementById("cancelModal");
  modal.classList.remove("show");
  setTimeout(() => {
    modal.style.display = "none";
  }, 300);
}

function confirmCancel() {
  //    window.location.reload();
  window.location.href = "orderlist";
}

function showDuplicateModal() {
  const modal = document.getElementById("duplicateModal");
  if (modal) {
    modal.style.display = "flex";
    setTimeout(() => {
      modal.classList.add("show");
    }, 10);
  }
}

function hideDuplicateModal() {
  const modal = document.getElementById("duplicateModal");
  if (modal) {
    modal.classList.remove("show");
    setTimeout(() => {
      modal.style.display = "none";
    }, 300);
  }
}

function confirmDuplicateSubmit() {
  hideDuplicateModal();
  setTimeout(() => {
    document.getElementById("orderForm").submit();
  }, 300);
}

// Initialize autocomplete functionality
function initializeAutocomplete() {
  // Add CSS for autocomplete if not already added
  if (!document.getElementById("autocomplete-styles")) {
    addAutocompleteStyles();
  }

  // Close dropdown when clicking outside
  document.addEventListener("click", function (e) {
    if (!e.target.closest(".autocomplete-container")) {
      document.querySelectorAll(".material-dropdown").forEach((dropdown) => {
        dropdown.style.display = "none";
      });
    }
  });
}

function addAutocompleteStyles() {
  const style = document.createElement("style");
  style.id = "autocomplete-styles";
  style.textContent = `
        .autocomplete-container {
            position: relative;
        }
        
        .material-input-wrapper {
            position: relative;
            display: flex;
            align-items: center;
        }
        
        .material-input.material-selected {
            background-color: #f8f9fa !important;
            cursor: default;
        }
        
        .clear-material-btn {
            position: absolute;
            right: 8px;
            top: 47%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #6c757d;
            cursor: pointer;
            border-radius: 50%;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 10;
        }
        
        .clear-material-btn:hover {
            background-color: #e9ecef;
            color: #495057;
        }
        
        .clear-material-btn i {
            font-size: 12px;
        }
        
        .material-dropdown {
            position: absolute;
            top: 100%;
            left: 0;
            right: 0;
            background: white;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            max-height: 300px;
            overflow-y: auto;
        }
        
        .dropdown-content {
            padding: 0;
        }
        
        .dropdown-item {
            padding: 0;
            cursor: pointer;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .dropdown-item:last-child {
            border-bottom: none;
        }
        
        .dropdown-item:hover {
            background-color: #f8f9fa;
        }
        
        .dropdown-item.no-results {
            padding: 12px;
            text-align: center;
            color: #6c757d;
            font-style: italic;
        }
        
        .material-option {
            display: flex;
            align-items: center;
            padding: 12px;
            gap: 12px;
        }
        
        .material-image {
            width: 50px;
            height: 50px;
            border-radius: 4px;
            overflow: hidden;
            background: #f8f9fa;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }
        
        .material-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .material-info {
            flex: 1;
            min-width: 0;
        }
        
        .material-name {
            font-weight: 500;
            color: #333;
            margin-bottom: 4px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        .material-details {
            display: flex;
            gap: 12px;
            font-size: 12px;
            color: #666;
        }
        
        .material-code {
            background: #e9ecef;
            padding: 2px 6px;
            border-radius: 3px;
            font-family: monospace;
        }
        
        .material-unit {
            background: #d4edda;
            padding: 2px 6px;
            border-radius: 3px;
            color: #155724;
        }
        
        .unit-display {
            background-color: #f8f9fa !important;
            cursor: not-allowed;
        }
        
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .material-option {
                padding: 8px;
                gap: 8px;
            }
            
            .material-image {
                width: 40px;
                height: 40px;
            }
            
            .material-name {
                font-size: 14px;
            }
            
            .material-details {
                font-size: 11px;
                gap: 8px;
            }
        }
    `;
  document.head.appendChild(style);
}

document.addEventListener("DOMContentLoaded", function () {
  // Initialize autocomplete
  initializeAutocomplete();

  // Nếu có items được restore từ server (khi có lỗi), cần xử lý lại
  document.querySelectorAll(".material-input").forEach((input) => {
    if (input.value) {
      // Find matching material and restore selection
      const materialName = input.value;
      const matchingMaterial = allMaterials.find(
        (m) => m.name === materialName
      );
      if (matchingMaterial) {
        const autocompleteContainer = input.closest(".autocomplete-container");
        const materialIdInput =
          autocompleteContainer.querySelector(".material-id-input");
        const unitDisplay = autocompleteContainer
          .closest(".order-item")
          .querySelector(".unit-display");
        const unitIdInput = autocompleteContainer
          .closest(".order-item")
          .querySelector(".unit-id-input");
        const clearBtn = autocompleteContainer.querySelector(
          ".clear-material-btn"
        );

        materialIdInput.value = matchingMaterial.materialId;
        unitDisplay.value = matchingMaterial.unitName;
        unitIdInput.value = matchingMaterial.unitId;

        // Disable input và show clear button
        input.disabled = true;
        input.classList.add("material-selected");
        if (clearBtn) clearBtn.style.display = "block";
      }
    }
  });
});

// Validation khi submit form
document.addEventListener("DOMContentLoaded", function () {
  const orderForm = document.getElementById("orderForm");

  if (orderForm) {
    orderForm.addEventListener("submit", function (e) {
      // Kiểm tra số lượng items
      const orderItems = document.querySelectorAll(".order-item");

      if (orderItems.length === 0) {
        e.preventDefault();
        showErrorModal();
        return false;
      }

      // Kiểm tra tất cả items có material được chọn không
      let hasInvalidItems = false;
      orderItems.forEach((item) => {
        const materialIdInput = item.querySelector(".material-id-input");
        if (!materialIdInput.value) {
          hasInvalidItems = true;
          item.classList.add("error-highlight");
        }
      });

      if (hasInvalidItems) {
        e.preventDefault();
        alert("Please select materials for all items before submitting.");
        return false;
      }

      // Kiểm tra duplicate items và hiển thị modal
      const duplicateItems = document.querySelectorAll(".duplicate-warning");
      if (duplicateItems.length > 0) {
        e.preventDefault();
        showDuplicateModal();
        return false;
      }
    });
  }

  // Tạo modal HTML và thêm vào body
  createErrorModal();
  createDuplicateModal();
  // Thêm CSS cho duplicate warning
  addDuplicateWarningStyles();
});

// Các hàm modal khác giữ nguyên như trước
function createDuplicateModal() {
  const modalHtml = `
        <div id="duplicateModal" class="modal" style="display: none;">
            <div class="modal-card">
                <div class="modal-header">
                    <h2><i class="fas fa-exclamation-triangle" style="color: #ffc107; margin-right: 10px;"></i>Duplicate Items Detected</h2>
                    <span class="close" onclick="hideDuplicateModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <div class="warning-box">
                        <div class="warning-header">
                            <i class="fas fa-info-circle warning-icon"></i>
                            <span class="warning-title">Items Will Be Merged</span>
                        </div>
                        <div class="warning-content">
                            You have duplicate items with the same material. 
                            <br><br>
                            <strong>What will happen:</strong>
                            <ul style="margin: 10px 0; padding-left: 20px;">
                                <li>Duplicate items will be automatically merged</li>
                                <li>Their quantities will be combined</li>
                                <li>Only one item entry will appear in the final order</li>
                            </ul>
                            <br>
                            Do you want to continue with creating the order?
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" onclick="confirmDuplicateSubmit()">
                        <i class="fas fa-check"></i>
                        Yes, Create Order
                    </button>
                    <button type="button" class="btn btn-gray" onclick="hideDuplicateModal()">
                        <i class="fas fa-edit"></i>
                        Review Items
                    </button>
                </div>
            </div>
        </div>
    `;

  document.body.insertAdjacentHTML("beforeend", modalHtml);
  addDuplicateModalStyles();
}

function createErrorModal() {
  const modalHtml = `
        <div id="errorModal" class="modal" style="display: none;">
            <div class="modal-card">
                <div class="modal-header">
                    <h2><i class="fas fa-exclamation-triangle" style="color: #dc3545; margin-right: 10px;"></i>Error</h2>
                    <span class="close" onclick="hideErrorModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <div class="error-box">
                        <div class="error-header">
                            <i class="fas fa-times-circle error-icon"></i>
                            <span class="error-title">Missing Information</span>
                        </div>
                        <div class="error-content">
                            Please add at least <strong>1 item</strong> to the order before creating it.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="hideErrorModalAndAddItem()">
                        <i class="fas fa-plus"></i>
                        Add Item
                    </button>
                    <button type="button" class="btn btn-gray" onclick="hideErrorModal()">
                        <i class="fas fa-times"></i>
                        Close
                    </button>
                </div>
            </div>
        </div>
    `;

  document.body.insertAdjacentHTML("beforeend", modalHtml);
  addErrorModalStyles();
}

function showErrorModal() {
  const modal = document.getElementById("errorModal");
  if (modal) {
    modal.style.display = "flex";
    setTimeout(() => {
      modal.classList.add("show");
    }, 10);
  }
}

function hideErrorModal() {
  const modal = document.getElementById("errorModal");
  if (modal) {
    modal.classList.remove("show");
    setTimeout(() => {
      modal.style.display = "none";
    }, 300);
  }
}

function hideErrorModalAndAddItem() {
  hideErrorModal();
  setTimeout(() => {
    addOrderItem();
    const container = document.getElementById("orderItemsContainer");
    if (container) {
      container.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    }
  }, 350);
}

function addDuplicateWarningStyles() {
  const style = document.createElement("style");
  style.textContent = `
        .duplicate-warning {
            border: 2px solid #ffc107 !important;
            background-color: #fff8e1;
        }
        
        .duplicate-warning-text {
            background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
            border: 1px solid #ffc107;
            border-radius: 6px;
            padding: 8px 12px;
            margin-top: 8px;
            font-size: 13px;
            color: #856404;
            display: flex;
            align-items: center;
        }
        
        .duplicate-warning-text i {
            color: #f39c12;
            margin-right: 8px;
            font-size: 14px;
        }
        
        .duplicate-warning .item-header {
            background-color: #fff8e1;
            border-bottom: 1px solid #ffc107;
        }
        
        .error-highlight {
            border: 2px solid #dc3545 !important;
            background-color: #fff5f5;
        }
    `;
  document.head.appendChild(style);
}

function addDuplicateModalStyles() {
  const style = document.createElement("style");
  style.textContent = `
        .warning-box {
            background: linear-gradient(135deg, #fff8f0 0%, #ffeaa7 100%);
            border: 1px solid #ffc107;
            border-radius: 8px;
            padding: 20px;
            margin: 10px 0;
        }
        
        .warning-box .warning-header {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }
        
        .warning-box .warning-icon {
            font-size: 24px;
            color: #f39c12;
            margin-right: 12px;
        }
        
        .warning-box .warning-title {
            font-size: 18px;
            font-weight: 600;
            color: #856404;
        }
        
        .warning-box .warning-content {
            color: #856404;
            font-size: 16px;
            line-height: 1.5;
            padding-left: 36px;
        }
        
        .warning-box ul {
            color: #744210;
        }
        
        .btn-warning {
            background-color: #ffc107;
            border-color: #ffc107;
            color: #212529;
        }
        
        .btn-warning:hover {
            background-color: #e0a800;
            border-color: #d39e00;
            color: #212529;
        }
    `;
  document.head.appendChild(style);
}

function addErrorModalStyles() {
  const style = document.createElement("style");
  style.textContent = `
        .error-box {
            background: linear-gradient(135deg, #fff5f5 0%, #fed7d7 100%);
            border: 1px solid #feb2b2;
            border-radius: 8px;
            padding: 20px;
            margin: 10px 0;
        }
        
        .error-header {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }
        
        .error-icon {
            font-size: 24px;
            color: #e53e3e;
            margin-right: 12px;
        }
        
        .error-title {
            font-size: 18px;
            font-weight: 600;
            color: #742a2a;
        }
        
        .error-content {
            color: #744210;
            font-size: 16px;
            line-height: 1.5;
            padding-left: 36px;
        }
        
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
            color: white;
        }
        
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
        
        .btn-gray {
            background-color: #6c757d;
            border-color: #6c757d;
            color: white;
        }
        
        .btn-gray:hover {
            background-color: #545b62;
            border-color: #545b62;
        }
        
        .btn {
            padding: 10px 16px;
            margin: 0 5px;
            border: 1px solid;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            display: inline-flex;
            align-items: center;
            text-decoration: none;
        }
        
        .btn i {
            margin-right: 8px;
        }
    `;
  document.head.appendChild(style);
}

// Đóng modal khi click outside hoặc nhấn ESC
document.addEventListener("DOMContentLoaded", function () {
  document.addEventListener("click", function (e) {
    if (e.target && e.target.id === "cancelModal") {
      hideCancelModal();
    }
    if (e.target && e.target.id === "errorModal") {
      hideErrorModal();
    }
    if (e.target && e.target.id === "duplicateModal") {
      hideDuplicateModal();
    }
  });

  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") {
      const cancelModal = document.getElementById("cancelModal");
      const errorModal = document.getElementById("errorModal");
      const duplicateModal = document.getElementById("duplicateModal");

      if (cancelModal && cancelModal.style.display === "flex") {
        hideCancelModal();
      }
      if (errorModal && errorModal.style.display === "flex") {
        hideErrorModal();
      }
      if (duplicateModal && duplicateModal.style.display === "flex") {
        hideDuplicateModal();
      }
    }
  });
});