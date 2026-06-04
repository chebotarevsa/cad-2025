package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.TestConfig;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
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

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(100, "Корма", "Корма для животных");
        categoryRepository.save(category);

        customer = new Customer(100, "Тест Тестов", "test@example.com", "+79001234567", "Москва");
        customerRepository.save(customer);

        product = new Product();
        product.setProductId(100L);
        product.setName("Корм тестовый");
        product.setCategory(category);
        product.setPrice(new BigDecimal("1000.00"));
        product.setStockQuantity(10);
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());
        productRepository.save(product);
    }

    // ─── createOrder ─────────────────────────────────────────────────────────

    @Test
    void createOrder_shouldPersistOrderInDatabase() {
        // Given / When
        Order created = orderService.createOrder(customer, List.of(product), "Москва, тест");

        // Then
        assertThat(created.getOrderId()).isNotNull();
        Optional<Order> found = orderService.findById(created.getOrderId());
        assertThat(found).isPresent();
        assertThat(found.get().getTotalPrice()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(found.get().getStatus()).isEqualTo("NEW");
    }

    @Test
    void createOrder_withMultipleProducts_shouldCalculateTotalCorrectly() {
        // Given
        Product product2 = new Product();
        product2.setProductId(101L);
        product2.setName("Игрушка");
        product2.setCategory(categoryRepository.getReferenceById(100));
        product2.setPrice(new BigDecimal("500.00"));
        product2.setStockQuantity(5);
        product2.setCreatedAt(LocalDate.now());
        product2.setUpdatedAt(LocalDate.now());
        productRepository.save(product2);

        // When
        Order created = orderService.createOrder(customer, List.of(product, product2), "Адрес");

        // Then
        assertThat(created.getTotalPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(created.getOrderDetails()).hasSize(2);
    }

    @Test
    void getAllOrders_shouldReturnCreatedOrders() {
        // Given
        orderService.createOrder(customer, List.of(product), "Адрес 1");
        orderService.createOrder(customer, List.of(), "Адрес 2");

        // When
        List<Order> orders = orderService.getAllOrders();

        // Then
        assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
    }

    // ─── updateOrder ─────────────────────────────────────────────────────────

    @Test
    void updateOrder_shouldPersistChangesInDatabase() {
        // Given
        Order created = orderService.createOrder(customer, List.of(product), "Старый адрес");

        // When
        orderService.updateOrder(created.getOrderId(), "SHIPPED", "Новый адрес");

        // Then
        Order found = orderService.findById(created.getOrderId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo("SHIPPED");
        assertThat(found.getShippingAddress()).isEqualTo("Новый адрес");
    }

    @Test
    void updateOrder_withNonExistentId_shouldThrowException() {
        // Given / When / Then
        assertThatThrownBy(() -> orderService.updateOrder(99999, "SHIPPED", "Адрес"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ─── deleteOrder ─────────────────────────────────────────────────────────

    @Test
    void deleteOrder_shouldRemoveOrderFromDatabase() {
        // Given
        Order created = orderService.createOrder(customer, List.of(), "Адрес");
        Integer id = created.getOrderId();
        assertThat(orderService.findById(id)).isPresent();

        // When
        orderService.deleteOrder(id);

        // Then
        assertThat(orderService.findById(id)).isEmpty();
    }
}
