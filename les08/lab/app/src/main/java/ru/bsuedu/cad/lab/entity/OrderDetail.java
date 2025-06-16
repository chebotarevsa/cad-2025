package ru.bsuedu.cad.lab.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
  // @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Id
  private Integer orderDetailId;

  @ManyToOne
  @JoinColumn(name = "orderId")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "productId")
  private Product product;

  private Integer quantity;
  private BigDecimal price;

  public OrderDetail() {

  }

  public OrderDetail(Integer orderDetailId, Order order, Product product, Integer quantity, BigDecimal price) {
    this.orderDetailId = orderDetailId;
    this.order = order;
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

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
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
