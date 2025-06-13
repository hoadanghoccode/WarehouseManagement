package model;

import java.sql.Date;

public class Import_note {
    private int importNoteId;
    private int orderId;
    private int userId;
    private int warehouseId;
    private Date createdAt;
    private boolean imported;
    private Date importedAt;

    // Constructors
    public Import_note() {}

    public Import_note(int importNoteId, int orderId, int userId, int warehouseId, Date createdAt, boolean imported, Date importedAt) {
        this.importNoteId = importNoteId;
        this.orderId = orderId;
        this.userId = userId;
        this.warehouseId = warehouseId;
        this.createdAt = createdAt;
        this.imported = imported;
        this.importedAt = importedAt;
    }

    // Getters and Setters
    public int getImportNoteId() { return importNoteId; }
    public void setImportNoteId(int importNoteId) { this.importNoteId = importNoteId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public boolean isImported() { return imported; }
    public void setImported(boolean imported) { this.imported = imported; }
    public Date getImportedAt() { return importedAt; }
    public void setImportedAt(Date importedAt) { this.importedAt = importedAt; }
}