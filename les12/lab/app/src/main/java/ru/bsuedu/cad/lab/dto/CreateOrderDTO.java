package ru.bsuedu.cad.lab.dto;

import java.util.List;

public class CreateOrderDTO {
    
    private int orderId;
    private int customerId;
    private List<CreateOrderItemDTO> items;
    private String address;
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public List<CreateOrderItemDTO> getItems() {
        return items;
    }
    public void setItems(List<CreateOrderItemDTO> items) {
        this.items = items;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
}
