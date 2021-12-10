package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.ProductDTO;
import io.github.palmierisousa.domain.entity.Product;

import java.util.List;

public interface ProductService {
    Product save(ProductDTO product);

    void update(ProductDTO product);

    void delete(Integer code);

    Product get(Integer code);

    List<Product> filter(ProductDTO filter);
}
