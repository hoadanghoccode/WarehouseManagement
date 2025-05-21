/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Users;
/**
 *
 * @author duong
 */
public class Logindao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    public Users checkLogin(String email,String pass){
        try{
            String query = "select * from users where email = ? and password = ?";
            conn = new DBContext().getConnection();
            
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while(rs.next()){
                Users u = new Users(
                    rs.getInt("User_id"),
                    rs.getInt("Role_id"),
                    rs.getInt("Branch_id"),
                    rs.getString("Full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("gender"),
                    rs.getString("phone_Number"),
                    rs.getString("address"),
                    rs.getDate("date_Of_Birth")
                );
                return u;
            }
        } catch (Exception e){
             e.printStackTrace(); 
        }
        
        return null;
        

    }
   
}
