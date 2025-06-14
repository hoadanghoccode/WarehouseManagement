package model;

import java.sql.Date;

public class Warehouse {
    private int warehouseId;
    private String name;
    private String address;

    public Warehouse() {}

    public Warehouse(int warehouseId, String name, String address) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.address = address;
    }

    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}