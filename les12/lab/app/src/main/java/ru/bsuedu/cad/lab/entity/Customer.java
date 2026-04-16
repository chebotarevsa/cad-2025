package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orders")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    private String name;

    private String email;

    private String phone;

    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}