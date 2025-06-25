package model;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialInventory {

    private int materialId;
    private String materialName;
    private int categoryId;
    private String categoryName;
    private int supplierId;
    private String supplierName;
    private int subUnitId;
    private String subUnitName;
    private BigDecimal availableQty;
    private BigDecimal notAvailableQty;
    private BigDecimal importQty;
    private BigDecimal exportQty;
    private int recordCount; 
    private String transactionType; 
    private Date inventoryDate;
    private String note;

    public MaterialInventory() {
        this.availableQty = BigDecimal.ZERO;
        this.notAvailableQty = BigDecimal.ZERO;
        this.importQty = BigDecimal.ZERO;
        this.exportQty = BigDecimal.ZERO;
        this.recordCount = 0;
    }

    // Getters and Setters
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public BigDecimal getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(BigDecimal availableQty) {
        this.availableQty = availableQty != null ? availableQty : BigDecimal.ZERO;
    }

    public BigDecimal getNotAvailableQty() {
        return notAvailableQty;
    }

    public void setNotAvailableQty(BigDecimal notAvailableQty) {
        this.notAvailableQty = notAvailableQty != null ? notAvailableQty : BigDecimal.ZERO;
    }

    public BigDecimal getImportQty() {
        return importQty;
    }

    public void setImportQty(BigDecimal importQty) {
        this.importQty = importQty != null ? importQty : BigDecimal.ZERO;
    }

    public BigDecimal getExportQty() {
        return exportQty;
    }

    public void setExportQty(BigDecimal exportQty) {
        this.exportQty = exportQty != null ? exportQty : BigDecimal.ZERO;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}