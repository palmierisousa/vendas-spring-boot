package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.ClientDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.domain.repository.Clients;
import io.github.palmierisousa.exception.NotFoundException;
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
                        new NotFoundException("Cliente inexistente: " + id));
    }

    @Override
    public Client save(ClientDTO client) {
        Client c = new Client();
        c.setCpf(client.getCpf());
        c.setName(client.getName());

        return repository.save(c);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.findById(id)
                .map(client -> {
                    repository.delete(client);
                    return client;
                })
                .orElseThrow(() -> new NotFoundException("Cliente inexistente: " + id));
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
                }).orElseThrow(() -> new NotFoundException("Cliente inexistente: " + client.getId()));
    }

    @Override
    public List<Client> filter(ClientDTO filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Client client = new Client();
        client.setName(filter.getName());
        client.setCpf(filter.getCpf());

        Example<Client> example = Example.of(client, matcher);
        return repository.findAll(example);
    }
}
