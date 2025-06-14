package model;

import java.math.BigDecimal;
import java.sql.Date;

public class MaterialInventory {
    private int materialId;
    private int categoryId;
    private int supplierId;
    private int subUnitId;
    private int qualityId;
    private String materialName;
    private String categoryName;
    private String supplierName;
    private String subUnitName;
    private String qualityName;
    private BigDecimal availableQty; 
    private BigDecimal notAvailableQty;
    private Date inventoryDate;
    private String note;

    public MaterialInventory() {
        this.availableQty = BigDecimal.ZERO;
        this.notAvailableQty = BigDecimal.ZERO;
    }

    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public int getSubUnitId() { return subUnitId; }
    public void setSubUnitId(int subUnitId) { this.subUnitId = subUnitId; }
    public int getQualityId() { return qualityId; }
    public void setQualityId(int qualityId) { this.qualityId = qualityId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSubUnitName() { return subUnitName; }
    public void setSubUnitName(String subUnitName) { this.subUnitName = subUnitName; }
    public String getQualityName() { return qualityName; }
    public void setQualityName(String qualityName) { this.qualityName = qualityName; }
    public BigDecimal getAvailableQty() { return availableQty; }
    public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }
    public BigDecimal getNotAvailableQty() { return notAvailableQty; }
    public void setNotAvailableQty(BigDecimal notAvailableQty) { this.notAvailableQty = notAvailableQty; }
    public Date getInventoryDate() { return inventoryDate; }
    public void setInventoryDate(Date inventoryDate) { this.inventoryDate = inventoryDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}