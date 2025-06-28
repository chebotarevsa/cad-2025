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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private Customer testCustomer;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(testProduct);
        orderDetail.setQuantity(2);
        orderDetail.setPrice(testProduct.getPrice());

        testOrder = new Order();
        testOrder.setCustomer(testCustomer);
        testOrder.setOrderDetails(new ArrayList<>(List.of(orderDetail))); // Используем изменяемый список
    }

    @Test
    void createOrder_shouldCalculateTotalPrice() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order createdOrder = orderService.createOrder(testOrder);

        assertThat(createdOrder.getTotalPrice()).isEqualTo(BigDecimal.valueOf(200));
        verify(orderRepository).save(testOrder);
    }

    @Test
    void getOrderById_shouldReturnOrder() {
        when(orderRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testOrder));

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getCustomer().getName()).isEqualTo("Test Customer");
    }

    @Test
    void updateOrder_shouldRecalculateTotalPrice() {
        OrderDetail newDetail = new OrderDetail();
        newDetail.setProduct(testProduct);
        newDetail.setQuantity(3);
        newDetail.setPrice(testProduct.getPrice());

        // Получаем текущий список и добавляем новый элемент
        List<OrderDetail> details = new ArrayList<>(testOrder.getOrderDetails());
        details.add(newDetail);
        testOrder.getOrderDetails().clear();
        testOrder.getOrderDetails().addAll(details);

        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order updatedOrder = orderService.updateOrder(testOrder);

        assertThat(updatedOrder.getTotalPrice()).isEqualTo(BigDecimal.valueOf(500));
        verify(orderRepository).save(testOrder);
    }

    @Test
    void deleteOrder_shouldCallRepository() {
        orderService.deleteOrder(1L);
        verify(orderRepository).deleteById(1L);
    }
}