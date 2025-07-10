/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import model.MaterialStatistic;
/**
 *
 * @author duong
 */
public class ResetService {
    private final int LIMIT_MINUS = 10;
    static final String from = "nhom2.swp391@gmail.com";
    static final String password = "tvun mzuk vfqb arly";
    
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
    
    public LocalDateTime expireDateTime() {
         return ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusMinutes(LIMIT_MINUS).toLocalDateTime();
    }
    
    public boolean isExpireTime(LocalDateTime time) {
         ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    LocalDateTime now = ZonedDateTime.now(zoneId).toLocalDateTime();
    return now.isAfter(time);
    }
    
    
    public boolean sendEmail(String to, String content, String name, String subject) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        
        Session session = Session.getInstance(props, auth);
        try {
            MimeMessage msg = new MimeMessage(session);
        msg.setFrom(from);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(content, "text/html; charset=UTF-8");
        Transport.send(msg);
        System.out.println("Send successfully");
        return true;
    } catch (Exception e) {
        System.out.println("Send error: " + e.getMessage());
        return false;
    }
    }
    public String generateRandomPassword() {
    int length = 10;
    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lower = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";
    String special = "!@#$%^&*()-_+=";

    String allChars = upper + lower + digits + special;
    StringBuilder password = new StringBuilder();
    Random random = new Random();

    // Đảm bảo ít nhất 1 ký tự từ mỗi nhóm
    password.append(upper.charAt(random.nextInt(upper.length())));
    password.append(lower.charAt(random.nextInt(lower.length())));
    password.append(digits.charAt(random.nextInt(digits.length())));
    password.append(special.charAt(random.nextInt(special.length())));

    // Thêm các ký tự ngẫu nhiên còn lại
    for (int i = 4; i < length; i++) {
        password.append(allChars.charAt(random.nextInt(allChars.length())));
    }

    // Shuffle để mật khẩu không theo thứ tự
    List<Character> pwdChars = password.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    Collections.shuffle(pwdChars);

    StringBuilder finalPassword = new StringBuilder();
    for (char c : pwdChars) {
        finalPassword.append(c);
    }

    return finalPassword.toString();
}
}
