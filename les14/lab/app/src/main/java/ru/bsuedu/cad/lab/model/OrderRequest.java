package ru.bsuedu.cad.lab.model;

import ru.bsuedu.cad.lab.service.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private Integer customerId;
    private String shippingAddress;
    private String status;
    private List<OrderItem> items = new ArrayList<>();

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
