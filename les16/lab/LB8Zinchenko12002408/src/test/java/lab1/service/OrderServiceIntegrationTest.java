package lab1.service;

import lab1.entity.Customer;
import lab1.entity.Order;
import lab1.entity.Product;
import lab1.repository.CustomerRepository;
import lab1.repository.OrderRepository;
import lab1.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = lab1.app.OrderApplication.class)
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer savedCustomer;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        savedCustomer = customerRepository.save(new Customer("Тест Клиент", "test@mail.ru", "111", "Адрес"));
        savedProduct = productRepository.save(new Product("Тест Товар", "Описание", new BigDecimal("50.00"), 100));
    }

    @Test
    void createOrder_Integration_Success() {
        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(savedProduct.getProductId());
        item.setQuantity(3);

        Order createdOrder = orderService.createOrder(
                savedCustomer.getCustomerId(),
                List.of(item),
                "Интеграционный адрес"
        );

        Assertions.assertNotNull(createdOrder.getOrderId());
        Assertions.assertTrue(orderRepository.findById(createdOrder.getOrderId()).isPresent());

        Product updatedProduct = productRepository.findById(savedProduct.getProductId()).orElseThrow();
        Assertions.assertEquals(97, updatedProduct.getStockQuantity());
        Assertions.assertEquals(new BigDecimal("150.00"), createdOrder.getTotalPrice());
    }

    @Test
    void createOrder_Integration_Fail_StockNotEnough() {
        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(savedProduct.getProductId());
        item.setQuantity(1000);

        Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(savedCustomer.getCustomerId(), List.of(item), "Адрес");
        });

        Product productInDb = productRepository.findById(savedProduct.getProductId()).orElseThrow();
        Assertions.assertEquals(100, productInDb.getStockQuantity());
        Assertions.assertEquals(0, orderRepository.count());
    }
}