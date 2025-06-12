/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author ADMIN
 */
public class SubUnit {

    private int subUnitId;
    private String name;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public SubUnit() {
    }

    public SubUnit(int subUnitId, String name, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.subUnitId = subUnitId;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SubUnit{" + "subUnitId=" + subUnitId + ", name=" + name + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
    

}
