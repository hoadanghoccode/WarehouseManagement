package model;

public class ExportNoteDetail {

    private int exportNoteDetailId;
    private int exportNoteId;
    private int materialId;
    private String materialName;
    private int subUnitId;
    private String subUnitName;
    private double quantity;
    private int qualityId;
    private String qualityName;

    // Constructor
    public ExportNoteDetail() {
    }

    public ExportNoteDetail(int exportNoteDetailId, int exportNoteId, int materialId, String materialName,
            int subUnitId, String subUnitName, double quantity, int qualityId, String qualityName) {
        this.exportNoteDetailId = exportNoteDetailId;
        this.exportNoteId = exportNoteId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.subUnitId = subUnitId;
        this.subUnitName = subUnitName;
        this.quantity = quantity;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
    }

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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
}
