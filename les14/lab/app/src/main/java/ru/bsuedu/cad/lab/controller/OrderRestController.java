package ru.bsuedu.cad.lab.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.dto.CreateOrderRequest;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        logger.info("REST: получить все заказы");
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Order getOrderById(@PathVariable Long id) {
        logger.info("REST: получить заказ {}", id);
        return orderService.getOrderById(id);
    }

    @PostMapping
    @Transactional
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        logger.info("REST: создать заказ");

        List<OrderService.OrderItemRequest> items = request.getItems().stream()
                .map(i -> new OrderService.OrderItemRequest(i.getProductId(), i.getQuantity()))
                .toList();

        return orderService.createOrder(
                request.getCustomerId(),
                items,
                request.getShippingAddress()
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deleteOrder(@PathVariable Long id) {
        logger.info("REST: удалить заказ {}", id);
        orderService.deleteOrder(id);
        return "Order deleted";
    }

    @PutMapping("/{id}")
    @Transactional
    public Order updateOrder(@PathVariable Long id,
                             @RequestParam String status) {

        logger.info("REST: обновить заказ {}", id);
        return orderService.updateOrderStatus(id, status);
    }
}