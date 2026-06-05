package ru.bsuedu.cad.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class DataLoaderService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataLoaderService(CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void loadData() {
        loadCategories();
        loadProducts();
        loadCustomers();
    }

    private void loadCategories() {
        try (BufferedReader reader = getReader("category.csv")) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Category category = new Category(
                        Long.parseLong(values[0]),
                        values[1],
                        values[2]
                );

                categoryRepository.save(category);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading category.csv", e);
        }
    }

    private void loadProducts() {
        try (BufferedReader reader = getReader("products.csv")) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Long categoryId = Long.parseLong(values[3]);
                Category category = categoryRepository.findById(categoryId);

                Product product = new Product(
                        Long.parseLong(values[0]),
                        values[1],
                        values[2],
                        new BigDecimal(values[4]),
                        Integer.parseInt(values[5]),
                        values[6],
                        java.time.LocalDate.parse(values[7]),
                        java.time.LocalDate.parse(values[8]),
                        category
                );

                productRepository.save(product);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading product.csv", e);
        }
    }

    private void loadCustomers() {
        try (BufferedReader reader = getReader("customer.csv")) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Customer customer = new Customer(
                        Long.parseLong(values[0]),
                        values[1],
                        values[2],
                        values[3],
                        values[4]
                );

                customerRepository.save(customer);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading customer.csv", e);
        }
    }

    private BufferedReader getReader(String fileName) {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );
    }
}