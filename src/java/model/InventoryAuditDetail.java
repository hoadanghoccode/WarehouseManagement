/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class InventoryAuditDetail {
    private int id;
    private int auditId;
    private int materialDetailId;
    private double systemQty;
    private double actualQty;
    private double difference;
    private String reason;

    public InventoryAuditDetail() {
    }

    public InventoryAuditDetail(int id, int auditId, int materialDetailId, double systemQty, double actualQty, double difference, String reason) {
        this.id = id;
        this.auditId = auditId;
        this.materialDetailId = materialDetailId;
        this.systemQty = systemQty;
        this.actualQty = actualQty;
        this.difference = difference;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public int getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(int materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public double getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(double systemQty) {
        this.systemQty = systemQty;
    }

    public double getActualQty() {
        return actualQty;
    }

    public void setActualQty(double actualQty) {
        this.actualQty = actualQty;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
    
}
