package ru.bsuedu.cad.lab.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.web.dto.OrderItemDto;
import ru.bsuedu.cad.lab.web.dto.OrderRequest;
import ru.bsuedu.cad.lab.web.dto.OrderResponse;
import ru.bsuedu.cad.lab.web.dto.OrderUpdateRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderRestController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public List<OrderResponse> listOrders() {
        return orderService.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        OrderEntity order = orderService.createOrder(request.getCustomerId(), request.getProductQuantities(), request.getShippingAddress());
        return toResponse(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponse updateOrder(@PathVariable Long id, @RequestBody OrderUpdateRequest request) {
        try {
            OrderEntity order = orderService.updateOrder(id, request.getStatus(), request.getShippingAddress());
            return toResponse(order);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found", e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    private OrderResponse toResponse(OrderEntity order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setCustomerId(order.getCustomer() != null ? order.getCustomer().getCustomerId() : null);
        response.setCustomerName(order.getCustomer() != null ? order.getCustomer().getName() : null);
        response.setOrderDate(order.getOrderDate());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setItems(order.getOrderDetails() != null ? order.getOrderDetails().stream().map(this::toItemDto).collect(Collectors.toList()) : List.of());
        return response;
    }

    private OrderItemDto toItemDto(OrderDetail detail) {
        OrderItemDto item = new OrderItemDto();
        item.setProductId(detail.getProduct() != null ? detail.getProduct().getProductId() : null);
        item.setProductName(detail.getProduct() != null ? detail.getProduct().getName() : null);
        item.setQuantity(detail.getQuantity());
        item.setPrice(detail.getPrice());
        return item;
    }
}
