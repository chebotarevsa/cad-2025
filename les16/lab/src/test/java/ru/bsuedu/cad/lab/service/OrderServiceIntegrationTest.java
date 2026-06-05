package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bsuedu.cad.lab.config.TestConfig;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

public class OrderServiceIntegrationTest {

    @Test
    void createOrder_persistsAndReducesStock() {
        try (var ctx = new AnnotationConfigApplicationContext(TestConfig.class)) {
            var customerRepo = ctx.getBean(CustomerRepository.class);
            var productRepo = ctx.getBean(ProductRepository.class);
            var orderService = ctx.getBean(OrderService.class);

            Customer c = new Customer();
            c.setName("Test");
            c.setEmail("test@example.com");
            customerRepo.save(c);

            Product p = new Product();
            p.setName("Toy");
            p.setPrice(new BigDecimal("3.00"));
            p.setStockQuantity(10);
            productRepo.save(p);

            var res = orderService.createOrder(c.getCustomerId(), Map.of(p.getProductId(), 4), "addr");

            assertNotNull(res.getOrderId());
            assertEquals(new BigDecimal("12.00"), res.getTotalPrice());

            var updated = productRepo.findById(p.getProductId()).orElseThrow();
            assertEquals(6, updated.getStockQuantity().intValue());
        }
    }
}
