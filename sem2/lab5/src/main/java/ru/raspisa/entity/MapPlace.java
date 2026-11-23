package ru.raspisa.entity;

import jakarta.persistence.*;

@Entity
public class MapPlace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, length = 120)
    public String title;
    @Column(nullable = false, length = 3000)
    public String description = "";
    public double latitude;
    public double longitude;
    public String audioName;
    public String audioType;
    public Long audioId;
    public Long imageId;
    public String imageName;
    public String imageType;
    @Column(length = 24)
    public String icon = "pin";
}
