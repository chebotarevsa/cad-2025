package ru.bsu.cad.lab.app;

import jakarta.persistence.*;
import ru.bsu.cad.lab.entity.*;
import ru.bsu.cad.lab.repository.*;
import ru.bsu.cad.lab.service.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab-unit");
        EntityManager em = emf.createEntityManager();

        CategoryRepository categoryRepo = new CategoryRepository(em);
        CustomerRepository customerRepo = new CustomerRepository(em);
        ProductRepository productRepo = new ProductRepository(em);

        loadCSVData(em, categoryRepo, customerRepo, productRepo);

        OrderService orderService = new OrderService(em);
        OrderQueryService queryService = new OrderQueryService(em);

        Order order = new Order();
        order.setCustomer(customerRepo.findById(1));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress("Some address");

        Product product = productRepo.findById(1);

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(2);
        detail.setPrice(product.getPrice());

        order.setOrderDetails(List.of(detail));
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(2)));

        orderService.createOrder(order);

        System.out.println("Создан заказ ID: " + order.getOrderId());

        List<Order> allOrders = queryService.getAllOrders();
        System.out.println("Все заказы:");
        allOrders.forEach(o -> System.out.println("Заказ ID: " + o.getOrderId() + ", статус: " + o.getStatus()));

        em.close();
        emf.close();
    }

    private static void loadCSVData(EntityManager em, CategoryRepository catRepo, CustomerRepository custRepo, ProductRepository prodRepo) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            loadCategories(catRepo);
            loadCustomers(custRepo);
            loadProducts(prodRepo, catRepo);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        }
    }

    private static void loadCategories(CategoryRepository repo) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream("/category.csv")))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Category c = new Category();
                c.setName(parts[1].trim());
                c.setDescription(parts[2].trim());
                repo.save(c);
            }
        }
    }

    private static void loadCustomers(CustomerRepository repo) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream("/customer.csv")))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Customer c = new Customer();
                c.setName(parts[1].trim());
                c.setEmail(parts[2].trim());
                c.setPhone(parts[3].trim());
                c.setAddress(parts[4].trim());
                repo.save(c);
            }
        }
    }

    private static void loadProducts(ProductRepository repo, CategoryRepository catRepo) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream("/product.csv")))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Product p = new Product();
                p.setName(parts[1].trim());
                p.setDescription(parts[2].trim());
                p.setCategory(catRepo.findById(Integer.parseInt(parts[3].trim())));
                p.setPrice(new BigDecimal(parts[4].trim()));
                p.setStockQuantity(Integer.parseInt(parts[5].trim()));
                p.setImageUrl(parts[6].trim());

                // ✅ Парсим дату без времени и добавляем время через atStartOfDay
                p.setCreatedAt(java.time.LocalDate.parse(parts[7].trim()).atStartOfDay());
                p.setUpdatedAt(java.time.LocalDate.parse(parts[8].trim()).atStartOfDay());

                repo.save(p);
            }
        }
    }
}