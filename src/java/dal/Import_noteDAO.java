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
    
    public boolean importNoteDetailsToInventory(int importNoteId, List<Integer> detailIds) throws SQLException {
        // Chuyển sang transaction
        connection.setAutoCommit(false);
        try {
            // Lấy chi tiết import_note_detail chưa được import
            String selDetail = "SELECT material_id, subunit_id, quality_id, quantity "
                             + "FROM import_note_detail "
                             + "WHERE import_note_detail_id = ? AND imported = false";
            PreparedStatement psSelDetail = connection.prepareStatement(selDetail);
            
            // Kiểm tra xem đã có tồn tại dòng tồn kho tương ứng chưa
            String selMd = "SELECT material_detail_id, quantity "
                         + "FROM material_detail "
                         + "WHERE material_id = ? AND subunit_id = ? AND quality_id = ?";
            PreparedStatement psSelMd = connection.prepareStatement(selMd);
            
            // Cập nhật số lượng trong bảng material_detail
            String updMd = "UPDATE material_detail SET quantity = ?, last_updated = CURRENT_TIMESTAMP "
                         + "WHERE material_detail_id = ?";
            PreparedStatement psUpdMd = connection.prepareStatement(updMd);

            // Thêm dòng mới vào material_detail
            String insMd = "INSERT INTO material_detail (material_id, subunit_id, quality_id, quantity, last_updated) "
                         + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement psInsMd = connection.prepareStatement(insMd);
            
             // Đánh dấu imported = true cho import_note khi toàn bộ import_note_detail của nó được imported
            String updInd = "UPDATE import_note_detail SET imported = true WHERE import_note_detail_id = ?";
            PreparedStatement psUpdInd = connection.prepareStatement(updInd);

            for (int detailId : detailIds) {
                // Lấy thông tin detail
                psSelDetail.setInt(1, detailId);
                ResultSet rsDet = psSelDetail.executeQuery();
                // Nếu đã nhập rồi hoặc không tồn tại, bỏ qua
                if (!rsDet.next()) continue;  

                int mId = rsDet.getInt("material_id");
                int suId = rsDet.getInt("subunit_id");
                int qId = rsDet.getInt("quality_id");
                double qty = rsDet.getDouble("quantity");

                // Kiểm tra material_detail
                psSelMd.setInt(1, mId);
                psSelMd.setInt(2, suId);
                psSelMd.setInt(3, qId);
                ResultSet rsMd = psSelMd.executeQuery();
                if (rsMd.next()) {
                    int mdId = rsMd.getInt("material_detail_id");
                    double existQty = rsMd.getDouble("quantity");
                    psUpdMd.setDouble(1, existQty + qty);
                    psUpdMd.setInt(2, mdId);
                    psUpdMd.executeUpdate();
                } else {
                    psInsMd.setInt(1, mId);
                    psInsMd.setInt(2, suId);
                    psInsMd.setInt(3, qId);
                    psInsMd.setDouble(4, qty);
                    psInsMd.executeUpdate();
                }

                // Đánh dấu detail đã imported
                psUpdInd.setInt(1, detailId);
                psUpdInd.executeUpdate();
            }

            // Nếu tất cả detail đã imported thì cập nhật Import_note
            String chkRemain = "SELECT 1 FROM import_note_detail "
                             + "WHERE import_note_id = ? AND imported = false LIMIT 1";
                    PreparedStatement psChk = connection.prepareStatement(chkRemain);
                    psChk.setInt(1, importNoteId);
                    ResultSet rsChk = psChk.executeQuery();
                    // Nếu không còn chi tiết nào chưa nhập => đánh dấu cả phiếu là đã nhập
                    if (!rsChk.next()) {
                        String updNote = "UPDATE import_note SET imported = true, imported_at = CURRENT_TIMESTAMP "
                                       + "WHERE import_note_id = ?";
                        PreparedStatement psUpdNote = connection.prepareStatement(updNote);
                        psUpdNote.setInt(1, importNoteId);
                        psUpdNote.executeUpdate();
                    }

                    connection.commit();
                    return true;
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                } finally {
                    connection.setAutoCommit(true);
                }
            }
}