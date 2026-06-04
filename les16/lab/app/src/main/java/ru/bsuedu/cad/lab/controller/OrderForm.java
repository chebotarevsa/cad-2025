package ru.bsuedu.cad.lab.controller;

import java.util.ArrayList;
import java.util.List;

public class OrderForm {
    private Integer customerId;
    private List<Long> productIds = new ArrayList<>();
    private String shippingAddress;

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
