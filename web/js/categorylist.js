/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Define modal functions globally - Make sure they're available immediately
function openAddModal() {
    console.log("Opening add modal..."); // Debug
    const modal = document.getElementById('addModal');
    if (modal) {
        modal.style.display = 'flex';
        const nameInput = document.getElementById('addCategoryName');
        if (nameInput) {
            nameInput.focus();
        }
        // Prevent body scroll when modal is open
        document.body.style.overflow = 'hidden';
    } else {
        console.error("Modal 'addModal' not found!");
    }
}

function openUpdateModal(categoryId, categoryName, parentId, status) {
    console.log("Opening update modal...", {categoryId, categoryName, parentId, status});
    const modal = document.getElementById('updateModal');
    if (modal) {
        // Chỉ lưu original data nếu KHÔNG phải từ validation fail
        if (!window.showUpdateModal) {
            modal.dataset.originalName = categoryName;
            modal.dataset.originalParentId = (parentId && parentId !== 'null' && parentId !== 'undefined') ? parentId.toString() : '0';
            modal.dataset.originalStatus = status;
            modal.dataset.originalCategoryId = categoryId;
        }

        // Set form values
        const categoryIdInput = document.getElementById('updateCategoryId');
        const categoryNameInput = document.getElementById('updateCategoryName');
        const parentSelect = document.getElementById('updateParentId');
        const statusSelect = document.getElementById('updateStatus');

        if (categoryIdInput) {
            categoryIdInput.value = categoryId;
        }

        if (categoryNameInput) {
            categoryNameInput.value = categoryName;
        }

        if (parentSelect) {
            // Ẩn option của category đang được update
            const options = parentSelect.querySelectorAll('.parent-option');
            options.forEach(option => {
                if (option.dataset.categoryId === categoryId) {
                    option.style.display = 'none';
                } else {
                    option.style.display = 'block';
                }
            });

            const parentValue = (parentId && parentId !== 'null' && parentId !== 'undefined') ? parentId.toString() : '0';
            parentSelect.value = parentValue;
        }

        if (statusSelect) {
            statusSelect.value = status;
        }

        modal.style.display = 'flex';
        if (categoryNameInput) {
            categoryNameInput.focus();
        }
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
        modal.style.display = 'none';
        // Restore body scroll
        document.body.style.overflow = 'auto';

        // Clear form when closing modal
        if (id === 'addModal') {
            const nameInput = document.getElementById('addCategoryName');
            const parentSelect = document.getElementById('addParentId');
            if (nameInput)
                nameInput.value = '';
            if (parentSelect)
                parentSelect.value = '0';

            // Hoàn toàn xóa tất cả window variables liên quan đến add modal
            clearAddModalData();
        } else if (id === 'updateModal') {
            // KHÔNG restore original values nữa, mà clear hết
            clearAllModalData();

            // Clear form input fields
            const nameInput = document.getElementById('updateCategoryName');
            const parentSelect = document.getElementById('updateParentId');
            const statusSelect = document.getElementById('updateStatus');
            const idInput = document.getElementById('updateCategoryId');

            if (nameInput)
                nameInput.value = '';
            if (parentSelect)
                parentSelect.value = '0';
            if (statusSelect)
                statusSelect.value = 'active';
            if (idInput)
                idInput.value = '';

            // Clear dataset của modal
            delete modal.dataset.originalName;
            delete modal.dataset.originalParentId;
            delete modal.dataset.originalStatus;
            delete modal.dataset.originalCategoryId;
        } else if (id === 'confirmModal') {
            clearConfirmModalData();
        }
    }
}

function clearUpdateModalData() {
    window.showUpdateModal = false;
    window.updateCategoryId = '';
    window.categoryName = '';
    window.parentId = '';
    window.categoryStatus = '';

    // Xóa hoàn toàn các properties khỏi window object
    delete window.showUpdateModal;
    delete window.updateCategoryId;
    delete window.categoryName;
    delete window.parentId;
    delete window.categoryStatus;
}

function clearAddModalData() {
    window.showAddModal = false;
    window.categoryName = '';
    window.parentId = '';
    delete window.showAddModal;
    delete window.categoryName;
    delete window.parentId;
}

function clearAllModalFlags() {
    window.showAddModal = false;
    window.showUpdateModal = false;
    window.showConfirmModal = false;
    window.errorMessage = '';
    window.successMessage = '';
    window.categoryName = '';
    window.parentId = '';
    window.updateCategoryId = '';
    window.categoryStatus = '';
}

function clearAllModalData() {
    clearAddModalData();
    clearUpdateModalData();
    clearConfirmModalData();

    // Clear error và success messages
    window.errorMessage = '';
    window.successMessage = '';
    delete window.errorMessage;
    delete window.successMessage;
}

function clearConfirmModalData() {
    window.showConfirmModal = false;
    delete window.showConfirmModal;
}

function showErrorModal(message) {
    const errorMessageEl = document.getElementById('errorMessage');
    const errorModal = document.getElementById('errorModal');

    if (errorMessageEl && errorModal) {
        errorMessageEl.textContent = String(message);
        errorModal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    } else {
        alert("Error: " + message);
    }
}

function closeErrorModal() {
    const errorModal = document.getElementById('errorModal');
    if (errorModal) {
        errorModal.style.display = 'none';
        // Restore body scroll
        document.body.style.overflow = 'auto';
    }
}

function openConfirmModal() {
    const modal = document.getElementById('confirmModal');
    if (modal) {
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }
}

function closeConfirmModal() {
    closeModal('confirmModal');
}

function debugModals() {
    console.log("=== MODAL DEBUG ===");
    console.log("Add Modal:", document.getElementById('addModal'));
    console.log("Update Modal:", document.getElementById('updateModal'));
    console.log("Error Modal:", document.getElementById('errorModal'));
    console.log("Error Message Element:", document.getElementById('errorMessage'));
    console.log("Window flags:", {
        showAddModal: window.showAddModal,
        showUpdateModal: window.showUpdateModal,
        errorMessage: window.errorMessage,
        categoryName: window.categoryName,
        parentId: window.parentId
    });
    console.log("Functions available:", {
        openAddModal: typeof openAddModal,
        openUpdateModal: typeof openUpdateModal,
        closeModal: typeof closeModal
    });
}

// Assign functions to window object as backup
window.openAddModal = openAddModal;
window.openUpdateModal = openUpdateModal;
window.closeModal = closeModal;
window.showErrorModal = showErrorModal;
window.closeErrorModal = closeErrorModal;

// Close modal when clicking outside
window.onclick = function (event) {
    const errorModal = document.getElementById('errorModal');
    const addModal = document.getElementById('addModal');
    const updateModal = document.getElementById('updateModal');
    const confirmModal = document.getElementById('confirmModal');

    if (event.target === errorModal) {
        closeErrorModal();
    }
    if (event.target === addModal) {
        closeModal('addModal');
    }
    if (event.target === updateModal) {
        closeModal('updateModal');
    }
    if (event.target === confirmModal) {
        confirmModal('confirmModal');
    }
};

// Close modal with Escape key
document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        closeErrorModal();
        closeModal('addModal');
        closeModal('updateModal');
        closeModal('confirmModal');
    }
});

// Add fade in animation
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
    }
`;
document.head.appendChild(style);

// DOM Content Loaded Event
document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM Content Loaded - Setting up subcategory toggles and modal handlers");
    // Debug functions availability
    debugModals();
    const buttons = document.querySelectorAll(".sub-count-btn");
    const visibleTooltips = new Set();
    buttons.forEach(btn => {
        const id = btn.dataset.id;
        const tooltipRow = document.getElementById("tooltip-" + id);
        // Kiểm tra xem button có subcategories không
        const hasSubCategories = !btn.classList.contains('zero-count');
        if (!tooltipRow || !hasSubCategories) {
            return; // Bỏ qua nếu không có tooltip hoặc không có subcategories
        }

        // Hover to show temporarily
        btn.addEventListener("mouseenter", () => {
            if (!visibleTooltips.has(id)) {
                tooltipRow.style.display = "table-row";
                tooltipRow.style.animation = "fadeIn 0.3s ease";
            }
        });
        btn.addEventListener("mouseleave", () => {
            if (!visibleTooltips.has(id)) {
                setTimeout(() => {
                    if (!visibleTooltips.has(id)) {
                        tooltipRow.style.display = "none";
                    }
                }, 300);
            }
        });
        // Keep tooltip visible when hovering over it
        tooltipRow.addEventListener("mouseenter", () => {
            if (!visibleTooltips.has(id)) {
                tooltipRow.style.display = "table-row";
            }
        });
        tooltipRow.addEventListener("mouseleave", () => {
            if (!visibleTooltips.has(id)) {
                setTimeout(() => {
                    if (!visibleTooltips.has(id)) {
                        tooltipRow.style.display = "none";
                    }
                }, 300);
            }
        });
        // Click to toggle persistent visibility
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (visibleTooltips.has(id)) {
                visibleTooltips.delete(id);
                tooltipRow.style.display = "none";
                btn.style.backgroundColor = "#007bff";
            } else {
                visibleTooltips.add(id);
                tooltipRow.style.display = "table-row";
                btn.style.backgroundColor = "#28a745";
            }
        });
    });
    // Auto show modals based on server-side conditions with a slight delay
    setTimeout(function () {
        console.log("Checking for auto-show modals...");
        // Check for showAddModal flag from server
        if (window.showAddModal) {
            console.log("Auto showing add modal due to validation error");
            openAddModal();
            // Restore form values if they exist
            if (window.categoryName) {
                const nameInput = document.getElementById('addCategoryName');
                if (nameInput) {
                    nameInput.value = window.categoryName;
                }
            }
            if (window.parentId) {
                const parentSelect = document.getElementById('addParentId');
                if (parentSelect) {
                    parentSelect.value = window.parentId;
                }
            }
            // KHÔNG clear flag ở đây, để closeModal xử lý
        }

        // Check for showUpdateModal flag from server
        if (window.showUpdateModal) {
            console.log("Auto showing update modal due to validation error");
            openUpdateModal(
                    window.updateCategoryId || '',
                    window.categoryName || '',
                    window.parentId || '0',
                    window.categoryStatus || 'active'
                    );
            // KHÔNG clear flag ở đây, để closeModal xử lý
        }

        if (window.showConfirmModal) {
            console.log("Auto showing confirm modal");
            openConfirmModal();
            // KHÔNG clear flag ở đây, để closeModal xử lý
        }

        // Show error modal if there's an error message
        if (window.errorMessage && window.errorMessage.trim() !== '') {
            console.log("Auto showing error modal:", window.errorMessage);
            showErrorModal(window.errorMessage);
            // Clear the error message after showing
            window.errorMessage = '';
        }

        // Show success message if exists
        if (window.successMessage) {
            alert(window.successMessage);
            // Clear the success message after showing
            window.successMessage = '';
        }

        // Handle URL parameters for status messages
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get("status");
        if (status === "success") {
            alert("Thêm danh mục thành công!");
            // Clear URL parameter để tránh hiện lại khi refresh
            if (window.history.replaceState) {
                const newUrl = window.location.pathname;
                window.history.replaceState({}, '', newUrl);
            }
        } else if (status === "fail") {
            alert("Thêm danh mục thất bại!");
            // Clear URL parameter để tránh hiện lại khi refresh
            if (window.history.replaceState) {
                const newUrl = window.location.pathname;
                window.history.replaceState({}, '', newUrl);
            }
        }
    }, 100);
// Thêm event listener để clear flags khi user navigate đi
//    window.addEventListener('beforeunload', function () {
//        clearAllModalFlags();
//    });

    window.addEventListener('beforeunload', function () {
        clearAllModalData();
    });
// Thêm event listener để clear flags khi trang được load
    window.addEventListener('pageshow', function (event) {
        // Nếu trang được load từ bfcache (back/forward cache)
        if (event.persisted) {
            clearAllModalData();
        }
    });

    const cancelUpdateBtn = document.getElementById('cancelUpdateBtn');
    if (cancelUpdateBtn) {
        cancelUpdateBtn.addEventListener('click', function () {
            closeModal('updateModal');
            clearAllModalData(); // Đảm bảo xoá hoàn toàn các biến lưu
        });
    }
});