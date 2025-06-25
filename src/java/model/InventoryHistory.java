package model;

import java.time.LocalDate;

public class InventoryHistory {

    private LocalDate transactionDate;
    private double availableQty;
    private double notAvailableQty;
    private double importQty;
    private double exportQty;
    private String note;

    // Constructors
    public InventoryHistory() {
    }

    public InventoryHistory(LocalDate transactionDate, double availableQty, double notAvailableQty, double importQty, double exportQty, String note) {
        this.transactionDate = transactionDate;
        this.availableQty = availableQty;
        this.notAvailableQty = notAvailableQty;
        this.importQty = importQty;
        this.exportQty = exportQty;
        this.note = note;
    }

    // Getters and Setters
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(double availableQty) {
        this.availableQty = availableQty;
    }

    public double getNotAvailableQty() {
        return notAvailableQty;
    }

    public void setNotAvailableQty(double notAvailableQty) {
        this.notAvailableQty = notAvailableQty;
    }

    public double getImportQty() {
        return importQty;
    }

    public void setImportQty(double importQty) {
        this.importQty = importQty;
    }

    public double getExportQty() {
        return exportQty;
    }

    public void setExportQty(double exportQty) {
        this.exportQty = exportQty;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
