package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer testCustomer;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFullName("Иван Петров");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Тестовый товар");
        testProduct.setPrice(100.0);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomer(testCustomer);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus("NEW");
    }

    @Test
    void createOrder_Success() {
        // given
        List<OrderService.OrderItemData> items = List.of(
            new OrderService.OrderItemData(1L, 2)
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        // when
        Order result = orderService.createOrder(1L, items);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Иван Петров", result.getCustomer().getFullName());
        assertEquals("NEW", result.getStatus());

        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void createOrder_CustomerNotFound_ThrowsException() {
        // given
        List<OrderService.OrderItemData> items = List.of(
            new OrderService.OrderItemData(1L, 2)
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> orderService.createOrder(1L, items));

        assertEquals("Customer not found with id: 1", exception.getMessage());

        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_ProductNotFound_ThrowsException() {
        // given
        List<OrderService.OrderItemData> items = List.of(
            new OrderService.OrderItemData(1L, 2)
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> orderService.createOrder(1L, items));

        assertEquals("Product not found with id: 1", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getAllOrders_Success() {
        // given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<Order> result = orderService.getAllOrders();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void deleteOrder_Success() {
        // given
        doNothing().when(orderRepository).deleteById(1L);
        when(orderRepository.existsById(1L)).thenReturn(true);

        // when
        orderService.deleteOrder(1L);

        // then
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        // given
        when(orderRepository.existsById(1L)).thenReturn(false);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> orderService.deleteOrder(1L));

        assertEquals("Order not found with id: 1", exception.getMessage());
        verify(orderRepository, never()).deleteById(anyLong());
    }
}