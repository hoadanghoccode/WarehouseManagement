/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class MaterialDetail {
    private int materialDetailId;
    private int materialId;
    private int qualityId;
    private double quantity;
    private Date lastUpdated;

    // Constructors
    public MaterialDetail() {}

    public MaterialDetail(int materialDetailId, int materialId, int qualityId, double quantity, Date lastUpdated) {
        this.materialDetailId = materialDetailId;
        this.materialId = materialId;
        this.qualityId = qualityId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public int getMaterialDetailId() { return materialDetailId; }
    public void setMaterialDetailId(int materialDetailId) { this.materialDetailId = materialDetailId; }
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public int getQualityId() { return qualityId; }
    public void setQualityId(int qualityId) { this.qualityId = qualityId; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
