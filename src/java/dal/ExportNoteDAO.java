package dal;

import model.ExportNote;
import model.ExportNoteDetail;
import model.ExportNoteTransaction;
import model.Order;
import model.OrderDetail;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportNoteDAO extends DBContext {

    private static final int PAGE_SIZE = 5;

    public ExportNoteDAO() {
        super();
    }

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
            sql.append(" AND (en.Customer_name LIKE ? OR en.Order_id LIKE ?)");
            params.add("%" + search.trim() + "%");
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
                    note.setUserName(rs.getString("User_name"));
                    note.setWarehouseId(rs.getObject("Warehouse_id", Integer.class));
                    note.setWarehouseName(rs.getString("Warehouse_name"));
                    note.setCreatedAt(rs.getDate("Created_at"));
                    note.setCustomerName(rs.getString("Customer_name"));
                    note.setExported(rs.getBoolean("Exported"));
                    note.setExportedAt(rs.getDate("Exported_at"));
                    note.setDetails(getExportNoteDetailsByNoteId(rs.getInt("Export_note_id")));
                    note.setHasBackOrder(hasPendingExportDetails(rs.getInt("Export_note_id")));
                    note.setAllExported(!hasPendingExportDetails(rs.getInt("Export_note_id")));
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
            sql.append(" AND (en.Customer_name LIKE ? OR en.Order_id LIKE ?)");
            params.add("%" + search.trim() + "%");
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
                   m.Unit_id, u.Name AS Unit_name, end.Quantity, end.Exported, 
                   end.Quality_id, q.Quality_name, COALESCE(imd.Ending_qty, 0) AS Available_quantity,
                   m.Image AS Material_image
            FROM Export_note_detail end
            JOIN Materials m ON end.Material_id = m.Material_id
            JOIN Units u ON m.Unit_id = u.Unit_id
            JOIN Quality q ON end.Quality_id = q.Quality_id
            LEFT JOIN Material_detail md ON end.Material_id = md.Material_id 
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
                    detail.setUnitId(rs.getInt("Unit_id"));
                    detail.setUnitName(rs.getString("Unit_name"));
                    detail.setQuantity(rs.getDouble("Quantity"));
                    detail.setExported(rs.getBoolean("Exported"));
                    detail.setQualityId(rs.getInt("Quality_id"));
                    detail.setQualityName(rs.getString("Quality_name"));
                    detail.setAvailableQuantity(rs.getDouble("Available_quantity"));
                    detail.setMaterialImage(rs.getString("Material_image"));
                    details.add(detail);
                }
            }
        }
        // Attach transactions to details
        List<ExportNoteTransaction> transactions = getAllExportTransactions(exportNoteId);
        for (ExportNoteDetail detail : details) {
            for (ExportNoteTransaction transaction : transactions) {
                if (transaction.getExportNoteDetailId() == detail.getExportNoteDetailId() && !transaction.isExported()) {
                    detail.setTransaction(transaction);
                    break;
                }
            }
        }
        return details;
    }

    public Order getOrderByExportNoteId(int exportNoteId) throws SQLException {
        String sql = """
            SELECT o.Order_id, o.Warehouse_id, o.User_id, u.Full_name AS User_name,
                   o.Created_at, o.Type, o.Supplier_id, s.Name AS Supplier_name, 
                   o.Note, o.Status
            FROM Orders o
            JOIN Export_note en ON o.Order_id = en.Order_id
            LEFT JOIN Users u ON o.User_id = u.User_id
            LEFT JOIN Suppliers s ON o.Supplier_id = s.Supplier_id
            WHERE en.Export_note_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("Order_id"));
                    order.setWarehouseId(rs.getInt("Warehouse_id"));
                    order.setUserId(rs.getInt("User_id"));
                    order.setUserName(rs.getString("User_name"));
                    order.setCreatedAt(rs.getTimestamp("Created_at"));
                    order.setType(rs.getString("Type"));
                    order.setSupplier(rs.getObject("Supplier_id", Integer.class) != null ? rs.getInt("Supplier_id") : 0);
                    order.setSupplierName(rs.getString("Supplier_name"));
                    order.setNote(rs.getString("Note"));
                    order.setStatus(rs.getString("Status"));
                    return order;
                }
            }
        }
        return null;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        String sql = """
            SELECT od.Order_detail_id, od.Order_id, od.Material_id, m.Name AS Material_name,
                   m.Image AS Material_image, m.Unit_id, u.Name AS Unit_name, 
                   od.Quantity, od.Quality_id
            FROM Order_detail od
            JOIN Materials m ON od.Material_id = m.Material_id
            JOIN Units u ON m.Unit_id = u.Unit_id
            WHERE od.Order_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderDetailId(rs.getInt("Order_detail_id"));
                    detail.setOrderId(rs.getInt("Order_id"));
                    detail.setMaterialId(rs.getInt("Material_id"));
                    detail.setMaterialName(rs.getString("Material_name"));
                    detail.setMaterialImage(rs.getString("Material_image"));
                    detail.setUnitName(rs.getString("Unit_name"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setQualityId(rs.getInt("Quality_id"));
                    details.add(detail);
                }
            }
        }
        return details;
    }

    public List<ExportNoteTransaction> getPendingExportTransactions(int exportNoteId) throws SQLException {
        List<ExportNoteTransaction> transactions = new ArrayList<>();
        String sql = """
            SELECT ent.Export_note_transaction_id, ent.Export_note_detail_id, ent.Material_id, m.Name AS Material_name,
                   m.Unit_id, u.Name AS Unit_name, ent.Requested_quantity, ent.Exported_quantity, ent.Remaining_quantity,
                   ent.Exported, ent.Quality_id, q.Quality_name, COALESCE(imd.Ending_qty, 0) AS Available_quantity,
                   ent.Created_at, end.Export_note_id
            FROM Export_note_transaction ent
            JOIN Export_note_detail end ON ent.Export_note_detail_id = end.Export_note_detail_id
            JOIN Materials m ON ent.Material_id = m.Material_id
            JOIN Units u ON m.Unit_id = u.Unit_id
            JOIN Quality q ON ent.Quality_id = q.Quality_id
            LEFT JOIN Material_detail md ON ent.Material_id = md.Material_id 
                AND ent.Quality_id = md.Quality_id
            LEFT JOIN InventoryMaterialDaily imd ON md.Material_detail_id = imd.Material_detail_id 
                AND imd.Inventory_Material_date = CURDATE()
            WHERE end.Export_note_id = ? AND ent.Exported = FALSE
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExportNoteTransaction transaction = new ExportNoteTransaction();
                    transaction.setExportNoteTransactionId(rs.getInt("Export_note_transaction_id"));
                    transaction.setExportNoteDetailId(rs.getInt("Export_note_detail_id"));
                    transaction.setExportNoteId(rs.getInt("Export_note_id"));
                    transaction.setMaterialId(rs.getInt("Material_id"));
                    transaction.setMaterialName(rs.getString("Material_name"));
                    transaction.setUnitId(rs.getInt("Unit_id"));
                    transaction.setUnitName(rs.getString("Unit_name"));
                    transaction.setRequestedQuantity(rs.getDouble("Requested_quantity"));
                    transaction.setExportedQuantity(rs.getDouble("Exported_quantity"));
                    transaction.setRemainingQuantity(rs.getDouble("Remaining_quantity"));
                    transaction.setExported(rs.getBoolean("Exported"));
                    transaction.setQualityId(rs.getInt("Quality_id"));
                    transaction.setQualityName(rs.getString("Quality_name"));
                    transaction.setAvailableQuantity(rs.getDouble("Available_quantity"));
                    transaction.setCreatedAt(rs.getTimestamp("Created_at"));
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }

    public List<ExportNoteTransaction> getAllExportTransactions(int exportNoteId) throws SQLException {
        List<ExportNoteTransaction> transactions = new ArrayList<>();
        String sql = """
            SELECT ent.Export_note_transaction_id, ent.Export_note_detail_id, ent.Material_id, m.Name AS Material_name,
                   m.Unit_id, u.Name AS Unit_name, ent.Requested_quantity, ent.Exported_quantity, 
                   ent.Remaining_quantity, ent.Exported, ent.Quality_id, q.Quality_name, 
                   COALESCE(imd.Ending_qty, 0) AS Available_quantity, ent.Created_at, end.Export_note_id
            FROM Export_note_transaction ent
            JOIN Export_note_detail end ON ent.Export_note_detail_id = end.Export_note_detail_id
            JOIN Materials m ON ent.Material_id = m.Material_id
            JOIN Units u ON m.Unit_id = u.Unit_id
            JOIN Quality q ON ent.Quality_id = q.Quality_id
            LEFT JOIN Material_detail md ON ent.Material_id = md.Material_id 
                AND ent.Quality_id = md.Quality_id
            LEFT JOIN InventoryMaterialDaily imd ON md.Material_detail_id = imd.Material_detail_id 
                AND imd.Inventory_Material_date = CURDATE()
            WHERE end.Export_note_id = ?
            ORDER BY ent.Created_at DESC
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExportNoteTransaction transaction = new ExportNoteTransaction();
                    transaction.setExportNoteTransactionId(rs.getInt("Export_note_transaction_id"));
                    transaction.setExportNoteDetailId(rs.getInt("Export_note_detail_id"));
                    transaction.setExportNoteId(rs.getInt("Export_note_id"));
                    transaction.setMaterialId(rs.getInt("Material_id"));
                    transaction.setMaterialName(rs.getString("Material_name"));
                    transaction.setUnitId(rs.getInt("Unit_id"));
                    transaction.setUnitName(rs.getString("Unit_name"));
                    transaction.setRequestedQuantity(rs.getDouble("Requested_quantity"));
                    transaction.setExportedQuantity(rs.getDouble("Exported_quantity"));
                    transaction.setRemainingQuantity(rs.getDouble("Remaining_quantity"));
                    transaction.setExported(rs.getBoolean("Exported"));
                    transaction.setQualityId(rs.getInt("Quality_id"));
                    transaction.setQualityName(rs.getString("Quality_name"));
                    transaction.setAvailableQuantity(rs.getDouble("Available_quantity"));
                    transaction.setCreatedAt(rs.getTimestamp("Created_at"));
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }

    public int addExportNote(ExportNote note) throws SQLException {
        String sql = """
            INSERT INTO Export_note (Order_id, User_id, Warehouse_id, Created_at, Customer_name, Exported, Exported_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            INSERT INTO Export_note_detail (Export_note_id, Material_id, Quantity, Quality_id, Exported)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getExportNoteId());
            stmt.setInt(2, detail.getMaterialId());
            stmt.setDouble(3, detail.getQuantity());
            stmt.setInt(4, detail.getQualityId());
            stmt.setBoolean(5, detail.isExported());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detail.setExportNoteDetailId(rs.getInt(1));
                }
            }
        }
    }

    public boolean checkInventoryAvailability(int materialDetailId, double quantity) throws SQLException {
        String sql = """
            SELECT imd.Ending_qty
            FROM InventoryMaterialDaily imd
            WHERE imd.Material_detail_id = ? 
            AND imd.Inventory_Material_date = CURDATE()
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
            SELECT COALESCE(Ending_qty, 0) AS Available_quantity
            FROM InventoryMaterialDaily
            WHERE Material_detail_id = ? AND Inventory_Material_date = CURDATE()
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Available_quantity");
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

    public int getMaterialDetailId(int materialId, int qualityId) throws SQLException {
        String sql = """
            SELECT Material_detail_id
            FROM Material_detail
            WHERE Material_id = ? AND Quality_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            stmt.setInt(2, qualityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Material_detail_id");
                }
                throw new SQLException("Material_detail_id not found for Material_id: " + materialId + ", Quality_id: " + qualityId);
            }
        }
    }

    public double getMaterialDetailQuantity(int materialDetailId) throws SQLException {
        String sql = "SELECT Quantity FROM Material_detail WHERE Material_detail_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Quantity");
                }
            }
        }
        return 0;
    }

    public void updateMaterialDetailQuantity(int materialDetailId, double quantity) throws SQLException {
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

    public ExportNoteTransaction getTransactionById(int transactionId) throws SQLException {
        String sql = """
            SELECT ent.*, end.Export_note_id
            FROM Export_note_transaction ent
            JOIN Export_note_detail end ON ent.Export_note_detail_id = end.Export_note_detail_id
            WHERE ent.Export_note_transaction_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ExportNoteTransaction tx = new ExportNoteTransaction();
                    tx.setExportNoteTransactionId(rs.getInt("Export_note_transaction_id"));
                    tx.setExportNoteDetailId(rs.getInt("Export_note_detail_id"));
                    tx.setExportNoteId(rs.getInt("Export_note_id"));
                    tx.setMaterialId(rs.getInt("Material_id"));
                    tx.setQualityId(rs.getInt("Quality_id"));
                    tx.setRequestedQuantity(rs.getDouble("Requested_quantity"));
                    tx.setExportedQuantity(rs.getDouble("Exported_quantity"));
                    tx.setRemainingQuantity(rs.getDouble("Remaining_quantity"));
                    tx.setExported(rs.getBoolean("Exported"));
                    tx.setCreatedAt(rs.getTimestamp("Created_at"));
                    return tx;
                }
            }
        }
        throw new SQLException("Không tìm thấy giao dịch với ID: " + transactionId);
    }

    public boolean isFirstExport(int exportNoteDetailId) throws SQLException {
        String sql = """
            SELECT COUNT(*) 
            FROM Export_note_transaction
            WHERE Export_note_detail_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

    public void markAsExported(
            int exportNoteId,
            List<Integer> detailIds,
            List<Double> detailQuantities,
            List<Integer> detailMaterialIds,
            List<Integer> unitIds,
            List<Integer> detailQualityIds,
            List<Integer> transactionIds,
            HttpServletRequest request
    ) throws SQLException {
        connection.setAutoCommit(false);
        StringBuilder pendingMessage = new StringBuilder();

        try {
            // Validate inputs
            if (detailIds.size() != detailQuantities.size() || detailIds.size() != detailMaterialIds.size()
                    || detailIds.size() != unitIds.size() || detailIds.size() != detailQualityIds.size()) {
                throw new SQLException("Mismatched input arrays for detail export.");
            }

            // Validate transactions belong to the correct export note
            for (int transactionId : transactionIds) {
                ExportNoteTransaction tx = getTransactionById(transactionId);
                if (tx.getExportNoteId() != exportNoteId) {
                    pendingMessage.append("Transaction #").append(transactionId)
                            .append(" does not belong to Export Note ID ").append(exportNoteId).append(". ");
                }
            }

            // Validate inventory for new export details
            for (int i = 0; i < detailIds.size(); i++) {
                int detailId = detailIds.get(i);
                double requestedQty = detailQuantities.get(i);
                int materialId = detailMaterialIds.get(i);
                int qualityId = detailQualityIds.get(i);
                int materialDetailId = getMaterialDetailId(materialId, qualityId);
                double availableQty = getAvailableQuantity(materialDetailId);

                if (requestedQty < 0) {
                    pendingMessage.append("Invalid requested quantity for Detail ID ").append(detailId)
                            .append(": ").append(String.format("%.2f", requestedQty)).append(". ");
                }
                if (availableQty <= 0) {
                    pendingMessage.append("No inventory available for Material ID ").append(materialId).append(". ");
                }
            }

            // Validate inventory for existing transactions
            for (int transactionId : transactionIds) {
                ExportNoteTransaction tx = getTransactionById(transactionId);
                int materialDetailId = getMaterialDetailId(tx.getMaterialId(), tx.getQualityId());
                double availableQty = getAvailableQuantity(materialDetailId);
                double remainingQty = tx.getRemainingQuantity();

                if (remainingQty < 0) {
                    pendingMessage.append("Invalid remaining quantity for Transaction ID ").append(transactionId)
                            .append(": ").append(String.format("%.2f", remainingQty)).append(". ");
                }
                if (availableQty < remainingQty) {
                    pendingMessage.append("Transaction #").append(transactionId)
                            .append(": Insufficient inventory. Required: ").append(String.format("%.2f", remainingQty))
                            .append(", Available: ").append(String.format("%.2f", availableQty)).append(". ");
                }
            }

            if (pendingMessage.length() > 0) {
                throw new SQLException(pendingMessage.toString());
            }

            // Process new export details
            for (int i = 0; i < detailIds.size(); i++) {
                int detailId = detailIds.get(i);
                double requestedQty = detailQuantities.get(i);
                int materialId = detailMaterialIds.get(i);
                int qualityId = detailQualityIds.get(i);
                int materialDetailId = getMaterialDetailId(materialId, qualityId);
                double availableQty = getAvailableQuantity(materialDetailId);

                boolean firstExport = isFirstExport(detailId);
                if (!firstExport) {
                    continue; // Skip if not first export, handled in transaction loop
                }
                double exportQty = Math.min(availableQty, requestedQty);
                double remainingQty = requestedQty - exportQty;

                // Update Export_note_detail
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE Export_note_detail SET Exported = ? WHERE Export_note_detail_id = ?"
                )) {
                    stmt.setBoolean(1, remainingQty == 0);
                    stmt.setInt(2, detailId);
                    stmt.executeUpdate();
                }

                // Insert Export_note_transaction (remove Remaining_quantity)
                try (PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO Export_note_transaction (Export_note_detail_id, Material_id, Quality_id, Requested_quantity, Exported_quantity, Exported, Created_at) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)"
                )) {
                    stmt.setInt(1, detailId);
                    stmt.setInt(2, materialId);
                    stmt.setInt(3, qualityId);
                    stmt.setDouble(4, requestedQty);
                    stmt.setDouble(5, exportQty);
                    stmt.setBoolean(6, remainingQty == 0);
                    stmt.executeUpdate();
                }

                // Update inventory
                if (exportQty > 0) {
                    updateInventoryMaterialDaily(materialDetailId, exportQty);
                    updateMaterialDetailQuantity(materialDetailId, exportQty);

                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO MaterialTransactionHistory (Material_detail_id, Export_note_detail_id, Transaction_date, Note) VALUES (?, ?, CURDATE(), ?)"
                    )) {
                        stmt.setInt(1, materialDetailId);
                        stmt.setInt(2, detailId);
                        stmt.setString(3, "Exported " + String.format("%.2f", exportQty) + " units");
                        stmt.executeUpdate();
                    }
                }

                // Inform user about partial export
                if (remainingQty > 0) {
                    pendingMessage.append("Material ID ").append(materialId)
                            .append(": Partial export of ").append(String.format("%.2f", exportQty))
                            .append(" units. Remaining: ").append(String.format("%.2f", remainingQty))
                            .append(" units not exported due to insufficient inventory. ");
                } else {
                    pendingMessage.append("Material ID ").append(materialId)
                            .append(": Fully exported ").append(String.format("%.2f", exportQty))
                            .append(" units. ");
                }
            }

            // Process existing transactions
            for (int transactionId : transactionIds) {
                ExportNoteTransaction tx = getTransactionById(transactionId);
                int exportNoteDetailId = tx.getExportNoteDetailId();
                double remainingQty = tx.getRemainingQuantity();
                int materialDetailId = getMaterialDetailId(tx.getMaterialId(), tx.getQualityId());
                double availableQty = getAvailableQuantity(materialDetailId);

                double exportQty = Math.min(availableQty, remainingQty);
                double newRemainingQty = remainingQty - exportQty;

                // Update Export_note_transaction (remove Remaining_quantity)
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE Export_note_transaction SET Exported_quantity = Exported_quantity + ?, Exported = ? WHERE Export_note_transaction_id = ?"
                )) {
                    stmt.setDouble(1, exportQty);
                    stmt.setBoolean(2, newRemainingQty == 0);
                    stmt.setInt(3, transactionId);
                    stmt.executeUpdate();
                }

                // Update Export_note_detail
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE Export_note_detail SET Exported = ? WHERE Export_note_detail_id = ?"
                )) {
                    stmt.setBoolean(1, newRemainingQty == 0);
                    stmt.setInt(2, exportNoteDetailId);
                    stmt.executeUpdate();
                }

                // Update inventory
                if (exportQty > 0) {
                    updateInventoryMaterialDaily(materialDetailId, exportQty);
                    updateMaterialDetailQuantity(materialDetailId, exportQty);

                    try (PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO MaterialTransactionHistory (Material_detail_id, Export_note_detail_id, Transaction_date, Note) VALUES (?, ?, CURDATE(), ?)"
                    )) {
                        stmt.setInt(1, materialDetailId);
                        stmt.setInt(2, exportNoteDetailId);
                        stmt.setString(3, "Exported " + String.format("%.2f", exportQty) + " units from transaction");
                        stmt.executeUpdate();
                    }
                }

                // Inform user about partial export
                if (newRemainingQty > 0) {
                    pendingMessage.append("Transaction ID ").append(transactionId)
                            .append(": Partial export of ").append(String.format("%.2f", exportQty))
                            .append(" units. Remaining: ").append(String.format("%.2f", newRemainingQty))
                            .append(" units not exported due to insufficient inventory. ");
                } else {
                    pendingMessage.append("Transaction ID ").append(transactionId)
                            .append(": Fully exported ").append(String.format("%.2f", exportQty))
                            .append(" units. ");
                }
            }

            // Update Export_note status
            boolean allExported = !hasPendingExportDetails(exportNoteId);
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE Export_note SET Exported = ?, Exported_at = ? WHERE Export_note_id = ?"
            )) {
                stmt.setBoolean(1, allExported);
                stmt.setDate(2, allExported ? new Date(System.currentTimeMillis()) : null);
                stmt.setInt(3, exportNoteId);
                stmt.executeUpdate();
            }

            if (pendingMessage.length() == 0) {
                pendingMessage.append("Export processed successfully.");
            }
            request.setAttribute("pendingMessage", pendingMessage.toString());

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean hasPendingExportDetails(int exportNoteId) throws SQLException {
        String sql = """
            SELECT COUNT(*) 
            FROM Export_note_detail end
            WHERE end.Export_note_id = ? AND end.Exported = FALSE
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

    public int getTodayExportNoteCount() {
        String sql = "SELECT COUNT(*) FROM Export_note WHERE DATE(Exported_at) = CURDATE() AND Exported = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getExportNoteCountInDateRange(Date fromDate, Date toDate) {
        String sql = "SELECT COUNT(*) FROM Export_note WHERE DATE(Exported_at) BETWEEN ? AND ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ExportNote getExportNoteById(int exportNoteId) throws SQLException {
        String sql = """
            SELECT en.Export_note_id, en.Order_id, en.User_id, u.Full_name AS User_name,
                   en.Warehouse_id, w.Name AS Warehouse_name, en.Created_at,
                   en.Customer_name, en.Exported, en.Exported_at
            FROM Export_note en
            LEFT JOIN Users u ON en.User_id = u.User_id
            LEFT JOIN Warehouse w ON en.Warehouse_id = w.Warehouse_id
            WHERE en.Export_note_id = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exportNoteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ExportNote note = new ExportNote();
                    note.setExportNoteId(rs.getInt("Export_note_id"));
                    note.setOrderId(rs.getInt("Order_id"));
                    note.setUserId(rs.getInt("User_id"));
                    note.setUserName(rs.getString("User_name"));
                    note.setWarehouseId(rs.getObject("Warehouse_id", Integer.class));
                    note.setWarehouseName(rs.getString("Warehouse_name"));
                    note.setCreatedAt(rs.getDate("Created_at"));
                    note.setCustomerName(rs.getString("Customer_name"));
                    note.setExported(rs.getBoolean("Exported"));
                    note.setExportedAt(rs.getDate("Exported_at"));
                    note.setAllExported(!hasPendingExportDetails(exportNoteId));
                    return note;
                }
            }
        }
        return null;
    }
}
