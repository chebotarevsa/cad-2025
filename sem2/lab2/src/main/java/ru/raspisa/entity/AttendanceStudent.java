package ru.raspisa.entity;

import jakarta.persistence.*;

@Entity
public class AttendanceStudent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false) public Long groupId;
    @Column(nullable = false, length = 150) public String name;
    public boolean archived;
}
