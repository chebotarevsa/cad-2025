package ru.bsuedu.cad.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public OrderEntity createOrder(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId);
        Product product = productRepository.findById(productId);

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(new ArrayList<>());

        orderRepository.save(order);
        return order;
    }

    public OrderEntity createOrder(OrderEntity order) {
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }

    public List<OrderEntity> findAllOrders() {
        return orderRepository.findAll();
    }

    public OrderEntity findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderEntity updateOrder(Long id, OrderEntity order) {
        OrderEntity existing = orderRepository.findById(id);
        existing.setCustomer(order.getCustomer());
        orderRepository.save(existing);
        return existing;
    }

    public void deleteOrder(Long id) {
        OrderEntity order = orderRepository.findById(id);
        orderRepository.delete(order);
    }
}