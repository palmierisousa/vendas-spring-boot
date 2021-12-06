package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.ProdutoDTO;
import io.github.palmierisousa.domain.entity.Produto;

import java.util.List;

public interface ProdutoService {
    Produto salvar(ProdutoDTO produto);

    void atualizar(ProdutoDTO produto);

    void deletar(Integer id);

    Produto obter(Integer id);

    List<Produto> filtrar(ProdutoDTO filtro);
}
