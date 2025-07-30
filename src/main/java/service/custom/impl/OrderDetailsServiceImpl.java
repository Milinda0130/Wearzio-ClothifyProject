package service.custom.impl;

import dao.DaoFactory;
import dao.custom.OrderDetaisDao;
import dto.OrderDetails;
import entity.OrderDetailsEntity;
import org.modelmapper.ModelMapper;
import service.custom.OrderDetailsService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {
    private static OrderDetailsServiceImpl orderDetailsServiceImpl;
    private final OrderDetaisDao orderDetailsDao;
    private final ModelMapper modelMapper;

    private OrderDetailsServiceImpl() {
        orderDetailsDao = DaoFactory.getInstance().getDao(DaoType.ORDERPRODUCT);
        modelMapper = new ModelMapper();
    }

    public static OrderDetailsServiceImpl getInstance() {
        if (orderDetailsServiceImpl == null) {
            orderDetailsServiceImpl = new OrderDetailsServiceImpl();
        }
        return orderDetailsServiceImpl;
    }
    @Override
    public List<OrderDetails> getOrderProducts() {
        List<OrderDetails> orderDetails = new ArrayList<>();
        try {
            List<OrderDetailsEntity> orderDetailEntities = orderDetailsDao.getAll();
            for (OrderDetailsEntity entity : orderDetailEntities) {
                orderDetails.add(modelMapper.map(entity, OrderDetails.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderDetails;
    }
}

