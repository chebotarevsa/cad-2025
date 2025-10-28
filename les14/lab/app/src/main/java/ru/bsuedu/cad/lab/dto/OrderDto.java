package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderDto {
  private Integer orderId;
  private CustomerDto customer;
  private Date orderDate;
  private BigDecimal totalPrice;
  private String status;
  private String shippingAddress;
  private List<OrderDetailDto> orderDetails;

  public OrderDto() {
  }

  public OrderDto(Integer orderId, CustomerDto customer, Date orderDate, BigDecimal totalPrice, String status,
      String shippingAddress, List<OrderDetailDto> orderDetails) {
    this.orderId = orderId;
    this.customer = customer;
    this.orderDate = orderDate;
    this.totalPrice = totalPrice;
    this.status = status;
    this.shippingAddress = shippingAddress;
    this.orderDetails = orderDetails;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public CustomerDto getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerDto customer) {
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

  public List<OrderDetailDto> getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(List<OrderDetailDto> orderDetails) {
    this.orderDetails = orderDetails;
  }
}
