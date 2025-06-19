/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let itemCounter = 0;

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
                <label for="status">Category <span class="required">*</span></label>
                <select class="form-control" name="category[]" onchange="updateMaterials(this)" required>
                    <option value="">Select Category</option>
                    ${categories.map(c => `<option value="${c.categoryId}">${c.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label for="status">Material <span class="required">*</span></label>
                <select class="form-control" name="material[]" onchange="checkDuplicateItems()" disabled required>
                    <option value="">Select Category First</option>
                </select>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label for="status">Unit <span class="required">*</span></label>
                <select class="form-control" name="unit[]" onchange="checkDuplicateItems()" required>
                    <option value="">Select Unit</option>
                    ${units.map(u => `<option value="${u.subUnitId}">${u.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label for="status">Quantity <span class="required">*</span></label>
                <div class="quantity-controls">
                    <button type="button" class="quantity-btn" onclick="adjustQuantity(this, -1)">-</button>
                    <input type="number" name="quantity[]" class="form-control quantity-input" value="1" min="1" max="9999" required />
                    <button type="button" class="quantity-btn" onclick="adjustQuantity(this, 1)">+</button>
                </div>
            </div>
        </div>
    </div>`;

    container.insertAdjacentHTML("beforeend", itemHtml);
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
}

function cancelOrder() {
    showCancelModal();
}

function updateMaterials(selectEl) {
    const itemEl = selectEl.closest(".order-item");
    const materialSelect = itemEl.querySelector("select[name='material[]']");
    const categoryId = selectEl.value;

    if (!categoryId) {
        materialSelect.innerHTML = '<option value="">Select Material</option>';
        materialSelect.disabled = true;
        return;
    }

    // AJAX bằng jQuery
    $.ajax({
        url: 'get-materials',
        type: 'GET',
        data: {categoryId: categoryId},
        dataType: 'json',
        success: function (data) {
            materialSelect.innerHTML = '<option value="">Select Material</option>';
            data.forEach(material => {
                const option = document.createElement("option");
                option.value = material.materialId;
                option.textContent = material.name;
                materialSelect.appendChild(option);
            });
            materialSelect.disabled = false;
        },
        error: function () {
            materialSelect.innerHTML = '<option value="">Error loading</option>';
            materialSelect.disabled = true;
        }
    });
}

// Hàm kiểm tra và cảnh báo về duplicate items
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

// Hàm hiển thị modal duplicate confirmation
function showDuplicateModal() {
    const modal = document.getElementById('duplicateModal');
    if (modal) {
        modal.style.display = 'flex';
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }
}

// Hàm ẩn modal duplicate confirmation
function hideDuplicateModal() {
    const modal = document.getElementById('duplicateModal');
    if (modal) {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);
    }
}

// Hàm xác nhận submit với duplicate items
function confirmDuplicateSubmit() {
    hideDuplicateModal();
    // Submit form trực tiếp
    setTimeout(() => {
        document.getElementById('orderForm').submit();
    }, 300);
}

// Validation khi submit form
document.addEventListener('DOMContentLoaded', function() {
    const orderForm = document.getElementById('orderForm');
    
    if (orderForm) {
        orderForm.addEventListener('submit', function(e) {
            // Kiểm tra số lượng items
            const orderItems = document.querySelectorAll('.order-item');
            
            if (orderItems.length === 0) {
                e.preventDefault(); // Ngăn không cho submit form
                
                // Hiển thị modal thông báo lỗi
                showErrorModal();
                
                return false;
            }
            
            // Kiểm tra duplicate items và hiển thị modal thay vì alert
            const duplicateItems = document.querySelectorAll('.duplicate-warning');
            if (duplicateItems.length > 0) {
                e.preventDefault(); // Ngăn submit form
                showDuplicateModal(); // Hiển thị modal confirmation
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
    
    // Thêm modal vào body
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Thêm CSS cho duplicate modal
    addDuplicateModalStyles();
}

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
                        Close
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

// Hàm thêm CSS cho duplicate warning
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