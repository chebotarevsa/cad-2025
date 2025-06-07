package ru.bsuedu.cad.lab.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    private int quantity;
    private BigDecimal price;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;


    public OrderDetail(){
        
    }

    public OrderDetail(int quantity, BigDecimal price, Order order, Product product) {       
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
    }
    
    public Long getOrderDetailId() {
        return orderDetailId;
    }
    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    
    
}
