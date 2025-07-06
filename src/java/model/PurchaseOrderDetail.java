/*
 * Click .netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click .netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author legia
 */
public class PurchaseOrderDetail {
    private int purchaseOrderDetailId;
    private int purchaseOrderId;
    private int materialId;
    private Integer qualityId;
    private double quantity;
    private Double price;
    private String materialName;
    private String qualityName;  

    public PurchaseOrderDetail() {}

    public PurchaseOrderDetail(int purchaseOrderDetailId, int purchaseOrderId, int materialId, Integer qualityId, double quantity, Double price, String materialName, String qualityName) {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
        this.purchaseOrderId = purchaseOrderId;
        this.materialId = materialId;
        this.qualityId = qualityId;
        this.quantity = quantity;
        this.price = price;
        this.materialName = materialName;
        this.qualityName = qualityName;
    }

    public int getPurchaseOrderDetailId() {
        return purchaseOrderDetailId;
    }

    public void setPurchaseOrderDetailId(int purchaseOrderDetailId) {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public Integer getQualityId() {
        return qualityId;
    }

    public void setQualityId(Integer qualityId) {
        this.qualityId = qualityId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }
}