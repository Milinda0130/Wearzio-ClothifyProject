package service.custom.impl;

import dao.DaoFactory;
import dao.custom.OrderDao;
import dao.custom.OrderDetaisDao;
import dao.custom.ProductDao;
import dto.Order;
import dto.OrderDetails;
import entity.OrderDetailsEntity;
import entity.OrderEntity;
import org.modelmapper.ModelMapper;
import service.custom.OrderService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private static OrderServiceImpl orderServiceImpl;
    private final OrderDao orderDao;
    private final OrderDetaisDao orderDetailsDao;
    private final ProductDao productDao;
    private final ModelMapper modelMapper;
    private OrderServiceImpl() {
        orderDao = DaoFactory.getInstance().getDao(DaoType.ORDERS);
        orderDetailsDao = DaoFactory.getInstance().getDao(DaoType.ORDERPRODUCT);
        productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
        modelMapper = new ModelMapper();
    }

    public static OrderServiceImpl getInstance() {
        if (orderServiceImpl == null) {
            orderServiceImpl = new OrderServiceImpl();
        }
        return orderServiceImpl;
    }

    @Override
    public int getLastOrderId() {
        try {
            return orderDao.getLastOrderId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Order> getOrders() {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (OrderDetailsEntity entity : orderDetailsDao.getAll()) {
            orderDetailsList.add(modelMapper.map(entity, OrderDetails.class));
        }

        List<Order> orders = new ArrayList<>();
        for (OrderEntity entity : orderDao.getAll()) {
            Order order = modelMapper.map(entity, Order.class);

            List<OrderDetails> matchedDetails = new ArrayList<>();
            for (OrderDetails details : orderDetailsList) {
                if (details.getOrderId() == order.getId()) {
                    matchedDetails.add(details);
                }
            }
            order.setOrderDetailsList(matchedDetails);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public Order getOrder(int orderId) {
        try {
            OrderEntity orderEntity = orderDao.search(String.valueOf(orderId));
            if (orderEntity != null) {
                return modelMapper.map(orderEntity, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addOrder(Order order) {
        try {
            List<OrderDetailsEntity> orderDetailEntities = new ArrayList<>();
            for (OrderDetails orderDetails : order.getOrderDetailsList()) {
                orderDetailEntities.add(modelMapper.map(orderDetails, OrderDetailsEntity.class));
            }

            OrderEntity orderEntity = new OrderEntity(
                    order.getId(),
                    order.getDate(),
                    order.getPrice(),
                    order.getPaymentMethod(),
                    order.getUserId(),
                    order.getCustomerId(),
                    orderDetailEntities
            );

            boolean isOrderSaved = orderDao.save(orderEntity);
            if (isOrderSaved) {
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
