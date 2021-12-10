package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Products extends JpaRepository<Product, Integer> {
    boolean existsByCode(Integer code);

    Optional<Product> findByCode(Integer code);
}