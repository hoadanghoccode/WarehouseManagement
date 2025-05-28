/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PC
 */
public class PermPayload {

    private int resourceId;
    private boolean canAdd;
    private boolean canView;
    private boolean canUpdate;
    private boolean canDelete;

    public PermPayload() {
    }

    public PermPayload(int resourceId, boolean canAdd, boolean canView, boolean canUpdate, boolean canDelete) {
        this.resourceId = resourceId;
        this.canAdd = canAdd;
        this.canView = canView;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

}
