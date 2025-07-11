/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author legia
 */
public class MaterialTransactionHistory {

    private int materialTransactionHistoryId;
    private int materialDetailId;
    private Integer importNoteDetailId;
    private Integer exportNoteDetailId;
    private Date transactionDate;
    private String note;
    private int materialId;
    private int UnitId;
    private int qualityId;
    private double quantity;
    private String materialName;
    private String UnitName;
    private String qualityName;

    public MaterialTransactionHistory() {
    }

    public MaterialTransactionHistory(int materialTransactionHistoryId, int materialDetailId, Integer importNoteDetailId, Integer exportNoteDetailId, Date transactionDate, String note, int materialId, int UnitId, int qualityId, double quantity, String materialName, String UnitName, String qualityName) {
        this.materialTransactionHistoryId = materialTransactionHistoryId;
        this.materialDetailId = materialDetailId;
        this.importNoteDetailId = importNoteDetailId;
        this.exportNoteDetailId = exportNoteDetailId;
        this.transactionDate = transactionDate;
        this.note = note;
        this.materialId = materialId;
        this.UnitId = UnitId;
        this.qualityId = qualityId;
        this.quantity = quantity;
        this.materialName = materialName;
        this.UnitName = UnitName;
        this.qualityName = qualityName;
    }

    public int getMaterialTransactionHistoryId() {
        return materialTransactionHistoryId;
    }

    public void setMaterialTransactionHistoryId(int materialTransactionHistoryId) {
        this.materialTransactionHistoryId = materialTransactionHistoryId;
    }

    public int getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(int materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public Integer getImportNoteDetailId() {
        return importNoteDetailId;
    }

    public void setImportNoteDetailId(Integer importNoteDetailId) {
        this.importNoteDetailId = importNoteDetailId;
    }

    public Integer getExportNoteDetailId() {
        return exportNoteDetailId;
    }

    public void setExportNoteDetailId(Integer exportNoteDetailId) {
        this.exportNoteDetailId = exportNoteDetailId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getUnitId() {
        return UnitId;
    }

    public void setUnitId(int UnitId) {
        this.UnitId = UnitId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

}
