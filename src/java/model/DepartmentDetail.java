/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDetail {
    private int departmentId;
    private String departmentName;
    private String description;
    private int roleId;
    private String roleName;
    private List<ResourcePerm> permissions = new ArrayList<>();

    public DepartmentDetail() {}

    public DepartmentDetail(int departmentId, String departmentName,
                            String description, int roleId, String roleName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<ResourcePerm> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<ResourcePerm> permissions) {
        this.permissions = permissions;
    }
}
