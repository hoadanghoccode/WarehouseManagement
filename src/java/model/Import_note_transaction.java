/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author legia
 */
import java.sql.Date;

public class Import_note_transaction {
    private int importNoteTransactionId;
    private int importNoteDetailId;
    private int materialId;
    private double quantity;
    private int qualityId;
    private boolean imported;
    private Date createdAt;

    // Constructors
    public Import_note_transaction() {}

    public Import_note_transaction(int importNoteTransactionId, int importNoteDetailId, int materialId, double quantity, int qualityId, boolean imported, Date createdAt) {
        this.importNoteTransactionId = importNoteTransactionId;
        this.importNoteDetailId = importNoteDetailId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.qualityId = qualityId;
        this.imported = imported;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getImportNoteTransactionId() { return importNoteTransactionId; }
    public void setImportNoteTransactionId(int importNoteTransactionId) { this.importNoteTransactionId = importNoteTransactionId; }
    public int getImportNoteDetailId() { return importNoteDetailId; }
    public void setImportNoteDetailId(int importNoteDetailId) { this.importNoteDetailId = importNoteDetailId; }
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public int getQualityId() { return qualityId; }
    public void setQualityId(int qualityId) { this.qualityId = qualityId; }
    public boolean isImported() { return imported; }
    public void setImported(boolean imported) { this.imported = imported; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
