package dao.custom.impl;

import dao.custom.SuplierDao;
import db.DBConnection;
import entity.SupplierEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SupplierDaoImpl implements SuplierDao {

    @Override
    public boolean save(SupplierEntity entity) {
        String query = "INSERT INTO supplier (Name, Company, Email, Item) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSupplierCompany());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getItem());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public SupplierEntity search(Integer integer) {
        return null;
    }

    @Override
    public boolean delete(Integer supplierId) {
        String query = "DELETE FROM supplier WHERE SupplierID = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, supplierId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(SupplierEntity entity) {
        String query = "Update supplier SET Name = ?, Company = ?, Email = ?, Item = ? WHERE SupplierID = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSupplierCompany());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getItem());
            statement.setInt(5, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<SupplierEntity> getAll() {
        return List.of();
    }
}
