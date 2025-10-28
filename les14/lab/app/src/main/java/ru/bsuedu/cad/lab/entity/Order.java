package ru.bsuedu.cad.lab.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orderId;

  @ManyToOne
  @JoinColumn(name = "customerId")
  private Customer customer;

  private Date orderDate;
  private BigDecimal totalPrice;
  private String status;
  private String shippingAddress;

  @OneToMany(mappedBy = "order")
  private List<OrderDetail> orderDetails = new ArrayList<>();

  public Order() {

  }

  public Order(Integer orderId, Customer customer, Date orderDate, BigDecimal totalPrice, String status,
      String shippingAddress,
      List<OrderDetail> orderDetails) {
    this.orderId = orderId;
    this.orderDate = orderDate;
    this.totalPrice = totalPrice;
    this.status = status;
    this.shippingAddress = shippingAddress;
    this.orderDetails = orderDetails;
    this.customer = customer;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setAddress(Integer orderId) {
    this.orderId = orderId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public List<OrderDetail> getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(List<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }
}
