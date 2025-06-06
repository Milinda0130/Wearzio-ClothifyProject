package service.custom;

import dto.Employee;
import service.SuperService;

import java.util.List;

public interface EmployeeService extends SuperService {

    boolean addEmployee (Employee employee);
    boolean updateEmployee (Employee employee);
    List<Employee> getEmployees();
    boolean deleteEmployee (int employeeId);
    Employee searchEmployeeById (int employeeId);
}
