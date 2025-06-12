// model/MaterialInventory.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MaterialInventory {
    private int materialId;
    private int categoryId;
    private String materialName;
    private String unitName;
    private BigDecimal quantity;
    private Timestamp lastUpdated;
    private int warehouseId;
    private String categoryName;

    // Constructor mặc định
    public MaterialInventory() {
    }

    // Constructor đầy đủ
    public MaterialInventory(int materialId, int categoryId, String materialName, String unitName,
                            BigDecimal quantity, Timestamp lastUpdated, int warehouseId) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.materialName = materialName;
        this.unitName = unitName;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
        this.warehouseId = warehouseId;
    }

    // Getters và Setters
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return "MaterialInventory{" +
               "materialId=" + materialId +
               ", categoryId=" + categoryId +
               ", materialName='" + materialName + '\'' +
               ", unitName='" + unitName + '\'' +
               ", quantity=" + quantity +
               ", lastUpdated=" + lastUpdated +
               ", warehouseId=" + warehouseId +
               ", categoryName='" + categoryName + '\'' +
               '}';
    }
}