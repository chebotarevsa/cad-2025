package com.example.demo;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Comment {
    private static final AtomicLong idCounter = new AtomicLong(1);

    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
    private Long orderId;

    public Comment() {
        this.id = idCounter.getAndIncrement();
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String text, String author, Long orderId) {
        this();
        this.text = text;
        this.author = author;
        this.orderId = orderId;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}