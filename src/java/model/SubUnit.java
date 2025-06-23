package model;

import java.sql.Timestamp;

public class SubUnit {

    private int subUnitId;
    private String name;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public SubUnit() {
    }

    public SubUnit(int subUnitId, String name, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.subUnitId = subUnitId;
        this.name = name;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "SubUnit{" + "subUnitId=" + subUnitId + ", name=" + name + ", isActive=" + isActive
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
