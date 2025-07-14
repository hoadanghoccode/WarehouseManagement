package model;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialInventory {

    private int materialId;
    private int categoryId;
    private int unitId;
    private String materialName;
    private String categoryName;
    private String unitName;
    private String image;
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

    public MaterialInventory(int materialId, int categoryId, int unitId, String materialName,
                             String categoryName, String unitName, String image, BigDecimal availableQty,
                             BigDecimal notAvailableQty, BigDecimal importQty, BigDecimal exportQty, Date inventoryDate,
                             String note) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.unitId = unitId;
        this.materialName = materialName;
        this.categoryName = categoryName;
        this.unitName = unitName;
        this.image = image;
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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
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

    public String getUnitName() {
        return unitName != null ? unitName : "N/A";
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getImage() {
        return image != null ? image : "";
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public String toString() {
        return "MaterialInventory{" +
                "materialId=" + materialId +
                ", categoryId=" + categoryId +
                ", unitId=" + unitId +
                ", materialName='" + materialName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", image='" + image + '\'' +
                ", availableQty=" + availableQty +
                ", notAvailableQty=" + notAvailableQty +
                ", importQty=" + importQty +
                ", exportQty=" + exportQty +
                ", inventoryDate=" + inventoryDate +
                ", note='" + note + '\'' +
                '}';
    }
}