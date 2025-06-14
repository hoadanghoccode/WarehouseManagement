package model;

import java.sql.Timestamp;

public class UnitConversion {
    private int unitConversionId;
    private int unitId;
    private int subUnitId;
    private double factor;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UnitConversion() {}

    public UnitConversion(int unitConversionId, int unitId, int subUnitId, double factor, Timestamp createdAt, Timestamp updatedAt) {
        this.unitConversionId = unitConversionId;
        this.unitId = unitId;
        this.subUnitId = subUnitId;
        this.factor = factor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUnitConversionId() { return unitConversionId; }
    public void setUnitConversionId(int unitConversionId) { this.unitConversionId = unitConversionId; }
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public int getSubUnitId() { return subUnitId; }
    public void setSubUnitId(int subUnitId) { this.subUnitId = subUnitId; }
    public double getFactor() { return factor; }
    public void setFactor(double factor) { this.factor = factor; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "UnitConversion{" + "unitConversionId=" + unitConversionId + ", unitId=" + unitId + 
               ", subUnitId=" + subUnitId + ", factor=" + factor + ", createdAt=" + createdAt + 
               ", updatedAt=" + updatedAt + '}';
    }
}