package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Quality;

public class QualityDAO extends DBContext {

    public List<Quality> getAllQualities() {
        List<Quality> list = new ArrayList<>();
        String sql = "SELECT Quality_id, Quality_name, Status FROM Quality";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Quality q = new Quality();
                q.setQualityId(rs.getInt("Quality_id"));
                q.setQualityName(rs.getString("Quality_name"));
                q.setStatus(rs.getString("Status"));
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Quality getQualityById(int qualityId) {
        String sql = "SELECT * FROM Quality WHERE Quality_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, qualityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Quality q = new Quality();
                    q.setQualityId(rs.getInt("Quality_id"));
                    q.setQualityName(rs.getString("Quality_name"));
                    q.setStatus(rs.getString("Status"));
                    return q;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
