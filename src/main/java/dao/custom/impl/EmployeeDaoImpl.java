package dao.custom.impl;

import dao.custom.EmployeeDao;
import entity.EmployeeEntity;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public boolean saveEmployee(EmployeeEntity employee) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO Employee (id, name, email, jobRole) VALUES (?, ?, ?, ?)",
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getJobRole()
        );
    }

    @Override
    public boolean updateEmloyee(EmployeeEntity employee) throws SQLException {
        return CrudUtil.execute(
                "UPDATE Employee SET name = ?, email = ?, jobRole = ? WHERE id = ?",
                employee.getName(),
                employee.getEmail(),
                employee.getJobRole(),
                employee.getId()
        );
    }

    @Override
    public boolean deleteEmployee(Integer id) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM Employee WHERE id = ?",
                id
        );
    }

    @Override
    public EmployeeEntity searchById(Integer id) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Employee WHERE id = ?", id);
        if (rs.next()) {
            return new EmployeeEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("jobRole")
            );
        }
        return null;
    }

    @Override
    public List<EmployeeEntity> getAll() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Employee");
        List<EmployeeEntity> employees = new ArrayList<>();
        while (rs.next()) {
            employees.add(new EmployeeEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("jobRole")
            ));
        }
        return employees;
    }
}
