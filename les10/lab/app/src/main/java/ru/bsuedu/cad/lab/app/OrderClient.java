package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

@Component
public class OrderClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderClient.class);

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderClient(OrderService orderService,
                       CustomerRepository customerRepository,
                       ProductRepository productRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public void run() {
        Customer customer = customerRepository.findById(1).orElseThrow();
        List<Product> products = List.of(
            productRepository.findById(1L).orElseThrow(),
            productRepository.findById(4L).orElseThrow(),
            productRepository.findById(7L).orElseThrow()
        );

        orderService.createOrder(customer, products, customer.getAddress());

        List<Order> allOrders = orderService.getAllOrders();
        LOGGER.info("Всего заказов в БД: {}", allOrders.size());
        allOrders.forEach(o ->
            LOGGER.info("  Заказ #{}: клиент='{}', сумма={} руб., статус='{}'",
                o.getOrderId(), o.getCustomer().getName(), o.getTotalPrice(), o.getStatus())
        );
    }
}
