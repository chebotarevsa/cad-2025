package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderApplication {
    private static final Logger logger = LoggerFactory.getLogger(OrderApplication.class);

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ProductRepository productRepository = context.getBean(ProductRepository.class);
            CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
            OrderService orderService = context.getBean(OrderService.class);

            createSampleOrder(productRepository, customerRepository, orderService);

            verifyOrderCreation(orderService);
        }
    }

    private static void createSampleOrder(ProductRepository productRepository,
                                          CustomerRepository customerRepository,
                                          OrderService orderService) {

        Customer customer = customerRepository.findAll().get(0);
        Product product = productRepository.findAll().get(0);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(2);
        orderDetail.setPrice(product.getPrice());

        orderService.createOrder(customer, Arrays.asList(orderDetail), customer.getAddress());
    }

    private static void verifyOrderCreation(OrderService orderService) {
        List<Order> orders = orderService.getAllOrders();
        logger.info("Нвйдено {} заказов в БД", orders.size());

        if (!orders.isEmpty()) {
            Order order = orders.get(0);
            logger.info("Детали заказа:");
            logger.info("  ID: {}", order.getId());
            logger.info("  Покупатель: {}", order.getCustomer().getName());
            logger.info("  Итого: {}", order.getTotalPrice());
            logger.info("  Статус: {}", order.getStatus());

            for (OrderDetail detail : order.getOrderDetails()) {
                logger.info("  Товар: {} x {} = {}",
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            }
        }
    }
}