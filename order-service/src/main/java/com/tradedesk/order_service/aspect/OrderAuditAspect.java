package com.tradedesk.order_service.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tradedesk.order_service.model.Order;

@Aspect
@Component
public class OrderAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(OrderAuditAspect.class);

    @AfterReturning(
            pointcut = "execution(* com.tradedesk.order_service.service.OrderService.placeOrder(..)) && args(order)",
            returning = "id"
    )
    public void logOrderPlaced(Order order, Long id) {
        log.info("AUDIT: order placed - id={}, symbol={}, side={}, quantity={}, status={}",
                id, order.getSymbol(), order.getSide(), order.getQuantity(), order.getStatus());
    }

    @AfterThrowing(
            pointcut = "execution(* com.tradedesk.order_service.service.OrderService.placeOrder(..)) && args(order)",
            throwing = "ex"
    )
    public void logOrderPlacementFailed(Order order, Exception ex) {
        log.error("AUDIT: order placement failed - symbol={}, side={}, quantity={}, error={}",
                order.getSymbol(), order.getSide(), order.getQuantity(), ex.getMessage());
    }
}
