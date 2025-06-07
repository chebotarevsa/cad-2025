package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;


public class OrderItemDto {
    @JsonProperty("orderItem")
    private Long orderDetailId;
    private int quantity;
    private BigDecimal price;
    private ProductDto productDto;


    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

}
