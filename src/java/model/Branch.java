package model;

public class Branch {
    private int branchId;
    private String name;

    public Branch(int branchId, String name) {
        this.branchId = branchId;
        this.name = name;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}