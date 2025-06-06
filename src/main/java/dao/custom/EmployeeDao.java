package dao.custom;

import dao.SuperDao;
import entity.EmployeeEntity;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends SuperDao {
    boolean saveEmployee(EmployeeEntity employee) throws SQLException;
    boolean updateEmloyee(EmployeeEntity employee) throws SQLException;
    boolean deleteEmployee(Integer id) throws SQLException;
    EmployeeEntity searchById(Integer id) throws SQLException;
    List<EmployeeEntity> getAll() throws SQLException;
}
