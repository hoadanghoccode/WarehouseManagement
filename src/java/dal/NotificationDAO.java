/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Notification;

/**
 *
 * @author PC
 */
public class NotificationDAO extends DBContext {
    private Connection conn;

    public NotificationDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    // Lấy danh sách notification của user (có trạng thái đã đọc/chưa)
    public List<Notification> getUserNotifications(int userId, int offset, int limit) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = """
            SELECT n.notification_id, n.title, n.content, n.link, n.created_at, nu.is_read, nu.read_at
            FROM Notification_User nu
            JOIN Notification n ON nu.notification_id = n.notification_id
            WHERE nu.user_id = ?
            ORDER BY n.created_at DESC
            LIMIT ?, ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification noti = new Notification();
                noti.setId(rs.getInt("notification_id"));
                noti.setTitle(rs.getString("title"));
                noti.setContent(rs.getString("content"));
                noti.setLink(rs.getString("link"));
                noti.setCreatedAt(rs.getTimestamp("created_at"));
                noti.setIsRead(rs.getBoolean("is_read"));
                noti.setReadAt(rs.getTimestamp("read_at"));
                list.add(noti);
            }
        }
        return list;
    }

    // Đếm số notification chưa đọc của user
    public int countUnreadNotifications(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notification_User WHERE user_id=? AND is_read=0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đánh dấu notification là đã đọc
    public void markAsRead(int userId, int notificationId) throws SQLException {
        String sql = "UPDATE Notification_User SET is_read=1, read_at=NOW() WHERE user_id=? AND notification_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, notificationId);
            ps.executeUpdate();
        }
    }
}
