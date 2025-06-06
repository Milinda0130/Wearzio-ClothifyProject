package service.custom.impl;

import dao.DaoFactory;
import dao.custom.EmployeeDao;
import dto.Employee;
import entity.EmployeeEntity;
import org.modelmapper.ModelMapper;
import service.custom.EmployeeService;
import util.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    EmployeeDao employeeDao = DaoFactory.getInstance().getDao(DaoType.EMPLOYEE);


    @Override
    public boolean addEmployee(Employee employee) {
        EmployeeEntity employee1 = new ModelMapper().map(employee, EmployeeEntity.class);
        try {
            return employeeDao.saveEmployee(employee1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        EmployeeEntity employee1 = new ModelMapper().map(employee, EmployeeEntity.class);
        try {
            return employeeDao.updateEmloyee(employee1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getEmployees() {
        try {
            List<EmployeeEntity> entityEmployees = employeeDao.getAll();
            List<Employee> employees = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();

            for (EmployeeEntity entity : entityEmployees) {
                employees.add(modelMapper.map(entity, Employee.class));
            }

            return employees;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean deleteEmployee(int employeeId) {

        try {
            return employeeDao.deleteEmployee(employeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee searchEmployeeById(int employeeId) {
        try {
            EmployeeEntity entity = employeeDao.searchById(employeeId);

            if (entity != null) {
                return new ModelMapper().map(entity, Employee.class);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
