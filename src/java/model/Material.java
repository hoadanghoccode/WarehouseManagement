/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author legia
 */

public class Material {
    private int materialId;
    private int categoryId;
    private String name;
    private String status;
    private String categoryName;
    private String parentCategoryName;
    private int unitId;
    private String unitName;
    private double price;
    private double quantity;
    private int supplierId;
    private String supplierName;

    public Material() {}

    public Material(int materialId, int categoryId, String name, String status, String categoryName, String parentCategoryName, 
                    int unitId, String unitName, double price, double quantity, int supplierId, String supplierName) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
        this.categoryName = categoryName;
        this.parentCategoryName = parentCategoryName;
        this.unitId = unitId;
        this.unitName = unitName;
        this.price = price;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getParentCategoryName() { return parentCategoryName; }
    public void setParentCategoryName(String parentCategoryName) { this.parentCategoryName = parentCategoryName; }
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}