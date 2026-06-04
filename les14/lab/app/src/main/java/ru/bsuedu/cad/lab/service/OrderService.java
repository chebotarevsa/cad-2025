package ru.bsuedu.cad.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Customer customer, List<Product> products, String shippingAddress) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderDetail> details = new ArrayList<>();
        for (Product p : products) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(p);
            detail.setQuantity(1);
            detail.setPrice(p.getPrice());
            details.add(detail);
            total = total.add(p.getPrice());
        }
        order.setTotalPrice(total);
        order.setOrderDetails(details);

        Order saved = orderRepository.save(order);
        LOGGER.info("Создан заказ #{} для клиента '{}', сумма: {} руб., товаров: {}",
            saved.getOrderId(), customer.getName(), saved.getTotalPrice(), products.size());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order updateOrder(Integer id, String status, String shippingAddress) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + id));
        order.setStatus(status);
        order.setShippingAddress(shippingAddress);
        Order saved = orderRepository.save(order);
        LOGGER.info("Обновлён заказ #{}: статус={}, адрес={}", id, status, shippingAddress);
        return saved;
    }

    @Transactional
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
        LOGGER.info("Удалён заказ #{}", id);
    }
}
