package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface Pedidos extends JpaRepository<Pedido, Integer> {

    Set<Pedido> findByCliente(Cliente cliente);
}
