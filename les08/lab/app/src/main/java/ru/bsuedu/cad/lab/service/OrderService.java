package ru.bsuedu.cad.lab.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public Order createOrder(int customerId, int productId, int quantity) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Не найден клиент с ID: " + customerId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Не найден продукт с ID: " + productId));

		Order order = new Order();
		order.setCustomer(customer);
		order.setOrderDate(new Date());
		order.setStatus("NEW");
		order.setShippingAddress(customer.getAddress());

		OrderDetail detail = new OrderDetail();
		detail.setOrder(order);
		detail.setProduct(product);
		detail.setQuantity(quantity);
		detail.setPrice(product.getPrice());

		order.getOrderDetails().add(detail);
		order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

		Order savedOrder = orderRepository.save(order);
		logger.info("Создан заказ ID: {}", savedOrder.getOrderId());

		return savedOrder;
	}

	@Transactional
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
}
