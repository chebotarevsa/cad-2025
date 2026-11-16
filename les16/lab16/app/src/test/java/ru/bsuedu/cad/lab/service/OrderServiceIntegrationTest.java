package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.config.TestAppConfig;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestAppConfig.class})
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Customer testCustomer;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        Category category = new Category("Food", "Animal food");
        category = categoryRepository.save(category);

        testCustomer = new Customer("Ivan Petrov", "ivan@mail.ru", "+79991234567", "Lenina st, 1");
        testCustomer = customerRepository.save(testCustomer);

        testProduct1 = new Product("Dog food", "Premium", category,
                new BigDecimal("1500.00"), 100, null);
        testProduct1 = productRepository.save(testProduct1);

        testProduct2 = new Product("Toy", "Rubber toy", category,
                new BigDecimal("300.00"), 50, null);
        testProduct2 = productRepository.save(testProduct2);
    }

    @Test
    void createOrder_Success() {
        List<OrderService.OrderItemRequest> items = Arrays.asList(
                new OrderService.OrderItemRequest(testProduct1.getId(), 2),
                new OrderService.OrderItemRequest(testProduct2.getId(), 3)
        );

        Order order = orderService.createOrder(testCustomer.getId(), items, testCustomer.getAddress());

        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo("NEW");
        assertThat(order.getOrderDetails()).hasSize(2);
    }

    @Test
    void createOrder_CustomerNotFound() {
        List<OrderService.OrderItemRequest> items = Arrays.asList(
                new OrderService.OrderItemRequest(testProduct1.getId(), 1)
        );

        assertThatThrownBy(() -> orderService.createOrder(9999L, items, "Address"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createOrder_ProductNotFound() {
        List<OrderService.OrderItemRequest> items = Arrays.asList(
                new OrderService.OrderItemRequest(9999L, 1)
        );

        assertThatThrownBy(() -> orderService.createOrder(testCustomer.getId(), items, "Address"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createOrder_InsufficientStock() {
        List<OrderService.OrderItemRequest> items = Arrays.asList(
                new OrderService.OrderItemRequest(testProduct1.getId(), 200)
        );

        assertThatThrownBy(() -> orderService.createOrder(testCustomer.getId(), items, "Address"))
                .isInstanceOf(RuntimeException.class);
    }
}
