package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.bsuedu.cad.lab.conf.SpringConfig;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfig.class)
@WebAppConfiguration
@Transactional
public class OrderServiceIntegrationTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	private int customerId;
	private int productId;

	@BeforeEach
	void setup() {
		Customer customer = new Customer(0, "Integration Test", "email@test.com", "123", "Test Addr");
		customer = customerRepository.save(customer);
		customerId = customer.getCustomerId();

		Product product = new Product(0, "Test Product", "Description", null,
				BigDecimal.valueOf(150), 10, "image.jpg", new Date(), new Date());
		product = productRepository.save(product);
		productId = product.getProductId();
	}

	@Test
	void testCreateOrder_Success() {
		var order = orderService.createOrder(customerId, productId, 2);
		assertNotNull(order);
		assertEquals(customerId, order.getCustomer().getCustomerId());
		assertEquals(1, order.getOrderDetails().size());
		assertEquals(BigDecimal.valueOf(300), order.getTotalPrice());
	}

	@Test
	void testCreateOrder_InvalidCustomer() {
		Exception ex = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(999, productId, 1);
		});
		assertTrue(ex.getMessage().contains("Не найден клиент"));
	}

	@Test
	void testCreateOrder_InvalidProduct() {
		Exception ex = assertThrows(RuntimeException.class, () -> {
			orderService.createOrder(customerId, 999, 1);
		});
		assertTrue(ex.getMessage().contains("Не найден продукт"));
	}
}