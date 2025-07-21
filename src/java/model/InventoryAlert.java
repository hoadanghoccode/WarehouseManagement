/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author duong
 */
public class InventoryAlert {
    private int materialId;
private String materialName;
private int qualityId;
private String qualityName;
private double quantity;
private double minQuantity;
private String unitName;
private String status;

    public InventoryAlert() {
    }

    public InventoryAlert(int materialId, String materialName, int qualityId, String qualityName, double quantity, double minQuantity, String unitName, String status) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.unitName = unitName;
        this.status = status;
    }

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

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(double minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  
    
}
