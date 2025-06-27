package ru.bsuedu.cad.lab.app;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class OrderClient {
    final private OrderService orderService;

    public OrderClient(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public void createOrder(){
        orderService.createOrder(7, 9, 3, new BigDecimal(1200));
    }
}
