package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Import_note;
import model.Import_note_detail;
import model.Material;
import model.SubUnit;
import model.Quality;

public class Import_noteDAO extends DBContext {

    // Get all import notes with filter by imported and sort by created_at
    public List<Import_note> getAllImportNotes(Boolean importedFilter, String sortOrder) {
        List<Import_note> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM import_note");
        if (importedFilter != null) {
            sql.append(" WHERE imported = ?");
        }
        if (sortOrder != null && !sortOrder.isEmpty()) {
            sql.append(" ORDER BY created_at ")
               .append(sortOrder.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (importedFilter != null) {
                ps.setBoolean(1, importedFilter);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Import_note in = new Import_note(
                    rs.getInt("import_note_id"),
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getInt("warehouse_id"),
                    rs.getDate("created_at"),
                    rs.getBoolean("imported"),
                    rs.getDate("imported_at")
                );
                list.add(in);
            }
        } catch (SQLException e) {
            System.out.println("getAllImportNotes error: " + e.getMessage());
        }
        return list;
    }

    // Get import note by ID
    public Import_note getImportNoteById(int importNoteId) {
        String query = "SELECT * FROM Import_note WHERE Import_note_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, importNoteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Import_note in = new Import_note();
                in.setImportNoteId(rs.getInt("Import_note_id"));
                in.setOrderId(rs.getInt("Order_id"));
                in.setUserId(rs.getInt("User_id"));
                in.setWarehouseId(rs.getInt("Warehouse_id"));
                in.setCreatedAt(rs.getDate("Created_at"));
                in.setImported(rs.getBoolean("Imported"));
                in.setImportedAt(rs.getDate("Imported_at"));
                return in;
            }
        } catch (SQLException e) {
            System.out.println("getImportNoteById error: " + e.getMessage());
        }
        return null;
    }

    // Get import note details by import note ID
    public List<Import_note_detail> getImportNoteDetailsByImportNoteId(int importNoteId) {
        List<Import_note_detail> list = new ArrayList<>();
        String query = "SELECT * FROM Import_note_detail WHERE Import_note_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, importNoteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Import_note_detail ind = new Import_note_detail();
                ind.setImportNoteDetailId(rs.getInt("Import_note_detail_id"));
                ind.setImportNoteId(rs.getInt("Import_note_id"));
                ind.setMaterialId(rs.getInt("Material_id"));
                ind.setSubUnitId(rs.getInt("SubUnit_id"));
                ind.setQuantity(rs.getDouble("Quantity"));
                ind.setQualityId(rs.getInt("Quality_id"));
                ind.setImported(rs.getBoolean("Imported"));
                list.add(ind);
            }
        } catch (SQLException e) {
            System.out.println("getImportNoteDetailsByImportNoteId error: " + e.getMessage());
        }
        return list;
    }

    // Get all materials
    public List<Material> getAllMaterials() {
        MaterialDAO mDao = new MaterialDAO();
        return mDao.getAllMaterials(null, null, null, null); // No filters
    }

    // Get all sub units
    public List<SubUnit> getAllSubUnits() {
        SubUnitDAO suDao = new SubUnitDAO();
        return suDao.getAllSubUnits(); // Assume SubUnitDAO has this method
    }

    // Get all qualities
    public List<Quality> getAllQualities() {
        QualityDAO qDao = new QualityDAO();
        return qDao.getAllQualities(); // Assume QualityDAO has this method
    }
}