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
    private int orderId;
    private int subUnitId;
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

  

}
