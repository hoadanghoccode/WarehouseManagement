/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Material {
    private int materialId;
    private int categoryId;
    private int unitId;
    private String name;
    private String image;
    private Date createAt;
    private String status;
    private String categoryName;
    private String unitName;
  
    // Constructors
    public Material() {}

    public Material(int materialId, int categoryId, int unitId, String name, String image, Date createAt, String status) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.unitId = unitId;
        this.name = name;
        this.image = image;
        this.createAt = createAt;
        this.status = status;
    }

    public Material(int materialId, int categoryId, int unitId, String name, String image, Date createAt, String status, String categoryName, String unitName) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.unitId = unitId;
        this.name = name;
        this.image = image;
        this.createAt = createAt;
        this.status = status;
        this.categoryName = categoryName;
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
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Date getCreateAt() { return createAt; }
    public void setCreateAt(Date createAt) { this.createAt = createAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    @Override
    public String toString() {
        return "Material{" + "materialId=" + materialId + ", categoryId=" + categoryId + ", unitId=" + unitId + ", name=" + name + ", image=" + image + ", createAt=" + createAt + ", status=" + status + ", categoryName=" + categoryName + ", unitName=" + unitName + '}';
    }
    
    
}