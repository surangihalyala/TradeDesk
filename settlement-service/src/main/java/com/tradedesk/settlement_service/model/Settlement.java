package com.tradedesk.settlement_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {

    private Long id;
    private Long orderId;
    private SettlementStatus status;
    private LocalDateTime createdAt;
}
