package com.tradedesk.order_service.service;

import org.springframework.stereotype.Service;

import com.tradedesk.order_service.dao.OrderDao;
import com.tradedesk.order_service.model.Order;
import com.tradedesk.order_service.model.OrderStatus;
import com.tradedesk.order_service.risk.RiskCheckService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final RiskCheckService riskCheckService;

    public OrderServiceImpl(OrderDao orderDao, RiskCheckService riskCheckService) {
        this.orderDao = orderDao;
        this.riskCheckService = riskCheckService;
    }

    @Override
    public Long placeOrder(Order order) {
        boolean approved = riskCheckService.checkRisk(order);
        order.setStatus(approved ? OrderStatus.NEW : OrderStatus.REJECTED);
        return orderDao.insertOrder(order);
    }
}
