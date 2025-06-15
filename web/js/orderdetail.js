// Modal functions
window.openApproveModal = function () {
    const modal = document.getElementById('approveOrderModal');
    if (modal) {
        // Lấy giá trị admin note và cập nhật vào form
        const adminNote = document.getElementById('adminNote')?.value || '';
        const approveForm = modal.querySelector('form');
        updateFormWithAdminNote(approveForm, adminNote);
        
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }
};

window.openRejectModal = function () {
    const modal = document.getElementById('rejectOrderModal');
    if (modal) {
        // Lấy giá trị admin note và cập nhật vào form
        const adminNote = document.getElementById('adminNote')?.value || '';
        const rejectForm = modal.querySelector('form');
        updateFormWithAdminNote(rejectForm, adminNote);
        
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }
};

window.closeModal = function (modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }
};

// Function to get admin note value and update form before submission
function updateFormWithAdminNote(form, adminNote) {
    if (!form) return;
    
    // Find or create hidden input for adminNote in the form
    let adminNoteInput = form.querySelector('input[name="adminNote"]');
    if (!adminNoteInput) {
        adminNoteInput = document.createElement('input');
        adminNoteInput.type = 'hidden';
        adminNoteInput.name = 'adminNote';
        form.appendChild(adminNoteInput);
    }
    
    // Update the value
    adminNoteInput.value = adminNote;
    console.log('Admin note updated in form:', adminNote);
}

// Add event listeners when document is ready
document.addEventListener('DOMContentLoaded', function() {
    // Add submit listeners to both forms để đảm bảo admin note được cập nhật
    const approveForm = document.querySelector('#approveOrderModal form');
    const rejectForm = document.querySelector('#rejectOrderModal form');
    
    if (approveForm) {
        approveForm.addEventListener('submit', function(e) {
            const adminNote = document.getElementById('adminNote')?.value || '';
            updateFormWithAdminNote(this, adminNote);
        });
    }
    
    if (rejectForm) {
        rejectForm.addEventListener('submit', function(e) {
            const adminNote = document.getElementById('adminNote')?.value || '';
            updateFormWithAdminNote(this, adminNote);
        });
    }
});

// Close modal with Escape key or click outside
document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') {
        ['approveOrderModal', 'rejectOrderModal'].forEach(id => closeModal(id));
    }
});

window.onclick = function (e) {
    ['approveOrderModal', 'rejectOrderModal'].forEach(id => {
        const modal = document.getElementById(id);
        if (e.target === modal) {
            closeModal(id);
        }
    });
};