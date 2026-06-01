package ru.bsuedu.cad.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

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
    public Order createOrder(Long customerId, List<OrderItemRequest> items, String shippingAddress) {
        logger.info("Создание нового заказа для покупателя с ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден с ID: " + customerId));

        Order order = new Order(customer, shippingAddress);
        Order savedOrder = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден с ID: " + item.getProductId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Недостаточно товара на складе: " + product.getName());
            }

            OrderDetail detail = new OrderDetail(product, item.getQuantity());
            detail.setOrder(savedOrder);
            orderDetailRepository.save(detail);
            savedOrder.addOrderDetail(detail);

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        savedOrder.setTotalPrice(totalPrice);
        Order finalOrder = orderRepository.save(savedOrder);

        logger.info("Заказ успешно создан. Итоговая цена: {}", finalOrder.getTotalPrice());
        return finalOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        logger.info("Поиск всех заказов");
        List<Order> orders = orderRepository.findAll();
        logger.info("Найдено {} заказов", orders.size());
        return orders;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        logger.info("Поиск заказа с ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден с ID: " + orderId));
    }

    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public OrderItemRequest(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() { return productId; }
        public Integer getQuantity() { return quantity; }
    }
}