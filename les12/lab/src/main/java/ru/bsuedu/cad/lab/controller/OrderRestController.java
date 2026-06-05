package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderEntity> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    public OrderEntity getOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PostMapping
    public OrderEntity createOrder(@RequestBody OrderEntity order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public OrderEntity updateOrder(@PathVariable Long id, @RequestBody OrderEntity order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}