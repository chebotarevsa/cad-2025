package ru.bsuedu.cad.lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderRestController(OrderService orderService,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.getAllOrders().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Integer id) {
        return orderService.findById(id)
                .map(o -> ResponseEntity.ok(toResponse(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        List<Product> products = request.getProductIds() == null ? List.of() :
                request.getProductIds().stream()
                        .map(id -> productRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + id)))
                        .collect(Collectors.toList());

        Order saved = orderService.createOrder(customer, products, request.getShippingAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Integer id,
                                                @RequestBody OrderUpdateRequest request) {
        return orderService.findById(id)
                .map(o -> {
                    Order updated = orderService.updateOrder(id, request.getStatus(), request.getShippingAddress());
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (orderService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse r = new OrderResponse();
        r.setOrderId(order.getOrderId());
        r.setCustomerId(order.getCustomer().getCustomerId());
        r.setCustomerName(order.getCustomer().getName());
        r.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toLocalDate().toString() : null);
        r.setTotalPrice(order.getTotalPrice());
        r.setStatus(order.getStatus());
        r.setShippingAddress(order.getShippingAddress());
        return r;
    }
}
