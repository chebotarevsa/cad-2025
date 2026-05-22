package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.ConfigJpa;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderItem;
import ru.bsuedu.cad.lab.service.OrderService;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ConfigJpa.class)) {
            DataSource dataSource = context.getBean(DataSource.class);
            LOGGER.info("DataSource created: {}", dataSource.getClass().getName());
            LOGGER.info("JPA configuration loaded");

            loadData(context);

            OrderService orderService = context.getBean(OrderService.class);
            Order order = orderService.createOrder(
                    1,
                    List.of(new OrderItem(1, 2), new OrderItem(2, 1)),
                    "Москва, ул. Ленина, д. 10"
            );

            LOGGER.info("Created order id={}, total={}, status={}",
                    order.getId(), order.getTotalPrice(), order.getStatus());

            List<Order> orders = orderService.findAllOrders();
            LOGGER.info("Orders in database: {}", orders.size());
            for (Order savedOrder : orders) {
                LOGGER.info("Saved order id={}, total={}, status={}",
                        savedOrder.getId(), savedOrder.getTotalPrice(), savedOrder.getStatus());
            }
        }
    }

    private static void loadData(AnnotationConfigApplicationContext context) {
        loadCategories(context.getBean(CategoryRepository.class));
        loadCustomers(context.getBean(CustomerRepository.class));
        loadProducts(context.getBean(ProductRepository.class), context.getBean(CategoryRepository.class));
    }

    private static void loadCategories(CategoryRepository categoryRepository) {
        readCsv("category.csv").stream().skip(1).forEach(line -> {
            String[] parts = line.split(",", 3);

            Category category = new Category();
            category.setId(Integer.parseInt(parts[0]));
            category.setName(parts[1]);
            category.setDescription(parts[2]);

            categoryRepository.save(category);
        });
        LOGGER.info("Categories loaded: {}", categoryRepository.count());
    }

    private static void loadCustomers(CustomerRepository customerRepository) {
        readCsv("customer.csv").stream().skip(1).forEach(line -> {
            String[] parts = line.split(",", 5);

            Customer customer = new Customer();
            customer.setId(Integer.parseInt(parts[0]));
            customer.setName(parts[1]);
            customer.setEmail(parts[2]);
            customer.setPhone(parts[3]);
            customer.setAddress(parts[4]);

            customerRepository.save(customer);
        });
        LOGGER.info("Customers loaded: {}", customerRepository.count());
    }

    private static void loadProducts(ProductRepository productRepository,
                                     CategoryRepository categoryRepository) {
        readCsv("product.csv").stream().skip(1).forEach(line -> {
            String cleanLine = line.replace("\uFEFF", "");
            String[] parts = cleanLine.split(",", 9);

            Product product = new Product();
            product.setId(Integer.parseInt(parts[0]));
            product.setName(parts[1]);
            product.setDescription(parts[2]);
            product.setCategory(categoryRepository.findById(Integer.parseInt(parts[3])).orElseThrow());
            product.setPrice(new BigDecimal(parts[4]));
            product.setStockQuantity(Integer.parseInt(parts[5]));
            product.setImageUrl(parts[6]);
            product.setCreatedAt(LocalDate.parse(parts[7]).atStartOfDay());
            product.setUpdatedAt(LocalDate.parse(parts[8]).atStartOfDay());

            productRepository.save(product);
        });
        LOGGER.info("Products loaded: {}", productRepository.count());
    }

    private static List<String> readCsv(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                App.class.getClassLoader().getResourceAsStream(fileName),
                StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException("Cannot read file: " + fileName, e);
        }
    }
}
