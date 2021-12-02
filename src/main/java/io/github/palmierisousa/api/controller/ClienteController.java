package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.domain.repository.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ClienteController {

    @Autowired
    private Clientes clientes;

//    A injeção de dependência pode ser da forma acima ou abaixo.
//
//    private Clientes clientes;
//    public ClienteController(Clientes clientes) {
//        this.clientes = clientes;
//    }

    @GetMapping("api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer id) {
        Optional<Cliente> cliente = clientes.findById(id);

        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("api/clientes")
    @ResponseBody
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = clientes.save(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    @DeleteMapping("api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Cliente> cliente = clientes.findById(id);

        if (cliente.isPresent()) {
            clientes.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("api/clientes/{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return clientes.findById(id).map(clienteExistente -> {
            cliente.setId(clienteExistente.getId());
            clientes.save(cliente);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("api/clientes")
    public ResponseEntity<List<Cliente>> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Cliente> example = Example.of(filtro, matcher);
        List<Cliente> lista = clientes.findAll(example);
        return ResponseEntity.ok(lista);
    }
}