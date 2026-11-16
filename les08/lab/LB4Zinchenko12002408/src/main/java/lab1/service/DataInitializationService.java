package lab1.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import lab1.entity.*;
import lab1.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DataInitializationService {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void initializeData() {
        log.info("Начало инициализации данных из CSV файлов");

        loadCategories();
        loadProducts();
        loadCustomers();

        log.info("Инициализация данных завершена");
        log.info("Загружено категорий: {}", categoryRepository.count());
        log.info("Загружено продуктов: {}", productRepository.count());
        log.info("Загружено клиентов: {}", customerRepository.count());
    }

    private void loadCategories() {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream("category.csv"))))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .withSkipLines(1)
                .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                Category category = new Category();
                category.setName(line[1]);
                category.setDescription(line[2]);
                categoryRepository.save(category);
                log.info("Загружена категория: {}", line[1]);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки категорий: {}", e.getMessage());
        }
    }

    private void loadProducts() {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream("product.csv"))))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build()) {

            List<Category> categories = categoryRepository.findAll();

            String[] line;
            while ((line = reader.readNext()) != null) {
                Product product = new Product();
                product.setName(line[1]);
                product.setDescription(line[2]);

                Category category = null;
                for (Category c : categories) {
                    if (c.getName().equals(line[3])) {
                        category = c;
                        break;
                    }
                }
                product.setCategory(category);

                product.setPrice(new BigDecimal(line[4]));
                product.setStockQuantity(Integer.parseInt(line[5]));
                product.setCreatedAt(new Date());
                product.setUpdatedAt(new Date());

                productRepository.save(product);
                log.info("Загружен продукт: {}", line[1]);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки продуктов: {}", e.getMessage());
        }
    }

    private void loadCustomers() {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream("customer.csv"))))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .withSkipLines(1)
                .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                Customer customer = new Customer();
                customer.setName(line[1]);
                customer.setEmail(line[2]);
                customer.setPhone(line[3]);
                customer.setAddress(line[4]);
                customerRepository.save(customer);
                log.info("Загружен клиент: {}", line[1]);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки клиентов: {}", e.getMessage());
        }
    }
}