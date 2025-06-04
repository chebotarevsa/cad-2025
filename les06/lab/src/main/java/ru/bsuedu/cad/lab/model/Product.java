
package ru.bsuedu.cad.lab.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private int categoryId;
    private double price;
    private int stockQuantity;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;

    public Product(int productId, String name, String description, int categoryId,
                   double price, int stockQuantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = "2024-01-01";
        this.updatedAt = "2024-01-01";
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getImageUrl() { return imageUrl; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
