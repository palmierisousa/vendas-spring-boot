package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.InformacoesPedidoDTO;
import io.github.palmierisousa.api.dto.PedidoDTO;
import io.github.palmierisousa.api.dto.StatusPedidoDTO;
import io.github.palmierisousa.domain.entity.Pedido;
import io.github.palmierisousa.domain.enums.StatusPedido;
import io.github.palmierisousa.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return service.obterPedidoCompleto(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody StatusPedidoDTO status) {
        service.atualizarStatus(id, StatusPedido.valueOf(status.getNovoStatus()));
    }
}
