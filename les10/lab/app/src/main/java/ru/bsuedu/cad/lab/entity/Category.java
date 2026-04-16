package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "products")
public class Category {

    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}