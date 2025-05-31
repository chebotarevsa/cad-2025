package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    
    private int orderId;

    private Date orderDate;

    private BigDecimal totalPrice;

    private String status;

    private String shippingAddress;

    private CustomerDTO customer;

    private List<OrderItemDTO> orderDetails = new ArrayList<>();

    public OrderDTO(int orderId, Date orderDate, BigDecimal totalPrice, String status, String shippingAddress,
            CustomerDTO customer, List<OrderItemDTO> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.customer = customer;
        this.orderDetails = orderDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<OrderItemDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderItemDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getCustomerName(){
        return customer.getName();
    }
}
