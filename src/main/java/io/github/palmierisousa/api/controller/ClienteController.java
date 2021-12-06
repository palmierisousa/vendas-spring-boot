package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.ClienteDTO;
import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Cliente getClienteById(@PathVariable Integer id) {
        return service.obter(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save(@RequestBody @Valid ClienteDTO cliente) {
        return service.salvar(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deletar(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid ClienteDTO cliente) {
        cliente.setId(id);
        service.atualizar(cliente);
    }

    @GetMapping
    public List<Cliente> find(ClienteDTO filtro) {
        return service.filtrar(filtro);
    }

}
