/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author PC
 */
public class RolePayload {
    private int roleId;
    private List<PermPayload> permissions;

    public RolePayload() {
    }

    public RolePayload(int roleId, List<PermPayload> permissions) {
        this.roleId = roleId;
        this.permissions = permissions;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<PermPayload> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermPayload> permissions) {
        this.permissions = permissions;
    }
    
    
}
