package model;

import java.sql.Date;

public class BackOrder {

    private int backOrderId;
    private int orderDetailId;
    private int materialId;
    private String materialName;
    private int subUnitId;
    private String subUnitName;
    private int qualityId;
    private String qualityType;
    private double requestedQuantity;
    private double remainingQuantity;
    private double availableQuantity;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String note;
    private int userId;
    private String userName;
    private int exportNoteDetailId; // Link to Export_note_detail

    public int getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(int backOrderId) {
        this.backOrderId = backOrderId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public String getSubUnitName() {
        return subUnitName;
    }

    public void setSubUnitName(String subUnitName) {
        this.subUnitName = subUnitName;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getQualityType() {
        return qualityType != null ? qualityType : switch (qualityId) {
            case 1 -> "Available";
            case 2 -> "Not Available";
            default -> "Unknown";
        };
    }

    public void setQualityType(String qualityType) {
        this.qualityType = qualityType;
    }

    public double getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(double requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNote() {
        return note != null ? note : "Low";
    }

    public void setNote(String note) {
        this.note = note != null && ("Low".equals(note) || "Medium".equals(note) || "High".equals(note)) ? note : "Low";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getExportNoteDetailId() {
        return exportNoteDetailId;
    }

    public void setExportNoteDetailId(int exportNoteDetailId) {
        this.exportNoteDetailId = exportNoteDetailId;
    }
}