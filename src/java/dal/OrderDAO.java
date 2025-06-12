/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

    /**
     * Tạo mới một Order cùng với OrderDetails
     *
     * @param order Order object chứa thông tin order và list orderDetails
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createOrder(Order order) {
        String orderQuery = "INSERT INTO Orders (Warehouse_id, User_id, Type, Supplier, Status) VALUES (?, ?, ?, ?, ?)";
        String orderDetailQuery = "INSERT INTO Order_detail (Material_id, Order_id, Unit_id, Quantity, Unit_price) VALUES (?, ?, ?, ?, ?)";

        try {
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // 1. Insert Order và lấy Order_id vừa tạo
            PreparedStatement psOrder = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getWarehouseId());
            psOrder.setInt(2, order.getUserId());
            psOrder.setInt(3, order.getType());
            psOrder.setString(4, order.getSupplier());
            psOrder.setString(5, order.getStatus() != null ? order.getStatus() : "pending");

            int orderRowsAffected = psOrder.executeUpdate();

            if (orderRowsAffected > 0) {
                // Lấy Order_id vừa tạo
                ResultSet generatedKeys = psOrder.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    order.setOrderId(orderId); // Set orderId cho object

                    // 2. Insert OrderDetails
                    if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                        PreparedStatement psOrderDetail = connection.prepareStatement(orderDetailQuery);

                        for (OrderDetail detail : order.getOrderDetails()) {
                            psOrderDetail.setInt(1, detail.getMaterialId());
                            psOrderDetail.setInt(2, orderId);
                            psOrderDetail.setInt(3, detail.getUnitId());
                            psOrderDetail.setInt(4, detail.getQuantity());
                            psOrderDetail.setInt(5, detail.getUnitPrice());
                            psOrderDetail.addBatch(); // Thêm vào batch
                        }

                        int[] detailResults = psOrderDetail.executeBatch();

                        // Kiểm tra xem tất cả OrderDetail có được insert thành công không
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
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Reset về auto commit
            } catch (SQLException e) {
                System.out.println("Error resetting auto commit: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy Order theo ID
     *
     * @param orderId ID của order
     * @return Order object hoặc null nếu không tìm thấy
     */
    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM Orders WHERE Order_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("Order_id"));
                order.setWarehouseId(rs.getInt("Warehouse_id"));
                order.setUserId(rs.getInt("User_id"));
                order.setCreatedAt(rs.getTimestamp("Created_at"));
                order.setType(rs.getInt("Type"));
                order.setSupplier(rs.getString("Supplier"));
                order.setStatus(rs.getString("Status"));

                // Lấy OrderDetails
                order.setOrderDetails(getOrderDetailsByOrderId(orderId));

                return order;
            }
        } catch (SQLException e) {
            System.out.println("Error getting order by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách OrderDetail theo Order ID
     *
     * @param orderId ID của order
     * @return List OrderDetail
     */
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String query = "SELECT * FROM Order_detail WHERE Order_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setMaterialId(rs.getInt("Material_id"));
                detail.setOrderId(rs.getInt("Order_id"));
                detail.setUnitId(rs.getInt("Unit_id"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setUnitPrice(rs.getInt("Unit_price"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.out.println("Error getting order details: " + e.getMessage());
            e.printStackTrace();
        }

        return details;
    }

    /**
     * Lấy tất cả Orders
     *
     * @return List Order
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders ORDER BY Created_at DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("Order_id"));
                order.setWarehouseId(rs.getInt("Warehouse_id"));
                order.setUserId(rs.getInt("User_id"));
                order.setCreatedAt(rs.getTimestamp("Created_at"));
                order.setType(rs.getInt("Type"));
                order.setSupplier(rs.getString("Supplier"));
                order.setStatus(rs.getString("Status"));

                // Lấy OrderDetails cho mỗi order
                order.setOrderDetails(getOrderDetailsByOrderId(order.getOrderId()));

                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Cập nhật status của Order
     *
     * @param orderId ID của order
     * @param status Status mới
     * @return true nếu cập nhật thành công
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE Orders SET Status = ? WHERE Order_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa Order và OrderDetails
     *
     * @param orderId ID của order cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteOrder(int orderId) {
        String deleteOrderDetailsQuery = "DELETE FROM Order_detail WHERE Order_id = ?";
        String deleteOrderQuery = "DELETE FROM Orders WHERE Order_id = ?";

        try {
            connection.setAutoCommit(false);

            // 1. Xóa OrderDetails trước
            PreparedStatement psDetails = connection.prepareStatement(deleteOrderDetailsQuery);
            psDetails.setInt(1, orderId);
            psDetails.executeUpdate();

            // 2. Xóa Order
            PreparedStatement psOrder = connection.prepareStatement(deleteOrderQuery);
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
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error resetting auto commit: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy Orders theo type (0: nhập, 1: xuất)
     *
     * @param type Loại order
     * @return List Order
     */
    public List<Order> getOrdersByType(int type) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE Type = ? ORDER BY Created_at DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("Order_id"));
                order.setWarehouseId(rs.getInt("Warehouse_id"));
                order.setUserId(rs.getInt("User_id"));
                order.setCreatedAt(rs.getTimestamp("Created_at"));
                order.setType(rs.getInt("Type"));
                order.setSupplier(rs.getString("Supplier"));
                order.setStatus(rs.getString("Status"));

                order.setOrderDetails(getOrderDetailsByOrderId(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders by type: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Lấy Orders theo status
     *
     * @param status Status của order
     * @return List Order
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE Status = ? ORDER BY Created_at DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("Order_id"));
                order.setWarehouseId(rs.getInt("Warehouse_id"));
                order.setUserId(rs.getInt("User_id"));
                order.setCreatedAt(rs.getTimestamp("Created_at"));
                order.setType(rs.getInt("Type"));
                order.setSupplier(rs.getString("Supplier"));
                order.setStatus(rs.getString("Status"));

                order.setOrderDetails(getOrderDetailsByOrderId(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders by status: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Test method
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();

//        // Test tạo order mới
//        Order testOrder = new Order();
//        testOrder.setWarehouseId(1);
//        testOrder.setUserId(1);
//        testOrder.setType(0); // nhập
//        testOrder.setSupplier("Test Supplier");
//        testOrder.setStatus("pending");
//
//        // Tạo OrderDetails
//        List<OrderDetail> details = new ArrayList<>();
//        OrderDetail detail1 = new OrderDetail();
//        detail1.setMaterialId(1);
//        detail1.setUnitId(1);
//        detail1.setQuantity(10);
//        detail1.setUnitPrice(50000);
//        details.add(detail1);
//
//        OrderDetail detail2 = new OrderDetail();
//        detail2.setMaterialId(2);
//        detail2.setUnitId(1);
//        detail2.setQuantity(5);
//        detail2.setUnitPrice(30000);
//        details.add(detail2);
//
//        testOrder.setOrderDetails(details);
//
//        boolean result = dao.createOrder(testOrder);
//        System.out.println("Create order result: " + result);
//        if (result) {
//            System.out.println("Created order with ID: " + testOrder.getOrderId());
//        }
        System.out.println(dao.getOrderById(1));
        
    }
    
    
   
}
