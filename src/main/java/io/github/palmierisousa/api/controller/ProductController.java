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

        return ProductDTO.builder().code(p.getCode()).price(p.getUnitPrice())
                .description(p.getDescription()).build();
    }

    @PutMapping("{code}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer code, @RequestBody @Valid ProductDTO product) {
        product.setCode(code);
        service.update(product);
    }

    @DeleteMapping("{code}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer code) {
        service.delete(code);
    }

    @GetMapping("{code}")
    public ProductDTO getByCode(@PathVariable Integer code) {
        Product p = service.get(code);

        return ProductDTO.builder().code(p.getCode()).price(p.getUnitPrice())
                .description(p.getDescription()).build();
    }

    @GetMapping
    public List<ProductDTO> find(ProductDTO filter) {
        List<Product> products = service.filter(filter);

        return products.stream().map(
                product -> ProductDTO.builder()
                        .code(product.getCode()).price(product.getUnitPrice()).description(product.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }
}
