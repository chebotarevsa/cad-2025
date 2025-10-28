package ru.bsuedu.cad.lab.mapper;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.dto.OrderDetailDto;
import ru.bsuedu.cad.lab.entity.OrderDetail;

@Component
public class OrderDetailMapper {

  private ProductMapper productMapper;

  public OrderDetailMapper(ProductMapper productMapper) {
    this.productMapper = productMapper;
  }

  public OrderDetailDto toDto(OrderDetail order) {
    if (order == null) {
      return null;
    }
    return new OrderDetailDto(
        order.getOrderDetailId(),
        productMapper.toDto(order.getProduct()),
        order.getQuantity(),
        order.getPrice());
  }
}
