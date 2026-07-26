package com.tradedesk.order_service.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tradedesk.order_service.model.Order;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insertOrder(Order order) {
        return jdbcTemplate.queryForObject(
                "{call usp_InsertOrder(?, ?, ?, ?, ?)}",
                Long.class,
                order.getSymbol(),
                order.getSide().toString(),
                order.getQuantity(),
                order.getPrice(),
                order.getStatus().toString()
        );
    }
}
