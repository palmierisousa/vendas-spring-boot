package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ProductDTO;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.domain.repository.Products;
import io.github.palmierisousa.exception.NotFoundException;
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
    public Product save(ProductDTO product) {
        return repository.save(
                Product.builder().description(product.getDescription()).unitPrice(product.getPrice()).build()
        );
    }

    @Override
    @Transactional
    public void update(ProductDTO product) {
        repository
                .findById(product.getId())
                .map(productFounded -> {
                    productFounded.setUnitPrice(product.getPrice());
                    productFounded.setDescription(product.getDescription());

                    repository.save(productFounded);
                    return productFounded;
                }).orElseThrow(() -> new NotFoundException("Produto inexistente: " + product.getId()));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.findById(id).map(p -> {
            repository.delete(p);
            return Void.TYPE;
        }).orElseThrow(() -> new NotFoundException("Produto inexistente: " + id));
    }

    @Override
    public Product get(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Produto inexistente: " + id));
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
                Product.builder().description(filter.getDescription()).build(),
                matcher);

        return repository.findAll(example);
    }
}
