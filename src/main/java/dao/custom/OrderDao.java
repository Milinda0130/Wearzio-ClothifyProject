package dao.custom;

import dao.CrudDao;
import entity.OrderEntity;

public interface OrderDao extends CrudDao<OrderEntity,String> {
    int getLastOrderId();

}
