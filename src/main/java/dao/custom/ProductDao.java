package dao.custom;

import dao.CrudDao;
import entity.OrderDetailsEntity;
import entity.ProductEntity;

import java.util.List;

public interface ProductDao extends CrudDao<ProductEntity, Integer> {
    boolean updateQuantity(List<OrderDetailsEntity> entities);

}
