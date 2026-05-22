package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderCreatesNewOrder() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Иван");

        Product food = new Product();
        food.setId(10);
        food.setName("Корм");
        food.setPrice(new BigDecimal("150.00"));

        Product toy = new Product();
        toy.setId(20);
        toy.setName("Игрушка");
        toy.setPrice(new BigDecimal("90.00"));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10)).thenReturn(Optional.of(food));
        when(productRepository.findById(20)).thenReturn(Optional.of(toy));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(
                1,
                List.of(new OrderItem(10, 2), new OrderItem(20, 1)),
                "Белгород, ул. Победы, 1"
        );

        assertEquals(customer, order.getCustomer());
        assertEquals("NEW", order.getStatus());
        assertEquals("Белгород, ул. Победы, 1", order.getShippingAddress());
        assertEquals(0, new BigDecimal("390.00").compareTo(order.getTotalPrice()));
        assertEquals(2, order.getDetails().size());
        assertNotNull(order.getOrderDate());
        verify(orderRepository).save(order);
    }

    @Test
    void createOrderThrowsExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.createOrder(99, List.of(new OrderItem(10, 1)), "Адрес")
        );

        assertEquals("Customer not found: 99", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrderThrowsExceptionWhenProductNotFound() {
        Customer customer = new Customer();
        customer.setId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(productRepository.findById(404)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1, List.of(new OrderItem(404, 1)), "Адрес")
        );

        assertEquals("Product not found: 404", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
