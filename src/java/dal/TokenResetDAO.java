/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import model.TokenResetPassword;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author duong
 */
public class TokenResetDAO extends DBContext{
    public String getFormatDate(LocalDateTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
    
    public boolean  insertTokenReset(TokenResetPassword tokenReset){
         String sql = "INSERT INTO tokenresetpassword (userId, token, expiryTime, isUsed) VALUES (?, ?, ?, ?)";
    try {
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, tokenReset.getUserId());
        ps.setString(2, tokenReset.getToken());
        ps.setTimestamp(3, Timestamp.valueOf(tokenReset.getExpiryTime()));
        ps.setBoolean(4, tokenReset.isIsUsed());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
    }
    public TokenResetPassword getTokenPassword(String token) {
    String sql = "SELECT * FROM tokenresetpassword WHERE token = ?";
    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, token);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return new TokenResetPassword(rs.getInt("userId"),
                                          rs.getString("token"), 
                                          rs.getTimestamp("expiryTime").toLocalDateTime(), 
                                          rs.getBoolean("isUsed"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public void updateStatus(TokenResetPassword token) {
    String sql = "UPDATE tokenrestpassword SET isUsed = ? WHERE token = ?";
    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setBoolean(1, token.isIsUsed());
        st.setString(2, token.getToken());
        st.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
