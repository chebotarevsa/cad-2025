package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.dto.OrderMapper;
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
    private final OrderMapper orderMapper;

    public OrderService(
            ShopOrderRepository shopOrderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper) {
        this.shopOrderRepository = Objects.requireNonNull(shopOrderRepository);
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.productRepository = Objects.requireNonNull(productRepository);
        this.orderMapper = Objects.requireNonNull(orderMapper);
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
        return shopOrderRepository.findAllWithCustomer();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrderDtos() {
        return shopOrderRepository.findAllWithCustomer().stream().map(orderMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public OrderDto findOrderDtoById(int orderId) {
        return orderMapper.toDto(findOrderById(orderId));
    }

    @Transactional
    public OrderDto createOrderDto(
            int customerId, String shippingAddress, List<OrderLineRequest> lines) {
        return orderMapper.toDto(createOrder(customerId, shippingAddress, lines));
    }

    @Transactional
    public OrderDto updateOrderDto(int orderId, String status, String shippingAddress) {
        return orderMapper.toDto(updateOrder(orderId, status, shippingAddress));
    }

    @Transactional(readOnly = true)
    public ShopOrder findOrderById(int orderId) {
        return shopOrderRepository
                .findByIdWithDetails(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
    }

    @Transactional
    public ShopOrder updateOrder(int orderId, String status, String shippingAddress) {
        ShopOrder order = findOrderById(orderId);
        if (status != null && !status.isBlank()) {
            order.setStatus(status);
        }
        if (shippingAddress != null) {
            order.setShippingAddress(shippingAddress);
        }
        ShopOrder saved = shopOrderRepository.save(order);
        LOGGER.info("Обновлён заказ id={}, статус={}", saved.getOrderId(), saved.getStatus());
        return saved;
    }

    @Transactional
    public void deleteOrder(int orderId) {
        if (!shopOrderRepository.existsById(orderId)) {
            throw new NoSuchElementException("Order not found: " + orderId);
        }
        shopOrderRepository.deleteById(orderId);
        LOGGER.info("Удалён заказ id={}", orderId);
    }
}
