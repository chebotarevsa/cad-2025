package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

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
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(new Date());
        order.setStatus("NEW");
        order.setShippingAddress(customer.getAddress());

        List<OrderDetail> details = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Integer productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());
            details.add(detail);

            totalPrice = totalPrice.add(product.getPrice());
        }

        order.setOrderDetails(details);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(int orderId, int customerId, List<Integer> productIds) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            orderDetailRepository.deleteAll(order.getOrderDetails());

            order.setCustomer(customer);
            order.setShippingAddress(customer.getAddress());

            List<OrderDetail> details = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (Integer productId : productIds) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(1);
                detail.setPrice(product.getPrice());
                details.add(detail);

                totalPrice = totalPrice.add(product.getPrice());
            }

            order.setOrderDetails(details);
            order.setTotalPrice(totalPrice);

            return orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public boolean deleteOrder(int orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            orderDetailRepository.deleteAll(optionalOrder.get().getOrderDetails());
            orderRepository.delete(optionalOrder.get());
            return true;
        }
        return false;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }
}