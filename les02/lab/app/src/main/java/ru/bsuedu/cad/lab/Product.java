package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private int productID;
    private String name;
    private BigDecimal price;
    private String description;
    private int stock_quantity;
    private String imageURL;
    private Date createdAt;
    private Date updatedAt;

    
    public Product(int productID, String name, String description, int categoryID, BigDecimal price, int stock_quantity,
            String imageURL, Date createdAt, Date updatedAt) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.categoryID = categoryID;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int categoryID;
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString(){
        return name;
    }


}