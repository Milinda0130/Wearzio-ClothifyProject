package service;

import service.custom.OrderDetailsService;
import service.custom.SupplierService;
import service.custom.impl.*;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType type) {
        switch (type) {
            case CUSTOMER:
                return (T) CustomerServiceImpl.getInstance();
            case EMPLOYEE:
                return (T) new EmployeeServiceImpl();
            case PRODUCT:
                return (T) ProductServiceImpl.getInstance();
            case USER:
                return (T) LoginSignupServiceImpl.getInstance();
            case SUPPLIER:
                return (T) SupplierServiceImpl.getInstance();
            case ORDERS:
                return (T) OrderServiceImpl.getInstance();
            case ORDERPRODUCT:
                return (T) OrderDetailsServiceImpl.getInstance();


        }
        return null;
    }
}