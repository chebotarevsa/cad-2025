package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientApp {

  private static final Logger logger = LoggerFactory.getLogger(ClientApp.class);

  public static void main(String[] args) {
    var context = new AnnotationConfigApplicationContext(ConfigBasic.class);

    // Репозитории
    var customerRepository = context.getBean(CustomerRepository.class);
    var productRepository = context.getBean(ProductRepository.class);
    var orderService = context.getBean(OrderService.class);
    var orderRepository = context.getBean(OrderRepository.class);
    var categoryRepository = context.getBean(CategoryRepository.class);
    var orderDetailRepository = context.getBean(OrderDetailRepository.class);

    Optional<Customer> customerOpt = customerRepository.findById(1); // ID клиента
    if (customerOpt.isEmpty()) {
      logger.error("Klient ne nayden!");
      context.close();
      return;
    }

    Customer customer = customerOpt.get();

    // Получим товары
    List<Product> products = productRepository.findAll();
    if (products.isEmpty()) {
      logger.error("Spisok tovarov pust!");
      context.close();
      return;
    }

    // Формируем OrderDetails
    List<OrderDetail> orderDetails = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (int i = 0; i < Math.min(2, products.size()); i++) {
      Product product = products.get(i);
      int quantity = 1;
      BigDecimal unitPrice = product.getPrice();

      OrderDetail detail = new OrderDetail();
      detail.setProduct(product);
      detail.setQuantity(quantity);
      detail.setPrice(unitPrice);
      orderDetails.add(detail);

      total = total.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));
    }

    // Создаем заказ
    Order order = new Order();
    order.setCustomer(customer);
    order.setOrderDate(new Date());
    order.setStatus("Создан");
    order.setShippingAddress(customer.getAddress());
    order.setTotalPrice(total);
    order.setOrderDetails(orderDetails);

    // Устанавливаем обратную связь
    for (OrderDetail detail : orderDetails) {
      detail.setOrder(order);
    }

    // Сохраняем заказ через сервис
    Order savedOrder = orderService.createOrder(order);

    logger.info("\n=== CATEGORY ===");
    categoryRepository.findAll().forEach(
        cat -> logger.info("ID={}, name={}, desc={}", cat.getCategoryId(), cat.getName(), cat.getDescription()));

    logger.info("\n=== CUSTOMERS ===");
    customerRepository.findAll().forEach(c -> logger.info("ID={}, name={}, email={}, phone={}, address={}",
        c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()));

    logger.info("\n=== PRODUCTS ===");
    productRepository.findAll().forEach(p -> logger.info("ID={}, name={}, price={}, stock={}", p.getProductId(),
        p.getName(), p.getPrice(), p.getStockQuantity()));

    logger.info("\n=== ORDERS ===");
    orderRepository.findAll().forEach(o -> logger.info("OrderID={}, date={}, total={}, customer={}", o.getOrderId(),
        o.getOrderDate(), o.getTotalPrice(), o.getCustomer().getName()));

    logger.info("\n=== ORDER DETAILS ===");
    orderDetailRepository.findAll().forEach(d -> logger.info("OrderID={}, Product={}, qty={}, price={}",
        d.getOrder().getOrderId(), d.getProduct().getName(), d.getQuantity(), d.getPrice()));

    // Проверка и логирование
    logger.info("Order created!!!: ID={}, for {}, client: {}",
        savedOrder.getOrderId(),
        savedOrder.getTotalPrice(),
        savedOrder.getCustomer().getName());

    // Проверка, что заказ действительно в БД
    Optional<Order> check = orderRepository.findById(savedOrder.getOrderId());
    check.ifPresentOrElse(
        o -> logger.info("Order confirmed in database: ID = {}", o.getOrderId()),
        () -> logger.error("ERROR! Order not found in base."));

    context.close();
  }
}
