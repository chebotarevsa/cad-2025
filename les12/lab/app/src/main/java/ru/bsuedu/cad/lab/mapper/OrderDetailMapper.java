package ru.bsuedu.cad.lab.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.dto.OrderDetailDto;
import ru.bsuedu.cad.lab.entity.OrderDetail;

@Component
public class OrderDetailMapper {
	private final ProductMapper productMapper;

	@Autowired
	public OrderDetailMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public OrderDetailDto toDto(OrderDetail orderDetail) {
		if (orderDetail == null) {
			return null;
		}
		return new OrderDetailDto(
				orderDetail.getOrderDetailId(),
				orderDetail.getQuantity(),
				orderDetail.getPrice(),
				productMapper.toDto(orderDetail.getProduct()));
	}
}