/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author duong
 */
public class MaterialStatistic {

    private int materialId;
    private double totalQuantity;
//    private String materialName;
    private int month;
    private double totalImport;
    private double totalExport;
    private double stock;
    private int materialCount;
    private String unitName;

    public MaterialStatistic() {
    }

    public MaterialStatistic(int materialId, double totalQuantity, int month, double totalImport, double totalExport, double stock, int materialCount, String unitName) {
        this.materialId = materialId;
        this.totalQuantity = totalQuantity;
        this.month = month;
        this.totalImport = totalImport;
        this.totalExport = totalExport;
        this.stock = stock;
        this.materialCount = materialCount;
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalImport() {
        return totalImport;
    }

    public void setTotalImport(double totalImport) {
        this.totalImport = totalImport;
    }

    public double getTotalExport() {
        return totalExport;
    }

    public void setTotalExport(double totalExport) {
        this.totalExport = totalExport;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

}
