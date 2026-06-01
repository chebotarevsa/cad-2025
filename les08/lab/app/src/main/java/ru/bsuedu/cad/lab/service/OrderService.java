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
        logger.info("Создан новый заказ для покупателя с ID: {}", customerId);

        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден с ID: " + customerId));

        // Create order with zero total initially
        Order order = new Order(customer, shippingAddress);
        order.setTotalPrice(BigDecimal.ZERO); // Начальная сумма
        Order savedOrder = orderRepository.save(order);
        logger.info("Создан заказ с ID: {}", savedOrder.getId());

        // Add items to order and calculate total
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден с ID: " + item.getProductId()));

            // Check stock
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Недостаточно товара на складе: " + product.getName());
            }

            // Create order detail
            OrderDetail detail = new OrderDetail(product, item.getQuantity());
            detail.setOrder(savedOrder);

            // Save detail
            orderDetailRepository.save(detail);
            savedOrder.addOrderDetail(detail);

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            // Calculate item total and add to order total
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);

            logger.debug("Added item: {} x {} for product: {} (item total: {})",
                    item.getQuantity(), product.getName(), product.getPrice(), itemTotal);
        }

        // Update order total
        savedOrder.setTotalPrice(totalPrice);

        // Save order with updated total
        Order finalOrder = orderRepository.save(savedOrder);

        logger.info("Заказ успешен. Итоговая цена: {}", finalOrder.getTotalPrice());
        return finalOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        logger.info("Ищем все заказы");
        List<Order> orders = orderRepository.findAll();
        logger.info("Найдено {} заказов", orders.size());
        return orders;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        logger.info("Ищем заказ с ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден с ID: " + orderId));
    }

    // Inner class for order item request
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public OrderItemRequest(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}