package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderEntity createOrder(Long customerId, Map<Long, Integer> productQuantities, String shippingAddress) {
        var customer = customerRepository.findById(customerId).orElseThrow();

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress);

        List<OrderDetail> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (var entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer qty = entry.getValue();
            var product = productRepository.findById(productId).orElseThrow();
            OrderDetail od = new OrderDetail();
            od.setProduct(product);
            od.setQuantity(qty);
            od.setPrice(product.getPrice());
            od.setOrder(order);
            details.add(od);

            // decrease stock if available
            if (product.getStockQuantity() != null) {
                product.setStockQuantity(product.getStockQuantity() - qty);
                productRepository.save(product);
            }

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        order.setOrderDetails(details);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }

    public java.util.List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }

    public OrderEntity findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Transactional
    public OrderEntity updateOrder(Long orderId, String status, String shippingAddress) {
        OrderEntity order = findById(orderId);
        if (status != null && !status.isBlank()) {
            order.setStatus(status);
        }
        if (shippingAddress != null) {
            order.setShippingAddress(shippingAddress);
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
