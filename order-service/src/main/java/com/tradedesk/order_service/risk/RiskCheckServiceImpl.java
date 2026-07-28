package com.tradedesk.order_service.risk;

import com.tradedesk.order_service.model.Order;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

@Component
public class RiskCheckServiceImpl implements RiskCheckService {

    private static final BigDecimal EXPOSURE_LIMIT = new BigDecimal("100000");
    private static final int MAX_CONCURRENT_RISK_API_CALLS = 3;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Semaphore riskApiPermits = new Semaphore(MAX_CONCURRENT_RISK_API_CALLS);

    @Override
    public boolean checkRisk(Order order) {
        Future<Boolean> future = executorService.submit(() -> callExternalRiskApi(order));
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Risk check interrupted", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Risk check failed", e.getCause());
        }
    }

    private boolean callExternalRiskApi(Order order) throws InterruptedException {
        riskApiPermits.acquire();
        try {
            Thread.sleep(500);
            BigDecimal exposure = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
            return exposure.compareTo(EXPOSURE_LIMIT) <= 0;
        } finally {
            riskApiPermits.release();
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
