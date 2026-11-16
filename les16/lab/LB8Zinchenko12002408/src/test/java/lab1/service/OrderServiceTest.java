package lab1.service;

import lab1.entity.Customer;
import lab1.entity.Order;
import lab1.entity.Product;
import lab1.repository.CustomerRepository;
import lab1.repository.OrderRepository;
import lab1.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Включаем Mockito
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        // Подготавливаем тестовые данные
        customer = new Customer("Иван", "ivan@mail.ru", "123", "Москва");
        customer.setCustomerId(1L);

        product = new Product("Корм", "Вкусный", new BigDecimal("100.00"), 10);
        product.setProductId(1L);
    }

    @Test
    void createOrder_Success() {
        // 1. Настраиваем поведение моков (успешный сценарий)
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(2);

        // 2. Вызываем метод
        Order result = orderService.createOrder(1L, List.of(item), "Адрес доставки");

        // 3. Проверяем результат
        assertNotNull(result);
        assertEquals("NEW", result.getStatus());
        assertEquals(new BigDecimal("200.00"), result.getTotalPrice()); // 100 * 2
        assertEquals(8, product.getStockQuantity()); // Было 10, списали 2

        // Проверяем, что методы репозиториев вызывались
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void createOrder_CustomerNotFound_ThrowsException() {
        // 1. Настраиваем мок (клиент не найден)
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(1);

        // 2. Проверяем, что выбросится исключение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(99L, List.of(item), "Адрес");
        });

        assertEquals("Клиент не найден", exception.getMessage());
        // Убеждаемся, что заказ не сохранялся
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_NotEnoughStock_ThrowsException() {
        // 1. Настраиваем мок (товара меньше, чем просят)
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        product.setStockQuantity(1); // На складе всего 1
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderService.OrderItemRequest item = new OrderService.OrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(5); // Просим 5

        // 2. Проверяем ошибку
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, List.of(item), "Адрес");
        });

        assertEquals("Недостаточно товара на складе", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }
}