package service.custom;

import dto.Customer;
import service.SuperService;
import java.util.List;

public interface CustomerService extends SuperService {
    List<Customer> getCustomers();
    boolean addCustomer(Customer customer);
    Customer getCustomerById(int id);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(int customerId);
}