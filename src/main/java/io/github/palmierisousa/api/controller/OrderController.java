package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.OrderDTO;
import io.github.palmierisousa.api.dto.OrderInformationDTO;
import io.github.palmierisousa.api.dto.OrderItemInformationsDTO;
import io.github.palmierisousa.api.dto.OrderStatusDTO;
import io.github.palmierisousa.domain.entity.Order;
import io.github.palmierisousa.domain.entity.OrderItem;
import io.github.palmierisousa.domain.enums.OrderStatus;
import io.github.palmierisousa.service.OrderService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid OrderDTO dto) {
        Order order = service.save(dto);
        return order.getId();
    }

    @GetMapping("{id}")
    public OrderInformationDTO getById(@PathVariable Integer id) {
        Order order = service.getFullOrder(id);

        return transformOrder(order);
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable("id") Integer orderId, @RequestBody OrderStatusDTO status) {
        service.updateStatus(orderId, OrderStatus.valueOf(status.getNewStatus()));
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
                        .unitPrice(item.getProduct().getUnitPrice())
                        .amount(item.getAmount())
                        .build()
        ).collect(Collectors.toList());
    }
}
