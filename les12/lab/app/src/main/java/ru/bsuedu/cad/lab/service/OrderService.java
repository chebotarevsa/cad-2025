package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.dto.OrderRequestDto;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                        CustomerService customerService,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findByIdWithDetails(id);
    }

    @Transactional
    public Order createOrder(Order order) {
        recalculateTotalPrice(order);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }

        if (order.getStatus() == null) {
            order.setStatus("NEW");
        }

        if (order.getOrderDetails() != null) {
            order.getOrderDetails().forEach(detail -> detail.setOrder(order));
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrderFromRequest(OrderRequestDto request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        List<OrderDetail> details = request.getItems().stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

                    OrderDetail detail = new OrderDetail();
                    detail.setProduct(product);
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(product.getPrice());
                    return detail;
                })
                .collect(Collectors.toList());

        order.setOrderDetails(details);
        return createOrder(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        recalculateTotalPrice(order);
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private void recalculateTotalPrice(Order order) {
        if (order.getOrderDetails() != null) {
            BigDecimal totalPrice = order.getOrderDetails().stream()
                    .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalPrice(totalPrice);
        } else {
            order.setTotalPrice(BigDecimal.ZERO);
        }
    }
}