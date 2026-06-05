package ru.bsuedu.cad.lab.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class DataLoader {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository, CustomerRepository customerRepository, OrderService orderService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }

    @PostConstruct
    public void run() throws Exception {
        loadCategories();
        loadCustomers();
        ensureDefaultCustomers();
        loadProducts();

        // create a demo order for first customer and first product
        var customer = customerRepository.findAll().stream().findFirst().orElse(null);
        var product = productRepository.findAll().stream().findFirst().orElse(null);
        if (customer != null && product != null) {
            Map<Long,Integer> items = new HashMap<>();
            items.put(product.getProductId(), 1);
            var order = orderService.createOrder(customer.getCustomerId(), items, "");
            System.out.println("Created order id=" + order.getOrderId());
        } else {
            System.out.println("No customers or products available to create demo order.");
        }
    }

    private void loadCategories() {
        Path path = Path.of("assets/category.csv");
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 1) continue;
                Category c = new Category();
                c.setName(parts[0].trim());
                if (parts.length > 1) c.setDescription(parts[1].trim());
                categoryRepository.save(c);
            }
            System.out.println("Categories loaded: " + categoryRepository.count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        Path path = Path.of("assets/customer.csv");
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 1) continue;
                Customer c = new Customer();
                c.setName(parts.length>0?parts[0].trim():"");
                c.setEmail(parts.length>1?parts[1].trim():"");
                c.setPhone(parts.length>2?parts[2].trim():"");
                c.setAddress(parts.length>3?parts[3].trim():"");
                customerRepository.save(c);
            }
            System.out.println("Customers loaded: " + customerRepository.count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureDefaultCustomers() {
        if (customerRepository.count() > 0) {
            return;
        }

        Customer c1 = new Customer();
        c1.setName("Иван Иванов");
        c1.setEmail("ivan@example.com");
        c1.setPhone("+7 900 000 0001");
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("Пётр Петров");
        c2.setEmail("petr@example.com");
        c2.setPhone("+7 900 000 0002");
        customerRepository.save(c2);

        System.out.println("Default customers created: " + customerRepository.count());
    }

    private void loadProducts() {
        Path path = Path.of("assets/product.csv");
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                Product p = new Product();
                p.setName(parts[0].trim());
                p.setDescription(parts[1].trim());
                // try to find category by name
                var cat = categoryRepository.findAll().stream().filter(c -> c.getName().equalsIgnoreCase(parts[2].trim())).findFirst().orElse(null);
                p.setCategory(cat);
                try { p.setPrice(new BigDecimal(parts[3].trim())); } catch (Exception ex) { p.setPrice(BigDecimal.ZERO); }
                p.setStockQuantity(parts.length>4?Integer.parseInt(parts[4].trim()):0);
                p.setCreatedAt(LocalDateTime.now());
                productRepository.save(p);
            }
            System.out.println("Products loaded: " + productRepository.count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
