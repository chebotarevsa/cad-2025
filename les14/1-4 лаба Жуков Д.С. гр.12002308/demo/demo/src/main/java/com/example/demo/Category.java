package com.example.demo;

import java.util.concurrent.atomic.AtomicLong;

public class Category {
    private static final AtomicLong idCounter = new AtomicLong(1);

    private Long id;
    private String name;
    private String color;

    public Category() {
        this.id = idCounter.getAndIncrement();
    }

    public Category(String name, String color) {
        this();
        this.name = name;
        this.color = color;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}