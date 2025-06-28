package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.config.TestJpaConfig;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJpaConfig.class, OrderService.class, CustomerService.class, ProductService.class})
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createOrder_shouldPersistOrderWithDetails() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer = customerRepository.save(customer);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product = productRepository.save(product);

        Order order = new Order();
        order.setCustomer(customer);

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(2);
        detail.setPrice(product.getPrice());
        order.setOrderDetails(List.of(detail));

        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getTotalPrice()).isEqualTo(BigDecimal.valueOf(200));

        Order foundOrder = orderRepository.findByIdWithDetails(savedOrder.getId()).orElseThrow();

        assertThat(foundOrder.getOrderDetails()).hasSize(1);
        assertThat(foundOrder.getOrderDetails().get(0).getProduct().getName()).isEqualTo("Test Product");
    }
}