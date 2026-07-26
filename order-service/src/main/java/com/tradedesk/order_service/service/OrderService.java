package com.tradedesk.order_service.service;

import com.tradedesk.order_service.model.Order;

public interface OrderService {

    Long placeOrder(Order order);
}
