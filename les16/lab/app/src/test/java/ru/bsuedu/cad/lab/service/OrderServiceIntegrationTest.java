package ru.bsuedu.cad.lab.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.config.IntegrationTestConfig;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.ShopOrderRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Корма");
        category.setDescription("Корма для животных");
        categoryRepository.save(category);

        Product product = new Product();
        product.setProductId(1L);
        product.setName("Сухой корм");
        product.setDescription("Корм для собак");
        product.setCategory(category);
        product.setPrice(new BigDecimal("1500.00"));
        product.setStockQuantity(50);
        product.setCreatedAt(java.time.LocalDateTime.now());
        product.setUpdatedAt(java.time.LocalDateTime.now());
        productRepository.save(product);

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setName("Алексей Иванов");
        customer.setEmail("test@example.com");
        customerRepository.save(customer);
    }

    @Test
    void createOrder_success_persistedInDatabase() {
        ShopOrder created = orderService.createOrder(
                1, "Москва, ул. Ленина, 10", List.of(new OrderLineRequest(1L, 2)));

        assertThat(created.getOrderId()).isNotNull();
        assertThat(shopOrderRepository.findById(created.getOrderId())).isPresent();
        assertThat(created.getTotalPrice()).isEqualByComparingTo(new BigDecimal("3000.00"));
        assertThat(created.getOrderDetails()).hasSize(1);
    }

    @Test
    void createOrder_customerNotFound() {
        assertThatThrownBy(() -> orderService.createOrder(
                        99, "Адрес", List.of(new OrderLineRequest(1L, 1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer not found");

        assertThat(shopOrderRepository.findAll()).isEmpty();
    }

    @Test
    void createOrder_productNotFound() {
        assertThatThrownBy(() -> orderService.createOrder(
                        1, "Адрес", List.of(new OrderLineRequest(99L, 1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found");

        assertThat(shopOrderRepository.findAll()).isEmpty();
    }
}
