package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.OrderDTO;
import io.github.palmierisousa.api.dto.OrderItemDTO;
import io.github.palmierisousa.domain.entity.Client;
import io.github.palmierisousa.domain.entity.Order;
import io.github.palmierisousa.domain.entity.OrderItem;
import io.github.palmierisousa.domain.entity.Product;
import io.github.palmierisousa.domain.enums.OrderStatus;
import io.github.palmierisousa.domain.repository.Clients;
import io.github.palmierisousa.domain.repository.ItemsOrder;
import io.github.palmierisousa.domain.repository.Orders;
import io.github.palmierisousa.domain.repository.Products;
import io.github.palmierisousa.exception.ElementNotFoundException;
import io.github.palmierisousa.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                .orElseThrow(() -> new ElementNotFoundException("Cliente inexistente: " + clientId));

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
    public Order getFullOrder(Integer id) {
        return ordersRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ElementNotFoundException("Pedido inexistente: " + id));
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, OrderStatus orderStatus) {
        ordersRepository.findById(id)
                .map(order -> {
                    order.setStatus(orderStatus);
                    return ordersRepository.save(order);
                }).orElseThrow(() -> new ElementNotFoundException("Pedido inexistente: " + id));
    }

    private List<OrderItem> transformItems(Order order, List<OrderItemDTO> items) {
        if (items.isEmpty()) {
            throw new ElementNotFoundException("Pedido sem itens: " + order.getId());
        }

        return items
                .stream()
                .map(dto -> {
                    Integer productCode = dto.getProduct();
                    Product product = productsRepository
                            .findByCode(productCode)
                            .orElseThrow(
                                    () -> new ElementNotFoundException(
                                            "Produto inexistente: " + productCode
                                    ));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setAmount(dto.getAmount());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    return orderItem;
                }).collect(Collectors.toList());
    }
}
