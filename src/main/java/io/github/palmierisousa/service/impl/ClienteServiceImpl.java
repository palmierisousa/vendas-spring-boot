package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ClienteDTO;
import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.domain.repository.Clientes;
import io.github.palmierisousa.exception.NotFoundException;
import io.github.palmierisousa.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private final Clientes repository;

//    A injeção de dependência pode ser da forma acima ou abaixo.
//
//    private Clientes repository;
//    public ClienteServiceImpl(Clientes repository) {
//        this.repository = repository;
//    }

    @Override
    public Cliente obter(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Cliente inexistente: " + id));
    }

    @Override
    public Cliente salvar(ClienteDTO cliente) {
        Cliente c = new Cliente();
        c.setCpf(cliente.getCpf());
        c.setNome(cliente.getNome());

        return repository.save(c);
    }

    @Override
    @Transactional
    public void deletar(Integer id) {
        repository.findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new NotFoundException("Cliente inexistente: " + id));
    }

    @Override
    @Transactional
    public void atualizar(ClienteDTO cliente) {
        repository
                .findById(cliente.getId())
                .map(clienteExistente -> {
                    clienteExistente.setNome(cliente.getNome());
                    clienteExistente.setCpf(cliente.getCpf());
                    repository.save(clienteExistente);

                    return clienteExistente;
                }).orElseThrow(() -> new NotFoundException("Cliente inexistente: " + cliente.getId()));
    }

    @Override
    public List<Cliente> filtrar(ClienteDTO filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Cliente cliente = new Cliente();
        cliente.setNome(filtro.getNome());
        cliente.setCpf(filtro.getCpf());

        Example<Cliente> example = Example.of(cliente, matcher);
        return repository.findAll(example);
    }
}
