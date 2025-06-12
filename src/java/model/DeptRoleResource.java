/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class DeptRoleResource {
     private Integer departmentId;
     private String description;
    private String departmentName;
    private Integer roleId;       // Có thể null nếu Dept chưa gán role nào
    private String roleName;      // Có thể null nếu Dept chưa gán role nào
    private Integer resourceId;   // Có thể null nếu Role chưa có resource mapping
    private String resourceName;  // Có thể null nếu Role chưa có resource mapping
    private Boolean canAdd;
    private Boolean canView;
    private Boolean canUpdate;
    private Boolean canDelete;

    public DeptRoleResource(Integer departmentId, String departmentName, String description,
                            Integer roleId, String roleName,
                            Integer resourceId, String resourceName,
                            Boolean canAdd, Boolean canView,
                            Boolean canUpdate, Boolean canDelete) {
        this.departmentId = departmentId;
        this.description = description;
        this.departmentName = departmentName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.canAdd = canAdd;
        this.canView = canView;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Boolean getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(Boolean canAdd) {
        this.canAdd = canAdd;
    }

    public Boolean getCanView() {
        return canView;
    }

    public void setCanView(Boolean canView) {
        this.canView = canView;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    
}
