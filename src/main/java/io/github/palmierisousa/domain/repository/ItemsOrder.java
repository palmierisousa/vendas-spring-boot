package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsOrder extends JpaRepository<OrderItem, Integer> {
}
