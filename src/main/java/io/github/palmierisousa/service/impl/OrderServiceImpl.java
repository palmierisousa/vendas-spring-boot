package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.OrderDTO;
import io.github.palmierisousa.api.dto.OrderInformationDTO;
import io.github.palmierisousa.api.dto.OrderItemDTO;
import io.github.palmierisousa.api.dto.OrderItemInformationsDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.domain.entity.Order;
import io.github.palmierisousa.domain.entity.OrderItem;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.domain.enums.OrderStatus;
import io.github.palmierisousa.domain.repository.Clients;
import io.github.palmierisousa.domain.repository.ItemsOrder;
import io.github.palmierisousa.domain.repository.Orders;
import io.github.palmierisousa.domain.repository.Products;
import io.github.palmierisousa.exception.NotFoundException;
import io.github.palmierisousa.service.OrderService;
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
public class OrderServiceImpl implements OrderService {

    private final Orders ordersRepository;
    private final Clients clientsRepository;
    private final Products productsRepository;
    private final ItemsOrder itemsOrderRepository;

    @Override
    @Transactional
    public Order save(OrderDTO dto) {
        Integer clientId = dto.getClientId();
        Client client = clientsRepository
                .findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente inexistente: " + clientId));

        Order order = new Order();
        order.setTotal(dto.getTotal());
        order.setOrderDate(LocalDate.now());
        order.setClient(client);
        order.setStatus(OrderStatus.PLACED);

        List<OrderItem> orderItems = transformItems(order, dto.getItems());
        ordersRepository.save(order);
        itemsOrderRepository.saveAll(orderItems);
        order.setItems(orderItems);
        return order;
    }

    @Override
    public OrderInformationDTO getFullOrder(Integer id) {
        return ordersRepository.findByIdFetchItems(id)
                .map(this::transformOrder)
                .orElseThrow(() -> new NotFoundException("Pedido inexistente: " + id));
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, OrderStatus orderStatus) {
        ordersRepository.findById(id)
                .map(order -> {
                    order.setStatus(orderStatus);
                    return ordersRepository.save(order);
                }).orElseThrow(() -> new NotFoundException("Pedido inexistente: " + id));
    }

    private List<OrderItem> transformItems(Order order, List<OrderItemDTO> items) {
        if (items.isEmpty()) {
            throw new NotFoundException("Pedido sem itens: " + order.getId());
        }

        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduct();
                    Product product = productsRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "Produto inexistente: " + idProduto
                                    ));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setAmount(dto.getAmount());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    return orderItem;
                }).collect(Collectors.toList());
    }

    private OrderInformationDTO transformOrder(Order order) {
        return OrderInformationDTO
                .builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(order.getClient().getCpf())
                .clientName(order.getClient().getName())
                .total(order.getTotal())
                .status(order.getStatus().name())
                .items(transformItems(order.getItems()))
                .build();
    }

    private List<OrderItemInformationsDTO> transformItems(List<OrderItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(
                item -> OrderItemInformationsDTO
                        .builder().productDescription(item.getProduct().getDescription())
                        .unitPrice(item.getProduct().getUnit_price())
                        .amount(item.getAmount())
                        .build()
        ).collect(Collectors.toList());
    }
}