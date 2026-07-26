package com.tradedesk.order_service.service;

import org.springframework.stereotype.Service;

import com.tradedesk.order_service.dao.OrderDao;
import com.tradedesk.order_service.model.Order;
import com.tradedesk.order_service.model.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Long placeOrder(Order order) {
        order.setStatus(OrderStatus.NEW);
        return orderDao.insertOrder(order);
    }
}
