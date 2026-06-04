package ru.bsuedu.cad.lab.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class CsvDataImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataImportService.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public CsvDataImportService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
        this.customerRepository = Objects.requireNonNull(customerRepository);
    }

    @Transactional
    public void importAll() {
        importCategories();
        importProducts();
        importCustomers();
        LOGGER.info(
                "Импорт CSV завершён: категорий={}, товаров={}, клиентов={}",
                categoryRepository.findAll().size(),
                productRepository.findAll().size(),
                customerRepository.findAll().size());
    }

    private void importCategories() {
        String content = readResource("category.csv");
        String[] lines = content.strip().split("\\R");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(",", 3);
            Category category = new Category();
            category.setCategoryId(Integer.parseInt(fields[0].trim()));
            category.setName(fields[1].trim());
            category.setDescription(fields[2].trim());
            categoryRepository.create(category);
        }
    }

    private void importProducts() {
        String content = readResource("product.csv");
        String[] lines = content.strip().split("\\R");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(",", 9);
            int categoryId = Integer.parseInt(fields[3].trim());
            Category category = categoryRepository
                    .findById(categoryId)
                    .orElseThrow(() -> new IllegalStateException("Category not found: " + categoryId));

            Product product = new Product();
            product.setProductId(Long.parseLong(fields[0].trim()));
            product.setName(fields[1].trim());
            product.setDescription(fields[2].trim());
            product.setCategory(category);
            product.setPrice(new BigDecimal(fields[4].trim()));
            product.setStockQuantity(Integer.parseInt(fields[5].trim()));
            product.setImageUrl(fields[6].trim());
            product.setCreatedAt(toDateTime(fields[7].trim()));
            product.setUpdatedAt(toDateTime(fields[8].trim()));
            productRepository.create(product);
        }
    }

    private void importCustomers() {
        String content = readResource("customer.csv");
        String[] lines = content.strip().split("\\R");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(",", 5);
            Customer customer = new Customer();
            customer.setCustomerId(Integer.parseInt(fields[0].trim()));
            customer.setName(fields[1].trim());
            customer.setEmail(fields[2].trim());
            customer.setPhone(fields[3].trim());
            customer.setAddress(fields[4].trim());
            customerRepository.create(customer);
        }
    }

    private LocalDateTime toDateTime(String date) {
        return LocalDate.parse(date).atTime(LocalTime.MIDNIGHT);
    }

    private String readResource(String name) {
        try (InputStream inputStream =
                CsvDataImportService.class.getClassLoader().getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: " + name);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + name, e);
        }
    }
}
