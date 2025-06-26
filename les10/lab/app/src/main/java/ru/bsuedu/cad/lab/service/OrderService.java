package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        List<Order> orderList = new ArrayList<>();
        for (Order i : orderRepository.findAll()) {
            orderList.add(i);
        }
        return orderList;
    }

    @Transactional
    public Order createOrder(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        BigDecimal price = product.getPrice();
        BigDecimal totalPrice = price.multiply(new BigDecimal(quantity));

        List<OrderDetail> listOrderDetail = new ArrayList<>();

        Date orderDate = Date.valueOf(LocalDate.of(2025, 3, 21));

        Order order = new Order(null, orderDate, totalPrice, "New", "Belgorod", listOrderDetail, customer);

        OrderDetail orderDetail = new OrderDetail(null, quantity, price, order, product);

        listOrderDetail.add(orderDetail);

        orderRepository.save(order);

        return order;
    }
}
