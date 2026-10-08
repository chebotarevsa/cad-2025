package ru.bsuedu.cad.lab.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.bsuedu.cad.lab.entity.CustomerOrder;
import ru.bsuedu.cad.lab.entity.OrderDetail;

public class OrderResponse {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Integer orderId;
    private Integer customerId;
    private String customerName;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;
    private String shippingAddress;
    private List<OrderItemResponse> items = new ArrayList<>();

    public static OrderResponse from(CustomerOrder order) {
        OrderResponse response = new OrderResponse();
        response.orderId = order.getOrderId();
        response.customerId = order.getCustomer().getCustomerId();
        response.customerName = order.getCustomer().getName();
        response.orderDate = order.getOrderDate().format(FORMATTER);
        response.totalPrice = order.getTotalPrice();
        response.status = order.getStatus();
        response.shippingAddress = order.getShippingAddress();
        for (OrderDetail detail : order.getDetails()) {
            OrderItemResponse item = new OrderItemResponse();
            item.setProductId(detail.getProduct().getProductId());
            item.setProductName(detail.getProduct().getName());
            item.setQuantity(detail.getQuantity());
            item.setPrice(detail.getPrice());
            response.items.add(item);
        }
        return response;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public static class OrderItemResponse {
        private Integer productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;

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
