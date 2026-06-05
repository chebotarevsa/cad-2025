package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

class OrderServiceTest {
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(customerRepository, productRepository, orderRepository);
    }

    @Test
    void createOrder_shouldPersistOrderAndUpdateStock() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Test Customer");

        Product product = new Product();
        product.setProductId(10L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(50));
        product.setStockQuantity(5);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderEntity order = orderService.createOrder(1L, Map.of(10L, 2), "Test Address");

        assertNotNull(order);
        assertEquals("NEW", order.getStatus());
        assertEquals(BigDecimal.valueOf(100), order.getTotalPrice());
        assertEquals("Test Address", order.getShippingAddress());
        assertEquals(1, order.getOrderDetails().size());
        assertEquals(3, product.getStockQuantity());

        verify(productRepository).save(product);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrder_shouldChangeStatusAndAddress() {
        OrderEntity order = new OrderEntity();
        order.setOrderId(2L);
        order.setStatus("NEW");
        order.setShippingAddress("Old Address");
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.valueOf(20));

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderEntity updated = orderService.updateOrder(2L, "SHIPPED", "New Address");

        assertEquals("SHIPPED", updated.getStatus());
        assertEquals("New Address", updated.getShippingAddress());
        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrder_shouldCallRepositoryDelete() {
        orderService.deleteOrder(3L);
        verify(orderRepository).deleteById(3L);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.findById(4L));
    }
}
