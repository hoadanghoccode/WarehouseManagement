package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class EmailDAO {

    private Connection conn;

    public EmailDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    public String getTemplateBodyByType(String type) {
        String body = null;
        String sql = "SELECT body FROM TemplateEmail WHERE type = ? AND isActive = 1 LIMIT 1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            rs = ps.executeQuery();
            if (rs.next()) {
                body = rs.getString("body");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    public String fillTemplate(String template, Map<String, String> values) {
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            // {user_name} -> actual name
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

}
