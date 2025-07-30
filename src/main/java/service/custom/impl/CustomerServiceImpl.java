package service.custom.impl;

import dao.DaoFactory;
import dao.custom.CustomerDao;
import dto.Customer;
import entity.CustomerEntity;
import org.modelmapper.ModelMapper;
import service.custom.CustomerService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private static CustomerServiceImpl instance;
    private final CustomerDao dao;
    private final ModelMapper modelMapper;

    private CustomerServiceImpl() {
        dao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);
        modelMapper = new ModelMapper();
    }

    public static CustomerServiceImpl getInstance() {
        if (instance == null) {
            instance = new CustomerServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Customer> getCustomers() {
        try {
            List<CustomerEntity> customerEntities = dao.getAll();
            List<Customer> customers = new ArrayList<>();
            for (CustomerEntity entity : customerEntities) {
                customers.add(modelMapper.map(entity, Customer.class));
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean addCustomer(Customer customer) {
        try {
            if (customer == null || customer.getName() == null || customer.getName().trim().isEmpty()) {
                return false;
            }
            CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
            return dao.save(customerEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getCustomerById(int id) {
        try {
            CustomerEntity entity = dao.search(String.valueOf(id));
            if (entity != null) {
                return modelMapper.map(entity, Customer.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try {
            if (customer == null || customer.getId() <= 0) {
                return false;
            }
            CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
            return dao.update(customerEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        try {
            if (customerId <= 0) {
                return false;
            }
            return dao.delete(String.valueOf(customerId));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
