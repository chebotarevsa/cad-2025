package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerOrderRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final CustomerOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(CustomerOrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public CustomerOrder createOrder(Integer customerId, Integer productId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + customerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше 0");
        }
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(customer.getAddress());
        order.setTotalPrice(linePrice);

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setPrice(product.getPrice());
        order.addDetail(detail);

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        CustomerOrder saved = orderRepository.save(order);
        LOGGER.info("Создан заказ #{} для клиента {} на сумму {}",
                saved.getOrderId(), customer.getName(), saved.getTotalPrice());
        return saved;
    }
}
