package ru.bsuedu.cad.lab.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderItem;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderItemRepository;
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
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(Long customerId, List<OrderItemData> items) {
        // Находим клиента
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        // Создаём новый заказ
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        Order savedOrder = orderRepository.save(order);
        System.out.println("Создан заказ #" + savedOrder.getId() + " для клиента: " + customer.getFullName());

        // Добавляем позиции в заказ
        double total = 0.0;
        for (OrderItemData itemData : items) {
            Product product = productRepository.findById(itemData.productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemData.productId));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(itemData.quantity);
            item.setPrice(product.getPrice());

            orderItemRepository.save(item);
            total += product.getPrice() * itemData.quantity;
            
            System.out.println("  + " + product.getName() + " x" + itemData.quantity + " = " + (product.getPrice() * itemData.quantity) + " руб.");
        }
        
        System.out.println("Общая сумма заказа #" + savedOrder.getId() + ": " + total + " руб.");
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Вспомогательный класс для передачи данных о позициях заказа
    public static class OrderItemData {
        public Long productId;
        public int quantity;

        public OrderItemData(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}