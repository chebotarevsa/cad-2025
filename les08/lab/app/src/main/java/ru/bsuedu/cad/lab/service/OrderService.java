package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import jakarta.persistence.Transient;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class OrderService {
    final private OrderRepository orderRepository;
    final private CustomerRepository customerRepository;
    final private ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            orders.add(order);
        }
        return orders;
    }

    public List<Order> getOrdersByGuava(){
       return Lists.newArrayList(orderRepository.findAll());
    }

    @Transient
    public Order createOrder(int customerId, int productId, int quantity, BigDecimal price){
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        Date orderDate = new Date(2024,03,22);
        BigDecimal totalPrice = price.multiply(new BigDecimal (quantity));
        Order newOrder = new Order(orderDate, totalPrice, "новый заказ", "Белгород", customer);
        OrderDetail orderItem = new OrderDetail(quantity, totalPrice, newOrder, product);
        List<OrderDetail> items = new ArrayList<>();
        items.add(orderItem);
        newOrder.setOrderDetails(items);
        return orderRepository.save(newOrder);
    }
    
}
