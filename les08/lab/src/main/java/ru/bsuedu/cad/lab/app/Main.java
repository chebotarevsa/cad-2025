package ru.bsuedu.cad.lab.app;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.service.DataLoaderService;
import ru.bsuedu.cad.lab.service.OrderService;

public class Main {
    public static void main(String[] args) {
        // Запускаем Spring контекст
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Загружаем данные из CSV
        DataLoaderService dataLoader = ctx.getBean(DataLoaderService.class);
        dataLoader.loadData();
        
        // Создаём заказ
        OrderService orderService = ctx.getBean(OrderService.class);
        
        // Позиции заказа: (productId, quantity)
        List<OrderService.OrderItemData> items = List.of(
            new OrderService.OrderItemData(1L, 2),   // товар с ID=1 (2 штуки)
            new OrderService.OrderItemData(3L, 1)    // товар с ID=3 (1 штука)
        );
        
        // Создаём заказ для клиента с ID=1
        var order = orderService.createOrder(1L, items);
        
        System.out.println("\n=== Все заказы ===");
        orderService.getAllOrders().forEach(o -> {
            System.out.println("Заказ #" + o.getId() + 
                " | Клиент: " + o.getCustomer().getFullName() + 
                " | Статус: " + o.getStatus() +
                " | Сумма: " + o.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum() + " руб.");
        });
        
        System.out.println("\nПриложение успешно завершено");
    }
}