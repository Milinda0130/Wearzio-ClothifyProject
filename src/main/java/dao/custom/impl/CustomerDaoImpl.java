package dao.custom.impl;

import dao.custom.CustomerDao;
import db.DBConnection;
import entity.CustomerEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private static CustomerDaoImpl instance;

    private CustomerDaoImpl() {}

    public static CustomerDaoImpl getInstance() {
        if (instance == null) {
            instance = new CustomerDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean save(CustomerEntity entity) {
        String query = "INSERT INTO customer (name, mobileNumber, address) VALUES (?,?,?)";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getMobile());
            statement.setString(3, entity.getAddress());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CustomerEntity search(String id) {
        String query = "SELECT customerId, name, mobileNumber, address FROM customer WHERE customerId = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CustomerEntity(
                            resultSet.getInt("customerId"),
                            resultSet.getString("name"),
                            resultSet.getString("mobileNumber"),
                            resultSet.getString("address")
                    );
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM customer WHERE customerId = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            return statement.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(CustomerEntity entity) {
        String query = "UPDATE customer SET name = ?, mobileNumber = ?, address = ? WHERE customerId = ?";
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getMobile());
            statement.setString(3, entity.getAddress());
            statement.setInt(4, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CustomerEntity> getAll() {
        String query = "SELECT customerId, name, mobileNumber, address FROM customer";
        List<CustomerEntity> customers = new ArrayList<>();
        try (PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                customers.add(new CustomerEntity(
                        resultSet.getInt("customerId"),
                        resultSet.getString("name"),
                        resultSet.getString("mobileNumber"),
                        resultSet.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}