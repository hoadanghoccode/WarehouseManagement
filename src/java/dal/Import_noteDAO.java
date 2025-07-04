package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import model.MaterialStatistic;
import model.Import_note;
import model.Import_note_detail;
import model.Material;
import model.SubUnit;
import model.Quality;

public class Import_noteDAO extends DBContext {

    public Connection getConnection() {
        return this.connection;
    }

    public List<Import_note> getAllImportNotes(List<Integer> userIds, Boolean importedFilter, String sortOrder) {
        List<Import_note> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM import_note WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (userIds != null && !userIds.isEmpty()) {
            sql.append(" AND user_id IN (").append(String.join(",", userIds.stream().map(id -> "?").toArray(String[]::new))).append(")");
            params.addAll(userIds);
        }
        if (importedFilter != null) {
            sql.append(" AND imported = ?");
            params.add(importedFilter);
        }
        if (sortOrder != null && !sortOrder.isEmpty()) {
            sql.append(" ORDER BY created_at ").append(sortOrder.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Import_note(
                        rs.getInt("import_note_id"),
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("warehouse_id"),
                        rs.getDate("created_at"),
                        rs.getBoolean("imported"),
                        rs.getDate("imported_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getAllImportNotes error: " + e.getMessage());
        }
        return list;
    }

    public List<Integer> getUserIdsByUsername(String searchUsername) {
        List<Integer> userIds = new ArrayList<>();
        if (searchUsername == null || searchUsername.trim().isEmpty()) {
            return userIds;
        }
        String sql = "SELECT user_id FROM Users WHERE full_name LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + searchUsername + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            System.out.println("getUserIdsByUsername error: " + e.getMessage());
        }
        return userIds;
    }

    public Import_note getImportNoteById(int importNoteId) {
        String sql = "SELECT * FROM import_note WHERE import_note_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importNoteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Import_note(
                        rs.getInt("import_note_id"),
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("warehouse_id"),
                        rs.getDate("created_at"),
                        rs.getBoolean("imported"),
                        rs.getDate("imported_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("getImportNoteById error: " + e.getMessage());
        }
        return null;
    }

    public List<Import_note_detail> getImportNoteDetailsByImportNoteId(int importNoteId) {
        List<Import_note_detail> list = new ArrayList<>();
        String sql = "SELECT * FROM import_note_detail WHERE import_note_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importNoteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Import_note_detail ind = new Import_note_detail();
                ind.setImportNoteDetailId(rs.getInt("import_note_detail_id"));
                ind.setImportNoteId(rs.getInt("import_note_id"));
                ind.setMaterialId(rs.getInt("material_id"));
                ind.setSubUnitId(rs.getInt("subunit_id"));
                ind.setQuantity(rs.getDouble("quantity"));
                ind.setQualityId(rs.getInt("quality_id"));
                ind.setImported(rs.getBoolean("imported"));
                list.add(ind);
            }
        } catch (SQLException e) {
            System.out.println("getImportNoteDetailsByImportNoteId error: " + e.getMessage());
        }
        return list;
    }

    public List<Material> getAllMaterials() {
        return new MaterialDAO().getAllMaterials(null, null, null);
    }

    public List<SubUnit> getAllSubUnits() {
        return new SubUnitDAO().getAllSubUnits("active");
    }

    public List<Quality> getAllQualities() {
        return new QualityDAO().getAllQualities();
    }

    public Import_note_detail getUnimportedDetail(int detailId) throws SQLException {
        String sql = "SELECT * FROM import_note_detail WHERE import_note_detail_id = ? AND imported = false";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detailId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Import_note_detail ind = new Import_note_detail();
                ind.setImportNoteDetailId(rs.getInt("import_note_detail_id"));
                ind.setImportNoteId(rs.getInt("import_note_id"));
                ind.setMaterialId(rs.getInt("material_id"));
                ind.setSubUnitId(rs.getInt("subunit_id"));
                ind.setQuantity(rs.getDouble("quantity"));
                ind.setQualityId(rs.getInt("quality_id"));
                ind.setImported(rs.getBoolean("imported"));
                return ind;
            }
        }
        return null;
    }

    public Integer findMaterialDetailId(int materialId, int subUnitId, int qualityId) throws SQLException {
        String sql = "SELECT material_detail_id FROM material_detail WHERE material_id = ? AND subunit_id = ? AND quality_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setInt(2, subUnitId);
            ps.setInt(3, qualityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("material_detail_id");
            }
        }
        return null;
    }

    public double getCurrentQuantity(int materialDetailId) throws SQLException {
        String sql = "SELECT quantity FROM material_detail WHERE material_detail_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialDetailId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("quantity");
            }
        }
        return 0;
    }

    public void updateMaterialDetail(int mdId, double newQty) throws SQLException {
        String sql = "UPDATE material_detail SET quantity = ?, last_updated = CURRENT_TIMESTAMP WHERE material_detail_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, newQty);
            ps.setInt(2, mdId);
            ps.executeUpdate();
        }
    }

    public void insertMaterialDetail(int materialId, int subUnitId, int qualityId, double qty) throws SQLException {
        String sql = "INSERT INTO material_detail (material_id, subunit_id, quality_id, quantity, last_updated) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setInt(2, subUnitId);
            ps.setInt(3, qualityId);
            ps.setDouble(4, qty);
            ps.executeUpdate();
        }
    }

    public void markDetailImported(int detailId) throws SQLException {
        String sql = "UPDATE import_note_detail SET imported = true WHERE import_note_detail_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, detailId);
            ps.executeUpdate();
        }
    }

    public boolean hasRemainingDetails(int importNoteId) throws SQLException {
        String sql = "SELECT 1 FROM import_note_detail WHERE import_note_id = ? AND imported = false LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importNoteId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void markNoteImported(int importNoteId) throws SQLException {
        String sql = "UPDATE import_note SET imported = true, imported_at = CURRENT_TIMESTAMP WHERE import_note_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importNoteId);
            ps.executeUpdate();
        }
    }

    public List<Material> getAllMaterial() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT Material_id, Name FROM materials WHERE Status = 'active'";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("Material_id"));
                m.setName(rs.getString("Name"));
                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertMaterialTransactionHistory(int materialDetailId, int importNoteDetailId, String note) throws SQLException {
        String sql = "INSERT INTO MaterialTransactionHistory (Material_detail_id, Import_note_detail_id, Transaction_date, Note) "
                + "VALUES (?, ?, CURDATE(), ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialDetailId);
            ps.setInt(2, importNoteDetailId);
            ps.setString(3, note);
            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật Import_qty trong InventoryMaterialDaily
     */ //Hàm của b Linh
    public void updateInventoryMaterialDaily(int materialId, int subUnitId, int qualityId, double quantity) throws SQLException {
        Integer materialDetailId = findMaterialDetailId(materialId, subUnitId, qualityId);
        if (materialDetailId == null) {
            System.out.println("Material_detail_id not found for materialId=" + materialId + ", subUnitId=" + subUnitId + ", qualityId=" + qualityId);
            return;
        }

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
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                openingQty = rs.getDouble("Opening_qty");
                currentImportQty = rs.getDouble("Import_qty");
                currentExportQty = rs.getDouble("Export_qty");
            }
        }

        if (openingQty != null) {
            String updateSql = """
            UPDATE InventoryMaterialDaily
            SET Import_qty = ?, Ending_qty = ?
            WHERE Material_detail_id = ? AND Inventory_Material_date = CURDATE()
        """;
            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setDouble(1, currentImportQty + quantity);
                ps.setDouble(2, openingQty + (currentImportQty + quantity) - currentExportQty);
                ps.setInt(3, materialDetailId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("No rows updated for materialDetailId: " + materialDetailId);
                }
            }
        } else {
            double materialDetailQty = getCurrentQuantity(materialDetailId);
            String insertSql = """
            INSERT INTO InventoryMaterialDaily (Material_detail_id, Inventory_Material_date, Opening_qty, Import_qty, Export_qty, Ending_qty)
            VALUES (?, CURDATE(), ?, ?, 0, ?)
        """;
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, materialDetailId);
                ps.setDouble(2, materialDetailQty);
                ps.setDouble(3, quantity);
                ps.setDouble(4, materialDetailQty + quantity);
                ps.executeUpdate();
            }
        }
    }

    public int getTodayImportNoteCount() {
        String sql = "SELECT COUNT(*) FROM import_note WHERE DATE(imported_at) = CURDATE() AND imported = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getImportNoteCountInDateRange(Date fromDate, Date toDate) {
        String sql = "SELECT COUNT(*) FROM import_note WHERE DATE(imported_at) BETWEEN ? AND ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
