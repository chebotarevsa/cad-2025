package ru.bsuedu.cad.lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.model.OrderRequest;
import ru.bsuedu.cad.lab.model.OrderResponse;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderService.findAllOrders().stream()
                .map(OrderResponse::fromOrder)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable("id") Integer id) {
        return OrderResponse.fromOrder(orderService.findOrder(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(
                request.getCustomerId(),
                request.getItems(),
                request.getShippingAddress()
        );
        return OrderResponse.fromOrder(order);
    }

    @PutMapping("/{id}")
    public OrderResponse update(@PathVariable("id") Integer id, @RequestBody OrderRequest request) {
        String status = request.getStatus() == null ? "NEW" : request.getStatus();
        Order order = orderService.updateOrder(
                id,
                request.getCustomerId(),
                request.getItems(),
                request.getShippingAddress(),
                status
        );
        return OrderResponse.fromOrder(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        orderService.deleteOrder(id);
    }
}
