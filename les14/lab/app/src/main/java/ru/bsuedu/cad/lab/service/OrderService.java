package ru.bsuedu.cad.lab.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                      OrderDetailRepository orderDetailRepository,
                      CustomerRepository customerRepository,
                      ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createOrder(int customerId, Map<Integer, Integer> productQuantities) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Создан");
        order.setShippingAddress(customer.getAddress());
        
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));

            if (product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException(
                    String.format("Недостаточно товара '%s'. Доступно: %d, запрошено: %d",
                        product.getName(), product.getStockQuantity(), quantity));
            }

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setPrice(product.getPrice());
            detail.setOrder(order);
            
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }
}