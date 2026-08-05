package com.tradedesk.settlement_service.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tradedesk.settlement_service.model.Settlement;

@Repository
public class SettlementDaoImpl implements SettlementDao {

    private final JdbcTemplate jdbcTemplate;

    public SettlementDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insertSettlement(Settlement settlement) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO settlements (order_id, status) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, settlement.getOrderId());
            ps.setString(2, settlement.getStatus().toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
