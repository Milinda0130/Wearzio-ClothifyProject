package dao.custom;

import dao.SuperDao;
import entity.OrderDetailsEntity;

import java.util.List;

public interface OrderDetaisDao extends SuperDao {
    boolean save(List<OrderDetailsEntity> orderProductEntities);
    List<OrderDetailsEntity> getAll();

}
