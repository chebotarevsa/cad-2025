package ru.bsuedu.cad.lab.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.bsuedu.cad.lab.dto.OrderMapper;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.ShopOrder;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.repository.ShopOrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ShopOrderRepository shopOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setName("Алексей Иванов");

        product = new Product();
        product.setProductId(1L);
        product.setName("Сухой корм");
        product.setPrice(new BigDecimal("1500.00"));
    }

    @Test
    void createOrder_success() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shopOrderRepository.create(any(ShopOrder.class))).thenAnswer(invocation -> {
            ShopOrder order = invocation.getArgument(0);
            order.setOrderId(10);
            return order;
        });

        ShopOrder result = orderService.createOrder(
                1, "Москва, ул. Ленина, 10", List.of(new OrderLineRequest(1L, 2)));

        assertThat(result.getOrderId()).isEqualTo(10);
        assertThat(result.getStatus()).isEqualTo("NEW");
        assertThat(result.getTotalPrice()).isEqualByComparingTo(new BigDecimal("3000.00"));
        assertThat(result.getOrderDetails()).hasSize(1);
        verify(shopOrderRepository).create(any(ShopOrder.class));
    }

    @Test
    void createOrder_customerNotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(
                        99, "Адрес", List.of(new OrderLineRequest(1L, 1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer not found");

        verify(shopOrderRepository, never()).create(any());
    }

    @Test
    void createOrder_productNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(
                        1, "Адрес", List.of(new OrderLineRequest(99L, 1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found");

        verify(shopOrderRepository, never()).create(any());
    }
}
