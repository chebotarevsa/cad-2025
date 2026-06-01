package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer testCustomer;
    private Product testProduct1;
    private Product testProduct2;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer("Ivan Petrov", "ivan@mail.ru", "+79991234567", "Lenina st, 1");
        testCustomer.setId(1L);

        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("Dog food");
        testProduct1.setPrice(new BigDecimal("1500.00"));
        testProduct1.setStockQuantity(100);

        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("Toy");
        testProduct2.setPrice(new BigDecimal("300.00"));
        testProduct2.setStockQuantity(50);

        testOrder = new Order(testCustomer, "Lenina st, 1");
        testOrder.setId(1L);
    }

    @Test
    void createOrder_Success() {
        Long customerId = 1L;
        String shippingAddress = "Lenina st, 1";
        List<OrderService.OrderItemRequest> items = Arrays.asList(
            new OrderService.OrderItemRequest(1L, 2),
            new OrderService.OrderItemRequest(2L, 1)
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(testProduct2));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderDetailRepository.save(any())).thenReturn(null);

        Order createdOrder = orderService.createOrder(customerId, items, shippingAddress);

        assertThat(createdOrder).isNotNull();
        verify(customerRepository, times(1)).findById(customerId);
        verify(productRepository, times(2)).findById(anyLong());
    }

    @Test
    void createOrder_CustomerNotFound() {
        Long customerId = 999L;
        String shippingAddress = "Lenina st, 1";
        List<OrderService.OrderItemRequest> items = Arrays.asList(
            new OrderService.OrderItemRequest(1L, 2)
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(customerId, items, shippingAddress))
            .isInstanceOf(RuntimeException.class);

        verify(customerRepository, times(1)).findById(customerId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_ProductNotFound() {
        Long customerId = 1L;
        String shippingAddress = "Lenina st, 1";
        List<OrderService.OrderItemRequest> items = Arrays.asList(
            new OrderService.OrderItemRequest(999L, 2)
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(customerId, items, shippingAddress))
            .isInstanceOf(RuntimeException.class);

        verify(customerRepository, times(1)).findById(customerId);
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void createOrder_InsufficientStock() {
        Long customerId = 1L;
        String shippingAddress = "Lenina st, 1";
        List<OrderService.OrderItemRequest> items = Arrays.asList(
            new OrderService.OrderItemRequest(1L, 200)
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct1));

        assertThatThrownBy(() -> orderService.createOrder(customerId, items, shippingAddress))
            .isInstanceOf(RuntimeException.class);

        verify(customerRepository, times(1)).findById(customerId);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getAllOrders_Success() {
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.getAllOrders();

        assertThat(actualOrders).hasSize(1);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_Success() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        Order foundOrder = orderService.getOrderById(orderId);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(orderId);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void deleteOrder_Success() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        doNothing().when(orderRepository).delete(any(Order.class));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(testOrder);
    }
}
