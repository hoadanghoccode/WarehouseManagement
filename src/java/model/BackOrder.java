package model;

import java.sql.Date;

public class BackOrder {
    private int backOrderId;
    private int orderDetailId;
    private int materialId;
    private int subUnitId;
    private double requestedQuantity;
    private double remainingQuantity;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String note;

    // Constructors
    public BackOrder() {}

    public BackOrder(int backOrderId, int orderDetailId, int materialId, int subUnitId, double requestedQuantity, double remainingQuantity, String status, Date createdAt, Date updatedAt, String note) {
        this.backOrderId = backOrderId;
        this.orderDetailId = orderDetailId;
        this.materialId = materialId;
        this.subUnitId = subUnitId;
        this.requestedQuantity = requestedQuantity;
        this.remainingQuantity = remainingQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.note = note;
    }

    // Getters and Setters
    public int getBackOrderId() { return backOrderId; }
    public void setBackOrderId(int backOrderId) { this.backOrderId = backOrderId; }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }

    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }

    public int getSubUnitId() { return subUnitId; }
    public void setSubUnitId(int subUnitId) { this.subUnitId = subUnitId; }

    public double getRequestedQuantity() { return requestedQuantity; }
    public void setRequestedQuantity(double requestedQuantity) { this.requestedQuantity = requestedQuantity; }

    public double getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(double remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}