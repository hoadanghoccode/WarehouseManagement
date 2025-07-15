/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class DeptWithRole {
    private Integer departmentId;
    private String description;
    private String departmentName;
    private RoleData role; // có thể null nếu department chưa gán role
    private Integer userCount;

    public DeptWithRole() {
    }

    public DeptWithRole(Integer departmentId, String description, String departmentName, RoleData role, Integer userCount) {
        this.departmentId = departmentId;
        this.description = description;
        this.departmentName = departmentName;
        this.role = role;
        this.userCount = userCount;
    }
    
    

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public RoleData getRole() {
        return role;
    }

    public void setRole(RoleData role) {
        this.role = role;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

   
}
