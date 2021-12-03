package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.domain.entity.Produto;
import io.github.palmierisousa.domain.repository.Produtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/produtos")
//TODO Criar classse de serviço e tratar exceções
public class ProdutoController {

    @Autowired
    private Produtos repository;

    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody Produto produto) {
        repository
                .findById(id)
                .map(p -> {
                    produto.setId(p.getId());
                    repository.save(produto);
                    return produto;
                }).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository
                .findById(id)
                .map(p -> {
                    repository.delete(p);
                    return Void.TYPE;
                }).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado."));
    }

    @GetMapping("{id}")
    public Produto getById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado."));
    }

    @GetMapping
    public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }
}
