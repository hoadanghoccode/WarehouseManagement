/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let itemCounter = 0;

// Load existing order items when page loads
$(document).ready(function() {
    if (existingOrderDetails && existingOrderDetails.length > 0) {
        loadExistingOrderItems();
    }
});

function loadMaterialCategory(materialId, categorySelect, materialSelect) {
    // AJAX call to get material's category
    $.ajax({
        url: 'get-material-category',
        type: 'GET',
        data: { materialId: materialId },
        dataType: 'json',
        success: function(data) {
            console.log('Material category data:', data); // Debug log
            
            if (data && data.categoryId) {
                // Set category value
                categorySelect.value = data.categoryId;
                
                // Load materials for this category and select the current material
                loadMaterialsAndSelect(data.categoryId, materialSelect, materialId);
            }
        },
        error: function(xhr, status, error) {
            console.error('Failed to load material category:', error);
            console.error('Response:', xhr.responseText);
        }
    });
}

// Hàm load materials và set selected material
function loadMaterialsAndSelect(categoryId, materialSelect, selectedMaterialId) {
    $.ajax({
        url: 'get-materials',
        type: 'GET',
        data: {categoryId: categoryId},
        dataType: 'json',
        success: function (data) {
            console.log('Materials loaded:', data); // Debug log
            
            materialSelect.innerHTML = '<option value="">Select Material</option>';
            
            let materialFound = false;
            data.forEach(material => {
                const option = document.createElement("option");
                option.value = material.materialId;
                option.textContent = material.name;
                
                // Set selected nếu là material hiện tại
                if (material.materialId == selectedMaterialId) {
                    option.selected = true;
                    materialFound = true;
                }
                
                materialSelect.appendChild(option);
            });
            
            // Nếu không tìm thấy material trong danh sách, có thể material đã bị xóa
            // Trong trường hợp này, chúng ta vẫn giữ lại để user có thể chọn material khác
            if (!materialFound && selectedMaterialId) {
                console.warn('Selected material not found in category materials');
            }
            
            materialSelect.disabled = false;
            
            // Kiểm tra duplicate sau khi load xong
            checkDuplicateItems();
        },
        error: function (xhr, status, error) {
            console.error('Failed to load materials:', error);
            console.error('Response:', xhr.responseText);
            materialSelect.innerHTML = '<option value="">Error loading materials</option>';
            materialSelect.disabled = true;
        }
    });
}

function loadExistingOrderItems() {
    console.log('Loading existing order items:', existingOrderDetails); // Debug log
    
    const container = document.getElementById("orderItemsContainer");
    const noMsg = document.getElementById("noItemsMessage");
    
    existingOrderDetails.forEach((detail, index) => {
        itemCounter++;
        
        const itemHtml = `
        <div class="order-item" data-item-id="${itemCounter}">
            <button type="button" class="remove-item-btn" onclick="removeOrderItem(this)">
                <i class="fas fa-times"></i>
            </button>
            <div class="item-header">
                <span class="item-number">Item #${index + 1}</span>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Category <span class="required">*</span></label>
                    <select class="form-control" name="category[]" onchange="updateMaterials(this)" required>
                        <option value="">Select Category</option>
                        ${categories.map(c => `<option value="${c.categoryId}">${c.name}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group">
                    <label>Material <span class="required">*</span></label>
                    <select class="form-control" name="material[]" onchange="checkDuplicateItems()" required>
                        <option value="">Loading materials...</option>
                    </select>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Unit <span class="required">*</span></label>
                    <select class="form-control" name="unit[]" onchange="checkDuplicateItems()" required>
                        <option value="">Select Unit</option>
                        ${units.map(u => `<option value="${u.subUnitId}" ${u.subUnitId == detail.subUnitId ? 'selected' : ''}>${u.name}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group">
                    <label>Quantity <span class="required">*</span></label>
                    <div class="quantity-controls">
                        <button type="button" class="quantity-btn" onclick="adjustQuantity(this, -1)">-</button>
                        <input type="number" name="quantity[]" class="form-control quantity-input" value="${detail.quantity}" min="1" max="9999" required />
                        <button type="button" class="quantity-btn" onclick="adjustQuantity(this, 1)">+</button>
                    </div>
                </div>
            </div>
        </div>`;
        
        container.insertAdjacentHTML("beforeend", itemHtml);
        
        // Get references to the newly added item's selects
        const lastItem = container.lastElementChild;
        const categorySelect = lastItem.querySelector("select[name='category[]']");
        const materialSelect = lastItem.querySelector("select[name='material[]']");
        
        // Load category and materials for this item
        loadMaterialCategory(detail.materialId, categorySelect, materialSelect);
    });
    
    if (noMsg) {
        noMsg.style.display = "none";
    }
}

function addOrderItem() {
    itemCounter++;
    const container = document.getElementById("orderItemsContainer");
    const noMsg = document.getElementById("noItemsMessage");
    if (noMsg)
        noMsg.style.display = "none";

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
                <label>Category <span class="required">*</span></label>
                <select class="form-control" name="category[]" onchange="updateMaterials(this)" required>
                    <option value="">Select Category</option>
                    ${categories.map(c => `<option value="${c.categoryId}">${c.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Material <span class="required">*</span></label>
                <select class="form-control" name="material[]" onchange="checkDuplicateItems()" disabled required>
                    <option value="">Select Category First</option>
                </select>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>Unit <span class="required">*</span></label>
                <select class="form-control" name="unit[]" onchange="checkDuplicateItems()" required>
                    <option value="">Select Unit</option>
                    ${units.map(u => `<option value="${u.subUnitId}">${u.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Quantity <span class="required">*</span></label>
                <div class="quantity-controls">
                    <button type="button" class="quantity-btn" onclick="adjustQuantity(this, -1)">-</button>
                    <input type="number" name="quantity[]" class="form-control quantity-input" value="1" min="1" max="9999" required />
                    <button type="button" class="quantity-btn" onclick="adjustQuantity(this, 1)">+</button>
                </div>
            </div>
        </div>
    </div>`;

    container.insertAdjacentHTML("beforeend", itemHtml);
    renumberItems();
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
    checkDuplicateItems(); // Kiểm tra lại duplicate sau khi xóa
}

function renumberItems() {
    document.querySelectorAll(".order-item").forEach((item, index) => {
        const label = item.querySelector(".item-number");
        if (label)
            label.textContent = `Item #${index + 1}`;
    });
}

function adjustQuantity(button, delta) {
    const input = button.closest(".quantity-controls").querySelector("input");
    let val = parseInt(input.value) || 1;
    val = Math.max(1, Math.min(9999, val + delta));
    input.value = val;
    
    // Kiểm tra lại duplicate khi thay đổi quantity
    checkDuplicateItems();
}


function updateMaterials(selectEl) {
    const itemEl = selectEl.closest(".order-item");
    const materialSelect = itemEl.querySelector("select[name='material[]']");
    const categoryId = selectEl.value;

    if (!categoryId) {
        materialSelect.innerHTML = '<option value="">Select Material</option>';
        materialSelect.disabled = true;
        checkDuplicateItems(); // Kiểm tra lại duplicate
        return;
    }

    // Show loading state
    materialSelect.innerHTML = '<option value="">Loading materials...</option>';
    materialSelect.disabled = true;

    // AJAX call to get materials by category
    $.ajax({
        url: 'get-materials',
        type: 'GET',
        data: {categoryId: categoryId},
        dataType: 'json',
        success: function (data) {
            console.log('Materials for category ' + categoryId + ':', data); // Debug log
            
            materialSelect.innerHTML = '<option value="">Select Material</option>';
            data.forEach(material => {
                const option = document.createElement("option");
                option.value = material.materialId;
                option.textContent = material.name;
                materialSelect.appendChild(option);
            });
            materialSelect.disabled = false;
            
            // Kiểm tra duplicate sau khi load materials
            checkDuplicateItems();
        },
        error: function (xhr, status, error) {
            console.error('Failed to load materials for category:', error);
            console.error('Response:', xhr.responseText);
            materialSelect.innerHTML = '<option value="">Error loading materials</option>';
            materialSelect.disabled = true;
        }
    });
}

function checkDuplicateItems() {
    const items = document.querySelectorAll(".order-item");
    const duplicateGroups = new Map();
    
    // Reset tất cả warning trước đó
    items.forEach(item => {
        item.classList.remove('duplicate-warning');
        const existingWarning = item.querySelector('.duplicate-warning-text');
        if (existingWarning) {
            existingWarning.remove();
        }
    });
    
    // Tìm các item trùng lặp
    items.forEach((item, index) => {
        const materialSelect = item.querySelector("select[name='material[]']");
        const unitSelect = item.querySelector("select[name='unit[]']");
        
        const materialId = materialSelect.value;
        const unitId = unitSelect.value;
        
        if (materialId && unitId) {
            const key = `${materialId}_${unitId}`;
            
            if (!duplicateGroups.has(key)) {
                duplicateGroups.set(key, []);
            }
            duplicateGroups.get(key).push({item, index});
        }
    });
    
    // Hiển thị warning cho các nhóm trùng lặp
    duplicateGroups.forEach((group, key) => {
        if (group.length > 1) {
            // Tính tổng số lượng của nhóm trùng lặp
            let totalQuantity = 0;
            group.forEach(({item}) => {
                const quantityInput = item.querySelector("input[name='quantity[]']");
                totalQuantity += parseInt(quantityInput.value) || 0;
                
                // Thêm class warning
                item.classList.add('duplicate-warning');
            });
            
            // Thêm thông báo warning vào item đầu tiên của nhóm
            const firstItem = group[0].item;
            const warningText = document.createElement('div');
            warningText.className = 'duplicate-warning-text';
            warningText.innerHTML = `
                <i class="fas fa-exclamation-triangle"></i>
                Duplicate item detected! This material-unit combination appears ${group.length} times 
                (Total quantity: ${totalQuantity}). These will be merged when saved.
            `;
            
            const itemHeader = firstItem.querySelector('.item-header');
            itemHeader.appendChild(warningText);
        }
    });
}

// Form validation before submit
$("#orderForm").on("submit", function(e) {
    const orderItems = document.querySelectorAll(".order-item");
    
    // Kiểm tra số lượng items
    if (orderItems.length === 0) {
        e.preventDefault();
        showErrorModal();
        return false;
    }
    
    // Validate each item
    let isValid = true;
    orderItems.forEach((item, index) => {
        const category = item.querySelector("select[name='category[]']").value;
        const material = item.querySelector("select[name='material[]']").value;
        const unit = item.querySelector("select[name='unit[]']").value;
        const quantity = item.querySelector("input[name='quantity[]']").value;
        
        if (!category || !material || !unit || !quantity || quantity < 1) {
            isValid = false;
            alert(`Please fill all fields for Item #${index + 1}`);
            return false;
        }
    });
    
    if (!isValid) {
        e.preventDefault();
        return false;
    }
    
    // Kiểm tra duplicate items và hiển thị modal thay vì alert
    const duplicateItems = document.querySelectorAll('.duplicate-warning');
    if (duplicateItems.length > 0) {
        e.preventDefault(); // Ngăn submit form
        showDuplicateModal(); // Hiển thị modal confirmation
        return false;
    }
    
    return true;
});



function cancelOrder() {
    showCancelModal();
}

function showCancelModal() {
    const modal = document.getElementById('cancelModal');
    modal.style.display = 'flex';
    setTimeout(() => {
        modal.classList.add('show');
    }, 10);
}

function hideCancelModal() {
    const modal = document.getElementById('cancelModal');
    modal.classList.remove('show');
    setTimeout(() => {
        modal.style.display = 'none';
    }, 300);
}

function confirmCancel() {
    // Reload trang như yêu cầu
    window.location.reload();
}

function showDuplicateModal() {
    const modal = document.getElementById('duplicateModal');
    if (modal) {
        modal.style.display = 'flex';
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }
}

function hideDuplicateModal() {
    const modal = document.getElementById('duplicateModal');
    if (modal) {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);
    }
}

function confirmDuplicateSubmit() {
    hideDuplicateModal();
    // Submit form trực tiếp
    setTimeout(() => {
        document.getElementById('orderForm').submit();
    }, 300);
}

// Đóng modal khi click outside
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('cancelModal').addEventListener('click', function(e) {
        if (e.target === this) {
            hideCancelModal();
        }
    });

    // Đóng modal khi nhấn ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            hideCancelModal();
        }
    });
});

// Thêm code này vào cuối file createorder.js

// Validation khi submit form
document.addEventListener('DOMContentLoaded', function() {
    const orderForm = document.getElementById('orderForm');
    
    // Tạo modal HTML và thêm vào body
    createErrorModal();
    createDuplicateModal();
    
    // Thêm CSS cho duplicate warning
    addDuplicateWarningStyles();
    
    // Đóng modal khi click outside
    document.addEventListener('click', function(e) {
        if (e.target && e.target.id === 'cancelModal') {
            hideCancelModal();
        }
        if (e.target && e.target.id === 'errorModal') {
            hideErrorModal();
        }
        if (e.target && e.target.id === 'duplicateModal') {
            hideDuplicateModal();
        }
    });

    // Đóng modal khi nhấn ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const cancelModal = document.getElementById('cancelModal');
            const errorModal = document.getElementById('errorModal');
            const duplicateModal = document.getElementById('duplicateModal');
            
            if (cancelModal && cancelModal.style.display === 'flex') {
                hideCancelModal();
            }
            if (errorModal && errorModal.style.display === 'flex') {
                hideErrorModal();
            }
            if (duplicateModal && duplicateModal.style.display === 'flex') {
                hideDuplicateModal();
            }
        }
    });
});

// Hàm tạo modal HTML
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
                        Đóng
                    </button>
                </div>
            </div>
        </div>
    `;
    
    // Thêm modal vào body
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Thêm CSS cho error modal
    addErrorModalStyles();
}

// Hàm tạo duplicate confirmation modal
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
                            You have duplicate items with the same material and unit combination. 
                            <br><br>
                            <strong>What will happen:</strong>
                            <ul style="margin: 10px 0; padding-left: 20px;">
                                <li>Duplicate items will be automatically merged</li>
                                <li>Their quantities will be combined</li>
                                <li>Only one item entry will appear in the final order</li>
                            </ul>
                            <br>
                            Do you want to continue with updating the order?
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" onclick="confirmDuplicateSubmit()">
                        <i class="fas fa-check"></i>
                        Yes, Update Order
                    </button>
                    <button type="button" class="btn btn-gray" onclick="hideDuplicateModal()">
                        <i class="fas fa-edit"></i>
                        Review Items
                    </button>
                </div>
            </div>
        </div>
    `;
    
    // Thêm modal vào body
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Thêm CSS cho duplicate modal
    addDuplicateModalStyles();
}

// Hàm hiển thị modal lỗi
function showErrorModal() {
    const modal = document.getElementById('errorModal');
    if (modal) {
        modal.style.display = 'flex';
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }
}

// Hàm ẩn modal lỗi
function hideErrorModal() {
    const modal = document.getElementById('errorModal');
    if (modal) {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);
    }
}

// Hàm ẩn modal và tự động thêm item
function hideErrorModalAndAddItem() {
    hideErrorModal();
    // Delay một chút để modal đóng trước
    setTimeout(() => {
        addOrderItem(); // Gọi hàm addOrderItem() có sẵn
        
        // Scroll đến item vừa thêm
        const container = document.getElementById("orderItemsContainer");
        if (container) {
            container.scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });
        }
    }, 350);
}

function addDuplicateWarningStyles() {
    const style = document.createElement('style');
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
    `;
    document.head.appendChild(style);
}

// Hàm thêm CSS cho duplicate modal
function addDuplicateModalStyles() {
    const style = document.createElement('style');
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

// Hàm thêm CSS cho error modal
function addErrorModalStyles() {
    const style = document.createElement('style');
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
document.addEventListener('DOMContentLoaded', function() {
    // Đóng modal khi click outside
    document.addEventListener('click', function(e) {
        if (e.target && e.target.id === 'errorModal') {
            hideErrorModal();
        }
    });

    // Đóng modal khi nhấn ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const errorModal = document.getElementById('errorModal');
            if (errorModal && errorModal.style.display === 'flex') {
                hideErrorModal();
            }
        }
    });
});

