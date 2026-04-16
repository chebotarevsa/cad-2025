package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
public class OrderDetail {

    @Id
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private BigDecimal price;
}