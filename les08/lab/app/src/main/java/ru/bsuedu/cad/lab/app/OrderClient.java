package ru.bsuedu.cad.lab.app;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class OrderClient {
	final private Logger logger = LoggerFactory.getLogger(OrderClient.class);
	private final OrderService orderService;

	public OrderClient(OrderService orderService) {
		this.orderService = orderService;
	}

	public void run() {
		try {
			logger.info("Создание нового заказа...");
			Order order = orderService.createOrder(2, 1, 2);
			logger.info("Успешно создан заказ: {}", order);

			List<Order> orders = orderService.getAllOrders();
			logger.info("Все заказы:");
			orders.forEach(o -> logger.info(o.toString()));
		} catch (Exception e) {
			logger.error("Ошибка создания заказа", e);
		}
	}
}
