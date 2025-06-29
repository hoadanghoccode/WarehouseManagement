/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author legia
 */
import model.PurchaseOrders;
import model.PurchaseOrderDetail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import model.Warehouse;

public class PurchaseOrderDAO extends DBContext {

    public List<PurchaseOrders> getAllPurchaseOrders(String usernameSearch, Integer warehouseId, Integer supplierId, String status, int page, int pageSize) {
        List<PurchaseOrders> list = new ArrayList<>();
        String query = "SELECT po.*, u.Full_name FROM PurchaseOrders po LEFT JOIN Users u ON po.User_id = u.User_id WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (usernameSearch != null && !usernameSearch.isEmpty()) {
            List<Integer> userIds = getUserIdsByUsername(usernameSearch);
            if (!userIds.isEmpty()) {
                query += " AND po.User_id IN (" + String.join(",", userIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
            } else {
                return list; 
            }
        }
        if (warehouseId != null) {
            query += " AND po.Warehouse_id = ?";
            params.add(warehouseId);
        }
        if (supplierId != null) {
            query += " AND po.Supplier_id = ?";
            params.add(supplierId);
        }
        if (status != null && !status.isEmpty()) {
            query += " AND po.Status = ?";
            params.add(status);
        }
        query += " ORDER BY po.Created_at DESC LIMIT ? OFFSET ?";
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PurchaseOrders po = new PurchaseOrders();
                po.setPurchaseOrderId(rs.getInt("PurchaseOrder_id"));
                po.setOrderId(rs.getInt("Order_id"));
                po.setWarehouseId(rs.getInt("Warehouse_id"));
                po.setUserId(rs.getInt("User_id"));
                po.setCreatedAt(rs.getDate("Created_at"));
                po.setStatus(rs.getString("Status"));
                po.setSupplierId(rs.getObject("Supplier_id") != null ? rs.getInt("Supplier_id") : null);
                po.setNote(rs.getString("Note"));
                po.setLastUpdated(rs.getDate("Last_updated"));
                po.setFullName(rs.getString("Full_name")); 
                list.add(po);
            }
        } catch (SQLException e) {
            System.out.println("getAllPurchaseOrders error: " + e.getMessage());
        }
        return list;
    }

    public int countPurchaseOrders(String usernameSearch, Integer warehouseId, Integer supplierId, String status) {
        String query = "SELECT COUNT(*) FROM PurchaseOrders po LEFT JOIN Users u ON po.User_id = u.User_id WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (usernameSearch != null && !usernameSearch.isEmpty()) {
            List<Integer> userIds = getUserIdsByUsername(usernameSearch);
            if (!userIds.isEmpty()) {
                query += " AND po.User_id IN (" + String.join(",", userIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
            } else {
                return 0; 
            }
        }
        if (warehouseId != null) {
            query += " AND po.Warehouse_id = ?";
            params.add(warehouseId);
        }
        if (supplierId != null) {
            query += " AND po.Supplier_id = ?";
            params.add(supplierId);
        }
        if (status != null && !status.isEmpty()) {
            query += " AND po.Status = ?";
            params.add(status);
        }

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("countPurchaseOrders error: " + e.getMessage());
        }
        return 0;
    }

    public List<Integer> getUserIdsByUsername(String searchUsername) {
        List<Integer> userIds = new ArrayList<>();
        if (searchUsername == null || searchUsername.trim().isEmpty()) {
            return userIds;
        }
        String sql = "SELECT User_id FROM Users WHERE Full_name LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + searchUsername + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIds.add(rs.getInt("User_id"));
            }
        } catch (SQLException e) {
            System.out.println("getUserIdsByUsername error: " + e.getMessage());
        }
        return userIds;
    }

    public PurchaseOrders getPurchaseOrderById(int purchaseOrderId) {
        String query = "SELECT po.*, u.Full_name FROM PurchaseOrders po LEFT JOIN Users u ON po.User_id = u.User_id WHERE PurchaseOrder_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, purchaseOrderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PurchaseOrders po = new PurchaseOrders();
                po.setPurchaseOrderId(rs.getInt("PurchaseOrder_id"));
                po.setOrderId(rs.getInt("Order_id"));
                po.setWarehouseId(rs.getInt("Warehouse_id"));
                po.setUserId(rs.getInt("User_id"));
                po.setCreatedAt(rs.getDate("Created_at"));
                po.setStatus(rs.getString("Status"));
                po.setSupplierId(rs.getObject("Supplier_id") != null ? rs.getInt("Supplier_id") : null);
                po.setNote(rs.getString("Note"));
                po.setLastUpdated(rs.getDate("Last_updated"));
                po.setFullName(rs.getString("Full_name")); // Add full_name
                po.setPurchaseOrderDetails(getPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId));
                return po;
            }
        } catch (SQLException e) {
            System.out.println("getPurchaseOrderById error: " + e.getMessage());
        }
        return null;
    }

    public List<PurchaseOrderDetail> getPurchaseOrderDetailsByPurchaseOrderId(int purchaseOrderId) {
        List<PurchaseOrderDetail> list = new ArrayList<>();
        String query = "SELECT * FROM PurchaseOrder_detail WHERE PurchaseOrder_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, purchaseOrderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PurchaseOrderDetail pod = new PurchaseOrderDetail();
                pod.setPurchaseOrderDetailId(rs.getInt("PurchaseOrder_detail_id"));
                pod.setPurchaseOrderId(rs.getInt("PurchaseOrder_id"));
                pod.setMaterialId(rs.getInt("Material_id"));
                pod.setSubUnitId(rs.getInt("SubUnit_id"));
                pod.setQualityId(rs.getObject("Quality_id") != null ? rs.getInt("Quality_id") : null);
                pod.setQuantity(rs.getDouble("Quantity"));
                pod.setPrice(rs.getObject("Price") != null ? rs.getDouble("Price") : null);
                list.add(pod);
            }
        } catch (SQLException e) {
            System.out.println("getPurchaseOrderDetailsByPurchaseOrderId error: " + e.getMessage());
        }
        return list;
    }

    public boolean updatePurchaseOrderStatus(int purchaseOrderId, String status) {
        String query = "UPDATE PurchaseOrders SET Status = ?, Last_updated = DEFAULT WHERE PurchaseOrder_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, purchaseOrderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("updatePurchaseOrderStatus error: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePurchaseOrder(PurchaseOrders po, List<PurchaseOrderDetail> details) {
        try {
            connection.setAutoCommit(false);

            String updatePOQuery = "UPDATE PurchaseOrders SET Supplier_id = ?, Note = ?, Status = 'pending', Last_updated = DEFAULT WHERE PurchaseOrder_id = ?";
            PreparedStatement psPO = connection.prepareStatement(updatePOQuery);
            if (po.getSupplierId() != null) {
                psPO.setInt(1, po.getSupplierId());
            } else {
                psPO.setNull(1, java.sql.Types.INTEGER);
            }
            psPO.setString(2, po.getNote());
            psPO.setInt(3, po.getPurchaseOrderId());
            int poUpdateResult = psPO.executeUpdate();

            if (poUpdateResult == 0) {
                connection.rollback();
                return false;
            }

            String updatePODQuery = "UPDATE PurchaseOrder_detail SET Price = ? WHERE PurchaseOrder_detail_id = ?";
            PreparedStatement psPOD = connection.prepareStatement(updatePODQuery);
            for (PurchaseOrderDetail detail : details) {
                if (detail.getPrice() != null) {
                    psPOD.setDouble(1, detail.getPrice());
                } else {
                    psPOD.setNull(1, java.sql.Types.DECIMAL);
                }
                psPOD.setInt(2, detail.getPurchaseOrderDetailId());
                psPOD.addBatch();
            }
            int[] detailResults = psPOD.executeBatch();
            for (int result : detailResults) {
                if (result <= 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("updatePurchaseOrder error: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public boolean updatePurchaseOrderToPurchased(int purchaseOrderId, int userId) {
        try {
            connection.setAutoCommit(false);

            PurchaseOrders po = getPurchaseOrderById(purchaseOrderId);
            if (po == null || !"approved".equals(po.getStatus())) {
                connection.rollback();
                return false;
            }

            String updatePOQuery = "UPDATE PurchaseOrders SET Status = 'purchased', Last_updated = DEFAULT WHERE PurchaseOrder_id = ?";
            PreparedStatement psPO = connection.prepareStatement(updatePOQuery);
            psPO.setInt(1, purchaseOrderId);
            int poUpdateResult = psPO.executeUpdate();
            if (poUpdateResult == 0) {
                connection.rollback();
                return false;
            }

            String insertImportNoteQuery = "INSERT INTO Import_note (Order_id, User_id, Warehouse_id, Created_at, Imported) VALUES (?, ?, ?, DEFAULT, ?)";
            PreparedStatement psImportNote = connection.prepareStatement(insertImportNoteQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            psImportNote.setInt(1, po.getOrderId());
            psImportNote.setInt(2, userId);
            psImportNote.setInt(3, po.getWarehouseId());
            psImportNote.setBoolean(4, false);
            int importNoteResult = psImportNote.executeUpdate();
            if (importNoteResult == 0) {
                connection.rollback();
                return false;
            }

            ResultSet generatedKeys = psImportNote.getGeneratedKeys();
            int importNoteId;
            if (generatedKeys.next()) {
                importNoteId = generatedKeys.getInt(1);
            } else {
                connection.rollback();
                return false;
            }

            List<PurchaseOrderDetail> details = getPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId);
            String insertImportNoteDetailQuery = "INSERT INTO Import_note_detail (Import_note_id, Material_id, SubUnit_id, Quality_id, Quantity, Imported) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psImportNoteDetail = connection.prepareStatement(insertImportNoteDetailQuery);
            for (PurchaseOrderDetail detail : details) {
                psImportNoteDetail.setInt(1, importNoteId);
                psImportNoteDetail.setInt(2, detail.getMaterialId());
                psImportNoteDetail.setInt(3, detail.getSubUnitId());
                if (detail.getQualityId() != null) {
                    psImportNoteDetail.setInt(4, detail.getQualityId());
                } else {
                    psImportNoteDetail.setNull(4, java.sql.Types.INTEGER);
                }
                psImportNoteDetail.setDouble(5, detail.getQuantity());
                psImportNoteDetail.setBoolean(6, false);
                psImportNoteDetail.addBatch();
            }
            int[] detailResults = psImportNoteDetail.executeBatch();
            for (int result : detailResults) {
                if (result <= 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("updatePurchaseOrderToPurchased error: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String query = "SELECT * FROM Suppliers WHERE Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("Supplier_id"));
                s.setName(rs.getString("Name"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("getAllSuppliers error: " + e.getMessage());
        }
        return list;
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String query = "SELECT * FROM Warehouse";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Warehouse w = new Warehouse();
                w.setWarehouseId(rs.getInt("Warehouse_id"));
                w.setName(rs.getString("Name"));
                w.setAddress(rs.getString("Address"));
                list.add(w);
            }
        } catch (SQLException e) {
            System.out.println("getAllWarehouses error: " + e.getMessage());
        }
        return list;
    }
}
