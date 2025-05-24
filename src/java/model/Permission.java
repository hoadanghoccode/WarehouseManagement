/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class Permission {

    private Integer id;          // resource_role_id
    private Integer resourceId;  // resource_id
    private String resourceName; // từ bảng Resource.name
    private Integer roleId;      // role_id
    private boolean canCreate;
    private boolean canRead;
    private boolean canDelete;
    private boolean canUpdate;

    public Permission() {
    }

    public Permission(Integer id, Integer resourceId, String resourceName, Integer roleId, boolean canCreate, boolean canRead, boolean canDelete, boolean canUpdate) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.roleId = roleId;
        this.canCreate = canCreate;
        this.canRead = canRead;
        this.canDelete = canDelete;
        this.canUpdate = canUpdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

}
