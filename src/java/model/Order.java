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
    private String type; 
    private int supplier;
    private String status;
    private String note;
    private List<OrderDetail> orderDetails;
    private String userName;
    private String supplierName;

   

    public Order(int orderId, int warehouseId, int userId, Timestamp createdAt, String type, int supplier, String status, String note, List<OrderDetail> orderDetails, String userName, String supplierName) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.type = type;
        this.supplier = supplier;
        this.status = status;
        this.note = note;
        this.orderDetails = orderDetails;
        this.userName = userName;
        this.supplierName = supplierName;
    }

    public Order() {
    }

    public Order(int orderId, int warehouseId, int userId, Timestamp createdAt, String type, int supplier, String status, String note, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.type = type;
        this.supplier = supplier;
        this.status = status;
        this.note = note;
        this.orderDetails = orderDetails;
    }

   

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

  

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
     public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", warehouseId=" + warehouseId + ", userId=" + userId + ", createdAt=" + createdAt + ", type=" + type + ", supplier=" + supplier + ", status=" + status + ", orderDetails=" + orderDetails + '}';
    }

    
    
}
