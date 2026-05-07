package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ========== WEB UI (Thymeleaf) ==========

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "order-form";
    }

    @PostMapping("/new")
    public String createOrder(@RequestParam Long customerId,
                              @RequestParam(required = false) List<Long> productIds,
                              @RequestParam(required = false) List<Integer> quantities) {
        if (productIds != null && quantities != null && productIds.size() == quantities.size()) {
            List<OrderService.OrderItemData> items = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                items.add(new OrderService.OrderItemData(productIds.get(i), quantities.get(i)));
            }
            orderService.createOrder(customerId, items);
        }
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.getAllOrders().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst().orElse(null);
        model.addAttribute("order", order);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "order-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id,
                              @RequestParam Long customerId,
                              @RequestParam String status) {
        orderService.updateOrder(id, customerId, status);
        return "redirect:/orders";
    }

    // ========== REST API ==========

    @RestController
    @RequestMapping("/api/orders")
    public static class OrderRestController {

        @Autowired
        private OrderService orderService;

        @GetMapping
        public List<Order> getAllOrders() {
            return orderService.getAllOrders();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
            return orderService.getAllOrders().stream()
                    .filter(o -> o.getId().equals(id))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
            List<OrderService.OrderItemData> items = request.items.stream()
                    .map(i -> new OrderService.OrderItemData(i.productId, i.quantity))
                    .collect(Collectors.toList());
            Order order = orderService.createOrder(request.customerId, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest request) {
            orderService.updateOrder(id, request.customerId, request.status);
            Order updatedOrder = orderService.getAllOrders().stream()
                    .filter(o -> o.getId().equals(id))
                    .findFirst().orElse(null);
            return ResponseEntity.ok(updatedOrder);
        }
    }

    // Вспомогательные классы для REST API
    static class CreateOrderRequest {
        public Long customerId;
        public List<Item> items;
    }

    static class Item {
        public Long productId;
        public int quantity;
    }

    static class UpdateOrderRequest {
        public Long customerId;
        public String status;
    }
}