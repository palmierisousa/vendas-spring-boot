package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.InformacoesItemPedidoDTO;
import io.github.palmierisousa.api.dto.InformacoesPedidoDTO;
import io.github.palmierisousa.api.dto.ItemPedidoDTO;
import io.github.palmierisousa.api.dto.PedidoDTO;
import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.domain.entity.ItemPedido;
import io.github.palmierisousa.domain.entity.Pedido;
import io.github.palmierisousa.domain.entity.Produto;
import io.github.palmierisousa.domain.enums.StatusPedido;
import io.github.palmierisousa.domain.repository.Clientes;
import io.github.palmierisousa.domain.repository.ItensPedido;
import io.github.palmierisousa.domain.repository.Pedidos;
import io.github.palmierisousa.domain.repository.Produtos;
import io.github.palmierisousa.exception.NotFoundException;
import io.github.palmierisousa.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidosRepository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new NotFoundException("Cliente inexistente: " + idCliente));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = converterItens(pedido, dto.getItens());
        pedidosRepository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public InformacoesPedidoDTO obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id)
                .map(this::converterPedido)
                .orElseThrow(() -> new NotFoundException("Pedido inexistente: " + id));
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        pedidosRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidosRepository.save(pedido);
                }).orElseThrow(() -> new NotFoundException("Pedido inexistente: " + id));
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new NotFoundException("Pedido sem itens: " + pedido.getId());
        }

        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "Produto inexistente: " + idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }

    private InformacoesPedidoDTO converterPedido(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converterItens(pedido.getItens()))
                .build();
    }

    private List<InformacoesItemPedidoDTO> converterItens(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacoesItemPedidoDTO
                        .builder().descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
