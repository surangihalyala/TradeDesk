package com.tradedesk.order_service.dao;

import com.tradedesk.order_service.model.Order;

public interface OrderDao {

    Long insertOrder(Order order);
}
