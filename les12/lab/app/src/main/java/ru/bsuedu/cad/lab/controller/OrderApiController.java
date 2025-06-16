package ru.bsuedu.cad.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.dto.UpdateOrderDto;
import ru.bsuedu.cad.lab.mapper.OrderMapper;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.entity.Order;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

	private final OrderService orderService;
	private final OrderMapper orderMapper;

	@Autowired
	public OrderApiController(OrderService orderService, OrderMapper orderMapper) {
		this.orderService = orderService;
		this.orderMapper = orderMapper;
	}

	@GetMapping
	public List<OrderDto> getOrders() {
		return orderService.getAllOrders().stream()
				.map(orderMapper::mapToDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") int id) {
		return orderService.getAllOrders().stream()
				.filter(o -> o.getOrderId() == id)
				.findFirst()
				.map(order -> ResponseEntity.ok(orderMapper.mapToDto(order)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@RequestParam(name = "customerId") int customerId,
			@RequestParam(name = "productId") int productId,
			@RequestParam(name = "quantity") int quantity) {
		try {
			Order created = orderService.createOrder(customerId, productId, quantity);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(orderMapper.mapToDto(created));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable("id") int id) {
		try {
			orderService.deleteOrder(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") int id,
			@RequestBody UpdateOrderDto dto) {
		try {
			Order updated = orderService.updateOrder(id, dto);
			return ResponseEntity.ok(orderMapper.mapToDto(updated));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}