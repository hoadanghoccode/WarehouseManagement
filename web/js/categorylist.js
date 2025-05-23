/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
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