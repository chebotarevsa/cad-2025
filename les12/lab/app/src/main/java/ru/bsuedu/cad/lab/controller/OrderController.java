package ru.bsuedu.cad.lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.dto.*;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.service.CustomerService;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequest) {
        try {
            Order order = convertToEntity(orderRequest);
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateDto updateRequest) {
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    updateEntity(existingOrder, updateRequest);
                    Order updatedOrder = orderService.updateOrder(existingOrder);
                    return ResponseEntity.ok(convertToDto(updatedOrder));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setShippingAddress(order.getShippingAddress());

        if (order.getCustomer() != null) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(order.getCustomer().getId());
            customerDto.setName(order.getCustomer().getName());
            customerDto.setEmail(order.getCustomer().getEmail());
            customerDto.setPhone(order.getCustomer().getPhone());
            customerDto.setAddress(order.getCustomer().getAddress());
            dto.setCustomer(customerDto);
        }

        if (order.getOrderDetails() != null) {
            List<OrderDetailDto> detailDtos = order.getOrderDetails().stream()
                    .map(this::convertDetailToDto)
                    .collect(Collectors.toList());
            dto.setOrderDetails(detailDtos);
        }

        return dto;
    }

    private OrderDetailDto convertDetailToDto(OrderDetail detail) {
        OrderDetailDto dto = new OrderDetailDto();
        dto.setId(detail.getId());
        dto.setQuantity(detail.getQuantity());
        dto.setPrice(detail.getPrice());

        if (detail.getProduct() != null) {
            ProductDto productDto = new ProductDto();
            productDto.setId(detail.getProduct().getId());
            productDto.setName(detail.getProduct().getName());
            productDto.setDescription(detail.getProduct().getDescription());
            productDto.setPrice(detail.getProduct().getPrice());
            dto.setProduct(productDto);
        }

        return dto;
    }

    private Order convertToEntity(OrderRequestDto request) {
        Order order = new Order();
        Customer customer = customerService.getCustomerById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Покупатель не найден"));
        order.setCustomer(customer);

        List<OrderDetail> details = request.getItems().stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

                    OrderDetail detail = new OrderDetail();
                    detail.setProduct(product);
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(product.getPrice());
                    return detail;
                })
                .collect(Collectors.toList());

        order.setOrderDetails(details);
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        return order;
    }

    private void updateEntity(Order order, OrderUpdateDto update) {
        order.setStatus(update.getStatus());
        order.setShippingAddress(update.getShippingAddress());
    }
}