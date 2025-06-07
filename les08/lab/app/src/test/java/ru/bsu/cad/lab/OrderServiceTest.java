package ru.bsu.cad.lab;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import ru.bsu.cad.lab.entity.*;
import ru.bsu.cad.lab.entity.Order;
import ru.bsu.cad.lab.repository.*;
import ru.bsu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private OrderService orderService;
    private CustomerRepository customerRepo;
    private ProductRepository productRepo;

    @BeforeAll
    static void initFactory() {
        emf = Persistence.createEntityManagerFactory("lab-unit");
    }

    @BeforeEach
    void init() {
        em = emf.createEntityManager();
        customerRepo = new CustomerRepository(em);
        productRepo = new ProductRepository(em);
        orderService = new OrderService(em);

        // Предзаполняем данные
        em.getTransaction().begin();

        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customer.setAddress("Test Address");
        customerRepo.save(customer);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(10);
        product.setImageUrl("img.png");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);

        em.getTransaction().commit();
    }

    @Test
    void testCreateOrder() {
        Customer customer = customerRepo.findAll().get(0);
        Product product = productRepo.findAll().get(0);

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress("Test Address");

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(1);
        detail.setPrice(product.getPrice());

        order.setOrderDetails(List.of(detail));
        order.setTotalPrice(product.getPrice());

        orderService.createOrder(order);

        assertNotNull(order.getOrderId());
        assertEquals("NEW", order.getStatus());
        assertEquals(customer.getCustomerId(), order.getCustomer().getCustomerId());
    }

    @AfterEach
    void closeEm() {
        if (em != null && em.isOpen()) em.close();
    }

    @AfterAll
    static void closeFactory() {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
