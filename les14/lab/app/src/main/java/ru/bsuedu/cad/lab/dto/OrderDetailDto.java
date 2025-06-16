package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;

public class OrderDetailDto {
  private Integer orderDetailId;
  private ProductDto product;
  private Integer quantity;
  private BigDecimal price;

  public OrderDetailDto() {
  }

  public OrderDetailDto(Integer orderDetailId, ProductDto product, Integer quantity, BigDecimal price) {
    this.orderDetailId = orderDetailId;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }

  public Integer getOrderDetailId() {
    return orderDetailId;
  }

  public void setOrderDetailId(Integer orderDetailId) {
    this.orderDetailId = orderDetailId;
  }

  public ProductDto getProduct() {
    return product;
  }

  public void setProduct(ProductDto product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
