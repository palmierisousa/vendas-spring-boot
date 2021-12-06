package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.ClienteDTO;
import io.github.palmierisousa.domain.entity.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente obter(Integer id);

    Cliente salvar(ClienteDTO cliente);

    void deletar(Integer id);

    void atualizar(ClienteDTO cliente);

    List<Cliente> filtrar(ClienteDTO filtro);
}
