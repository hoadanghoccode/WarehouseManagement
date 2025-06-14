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

    private int id;
    private String name;
    private String status;

    public Supplier() {
    }

    public Supplier(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSupplierId() {
        return id; 
    }

    public void setSupplierId(int id) {
        this.id = id; 
    }

    @Override
    public String toString() {
        return "Supplier{" + "id=" + id + ", name=" + name + ", status=" + status + '}';
    }
}