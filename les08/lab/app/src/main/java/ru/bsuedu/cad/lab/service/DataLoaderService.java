package ru.bsuedu.cad.lab.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoaderService.class);

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    private Map<Long, Category> categoryMap = new HashMap<>();
    private Map<Long, Customer> customerMap = new HashMap<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DataLoaderService(CategoryRepository categoryRepository,
                             CustomerRepository customerRepository,
                             ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void loadAllData() {
        logger.info("Загрузка CSV-файлов...");

        loadCategories();
        loadCustomers();
        loadProducts();

        logger.info("Загрузка данных успешно выполнена");
    }

    @Transactional
    public void loadCategories() {
        logger.info("Загрузка категорий...");

        try (Reader reader = new InputStreamReader(
                new ClassPathResource("data/category.csv").getInputStream(),
                StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()
                     .parse(reader)) {

            Map<String, Integer> headerMap = parser.getHeaderMap();
            logger.debug("Category CSV Headers: {}", headerMap.keySet());

            for (CSVRecord record : parser) {
                Long id = Long.parseLong(record.get("category_id"));
                String name = record.get("name");
                String description = record.get("description");

                Category category = new Category(name, description);
                Category savedCategory = categoryRepository.save(category);
                categoryMap.put(id, savedCategory);

                logger.debug("Загружены категории: {} - {}", savedCategory.getId(), savedCategory.getName());
            }

            logger.info("Загружены {} категорий", categoryMap.size());

        } catch (Exception e) {
            logger.error("Ошибка загрузки категорий", e);
            throw new RuntimeException("Failed to load categories", e);
        }
    }

    @Transactional
    public void loadCustomers() {
        logger.info("Загрузка покупателей...");

        try (Reader reader = new InputStreamReader(
                new ClassPathResource("data/customer.csv").getInputStream(),
                StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()
                     .parse(reader)) {

            Map<String, Integer> headerMap = parser.getHeaderMap();
            logger.debug("Customer CSV Headers: {}", headerMap.keySet());

            for (CSVRecord record : parser) {
                Long id = Long.parseLong(record.get("customer_id"));
                String name = record.get("name");
                String email = record.get("email");
                String phone = record.get("phone");
                String address = record.get("address");

                Customer customer = new Customer(name, email, phone, address);
                Customer savedCustomer = customerRepository.save(customer);
                customerMap.put(id, savedCustomer);

                logger.debug("Загружены покупатели: {} - {} ({})",
                        savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getEmail());
            }

            logger.info("Загружено {} покупателей", customerMap.size());

        } catch (Exception e) {
            logger.error("Ошибка загрузки покупателей", e);
            throw new RuntimeException("Failed to load customers", e);
        }
    }

    @Transactional
    public void loadProducts() {
        logger.info("Загрузка товаров...");

        try (Reader reader = new InputStreamReader(
                new ClassPathResource("data/product.csv").getInputStream(),
                StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()
                     .parse(reader)) {

            Map<String, Integer> rawHeaderMap = parser.getHeaderMap();
            logger.debug("Raw Product CSV Headers: {}", rawHeaderMap.keySet());

            Map<String, String> headerMapping = new HashMap<>();
            for (String header : rawHeaderMap.keySet()) {
                String cleanHeader = header.replace("\uFEFF", "").trim();
                headerMapping.put(cleanHeader, header);
                logger.debug("Header mapping: '{}' -> '{}'", cleanHeader, header);
            }

            int count = 0;
            for (CSVRecord record : parser) {
                try {
                    Long id = Long.parseLong(getValue(record, headerMapping, "product_id"));
                    String name = getValue(record, headerMapping, "name");
                    String description = getValue(record, headerMapping, "description");
                    Long categoryId = Long.parseLong(getValue(record, headerMapping, "category_id"));
                    BigDecimal price = new BigDecimal(getValue(record, headerMapping, "price"));
                    Integer stockQuantity = Integer.parseInt(getValue(record, headerMapping, "stock_quantity"));
                    String imageUrl = getValue(record, headerMapping, "image_url");

                    LocalDateTime createdAt = null;
                    LocalDateTime updatedAt = null;

                    try {
                        String createdAtStr = getValue(record, headerMapping, "created_at");
                        if (createdAtStr != null && !createdAtStr.isEmpty()) {
                            createdAt = LocalDate.parse(createdAtStr, DATE_FORMATTER).atStartOfDay();
                        }
                    } catch (Exception e) {
                        logger.warn("Could not parse created_at for product {}: {}", id, getValue(record, headerMapping, "created_at"));
                    }

                    try {
                        String updatedAtStr = getValue(record, headerMapping, "updated_at");
                        if (updatedAtStr != null && !updatedAtStr.isEmpty()) {
                            updatedAt = LocalDate.parse(updatedAtStr, DATE_FORMATTER).atStartOfDay();
                        }
                    } catch (Exception e) {
                        logger.warn("Could not parse updated_at for product {}: {}", id, getValue(record, headerMapping, "updated_at"));
                    }

                    Category category = categoryMap.get(categoryId);
                    if (category == null) {
                        throw new RuntimeException("Не найден товар с ID: " + categoryId);
                    }

                    Product product = new Product(name, description, category, price, stockQuantity, imageUrl);

                    if (createdAt != null) {
                        product.setCreatedAt(createdAt);
                    }
                    if (updatedAt != null) {
                        product.setUpdatedAt(updatedAt);
                    }

                    productRepository.save(product);
                    count++;

                    logger.debug("Загружены товары: {} - {} (цена: {}, количество: {})",
                            product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());

                } catch (Exception e) {
                    logger.error("Ошибка записи товаров: {}", record);
                    throw e;
                }
            }

            logger.info("Записано {} продуктов", count);

        } catch (Exception e) {
            logger.error("Ошибка загрузки продуктов", e);
            throw new RuntimeException("Failed to load products", e);
        }
    }

    private String getValue(CSVRecord record, Map<String, String> headerMapping, String key) {
        String actualHeader = headerMapping.get(key);
        if (actualHeader == null) {
            throw new IllegalArgumentException("Mapping for " + key + " not found");
        }
        return record.get(actualHeader);
    }

    public Customer getCustomerByOriginalId(Long originalId) {
        return customerMap.get(originalId);
    }

    public Category getCategoryByOriginalId(Long originalId) {
        return categoryMap.get(originalId);
    }
}