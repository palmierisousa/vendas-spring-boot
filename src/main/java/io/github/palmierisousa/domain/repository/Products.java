package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Products extends JpaRepository<Product, Integer> {
}