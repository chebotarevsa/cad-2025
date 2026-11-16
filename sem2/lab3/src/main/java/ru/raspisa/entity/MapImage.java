package ru.raspisa.entity;

import jakarta.persistence.*;

@Entity
public class MapImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(length = 8388608, nullable = false)
    public byte[] content;
}
