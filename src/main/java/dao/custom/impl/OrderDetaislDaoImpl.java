package dao.custom.impl;

import dao.custom.OrderDetaisDao;
import db.DBConnection;
import entity.OrderDetailsEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetaislDaoImpl implements OrderDetaisDao {

    private static OrderDetaislDaoImpl orderDetailsDaoImpl;
    public static OrderDetaislDaoImpl getInstance() {
        if (orderDetailsDaoImpl == null) {
            orderDetailsDaoImpl = new OrderDetaislDaoImpl();
        }
        return orderDetailsDaoImpl;
    }
    @Override
    public boolean save(List<OrderDetailsEntity> orderProductEntities) {
        String query = "INSERT INTO orderdetails (orderId, productId, quantity) VALUES (?, ?, ?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            for (OrderDetailsEntity entity : orderProductEntities) {
                statement.setInt(1, entity.getOrderId());
                statement.setInt(2, entity.getProductId());
                statement.setInt(3, entity.getQuantity());

                if (statement.executeUpdate() <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving order details", e);
        }
    }

    @Override
    public List<OrderDetailsEntity> getAll() {
        String query = "SELECT orderDetailsId, orderId, productId, quantity FROM orderdetails";
        List<OrderDetailsEntity> orderProducts = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderProducts.add(new OrderDetailsEntity(
                        resultSet.getInt("orderDetailsId"),
                        resultSet.getInt("orderId"),
                        resultSet.getInt("productId"),
                        resultSet.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching order details", e);
        }
        return orderProducts;
    }
}
