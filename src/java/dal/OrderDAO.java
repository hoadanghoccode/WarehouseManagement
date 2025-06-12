package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderDetail;

/**
 * Data Access Object for Order operations
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

   
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
                if (psOrder != null) psOrder.close();
                if (psOrderDetail != null) psOrderDetail.close();
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

    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders ORDER BY Created_at DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
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
                if (psDetails != null) psDetails.close();
                if (psOrder != null) psOrder.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error in finally block: " + e.getMessage());
            }
        }
    }

   
    public List<Order> getOrdersByType(String type) {
        return getOrdersByCondition("Type = ?", type);
    }

  
    public List<Order> getOrdersByStatus(String status) {
        return getOrdersByCondition("Status = ?", status);
    }

   
    public List<Order> getOrdersByUserId(int userId) {
        return getOrdersByCondition("User_id = ?", userId);
    }

  
    public List<Order> getOrdersBySupplierId(int supplierId) {
        return getOrdersByCondition("Supplier_id = ?", supplierId);
    }

 
    private List<Order> getOrdersByCondition(String condition, Object parameter) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE " + condition + " ORDER BY Created_at DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            
            if (parameter instanceof String) {
                ps.setString(1, (String) parameter);
            } else if (parameter instanceof Integer) {
                ps.setInt(1, (Integer) parameter);
            }
            
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders by condition: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return orders;
    }

    /**
     * Helper method to create Order object from ResultSet
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
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    private void closeResources(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    /**
     * Get Orders by date range
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

    /**
     * Count orders by status
     * @param status Order status
     * @return Number of orders
     */
    public int countOrdersByStatus(String status) {
        String query = "SELECT COUNT(*) FROM Orders WHERE Status = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting orders by status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return 0;
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
        List<Order> allOrders = dao.getAllOrders();
        System.out.println("Total orders: " + dao.getAllOrders());
    }
}