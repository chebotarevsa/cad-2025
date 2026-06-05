package ru.bsuedu.cad.lab.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.OrderEntity;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.web.dto.OrderRequest;
import ru.bsuedu.cad.lab.web.dto.OrderUpdateRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderRestControllerTest {
    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderRestController controller;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        orderRepository = mock(OrderRepository.class);
        controller = new OrderRestController(orderService, orderRepository);
    }

    @Test
    void getOrder_shouldReturnOrderResponse() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Client");

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Widget");
        product.setPrice(BigDecimal.valueOf(25));

        OrderDetail detail = new OrderDetail();
        detail.setProduct(product);
        detail.setQuantity(2);
        detail.setPrice(BigDecimal.valueOf(25));

        OrderEntity order = new OrderEntity();
        order.setOrderId(10L);
        order.setCustomer(customer);
        order.setStatus("NEW");
        order.setTotalPrice(BigDecimal.valueOf(50));
        order.setOrderDetails(List.of(detail));

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        var response = controller.getOrder(10L);

        assertEquals(10L, response.getOrderId());
        assertEquals("Client", response.getCustomerName());
        assertEquals(1, response.getItems().size());
        assertEquals("Widget", response.getItems().get(0).getProductName());
    }

    @Test
    void createOrder_shouldReturnCreatedOrderResponse() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setProductQuantities(Map.of(2L, 1));
        request.setShippingAddress("Address");

        OrderEntity order = new OrderEntity();
        order.setOrderId(11L);
        order.setStatus("NEW");
        order.setTotalPrice(BigDecimal.valueOf(100));

        when(orderService.createOrder(anyLong(), anyMap(), anyString())).thenReturn(order);

        var response = controller.createOrder(request);

        assertEquals(11L, response.getOrderId());
        assertEquals("NEW", response.getStatus());
    }

    @Test
    void updateOrder_shouldForwardToService() {
        OrderUpdateRequest request = new OrderUpdateRequest();
        request.setStatus("SHIPPED");
        request.setShippingAddress("New Addr");

        OrderEntity order = new OrderEntity();
        order.setOrderId(12L);
        order.setStatus("SHIPPED");
        order.setShippingAddress("New Addr");

        when(orderService.updateOrder(12L, "SHIPPED", "New Addr")).thenReturn(order);

        var response = controller.updateOrder(12L, request);

        assertEquals("SHIPPED", response.getStatus());
        assertEquals("New Addr", response.getShippingAddress());
    }
}
