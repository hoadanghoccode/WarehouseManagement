package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import model.Order;
import model.OrderDetail;

/**
 * Data Access Object for Order operations
 *
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

    private Connection conn;

    public OrderDAO() {
        // Khởi tạo connection từ DBContext
        conn = new DBContext().getConnection();
    }

    public boolean createOrder(Order order) {
        String orderQuery = "INSERT INTO Orders (Warehouse_id, User_id, Type, Supplier_id, Note, Status) VALUES (?, ?, ?, ?, ?, ?)";
        String orderDetailQuery = "INSERT INTO Order_detail (Material_id, Order_id, Quality_id, SubUnit_id, Quantity) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement psOrder = null;
        PreparedStatement psOrderDetail = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Insert Order and get generated Order_id
            psOrder = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getWarehouseId());
            psOrder.setInt(2, order.getUserId());
            psOrder.setString(3, order.getType());

            // Handle null supplier_id
            if (order.getSupplier() > 0) {
                psOrder.setInt(4, order.getSupplier());
            } else {
                psOrder.setNull(4, java.sql.Types.INTEGER);
            }

            psOrder.setString(5, order.getNote());
            psOrder.setString(6, order.getStatus() != null ? order.getStatus() : "pending");

            int orderRowsAffected = psOrder.executeUpdate();

            if (orderRowsAffected > 0) {
                // Get generated Order_id
                ResultSet generatedKeys = psOrder.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    order.setOrderId(orderId);

                    // 2. Insert OrderDetails if exist
                    if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                        psOrderDetail = connection.prepareStatement(orderDetailQuery);

                        for (OrderDetail detail : order.getOrderDetails()) {
                            psOrderDetail.setInt(1, detail.getMaterialId());
                            psOrderDetail.setInt(2, orderId);

                            // Handle nullable Quality_id
                            if (detail.getQualityId() > 0) {
                                psOrderDetail.setInt(3, detail.getQualityId());
                            } else {
                                psOrderDetail.setNull(3, java.sql.Types.INTEGER);
                            }

                            psOrderDetail.setInt(4, detail.getSubUnitId());
                            psOrderDetail.setInt(5, detail.getQuantity());
                            psOrderDetail.addBatch();
                        }

                        int[] detailResults = psOrderDetail.executeBatch();

                        // Check if all OrderDetails were inserted successfully
                        for (int result : detailResults) {
                            if (result <= 0) {
                                connection.rollback();
                                return false;
                            }
                        }
                    }

                    // Commit transaction
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (psOrder != null) {
                    psOrder.close();
                }
                if (psOrderDetail != null) {
                    psOrderDetail.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM Orders WHERE Order_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("Order_id"));
                order.setWarehouseId(rs.getInt("Warehouse_id"));
                order.setUserId(rs.getInt("User_id"));
                order.setCreatedAt(rs.getTimestamp("Created_at"));
                order.setType(rs.getString("Type"));

                // Handle nullable Supplier_id
                int supplierId = rs.getInt("Supplier_id");
                if (!rs.wasNull()) {
                    order.setSupplier(supplierId);
                }

                order.setNote(rs.getString("Note"));
                order.setStatus(rs.getString("Status"));

                // Get OrderDetails
                order.setOrderDetails(getOrderDetailsByOrderId(orderId));

                return order;
            }
        } catch (SQLException e) {
            System.err.println("Error getting order by id: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }
        return null;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String query = "SELECT * FROM Order_detail WHERE Order_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(rs.getInt("Order_detail_id"));
                detail.setMaterialId(rs.getInt("Material_id"));
                detail.setOrderId(rs.getInt("Order_id"));

                // Handle nullable Quality_id
                int qualityId = rs.getInt("Quality_id");
                if (!rs.wasNull()) {
                    detail.setQualityId(qualityId);
                }

                detail.setSubUnitId(rs.getInt("SubUnit_id"));
                detail.setQuantity(rs.getInt("Quantity"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error getting order details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return details;
    }

    public List<Order> getAllOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.Full_name AS User_name, s.Name AS Supplier_name "
                + "FROM Orders o "
                + "LEFT JOIN Users u ON o.User_id = u.User_id "
                + "LEFT JOIN Suppliers s ON o.Supplier_id = s.Supplier_id "
                + "WHERE o.User_id = ? "
                + "ORDER BY o.Created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("Order_id"));
                o.setWarehouseId(rs.getInt("Warehouse_id"));
                o.setUserId(rs.getInt("User_id"));
                o.setCreatedAt(rs.getTimestamp("Created_at"));
                o.setType(rs.getString("Type"));
                o.setSupplier(rs.getInt("Supplier_id"));
                o.setNote(rs.getString("Note"));
                o.setStatus(rs.getString("Status"));
                o.setUserName(rs.getString("User_name"));
                o.setSupplierName(rs.getString("Supplier_name"));
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE Orders SET Status = ? WHERE Order_id = ?";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(ps, null);
        }
    }

    public boolean updateOrderNote(int orderId, String note) {
        String query = "UPDATE Orders SET Note = ? WHERE Order_id = ?";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, note);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order note: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(ps, null);
        }
    }

    public boolean deleteOrder(int orderId) {
        String deleteOrderDetailsQuery = "DELETE FROM Order_detail WHERE Order_id = ?";
        String deleteOrderQuery = "DELETE FROM Orders WHERE Order_id = ?";

        PreparedStatement psDetails = null;
        PreparedStatement psOrder = null;

        try {
            connection.setAutoCommit(false);

            // 1. Delete OrderDetails first (foreign key constraint)
            psDetails = connection.prepareStatement(deleteOrderDetailsQuery);
            psDetails.setInt(1, orderId);
            psDetails.executeUpdate();

            // 2. Delete Order
            psOrder = connection.prepareStatement(deleteOrderQuery);
            psOrder.setInt(1, orderId);
            int rowsAffected = psOrder.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psDetails != null) {
                    psDetails.close();
                }
                if (psOrder != null) {
                    psOrder.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to create Order object from ResultSet
     *
     * @param rs ResultSet
     * @return Order object
     * @throws SQLException
     */
    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("Order_id"));
        order.setWarehouseId(rs.getInt("Warehouse_id"));
        order.setUserId(rs.getInt("User_id"));
        order.setCreatedAt(rs.getTimestamp("Created_at"));
        order.setType(rs.getString("Type"));

        // Handle nullable Supplier_id
        int supplierId = rs.getInt("Supplier_id");
        if (!rs.wasNull()) {
            order.setSupplier(supplierId);
        }

        order.setNote(rs.getString("Note"));
        order.setStatus(rs.getString("Status"));

        // Get OrderDetails for each order
        order.setOrderDetails(getOrderDetailsByOrderId(order.getOrderId()));

        return order;
    }

    /**
     * Helper method to close database resources
     *
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    private void closeResources(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    /**
     * Get Orders by date range
     *
     * @param startDate Start date
     * @param endDate End date
     * @return List of Order
     */
    public List<Order> getOrdersByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE Created_at BETWEEN ? AND ? ORDER BY Created_at DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setTimestamp(1, startDate);
            ps.setTimestamp(2, endDate);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders by date range: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return orders;
    }

    public List<Order> getPagedOrderList(String search, String type, String status, Date fromDate, Date toDate,
            int offset, int limit, String sortColumn, String sortDirection) {
        List<Order> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, u.Full_name AS User_name, s.Name AS Supplier_name "
                + "FROM Orders o "
                + "LEFT JOIN Users u ON o.User_id = u.User_id "
                + "LEFT JOIN Suppliers s ON o.Supplier_id = s.Supplier_id WHERE 1=1 ");

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (o.Note LIKE ? OR o.Type LIKE ? OR u.Full_name LIKE ? OR s.Name LIKE ?) ");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND o.Type = ? ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.Status = ? ");
        }
        if (fromDate != null) {
            sql.append(" AND DATE(o.Created_at) >= ? ");
        }
        if (toDate != null) {
            sql.append(" AND DATE(o.Created_at) <= ? ");
        }

        sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sortDirection).append(" LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (search != null && !search.isEmpty()) {
                for (int i = 0; i < 4; i++) {
                    ps.setString(index++, "%" + search + "%");
                }
            }
            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(index++, status);
            }
            if (fromDate != null) {
                ps.setDate(index++, fromDate);
            }
            if (toDate != null) {
                ps.setDate(index++, toDate);
            }
            ps.setInt(index++, limit);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("Order_id"));
                o.setWarehouseId(rs.getInt("Warehouse_id"));
                o.setUserId(rs.getInt("User_id"));
                o.setCreatedAt(rs.getTimestamp("Created_at"));
                o.setType(rs.getString("Type"));
                o.setSupplier(rs.getInt("Supplier_id"));
                o.setNote(rs.getString("Note"));
                o.setStatus(rs.getString("Status"));
                o.setUserName(rs.getString("User_name"));
                o.setSupplierName(rs.getString("Supplier_name"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countOrdersWithFilter(String search, String type, String status, Date fromDate, Date toDate) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Orders o "
                + "LEFT JOIN Users u ON o.User_id = u.User_id "
                + "LEFT JOIN Suppliers s ON o.Supplier_id = s.Supplier_id WHERE 1=1 ");

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (o.Note LIKE ? OR o.Type LIKE ? OR u.Full_name LIKE ? OR s.Name LIKE ?) ");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND o.Type = ? ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND o.Status = ? ");
        }
        if (fromDate != null) {
            sql.append(" AND DATE(o.Created_at) >= ? ");
        }
        if (toDate != null) {
            sql.append(" AND DATE(o.Created_at) <= ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (search != null && !search.isEmpty()) {
                for (int i = 0; i < 4; i++) {
                    ps.setString(index++, "%" + search + "%");
                }
            }
            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(index++, status);
            }
            if (fromDate != null) {
                ps.setDate(index++, fromDate);
            }
            if (toDate != null) {
                ps.setDate(index++, toDate);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (Warehouse_id, User_id, Type, Note, Status, Created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getWarehouseId());
            ps.setInt(2, order.getUserId());
            ps.setString(3, order.getType());
            ps.setString(4, order.getNote());
            ps.setString(5, order.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về id vừa insert
            }
        }
        return -1; // Không thành công
    }

    public void insertOrderDetail(OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO Order_detail (Material_id, Order_id, Quality_id, SubUnit_id, Quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getMaterialId());
            ps.setInt(2, detail.getOrderId());
            ps.setInt(3, detail.getQualityId());
            ps.setInt(4, detail.getSubUnitId());
            ps.setDouble(5, detail.getQuantity());
            ps.executeUpdate();
        }
    }

    public boolean updateOrder(Order order) {
        String updateOrderSQL = "UPDATE Orders SET Type = ?, Supplier_id = ?, Note = ?, Status = ? WHERE Order_id = ?";
        String deleteDetailsSQL = "DELETE FROM Order_detail WHERE Order_id = ?";
        String insertDetailSQL = "INSERT INTO Order_detail (Material_id, Order_id, Quality_id, SubUnit_id, Quantity) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement psOrder = null;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Update main order table
            psOrder = connection.prepareStatement(updateOrderSQL);
            psOrder.setString(1, order.getType());

            // Handle nullable Supplier_id
            if (order.getSupplier() > 0) {
                psOrder.setInt(2, order.getSupplier());
            } else {
                psOrder.setNull(2, java.sql.Types.INTEGER);
            }

            psOrder.setString(3, order.getNote());
            psOrder.setString(4, order.getStatus() != null ? order.getStatus() : "pending");
            psOrder.setInt(5, order.getOrderId());

            int orderUpdateResult = psOrder.executeUpdate();

            if (orderUpdateResult == 0) {
                System.err.println("No order found with ID: " + order.getOrderId());
                connection.rollback();
                return false;
            }

            // 2. Delete existing order details
            psDelete = connection.prepareStatement(deleteDetailsSQL);
            psDelete.setInt(1, order.getOrderId());
            psDelete.executeUpdate();

            // 3. Insert new order details
            if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                psInsert = connection.prepareStatement(insertDetailSQL);

                for (OrderDetail detail : order.getOrderDetails()) {
                    psInsert.setInt(1, detail.getMaterialId());
                    psInsert.setInt(2, order.getOrderId());

                    // Handle nullable Quality_id
                    if (detail.getQualityId() > 0) {
                        psInsert.setInt(3, detail.getQualityId());
                    } else {
                        psInsert.setNull(3, java.sql.Types.INTEGER);
                    }

                    psInsert.setInt(4, detail.getSubUnitId());
                    psInsert.setInt(5, detail.getQuantity());
                    psInsert.addBatch();
                }

                int[] batchResults = psInsert.executeBatch();

                // Check if all inserts were successful
                for (int result : batchResults) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        System.err.println("Failed to insert order detail");
                        connection.rollback();
                        return false;
                    }
                }
            }

            // Commit transaction
            connection.commit();
            System.out.println("Order updated successfully: " + order.getOrderId());
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources properly
            try {
                if (psOrder != null) {
                    psOrder.close();
                }
                if (psDelete != null) {
                    psDelete.close();
                }
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                    // DON'T close connection here - it should be managed by DBContext
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Approve order and create import note for import type orders
     *
     * @param orderId Order ID to approve
     * @param userId User ID who approves the order
     * @return true if successful, false otherwise
     */
    public boolean approveOrderAndCreateImportNote(int orderId, int userId) {
        PreparedStatement psUpdateOrder = null;
        PreparedStatement psInsertImportNote = null;
        PreparedStatement psInsertImportDetail = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Get order details first
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("Order not found with ID: " + orderId);
                connection.rollback();
                return false;
            }

            // 2. Update order status to approved
            String updateOrderQuery = "UPDATE Orders SET Status = 'approved' WHERE Order_id = ?";
            psUpdateOrder = connection.prepareStatement(updateOrderQuery);
            psUpdateOrder.setInt(1, orderId);

            int orderUpdateResult = psUpdateOrder.executeUpdate();
            if (orderUpdateResult == 0) {
                System.err.println("Failed to update order status");
                connection.rollback();
                return false;
            }

            // 3. Create import note only for import type orders
            if ("import".equals(order.getType())) {
                String insertImportNoteQuery = "INSERT INTO Import_note (Order_id, User_id, Warehouse_id) VALUES (?, ?, ?)";
                psInsertImportNote = connection.prepareStatement(insertImportNoteQuery, Statement.RETURN_GENERATED_KEYS);
                psInsertImportNote.setInt(1, orderId);
                psInsertImportNote.setInt(2, userId);
                psInsertImportNote.setInt(3, order.getWarehouseId());

                int importNoteResult = psInsertImportNote.executeUpdate();
                if (importNoteResult > 0) {
                    // Get generated Import_note_id
                    ResultSet generatedKeys = psInsertImportNote.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int importNoteId = generatedKeys.getInt(1);

                        // 4. Create import note details
                        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                            String insertImportDetailQuery = "INSERT INTO Import_note_detail (Import_note_id, Material_id, SubUnit_id, Quantity, Quality_id) VALUES (?, ?, ?, ?, ?)";
                            psInsertImportDetail = connection.prepareStatement(insertImportDetailQuery);

                            for (OrderDetail detail : order.getOrderDetails()) {
                                psInsertImportDetail.setInt(1, importNoteId);
                                psInsertImportDetail.setInt(2, detail.getMaterialId());
                                psInsertImportDetail.setInt(3, detail.getSubUnitId());
                                psInsertImportDetail.setInt(4, detail.getQuantity());

                                // Handle Quality_id - use default quality if not specified
                                if (detail.getQualityId() > 0) {
                                    psInsertImportDetail.setInt(5, detail.getQualityId());
                                } else {
                                    // You might want to set a default quality ID or handle this differently
                                    psInsertImportDetail.setInt(5, 1); // Assuming 1 is default quality
                                }

                                psInsertImportDetail.addBatch();
                            }

                            int[] detailResults = psInsertImportDetail.executeBatch();

                            // Check if all import details were created successfully
                            for (int result : detailResults) {
                                if (result <= 0) {
                                    System.err.println("Failed to create import note detail");
                                    connection.rollback();
                                    return false;
                                }
                            }
                        }

                        System.out.println("Import note created successfully with ID: " + importNoteId);
                    } else {
                        System.err.println("Failed to get generated import note ID");
                        connection.rollback();
                        return false;
                    }
                } else {
                    System.err.println("Failed to create import note");
                    connection.rollback();
                    return false;
                }
            }

            // Commit transaction
            connection.commit();
            System.out.println("Order approved successfully: " + orderId);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error approving order and creating import note: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (psUpdateOrder != null) {
                    psUpdateOrder.close();
                }
                if (psInsertImportNote != null) {
                    psInsertImportNote.close();
                }
                if (psInsertImportDetail != null) {
                    psInsertImportDetail.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

    public boolean approveOrderAndCreateExportNote(int orderId, int userId) {
        PreparedStatement psUpdateOrder = null;
        PreparedStatement psInsertExportNote = null;
        PreparedStatement psInsertExportDetail = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Get order details first
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("Order not found with ID: " + orderId);
                connection.rollback();
                return false;
            }

            // 2. Update order status to approved
            String updateOrderQuery = "UPDATE Orders SET Status = 'approved' WHERE Order_id = ?";
            psUpdateOrder = connection.prepareStatement(updateOrderQuery);
            psUpdateOrder.setInt(1, orderId);

            int orderUpdateResult = psUpdateOrder.executeUpdate();
            if (orderUpdateResult == 0) {
                System.err.println("Failed to update order status");
                connection.rollback();
                return false;
            }

            // 3. Create export note for export or exportToRepair type orders
            if ("export".equals(order.getType()) || "exportToRepair".equals(order.getType())) {
                String insertExportNoteQuery = "INSERT INTO Export_note (Order_id, User_id, Warehouse_id) VALUES (?, ?, ?)";
                psInsertExportNote = connection.prepareStatement(insertExportNoteQuery, Statement.RETURN_GENERATED_KEYS);
                psInsertExportNote.setInt(1, orderId);
                psInsertExportNote.setInt(2, userId);
                psInsertExportNote.setInt(3, order.getWarehouseId());

                int exportNoteResult = psInsertExportNote.executeUpdate();
                if (exportNoteResult > 0) {
                    // Get generated Export_note_id
                    ResultSet generatedKeys = psInsertExportNote.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int exportNoteId = generatedKeys.getInt(1);

                        // 4. Create export note details
                        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                            String insertExportDetailQuery = "INSERT INTO Export_note_detail (Export_note_id, Material_id, SubUnit_id, Quantity, Quality_id) VALUES (?, ?, ?, ?, ?)";
                            psInsertExportDetail = connection.prepareStatement(insertExportDetailQuery);

                            for (OrderDetail detail : order.getOrderDetails()) {
                                psInsertExportDetail.setInt(1, exportNoteId);
                                psInsertExportDetail.setInt(2, detail.getMaterialId());
                                psInsertExportDetail.setInt(3, detail.getSubUnitId());
                                psInsertExportDetail.setInt(4, detail.getQuantity());

                                // Handle Quality_id - use default quality if not specified
                                if (detail.getQualityId() > 0) {
                                    psInsertExportDetail.setInt(5, detail.getQualityId());
                                } else {
                                    // You might want to set a default quality ID or handle this differently
                                    psInsertExportDetail.setInt(5, 1); // Assuming 1 is default quality
                                }

                                psInsertExportDetail.addBatch();
                            }

                            int[] detailResults = psInsertExportDetail.executeBatch();

                            // Check if all export details were created successfully
                            for (int result : detailResults) {
                                if (result <= 0) {
                                    System.err.println("Failed to create export note detail");
                                    connection.rollback();
                                    return false;
                                }
                            }
                        }

                        System.out.println("Export note created successfully with ID: " + exportNoteId);
                    } else {
                        System.err.println("Failed to get generated export note ID");
                        connection.rollback();
                        return false;
                    }
                } else {
                    System.err.println("Failed to create export note");
                    connection.rollback();
                    return false;
                }
            }

            // Commit transaction
            connection.commit();
            System.out.println("Order approved successfully: " + orderId);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error approving order and creating export note: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (psUpdateOrder != null) {
                    psUpdateOrder.close();
                }
                if (psInsertExportNote != null) {
                    psInsertExportNote.close();
                }
                if (psInsertExportDetail != null) {
                    psInsertExportDetail.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

//    Xử lý purchase của b Giang
    public boolean approveOrderAndCreatePurchaseNote(int orderId, int userId) {
        PreparedStatement psUpdateOrder = null;
        PreparedStatement psInsertPurchaseNote = null;
        PreparedStatement psInsertPurchaseDetail = null;

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Get order details first
            Order order = getOrderById(orderId);
            if (order == null) {
                System.err.println("Order not found with ID: " + orderId);
                connection.rollback();
                return false;
            }

            // 2. Update order status to approved
            String updateOrderQuery = "UPDATE Orders SET Status = 'approved' WHERE Order_id = ?";
            psUpdateOrder = connection.prepareStatement(updateOrderQuery);
            psUpdateOrder.setInt(1, orderId);

            int orderUpdateResult = psUpdateOrder.executeUpdate();
            if (orderUpdateResult == 0) {
                System.err.println("Failed to update order status");
                connection.rollback();
                return false;
            }

            // 3. Create purchase note only for purchase type orders
            if ("purchase".equals(order.getType())) {
                String insertPurchaseNoteQuery = "INSERT INTO PurchaseOrders (Order_id, User_id, Warehouse_id, Status) VALUES (?, ?, ?, 'new')";
                psInsertPurchaseNote = connection.prepareStatement(insertPurchaseNoteQuery, Statement.RETURN_GENERATED_KEYS);
                psInsertPurchaseNote.setInt(1, orderId);
                psInsertPurchaseNote.setInt(2, userId);
                psInsertPurchaseNote.setInt(3, order.getWarehouseId());

                int purchaseNoteResult = psInsertPurchaseNote.executeUpdate();
                if (purchaseNoteResult > 0) {
                    // Get generated Purchase_note_id
                    ResultSet generatedKeys = psInsertPurchaseNote.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int purchaseNoteId = generatedKeys.getInt(1);

                        // 4. Create purchase note details
                        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                            String insertPurchaseDetailQuery = "INSERT INTO PurchaseOrder_detail (PurchaseOrder_id, Material_id, SubUnit_id, Quantity, Quality_id) VALUES (?, ?, ?, ?, ?)";
                            psInsertPurchaseDetail = connection.prepareStatement(insertPurchaseDetailQuery);

                            for (OrderDetail detail : order.getOrderDetails()) {
                                psInsertPurchaseDetail.setInt(1, purchaseNoteId);
                                psInsertPurchaseDetail.setInt(2, detail.getMaterialId());
                                psInsertPurchaseDetail.setInt(3, detail.getSubUnitId());
                                psInsertPurchaseDetail.setInt(4, detail.getQuantity());

                                // Handle Quality_id - use default quality if not specified
                                if (detail.getQualityId() > 0) {
                                    psInsertPurchaseDetail.setInt(5, detail.getQualityId());
                                } else {
                                    psInsertPurchaseDetail.setInt(5, 1); // Assuming 1 is default quality
                                }

                                psInsertPurchaseDetail.addBatch();
                            }

                            int[] detailResults = psInsertPurchaseDetail.executeBatch();

                            // Check if all purchase details were created successfully
                            for (int result : detailResults) {
                                if (result <= 0) {
                                    System.err.println("Failed to create purchase note detail");
                                    connection.rollback();
                                    return false;
                                }
                            }
                        }

                        System.out.println("Purchase note created successfully with ID: " + purchaseNoteId);
                    } else {
                        System.err.println("Failed to get generated purchase note ID");
                        connection.rollback();
                        return false;
                    }
                } else {
                    System.err.println("Failed to create purchase note");
                    connection.rollback();
                    return false;
                }
            }

            // Commit transaction
            connection.commit();
            System.out.println("Order approved successfully: " + orderId);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error approving order and creating purchase note: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (psUpdateOrder != null) {
                    psUpdateOrder.close();
                }
                if (psInsertPurchaseNote != null) {
                    psInsertPurchaseNote.close();
                }
                if (psInsertPurchaseDetail != null) {
                    psInsertPurchaseDetail.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

    // Test method
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();

        // Test get order by ID
        Order order = dao.getOrderById(1);
        if (order != null) {
            System.out.println("Found order: " + dao.getOrderById(1));
        } else {
            System.out.println("Order not found");
        }

        // Test get all orders
        List<Order> ordersByUser = dao.getAllOrdersByUserId(4);
        System.out.println("Orders by user 1: " + ordersByUser.size());

    }
}
