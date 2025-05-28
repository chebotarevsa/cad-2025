package ru.bsuedu.cad.lab.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.LabApplication;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = LabApplication.class)
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCreateOrderIntegration() {
        Customer customer = new Customer();
        customer.setName("Анна");
        customer.setAddress("ул. Ленина, 45");
        customerRepository.save(customer);

        Product product = new Product();
        product.setName("Игрушка");
        product.setPrice(new BigDecimal("50.00"));
        productRepository.save(product);

        orderService.createOrder(customer, List.of(product));
    }
}
