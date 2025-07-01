/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Material {
    private int materialId;
    private int categoryId;
    private String name;
    private String image;
    private Date createAt;
    private String status;

    // Constructors
    public Material() {}

    public Material(int materialId, int categoryId, String name, String image, Date createAt, String status) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.createAt = createAt;
        this.status = status;
    }

    // Getters and Setters
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Date getCreateAt() { return createAt; }
    public void setCreateAt(Date createAt) { this.createAt = createAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}