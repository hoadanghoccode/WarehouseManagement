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
    private int supplierId;
    private String supplierName;

    public Material() {}

    public Material(int materialId, int categoryId, String name, String status, int supplierId, String supplierName) {
        this.materialId = materialId;
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
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
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}