package dao.custom.impl;

import dao.custom.OrderDao;
import db.DBConnection;
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
        String query = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";
        try (
                Statement stmt = DBConnection.getInstance().getConnection().createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("orderId");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching last OrderID", e);
        }
        return -1;
    }

    @Override
    public boolean save(OrderEntity entity) {
        return false;
    }

    @Override
    public OrderEntity search(String id) {
        String query = "SELECT * FROM orders WHERE orderId = ?";
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
        String query = "DELETE FROM orders WHERE orderId = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }    }

    @Override
    public boolean update(OrderEntity entity) {
        String query = "UPDATE orders SET orderDate = ?, totalAmount = ?, paymentMethod = ?, employeeId = ?, customerId = ? WHERE orderId = ?";
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
        String query = "SELECT orderId, orderDate, totalAmount, paymentMethod, employeeId, customerId FROM orders";
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
                resultSet.getInt("orderId"),
                resultSet.getDate("orderDate"),
                resultSet.getDouble("totalAmount"),
                resultSet.getString("paymentMethod"),
                resultSet.getInt("employeeId"),
                resultSet.getInt("customerId"),
                List.of()
        );
    }
}
