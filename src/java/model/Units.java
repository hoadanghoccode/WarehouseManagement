package model;

import java.sql.Timestamp;

public class Units {

    private int unitId;
    private String name;
    private int subUnitId;
    private double factor;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Units() {
    }

    public Units(int unitId, String name, int subUnitId, double factor, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.unitId = unitId;
        this.name = name;
        this.subUnitId = subUnitId;
        this.factor = factor;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public boolean getIsActive() {
        return isActive;
    } 

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    } 

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Units{" + "unitId=" + unitId + ", name=" + name + ", subUnitId=" + subUnitId
                + ", factor=" + factor + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
