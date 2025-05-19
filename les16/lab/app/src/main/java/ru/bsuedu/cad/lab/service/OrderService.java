package ru.bsuedu.cad.lab.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public void createOrder(Customer customer, List<Product> products) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Оформлен");
        order.setShippingAddress(customer.getAddress());

        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        int i = 0;
        for (Product product : products) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());
            orderDetailRepository.save(detail);
        }
    }
}
