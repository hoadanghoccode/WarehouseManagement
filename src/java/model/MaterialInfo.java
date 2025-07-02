package model;

public class MaterialInfo {

    private String materialName;
    private String categoryName;
    private String subUnitName;
    private double availableQty;

    // Constructors
    public MaterialInfo() {
    }

    public MaterialInfo(String materialName, String categoryName, String subUnitName, double availableQty) {
        this.materialName = materialName;
        this.categoryName = categoryName;
        this.subUnitName = subUnitName;
        this.availableQty = availableQty;
    }

    // Getters and Setters
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubUnitName() {
        return subUnitName;
    }

    public void setSubUnitName(String subUnitName) {
        this.subUnitName = subUnitName;
    }

    public double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(double availableQty) {
        this.availableQty = availableQty;
    }
}
