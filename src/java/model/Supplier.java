/*
 * Click nbfs://nofollow/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nofollow/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author legia
 */
public class Supplier {

    private int supplierId;
    private String name;
    private String status;

    public Supplier() {
    }

    public Supplier(int supplierId, String name, String status) {
        this.supplierId = supplierId;
        this.name = name;
        this.status = status;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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



    @Override
    public String toString() {
        return "Supplier{" + "supplierId=" + supplierId + ", name=" + name + ", status=" + status + '}';
    }
}