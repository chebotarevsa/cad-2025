package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private OrderService orderService;

	private Customer testCustomer;
	private Product testProduct;

	@BeforeEach
	void setUp() {
		testCustomer = new Customer(1, "Test Customer", "test@example.com", "123456789", "Test Address");
		testProduct = new Product(1, "Test Product", "Test Description", null,
				BigDecimal.valueOf(100.0), 10, "test.jpg", new Date(), new Date());
	}

	@Test
	void createOrder_Success() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
		when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0);
			order.setOrderId(1);
			return order;
		});

		Order result = orderService.createOrder(1, 1, 2);

		assertNotNull(result);
		assertEquals(1, result.getOrderId());
		assertEquals("NEW", result.getStatus());
		assertEquals(testCustomer.getAddress(), result.getShippingAddress());
		assertEquals(BigDecimal.valueOf(200.0), result.getTotalPrice());
		assertEquals(1, result.getOrderDetails().size());

		OrderDetail detail = result.getOrderDetails().get(0);
		assertEquals(2, detail.getQuantity());
		assertEquals(testProduct.getPrice(), detail.getPrice());
		assertEquals(testProduct, detail.getProduct());

		verify(customerRepository).findById(1);
		verify(productRepository).findById(1);
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	void createOrder_CustomerNotFound() {
		when(customerRepository.findById(1)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> orderService.createOrder(1, 1, 1));
		assertEquals("Не найден клиент с ID: 1", exception.getMessage());

		verify(customerRepository).findById(1);
		verify(productRepository, never()).findById(anyInt());
		verify(orderRepository, never()).save(any());
	}

	@Test
	void createOrder_ProductNotFound() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
		when(productRepository.findById(1)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> orderService.createOrder(1, 1, 1));
		assertEquals("Не найден продукт с ID: 1", exception.getMessage());

		verify(customerRepository).findById(1);
		verify(productRepository).findById(1);
		verify(orderRepository, never()).save(any());
	}

	@Test
	void deleteOrder_Success() {
		when(orderRepository.existsById(1)).thenReturn(true);

		orderService.deleteOrder(1);

		verify(orderRepository).existsById(1);
		verify(orderRepository).deleteById(1);
	}

}