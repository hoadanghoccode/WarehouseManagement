package model;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialInventory {

    private int materialId;
    private int categoryId;
    private int supplierId;
    private int subUnitId;
    private String materialName;
    private String categoryName;
    private String supplierName;
    private String subUnitName;
    private BigDecimal availableQty;
    private BigDecimal notAvailableQty;
    private BigDecimal importQty;
    private BigDecimal exportQty;
    private Date inventoryDate;
    private String note;

    // Constructors
    public MaterialInventory() {
    }

    public MaterialInventory(BigDecimal availableQty, BigDecimal notAvailableQty) {
        this.availableQty = availableQty;
        this.notAvailableQty = notAvailableQty;
    }

    public MaterialInventory(int materialId, int categoryId, int supplierId, int subUnitId, String materialName,
                            String categoryName, String supplierName, String subUnitName, BigDecimal availableQty,
                            BigDecimal notAvailableQty, BigDecimal importQty, BigDecimal exportQty, Date inventoryDate,
                            String note) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.subUnitId = subUnitId;
        this.materialName = materialName;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
        this.subUnitName = subUnitName;
        this.availableQty = availableQty;
        this.notAvailableQty = notAvailableQty;
        this.importQty = importQty;
        this.exportQty = exportQty;
        this.inventoryDate = inventoryDate;
        this.note = note;
    }

    // Getters and Setters
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public String getMaterialName() {
        return materialName != null ? materialName : "N/A";
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCategoryName() {
        return categoryName != null ? categoryName : "N/A";
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName != null ? supplierName : "N/A";
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSubUnitName() {
        return subUnitName != null ? subUnitName : "N/A";
    }

    public void setSubUnitName(String subUnitName) {
        this.subUnitName = subUnitName;
    }

    public BigDecimal getAvailableQty() {
        return availableQty != null ? availableQty : BigDecimal.ZERO;
    }

    public void setAvailableQty(BigDecimal availableQty) {
        this.availableQty = availableQty;
    }

    public BigDecimal getNotAvailableQty() {
        return notAvailableQty != null ? notAvailableQty : BigDecimal.ZERO;
    }

    public void setNotAvailableQty(BigDecimal notAvailableQty) {
        this.notAvailableQty = notAvailableQty;
    }

    public BigDecimal getImportQty() {
        return importQty != null ? importQty : BigDecimal.ZERO;
    }

    public void setImportQty(BigDecimal importQty) {
        this.importQty = importQty;
    }

    public BigDecimal getExportQty() {
        return exportQty != null ? exportQty : BigDecimal.ZERO;
    }

    public void setExportQty(BigDecimal exportQty) {
        this.exportQty = exportQty;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getNote() {
        return note != null ? note : "No recent transactions";
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Optional: Thêm toString cho debug
    @Override
    public String toString() {
        return "MaterialInventory{" +
                "materialId=" + materialId +
                ", categoryId=" + categoryId +
                ", supplierId=" + supplierId +
                ", subUnitId=" + subUnitId +
                ", materialName='" + materialName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", subUnitName='" + subUnitName + '\'' +
                ", availableQty=" + availableQty +
                ", notAvailableQty=" + notAvailableQty +
                ", importQty=" + importQty +
                ", exportQty=" + exportQty +
                ", inventoryDate=" + inventoryDate +
                ", note='" + note + '\'' +
                '}';
    }
}