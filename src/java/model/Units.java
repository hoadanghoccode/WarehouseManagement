package model;

public class Units {
    private int unitId;
    private String name;
    private boolean isActive;

    public Units() {}

    public Units(int unitId, String name, boolean isActive) {
        this.unitId = unitId;
        this.name = name;
        this.isActive = isActive;
    }

    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "Units{" + "unitId=" + unitId + ", name=" + name + ", isActive=" + isActive + '}';
    }
}