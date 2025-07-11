/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class OrderDetail {

    private int orderDetailId;
    private int materialId;
    private String materialName;
    private String materialImage;
    private int orderId;
    private String unitName;
    private int qualityId;
    private int quantity;

    public OrderDetail(int orderDetailId, int materialId, String materialName, String materialImage, int orderId, String unitName, int qualityId, int quantity) {
        this.orderDetailId = orderDetailId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.materialImage = materialImage;
        this.orderId = orderId;
        this.unitName = unitName;
        this.qualityId = qualityId;
        this.quantity = quantity;
    }

    public OrderDetail() {
    }


    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
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

    @Override
    public String toString() {
        return "OrderDetail{" + "orderDetailId=" + orderDetailId + ", materialId=" + materialId + ", materialName=" + materialName + ", materialImage=" + materialImage + ", orderId=" + orderId + ", unitName=" + unitName + ", qualityId=" + qualityId + ", quantity=" + quantity + '}';
    }

    
}
