package ru.bsuedu.cad.lab.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
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
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void loadData() {
        loadCategories();
        loadCustomers();
        loadProducts();
        System.out.println("Данные загружены в БД");
    }

    private void loadCategories() {
        if (categoryRepository.count() > 0) return;
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("category.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = line.split(",");
                Category c = new Category();
                c.setId(Long.parseLong(parts[0]));
                c.setName(parts[1]);
                categoryRepository.save(c);
                System.out.println("Загружена категория: " + c.getName());
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки категорий: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        if (customerRepository.count() > 0) return;
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("customer.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = line.split(",");
                Customer c = new Customer();
                c.setId(Long.parseLong(parts[0]));
                c.setFullName(parts[1]);
                c.setEmail(parts[2]);
                c.setPhone(parts[3]);
                customerRepository.save(c);
                System.out.println("Загружен клиент: " + c.getFullName());
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки клиентов: " + e.getMessage());
        }
    }

    private void loadProducts() {
    if (productRepository.count() > 0) return;
    
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("product.csv");
         BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 4) {
                System.err.println("Пропущена строка (неверный формат): " + line);
                continue;
            }
            try {
                Product p = new Product();
                p.setId(Long.parseLong(parts[0].trim()));
                p.setName(parts[1].trim());
                p.setPrice(Double.parseDouble(parts[2].trim()));
                Long catId = Long.parseLong(parts[3].trim());
                p.setCategory(categoryRepository.findById(catId).orElse(null));
                productRepository.save(p);
                System.out.println("Загружен товар: " + p.getName() + " (" + p.getPrice() + " руб.)");
            } catch (NumberFormatException e) {
                System.err.println("Ошибка парсинга строки: " + line + " | " + e.getMessage());
            }
        }
    } catch (Exception e) {
        System.err.println("Ошибка загрузки товаров: " + e.getMessage());
    }
}
}