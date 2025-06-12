package ru.bsuedu.cad.lab.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
	private final CustomerMapper customerMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Autowired
	public OrderMapper(CustomerMapper customerMapper, OrderDetailMapper orderDetailMapper) {
		this.customerMapper = customerMapper;
		this.orderDetailMapper = orderDetailMapper;
	}

	// public static OrderDto toDto(Order order) {
	// OrderMapper mapper = new OrderMapper(new CustomerMapper(), new
	// OrderDetailMapper(new ProductMapper()));
	// return mapper.mapToDto(order);
	// }

	public OrderDto mapToDto(Order order) {
		if (order == null) {
			return null;
		}
		return new OrderDto(
				order.getOrderId(),
				order.getOrderDate(),
				order.getTotalPrice(),
				order.getStatus(),
				order.getShippingAddress(),
				customerMapper.toDto(order.getCustomer()),
				order.getOrderDetails().stream()
						.map(orderDetailMapper::toDto)
						.collect(Collectors.toList()));
	}
}