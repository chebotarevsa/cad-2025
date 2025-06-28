package ru.bsuedu.cad.lab.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.util.CsvDataLoader;

import java.util.List;

@Service
public class DataInitializationService {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CsvDataLoader csvDataLoader;

    public DataInitializationService(CategoryRepository categoryRepository,
                                     ProductRepository productRepository,
                                     CustomerRepository customerRepository,
                                     CsvDataLoader csvDataLoader) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.csvDataLoader = csvDataLoader;
    }

    @PostConstruct
    @Transactional
    public void init() {
        if (categoryRepository.count() == 0) {
            loadCategories();
        }
        if (productRepository.count() == 0) {
            loadProducts();
        }
        if (customerRepository.count() == 0) {
            loadCustomers();
        }
    }

    private void loadCategories() {
        List<String[]> categoryData = csvDataLoader.loadCsvData("category.csv");
        for (String[] row : categoryData) {
            Category category = new Category();
            category.setId(Long.parseLong(row[0]));
            category.setName(row[1]);
            category.setDescription(row[2]);
            categoryRepository.save(category);
        }
        logger.info("Загружено {} категорий", categoryData.size());
    }

    private void loadProducts() {
        List<String[]> productData = csvDataLoader.loadCsvData("product.csv");
        for (String[] row : productData) {
            Product product = new Product();
            product.setId(Long.parseLong(row[0]));
            product.setName(row[1]);
            product.setDescription(row[2]);

            Category category = categoryRepository.findById(Long.parseLong(row[3]))
                    .orElseThrow(() -> new RuntimeException("Категории не найдены"));
            product.setCategory(category);

            product.setPrice(csvDataLoader.parsePrice(row[4]));
            product.setStockQuantity(Integer.parseInt(row[5]));
            product.setImageUrl(row[6]);
            product.setCreatedAt(csvDataLoader.parseDate(row[7]));
            product.setUpdatedAt(csvDataLoader.parseDate(row[8]));

            productRepository.save(product);
        }
        logger.info("Загружено {} товаров", productData.size());
    }

    private void loadCustomers() {
        List<String[]> customerData = csvDataLoader.loadCsvData("customer.csv");
        for (String[] row : customerData) {
            Customer customer = new Customer();
            customer.setId(Long.parseLong(row[0]));
            customer.setName(row[1]);
            customer.setEmail(row[2]);
            customer.setPhone(row[3]);
            customer.setAddress(row[4]);
            customerRepository.save(customer);
        }
        logger.info("Загружено {} покупателей", customerData.size());
    }
}