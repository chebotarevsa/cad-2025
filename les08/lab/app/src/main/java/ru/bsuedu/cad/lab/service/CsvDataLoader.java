package ru.bsuedu.cad.lab.service;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CsvDataLoader {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void loadData() throws Exception {
        loadCategories();
        loadCustomers();
        loadProducts();
        System.out.println("📥 CSV-данные успешно загружены");
    }

    private void loadCategories() throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("category.csv").getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0); // remove header
            for (String[] line : lines) {
                Category c = new Category();
                c.setId(Integer.parseInt(line[0]));
                c.setName(line[1]);
                c.setDescription(line[2]);
                categoryRepository.save(c);
            }
        }
    }

    private void loadCustomers() throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("customer.csv").getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);
            for (String[] line : lines) {
                Customer c = new Customer();
                c.setId(Integer.parseInt(line[0]));
                c.setName(line[1]);
                c.setEmail(line[2]);
                c.setPhone(line[3]);
                c.setAddress(line[4]);
                customerRepository.save(c);
            }
        }
    }

    private void loadProducts() throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("product.csv").getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);
            for (String[] line : lines) {
                Product p = new Product();
                p.setId(Integer.parseInt(line[0]));
                p.setName(line[1]);
                p.setDescription(line[2]);
                int catId = Integer.parseInt(line[3]);
                Category cat = categoryRepository.findById(catId).orElseThrow();
                p.setCategory(cat);
                p.setPrice(new BigDecimal(line[4]));
                p.setStockQuantity(Integer.parseInt(line[5]));
                p.setImageUrl(line[6]);
                p.setCreatedAt(java.time.LocalDate.parse(line[7]).atStartOfDay());
                p.setUpdatedAt(java.time.LocalDate.parse(line[8]).atStartOfDay());
                productRepository.save(p);
            }
        }
    }
}
