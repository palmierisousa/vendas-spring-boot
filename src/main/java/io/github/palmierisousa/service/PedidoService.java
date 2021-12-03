package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.InformacoesPedidoDTO;
import io.github.palmierisousa.api.dto.PedidoDTO;
import io.github.palmierisousa.domain.entity.Pedido;
import io.github.palmierisousa.domain.enums.StatusPedido;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    InformacoesPedidoDTO obterPedidoCompleto(Integer id);

    void atualizarStatus(Integer id, StatusPedido statusPedido);
}