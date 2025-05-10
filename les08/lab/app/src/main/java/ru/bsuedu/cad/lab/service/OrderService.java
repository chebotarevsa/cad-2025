package ru.bsuedu.cad.lab.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    // Метод создания заказа
    @Transactional
    public void createOrder(Customer customer, List<Product> products) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Оформлен");
        order.setShippingAddress(customer.getAddress());

        // Вычисляем общую цену
        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Сохраняем детали заказа
        int i = 0;
        for (Product product : products) {
            OrderDetail detail = new OrderDetail();
            detail.setId(order.getId() * 100 + i++); // простой способ задать уникальный id
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());
            orderDetailRepository.save(detail);
        }

        System.out.println("✅ Заказ создан: #" + order.getId() + " на сумму " + totalPrice);
    }
}
