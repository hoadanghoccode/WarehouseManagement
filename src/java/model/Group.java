package model;

public class Group {
    private int groupId;
    private String name;
    private int roleId;

    public Group() {
    }

    public Group(int groupId, String name, int roleId) {
        this.groupId = groupId;
        this.name = name;
        this.roleId = roleId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}