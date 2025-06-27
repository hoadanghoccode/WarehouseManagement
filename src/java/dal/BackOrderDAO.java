package dal;

import model.BackOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BackOrderDAO extends DBContext {

    private static final int PAGE_SIZE = 5;

    public List<BackOrder> getAllBackOrders(String search, String status, String sortBy, int page) throws SQLException {
        List<BackOrder> backOrders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT bo.BackOrder_id, bo.Order_detail_id, bo.Material_id, m.Name AS Material_name,
                   bo.SubUnit_id, su.Name AS SubUnit_name, bo.Quality_id, bo.Requested_quantity,
                   bo.Remaining_quantity, COALESCE(md.Quantity, 0) AS Available_quantity,
                   bo.Status, bo.Created_at, bo.Updated_at, bo.Note
            FROM BackOrder bo
            JOIN Materials m ON bo.Material_id = m.Material_id
            JOIN SubUnits su ON bo.SubUnit_id = su.SubUnit_id
            LEFT JOIN Material_detail md ON bo.Material_id = md.Material_id
                AND bo.SubUnit_id = md.SubUnit_id
                AND bo.Quality_id = md.Quality_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (m.Name LIKE ? OR bo.Note LIKE ?)");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND bo.Status = ?");
            params.add(status);
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "bo.Created_at";
        } else {
            switch (sortBy) {
                case "Remaining_quantity":
                    sortBy = "bo.Remaining_quantity";
                    break;
                case "Material_name":
                    sortBy = "m.Name";
                    break;
                case "Available_quantity":
                    sortBy = "md.Quantity";
                    break;
                default:
                    sortBy = "bo.Created_at";
            }
        }
        sql.append(" ORDER BY ").append(sortBy).append(" DESC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(PAGE_SIZE);
        params.add((page - 1) * PAGE_SIZE);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BackOrder bo = new BackOrder();
                    bo.setBackOrderId(rs.getInt("BackOrder_id"));
                    bo.setOrderDetailId(rs.getInt("Order_detail_id"));
                    bo.setMaterialId(rs.getInt("Material_id"));
                    bo.setMaterialName(rs.getString("Material_name"));
                    bo.setSubUnitId(rs.getInt("SubUnit_id"));
                    bo.setSubUnitName(rs.getString("SubUnit_name"));
                    bo.setQualityId(rs.getInt("Quality_id"));
                    bo.setRequestedQuantity(rs.getDouble("Requested_quantity"));
                    bo.setRemainingQuantity(rs.getDouble("Remaining_quantity"));
                    bo.setAvailableQuantity(rs.getDouble("Available_quantity"));
                    bo.setStatus(rs.getString("Status"));
                    bo.setCreatedAt(rs.getDate("Created_at"));
                    bo.setUpdatedAt(rs.getDate("Updated_at"));
                    bo.setNote(rs.getString("Note"));
                    backOrders.add(bo);
                }
            }
        }
        return backOrders;
    }

    public int getTotalBackOrders(String search, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM BackOrder bo
            JOIN Materials m ON bo.Material_id = m.Material_id
            WHERE 1=1
        """);
        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (m.Name LIKE ? OR bo.Note LIKE ?)");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND bo.Status = ?");
            params.add(status);
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int[] getBackOrderStats(String search) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                COUNT(*) AS total,
                SUM(CASE WHEN bo.Status = 'PENDING' THEN 1 ELSE 0 END) AS pending,
                SUM(CASE WHEN bo.Status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed,
                SUM(CASE WHEN bo.Status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled,
                SUM(CASE WHEN bo.Status = 'EXPORTED' THEN 1 ELSE 0 END) AS exported
            FROM BackOrder bo
            JOIN Materials m ON bo.Material_id = m.Material_id
            WHERE 1=1
        """);
        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (m.Name LIKE ? OR bo.Note LIKE ?)");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new int[] {
                        rs.getInt("total"),
                        rs.getInt("pending"),
                        rs.getInt("completed"),
                        rs.getInt("cancelled"),
                        rs.getInt("exported")
                    };
                }
            }
        }
        return new int[] {0, 0, 0, 0, 0};
    }

    public void updateBackOrder(int backOrderId, String newStatus, String priority, String note) throws SQLException {
        String sql = """
            UPDATE BackOrder
            SET Status = COALESCE(?, Status), Note = ?, Updated_at = CURRENT_TIMESTAMP
            WHERE BackOrder_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            String updatedNote = (priority != null ? "Priority: " + priority + ". " : "") + (note != null ? note : "");
            stmt.setString(2, updatedNote);
            stmt.setInt(3, backOrderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update BackOrder with ID: " + backOrderId);
            }
        }
    }

    public boolean exportBackOrder(int backOrderId) throws SQLException {
        BackOrder backOrder = getBackOrderById(backOrderId);
        if (backOrder == null || backOrder.getRemainingQuantity() <= 0 || !"PENDING".equals(backOrder.getStatus())) {
            return false;
        }

        double remainingQty = backOrder.getRemainingQuantity();
        double availableQty = backOrder.getAvailableQuantity();
        if (availableQty < remainingQty) {
            return false;
        }

        String updateMaterialSql = """
            UPDATE Material_detail md
            JOIN BackOrder bo ON md.Material_id = bo.Material_id 
                AND md.SubUnit_id = bo.SubUnit_id 
                AND md.Quality_id = bo.Quality_id
            SET md.Quantity = md.Quantity - ?
            WHERE bo.BackOrder_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(updateMaterialSql)) {
            stmt.setDouble(1, remainingQty);
            stmt.setInt(2, backOrderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No matching Material_detail found for BackOrder ID: " + backOrderId);
            }
        }

        String updateBackOrderSql = """
            UPDATE BackOrder
            SET Remaining_quantity = 0, Status = 'EXPORTED', Updated_at = CURRENT_TIMESTAMP
            WHERE BackOrder_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(updateBackOrderSql)) {
            stmt.setInt(1, backOrderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update BackOrder with ID: " + backOrderId);
            }
        }

        return true;
    }

    public BackOrder getBackOrderById(int backOrderId) throws SQLException {
        String sql = """
            SELECT bo.BackOrder_id, bo.Order_detail_id, bo.Material_id, m.Name AS Material_name,
                   bo.SubUnit_id, su.Name AS SubUnit_name, bo.Quality_id, bo.Requested_quantity,
                   bo.Remaining_quantity, COALESCE(md.Quantity, 0) AS Available_quantity,
                   bo.Status, bo.Created_at, bo.Updated_at, bo.Note
            FROM BackOrder bo
            JOIN Materials m ON bo.Material_id = m.Material_id
            JOIN SubUnits su ON bo.SubUnit_id = su.SubUnit_id
            LEFT JOIN Material_detail md ON bo.Material_id = md.Material_id
                AND bo.SubUnit_id = md.SubUnit_id
                AND bo.Quality_id = md.Quality_id
            WHERE bo.BackOrder_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, backOrderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BackOrder bo = new BackOrder();
                    bo.setBackOrderId(rs.getInt("BackOrder_id"));
                    bo.setOrderDetailId(rs.getInt("Order_detail_id"));
                    bo.setMaterialId(rs.getInt("Material_id"));
                    bo.setMaterialName(rs.getString("Material_name"));
                    bo.setSubUnitId(rs.getInt("SubUnit_id"));
                    bo.setSubUnitName(rs.getString("SubUnit_name"));
                    bo.setQualityId(rs.getInt("Quality_id"));
                    bo.setRequestedQuantity(rs.getDouble("Requested_quantity"));
                    bo.setRemainingQuantity(rs.getDouble("Remaining_quantity"));
                    bo.setAvailableQuantity(rs.getDouble("Available_quantity"));
                    bo.setStatus(rs.getString("Status"));
                    bo.setCreatedAt(rs.getDate("Created_at"));
                    bo.setUpdatedAt(rs.getDate("Updated_at"));
                    bo.setNote(rs.getString("Note"));
                    return bo;
                }
            }
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } finally {
            super.finalize();
        }
    }
}