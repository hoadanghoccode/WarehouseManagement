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
    private String materialName;
    private double totalImport;
    private double totalExport;
    private double stock;

    public MaterialStatistic() {
    }

    public MaterialStatistic(int materialId, double totalQuantity, String materialName, double totalImport, double totalExport, double stock) {
        this.materialId = materialId;
        this.totalQuantity = totalQuantity;
        this.materialName = materialName;
        this.totalImport = totalImport;
        this.totalExport = totalExport;
        this.stock = stock;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

}
