
package model;

import java.util.Date;
import java.sql.Timestamp;

public class Users {
    private int userId;
    private int roleId;
    private String fullName;
    private String password;
    private String image;
    private boolean gender;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean status;
    private String resetPasswordToken;
    private Timestamp resetPasswordExpiry;
    private String roleName;
    private int departmentId;
    private String departmentName;

    public Users() {
    }

    public Users(int userId, int roleId, String fullName, String email, String password,
                 boolean gender, String phoneNumber, String address, Date dateOfBirth,
                 String image, Timestamp createdAt, Timestamp updatedAt, boolean status,
                 String resetPasswordToken, Timestamp resetPasswordExpiry) {
        this.userId = userId;
        this.roleId = roleId;
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

    public Users(int userId, int roleId, String fullName, String email, String password,
                 boolean gender, String phoneNumber, String address, Date dateOfBirth,
                 String image, Timestamp createdAt, Timestamp updatedAt, boolean status,
                 String resetPasswordToken, Timestamp resetPasswordExpiry, String roleName,
                 int departmentId, String departmentName) {
        this.userId = userId;
        this.roleId = roleId;
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
        this.roleName = roleName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
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

    public Timestamp getResetPasswordExpiry() {
        return resetPasswordExpiry;
    }

    public void setResetPasswordExpiry(Timestamp resetPasswordExpiry) {
        this.resetPasswordExpiry = resetPasswordExpiry;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
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

    @Override
    public String toString() {
        return "Users{" +
               "userId=" + userId +
               ", roleId=" + roleId +
               ", fullName='" + fullName + '\'' +
               ", password='[PROTECTED]'" +
               ", image='" + image + '\'' +
               ", gender=" + gender +
               ", email='" + email + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", address='" + address + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", status=" + status +
               ", resetPasswordToken='" + resetPasswordToken + '\'' +
               ", resetPasswordExpiry=" + resetPasswordExpiry +
               ", roleName='" + roleName + '\'' +
               ", departmentId=" + departmentId +
               ", departmentName='" + departmentName + '\'' +
               '}';
    }
}

