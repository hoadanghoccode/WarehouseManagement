package model;

public class InventoryAuditDetail {

    private int id;
    private int auditId;
    private int materialDetailId;
    private double systemQty;
    private double actualQty;
    private double difference;
    private String reason;
    private String materialName;
    private int materialId;
    private int subUnitId;
    private int qualityId;

    public InventoryAuditDetail() {
    }

    public InventoryAuditDetail(int id, int auditId, int materialDetailId, double systemQty, double actualQty, double difference, String reason, String materialName, int materialId, int subUnitId, int qualityId) {
        this.id = id;
        this.auditId = auditId;
        this.materialDetailId = materialDetailId;
        this.systemQty = systemQty;
        this.actualQty = actualQty;
        this.difference = difference;
        this.reason = reason;
        this.materialName = materialName;
        this.materialId = materialId;
        this.subUnitId = subUnitId;
        this.qualityId = qualityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public int getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(int materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public double getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(double systemQty) {
        this.systemQty = systemQty;
    }

    public double getActualQty() {
        return actualQty;
    }

    public void setActualQty(double actualQty) {
        this.actualQty = actualQty;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    @Override
    public String toString() {
        return "InventoryAuditDetail{" + "id=" + id + ", auditId=" + auditId + ", materialDetailId=" + materialDetailId + ", systemQty=" + systemQty + ", actualQty=" + actualQty + ", difference=" + difference + ", reason=" + reason + ", materialName=" + materialName + ", materialId=" + materialId + ", subUnitId=" + subUnitId + ", qualityId=" + qualityId + '}';
    }
    
    

}
