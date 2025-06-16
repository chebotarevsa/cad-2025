package ru.bsuedu.cad.lab.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.dto.OrderDto;
import ru.bsuedu.cad.lab.entity.Order;

@Component
public class OrderMapper {

  private CustomerMapper customerMapper;
  private OrderDetailMapper orderDetailMapper;

  @Autowired
  public OrderMapper(CustomerMapper customerMapper, OrderDetailMapper orderDetailMapper) {
    this.customerMapper = customerMapper;
    this.orderDetailMapper = orderDetailMapper;
  }

  public static OrderDto toDto(Order order) {
    OrderMapper mapper = new OrderMapper(new CustomerMapper(), new OrderDetailMapper(new ProductMapper()));
    return mapper.mapToDto(order);
  }

  public OrderDto mapToDto(Order order) {
    if (order == null) {
      return null;
    }

    return new OrderDto(
        order.getOrderId(),
        customerMapper.toDto(order.getCustomer()),
        order.getOrderDate(),
        order.getTotalPrice(),
        order.getStatus(),
        order.getShippingAddress(),
        order.getOrderDetails().stream()
            .map(orderDetailMapper::toDto)
            .collect(Collectors.toList()));

  }
}
