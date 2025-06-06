package service;

import service.custom.impl.EmployeeServiceImpl;
import service.custom.impl.ProductServiceImpl;
import util.ServiceType;

public class ServiceFactory {


        private static service.ServiceFactory instance;

        private ServiceFactory(){}

        public static service.ServiceFactory getInstance() {
            return instance==null?instance=new service.ServiceFactory():instance;
        }

        public <T extends SuperService>T getServiceType(ServiceType type){

            switch (type){
                case EMPLOYEE: return (T) new EmployeeServiceImpl();
                case PRODUCT: return (T) new ProductServiceImpl();
            }
            return null;

        }
    }


