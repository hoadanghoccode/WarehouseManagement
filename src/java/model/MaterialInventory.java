package model;

import java.math.BigDecimal;
import java.sql.Date;

public class MaterialInventory {
    private int materialId;
    private int categoryId;
    private String materialName;
    private String subUnitName;
    private BigDecimal closingQty;
    private Date inventoryDate;
    private String categoryName;

    public MaterialInventory() {
    }

    public MaterialInventory(int materialId, int categoryId, String materialName, String subUnitName,
                             BigDecimal closingQty, Date inventoryDate, String categoryName) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.materialName = materialName;
        this.subUnitName = subUnitName;
        this.closingQty = closingQty;
        this.inventoryDate = inventoryDate;
        this.categoryName = categoryName;
    }

    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getSubUnitName() { return subUnitName; }
    public void setSubUnitName(String subUnitName) { this.subUnitName = subUnitName; }
    public BigDecimal getClosingQty() { return closingQty; }
    public void setClosingQty(BigDecimal closingQty) { this.closingQty = closingQty; }
    public Date getInventoryDate() { return inventoryDate; }
    public void setInventoryDate(Date inventoryDate) { this.inventoryDate = inventoryDate; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}