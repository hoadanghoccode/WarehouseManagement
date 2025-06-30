package dal;

import model.ExportNote;
import model.ExportNoteDetail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

public class ExportNoteDAO extends DBContext {

    private static final int PAGE_SIZE = 5;

    public List<ExportNote> getAllExportNotes(String search, String exported, String sortOrder, int page) throws SQLException {
        List<ExportNote> exportNotes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT en.Export_note_id, en.Order_id, en.User_id, u.Full_name AS User_name,
                   en.Warehouse_id, w.Name AS Warehouse_name, en.Created_at,
                   en.Customer_name, en.Exported, en.Exported_at
            FROM Export_note en
            LEFT JOIN Users u ON en.User_id = u.User_id
            LEFT JOIN Warehouse w ON en.Warehouse_id = w.Warehouse_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND en.Customer_name LIKE ?");
            params.add("%" + search.trim() + "%");
        }
        if (exported != null && !exported.isEmpty()) {
            sql.append(" AND en.Exported = ?");
            params.add(Boolean.parseBoolean(exported));
        }
        sql.append(" ORDER BY en.Created_at ").append(sortOrder != null && sortOrder.equals("asc") ? "ASC" : "DESC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(PAGE_SIZE);
        params.add((page - 1) * PAGE_SIZE);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExportNote note = new ExportNote();
                    note.setExportNoteId(rs.getInt("Export_note_id"));
                    note.setOrderId(rs.getInt("Order_id"));
                    note.setUserId(rs.getInt("User_id"));
                    note.setWarehouseId(rs.getObject("Warehouse_id", Integer.class));
                    note.setWarehouseName(rs.getString("Warehouse_name"));
                    note.setUserName(rs.getString("User_name"));
                    note.setCreatedAt(rs.getDate("Created_at"));
                    note.setCustomerName(rs.getString("Customer_name"));
                    note.setExported(rs.getBoolean("Exported"));
                    note.setExportedAt(rs.getDate("Exported_at"));
                    note.setDetails(getExportNoteDetailsByNoteId(rs.getInt("Export_note_id")));
                    note.setHasBackOrder(hasBackOrder(rs.getInt("Export_note_id")));
                    exportNotes.add(note);
                }
            }
        }
        return exportNotes;
    }

    public int getTotalExportNotes(String search, String exported) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*) 
            FROM Export_note en
            WHERE 1=1
        """);
        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND en.Customer_name LIKE ?");
            params.add("%" + search.trim() + "%");
        }
        if (exported != null && !exported.isEmpty()) {
            sql.append(" AND en.Exported = ?");
            params.add(Boolean.parseBoolean(exported));
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

    public List<ExportNoteDetail> getExportNoteDetailsByNoteId(int exportNoteId) throws SQLException {
    List<ExportNoteDetail> details = new ArrayList<>();
    String sql = """
        SELECT end.Export_note_detail_id, end.Export_note_id, end.Material_id, m.Name AS Material_name,
               end.SubUnit_id, su.Name AS SubUnit_name, end.Quantity, end.Quality_id, q.Quality_name,
               COALESCE(imd.Ending_qty, 0) AS Available_quantity
        FROM Export_note_detail end
        JOIN Materials m ON end.Material_id = m.Material_id
        JOIN SubUnits su ON end.SubUnit_id = su.SubUnit_id
        JOIN Quality q ON end.Quality_id = q.Quality_id
        JOIN Material_detail md ON end.Material_id = md.Material_id 
            AND end.SubUnit_id = md.SubUnit_id 
            AND end.Quality_id = md.Quality_id
        LEFT JOIN InventoryMaterialDaily imd ON md.Material_detail_id = imd.Material_detail_id 
            AND imd.Inventory_Material_date = CURDATE()
        WHERE end.Export_note_id = ?
    """;
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, exportNoteId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ExportNoteDetail detail = new ExportNoteDetail();
                detail.setExportNoteDetailId(rs.getInt("Export_note_detail_id"));
                detail.setExportNoteId(rs.getInt("Export_note_id"));
                detail.setMaterialId(rs.getInt("Material_id"));
                detail.setMaterialName(rs.getString("Material_name"));
                detail.setSubUnitId(rs.getInt("SubUnit_id"));
                detail.setSubUnitName(rs.getString("SubUnit_name"));
                detail.setQuantity(rs.getDouble("Quantity"));
                detail.setQualityId(rs.getInt("Quality_id"));
                detail.setQualityName(rs.getString("Quality_name"));
                detail.setAvailableQuantity(rs.getDouble("Available_quantity")); // Set the available quantity
                details.add(detail);
            }
        }
    }
    return details;
}

    public int addExportNote(ExportNote note) throws SQLException {
        String sql = """
            INSERT INTO Export_note (Order_id, User_id, Warehouse_id, Created_at, Customer_name, Exported, Exported_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getOrderId());
            stmt.setInt(2, note.getUserId());
            stmt.setObject(3, note.getWarehouseId());
            stmt.setDate(4, note.getCreatedAt());
            stmt.setString(5, note.getCustomerName());
            stmt.setBoolean(6, note.isExported());
            stmt.setDate(7, note.getExportedAt());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void addExportNoteDetail(ExportNoteDetail detail) throws SQLException {
        String sql = """
            INSERT INTO Export_note_detail (Export_note_id, Material_id, SubUnit_id, Quantity, Quality_id)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detail.getExportNoteId());
            stmt.setInt(2, detail.getMaterialId());
            stmt.setInt(3, detail.getSubUnitId());
            stmt.setDouble(4, detail.getQuantity());
            stmt.setInt(5, detail.getQualityId());
            stmt.executeUpdate();
        }
    }

    public boolean checkInventoryAvailability(int materialDetailId, double quantity) throws SQLException {
        String sql = """
            SELECT imd.Ending_qty
            FROM InventoryMaterialDaily imd
            JOIN Material_detail md ON imd.Material_detail_id = md.Material_detail_id
            JOIN Quality q ON md.Quality_id = q.Quality_id
            WHERE imd.Material_detail_id = ? 
            AND imd.Inventory_Material_date = CURDATE()
            AND q.Quality_name = 'available'
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Ending_qty") >= quantity;
                }
            }
        }
        return false;
    }

    public double getAvailableQuantity(int materialDetailId) throws SQLException {
        String sql = """
            SELECT Ending_qty
            FROM InventoryMaterialDaily
            WHERE Material_detail_id = ? AND Inventory_Material_date = CURDATE()
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Ending_qty");
                }
            }
        }
        return 0;
    }

    public void updateInventoryMaterialDaily(int materialDetailId, double quantity) throws SQLException {
        String selectSql = """
            SELECT Opening_qty, Import_qty, Export_qty
            FROM InventoryMaterialDaily
            WHERE Material_detail_id = ? AND Inventory_Material_date = CURDATE()
        """;
        Double openingQty = null;
        double currentImportQty = 0;
        double currentExportQty = 0;

        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setInt(1, materialDetailId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    openingQty = rs.getDouble("Opening_qty");
                    currentImportQty = rs.getDouble("Import_qty");
                    currentExportQty = rs.getDouble("Export_qty");
                }
            }
        }

        if (openingQty != null) {
            String updateSql = """
                UPDATE InventoryMaterialDaily
                SET Export_qty = ?, Ending_qty = ?
                WHERE Material_detail_id = ? AND Inventory_Material_date = CURDATE()
            """;
            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setDouble(1, currentExportQty + quantity);
                ps.setDouble(2, openingQty + currentImportQty - (currentExportQty + quantity));
                ps.setInt(3, materialDetailId);
                ps.executeUpdate();
            }
        } else {
            String insertSql = """
                INSERT INTO InventoryMaterialDaily (Material_detail_id, Inventory_Material_date, Opening_qty, Import_qty, Export_qty, Ending_qty, Note)
                SELECT md.Material_detail_id, CURDATE(), md.Quantity, 0, ?, md.Quantity - ?, NULL
                FROM Material_detail md
                WHERE md.Material_detail_id = ?
            """;
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setDouble(1, quantity);
                ps.setDouble(2, quantity);
                ps.setInt(3, materialDetailId);
                ps.executeUpdate();
            }
        }
    }

    public int getMaterialDetailId(int materialId, int subUnitId, int qualityId) throws SQLException {
        String sql = """
            SELECT Material_detail_id
            FROM Material_detail
            WHERE Material_id = ? AND SubUnit_id = ? AND Quality_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            stmt.setInt(2, subUnitId);
            stmt.setInt(3, qualityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Material_detail_id");
                }
                throw new SQLException("Material_detail_id not found for Material_id: " + materialId +
                                      ", SubUnit_id: " + subUnitId + ", Quality_id: " + qualityId);
            }
        }
    }

    public int getQualityIdByName(String qualityName) throws SQLException {
        String sql = """
            SELECT Quality_id
            FROM Quality
            WHERE Quality_name = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, qualityName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quality_id");
                }
                throw new SQLException("Quality not found for name: " + qualityName);
            }
        }
    }

    public void markAsExported(int exportNoteId, List<Integer> detailIds, List<Double> quantities, List<Integer> materialIds, 
                              List<Integer> subUnitIds, List<Integer> qualityIds, HttpServletRequest request) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String checkSql = "SELECT Exported FROM Export_note WHERE Export_note_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, exportNoteId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getBoolean("Exported")) {
                        throw new SQLException("This export note has already been exported.");
                    }
                }
            }

            String updateNoteSql = """
                UPDATE Export_note 
                SET Exported = TRUE, Exported_at = CURRENT_TIMESTAMP 
                WHERE Export_note_id = ?
            """;
            try (PreparedStatement stmt = connection.prepareStatement(updateNoteSql)) {
                stmt.setInt(1, exportNoteId);
                stmt.executeUpdate();
            }

            StringBuilder backOrderMessage = new StringBuilder();
            for (int i = 0; i < detailIds.size(); i++) {
                int detailId = detailIds.get(i);
                double requestedQuantity = quantities.get(i);
                int materialId = materialIds.get(i);
                int subUnitId = subUnitIds.get(i);
                int currentQualityId = qualityIds.get(i);

                int materialDetailId = getMaterialDetailId(materialId, subUnitId, currentQualityId);
                double availableQty = getAvailableQuantity(materialDetailId);

                double exportedQty = Math.min(requestedQuantity, availableQty > 0 ? availableQty : 0);
                if (exportedQty > 0) {
                    updateExportNoteDetail(detailId, exportedQty);
                    updateInventoryMaterialDaily(materialDetailId, exportedQty);
                    updateMaterialDetailQuantity(materialDetailId, exportedQty);
                    logTransaction(materialDetailId, detailId, exportedQty < requestedQuantity ? "Partial export from inventory" : "Exported from inventory");
                }
                if (exportedQty < requestedQuantity) {
                    double remainingQty = requestedQuantity - exportedQty;
                    // Lấy Supplier_id từ Materials
                    int supplierId = getSupplierIdFromMaterial(materialId);
                    // Lấy Order_detail_id từ Order_detail dựa trên Export_note
                    int orderDetailId = getOrderDetailIdFromExportNoteDetail(exportNoteId, detailId, materialId);
                    createBackOrder(orderDetailId, materialId, subUnitId, currentQualityId, supplierId, requestedQuantity, remainingQty);
                    backOrderMessage.append("Insufficient stock for Detail ID ").append(detailId)
                                   .append(". Exported ").append(exportedQty)
                                   .append(", Remaining ").append(remainingQty)
                                   .append(" added to BackOrder with Supplier ID ").append(supplierId).append(". ");
                }
            }

            if (backOrderMessage.length() > 0) {
                request.setAttribute("backOrderMessage", backOrderMessage.toString());
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void updateMaterialDetailQuantity(int materialDetailId, double quantity) throws SQLException {
        String checkSql = """
            SELECT Quantity FROM Material_detail WHERE Material_detail_id = ?
        """;
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, materialDetailId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getDouble("Quantity") < quantity) {
                    throw new SQLException("Insufficient quantity in Material_detail for Material_detail_id: " + materialDetailId);
                }
            }
        }

        String sql = """
            UPDATE Material_detail
            SET Quantity = Quantity - ?, Last_updated = CURRENT_TIMESTAMP
            WHERE Material_detail_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, quantity);
            stmt.setInt(2, materialDetailId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update Material_detail for Material_detail_id: " + materialDetailId);
            }
        }
    }

    private void updateExportNoteDetail(int detailId, double quantity) throws SQLException {
        String sql = """
            UPDATE Export_note_detail
            SET Quantity = ?
            WHERE Export_note_detail_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, quantity);
            stmt.setInt(2, detailId);
            stmt.executeUpdate();
        }
    }

    private void createBackOrder(int orderDetailId, int materialId, int subUnitId, int qualityId, int supplierId, double requestedQty, double remainingQty) throws SQLException {
        String sql = """
            INSERT INTO BackOrder (Order_detail_id, Material_id, SubUnit_id, Quality_id, Supplier_id, Requested_quantity, Remaining_quantity, Status, Created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING', CURRENT_TIMESTAMP)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderDetailId);
            stmt.setInt(2, materialId);
            stmt.setInt(3, subUnitId);
            stmt.setInt(4, qualityId);
            stmt.setInt(5, supplierId);
            stmt.setDouble(6, requestedQty);
            stmt.setDouble(7, remainingQty);
            stmt.executeUpdate();
        }
    }

    // Thêm phương thức để lấy Supplier_id từ Materials
    private int getSupplierIdFromMaterial(int materialId) throws SQLException {
        String sql = """
            SELECT SupplierId
            FROM Materials
            WHERE Material_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SupplierId");
                }
                throw new SQLException("Supplier_id not found for Material_id: " + materialId);
            }
        }
    }

    // Thêm phương thức để lấy Order_detail_id từ Export_note_detail
    private int getOrderDetailIdFromExportNoteDetail(int exportNoteId, int exportDetailId, int materialId) throws SQLException {
        String sql = """
            SELECT od.Order_detail_id
            FROM Order_detail od
            JOIN Export_note en ON od.Order_id = en.Order_id
            JOIN Export_note_detail end ON od.Material_id = end.Material_id
            WHERE en.Export_note_id = ? AND end.Export_note_detail_id = ? AND od.Material_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            stmt.setInt(2, exportDetailId);
            stmt.setInt(3, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Order_detail_id");
                }
                throw new SQLException("Order_detail_id not found for Export_note_id: " + exportNoteId + ", Export_note_detail_id: " + exportDetailId);
            }
        }
    }

    private void logTransaction(int materialDetailId, int detailId, String note) throws SQLException {
        String sql = """
            INSERT INTO MaterialTransactionHistory (Material_detail_id, Export_note_detail_id, Transaction_date, Note)
            VALUES (?, ?, CURDATE(), ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialDetailId);
            stmt.setInt(2, detailId);
            stmt.setString(3, note);
            stmt.executeUpdate();
        }
    }

    private void updateMaterialQuality(int materialDetailId, int qualityId) throws SQLException {
        String sql = """
            UPDATE Material_detail
            SET Quality_id = ?, Last_updated = CURRENT_TIMESTAMP
            WHERE Material_detail_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, qualityId);
            stmt.setInt(2, materialDetailId);
            stmt.executeUpdate();
        }
    }

    public boolean hasBackOrder(int exportNoteId) throws SQLException {
        String sql = """
            SELECT COUNT(*) 
            FROM BackOrder bo
            JOIN Export_note_detail end ON bo.Order_detail_id = end.Export_note_detail_id
            WHERE end.Export_note_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}