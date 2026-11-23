package ru.raspisa.entity;

import jakarta.persistence.*;

@Entity
public class MapAudio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 20971520, nullable = false)
    public byte[] content;
}
