package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.DataLoaderService;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderApplication {
    private static final Logger logger = LoggerFactory.getLogger(OrderApplication.class);

    public static void main(String[] args) {
        logger.info("==========================================");
        logger.info("Запуск приложения");
        logger.info("==========================================");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            DataLoaderService dataLoaderService = context.getBean(DataLoaderService.class);
            OrderService orderService = context.getBean(OrderService.class);
            CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);
            CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
            ProductRepository productRepository = context.getBean(ProductRepository.class);
            OrderRepository orderRepository = context.getBean(OrderRepository.class);

            logger.info("Загрузка начальных данных из CSV файлов...");
            dataLoaderService.loadAllData();

            logger.info("==========================================");
            logger.info("Загруженные данные:");
            logger.info("==========================================");

            List<Category> categories = categoryRepository.findAll();
            logger.info("Категории ({}):", categories.size());
            for (Category c : categories) {
                logger.info("  {} - {}", c.getId(), c.getName());
            }

            List<Customer> customers = customerRepository.findAll();
            logger.info("Клиенты ({}):", customers.size());
            for (Customer c : customers) {
                logger.info("  {} - {} ({})", c.getId(), c.getName(), c.getEmail());
            }

            List<Product> products = productRepository.findAll();
            logger.info("Товары ({}):", products.size());
            for (Product p : products) {
                logger.info("  {} - {} ({} руб.) - остаток: {}",
                        p.getId(), p.getName(), p.getPrice(), p.getStockQuantity());
            }

            logger.info("==========================================");
            logger.info("Создание нового заказа:");
            logger.info("==========================================");

            Customer customer = customers.get(0);
            logger.info("Клиент: {} ({})", customer.getName(), customer.getEmail());
            logger.info("Адрес доставки: {}", customer.getAddress());

            List<OrderService.OrderItemRequest> items = Arrays.asList(
                    new OrderService.OrderItemRequest(products.get(0).getId(), 1),
                    new OrderService.OrderItemRequest(products.get(1).getId(), 2),
                    new OrderService.OrderItemRequest(products.get(2).getId(), 1)
            );

            logger.info("Состав заказа:");
            for (OrderService.OrderItemRequest item : items) {
                Product p = findProductById(products, item.getProductId());
                BigDecimal itemTotal = p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                logger.info("  - {}: {} x {} руб. = {} руб.",
                        p.getName(),
                        item.getQuantity(),
                        p.getPrice(),
                        itemTotal);
            }

            Order newOrder = orderService.createOrder(
                    customer.getId(),
                    items,
                    customer.getAddress()
            );

            logger.info("==========================================");
            logger.info("Заказ успешно создан!");
            logger.info("==========================================");
            logger.info("Номер заказа: {}", newOrder.getId());
            logger.info("Дата заказа: {}", newOrder.getOrderDate());
            logger.info("Статус: {}", newOrder.getStatus());
            logger.info("Общая сумма: {} руб.", newOrder.getTotalPrice());
            logger.info("Клиент: {}", newOrder.getCustomer().getName());
            logger.info("Адрес доставки: {}", newOrder.getShippingAddress());
            logger.info("Детали заказа:");

            newOrder.getOrderDetails().size();
            for (var detail : newOrder.getOrderDetails()) {
                detail.getProduct().getName();
                logger.info("  - {}: {} x {} руб. = {} руб.",
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getPrice(),
                        detail.getSubtotal());
            }

            logger.info("==========================================");
            logger.info("Проверка сохранения заказа:");
            logger.info("==========================================");

            List<Order> allOrders = orderService.getAllOrders();
            logger.info("Всего заказов в базе данных: {}", allOrders.size());

            for (Order o : allOrders) {
                logger.info("Заказ #{} от {} на сумму {} руб. ({}):",
                        o.getId(), o.getOrderDate(), o.getTotalPrice(), o.getStatus());

                o.getOrderDetails().size();
                for (var d : o.getOrderDetails()) {
                    d.getProduct().getName();
                    logger.info("    - {}: {} x {} руб.",
                            d.getProduct().getName(), d.getQuantity(), d.getPrice());
                }
            }

            logger.info("==========================================");
            logger.info("Обновление остатков товаров:");
            logger.info("==========================================");

            List<Product> updatedProducts = productRepository.findAll();
            for (Product p : updatedProducts) {
                logger.info("Товар '{}': новый остаток = {}",
                        p.getName(), p.getStockQuantity());
            }

            logger.info("==========================================");
            logger.info("Приложение успешно завершено!");
            logger.info("==========================================");

        } catch (Exception e) {
            logger.error("Ошибка при выполнении", e);
            System.exit(1);
        }
    }

    private static Product findProductById(List<Product> products, Long productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        throw new RuntimeException("Продукт не найден с ID: " + productId);
    }
}