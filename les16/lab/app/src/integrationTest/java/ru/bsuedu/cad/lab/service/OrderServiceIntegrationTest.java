package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.TestJpaConfig;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceIntegrationTest {
    private AnnotationConfigApplicationContext context;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(TestJpaConfig.class);
        orderService = context.getBean(OrderService.class);
        orderRepository = context.getBean(OrderRepository.class);
        customerRepository = context.getBean(CustomerRepository.class);
        productRepository = context.getBean(ProductRepository.class);
        categoryRepository = context.getBean(CategoryRepository.class);

        Category category = new Category();
        category.setId(1);
        category.setName("Товары для кошек");
        category.setDescription("Корм и игрушки");
        categoryRepository.save(category);

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Мария");
        customer.setEmail("maria@example.com");
        customer.setPhone("+79000000000");
        customer.setAddress("Белгород");
        customerRepository.save(customer);

        Product product = new Product();
        product.setId(10);
        product.setName("Корм");
        product.setDescription("Сухой корм");
        product.setCategory(category);
        product.setPrice(new BigDecimal("250.00"));
        product.setStockQuantity(15);
        product.setImageUrl("/img/cat-food.png");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void createOrderSavesOrderAndDetails() {
        Order order = orderService.createOrder(
                1,
                List.of(new OrderItem(10, 3)),
                "Белгород, ул. Садовая, 5"
        );

        assertNotNull(order.getId());
        assertEquals(1, orderRepository.count());

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals("NEW", savedOrder.getStatus());
        assertEquals("Белгород, ул. Садовая, 5", savedOrder.getShippingAddress());
        assertEquals(0, new BigDecimal("750.00").compareTo(savedOrder.getTotalPrice()));
        assertEquals(1, savedOrder.getDetails().size());
        assertEquals(10, savedOrder.getDetails().get(0).getProduct().getId());
        assertEquals(3, savedOrder.getDetails().get(0).getQuantity());
    }

    @Test
    void createOrderDoesNotSaveOrderWhenProductIsMissing() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1, List.of(new OrderItem(999, 1)), "Белгород")
        );

        assertEquals("Product not found: 999", exception.getMessage());
        assertTrue(orderRepository.findAll().isEmpty());
    }
}
