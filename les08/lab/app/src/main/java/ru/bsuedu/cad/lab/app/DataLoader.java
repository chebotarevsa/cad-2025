package ru.bsuedu.cad.lab.app;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataLoader(CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void load() throws Exception {
        loadCategories();
        loadProducts();
        loadCustomers();
        LOGGER.info("Данные загружены: {} категорий, {} товаров, {} клиентов",
            categoryRepository.count(), productRepository.count(), customerRepository.count());
    }

    private void loadCategories() throws Exception {
        String data = readFile("category.csv");
        List<Category> list = new ArrayList<>();
        for (String line : data.split("\\r?\\n")) {
            if (line.startsWith("category_id")) continue;
            String[] p = line.split(",", 3);
            list.add(new Category(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim()));
        }
        categoryRepository.saveAll(list);
    }

    private void loadProducts() throws Exception {
        String data = readFile("product.csv");
        List<Product> list = new ArrayList<>();
        for (String line : data.split("\\r?\\n")) {
            if (line.startsWith("product_id")) continue;
            String[] p = line.split(",");
            Product product = new Product();
            product.setProductId(Long.parseLong(p[0].trim()));
            product.setName(p[1].trim());
            product.setDescription(p[2].trim());
            product.setCategory(categoryRepository.getReferenceById(Integer.parseInt(p[3].trim())));
            product.setPrice(new BigDecimal(p[4].trim()));
            product.setStockQuantity(Integer.parseInt(p[5].trim()));
            product.setImageUrl(p[6].trim());
            product.setCreatedAt(LocalDate.parse(p[7].trim()));
            product.setUpdatedAt(LocalDate.parse(p[8].trim()));
            list.add(product);
        }
        productRepository.saveAll(list);
    }

    private void loadCustomers() throws Exception {
        String data = readFile("customer.csv");
        List<Customer> list = new ArrayList<>();
        for (String line : data.split("\\r?\\n")) {
            if (line.startsWith("customer_id")) continue;
            String[] p = line.split(",", 5);
            list.add(new Customer(
                Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()
            ));
        }
        customerRepository.saveAll(list);
    }

    private String readFile(String filename) throws Exception {
        ClassPathResource res = new ClassPathResource(filename);
        String content = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        // strip BOM if present
        return content.startsWith("﻿") ? content.substring(1) : content;
    }
}
