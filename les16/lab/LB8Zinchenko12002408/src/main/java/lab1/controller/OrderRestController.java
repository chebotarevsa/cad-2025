package lab1.controller;

import lab1.entity.Order;
import lab1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getAllOrders().stream()
                .filter(o -> o.getOrderId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(request.getProductId()); // теперь работает
        item.setQuantity(request.getQuantity());   // теперь работает

        return orderService.createOrder(
                request.getCustomerId(),           // теперь работает
                List.of(item),
                request.getShippingAddress()       // теперь работает
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updateRequest) {
        Order updated = orderService.updateOrder(
                id,
                updateRequest.getStatus(),
                updateRequest.getShippingAddress()
        );
        return ResponseEntity.ok(updated);
    }
    // --- ИСПРАВЛЕННЫЙ DTO ---
    public static class CreateOrderRequest {
        private Long customerId;
        private Long productId;
        private int quantity;
        private String shippingAddress;

        // Геттеры и сеттеры, чтобы Jackson мог их прочитать из JSON,
        // а контроллер — вызвать методы
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    }
}