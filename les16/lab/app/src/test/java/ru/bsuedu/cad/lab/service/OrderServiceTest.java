package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "Иван Иванов", "ivan@test.com", "+79001234567", "Москва");

        Category category = new Category(1, "Корма", "Корма для животных");

        product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Сухой корм");
        product1.setCategory(category);
        product1.setPrice(new BigDecimal("1500.00"));
        product1.setStockQuantity(50);

        product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Игрушка");
        product2.setCategory(category);
        product2.setPrice(new BigDecimal("300.00"));
        product2.setStockQuantity(100);
    }

    // ─── createOrder ─────────────────────────────────────────────────────────

    @Test
    void createOrder_withProducts_shouldCalculateTotalAndSave() {
        // Given
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        Order result = orderService.createOrder(customer,
                List.of(product1, product2), "Москва, ул. Ленина, д. 1");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("NEW");
        assertThat(result.getTotalPrice()).isEqualByComparingTo(new BigDecimal("1800.00"));
        assertThat(result.getOrderDetails()).hasSize(2);
        assertThat(result.getCustomer()).isEqualTo(customer);
        assertThat(result.getShippingAddress()).isEqualTo("Москва, ул. Ленина, д. 1");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_withEmptyProducts_shouldSetZeroTotal() {
        // Given
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        Order result = orderService.createOrder(customer, List.of(), "Адрес");

        // Then
        assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getOrderDetails()).isEmpty();
    }

    @Test
    void createOrder_shouldSetStatusNew() {
        // Given
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When
        Order result = orderService.createOrder(customer, List.of(product1), "Адрес");

        // Then
        assertThat(result.getStatus()).isEqualTo("NEW");
    }

    // ─── getAllOrders ─────────────────────────────────────────────────────────

    @Test
    void getAllOrders_shouldDelegateToRepository() {
        // Given
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(List.of(order));

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertThat(result).hasSize(1);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrders_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(orderRepository.findAll()).thenReturn(List.of());

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertThat(result).isEmpty();
    }

    // ─── findById ────────────────────────────────────────────────────────────

    @Test
    void findById_whenOrderExists_shouldReturnOrder() {
        // Given
        Order order = new Order();
        order.setOrderId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When
        Optional<Order> result = orderService.findById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo(1);
    }

    @Test
    void findById_whenOrderNotExists_shouldReturnEmpty() {
        // Given
        when(orderRepository.findById(99)).thenReturn(Optional.empty());

        // When
        Optional<Order> result = orderService.findById(99);

        // Then
        assertThat(result).isEmpty();
    }

    // ─── updateOrder ─────────────────────────────────────────────────────────

    @Test
    void updateOrder_whenOrderExists_shouldUpdateStatusAndAddress() {
        // Given
        Order order = new Order();
        order.setOrderId(1);
        order.setStatus("NEW");
        order.setShippingAddress("Старый адрес");
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Order result = orderService.updateOrder(1, "SHIPPED", "Новый адрес");

        // Then
        assertThat(result.getStatus()).isEqualTo("SHIPPED");
        assertThat(result.getShippingAddress()).isEqualTo("Новый адрес");
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrder_whenOrderNotFound_shouldThrowIllegalArgumentException() {
        // Given
        when(orderRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> orderService.updateOrder(99, "SHIPPED", "Адрес"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    // ─── deleteOrder ─────────────────────────────────────────────────────────

    @Test
    void deleteOrder_shouldCallRepositoryDeleteById() {
        // Given
        doNothing().when(orderRepository).deleteById(1);

        // When
        orderService.deleteOrder(1);

        // Then
        verify(orderRepository, times(1)).deleteById(1);
    }
}
