package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ClientDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Client getClienteById(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client save(@RequestBody @Valid ClientDTO client) {
        return service.save(client);
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
    public List<Client> find(ClientDTO filter) {
        return service.filter(filter);
    }

}
