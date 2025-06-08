
package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderDetailRepository = mock(OrderDetailRepository.class);
        orderService = new OrderService(orderRepository, orderDetailRepository);

        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> {
            CustomerOrder order = invocation.getArgument(0);
            order.setId(1); // установить ID вручную
            return order;
        });
    }

    @Test
    void testCreateOrder_success() {
        Customer customer = new Customer();
        customer.setName("Пётр");
        customer.setAddress("ул. Победы 1");

        Product product = new Product();
        product.setName("Корм");
        product.setPrice(new BigDecimal("150.00"));

        orderService.createOrder(customer, List.of(product));

        verify(orderRepository, times(1)).save(any());
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
    }
}
