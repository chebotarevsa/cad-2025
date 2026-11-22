package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@WebListener
public class AppInitializer implements ServletContextListener {

    private EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Инициализация веб-приложения ===");

        emf = Persistence.createEntityManagerFactory("zoo-shop-pu");
        sce.getServletContext().setAttribute("emf", emf);

        ProductRepository productRepo = new ProductRepository(emf);
        CategoryRepository categoryRepo = new CategoryRepository(emf);
        CustomerRepository customerRepo = new CustomerRepository(emf);
        OrderRepository orderRepo = new OrderRepository(emf);
        OrderItemRepository orderItemRepo = new OrderItemRepository(emf);

        OrderService orderService = new OrderService(
                orderRepo, orderItemRepo, productRepo, customerRepo
        );

        sce.getServletContext().setAttribute("productRepo", productRepo);
        sce.getServletContext().setAttribute("categoryRepo", categoryRepo);
        sce.getServletContext().setAttribute("customerRepo", customerRepo);
        sce.getServletContext().setAttribute("orderRepo", orderRepo);
        sce.getServletContext().setAttribute("orderService", orderService);

        System.out.println("=== Инициализация завершена ===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== Завершение работы приложения ===");
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}