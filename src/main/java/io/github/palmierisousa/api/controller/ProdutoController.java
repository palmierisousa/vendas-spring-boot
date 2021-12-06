package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ProdutoDTO;
import io.github.palmierisousa.domain.entity.Produto;
import io.github.palmierisousa.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody @Valid ProdutoDTO produto) {
        return service.salvar(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid ProdutoDTO produto) {
        produto.setId(id);
        service.atualizar(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deletar(id);
    }

    @GetMapping("{id}")
    public Produto getById(@PathVariable Integer id) {
        return service.obter(id);
    }

    @GetMapping
    public List<Produto> find(ProdutoDTO filtro) {
        return service.filtrar(filtro);
    }
}
