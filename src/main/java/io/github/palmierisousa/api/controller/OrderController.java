package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.OrderDTO;
import io.github.palmierisousa.api.dto.OrderInformationDTO;
import io.github.palmierisousa.api.dto.OrderStatusDTO;
import io.github.palmierisousa.domain.entity.Order;
import io.github.palmierisousa.domain.enums.OrderStatus;
import io.github.palmierisousa.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return service.getFullOrder(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody OrderStatusDTO status) {
        service.updateStatus(id, OrderStatus.valueOf(status.getNewStatus()));
    }
}
