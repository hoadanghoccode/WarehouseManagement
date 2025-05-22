package model;

import java.util.Date;

public class Users {

    private int userId;
    private int roleId;
    private String roleName;
    private int branchId;
    private String branchName;
    private String fullName;
    private String email;
    private String password;
    private int gender;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private boolean status;
    private String resetPasswordToken;
    private Date resetPasswordExpiry;

    public Users() {
    }

    public Users(int userId, int roleId, int branchId, String fullName, String email, String password, int gender,
            String phoneNumber, String address, Date dateOfBirth, String image, Date createdAt, Date updatedAt,
            boolean status, String resetPasswordToken, Date resetPasswordExpiry) {
        this.userId = userId;
        this.roleId = roleId;
        this.branchId = branchId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordExpiry = resetPasswordExpiry;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getResetPasswordExpiry() {
        return resetPasswordExpiry;
    }

    public void setResetPasswordExpiry(Date resetPasswordExpiry) {
        this.resetPasswordExpiry = resetPasswordExpiry;
    }
}