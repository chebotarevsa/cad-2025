package ru.bsuedu.cad.lab.app;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.repository.ShopOrderRepository;
import ru.bsuedu.cad.lab.service.CsvDataImportService;
import ru.bsuedu.cad.lab.service.OrderLineRequest;
import ru.bsuedu.cad.lab.service.OrderService;

@Component
public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final CsvDataImportService csvDataImportService;
    private final OrderService orderService;
    private final ShopOrderRepository shopOrderRepository;

    public Client(
            CsvDataImportService csvDataImportService,
            OrderService orderService,
            ShopOrderRepository shopOrderRepository) {
        this.csvDataImportService = Objects.requireNonNull(csvDataImportService);
        this.orderService = Objects.requireNonNull(orderService);
        this.shopOrderRepository = Objects.requireNonNull(shopOrderRepository);
    }

    @Transactional
    public void run() {
        csvDataImportService.importAll();

        ShopOrder created = orderService.createOrder(
                1,
                "Москва, ул. Ленина, д. 10",
                List.of(new OrderLineRequest(1L, 2), new OrderLineRequest(3L, 1)));

        ShopOrder fromDatabase = shopOrderRepository
                .findById(created.getOrderId())
                .orElseThrow(() -> new IllegalStateException("Order not found after save"));

        LOGGER.info(
                "Заказ подтверждён в БД: id={}, статус={}, сумма={}, позиций={}",
                fromDatabase.getOrderId(),
                fromDatabase.getStatus(),
                fromDatabase.getTotalPrice(),
                fromDatabase.getOrderDetails().size());

        List<ShopOrder> allOrders = orderService.findAllOrders();
        LOGGER.info("Всего заказов в базе: {}", allOrders.size());
        for (ShopOrder order : allOrders) {
            LOGGER.info(
                    "Заказ id={}, клиент id={}, сумма={}",
                    order.getOrderId(),
                    order.getCustomer().getCustomerId(),
                    order.getTotalPrice());
        }
    }
}
