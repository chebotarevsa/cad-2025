package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @Column(name = "order_detail_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order; 

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; 

    private int quantity;

    private BigDecimal price;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
    
}
