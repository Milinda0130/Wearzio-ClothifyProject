package dao;

import dao.custom.EmployeeDao;
import dao.custom.impl.EmployeeDaoImpl;
import dao.custom.impl.ProductDaoImpl;
import util.DaoType;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {

        return instance == null ? instance = new DaoFactory() : instance;

    }

    public <T extends SuperDao> T getDao(DaoType daoType) {
        switch (daoType) {
            case PRODUCT:
                return (T) ProductDaoImpl.getInstance();
            case EMPLOYEE:
                return (T) new EmployeeDaoImpl();
            case USER:
                return (T) new EmployeeDaoImpl();

        }
        return null;
    }
}
