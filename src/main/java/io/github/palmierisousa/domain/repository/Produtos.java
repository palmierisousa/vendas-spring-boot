package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
