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
    private int subUnitId;
    private String subUnitName;
    private int qualityId;
    private int quantity;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int materialId, int orderId, int subUnitId, int qualityId, int quantity) {
        this.orderDetailId = orderDetailId;
        this.materialId = materialId;
        this.orderId = orderId;
        this.subUnitId = subUnitId;
        this.qualityId = qualityId;
        this.quantity = quantity;
    }

    public OrderDetail(int orderDetailId, int materialId, String materialName, int orderId, int subUnitId, String subUnitName, int qualityId, int quantity, String materialImage) {
        this.orderDetailId = orderDetailId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.orderId = orderId;
        this.subUnitId = subUnitId;
        this.subUnitName = subUnitName;
        this.qualityId = qualityId;
        this.quantity = quantity;
        this.materialImage = materialImage;
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

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
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

    public String getSubUnitName() {
        return subUnitName;
    }

    public void setSubUnitName(String subUnitName) {
        this.subUnitName = subUnitName;
    }

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "orderDetailId=" + orderDetailId + ", materialId=" + materialId + ", materialName=" + materialName + ", materialImage=" + materialImage + ", orderId=" + orderId + ", subUnitId=" + subUnitId + ", subUnitName=" + subUnitName + ", qualityId=" + qualityId + ", quantity=" + quantity + '}';
    }

}
