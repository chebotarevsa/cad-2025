package ru.bsuedu.cad.lab.dto;

import java.util.List;

import ru.bsuedu.cad.lab.service.OrderLineRequest;

public record CreateOrderRequest(
        int customerId,
        String shippingAddress,
        List<OrderLineRequest> lines) {
}
