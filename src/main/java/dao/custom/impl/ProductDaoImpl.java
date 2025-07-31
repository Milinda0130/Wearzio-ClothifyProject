package dao.custom.impl;

import dao.custom.ProductDao;
import db.DBConnection;
import entity.OrderDetailsEntity;
import entity.ProductEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private static ProductDaoImpl productDaoImpl;

    public static ProductDaoImpl getInstance() {
        if (productDaoImpl == null) {
            productDaoImpl = new ProductDaoImpl();
        }
        return productDaoImpl;

    }

    @Override
    public boolean updateQuantity(List<OrderDetailsEntity> entities) {
        for (OrderDetailsEntity entity : entities) {
            boolean isUpdate = minusQuantity(entity);
            if (!isUpdate) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean save(ProductEntity entity) {
        String query = "INSERT INTO product (name, size, price, quantityOnHand , category, image, supplierId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSize());
            statement.setDouble(3, entity.getPrice());
            statement.setInt(4, entity.getQuantityOnHand());
            statement.setString(5, entity.getCategory());
            statement.setString(6, entity.getImage());
            statement.setInt(7, entity.getSupplierId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public ProductEntity search(Integer id) {
        try {
            String query = "SELECT * FROM product WHERE id = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new ProductEntity(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(8)


                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM product WHERE id = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(ProductEntity entity) {
        String query = "UPDATE product SET name, category = ?, size = ?, price = ?, quantityOnHand = ?, image = ?, supplierId = ? WHERE id = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCategory());
            statement.setString(3, entity.getSize());
            statement.setDouble(4, entity.getPrice());
            statement.setInt(5, entity.getQuantityOnHand());
            statement.setString(6, entity.getImage());
            statement.setInt(7, entity.getSupplierId());
            statement.setInt(8, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<ProductEntity> getAll() {
        String query = "SELECT * FROM product";
        List<ProductEntity> products = new ArrayList<>();
        try {
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(query);
            while (resultSet.next()) {
                ProductEntity product = new ProductEntity(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(8)
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
    public boolean minusQuantity(OrderDetailsEntity entity) {
        String query = "UPDATE product SET quantityOnHand = quantityOnHand-? WHERE id ?";
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, entity.getQuantity());
            statement.setInt(2, entity.getProductId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
