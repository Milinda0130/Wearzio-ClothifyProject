package service.custom;

import dto.OrderDetails;
import service.SuperService;

import java.util.List;

public interface OrderDetailsService extends SuperService {
    List<OrderDetails> getOrderProducts();
}
