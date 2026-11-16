package ru.bsuedu.cad.lab;

public class Product {
    private long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    public Product(long id, String name, String description,
                   double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
}