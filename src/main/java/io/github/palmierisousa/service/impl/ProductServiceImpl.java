package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ProductDTO;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.domain.repository.Products;
import io.github.palmierisousa.exception.ElementAlreadyExists;
import io.github.palmierisousa.exception.ElementNotFoundException;
import io.github.palmierisousa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final Products repository;

    @Override
    @Transactional
    public Product save(ProductDTO product) {
        boolean exists = repository.existsByCode(product.getCode());

        if (exists) {
            throw new ElementAlreadyExists("Produto já cadastrado: " + product.getCode());
        }

        return repository.save(
                Product.builder().code(product.getCode()).description(product.getDescription())
                        .unitPrice(product.getPrice()).build()
        );
    }

    @Override
    @Transactional
    public void update(ProductDTO product) {
        repository
                .findByCode(product.getCode())
                .map(productFounded -> {
                    productFounded.setUnitPrice(product.getPrice());
                    productFounded.setDescription(product.getDescription());

                    repository.save(productFounded);
                    return productFounded;
                }).orElseThrow(() -> new ElementNotFoundException("Produto inexistente: " + product.getCode()));
    }

    @Override
    @Transactional
    public void delete(Integer code) {
        repository.findByCode(code).map(p -> {
            repository.delete(p);
            return Void.TYPE;
        }).orElseThrow(() -> new ElementNotFoundException("Produto inexistente: " + code));
    }

    @Override
    public Product get(Integer code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ElementNotFoundException("Produto inexistente: " + code));
    }

    @Override
    public List<Product> filter(ProductDTO filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Product product = new Product();
        product.setDescription(filter.getDescription());

        Example<Product> example = Example.of(
                Product.builder().code(filter.getCode()).description(filter.getDescription()).build(),
                matcher);

        return repository.findAll(example);
    }
}
