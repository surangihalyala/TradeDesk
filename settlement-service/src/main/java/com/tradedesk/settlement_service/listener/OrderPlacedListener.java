package com.tradedesk.settlement_service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.tradedesk.settlement_service.dao.SettlementDao;
import com.tradedesk.settlement_service.event.OrderPlacedEvent;
import com.tradedesk.settlement_service.model.Settlement;
import com.tradedesk.settlement_service.model.SettlementStatus;

@Component
public class OrderPlacedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedListener.class);

    private final SettlementDao settlementDao;

    public OrderPlacedListener(SettlementDao settlementDao) {
        this.settlementDao = settlementDao;
    }

    @KafkaListener(topics = "order-placed", groupId = "settlement-service")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received OrderPlaced event: orderId={}, symbol={}, side={}, quantity={}",
                event.getOrderId(), event.getSymbol(), event.getSide(), event.getQuantity());

        Settlement settlement = Settlement.builder()
                .orderId(event.getOrderId())
                .status(SettlementStatus.PENDING)
                .build();

        Long settlementId = settlementDao.insertSettlement(settlement);
        log.info("Created settlement id={} for orderId={}, status=PENDING", settlementId, event.getOrderId());
    }
}
