package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ClientDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.domain.repository.Clients;
import io.github.palmierisousa.exception.ElementAlreadyExists;
import io.github.palmierisousa.exception.ElementNotFoundException;
import io.github.palmierisousa.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private final Clients repository;

//    A injeção de dependência pode ser da forma acima ou abaixo.
//
//    private Clientes repository;
//    public ClienteServiceImpl(Clientes repository) {
//        this.repository = repository;
//    }

    @Override
    public Client get(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ElementNotFoundException("Cliente inexistente: " + id));
    }

    @Override
    @Transactional
    public Client save(ClientDTO client) {
        boolean exists = repository.existsByCpf(client.getCpf());

        if (exists) {
            throw new ElementAlreadyExists("Cliente já cadastrado: " + client.getCpf());
        }

        return repository.save(
                Client.builder().cpf(client.getCpf()).name(client.getName()).build()
        );
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.findById(id)
                .map(client -> {
                    repository.delete(client);
                    return client;
                })
                .orElseThrow(() -> new ElementNotFoundException("Cliente inexistente: " + id));
    }

    @Override
    @Transactional
    public void update(ClientDTO client) {
        repository
                .findById(client.getId())
                .map(clientFounded -> {
                    clientFounded.setName(client.getName());
                    clientFounded.setCpf(client.getCpf());
                    repository.save(clientFounded);

                    return clientFounded;
                }).orElseThrow(() -> new ElementNotFoundException("Cliente inexistente: " + client.getId()));
    }

    @Override
    public List<Client> filter(ClientDTO filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<Client> example = Example.of(
                Client.builder().cpf(filter.getCpf()).name(filter.getName()).build(),
                matcher);
        return repository.findAll(example);
    }
}
