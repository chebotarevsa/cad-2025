package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category"})
public class Product {

    @Id
    @Column(name = "product_id")
    private Integer productId;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}