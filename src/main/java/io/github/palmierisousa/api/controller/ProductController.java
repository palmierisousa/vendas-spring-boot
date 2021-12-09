package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ProductDTO;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public ProductDTO save(@RequestBody @Valid ProductDTO product) {
        Product p = service.save(product);

        return ProductDTO.builder().id(p.getId()).price(p.getUnitPrice()).description(p.getDescription()).build();
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
    public ProductDTO getById(@PathVariable Integer id) {
        Product p = service.get(id);

        return ProductDTO.builder().id(p.getId()).price(p.getUnitPrice()).description(p.getDescription()).build();
    }

    @GetMapping
    public List<ProductDTO> find(ProductDTO filter) {
        List<Product> products = service.filter(filter);

        return products.stream().map(
                product -> ProductDTO.builder().id(product.getId())
                        .price(product.getUnitPrice()).description(product.getDescription()).build()
        ).collect(Collectors.toList());
    }
}
