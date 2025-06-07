package ru.bsuedu.cad.lab.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class OrderClient {
    @Autowired
    public OrderService orderService;

    final private Logger logger = LoggerFactory.getLogger(OrderClient.class);

    public void run()
    {
        var order = orderService.createOrder(1L, 1L, 1);
        var orderId = order.getOrderID();
        logger.info("The order " + orderId + " created ");
    }
}
