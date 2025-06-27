package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

public class OrderItemDTO {

    private int orderDetailId;

    private int quantity;

    private BigDecimal price;

    private ProductDTO product;

    public OrderItemDTO(int orderDetailId, int quantity, BigDecimal price, ProductDTO product) {
        this.orderDetailId = orderDetailId;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
