package util;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author duong
 */
import dal.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
public class UpdatePasswordTool {
      public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // Ví dụ update password cho 1 user
        String email = "duyddhe173473@fpt.edu.vn"; // sửa thành email user bạn muốn update
        String newPassword = "0332105025Duy!"; // password mới muốn đặt

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        boolean result = dao.updatePasswordbyEmail(email, newPassword);

        if (result) {
            System.out.println("Update password success for user: " + email);
        } else {
            System.out.println("Update password FAILED for user: " + email);
        }
    }

}
