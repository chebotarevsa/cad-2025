package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.List;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderEntity createOrder(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId);
        Product product = productRepository.findById(productId);

        OrderItem item = new OrderItem(product, quantity);
        OrderEntity order = new OrderEntity(customer, List.of(item));

        orderRepository.save(order);

        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> findAllOrders() {
        return orderRepository.findAll();
    }
}