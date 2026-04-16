package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @Test
    void createOrder() {
        OrderService service = new  OrderService(orderRepository);
        Mockito.when(orderRepository.save(Mockito.eq(null))).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> service.createOrder(null));
    }

    @Test
    void createOrderSuccessful() {
        OrderService service = new  OrderService(orderRepository);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(Mockito.any(Order.class));
        service.createOrder(new Order());
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void findAll() {
        OrderService service = new OrderService(orderRepository);
        Mockito.when(orderRepository.findAll()).thenReturn(List.of());
        List<Order> orders = service.findAll();
        Assertions.assertEquals(0, orders.size());
    }
}