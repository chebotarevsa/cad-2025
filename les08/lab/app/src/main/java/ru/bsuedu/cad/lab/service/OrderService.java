package ru.bsuedu.cad.lab.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Customer customer, List<OrderDetail> orderDetails, String shippingAddress) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress);

        BigDecimal totalPrice = orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetails);

        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        logger.info("Создан новый заказ с ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}