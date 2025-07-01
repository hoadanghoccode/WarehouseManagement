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
    private int subUnitId;
    private int qualityId;
    private double quantity;          
    private String materialName;       
    private String subUnitName;       
    private String qualityName;     

    // Constructor
 
    // Constructor
    public MaterialTransactionHistory() {
    }

//    public MaterialTransactionHistory(int materialTransactionHistoryId, int materialDetailId, 
//            Integer importNoteDetailId, Integer exportNoteDetailId, Date transactionDate, String note) {
//        this.materialTransactionHistoryId = materialTransactionHistoryId;
//        this.materialDetailId = materialDetailId;
//        this.importNoteDetailId = importNoteDetailId;
//        this.exportNoteDetailId = exportNoteDetailId;
//        this.transactionDate = transactionDate;
//        this.note = note;
//    }

    public MaterialTransactionHistory(int materialTransactionHistoryId, int materialDetailId, Integer importNoteDetailId, Integer exportNoteDetailId, Date transactionDate, String note, int materialId, int subUnitId, int qualityId, double quantity, String materialName, String subUnitName, String qualityName) {
        this.materialTransactionHistoryId = materialTransactionHistoryId;
        this.materialDetailId = materialDetailId;
        this.importNoteDetailId = importNoteDetailId;
        this.exportNoteDetailId = exportNoteDetailId;
        this.transactionDate = transactionDate;
        this.note = note;
        this.materialId = materialId;
        this.subUnitId = subUnitId;
        this.qualityId = qualityId;
        this.quantity = quantity;
        this.materialName = materialName;
        this.subUnitName = subUnitName;
        this.qualityName = qualityName;
    }

    public int getMaterialTransactionHistoryId() {
        return materialTransactionHistoryId;
    }

    public int getMaterialDetailId() {
        return materialDetailId;
    }

    public Integer getImportNoteDetailId() {
        return importNoteDetailId;
    }

    public Integer getExportNoteDetailId() {
        return exportNoteDetailId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getNote() {
        return note;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getSubUnitId() {
        return subUnitId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getSubUnitName() {
        return subUnitName;
    }

    public String getQualityName() {
        return qualityName;
    }

    public void setMaterialTransactionHistoryId(int materialTransactionHistoryId) {
        this.materialTransactionHistoryId = materialTransactionHistoryId;
    }

    public void setMaterialDetailId(int materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public void setImportNoteDetailId(Integer importNoteDetailId) {
        this.importNoteDetailId = importNoteDetailId;
    }

    public void setExportNoteDetailId(Integer exportNoteDetailId) {
        this.exportNoteDetailId = exportNoteDetailId;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setSubUnitId(int subUnitId) {
        this.subUnitId = subUnitId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setSubUnitName(String subUnitName) {
        this.subUnitName = subUnitName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }

    
}