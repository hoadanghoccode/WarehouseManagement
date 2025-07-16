package model;

import java.util.List;

public class ExportNoteDetail {
    private int exportNoteDetailId;
    private int exportNoteId;
    private int materialId;
    private String materialName;
    private int unitId;
    private String unitName;
    private double quantity;
    private boolean exported;
    private int qualityId;
    private String qualityName;
    private double availableQuantity;
    private String materialImage;
    private List<ExportNoteTransaction> transactions;

    // Getters and Setters
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    public List<ExportNoteTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ExportNoteTransaction> transactions) {
        this.transactions = transactions;
    }
}