package ru.bsuedu.cad.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Transactional
    public Order createOrder(int customerId, List<Integer> productIds) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        order.setShippingAddress(customer.getAddress());

        BigDecimal total = BigDecimal.ZERO;
        for (Integer productId : productIds) {
            Product product = productRepository.findById(productId).orElseThrow();
            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());
            detail.setOrder(order);
            total = total.add(product.getPrice());
            order.getOrderDetails().add(detail);
        }

        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
