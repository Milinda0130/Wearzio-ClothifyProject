package dao.custom.impl;

import dao.custom.OrderDao;
import db.DBConnection;
import entity.OrderDetailsEntity;
import entity.OrderEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static OrderDaoImpl orderDaoImpl;

    public static OrderDaoImpl getInstance() {
        if (orderDaoImpl == null) {
            orderDaoImpl = new OrderDaoImpl();
        }
        return orderDaoImpl;
    }

    @Override
    public int getLastOrderId() {
        String query = "SELECT id FROM `order` ORDER BY id DESC LIMIT 1";
        try (
                Statement stmt = DBConnection.getInstance().getConnection().createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching last iD", e);
        }
        return -1;
    }

    @Override
    public boolean save(OrderEntity entity) {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Insert order
            String orderQuery = "INSERT INTO `order` (id, date, price, paymentMethod, userId, customerId) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery)) {
                orderStmt.setInt(1, entity.getId());
                orderStmt.setDate(2, entity.getDate());
                orderStmt.setDouble(3, entity.getPrice());
                orderStmt.setString(4, entity.getPaymentMethod());
                orderStmt.setInt(5, entity.getUserId());
                orderStmt.setInt(6, entity.getCustomerId());

                int orderResult = orderStmt.executeUpdate();
                if (orderResult == 0) {
                    connection.rollback();
                    return false;
                }
            }

            if (entity.getOrderDetailsList() != null && !entity.getOrderDetailsList().isEmpty()) {
                String orderDetailsQuery = "INSERT INTO orderdetails (orderId, productId, quantity) VALUES (?, ?, ?)";

                for (OrderDetailsEntity orderDetail : entity.getOrderDetailsList()) {
                    try (PreparedStatement detailStmt = connection.prepareStatement(orderDetailsQuery)) {
                        detailStmt.setInt(1, entity.getId()); // orderId
                        detailStmt.setInt(2, orderDetail.getProductId()); // ✅ productId
                        detailStmt.setInt(3, orderDetail.getQuantity()); // ✅ quantity

                        int detailResult = detailStmt.executeUpdate();
                        if (detailResult == 0) {
                            connection.rollback();
                            return false;
                        }
                    }






                // Update product quantity
                    String updateProductQuery = "UPDATE product SET quantityOnHand = quantityOnHand - ? WHERE id = ?";
                    try (PreparedStatement productStmt = connection.prepareStatement(updateProductQuery)) {
                        productStmt.setInt(1, orderDetail.getQuantity());
                        productStmt.setInt(2, orderDetail.getProductId());

                        int productResult = productStmt.executeUpdate();
                        if (productResult == 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                }
            }

            connection.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error saving order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    @Override
    public OrderEntity search(String id) {
        String query = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToOrderEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching order", e);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM `order` WHERE id = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }    }

    @Override
    public boolean update(OrderEntity entity) {
        String query = "UPDATE `order` SET date = ?, price = ?, paymentMethod = ?, userId = ?, customerId = ? WHERE id = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setDate(1, entity.getDate());
            statement.setDouble(2, entity.getPrice());
            statement.setString(3, entity.getPaymentMethod());
            statement.setInt(4, entity.getUserId());
            statement.setInt(5, entity.getCustomerId());
            statement.setInt(6, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    @Override
    public List<OrderEntity> getAll() {
        String query = "SELECT id, date, price, paymentMethod, userId, customerId FROM `order`";
        List<OrderEntity> orders = new ArrayList<>();

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                orders.add(mapResultSetToOrderEntity(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }

        return orders;
    }
    private OrderEntity mapResultSetToOrderEntity(ResultSet resultSet) throws SQLException {
        return new OrderEntity(
                resultSet.getInt("id"),
                resultSet.getDate("date"),
                resultSet.getDouble("price"),
                resultSet.getString("paymentMethod"),
                resultSet.getInt("userId"),
                resultSet.getInt("customerId"),
                List.of()
        );
    }
}
