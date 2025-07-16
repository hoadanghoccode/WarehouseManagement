package model;

import java.sql.Timestamp;

public class ExportNoteTransaction {
    private int exportNoteTransactionId;
    private int exportNoteDetailId;
    private int exportNoteId;
    private int materialId;
    private String materialName;
    private int unitId;
    private String unitName;
    private double requestedQuantity;
    private double exportedQuantity;
    private double remainingQuantity;
    private boolean exported;
    private int qualityId;
    private String qualityName;
    private double availableQuantity;
    private Timestamp createdAt;

    // Getters and Setters
    public int getExportNoteTransactionId() {
        return exportNoteTransactionId;
    }

    public void setExportNoteTransactionId(int exportNoteTransactionId) {
        this.exportNoteTransactionId = exportNoteTransactionId;
    }

    public int getExportNoteDetailId() {
        return exportNoteDetailId;
    }

    public void setExportNoteDetailId(int exportNoteDetailId) {
        this.exportNoteDetailId = exportNoteDetailId;
    }

    public int getExportNoteId() {
        return exportNoteId;
    }

    public void setExportNoteId(int exportNoteId) {
        this.exportNoteId = exportNoteId;
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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(double requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public double getExportedQuantity() {
        return exportedQuantity;
    }

    public void setExportedQuantity(double exportedQuantity) {
        this.exportedQuantity = exportedQuantity;
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}