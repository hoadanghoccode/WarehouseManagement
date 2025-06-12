/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Order {

    private int orderId;
    private int warehouseId;
    private int userId;
    private Timestamp createdAt;
    private int type; // 0: nhập, 1: xuất
    private String supplier;
    private String status;
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(int orderId, int warehouseId, int userId, Timestamp createdAt, int type, String supplier, String status, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.type = type;
        this.supplier = supplier;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", warehouseId=" + warehouseId + ", userId=" + userId + ", createdAt=" + createdAt + ", type=" + type + ", supplier=" + supplier + ", status=" + status + ", orderDetails=" + orderDetails + '}';
    }

    
    
}
