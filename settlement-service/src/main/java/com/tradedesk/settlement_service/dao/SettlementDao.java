package com.tradedesk.settlement_service.dao;

import com.tradedesk.settlement_service.model.Settlement;

public interface SettlementDao {

    Long insertSettlement(Settlement settlement);
}
