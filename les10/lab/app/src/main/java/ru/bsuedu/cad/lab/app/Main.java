package ru.bsuedu.cad.lab.app;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.AppConfig;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.DataLoaderService;
import ru.bsuedu.cad.lab.service.OrderService;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            DataLoaderService dataLoader = context.getBean(DataLoaderService.class);
            dataLoader.loadData();
            logger.info("Data loaded from CSV files");

            OrderService orderService = context.getBean(OrderService.class);
            List<Integer> productIds = Arrays.asList(1, 3);
            Order order = orderService.createOrder(1, productIds);
            logger.info("Order created: ID={}, Customer={}, Total={}, Status={}",
                    order.getOrderId(),
                    order.getCustomer().getName(),
                    order.getTotalPrice(),
                    order.getStatus());

            List<Order> allOrders = orderService.getAllOrders();
            logger.info("Total orders in database: {}", allOrders.size());
            for (Order o : allOrders) {
                logger.info("Order ID={}, Customer={}, Total={}, Status={}",
                        o.getOrderId(), o.getCustomer().getName(),
                        o.getTotalPrice(), o.getStatus());
            }
        } catch (Exception e) {
            logger.error("Application error", e);
        }
    }
}