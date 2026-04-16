package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"customer", "orderDetails"})
public class Order {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @OneToMany
    private List<OrderDetail> orderDetails;
}