/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class InventoryItem {

    private int materialId;
    private String materialName;
    private String materialImage;
    private String unitName;
    private double availableQty;
    private double notAvailableQty;

    public InventoryItem() {
    }

    public InventoryItem(int materialId, String materialName, String materialImage, String unitName, double availableQty, double notAvailableQty) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.materialImage = materialImage;
        this.unitName = unitName;
        this.availableQty = availableQty;
        this.notAvailableQty = notAvailableQty;
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

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(double availableQty) {
        this.availableQty = availableQty;
    }

    public double getNotAvailableQty() {
        return notAvailableQty;
    }

    public void setNotAvailableQty(double notAvailableQty) {
        this.notAvailableQty = notAvailableQty;
    }

}
