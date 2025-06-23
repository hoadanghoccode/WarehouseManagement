<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Material Inventory Audit</title>
        <!-- Bootstrap 5, Font Awesome, jQuery -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
        <style>
            .inventory-item-row {
                background: #f9fafd;
            }
            .reason-row input {
                background: #fffbe6;
            }
        </style>
    </head>
    <body>
        <div class="container py-4">

            <h3 class="mb-4">Material Inventory Audit</h3>

            <!-- Button to open modal -->
            <button class="btn btn-warning mb-4" id="btnOpenAuditModal">
                <i class="fa fa-clipboard-list"></i> Create Audit Record
            </button>

            <!-- Audit modal -->
            <div class="modal fade" id="auditModal" tabindex="-1">
                <div class="modal-dialog modal-lg modal-dialog-scrollable">
                    <div class="modal-content">
                        <div class="modal-header bg-info text-white">
                            <h5 class="modal-title"><i class="fa fa-clipboard-check"></i> Inventory Audit Record</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body" id="auditModalBody">
                            <form id="inventoryAuditForm">
                                <c:forEach var="item" items="${inventoryList}" varStatus="st">
                                    <div class="border rounded p-3 mb-3 inventory-item-row">
                                        <div class="row align-items-center">
                                            <div class="col-2"><b>ID:</b> ${item.materialId}</div>
                                            <div class="col-4"><b>Name:</b> ${item.materialName}</div>
                                            <div class="col-3">
                                                <b>System Quantity:</b>
                                                <span class="sys-qty">${item.availableQty}</span>
                                            </div>
                                            <div class="col-3">
                                                <input type="number" step="0.01" min="0"
                                                       class="form-control actual-qty-input"
                                                       placeholder="Enter actual quantity"
                                                       data-sysqty="${item.availableQty}"
                                                       name="actualQty_${item.materialId}">
                                            </div>
                                        </div>
                                        <div class="row mt-2 d-none reason-row">
                                            <div class="col-12">
                                                <input type="text" class="form-control reason-input"
                                                       name="reason_${item.materialId}"
                                                       placeholder="Enter reason for discrepancy">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="btnSaveAudit">Save Audit</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <!-- Script to handle modal, actual quantity entry, and save -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Show the audit modal
            $('#btnOpenAuditModal').click(function () {
                $('#auditModal').modal('show');
            });

            // When entering actual quantity, check for discrepancy
            $('#auditModal').on('input', '.actual-qty-input', function () {
                var actual = parseFloat($(this).val()) || 0;
                var sys = parseFloat($(this).data('sysqty')) || 0;
                var $block = $(this).closest('.inventory-item-row');
                if (actual !== sys) {
                    $block.find('.reason-row').removeClass('d-none');
                    $block.find('.reason-input').attr('required', true);
                } else {
                    $block.find('.reason-row').addClass('d-none');
                    $block.find('.reason-input').val('').removeAttr('required');
                }
            });

            // Save audit (collect data, send via ajax, handle on backend)
            $('#btnSaveAudit').click(function () {
                let data = [];
                $('#inventoryAuditForm .inventory-item-row').each(function () {
                    let materialId = $(this).find('.actual-qty-input').attr('name').split('_')[1];
                    let sysQty = $(this).find('.actual-qty-input').data('sysqty');
                    let actualQty = $(this).find('.actual-qty-input').val();
                    let reason = $(this).find('.reason-input').val();
                    data.push({
                        materialId: materialId,
                        systemQty: sysQty,
                        actualQty: actualQty,
                        reason: reason
                    });
                });
                // Send to backend via ajax (adjust url to your servlet)
                $.ajax({
                    url: 'inventoryaudit', // Create a servlet to handle this data
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        alert('Saved successfully!');
                        $('#auditModal').modal('hide');
                    },
                    error: function () {
                        alert('An error occurred!');
                    }
                });
            });
        </script>
    </body>
</html>
