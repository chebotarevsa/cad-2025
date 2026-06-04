package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.ShopOrderRepository;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final ShopOrderRepository shopOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            ShopOrderRepository shopOrderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.shopOrderRepository = Objects.requireNonNull(shopOrderRepository);
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    @Transactional
    public ShopOrder createOrder(
            int customerId, String shippingAddress, List<OrderLineRequest> lines) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        ShopOrder order = new ShopOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineRequest line : lines) {
            Product product = productRepository
                    .findById(line.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + line.productId()));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(line.quantity());
            detail.setPrice(product.getPrice());
            order.addOrderDetail(detail);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotalPrice(total);

        ShopOrder saved = shopOrderRepository.create(order);
        LOGGER.info(
                "Создан заказ id={}, клиент={}, сумма={}",
                saved.getOrderId(),
                customer.getName(),
                saved.getTotalPrice());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ShopOrder> findAllOrders() {
        return shopOrderRepository.findAll();
    }
}
