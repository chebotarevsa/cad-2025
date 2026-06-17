package lab1.service;

import lab1.entity.*;
import lab1.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Long customerId, List<OrderItemRequest> items, String shippingAddress) {
        log.info("Создание заказа для клиента ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        Order order = new Order(customer, "NEW", shippingAddress);

        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Недостаточно товара на складе");
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            OrderDetail detail = new OrderDetail(product, item.getQuantity(), product.getPrice());
            order.addOrderDetail(detail);

            log.info("Добавлен товар: {} x {}", product.getName(), item.getQuantity());
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Заказ создан. ID: {}, Сумма: {}", savedOrder.getOrderId(), savedOrder.getTotalPrice());

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}