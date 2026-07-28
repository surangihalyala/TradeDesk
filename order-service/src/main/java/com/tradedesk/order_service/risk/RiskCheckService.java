package com.tradedesk.order_service.risk;

import com.tradedesk.order_service.model.Order;

public interface RiskCheckService {

    boolean checkRisk(Order order);
}
