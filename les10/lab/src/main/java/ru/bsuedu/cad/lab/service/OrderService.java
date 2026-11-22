package ru.bsuedu.cad.lab.service;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderItemRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public OrderEntity createOrder(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId);
        Product product = productRepository.findById(productId);

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);

        orderRepository.save(order);

        OrderItem item = new OrderItem(product, quantity);
        item.setOrder(order);

        orderItemRepository.save(item);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        return order;
    }

    public List<OrderEntity> findAllOrders() {
        return orderRepository.findAll();
    }
}