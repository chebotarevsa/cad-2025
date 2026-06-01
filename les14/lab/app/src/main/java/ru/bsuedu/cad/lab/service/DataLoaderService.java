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
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    @Transactional
    public void loadAllData() {
        logger.info("========== ЗАГРУЗКА ==========");

        try {
            loadCategories();
            loadCustomers();
            loadProducts();

            // Проверка результатов
            int catCount = categoryRepository.findAll().size();
            int custCount = customerRepository.findAll().size();
            int prodCount = productRepository.findAll().size();

            logger.info("========== ЗАГРУЗКА ЗАВЕРШЕНА ==========");
            logger.info("В БД загружено: категорий={}, покупателей={}, товаров={}",
                    catCount, custCount, prodCount);
        } catch (Exception e) {
            logger.error("Ошибка при загрузке данных!", e);
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadCategories() {
        logger.info("--- ЗАГРУЗКА КАТЕГОРИЙ ---");

        try {
            ClassPathResource resource = new ClassPathResource("data/category.csv");
            logger.info("Файл category.csv существует: {}", resource.exists());
            logger.info("Абсолютный путь: {}", resource.getFile().getAbsolutePath());

            if (!resource.exists()) {
                logger.error("Файл не найден!");
                return;
            }

            Reader reader = new InputStreamReader(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8);

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            int count = 0;
            for (CSVRecord record : parser) {
                count++;
                Long id = Long.parseLong(record.get("category_id"));
                String name = record.get("name");
                String description = record.get("description");

                Category category = new Category(name, description);
                Category savedCategory = categoryRepository.save(category);
                categoryMap.put(id, savedCategory);

                logger.info("Загружена категория: {} - {}", savedCategory.getId(), name);
            }

            logger.info("Всего загружено категорий: {}", count);

        } catch (Exception e) {
            logger.error("Ошибка загрузки категорий!", e);
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadCustomers() {
        logger.info("--- ЗАГРУЗКА ПОКУПАТЕЛЕЙ ---");

        try {
            ClassPathResource resource = new ClassPathResource("data/customer.csv");
            logger.info("Файл customer.csv существует: {}", resource.exists());

            if (!resource.exists()) {
                logger.error("Файл не найден!");
                return;
            }

            Reader reader = new InputStreamReader(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8);

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            int count = 0;
            for (CSVRecord record : parser) {
                count++;
                Long id = Long.parseLong(record.get("customer_id"));
                String name = record.get("name");
                String email = record.get("email");
                String phone = record.get("phone");
                String address = record.get("address");

                Customer customer = new Customer(name, email, phone, address);
                Customer savedCustomer = customerRepository.save(customer);
                customerMap.put(id, savedCustomer);

                logger.info("Загружен покупатель: {} - {} ({})", savedCustomer.getId(), name, email);
            }

            logger.info("Всего загружено покупателей: {}", count);

        } catch (Exception e) {
            logger.error("Ошибка загрузки покупателей!", e);
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadProducts() {
        logger.info("--- ЗАГРУЗКА ТОВАРОВ ---");

        try {
            ClassPathResource resource = new ClassPathResource("data/product.csv");
            logger.info("Файл product.csv существует: {}", resource.exists());

            if (!resource.exists()) {
                logger.error("Файл не найден!");
                return;
            }

            Reader reader = new InputStreamReader(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8);

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            int count = 0;
            for (CSVRecord record : parser) {
                count++;

                Long id = Long.parseLong(record.get("product_id"));
                String name = record.get("name");
                String description = record.get("description");
                Long categoryId = Long.parseLong(record.get("category_id"));
                BigDecimal price = new BigDecimal(record.get("price"));
                Integer stockQuantity = Integer.parseInt(record.get("stock_quantity"));
                String imageUrl = record.get("image_url");

                Category category = categoryMap.get(categoryId);
                if (category == null) {
                    logger.error("Категория с ID {} не найдена для товара {}!", categoryId, name);
                    continue;
                }

                Product product = new Product(name, description, category, price, stockQuantity, imageUrl);

                productRepository.save(product);
                logger.info("Загружен товар: {} - {} (категория: {})", id, name, category.getName());
            }

            logger.info("Всего загружено товаров: {}", count);

        } catch (Exception e) {
            logger.error("Ошибка загрузки товаров!", e);
            e.printStackTrace();
        }
    }
}