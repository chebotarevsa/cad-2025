package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerOrderRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final CustomerOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(CustomerOrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CustomerOrder findById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + id));
    }

    @Transactional
    public CustomerOrder createOrder(Integer customerId, Integer productId, Integer quantity,
                                     String status, String shippingAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + customerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше 0");
        }
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(status == null || status.isBlank() ? "NEW" : status);
        order.setShippingAddress(
                shippingAddress == null || shippingAddress.isBlank()
                        ? customer.getAddress()
                        : shippingAddress);
        order.setTotalPrice(linePrice);

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setPrice(product.getPrice());
        order.addDetail(detail);

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        CustomerOrder saved = orderRepository.save(order);
        LOGGER.info("Создан заказ #{} для клиента {}", saved.getOrderId(), customer.getName());
        return saved;
    }

    @Transactional
    public CustomerOrder updateOrder(Integer orderId, Integer customerId, Integer productId,
                                     Integer quantity, String status, String shippingAddress) {
        CustomerOrder order = findById(orderId);

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Клиент не найден: " + customerId));
            order.setCustomer(customer);
        }
        if (status != null && !status.isBlank()) {
            order.setStatus(status);
        }
        if (shippingAddress != null && !shippingAddress.isBlank()) {
            order.setShippingAddress(shippingAddress);
        }

        if (productId != null && quantity != null) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Количество должно быть больше 0");
            }
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));

            // возвращаю старый остаток
            if (!order.getDetails().isEmpty()) {
                OrderDetail old = order.getDetails().get(0);
                Product oldProduct = old.getProduct();
                oldProduct.setStockQuantity(oldProduct.getStockQuantity() + old.getQuantity());
                productRepository.save(oldProduct);
            }

            if (product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException("Недостаточно товара на складе");
            }

            order.getDetails().clear();
            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setPrice(product.getPrice());
            order.addDetail(detail);

            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
            order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        CustomerOrder saved = orderRepository.save(order);
        LOGGER.info("Обновлён заказ #{}", saved.getOrderId());
        return saved;
    }

    @Transactional
    public void deleteOrder(Integer orderId) {
        CustomerOrder order = findById(orderId);
        for (OrderDetail detail : order.getDetails()) {
            Product product = detail.getProduct();
            product.setStockQuantity(product.getStockQuantity() + detail.getQuantity());
            productRepository.save(product);
        }
        orderRepository.delete(order);
        LOGGER.info("Удалён заказ #{}", orderId);
    }
}
