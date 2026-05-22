package ru.bsuedu.cad.lab.model;

import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;
    private String shippingAddress;
    private List<OrderRow> items;

    public static OrderResponse fromOrder(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setOrderDate(order.getOrderDate().toString());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setItems(order.getDetails().stream().map(OrderRow::fromDetail).toList());
        return response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
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

    public List<OrderRow> getItems() {
        return items;
    }

    public void setItems(List<OrderRow> items) {
        this.items = items;
    }

    public static class OrderRow {
        private Integer productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;

        public static OrderRow fromDetail(OrderDetail detail) {
            OrderRow row = new OrderRow();
            row.setProductId(detail.getProduct().getId());
            row.setProductName(detail.getProduct().getName());
            row.setQuantity(detail.getQuantity());
            row.setPrice(detail.getPrice());
            return row;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
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
}
