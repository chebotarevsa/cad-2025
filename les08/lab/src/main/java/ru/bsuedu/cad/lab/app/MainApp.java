package ru.bsuedu.cad.lab.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.CsvLoaderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication(scanBasePackages = "ru.bsuedu.cad.lab")
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
@EntityScan(basePackages = "ru.bsuedu.cad.lab.entity")
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Bean
    public CommandLineRunner run(
        CategoryRepository categoryRepo,
        ProductRepository productRepo,
        CustomerRepository customerRepo,
        OrderService orderService,
        CsvLoaderService loader
    ) {
        return args -> {
            loader.loadCsvData();
            Category cat = new Category();
            cat.setName("Корма");
            cat.setDescription("Корма для животных");
            categoryRepo.save(cat);

            Product product = new Product();
            product.setName("Корм для собак");
            product.setDescription("Полноценный сухой корм");
            product.setCategory(cat);
            product.setPrice(new BigDecimal("19.99"));
            product.setStockQuantity(50);
            product.setImageUrl("https://example.com/dogfood.jpg");
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());
            productRepo.save(product);

            Customer customer = new Customer();
            customer.setName("Иван Иванов");
            customer.setEmail("ivan@example.com");
            customer.setPhone("+123456789");
            customer.setAddress("ул. Пушкина, д. 10");
            customerRepo.save(customer);

            Order order = orderService.createOrder(customer.getCustomerId(), List.of(product.getProductId()));

            System.out.println("Заказ создан: #" + order.getOrderId());
            System.out.println("Общая сумма: " + order.getTotalPrice());
        };
    }
}
