package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
