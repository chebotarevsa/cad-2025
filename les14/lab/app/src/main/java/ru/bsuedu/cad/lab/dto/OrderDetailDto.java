package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

public record OrderDetailDto(
        long productId,
        String productName,
        int quantity,
        BigDecimal price) {
}
