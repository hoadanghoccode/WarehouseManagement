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

    // Constructor
    public MaterialTransactionHistory() {
    }

    public MaterialTransactionHistory(int materialTransactionHistoryId, int materialDetailId, 
            Integer importNoteDetailId, Integer exportNoteDetailId, Date transactionDate, String note) {
        this.materialTransactionHistoryId = materialTransactionHistoryId;
        this.materialDetailId = materialDetailId;
        this.importNoteDetailId = importNoteDetailId;
        this.exportNoteDetailId = exportNoteDetailId;
        this.transactionDate = transactionDate;
        this.note = note;
    }

    // Getters and Setters
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
}