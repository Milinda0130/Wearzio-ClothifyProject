package dao;

import dao.custom.impl.*;
import util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return instance == null ? instance = new DaoFactory() : instance;
    }

    public <T extends SuperDao> T getDao(DaoType daoType) {
        switch (daoType) {
            case CUSTOMER:
                return (T) CustomerDaoImpl.getInstance();
            case PRODUCT:
                return (T) ProductDaoImpl.getInstance();
            case EMPLOYEE:
                return (T) new EmployeeDaoImpl();
            case USER:
                return (T) LoginSignupDaoImpl.getInstance();
            case ORDERPRODUCT:
                return (T) OrderDetaislDaoImpl.getInstance();
            case ORDERS:
                return (T) OrderDaoImpl.getInstance();
            case SUPPLIER:
                return (T) new SupplierDaoImpl();
        }
        return null;
    }
}