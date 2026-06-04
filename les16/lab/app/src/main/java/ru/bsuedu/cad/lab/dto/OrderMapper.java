package ru.bsuedu.cad.lab.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.entity.ShopOrder;

@Component
public class OrderMapper {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderDto toDto(ShopOrder order) {
        List<OrderDetailDto> details = order.getOrderDetails().stream()
                .map(this::toDetailDto)
                .toList();
        return new OrderDto(
                order.getOrderId(),
                order.getCustomer().getCustomerId(),
                order.getCustomer().getName(),
                order.getOrderDate().format(DATE_TIME_FORMAT),
                order.getTotalPrice(),
                order.getStatus(),
                order.getShippingAddress(),
                details);
    }

    private OrderDetailDto toDetailDto(OrderDetail detail) {
        return new OrderDetailDto(
                detail.getProduct().getProductId(),
                detail.getProduct().getName(),
                detail.getQuantity(),
                detail.getPrice());
    }
}
