package ru.bsuedu.cad.lab.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderRepository orderRepository;

    public OrderRestController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Постраничный вывод заказов
    @GetMapping
    public Page<CustomerOrder> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return orderRepository.findAll(pageable);
    }

    // Получение конкретного заказа
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Integer id) {
        Optional<CustomerOrder> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Создание заказа (остается как в предыдущем примере)
    @PostMapping
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CustomerOrder order) {
        CustomerOrder savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Полное обновление заказа
    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrder> updateOrder(
            @PathVariable Integer id,
            @RequestBody CustomerOrder orderDetails) {
        
        return orderRepository.findById(id)
            .map(existingOrder -> {
                existingOrder.setStatus(orderDetails.getStatus());
                existingOrder.setShippingAddress(orderDetails.getShippingAddress());
                existingOrder.setTotalPrice(orderDetails.getTotalPrice());
                // Можно обновлять и другие поля по необходимости
                
                CustomerOrder updatedOrder = orderRepository.save(existingOrder);
                return ResponseEntity.ok(updatedOrder);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Частичное обновление (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerOrder> partialUpdateOrder(
            @PathVariable Integer id,
            @RequestBody CustomerOrder partialUpdates) {
        
        return orderRepository.findById(id)
            .map(existingOrder -> {
                if (partialUpdates.getStatus() != null) {
                    existingOrder.setStatus(partialUpdates.getStatus());
                }
                if (partialUpdates.getShippingAddress() != null) {
                    existingOrder.setShippingAddress(partialUpdates.getShippingAddress());
                }
                if (partialUpdates.getTotalPrice() != null) {
                    existingOrder.setTotalPrice(partialUpdates.getTotalPrice());
                }
                
                CustomerOrder updatedOrder = orderRepository.save(existingOrder);
                return ResponseEntity.ok(updatedOrder);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Удаление заказа
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}