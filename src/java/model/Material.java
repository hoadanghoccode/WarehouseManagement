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
    private int unitId;
    private String name;
    private String description;
    private int inventoryQuantity;
    private double price;
    private String image;
    private String quality; 
    private String status;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private String categoryName;
    private String parentCategoryName;
    private String unitName;

    // Constructors
    public Material() {}

    public Material(int materialId, int categoryId, int unitId, String name, String description, int inventoryQuantity, double price, String image, String quality, String status, java.sql.Timestamp createdAt, java.sql.Timestamp updatedAt, String categoryName, String parentCategoryName, String unitName) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.inventoryQuantity = inventoryQuantity;
        this.price = price;
        this.image = image;
        this.quality = quality;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.parentCategoryName = parentCategoryName;
        this.unitName = unitName;
    }

    // Getters and Setters
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getInventoryQuantity() { return inventoryQuantity; }
    public void setInventoryQuantity(int inventoryQuantity) { this.inventoryQuantity = inventoryQuantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }
    public java.sql.Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.sql.Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getParentCategoryName() { return parentCategoryName; }
    public void setParentCategoryName(String parentCategoryName) { this.parentCategoryName = parentCategoryName; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
}