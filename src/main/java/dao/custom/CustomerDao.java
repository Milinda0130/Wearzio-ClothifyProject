package dao.custom;

import dao.SuperDao;
import entity.CustomerEntity;
import java.util.List;

public interface CustomerDao extends SuperDao {
    boolean save(CustomerEntity entity);
    CustomerEntity search(String id);
    boolean delete(String id);
    boolean update(CustomerEntity entity);
    List<CustomerEntity> getAll();
}