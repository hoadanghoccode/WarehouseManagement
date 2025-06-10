
package model;

public class Departmentt {
    private int departmentId;
    private String name;
    private String description;
    private int roleId;

    // Default constructor
    public Departmentt() {
    }

    // Constructor

    public Departmentt(int departmentId, String name, String description, int roleId) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.roleId = roleId;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    
}
