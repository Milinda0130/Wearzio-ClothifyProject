package service.custom;

import dto.Order;
import service.SuperService;

import java.util.List;

public interface OrderService extends SuperService {
    int getLastOrderId();

    List<Order> getOrders();

    Order getOrder(int orderId);

    boolean addOrder(Order order);
}