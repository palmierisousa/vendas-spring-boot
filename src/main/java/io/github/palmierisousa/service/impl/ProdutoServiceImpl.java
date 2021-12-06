package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ProdutoDTO;
import io.github.palmierisousa.domain.entity.Produto;
import io.github.palmierisousa.domain.repository.Produtos;
import io.github.palmierisousa.exception.NotFoundException;
import io.github.palmierisousa.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {
    @Autowired
    private final Produtos repository;

    @Override
    public Produto salvar(ProdutoDTO produto) {
        Produto p = new Produto();
        p.setDescricao(produto.getDescricao());
        p.setPreco(produto.getPreco());

        repository.save(p);

        return p;
    }

    @Override
    @Transactional
    public void atualizar(ProdutoDTO produto) {
        repository
                .findById(produto.getId())
                .map(produtoExistente -> {
                    produtoExistente.setPreco(produto.getPreco());
                    produtoExistente.setDescricao(produto.getDescricao());

                    repository.save(produtoExistente);
                    return produtoExistente;
                }).orElseThrow(() -> new NotFoundException("Produto inexistente: " + produto.getId()));
    }

    @Override
    @Transactional
    public void deletar(Integer id) {
        repository.findById(id).map(p -> {
            repository.delete(p);
            return Void.TYPE;
        }).orElseThrow(() -> new NotFoundException("Produto inexistente: " + id));
    }

    @Override
    public Produto obter(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Produto inexistente: " + id));
    }

    @Override
    public List<Produto> filtrar(ProdutoDTO filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Produto produto = new Produto();
        produto.setDescricao(filtro.getDescricao());
        
        Example example = Example.of(produto, matcher);

        return repository.findAll(example);
    }
}
