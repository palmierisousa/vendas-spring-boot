package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ClientDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ClientDTO getClienteById(@PathVariable Integer id) {
        Client c = service.get(id);

        return ClientDTO.builder().id(c.getId()).cpf(c.getCpf()).name(c.getName()).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO save(@RequestBody @Valid ClientDTO client) {
        Client c = service.save(client);

        return ClientDTO.builder().id(c.getId()).name(c.getName()).cpf(c.getCpf()).build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid ClientDTO client) {
        client.setId(id);
        service.update(client);
    }

    @GetMapping
    public List<ClientDTO> find(ClientDTO filter) {
        List<Client> clients = service.filter(filter);

        return clients.stream().map(
                c -> ClientDTO.builder().id(c.getId()).name(c.getName()).cpf(c.getCpf()).build()
        ).collect(Collectors.toList());
    }
}
