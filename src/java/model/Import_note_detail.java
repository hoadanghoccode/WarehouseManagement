package model;

import java.sql.Date;

public class Import_note_detail {
    private int importNoteDetailId;
    private int importNoteId;
    private int materialId;
    private int subUnitId;
    private double quantity;
    private int qualityId;
    private boolean imported;

    // Constructors
    public Import_note_detail() {}

    public Import_note_detail(int importNoteDetailId, int importNoteId, int materialId, int subUnitId, double quantity, int qualityId, boolean imported) {
        this.importNoteDetailId = importNoteDetailId;
        this.importNoteId = importNoteId;
        this.materialId = materialId;
        this.subUnitId = subUnitId;
        this.quantity = quantity;
        this.qualityId = qualityId;
        this.imported = imported;
    }

    // Getters and Setters
    public int getImportNoteDetailId() { return importNoteDetailId; }
    public void setImportNoteDetailId(int importNoteDetailId) { this.importNoteDetailId = importNoteDetailId; }
    public int getImportNoteId() { return importNoteId; }
    public void setImportNoteId(int importNoteId) { this.importNoteId = importNoteId; }
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getSubUnitId() { return subUnitId; }
    public void setSubUnitId(int subUnitId) { this.subUnitId = subUnitId; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public int getQualityId() { return qualityId; }
    public void setQualityId(int qualityId) { this.qualityId = qualityId; }
    public boolean isImported() { return imported; }
    public void setImported(boolean imported) { this.imported = imported; }
}