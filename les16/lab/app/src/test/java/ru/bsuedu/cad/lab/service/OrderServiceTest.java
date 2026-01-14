package ru.bsuedu.cad.lab.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrders_ShouldReturnOrdersList(){
        Customer customer = new Customer(1, "John", "john@gmail.com", "41-14-63", "Belgorod");
        Order order = new Order(new Date(2024, 11,8), new BigDecimal(1200), "new order", "Belgorod",
            customer);
        when(orderRepository.findAll()).thenReturn(List.of(order));
        var orders = orderService.getOrders();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getCustomer().getName()).isEqualTo("John");

        verify(orderRepository).findAll();
    }
}
