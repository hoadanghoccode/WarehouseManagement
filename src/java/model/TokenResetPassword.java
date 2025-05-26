/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author duong
 */
public class TokenResetPassword {
    private int id, userId;
private String token;
private LocalDateTime expiryTime;
private boolean isUsed;

    public TokenResetPassword() {
    }

    public TokenResetPassword(int id, int userId, String token, LocalDateTime expiryTime, boolean isUsed) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
    }

    public TokenResetPassword(int userId, String token, LocalDateTime expiryTime, boolean isUsed) {
        this.userId = userId;
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

   
}
