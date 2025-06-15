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
                <select class="form-control" name="material[]" disabled required>
                    <option value="">Select Category First</option>
                </select>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label for="status">Unit <span class="required">*</span></label>
                <select class="form-control" name="unit[]" required>
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
    if (confirm("Are you sure to cancel this order?")) {
        window.location.href = "orders.jsp";
    }
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

