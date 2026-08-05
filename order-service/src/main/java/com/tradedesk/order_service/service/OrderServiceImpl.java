package com.tradedesk.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tradedesk.order_service.dao.OrderDao;
import com.tradedesk.order_service.event.OrderPlacedEvent;
import com.tradedesk.order_service.model.Order;
import com.tradedesk.order_service.model.OrderStatus;
import com.tradedesk.order_service.risk.RiskCheckService;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_PLACED_TOPIC = "order-placed";

    private final OrderDao orderDao;
    private final RiskCheckService riskCheckService;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderServiceImpl(OrderDao orderDao, RiskCheckService riskCheckService,
                             KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.orderDao = orderDao;
        this.riskCheckService = riskCheckService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Long placeOrder(Order order) {
        boolean approved = riskCheckService.checkRisk(order);
        order.setStatus(approved ? OrderStatus.NEW : OrderStatus.REJECTED);
        Long id = orderDao.insertOrder(order);

        if (approved) {
            OrderPlacedEvent event = OrderPlacedEvent.builder()
                    .orderId(id)
                    .symbol(order.getSymbol())
                    .side(order.getSide().toString())
                    .quantity(order.getQuantity())
                    .price(order.getPrice())
                    .status(order.getStatus().toString())
                    .build();
            kafkaTemplate.send(ORDER_PLACED_TOPIC, id.toString(), event);
        }

        return id;
    }
}
