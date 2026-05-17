package ru.bsuedu.cad.lab.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class DataLoaderService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void loadData() throws Exception {
        loadCategories();
        loadProducts();
        loadCustomers();
    }

    private void loadCategories() throws Exception {
        ClassPathResource resource = new ClassPathResource("category.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Category category = new Category();
                category.setCategoryId(Integer.parseInt(fields[0].trim()));
                category.setName(fields[1].trim());
                category.setDescription(fields[2].trim());
                categoryRepository.save(category);
            }
        }
    }

    private void loadProducts() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ClassPathResource resource = new ClassPathResource("product.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Product product = new Product();
                product.setProductId(Integer.parseInt(fields[0].trim()));
                product.setName(fields[1].trim());
                product.setDescription(fields[2].trim());
                product.setPrice(new BigDecimal(fields[4].trim()));
                product.setStockQuantity(Integer.parseInt(fields[5].trim()));
                product.setImageUrl(fields[6].trim());
                product.setCreatedAt(dateFormat.parse(fields[7].trim()));
                product.setUpdatedAt(dateFormat.parse(fields[8].trim()));

                int categoryId = Integer.parseInt(fields[3].trim());
                Category category = categoryRepository.findById(categoryId).orElse(null);
                product.setCategory(category);
                productRepository.save(product);
            }
        }
    }

    private void loadCustomers() throws Exception {
        ClassPathResource resource = new ClassPathResource("customer.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Customer customer = new Customer();
                customer.setCustomerId(Integer.parseInt(fields[0].trim()));
                customer.setName(fields[1].trim());
                customer.setEmail(fields[2].trim());
                customer.setPhone(fields[3].trim());
                customer.setAddress(fields[4].trim());
                customerRepository.save(customer);
            }
        }
    }
}