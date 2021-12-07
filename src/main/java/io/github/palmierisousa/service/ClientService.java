package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.ClientDTO;
import io.github.palmierisousa.domain.entity.Client;

import java.util.List;

public interface ClientService {
    Client get(Integer id);

    Client save(ClientDTO client);

    void delete(Integer id);

    void update(ClientDTO client);

    List<Client> filter(ClientDTO filter);
}
