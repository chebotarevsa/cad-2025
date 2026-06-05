package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.service.OrderService;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        OrderService orderService = context.getBean(OrderService.class);

        OrderEntity order = orderService.createOrder(1L, 1L, 2);

        logger.info("Order created with id: {}", order.getId());
        logger.info("Orders count in database: {}", orderService.findAllOrders().size());

        context.close();
    }
}