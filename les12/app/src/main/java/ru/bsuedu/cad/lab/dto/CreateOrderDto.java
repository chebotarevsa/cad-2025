package ru.bsuedu.cad.lab.dto;

import java.util.List;

public class CreateOrderDto {
    private Long customerId;
    private List<CreateOrderItemDto> items;
    private String address;
    
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public List<CreateOrderItemDto> getItems() {
        return items;
    }
    public void setItems(List<CreateOrderItemDto> items) {
        this.items = items;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
