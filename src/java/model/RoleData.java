
package model;
import java.util.ArrayList;
import java.util.List;

public class RoleData {
    private int roleId;
    private String role;
    public List<ResourcePerm> resources = new ArrayList<>();

    public RoleData() {}

    public RoleData(int roleId, String role) {
        this.roleId = roleId;
        this.role   = role;
    }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<ResourcePerm> getResources() { return resources; }
    public void setResources(List<ResourcePerm> resources) { this.resources = resources; }
}
