/*
 * Click nbfs://nofollow/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nofollow/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author legia
 */
//Bạn Giang tạo để test cái nhé.
public class Supplier {
    private int supplierId;
    private String name;
    private String status;

    // Constructors
    public Supplier() {}

    public Supplier(int supplierId, String name, String status) {
        this.supplierId = supplierId;
        this.name = name;
        this.status = status;
    }

    // Getters and Setters
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}