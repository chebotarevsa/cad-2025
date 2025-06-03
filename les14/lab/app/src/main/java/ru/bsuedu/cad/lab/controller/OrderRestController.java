package ru.bsuedu.cad.lab.controller;

import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderRepository orderRepository;

    public OrderRestController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<CustomerOrder> allOrders() {
        return orderRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        orderRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrder> updateOrder(
            @PathVariable("id") Integer id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String shippingAddress,
            @RequestParam(required = false) BigDecimal totalPrice
    ) {
        return orderRepository.findById(id).map(order -> {
            if (status != null) order.setStatus(status);
            if (shippingAddress != null) order.setShippingAddress(shippingAddress);
            if (totalPrice != null) order.setTotalPrice(totalPrice);
            CustomerOrder saved = orderRepository.save(order);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
