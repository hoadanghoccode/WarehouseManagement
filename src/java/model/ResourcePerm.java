// src/main/java/model/ResourcePerm.java
package model;

public class ResourcePerm {
    private Integer resourceId;
    private String resourceName;
    private boolean canAdd;
    private boolean canView;
    private boolean canUpdate;
    private boolean canDelete;

    public ResourcePerm() {
    }

    public ResourcePerm(Integer resourceId, String resourceName, boolean canAdd, boolean canView, boolean canUpdate, boolean canDelete) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.canAdd = canAdd;
        this.canView = canView;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
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
