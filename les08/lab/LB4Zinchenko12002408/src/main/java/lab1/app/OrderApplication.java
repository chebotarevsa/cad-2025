package lab1.app;

import lab1.service.DataInitializationService;
import lab1.service.OrderService;
import lab1.entity.Product;
import lab1.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "lab1")
@EntityScan(basePackages = "lab1.entity")
@EnableJpaRepositories(basePackages = "lab1.repository")
public class OrderApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

    private final DataInitializationService dataInitService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    public OrderApplication(DataInitializationService dataInitService,
                            OrderService orderService,
                            ProductRepository productRepository) {
        this.dataInitService = dataInitService;
        this.orderService = orderService;
        this.productRepository = productRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("=== ЗАПУСК ПРИЛОЖЕНИЯ ===");

        dataInitService.initializeData();

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.error("Нет продуктов в базе данных!");
            return;
        }

        createNewOrder(products);
        displayAllOrders();

        log.info("=== ПРИЛОЖЕНИЕ ЗАВЕРШИЛО РАБОТУ ===");
    }

    private void createNewOrder(List<Product> products) {
        log.info("=== СОЗДАНИЕ НОВОГО ЗАКАЗА ===");

        OrderService.OrderItemRequest item1 = new OrderService.OrderItemRequest();
        item1.setProductId(products.get(0).getProductId());
        item1.setQuantity(2);

        OrderService.OrderItemRequest item2 = new OrderService.OrderItemRequest();
        item2.setProductId(products.get(1).getProductId());
        item2.setQuantity(1);

        try {
            orderService.createOrder(1L, Arrays.asList(item1, item2), "г. Москва, ул. Примерная, д. 1");
            log.info("✅ Заказ успешно создан");
        } catch (Exception e) {
            log.error("❌ Ошибка: {}", e.getMessage());
        }
    }

    private void displayAllOrders() {
        log.info("=== ВСЕ ЗАКАЗЫ ===");
        var orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            log.info("Заказов нет");
        } else {
            orders.forEach(order -> {
                log.info("Заказ #{}: Клиент: {}, Сумма: {}",
                        order.getOrderId(),
                        order.getCustomer().getName(),
                        order.getTotalPrice());
            });
        }
    }
}