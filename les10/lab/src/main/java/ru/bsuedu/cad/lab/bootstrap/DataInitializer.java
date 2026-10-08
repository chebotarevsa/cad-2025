package ru.bsuedu.cad.lab.bootstrap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Component
public class DataInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private boolean loaded;

    public DataInitializer(CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (loaded || categoryRepository.count() > 0) {
            return;
        }
        loadCategories();
        loadCustomers();
        loadProducts();
        loaded = true;
        LOGGER.info("Справочники загружены из CSV: categories={}, products={}, customers={}",
                categoryRepository.count(), productRepository.count(), customerRepository.count());
    }

    private void loadCategories() {
        try (BufferedReader reader = open("data/category.csv")) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                Category category = new Category();
                category.setCategoryId(Integer.parseInt(parts[0].trim()));
                category.setName(parts[1].trim());
                category.setDescription(parts.length > 2 ? parts[2].trim() : "");
                categoryRepository.save(category);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось загрузить category.csv", e);
        }
    }

    private void loadCustomers() {
        try (BufferedReader reader = open("data/customer.csv")) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 5);
                Customer customer = new Customer();
                customer.setCustomerId(Integer.parseInt(parts[0].trim()));
                customer.setName(parts[1].trim());
                customer.setEmail(parts[2].trim());
                customer.setPhone(parts[3].trim());
                customer.setAddress(parts.length > 4 ? parts[4].trim() : "");
                customerRepository.save(customer);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось загрузить customer.csv", e);
        }
    }

    private void loadProducts() {
        Map<Integer, Category> categories = new HashMap<>();
        categoryRepository.findAll().forEach(c -> categories.put(c.getCategoryId(), c));

        try (BufferedReader reader = open("data/product.csv")) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 9);
                Product product = new Product();
                product.setProductId(Integer.parseInt(parts[0].trim()));
                product.setName(parts[1].trim());
                product.setDescription(parts[2].trim());
                Integer categoryId = Integer.parseInt(parts[3].trim());
                product.setCategory(categories.get(categoryId));
                product.setPrice(new BigDecimal(parts[4].trim()));
                product.setStockQuantity(Integer.parseInt(parts[5].trim()));
                product.setImageUrl(parts[6].trim());
                product.setCreatedAt(LocalDate.parse(parts[7].trim()));
                product.setUpdatedAt(LocalDate.parse(parts[8].trim()));
                productRepository.save(product);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось загрузить product.csv", e);
        }
    }

    private BufferedReader open(String path) throws Exception {
        return new BufferedReader(new InputStreamReader(
                new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8));
    }
}
