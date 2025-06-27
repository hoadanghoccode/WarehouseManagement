package model;

import java.sql.Date;
import java.util.List;

public class ExportNote {

    private int exportNoteId;
    private int orderId;
    private int userId;
    private Integer warehouseId;
    private String warehouseName;
    private String userName;
    private Date createdAt;
    private String customerName;
    private boolean exported;
    private Date exportedAt;
    private List<ExportNoteDetail> details;
    private boolean hasBackOrder;

    // Getters and Setters
    public int getExportNoteId() {
        return exportNoteId;
    }

    public void setExportNoteId(int exportNoteId) {
        this.exportNoteId = exportNoteId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    public Date getExportedAt() {
        return exportedAt;
    }

    public void setExportedAt(Date exportedAt) {
        this.exportedAt = exportedAt;
    }

    public List<ExportNoteDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExportNoteDetail> details) {
        this.details = details;
    }

    public boolean isHasBackOrder() {
        return hasBackOrder;
    } // Getter

    public void setHasBackOrder(boolean hasBackOrder) {
        this.hasBackOrder = hasBackOrder;
    } // Setter
}
