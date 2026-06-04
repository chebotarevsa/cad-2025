package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        int orderId,
        int customerId,
        String customerName,
        String orderDate,
        BigDecimal totalPrice,
        String status,
        String shippingAddress,
        List<OrderDetailDto> details) {
}
