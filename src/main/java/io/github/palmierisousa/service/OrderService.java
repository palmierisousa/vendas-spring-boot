package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.OrderDTO;
import io.github.palmierisousa.domain.entity.Order;
import io.github.palmierisousa.domain.enums.OrderStatus;

public interface OrderService {
    Order save(OrderDTO dto);

    Order getFullOrder(Integer id);

    void updateStatus(Integer id, OrderStatus orderStatus);
}