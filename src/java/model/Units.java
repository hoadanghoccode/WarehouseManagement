package model;

import java.sql.Timestamp;

public class Units {
    private int unitId;
    private String name;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Units() {}

    public Units(int unitId, String name, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.unitId = unitId;
        this.name = name;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Units{" + "unitId=" + unitId + ", name=" + name + ", isActive=" + isActive + 
               ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}