package ru.bsuedu.cad.lab.controller;

import java.math.BigDecimal;

public class OrderResponse {
    private Integer orderId;
    private Integer customerId;
    private String customerName;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;
    private String shippingAddress;

    public OrderResponse() {}

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
