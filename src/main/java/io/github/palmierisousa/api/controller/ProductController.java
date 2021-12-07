package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ProductDTO;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Product save(@RequestBody @Valid ProductDTO product) {
        return service.save(product);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid ProductDTO product) {
        product.setId(id);
        service.update(product);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("{id}")
    public Product getById(@PathVariable Integer id) {
        return service.get(id);
    }

    @GetMapping
    public List<Product> find(ProductDTO filter) {
        return service.filter(filter);
    }
}
